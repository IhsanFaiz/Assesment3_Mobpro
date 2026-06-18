package com.ihsanfaiz0048.asessment3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ihsanfaiz0048.asessment3.navigation.SetupNavGraph
import com.ihsanfaiz0048.asessment3.ui.theme.Asessment3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Asessment3Theme {
                SetupNavGraph()
            }
        }
    }
}