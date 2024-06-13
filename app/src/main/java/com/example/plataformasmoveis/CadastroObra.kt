package com.example.plataformasmoveis

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import java.io.ByteArrayOutputStream

class CadastroObra : AppCompatActivity() {

    private lateinit var imagem: ImageView
    private var base64Image: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cadastro_obra)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.btn_scan)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        imagem = findViewById<ImageView>(R.id.editar_imagem)

        var nomeObra = findViewById<EditText>(R.id.edt_nomeEdicao)
        var anoObra = findViewById<EditText>(R.id.edt_anoEdicao)
        var artistaObra = findViewById<EditText>(R.id.edt_artistaEdicao)
        var descricaoObra = findViewById<EditText>(R.id.edt_descricaoEdicao)

        var btn_confcadastro: Button = findViewById(R.id.btn_ConfirmarCadastro)

        imagem.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 1)
        }

        btn_confcadastro.setOnClickListener{
            val obraNova = mapOf(
                "nomeObra" to nomeObra.text.toString(),
                "anoObra" to anoObra.text.toString(),
                "artistaObra" to artistaObra.text.toString(),
                "descricaoObra" to descricaoObra.text.toString(),
                "imagemBase64" to base64Image
            )

            Firebase.firestore.collection("Obras").add(obraNova)
                .addOnSuccessListener { documentReference ->
                    val docId = documentReference.id
                    val qrCodeBitmap = generateQRCode(docId)
                    val qrCodeBase64 = convertBitmapToBase64(qrCodeBitmap)

                    Firebase.firestore.collection("Obras").document(docId)
                        .update("qrcodeObra", qrCodeBase64)
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri: Uri? = data.data
            try {
                imageUri?.let {
                    val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
                    imagem.setImageBitmap(bitmap)
                    base64Image = convertBitmapToBase64(bitmap)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun convertBitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun generateQRCode(content: String): Bitmap {
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 512, 512)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bmp.setPixel(x, y, if (bitMatrix.get(x, y)) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
            }
        }
        return bmp
    }
}
