package dev.thesummit.rook.data.links.impl

import dev.thesummit.rook.data.Result
import dev.thesummit.rook.data.links.LinksRepository
import dev.thesummit.rook.model.Link
import kotlinx.coroutines.withContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.Dispatchers

class FakeLinksRepository: LinksRepository {

  override suspend fun getLinks(): Result<List<Link>> {

    return withContext(Dispatchers.IO){
      delay(800) // pretend we are on a slow network
      Result.Success(links)
    }

  }

}
