package com.example.plataformasmoveis

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ObrasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_obras)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var layoutObra0: LinearLayout = findViewById(R.id.l_lay_obra0)
        var img_btn_busca: ImageButton = findViewById(R.id.img_btn_busca)
        var img_btn_qrcode: ImageButton = findViewById(R.id.img_btn_qrcode)
        var img_btn_avaliacao: ImageButton = findViewById(R.id.img_btn_avaliacao)


        layoutObra0.setOnClickListener(){
            GoTela(obra_detalhada::class.java)
        }
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