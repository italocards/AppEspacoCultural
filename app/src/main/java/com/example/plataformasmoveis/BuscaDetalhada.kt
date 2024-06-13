package com.example.plataformasmoveis

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class BuscaDetalhada : AppCompatActivity() {

    private lateinit var dbref: DatabaseReference
    private lateinit var buscaRecyclerview: RecyclerView
    private lateinit var buscaArraylist: ArrayList<Obra>
    private lateinit var tempArraylist: ArrayList<Obra>
    private lateinit var searchView: SearchView
    private lateinit var adapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_busca_detalhada)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.btn_scan)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        buscaRecyclerview = findViewById(R.id.buscaList)
        buscaRecyclerview.layoutManager = LinearLayoutManager(this)
        buscaRecyclerview.setHasFixedSize(true)

        buscaArraylist = arrayListOf()
        tempArraylist = arrayListOf()

        var img_btn_busca: ImageButton = findViewById(R.id.img_btn_busca)
        var img_btn_qrcode: ImageButton = findViewById(R.id.img_btn_qrcode)
        var img_btn_avaliacao: ImageButton = findViewById(R.id.img_btn_avaliacao)

        img_btn_qrcode.setOnClickListener {
            GoTela(QRCodeDetalhado::class.java)
        }
        img_btn_avaliacao.setOnClickListener {
            GoTela(FavoritosDetalhado::class.java)
        }

        searchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                tempArraylist.clear()
                val searchText = newText?.toLowerCase() ?: ""
                if (searchText.isNotEmpty()) {
                    buscaArraylist.forEach {
                        it.nome?.toLowerCase()?.let { nome ->
                            if (nome.contains(searchText)) {
                                tempArraylist.add(it)
                            }
                        }
                    }
                    buscaRecyclerview.adapter?.notifyDataSetChanged()
                } else {
                    tempArraylist.clear()
                    tempArraylist.addAll(buscaArraylist)
                    buscaRecyclerview.adapter?.notifyDataSetChanged()
                }
                return false
            }
        })

        getUserData()
    }

    private fun getUserData() {
        dbref = FirebaseDatabase.getInstance().getReference("Obras")
        Log.d("ContentValues", "GetUserData")
        Firebase.firestore.collection("Obras")
            .get().addOnSuccessListener { result ->
                for (doc in result) {
                    val obra = Obra().apply {
                        nome = doc.getString("nomeObra") ?: ""
                        ano = doc.getString("anoObra") ?: ""
                        artista = doc.getString("artistaObra") ?: ""
                        imagem = doc.getString("imagemBase64") ?: ""
                        descricao = doc.getString("descricaoObra") ?: ""
                    }
                    buscaArraylist.add(obra)
                }
                tempArraylist.addAll(buscaArraylist)
                adapter = MyAdapter(tempArraylist)
                buscaRecyclerview.adapter = adapter
            }
    }

    private fun GoTela(classeTela: Class<*>) {
        val teladetalhada = Intent(this, classeTela)
        startActivity(teladetalhada)
    }
}
