package com.example.workout.model
// hier hab ich meine Datenklasse angelegt, so das jedes Workout einen Namen, eine beschriebung, ein Bild und eine Dauer zugeordnet bekommt
data class Exercise(
    val name: String,
    val description: String,
    val imageResId: Int,
    val durationSeconds: Int
)
