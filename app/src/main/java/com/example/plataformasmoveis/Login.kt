package com.example.plataformasmoveis

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.w3c.dom.Text

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var tv_feedback: TextView = findViewById(R.id.tv_feedback)

        var edtLogin: EditText = findViewById(R.id.edt_login)

        var edtSenha: EditText = findViewById(R.id.edt_senha)

        var btnConfirm: Button = findViewById(R.id.btn_confirm)

        btnConfirm.setOnClickListener(){
            if(edtLogin.text.toString() == "admin" && edtSenha.text.toString() == "12345"){
                GoTelaAdmin()
            }else{
                tv_feedback.text = "Acesso Negado"
            }
        }
    }

    private fun GoTelaAdmin(){
        val telaadmin = Intent(this,SelecaoAdmin:: class.java)
        startActivity(telaadmin)
    }
}