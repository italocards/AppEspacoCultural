package com.example.plataformasmoveis

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.zxing.integration.android.IntentIntegrator

class QRCodeDetalhado : AppCompatActivity() {

    private lateinit var btn_scan: Button
    private lateinit var tv_resultado: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_qrcode_detalhado)

        btn_scan = findViewById(R.id.btn_scan)
        tv_resultado = findViewById(R.id.tv_resultado)

        btn_scan.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                startQRCodeScanner()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
            }
        }
    }

    private fun startQRCodeScanner() {
        IntentIntegrator(this).apply {
            setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            setPrompt("Escaneie um QR Code")
            setCameraId(0)  // Use a câmera traseira
            setBeepEnabled(false)
            setBarcodeImageEnabled(true)
            initiateScan()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                startQRCodeScanner()
            } else {
                tv_resultado.text = "Permissão da câmera é necessária para escanear QR codes"
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                tv_resultado.text = "Cancelado"
            } else {
                val qrCodeContent = result.contents
                val docId = qrCodeContent.substringAfterLast("/obra/")
                fetchObraDetails(docId)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun fetchObraDetails(docId: String) {
        Firebase.firestore.collection("Obras").document(docId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val nome = document.getString("nomeObra")
                    val ano = document.getString("anoObra")
                    val artista = document.getString("artistaObra")
                    val descricao = document.getString("descricaoObra")
                    val imagemBase64 = document.getString("imagemBase64")

                    val intent = Intent(this, ObraInteira::class.java)
                    intent.putExtra("nome", nome)
                    intent.putExtra("ano", ano)
                    intent.putExtra("artista", artista)
                    intent.putExtra("descricao", descricao)
                    intent.putExtra("imagem", imagemBase64)
                    startActivity(intent)
                } else {
                    tv_resultado.text = "Documento não encontrado"
                }
            }
            .addOnFailureListener { exception ->
                tv_resultado.text = "Erro ao buscar documento: ${exception.message}"
            }
    }

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 101
    }
}
