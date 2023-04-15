package dev.thesummit.rook.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import dev.thesummit.rook.R

@OptIn(ExperimentalTextApi::class)
val provider =
    GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs
    )

@OptIn(ExperimentalTextApi::class) val FiraSansFont = GoogleFont("Fira Sans")
@OptIn(ExperimentalTextApi::class) val FiraMonoFont = GoogleFont("Fira Mono")

@OptIn(ExperimentalTextApi::class)
val FiraSansFontFamily =
    FontFamily(
        Font(googleFont = FiraSansFont, fontProvider = provider, weight = FontWeight.W100),
        Font(googleFont = FiraSansFont, fontProvider = provider, weight = FontWeight.W200),
        Font(googleFont = FiraSansFont, fontProvider = provider, weight = FontWeight.W300),
        Font(googleFont = FiraSansFont, fontProvider = provider, weight = FontWeight.W400),
        Font(googleFont = FiraSansFont, fontProvider = provider, weight = FontWeight.W500),
        Font(googleFont = FiraSansFont, fontProvider = provider, weight = FontWeight.W600),
        Font(googleFont = FiraSansFont, fontProvider = provider, weight = FontWeight.W700),
        Font(googleFont = FiraSansFont, fontProvider = provider, weight = FontWeight.W800),
        Font(googleFont = FiraSansFont, fontProvider = provider, weight = FontWeight.W900),
    )

@OptIn(ExperimentalTextApi::class)
val FiraMonoFontFamily = FontFamily(
        Font(googleFont = FiraMonoFont, fontProvider = provider, weight = FontWeight.W400),
        Font(googleFont = FiraMonoFont, fontProvider = provider, weight = FontWeight.W500),
        Font(googleFont = FiraMonoFont, fontProvider = provider, weight = FontWeight.W700),

)

// Set of Material typography styles to start with
val Typography =
    Typography(
        displayLarge =
            TextStyle(
                fontFamily = FiraSansFontFamily,
                fontSize = 28.sp,
                lineHeight = 30.sp,
                fontWeight = FontWeight.W300,
                letterSpacing = -1.sp,
            ),
        displayMedium =
            TextStyle(
                fontFamily = FiraSansFontFamily,
            ),
        displaySmall =
            TextStyle(
                fontFamily = FiraSansFontFamily,
            ),
        headlineLarge =
            TextStyle(
                fontFamily = FiraSansFontFamily,
            ),
        headlineMedium =
            TextStyle(
                fontFamily = FiraSansFontFamily,
            ),
        headlineSmall =
            TextStyle(
                fontFamily = FiraSansFontFamily,
            ),
        titleLarge =
            TextStyle(
                fontFamily = FiraSansFontFamily,
                fontSize = 22.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.W800,
            ),
        titleMedium =
            TextStyle(
                fontFamily = FiraSansFontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.W500,
            ),
        titleSmall =
            TextStyle(
                fontFamily = FiraSansFontFamily,
            ),
        labelLarge =
            TextStyle(
                fontFamily = FiraMonoFontFamily,
            ),
        labelMedium =
            TextStyle(
                fontFamily = FiraMonoFontFamily,
            ),
        labelSmall =
            TextStyle(
                fontFamily = FiraMonoFontFamily,
            ),
        bodyLarge =
            TextStyle(
                fontFamily = FiraSansFontFamily,
            ),
        bodyMedium =
            TextStyle(
                fontFamily = FiraSansFontFamily,
            ),
        bodySmall =
            TextStyle(
                fontFamily = FiraSansFontFamily,
            ),
    )
