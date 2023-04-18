package dev.thesummit.rook.model

import kotlinx.coroutines.flow.Flow

data class LinksFeed(val links: List<Link>) {
  val allLinks: List<Link> = links
}
