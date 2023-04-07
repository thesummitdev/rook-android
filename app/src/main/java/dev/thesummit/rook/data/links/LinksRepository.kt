package dev.thesummit.rook.data.links

import dev.thesummit.rook.model.Link
import dev.thesummit.rook.data.Result

/**
 * Interface to the Links data layer.
 */
interface LinksRepository {

  suspend fun getLinks(): Result<List<Link>>

}
