package dev.thesummit.rook.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ElevatedSuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import kotlin.collections.listOf

@Composable
fun AutocompleteTextField(
    value: TextFieldValue,
    predictions: List<String> = listOf(),
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
) {

  // Track if the text field has focus
  val interactionSource = remember { MutableInteractionSource() }
  val isFocused by interactionSource.collectIsFocusedAsState()
  val lazyListState = rememberLazyListState()

  Column(
      modifier = modifier.animateContentSize(),
  ) {

    // Text field is the top item
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        label = label,
        isError = isError,
        supportingText = supportingText,
        onValueChange = onValueChange,
        interactionSource = interactionSource,
    )

    // Only show predictions if the text field has focus and some text has been entered
    if (!value.text.isNullOrEmpty() && isFocused) {
      LazyRow(
          state = lazyListState,
      ) {
        items(predictions) { prediction ->
          ElevatedSuggestionChip(
              modifier = Modifier.padding(4.dp),
              onClick = { onValueChange(appendToValue(value, prediction)) },
              label = { Text(prediction) }
          )
        }
      }
    }
  }
}

/**
 * Appends the new value to the end of a new TextFieldValue, consuming a matching prefix if
 * one exists.
 *
 * @return the updated TextFieldValue.
 */
private fun appendToValue(startingValue: TextFieldValue, newValue: String): TextFieldValue {

  val words = startingValue.text.split(" ")

  if (words.last().startsWith(newValue.substring(0, Math.min(words.last().length, newValue.length)))) {
    val newWords = listOf(*words.dropLast(1).toTypedArray(), newValue)
    val newText = newWords.toSet().joinToString(" ")
    return startingValue.copy(text = newText, selection = TextRange(newText.length))
  }

  val newWords = listOf(*words.dropLast(1).toTypedArray(), newValue, words.last())
  val newText = newWords.toSet().joinToString(" ")
  return startingValue.copy(text = newText, selection = TextRange(newText.length))
}
