package com.example.sharedtransitionsdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.sharedtransitionsdemo.navigation.AppHavHost
import com.example.sharedtransitionsdemo.screens.main.MainDestination
import com.example.sharedtransitionsdemo.ui.theme.SharedTransitionsDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SharedTransitionsDemoTheme {
                AppHavHost(startDestination = MainDestination)
            }
        }
    }
}