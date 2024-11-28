package com.group10.uxuiapp.view_model

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.group10.uxuiapp.view.MainNavigation
import com.group10.uxuiapp.ui.theme.UXUIApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UXUIApplicationTheme {
                MainNavigation()
            }
        }

    }
}