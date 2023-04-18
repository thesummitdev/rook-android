package dev.thesummit.rook.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.thesummit.rook.model.Link
import dev.thesummit.rook.model.LinkDao
import kotlin.synchronized

@Database(entities = [Link::class], version = 1, exportSchema = false)
abstract class RookDatabase : RoomDatabase() {

  abstract fun linkDao(): LinkDao

  companion object {
    @Volatile private var Instance: RookDatabase? = null

    fun getDatabase(context: Context): RookDatabase {
      return Instance
          ?: synchronized(this) {
            Room.databaseBuilder(context, RookDatabase::class.java, "rook_database")
                .fallbackToDestructiveMigration()
                .build()
                .also { Instance = it }
          }
    }
  }
}
