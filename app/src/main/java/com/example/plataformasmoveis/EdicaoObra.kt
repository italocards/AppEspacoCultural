package com.example.plataformasmoveis

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EdicaoObra : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edicao_obra)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val editarImagem: ImageView = findViewById(R.id.editar_imagem)
        val edtNomeEdicao: EditText = findViewById(R.id.edt_nomeEdicao)
        val edtAnoEdicao: EditText = findViewById(R.id.edt_anoEdicao)
        val edtArtistaEdicao: EditText = findViewById(R.id.edt_artistaEdicao)
        val edtDescricaoEdicao: EditText = findViewById(R.id.edt_descricaoEdicao)
        val btnEditar: Button = findViewById(R.id.btn_editarobra)
        val btnExcluir: Button = findViewById(R.id.btn_excluirobra)

        val obraId = intent.getStringExtra("obraId")  // Obtenha o ID do documento
        val nome = intent.getStringExtra("nome")
        val ano = intent.getStringExtra("ano")
        val artista = intent.getStringExtra("artista")
        val descricao = intent.getStringExtra("descricao")
        val imagemBytes = intent.getStringExtra("imagem")

        edtNomeEdicao.setText(nome)
        edtAnoEdicao.setText(ano)
        edtArtistaEdicao.setText(artista)
        edtDescricaoEdicao.setText(descricao)

        val imageBytes = Base64.decode(imagemBytes, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        editarImagem.setImageBitmap(decodedImage)

        btnEditar.setOnClickListener {
            val updatedObra = hashMapOf(
                "nomeObra" to edtNomeEdicao.text.toString(),
                "anoObra" to edtAnoEdicao.text.toString(),
                "artistaObra" to edtArtistaEdicao.text.toString(),
                "descricaoObra" to edtDescricaoEdicao.text.toString(),
                "imagemBase64" to imagemBytes
            )

            Firebase.firestore.collection("Obras").document(obraId!!)
                .set(updatedObra)
                .addOnSuccessListener {
                    Log.d("EdicaoObra", "Obra atualizada com sucesso!")
                    finish()
                }
                .addOnFailureListener { e ->
                    Log.w("EdicaoObra", "Erro ao atualizar a obra", e)
                }
        }

        btnExcluir.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle("Confirmação de Exclusão")
                setMessage("Tem certeza que deseja excluir esta obra?")
                setPositiveButton("Sim") { dialog, which ->
                    Firebase.firestore.collection("Obras").document(obraId!!)
                        .delete()
                        .addOnSuccessListener {
                            Log.d("EdicaoObra", "Obra excluída com sucesso!")
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Log.w("EdicaoObra", "Erro ao excluir a obra", e)
                        }
                }
                setNegativeButton("Não", null)
            }.show()
        }
    }
}
