package com.example.plataformasmoveis

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ObrasActivity : AppCompatActivity() {

    private lateinit var dbref : DatabaseReference
    private lateinit var obraRecyclerview : RecyclerView
    private lateinit var obraArraylist : ArrayList<Obra>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_obras)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        obraRecyclerview = findViewById(R.id.obraList)
        obraRecyclerview.layoutManager = LinearLayoutManager(this)
        obraRecyclerview.setHasFixedSize(true)

        obraArraylist = arrayListOf<Obra>()


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
                    obra.nome = doc.get("nomeObra").toString()
                    obra.ano = doc.get("anoObra").toString()
                    obra.artista = doc.get("artistaObra").toString()
                    obra.imagem = doc.get("imagemBase64").toString()
                    obra.descricao = doc.get("descricaoObra").toString()
                    obraArraylist.add(obra)
                    Log.d("ContentValues", "SIZE: "+obraArraylist.size)

                }
                obraRecyclerview.adapter = MyAdapter(obraArraylist)

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
    private fun GoTela(classeTela: Class<*>) {
        val teladetalhada = Intent(this, classeTela)
        startActivity(teladetalhada)
    }


}