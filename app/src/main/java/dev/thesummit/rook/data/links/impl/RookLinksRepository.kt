package dev.thesummit.rook.data.links.impl

import dev.thesummit.rook.data.Result
import dev.thesummit.rook.data.links.LinksRepository
import dev.thesummit.rook.model.Link
import dev.thesummit.rook.model.LinkDao
import kotlin.collections.flatMap
import kotlin.collections.map as collectionMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

private const val TAG = "Rook"

class RookLinksRepository(private val linkDao: LinkDao) : LinksRepository {

  override suspend fun getLinks(): Flow<Result<List<Link>>> {

    return withContext(Dispatchers.IO) {
      try {
        linkDao.getAllLinks().map { links -> Result.Success(links) }
      } catch (exception: Exception) {
        flow { Result.Error(exception) }
      }
    }
  }

  override suspend fun addLink(link: Link) {
    linkDao.insert(link)
  }

  override suspend fun getTags(prefix: String): List<String> {
    if (prefix.isEmpty()) {
      return listOf()
    }

    // TODO would be nice to run this as a query in the database rather than post processing the
    // list of duplicates
    return linkDao
        .getTags(prefix)
        .collectionMap { it.split(" ") }
        .flatMap { it }
        .asSequence()
        .filter { it.startsWith(prefix) }
        .distinct()
        .sorted()
        .toList()
  }

  override suspend fun deleteLink(link: Link) {
    linkDao.delete(link)
  }

  override suspend fun dropAllLinks() {
    linkDao.dropAllLinks()
  }
}
