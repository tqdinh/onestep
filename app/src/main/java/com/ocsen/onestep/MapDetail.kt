package com.ocsen.onestep

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ocsen.onestep.databinding.ActivityMapDetailBinding

class MapDetail : AppCompatActivity() {
    lateinit var binding: ActivityMapDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}