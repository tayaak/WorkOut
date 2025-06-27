package com.example.workout.viewmodel
import android.content.Context
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.workout.model.Exercise
import com.example.workout.R

//hier wird der Zustand meines Workouts gespeichert
class WorkoutViewModel() : ViewModel() {

    //    mit der enum class, aknn ich definieren, welche Optionen es als Konstanten gibt.Also entweder Exercise oder Rest
    enum class Workstate {
        EXERCISE,
        REST
    }

    // Liste aller Übungen im Training
    private val exercises = listOf(
        Exercise("Chrunch", "Stell dir vor du bist ein Shrimp", R.drawable.crunch, 30),
        Exercise("Plank", "Versuche nicht einzuknicken, schön die Spannung halten", R.drawable.plank, 30),
        Exercise("Pushup", "Jetzt ein paar Push Ups", R.drawable.pushup, 30),
        Exercise("Auswahl", "Jetzte hast du die Wahl, welche Übung möchtest du?", R.drawable.besonders, 30),
        Exercise("Das Boot", "versuche ein Boot zu machen", R.drawable.boot, 30),
        Exercise("Die Brücke", "Heb die Hüfte,für die Dehnung", R.drawable.bruecke, 30),
        Exercise("Cool down ", "Zum entspannen, dehn dich etwas ", R.drawable.dehnen, 30)
    )

    // welche Übung ist gerade dran, 0 ist die erste übung
    private var currentIndex = 0

    // LiveData ist für die aktuelle Übung zuständig, man kann das value nicht von außen geänder werden, mutableLivedata, kann später noch geändert werden
    private val _currentExercise = MutableLiveData<Exercise?>()
    val currentExercise: LiveData<Exercise?> = _currentExercise

    //gibt die Information, welcher State gerrade ist; Übung oder Pause
    private val _state = MutableLiveData<Workstate?>()
    val state: LiveData<Workstate?> = _state

    // Countdown in Sekunden der übriggeböiebenen Zeit
    private val _timeLeft = MutableLiveData<Int>()
    val timeLeft: LiveData<Int> = _timeLeft

    // Fortschritt (Anzahl abgeschlossener Übungen)
    private val _progress = MutableLiveData<Int>()
    val progress: LiveData<Int> = _progress

    // hier wird der timer abgespeichert
    private var timer: CountDownTimer? = null



    // hier kommt die Musik
    private var mediaPlayer: MediaPlayer? = null

    private fun playSound(context: Context, resId: Int) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(context, resId)
        mediaPlayer?.start()
    }


    // if- else Bedingung- wenn wir noch nicht bei der letzten Übung sind kommt die nächst, sonst ist man fertig
    fun startExercise(context:Context) {
        if (currentIndex < exercises.size) { //größer als anzahl der exercises
            val exercise = exercises[currentIndex]
            _currentExercise.value = exercise
            _progress.value = currentIndex
            _state.value = Workstate.EXERCISE
            playSound(context,R.raw.start)
            startTimer(exercise.durationSeconds,context) // nächste Übung
        } else {
            _currentExercise.value = null // fertig!
            _state.value = null
            playSound(context,R.raw.ende)
        }
    }

    // habe hier einfach meien Dataclass Exercise genommen, weiß nicht ob da sso best practice ist, aber hat funktioniert
    private fun startReset(context: Context) {
        _state.value = Workstate.REST
        _currentExercise.value = Exercise("Kurz Pause ", "Trink ruhig was, gleich geht es weiter ", R.drawable.pause, 15)
        playSound(context,R.raw.paus)
        startTimer(15,context)


    }



    // Hier wird der alte Timer gestoppt und der neue gesetzt

    private fun startTimer(duration: Int, context: Context) {


        _timeLeft.value = duration

        timer?.cancel()
        timer = object : CountDownTimer(duration * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _timeLeft.value = (millisUntilFinished / 1000).toInt()
// jede sekunde wird immer aktualiesiert
            }
// If else Bedinung; wenn der Timer fertig ist und wir in der Übung waren, dann Pause, sonst Index erhöhen und nächste Übung
            override fun onFinish() {

                if (_state.value == Workstate.EXERCISE) {
                    startReset(context)
                } else {
                    currentIndex++
                    startExercise(context)
                }
            }
        }.start()
    }
}




