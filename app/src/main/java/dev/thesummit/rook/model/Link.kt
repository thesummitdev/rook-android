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

@Entity(tableName = "links")
data class Link(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val title: String,
    val tags: String,
    val url: String,
    val modified: Int
)

@Dao
interface LinkDao {

  @Insert(onConflict = OnConflictStrategy.IGNORE) suspend fun insert(link: Link)
  @Update suspend fun update(link: Link)
  @Delete suspend fun delete(link: Link)

  @Query("SELECT * from links ORDER BY id ASC") fun getAllLinks(): Flow<List<Link>>
}
