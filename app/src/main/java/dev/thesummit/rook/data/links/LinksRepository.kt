package dev.thesummit.rook.data.links

import dev.thesummit.rook.data.Result
import dev.thesummit.rook.model.Link
import kotlinx.coroutines.flow.Flow

/** Interface to the Links data layer. */
interface LinksRepository {

  suspend fun addLink(link:Link)
  suspend fun getLinks(): Flow<Result<List<Link>>>
  suspend fun searchLinks(search:String): Flow<Result<List<Link>>>
  suspend fun getTags(prefix:String): List<String>
  suspend fun deleteLink(link:Link): Unit
  suspend fun dropAllLinks(): Unit
}
