package com.example.myapp

import android.content.ContentValues
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.adapter.DicaAdapter
import com.example.myapp.database.DicaDatabaseHelper
import com.example.myapp.model.Dica

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerViewDicas: RecyclerView
    private lateinit var dicaAdapter: DicaAdapter
    private lateinit var dbHelper: DicaDatabaseHelper
    private lateinit var dicas: MutableList<Dica>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DicaDatabaseHelper(this)

        dicas = getAllDicas()

        recyclerViewDicas = findViewById(R.id.recyclerViewDicas)
        dicaAdapter = DicaAdapter(dicas) { dica ->
            Toast.makeText(this, dica.descricao, Toast.LENGTH_SHORT).show()
        }
        recyclerViewDicas.layoutManager = LinearLayoutManager(this)
        recyclerViewDicas.adapter = dicaAdapter

        val searchView: SearchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                filterDicas(newText.orEmpty())
                return true
            }
        })
    }

    private fun addInitialDica() {

        if (getAllDicas().isEmpty()) {
            addDica("Compostagem Doméstica", "Transforme restos de comida em adubo.")
        }
    }

    private fun getAllDicas(): MutableList<Dica> {
        val dicas = mutableListOf<Dica>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(DicaDatabaseHelper.TABLE_NAME, null, null, null, null, null, null)

        with(cursor) {
            while (moveToNext()) {
                val title = getString(getColumnIndexOrThrow(DicaDatabaseHelper.COLUMN_TITLE))
                val description = getString(getColumnIndexOrThrow(DicaDatabaseHelper.COLUMN_DESCRIPTION))
                dicas.add(Dica(title, description))
            }
            close()
        }
        return dicas
    }

    private fun filterDicas(query: String) {
        val filteredDicas = dicas.filter { it.titulo.contains(query, ignoreCase = true) }
        dicaAdapter.updateData(filteredDicas)
    }

    private fun addDica(title: String, description: String) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DicaDatabaseHelper.COLUMN_TITLE, title)
            put(DicaDatabaseHelper.COLUMN_DESCRIPTION, description)
        }
        db.insert(DicaDatabaseHelper.TABLE_NAME, null, values)
        dicas.add(Dica(title, description))
        dicaAdapter.notifyItemInserted(dicas.size - 1)
    }
}