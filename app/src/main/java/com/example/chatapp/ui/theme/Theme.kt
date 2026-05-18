package com.example.chatapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable



private val LightColorScheme =

    lightColorScheme(

        primary =
            PrimaryLight,

        background =
            BackgroundLight,

        surface =
            SurfaceLight

    )



private val DarkColorScheme =

    darkColorScheme(

        primary =
            PrimaryDark,

        background =
            BackgroundDark,

        surface =
            SurfaceDark

    )



@Composable
fun ChatAppTheme(

    darkTheme:Boolean =
        isSystemInDarkTheme(),

    content:
    @Composable () -> Unit

) {

    MaterialTheme(
        colorScheme =
            if(
                darkTheme
            )
                DarkColorScheme
            else
                LightColorScheme,
        typography =
            Typography,
        content =
            content
    )

}