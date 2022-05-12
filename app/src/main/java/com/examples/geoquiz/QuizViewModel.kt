package com.examples.geoquiz

import android.util.Log
import androidx.lifecycle.ViewModel


private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel() {

    var currentIndex = 0
    var isCheater = false

    val questionBank = listOf(
        Questions(R.string.question_australia, true),
        Questions(R.string.question_oceans, true),
        Questions(R.string.question_mideast, false),
        Questions(R.string.question_africa, false),
        Questions(R.string.question_americas, true),
        Questions(R.string.question_asia, true))

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText:Int
        get() = questionBank[currentIndex].textResID

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }
}