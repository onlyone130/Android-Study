package com.example.bmicalculator

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.preference.PreferenceManager
import com.example.bmicalculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        loadData()

        binding.resultButton.setOnClickListener {
            if (binding.weightEditText.text.isNotBlank() && binding.heightEditText.text.isNotBlank()) {
                // 마지막에 입력한 내용 저장
                saveData(
                    binding.heightEditText.text.toString().toFloat(),
                    binding.weightEditText.text.toString().toFloat(),
                )

                val intent = Intent(this, ResultActivity::class.java).apply {
                    putExtra("weight", binding.weightEditText.text.toString().toFloat())
                    putExtra("height", binding.heightEditText.text.toString().toFloat())
                }
                startActivity(intent)
            }
        }
    }

    private fun saveData(height: Float, weight: Float) {
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = pref.edit()

        editor.putFloat("KEY_HEIGHT", height)
            .putFloat("KEY_WEIGHT", weight)
            .apply()
    }

    private fun loadData() {
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val height = pref.getFloat("KEY_HEIGHT", 0f)
        val weight = pref.getFloat("KEY_WEIGHT", 0f)

        if (height != 0f && weight != 0f) {
            binding.heightEditText.setText(height.toString())
            binding.weightEditText.setText(weight.toString())
        }
    }
}