package com.example.myapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.R
import com.example.myapp.model.Dica

class DicaAdapter(private var dicas: List<Dica>, private val itemClickListener: (Dica) -> Unit) :
    RecyclerView.Adapter<DicaAdapter.DicaViewHolder>() {

    class DicaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tituloDica: TextView = view.findViewById(R.id.tituloDica)
        val descricaoDica: TextView = view.findViewById(R.id.descricaoDica)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DicaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dica, parent, false)
        return DicaViewHolder(view)
    }

    override fun onBindViewHolder(holder: DicaViewHolder, position: Int) {
        val dica = dicas[position]
        holder.tituloDica.text = dica.titulo
        holder.descricaoDica.text = dica.descricao
        holder.itemView.setOnClickListener { itemClickListener(dica) }
    }

    override fun getItemCount(): Int = dicas.size

    fun updateData(newDicas: List<Dica>) {
        dicas = newDicas
        notifyDataSetChanged()
    }
}

