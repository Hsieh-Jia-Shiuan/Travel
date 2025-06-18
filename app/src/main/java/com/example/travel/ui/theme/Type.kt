package com.example.travel.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    displayMedium = ProjectTextStyle.H1,
    displaySmall = ProjectTextStyle.H2,
    headlineLarge = ProjectTextStyle.H3,
    headlineMedium = ProjectTextStyle.H4,
    headlineSmall = ProjectTextStyle.H5,
    titleLarge = ProjectTextStyle.H6,
    titleMedium = ProjectTextStyle.H7,
    bodyLarge = ProjectTextStyle.H8,
    bodyMedium = ProjectTextStyle.H9,
    labelMedium = ProjectTextStyle.H10
)

object ProjectTextStyle {

    /**
     * 需要被強調的重點應用
     */
    val H1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 48.sp,
        letterSpacing = 0.06.em
    )

    /**
     * 使用在頁面的主標題
     */
    val H2 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.06.em
    )

    /**
     * 區塊主要標題應用
     */
    val H3 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.06.em
    )

    /**
     * 區塊主要標題應用
     */
    val H4 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 34.sp,
        letterSpacing = 0.06.em
    )

    /**
     * 區塊主要標題應用
     */
    val H5 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 30.sp,
        letterSpacing = 0.06.em
    )

    /**
     * 區塊次要標題應用
     */
    val H6 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.06.em
    )

    /**
     * 區塊次要標題應用
     */
    val H7 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.06.em
    )

    /**
     * 內文資訊應用
     */
    val H8 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.04.em
    )

    /**
     * 內文資訊應用
     */
    val H9 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.em
    )

    /**
     * 輔助資訊應用
     */
    val H10 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 17.sp,
        letterSpacing = 0.em
    )
}