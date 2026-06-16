package com.ihsanfaiz0048.asessment3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ihsanfaiz0048.asessment3.navigation.SetupNavGraph
import com.ihsanfaiz0048.asessment3.ui.theme.Asessment3Theme
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