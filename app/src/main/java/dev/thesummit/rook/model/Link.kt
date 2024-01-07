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
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "links")
data class Link(
    @PrimaryKey(autoGenerate = false) var id: Int,
    val title: String,
    val tags: String,
    val url: String,
    val modified: Long,
) {}

@Dao
interface LinkDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(link: Link)
  @Update suspend fun update(link: Link)
  @Delete suspend fun delete(link: Link)

  @Query("SELECT tags FROM links where tags LIKE '%' || :prefix || '%'")suspend fun getTags(prefix:String):List<String>

  @Query("SELECT * from links ORDER BY id DESC") fun getAllLinks(): Flow<List<Link>>
  @Query("DELETE FROM links") fun dropAllLinks()
}
