package com.examples.geoquiz

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import java.lang.Exception

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
private const val REQUEST_CODE_CHEAT = 0

class MainActivity : AppCompatActivity() {
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var previousButton: ImageButton
    private lateinit var questionTextView: TextView
    private lateinit var cheatButton: Button

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }


    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: called")
        setContentView(R.layout.activity_main)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex

//        val provider: ViewModelProvider = ViewModelProviders.of(this)
//        val quizViewModel = provider.get(QuizViewModel::class.java)
//        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        questionTextView = findViewById(R.id.question_text_view)
        nextButton = findViewById(R.id.next_button)
        previousButton = findViewById(R.id.previous_button)
        cheatButton = findViewById(R.id.cheat_button)


        trueButton.setOnClickListener {
            trueButton.isEnabled=false
            falseButton.isEnabled=false
            quizViewModel.questionBank[quizViewModel.currentIndex].answered=true
            checkAnswer(true)
        }

        falseButton.setOnClickListener {
            trueButton.isEnabled=false
            falseButton.isEnabled=false
            quizViewModel.questionBank[quizViewModel.currentIndex].answered=true
            checkAnswer(false)
        }

        nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            isAnswered(quizViewModel.currentIndex)
            updateQuestion()
        }

        cheatButton.setOnClickListener {view ->
            //Start Activity
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this, answerIsTrue)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val options = ActivityOptions.makeClipRevealAnimation(view, 0, 0, view.width, view.height)
                startActivityForResult(intent, REQUEST_CODE_CHEAT, options.toBundle())
            }
            else {
                startActivityForResult(intent, REQUEST_CODE_CHEAT)
            }
            updateQuestion()
        }

        previousButton.setOnClickListener {
            quizViewModel.currentIndex = (quizViewModel.currentIndex  -1) % quizViewModel.questionBank.size
            if (quizViewModel.currentIndex == -1) quizViewModel.currentIndex = quizViewModel.questionBank.lastIndex
            isAnswered(quizViewModel.currentIndex)
            updateQuestion()
        }

        questionTextView.setOnClickListener(View.OnClickListener {
            quizViewModel.currentIndex = (quizViewModel.currentIndex + 1) % quizViewModel.questionBank.size
            updateQuestion()
        })



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_CODE_CHEAT){
            quizViewModel.isCheater = data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: called")
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)

    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: called")
    }



    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        quizViewModel.questionBank[quizViewModel.currentIndex].apply {
            answered = true
            this.answered = userAnswer
        }

       val messageResId = when {
           quizViewModel.isCheater -> R.string.judgement_toast
           userAnswer == correctAnswer -> R.string.correct_toast
           else -> R.string.incorrect_toast
       }


        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }

    private fun isAnswered(index: Int) {
        if (quizViewModel.questionBank[index].answered == true){
            trueButton.isEnabled = false
            falseButton.isEnabled =false
        }
        else {
            trueButton.isEnabled = true
            falseButton.isEnabled = true
        }
    }

}
