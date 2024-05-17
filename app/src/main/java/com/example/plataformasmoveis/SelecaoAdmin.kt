package com.example.plataformasmoveis

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SelecaoAdmin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_selecao_admin)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var btn_cadastro: Button = findViewById(R.id.btn_cadastro)
        var btn_gerencia: Button = findViewById(R.id.btn_gerenciar)

        btn_cadastro.setOnClickListener(){
            GoTelaCadastro()
        }

        btn_gerencia.setOnClickListener(){
            GoTelaGerenciar()
        }
    }

    private fun GoTelaCadastro() {
        val telacadastro = Intent(this, CadastroObra::class.java)
        startActivity(telacadastro)
    }

    private fun GoTelaGerenciar() {
        val telagerencia = Intent(this, GerenciamentoObra::class.java)
        startActivity(telagerencia)
    }
}