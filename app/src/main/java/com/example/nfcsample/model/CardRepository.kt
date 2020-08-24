package com.example.nfcsample.model


import androidx.lifecycle.LiveData

class CardRepository(private val cardDao: CardDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allCards: LiveData<List<Card>> = cardDao.getCards()

    suspend fun insert(card: Card) {
        cardDao.insert(card)
    }
}