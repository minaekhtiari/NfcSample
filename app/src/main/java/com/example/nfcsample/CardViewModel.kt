
package com.example.nfcsample

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.nfcsample.model.Card
import com.example.nfcsample.model.CardRepository
import com.example.nfcsample.model.CardRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CardViewModel(application: Application) : AndroidViewModel(application) {


    private val repository: CardRepository

    val allCards: LiveData<List<Card>>

    init {


        val cardsDao =CardRoomDatabase.getDatabase(application, viewModelScope).cardDao()
        repository = CardRepository(cardsDao)
        allCards = repository.allCards
    }


   fun insert(card: Card) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(card)
    }
}