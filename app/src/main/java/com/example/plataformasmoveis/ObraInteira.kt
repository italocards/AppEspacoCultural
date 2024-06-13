package com.example.plataformasmoveis

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Base64
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Locale

class ObraInteira : AppCompatActivity() {

    private lateinit var textToSpeech:TextToSpeech
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_obra_inteira)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.btn_scan)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Recebe os dados da Intent
        val nome = intent.getStringExtra("nome")
        val ano = intent.getStringExtra("ano")
        val artista = intent.getStringExtra("artista")
        val imagemBase64 = intent.getStringExtra("imagem")
        val descricao = intent.getStringExtra("descricao")

        // Encontra as Views e define os valores
        val nomeTextView: TextView = findViewById(R.id.tv_nomeobra_detalhada)
        val anoTextView: TextView = findViewById(R.id.tv_dataobra_detalhada)
        val artistaTextView: TextView = findViewById(R.id.tv_autorobra_detalhada)
        val descricaoTextView: TextView = findViewById(R.id.tv_descricaoobra_detalhada)
        val imagemImageView: ImageView = findViewById(R.id.img_obra_detalhada)

        nomeTextView.text = nome
        anoTextView.text = ano
        artistaTextView.text = artista
        descricaoTextView.text = descricao

        val imageBytes = Base64.decode(imagemBase64, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        imagemImageView.setImageBitmap(decodedImage)

        textToSpeech = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech.setLanguage(Locale.getDefault())
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(this, "Language not supported", Toast.LENGTH_LONG).show()
                }
            }
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

        val btn_tts: ImageButton = findViewById(R.id.btn_TextToSpeech)

        //val textToSpeech = TextToSpeech(this){status->
            //if(status == TextToSpeech.SUCCESS){
                //val resultado = textToSpeech.setLanguage(Locale.getDefault())
                //if(resultado == TextToSpeech.LANG_MISSING_DATA || resultado == TextToSpeech.LANG_NOT_SUPPORTED){
                    //Toast.makeText(this,"language is not supported",Toast.LENGTH_LONG).show()
                //}
            //}
        //}

        btn_tts.setOnClickListener {
            val textToSpeak = StringBuilder().append(nomeTextView.text.toString().trim())
                .append(". ")
                .append(anoTextView.text.toString().trim())
                .append(". ")
                .append("Por ")
                .append(artistaTextView.text.toString().trim())
                .append(". ")
                .append(descricaoTextView.text.toString().trim())
                .toString()
            if (textToSpeech.isSpeaking) {
                textToSpeech.stop()
            } else {
                textToSpeech.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, null)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        textToSpeech.shutdown() // Properly release TextToSpeech resources
    }

    private fun GoTela(classeTela: Class<*>) {
        val teladetalhada = Intent(this, classeTela)
        startActivity(teladetalhada)
    }

}