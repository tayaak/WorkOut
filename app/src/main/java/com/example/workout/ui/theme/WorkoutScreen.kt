package com.example.workout.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.workout.viewmodel.WorkoutViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext




@Composable

//hier wird durch das viewModel die aktuellen Daten geholt, also :
fun WorkoutScreen(workoutViewModel: WorkoutViewModel = viewModel()) {
    val exercise by workoutViewModel.currentExercise.observeAsState() //welche Übung
    val timeLeft by workoutViewModel.timeLeft.observeAsState(0) //wie viel zeit
    val progress by workoutViewModel.progress.observeAsState(0)//wie viel Fortschritt
     val totalExercises = 7 // Anzahl der Übungen, am besten aus ViewModel holen, hier vereinfacht

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        workoutViewModel.startExercise(context)
    }

    //hier kommt das UI desing
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (exercise != null) { //läuft die Übung noch, falls ja, UI für anzeige
                Text(
                    text = exercise!!.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                Image(
                    painter = painterResource(id = exercise!!.imageResId),
                    contentDescription = exercise!!.name,
                    modifier = Modifier.size(200.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = exercise!!.description)
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Zeit übrig: $timeLeft Sekunden")
                Spacer(modifier = Modifier.height(16.dp))
//zeigt einen Fortschritsbalken an
                LinearProgressIndicator(
                    progress = progress / totalExercises.toFloat(),
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Text(text = "Du hast es geschafft!🙌 Das heutige Work Out ist beendet 🥳", style = MaterialTheme.typography.headlineMedium)
            }
        }
    }
}
