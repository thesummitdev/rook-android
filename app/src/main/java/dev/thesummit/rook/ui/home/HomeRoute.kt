package dev.thesummit.rook.ui.home

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

@Composable
fun HomeRoute() {
  val lazyListState = rememberLazyListState()
  HomeLinksFeed(lazyListState = lazyListState)
}
