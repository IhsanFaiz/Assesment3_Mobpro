package com.ihsanfaiz0048.assesment2mobpro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ihsanfaiz0048.assesment2mobpro.ui.theme.Assesment2MobproTheme
import com.ihsanfaiz0048.assesment2mobpro.navigation.SetupNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Assesment2MobproTheme {
                SetupNavGraph()
            }
        }
    }
}

