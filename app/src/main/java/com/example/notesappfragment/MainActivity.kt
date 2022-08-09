package com.example.notesappfragment

import android.os.Bundle
import android.view.ActionMode
import android.view.MenuItem

import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.example.notesappfragment.UI.fragment.HomeFragment
import com.example.notesappfragment.databinding.ActivityMainBinding
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private val setPosition = ArrayList<Int>()
    private var actionMode: ActionMode? = null
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var toolbar: Toolbar

    companion object {
        const val EXTRA_RESULTS = "android.speech.extra.RESULTS"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        toolbar = findViewById(R.id.my_toolbar)
        toolbar.title = ""
        setSupportActionBar(toolbar)
        toggle = ActionBarDrawerToggle(this, binding.mainDrawerLayout, toolbar, 0, 0)
        binding.mainDrawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.mainNavigationMenu.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_voice -> {
                    Toast.makeText(this@MainActivity, "clicked menu voice", Toast.LENGTH_SHORT)
                        .show()
                    true
                }
                R.id.menu_image -> {
                    Toast.makeText(this@MainActivity, "clicked menu image", Toast.LENGTH_SHORT)
                        .show()
                    true
                }
                R.id.menu_web_link -> {
                    Toast.makeText(this@MainActivity, "clicked menu web link", Toast.LENGTH_SHORT)
                        .show()
                    true
                }
                else -> false
            }
            binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener true
        }

        val homeFragment = HomeFragment()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView, homeFragment).commit()

        }


    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }


    }


}