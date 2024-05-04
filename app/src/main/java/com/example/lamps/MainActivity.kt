package com.example.lamps

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private var currentLevel = 1
    private var score = 0
    private val gridSize = 4 // Constant grid size
    private lateinit var buttons: Array<Array<Button>>
    private var lightsOn: Array<BooleanArray> = Array(gridSize) { BooleanArray(gridSize) }
    private lateinit var textViewScore: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textViewScore = findViewById(R.id.textViewScore)
        initializeGame()
        setupButtonListeners()
    }

    private fun initializeGame() {
        buttons = Array(gridSize) { row ->
            Array(gridSize) { col ->
                findViewById<Button>(resources.getIdentifier("button_$row$col", "id", packageName))
            }
        }

        for (i in 0 until gridSize) {
            for (j in 0 until gridSize) {
                lightsOn[i][j] = Math.random() < 0.5
                updateButtonAppearance(i, j)
            }
        }
    }

    private fun setupButtonListeners() {
        for (i in 0 until gridSize) {
            for (j in 0 until gridSize) {
                buttons[i][j].setOnClickListener {
                    toggleLights(i, j)
                    if (checkGameFinished()) {
                        Toast.makeText(this, "Поздравляем! Вы выиграли!", Toast.LENGTH_SHORT).show()
                        increaseLevel()
                        score++
                        updateScore()
                        shuffleLights()
                    }
                }
            }
        }
    }

    private fun updateButtonAppearance(row: Int, col: Int) {
        if (lightsOn[row][col]) {
            buttons[row][col].setBackgroundColor(ContextCompat.getColor(this, R.color.button_on))
        } else {
            buttons[row][col].setBackgroundColor(ContextCompat.getColor(this, R.color.button_off))
        }
        buttons[row][col].invalidate()
    }

    private fun toggleLights(row: Int, col: Int) {
        lightsOn[row][col] = !lightsOn[row][col]

        for (i in 0 until gridSize) {
            if (i != col) {
                lightsOn[row][i] = !lightsOn[row][i]
            }
            if (i != row) {
                lightsOn[i][col] = !lightsOn[i][col]
            }
        }
        for (i in 0 until gridSize) {
            for (j in 0 until gridSize) {
                updateButtonAppearance(i, j)
            }
        }
        logCurrentButtonState()
    }

    private fun updateScore() {
        textViewScore.text = "Score: $score"
    }

    private fun logCurrentButtonState() {
        val sb = StringBuilder()
        for (i in 0 until gridSize) {
            for (j in 0 until gridSize) {
                sb.append(if (lightsOn[i][j]) "1" else "0")
                sb.append(" ")
            }
            sb.append("\n")
        }
        Log.d("ButtonState", sb.toString())
    }

    private fun checkGameFinished(): Boolean {
        val firstLightState = lightsOn[0][0]
        for (i in 0 until gridSize) {
            for (j in 0 until gridSize) {
                if (lightsOn[i][j] != firstLightState) {
                    return false
                }
            }
        }
        return true
    }

    private fun increaseLevel() {
        currentLevel++
    }

    private fun shuffleLights() {
        for (i in 0 until gridSize) {
            for (j in 0 until gridSize) {
                lightsOn[i][j] = Math.random() < 0.5
                updateButtonAppearance(i, j)
            }
        }
        updateScore()
    }
}
