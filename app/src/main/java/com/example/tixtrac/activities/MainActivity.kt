package com.example.tixtrac.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tixtrac.R
import com.example.tixtrac.databinding.ActivityMainBinding
import com.example.tixtrac.fragments.BuyTicketsFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.mainFragmentContainer,BuyTicketsFragment())
            commit()
        }

        binding.bottomNavView.setOnItemSelectedListener {item ->
            when(item.itemId){
                R.id.mi_buy_tickets -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.mainFragmentContainer,BuyTicketsFragment())
                        commit()
                    }
                }
                R.id.mi_scan -> {
                    Toast.makeText(
                        this,
                        "Feature not available yet!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                R.id.mi_tickets -> {
                    Toast.makeText(
                        this,
                        "Feature not available yet!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            true
        }
    }
}