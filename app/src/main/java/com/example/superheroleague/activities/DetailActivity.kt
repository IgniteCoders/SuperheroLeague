package com.example.superheroleague.activities

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.superheroleague.R
import com.example.superheroleague.data.Superhero
import com.example.superheroleague.databinding.ActivityDetailBinding
import com.example.superheroleague.utils.SuperheroService
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    companion object {
        const val SUPERHERO_ID = "SUPERHERO_ID"
    }

    lateinit var binding: ActivityDetailBinding

    lateinit var superhero: Superhero

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val id = intent.getStringExtra(SUPERHERO_ID)!!

        getSuperheroById(id)

        binding.navigationView.setOnItemSelectedListener { menuItem ->
            binding.contentBiography.visibility = View.GONE
            binding.contentAppearance.visibility = View.GONE
            binding.contentStats.visibility = View.GONE

            when (menuItem.itemId) {
                R.id.menu_biography -> binding.contentBiography.visibility = View.VISIBLE
                R.id.menu_appearance -> binding.contentAppearance.visibility = View.VISIBLE
                R.id.menu_stats -> binding.contentStats.visibility = View.VISIBLE
            }
            true
        }

        binding.navigationView.selectedItemId = R.id.menu_biography
    }

    fun getSuperheroById(id: String) {
        // Llamada en un hilo secundario
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val service = SuperheroService.getInstance()
                superhero = service.findSuperheroById(id)

                // Volvemos al hilo principal
                CoroutineScope(Dispatchers.Main).launch {
                    loadData()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun loadData() {
        supportActionBar?.title = superhero.name
        supportActionBar?.subtitle = superhero.biography.realName
        Picasso.get().load(superhero.image.url).into(binding.avatarImageView)

        // Biography
        binding.publisherTextView.text = superhero.biography.publisher
        binding.placeOfBirthTextView.text = superhero.biography.placeOfBirth
        binding.alignmentTextView.text = superhero.biography.alignment

        // Stats
        binding.intelligenceTextView.text = "${superhero.stats.intelligence.toIntOrNull() ?: 0}"
        binding.intelligenceProgress.progress = superhero.stats.intelligence.toIntOrNull() ?: 0
    }
}