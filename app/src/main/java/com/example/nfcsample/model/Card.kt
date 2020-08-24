package com.example.nfcsample.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "card_table")
class Card(@PrimaryKey @ColumnInfo(name = "card") val card: String)
