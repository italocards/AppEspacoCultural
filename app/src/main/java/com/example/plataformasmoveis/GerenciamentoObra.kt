package com.example.plataformasmoveis

import android.os.Bundle
import android.util.Log
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

class GerenciamentoObra : AppCompatActivity() {

    private lateinit var dbref : DatabaseReference
    private lateinit var gerenciaRecyclerview : RecyclerView
    private lateinit var gerenciaArraylist : ArrayList<Obra>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gerenciamento_obra)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.btn_scan)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        gerenciaRecyclerview = findViewById(R.id.gerenciaList)
        gerenciaRecyclerview.layoutManager = LinearLayoutManager(this)
        gerenciaRecyclerview.setHasFixedSize(true)

        gerenciaArraylist = arrayListOf<Obra>()

        getUserData()

    }

    private fun getUserData() {


        dbref = FirebaseDatabase.getInstance().getReference("Obras")
        Log.d("ContentValues", "GetUserData")
        Firebase.firestore.collection("Obras")
            .get().addOnSuccessListener { result ->
                for (doc in result) {
                    //Log.d("contentValues", doc.data.toString())
                    var obra = Obra()
                    obra.id = doc.id
                    obra.nome = doc.get("nomeObra").toString()
                    obra.ano = doc.get("anoObra").toString()
                    obra.artista = doc.get("artistaObra").toString()
                    obra.imagem = doc.get("imagemBase64").toString()
                    obra.descricao = doc.get("descricaoObra").toString()
                    gerenciaArraylist.add(obra)
                    Log.d("ContentValues", "SIZE: "+gerenciaArraylist.size)

                }
                gerenciaRecyclerview.adapter = MyAdapterGerenciamento(gerenciaArraylist)

            }



//        dbref.addValueEventListener(object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                Log.d("ContentValues", "SnapShot")
//
//                if(snapshot.exists()){
//
//                    for (obraSnapshot in snapshot.children){
//
//                        val obra = obraSnapshot.getValue(Obra::class.java)
//                        obraArraylist.add(obra!!)
//                    }
//
//
//                    Log.d("ContentValues", ""+obraArraylist.size)
//
//                }
//            }


    }
}