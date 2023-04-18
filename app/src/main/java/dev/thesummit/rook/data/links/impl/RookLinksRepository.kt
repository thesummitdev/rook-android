package dev.thesummit.rook.data.links.impl

import dev.thesummit.rook.data.Result
import dev.thesummit.rook.data.links.LinksRepository
import dev.thesummit.rook.model.Link
import dev.thesummit.rook.model.LinkDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class RookLinksRepository(private val linkDao: LinkDao) : LinksRepository {

  override suspend fun getLinks(): Flow<Result<List<Link>>> {

    return withContext(Dispatchers.IO) {
      linkDao.getAllLinks().map { links -> Result.Success(links) }
    }
  }
}
