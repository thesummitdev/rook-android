package dev.thesummit.rook.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

enum class SettingKey(val key: String) {
  API_KEY("apiKey"),
  HOST("host"),
}

@Entity(tableName = "settings")
data class Setting(
    @PrimaryKey(autoGenerate = false) val key: String,
    val value: String,
)

@Dao
interface SettingDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(setting: Setting)
  @Update suspend fun update(setting: Setting)
  @Delete suspend fun delete(setting: Setting)
  @Query("SELECT * from settings WHERE key=:key") fun get(key: String): Flow<Setting?>
  @Query("SELECT * from settings WHERE key=:key") suspend fun getOnce(key: String): Setting?
  @Query("DELETE from settings WHERE key in (:keys)") suspend fun dropSettings(keys:List<String>)
  @Query("DELETE from settings") suspend fun dropAllSettings()
}
