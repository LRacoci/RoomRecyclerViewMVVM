package com.example.roomwordsample

import android.app.*
import android.graphics.Color
import androidx.lifecycle.*
import kotlinx.coroutines.*
import kotlin.coroutines.*


class WordViewModel(application: Application) : AndroidViewModel(application) {

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)
    var bgIter = 0
    val colors = listOf(Color.CYAN, Color.GREEN, Color.YELLOW, Color.MAGENTA)
    var textViewBackgroundColor = colors[bgIter % colors.size]
    private val repository: WordRepository
    val allWords: LiveData<List<Word>>

    init {
        val wordsDao = WordRoomDatabase.getDatabase(application, scope).wordDao()
        repository = WordRepository(wordsDao)
        allWords = repository.allWords
    }

    fun insert(word: Word) = scope.launch(Dispatchers.IO) {
        repository.insert(word)
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun changeColor() {
        bgIter = bgIter + 1
        textViewBackgroundColor = colors[bgIter % colors.size]
    }
}