package com.example.plataformasmoveis

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CadastroObra : AppCompatActivity() {

    private lateinit var imagem: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cadastro_obra)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        imagem = findViewById<ImageView>(R.id.cadastrar_imagem)

        var nomeObra = findViewById<EditText>(R.id.edt_nomeObra)
        var anoObra = findViewById<EditText>(R.id.edt_anoObra)
        var artistaObra = findViewById<EditText>(R.id.edt_artistaObra)
        var descricaoObra = findViewById<EditText>(R.id.edt_descricaoObra)

        var btn_confcadastro: Button = findViewById(R.id.btn_ConfirmarCadastro)


        imagem.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 1)
        }

        btn_confcadastro.setOnClickListener(){

            //converter base64 bitmap factory

            Firebase.firestore.collection("Obras").add(mapOf(
                "nomeObra" to nomeObra.text.toString(),
                "anoObra" to anoObra.text.toString(),
                "artistaObra" to artistaObra.text.toString(),
                "descricaoObra" to descricaoObra.text.toString()
            ))
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
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}