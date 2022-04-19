package com.example.android.unscramble.ui.game

import android.service.autofill.Dataset
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel: ViewModel() {

    private val _score = MutableLiveData<Int>(0)
    val score: LiveData<Int>
        get() = _score
    private val _currentWordCount = MutableLiveData<Int>(0)
    val currentWordCount: MutableLiveData<Int>
        get() = _currentWordCount
    private val _currentScrambledWord = MutableLiveData<String>()
    val currentScrambledWord: LiveData<String>
        get() = _currentScrambledWord
    private var wordList: MutableList<String> = mutableListOf()
    private lateinit var currentWord: String

    private fun getNextWord(){

        currentWord = allWordsList.random()
        val tempWord = currentWord.toCharArray()

        while(String(tempWord) == currentWord) {
           tempWord.shuffle()
        }

        if(wordList.contains(currentWord)){
            getNextWord()
        } else {
            _currentScrambledWord.value = String(tempWord)
            _currentWordCount.value = (_currentWordCount.value)?.inc()
            wordList.add(currentWord)
        }

    }

//    Initialization of the view-model
    init{
        Log.d("GameFragment","GameViewModel Created")
        getNextWord()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("GameFragment", "GameViewModel Destroyed")
    }
/*
* Returns true if the current word count is less than MAX_NO_OF_WORDS.
* Updates the next word.
*/
    fun nextWord(): Boolean{
        return if(_currentWordCount.value!! < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        } else false
    }

//  Helper Method to increase score
    private fun increaseScore(){
        _score.value = (_score.value)?.plus(SCORE_INCREASE)
    }

//    Helper function to check if the user inputted the correct word, if so increment the score
    fun isUserWordCorrect(wordInputted: String): Boolean {
        if (wordInputted.equals(currentWord, true)){
            increaseScore()
            return true
        }
        return false
    }

//    Re-initialized the game data to restart game
    fun reinitializeData(){
        _score.value = 0
        _currentWordCount.value = 0
        wordList.clear()
        getNextWord()
    }
}
