package dev.thesummit.rook.ui.home

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.thesummit.rook.R
import dev.thesummit.rook.model.Link
import dev.thesummit.rook.model.LinksFeed

@Composable
fun HomeLinksFeed(
    uiState: HomeUiState,
    lazyListState: LazyListState,
    onRefreshLinks: () -> Unit,
    modifier: Modifier = Modifier,
) {

  HomeScreenWithList(
      uiState = uiState,
      onRefreshLinks = onRefreshLinks,
      modifier = modifier,
  ) { hasLinksUiState, contentModifier ->
    LinkList(
        linksFeed = hasLinksUiState.linksFeed,
        modifier = contentModifier,
        state = lazyListState
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenWithList(
    uiState: HomeUiState,
    onRefreshLinks: () -> Unit,
    modifier: Modifier = Modifier,
    hasLinksContent: @Composable (uiState: HomeUiState.HasLinks, modifier: Modifier) -> Unit
) {
  LoadingContent(
      empty = uiState.isLoading,
      emptyContent = { FullScreenLoading() },
      refreshing = uiState.isLoading,
      onRefreshLinks = onRefreshLinks,
      content = {
        when (uiState) {
          is HomeUiState.HasLinks -> hasLinksContent(uiState, Modifier)
          is HomeUiState.NoLinks -> noLinksContent(uiState)
        }
      }
  )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun noLinksContent(uiState: HomeUiState.NoLinks, modifier: Modifier = Modifier) {

  val scrollState = rememberScrollState()

  Box(modifier.fillMaxSize().wrapContentSize(Alignment.Center).verticalScroll(scrollState)) {
    Text(text = stringResource(R.string.load_error))
  }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LoadingContent(
    empty: Boolean,
    emptyContent: @Composable () -> Unit,
    refreshing: Boolean,
    onRefreshLinks: () -> Unit,
    content: @Composable () -> Unit,
) {

  val pullRefreshState = rememberPullRefreshState(refreshing, { onRefreshLinks() })

  if (empty) {
    emptyContent()
  } else {
    Box(Modifier.pullRefresh(pullRefreshState).fillMaxSize()) {
      if (!refreshing) {
        content()
      }
      PullRefreshIndicator(
          refreshing,
          pullRefreshState,
          Modifier.align(Alignment.TopCenter),
          backgroundColor = MaterialTheme.colorScheme.surface,
          contentColor = MaterialTheme.colorScheme.onSurface,
      )
    }
  }
}

/** Full screen circular progress indicator */
@Composable
private fun FullScreenLoading() {
  Box(modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center)) {
    CircularProgressIndicator()
  }
}

@Composable
fun LinkList(
    linksFeed: LinksFeed,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    state: LazyListState = rememberLazyListState(),
) {
  LazyColumn(modifier = modifier, contentPadding = contentPadding, state = state) {
    items(linksFeed.allLinks, { link: Link -> link.url }) { link -> LinkCard(link) }
  }
}

@Composable
fun LinkTitle(link: Link) {
  Text(
      text = link.title,
      style = MaterialTheme.typography.titleMedium,
      maxLines = 3,
      overflow = TextOverflow.Ellipsis,
      color = MaterialTheme.colorScheme.tertiary,
  )
}

@Composable
fun SimpleLink(link: Link) {
  LinkTitle(link)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LinkCard(link: Link) {

  val context = LocalContext.current

  ListItem(
      headlineContent = { LinkTitle(link) },
      supportingContent = {
        Text(
            text = link.url,
            style = MaterialTheme.typography.labelSmall,
        )
        Text(
            text = link.tags,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.secondary,
        )
      },
      modifier =
          Modifier.clickable {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link.url))
            context.startActivity(intent)
          },
  )
  Divider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.tertiary)
}
