package com.example.superheroleague.activities

import android.os.Bundle
import android.view.MenuItem
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

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id = intent.getStringExtra(SUPERHERO_ID)!!

        getSuperheroById(id)

        binding.navigationView.setOnItemSelectedListener { menuItem ->
            binding.contentBiography.root.visibility = View.GONE
            binding.contentAppearance.root.visibility = View.GONE
            binding.contentStats.root.visibility = View.GONE

            when (menuItem.itemId) {
                R.id.menu_biography -> binding.contentBiography.root.visibility = View.VISIBLE
                R.id.menu_appearance -> binding.contentAppearance.root.visibility = View.VISIBLE
                R.id.menu_stats -> binding.contentStats.root.visibility = View.VISIBLE
            }
            true
        }

        binding.navigationView.selectedItemId = R.id.menu_biography
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
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
        binding.contentBiography.publisherTextView.text = superhero.biography.publisher
        binding.contentBiography.placeOfBirthTextView.text = superhero.biography.placeOfBirth
        binding.contentBiography.alignmentTextView.text = superhero.biography.alignment
        binding.contentBiography.alignmentTextView.setTextColor(getColor(superhero.getAlignmentColor()))
        binding.contentBiography.occupationTextView.text = superhero.work.occupation
        binding.contentBiography.baseTextView.text = superhero.work.base

        // Appearance
        binding.contentAppearance.genderTextView.text = superhero.appearance.gender
        binding.contentAppearance.raceTextView.text = superhero.appearance.race
        binding.contentAppearance.eyeColorTextView.text = superhero.appearance.eyeColor
        binding.contentAppearance.hairColorTextView.text = superhero.appearance.hairColor
        binding.contentAppearance.weightTextView.text = superhero.appearance.weight[1]
        binding.contentAppearance.heightTextView.text = superhero.appearance.height[1]

        // Stats
        with(superhero.stats) {
            binding.contentStats.intelligenceTextView.text = "$intelligence"
            binding.contentStats.intelligenceProgress.progress = intelligence
            binding.contentStats.strengthTextView.text = "$strength"
            binding.contentStats.strengthProgress.progress = strength
            binding.contentStats.speedTextView.text = "$speed"
            binding.contentStats.speedProgress.progress = speed
            binding.contentStats.durabilityTextView.text = "$durability"
            binding.contentStats.durabilityProgress.progress = durability
            binding.contentStats.powerTextView.text = "$power"
            binding.contentStats.powerProgress.progress = power
            binding.contentStats.combatTextView.text = "$combat"
            binding.contentStats.combatProgress.progress = combat
        }
    }
}