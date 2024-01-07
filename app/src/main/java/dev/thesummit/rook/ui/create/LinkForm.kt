package dev.thesummit.rook.ui.create
import androidx.compose.ui.text.input.TextFieldValue

sealed class LinkForm {
  enum class Fields {
    TITLE,
    URL,
    TAGS,
  }

  data class LinkFormState(
      val title: String = "",
      val titleErrorMessage: String = "",
      val url: String = "",
      val urlErrorMessage: String = "",
      val tags: TextFieldValue = TextFieldValue(""),
      val invalidFields: Set<Fields> = setOf(),
      val valid: Boolean = false,
  )
}
