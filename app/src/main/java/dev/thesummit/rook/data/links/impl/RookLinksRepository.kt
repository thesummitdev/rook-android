package dev.thesummit.rook.data.links.impl

import dev.thesummit.rook.data.Result
import dev.thesummit.rook.data.links.LinksRepository
import dev.thesummit.rook.model.Link
import dev.thesummit.rook.model.LinkDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.chromium.net.CronetEngine

private const val TAG = "Rook"

class RookLinksRepository(private val linkDao: LinkDao, private val cronetEngine: CronetEngine) :
    LinksRepository {

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

  override suspend fun dropAllLinks() {
    linkDao.dropAllLinks();
  }
}
