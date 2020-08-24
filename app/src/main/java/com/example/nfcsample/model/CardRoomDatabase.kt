package com.example.nfcsample.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

@Database(entities = [Card::class], version = 1, exportSchema = false)
abstract class CardRoomDatabase: RoomDatabase() {

    abstract fun cardDao(): CardDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: CardRoomDatabase? = null
        fun getDatabase(
                context: Context,
                scope: CoroutineScope
        ): CardRoomDatabase {


            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        CardRoomDatabase::class.java,
                        "card_database"
                ).addCallback(CardDatabaseCallback(scope)).build()
                INSTANCE = instance
                return instance
            }
        }

    }
    private class CardDatabaseCallback(
            private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.cardDao())
                }
            }
        }

        suspend fun populateDatabase(cardDao: CardDao) {
            // Delete all content here.
            cardDao.deleteAll()

            // Add sample
            var card = Card("READ NFC")
           cardDao.insert(card)


        }
    }
}