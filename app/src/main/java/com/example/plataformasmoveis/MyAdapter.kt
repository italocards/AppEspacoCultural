package com.example.plataformasmoveis

import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import obra_detalhada

class MyAdapter(private val obraList: ArrayList<Obra>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.obra_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentObra = obraList[position]

        holder.nomeObra.text = currentObra.nome
        holder.dataObra.text = currentObra.ano
        holder.artistaObra.text = currentObra.artista

        val imageBytes = Base64.decode(currentObra.imagem, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        holder.imagemObra.setImageBitmap(decodedImage)

        // Adiciona o listener de clique ao item
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, obra_detalhada::class.java).apply {
                putExtra("nome", currentObra.nome)
                putExtra("ano", currentObra.ano)
                putExtra("artista", currentObra.artista)
                putExtra("imagem", currentObra.imagem)
                putExtra("descricao", currentObra.descricao)
            }
            Log.d("MyAdapter", "Item clicado: ${currentObra.nome}")
            context.startActivity(intent)
        }
    }
    
    override fun getItemCount(): Int {
        return obraList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagemObra: ImageView = itemView.findViewById(R.id.img_obra)
        val nomeObra: TextView = itemView.findViewById(R.id.tv_nomeobra)
        val dataObra: TextView = itemView.findViewById(R.id.tv_dataobra)
        val artistaObra: TextView = itemView.findViewById(R.id.tv_autorobra)
    }
}
