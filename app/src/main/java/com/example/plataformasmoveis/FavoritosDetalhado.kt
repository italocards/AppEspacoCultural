package com.example.plataformasmoveis

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FavoritosDetalhado : AppCompatActivity() {

    private var notaRB: Float = 0.0f
    private var media: Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_favoritos_detalhado)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.btn_scan)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //variavel media puxada do firebase

        val db = Firebase.firestore
        val mediaNotasRef = db.collection("MediaNotas").document("udlUbwufakYYvV6bkoth")
        mediaNotasRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    media = document.getDouble("Media") ?: 0.0

                    val mediaFormatada = String.format("%.1f", media)
                    val tv_media: TextView = findViewById(R.id.tv_media)
                    tv_media.text = mediaFormatada
                } else {
                    // O documento não existe
                }
            }
            .addOnFailureListener { exception ->
                // Tratamento de falhas
            }

        val tv_media: TextView = findViewById(R.id.tv_media)
        var btn_avaliar: Button = findViewById(R.id.btn_avaliar)
        var tv_nome: TextView = findViewById(R.id.tv_nome)
        var tv_contato: TextView = findViewById(R.id.tv_contato)
        var tv_comentario: TextView = findViewById(R.id.tv_comentario)
        val ratingBar: RatingBar = findViewById(R.id.rb_avaliacao)

        //val mediaFormatada = String.format("%.1f", media)
        //tv_media.text = mediaFormatada

        notaRB = ratingBar.rating

        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            notaRB = rating
            Toast.makeText(this, "Rating: $rating", Toast.LENGTH_SHORT).show()
        }

        btn_avaliar.setOnClickListener(){
            val notaNova = mapOf(
                "Nome" to tv_nome.text.toString(),
                "Contato" to tv_contato.text.toString(),
                "Comentário" to tv_comentario.text.toString(),
                "Nota" to notaRB,
            )

            Firebase.firestore.collection("Nota").add(notaNova)

            val novaMedia = (notaRB + media).toDouble() / 2

            mediaNotasRef.update("Media", novaMedia)
        }




        var img_btn_busca: ImageButton = findViewById(R.id.img_btn_busca)
        var img_btn_qrcode: ImageButton = findViewById(R.id.img_btn_qrcode)
        var img_btn_avaliacao: ImageButton = findViewById(R.id.img_btn_avaliacao)

        img_btn_busca.setOnClickListener(){
            GoTela(BuscaDetalhada::class.java)
        }
        img_btn_qrcode.setOnClickListener(){
            GoTela(QRCodeDetalhado::class.java)
        }
        img_btn_avaliacao.setOnClickListener(){
            GoTela(FavoritosDetalhado::class.java)
        }
    }

    private fun GoTela(classeTela: Class<*>) {
        val teladetalhada = Intent(this, classeTela)
        startActivity(teladetalhada)
    }

}