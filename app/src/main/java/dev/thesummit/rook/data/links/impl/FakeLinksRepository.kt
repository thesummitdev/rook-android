package dev.thesummit.rook.data.links.impl

import android.util.Log
import dev.thesummit.rook.data.Result
import dev.thesummit.rook.data.links.LinksRepository
import dev.thesummit.rook.model.Link
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class FakeLinksRepository() : LinksRepository {

  override suspend fun getLinks(): Flow<Result<List<Link>>> {
    return withContext(Dispatchers.IO) {
      delay(800) // pretend we are on a slow network
      if (shouldRandomlyFail()) {
        Log.i("Rook", "failing randomly")
        flow { emit(Result.Error(IllegalStateException("randomly failed"))) }
      } else {
        flow { emit(Result.Success(fakeLinks)) }
      }
    }
  }

  override suspend fun searchLinks(search:String): Flow<Result<List<Link>>> {
    return withContext(Dispatchers.IO) {
      delay(800) // pretend we are on a slow network
      if (shouldRandomlyFail()) {
        Log.i("Rook", "failing randomly")
        flow { emit(Result.Error(IllegalStateException("randomly failed"))) }
      } else {
        flow { emit(Result.Success(fakeLinks)) }
      }
    }
  }

  override suspend fun addLink(link: Link) {}
  override suspend fun deleteLink(link:Link) {}
  private var requestCount = 0

  private fun shouldRandomlyFail(): Boolean = ++requestCount % 5 == 0

  override suspend fun dropAllLinks() {}
  override suspend fun getTags(prefix:String):List<String> {
    return listOf()
  }
}
