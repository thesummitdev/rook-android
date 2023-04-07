package dev.thesummit.rook.model

data class Link(
  val id: Int,
  val title: String,
  val tags: String,
  val url: String,
  val modified: Int
)
