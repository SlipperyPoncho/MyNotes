package com.artem.android.mynotes

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Note(@PrimaryKey val id: UUID = UUID.randomUUID(), var title: String = "", var text: String = "")