package dev.thesummit.rook.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.thesummit.rook.model.Link
import dev.thesummit.rook.model.LinkDao
import dev.thesummit.rook.model.Setting
import dev.thesummit.rook.model.SettingDao
import kotlin.synchronized

@Database(entities = [Link::class, Setting::class], version = 1, exportSchema = false)
abstract class RookDatabase : RoomDatabase() {

  abstract fun links(): LinkDao
  abstract fun settings(): SettingDao

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
