package dev.thesummit.rook.data.links

import dev.thesummit.rook.data.Result
import dev.thesummit.rook.model.Link
import kotlinx.coroutines.flow.Flow

/** Interface to the Links data layer. */
interface LinksRepository {

  suspend fun addLink(link:Link)
  suspend fun getLinks(): Flow<Result<List<Link>>>
  suspend fun dropAllLinks(): Unit
}
