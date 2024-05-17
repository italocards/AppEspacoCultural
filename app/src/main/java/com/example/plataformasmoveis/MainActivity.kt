package com.example.plataformasmoveis

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var imagebutton: ImageButton = findViewById(R.id.img_btn_espacocultural)
        var tv_acesso: TextView = findViewById(R.id.tv_acessoadmin)

        imagebutton.setOnClickListener(){
            GoTelaObras()
        }

        tv_acesso.setOnClickListener(){
            GoTelaLogin()
        }

    }

    private fun GoTelaObras(){
        val telaobras = Intent(this,ObrasActivity:: class.java)
        startActivity(telaobras)
    }

    private fun GoTelaLogin(){
        val telalogin = Intent(this,Login:: class.java)
        startActivity(telalogin)
    }
}