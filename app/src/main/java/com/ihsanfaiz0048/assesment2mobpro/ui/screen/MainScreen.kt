package com.ihsanfaiz0048.assesment2mobpro.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.SettingsInputComponent
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ihsanfaiz0048.assesment2mobpro.R
import com.ihsanfaiz0048.assesment2mobpro.model.Menu
import com.ihsanfaiz0048.assesment2mobpro.navigation.Screen
import com.ihsanfaiz0048.assesment2mobpro.ui.theme.Assesment2MobproTheme
import com.ihsanfaiz0048.assesment2mobpro.util.SettingsDataStore
import com.ihsanfaiz0048.assesment2mobpro.util.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController){
    val dataStore = SettingsDataStore(LocalContext.current)
    val showList by dataStore.layoutFlow.collectAsState(true)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            dataStore.saveLayout(!showList)
                        }
                    }) {
                        Icon(
                            painter = painterResource(
                                if (showList) R.drawable.outline_grid_view_24
                                else R.drawable.baseline_view_list_24
                            ),
                            contentDescription = stringResource(
                                if (showList) R.string.grid
                                else R.string.list
                            ),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Outlined.History,
                            contentDescription = stringResource(R.string.history),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        ScreenContent(showList, Modifier.padding(innerPadding), navController)
    }
}

@Composable
fun ScreenContent(showList: Boolean,modifier: Modifier = Modifier, navController: NavHostController){
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: MainViewModel = viewModel(factory = factory)
    val data by viewModel.data.collectAsState()
    val dataMakanan by viewModel.dataMakanan.collectAsState()
    val dataMinuman by viewModel.dataMinuman.collectAsState()
    var filtered by remember { mutableStateOf("Semua") }

    if (data.isEmpty()){
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.list_kosong)
            )
        }
    }else{
        if (showList){
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 84.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {filtered = "Semua"},
                            modifier = Modifier.padding(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.SettingsInputComponent,
                                contentDescription = ""
                            )
                        }
                        Button(
                            onClick = {filtered = "Makanan"},
                            modifier = Modifier.padding(8.dp),
                        ) {
                            Text(text = stringResource(R.string.makanan))
                        }
                        Button(
                            onClick = {filtered = "Minuman"},
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(text = stringResource(R.string.minuman))
                        }
                        Button(
                            onClick = {filtered = "Semua"},
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(text = stringResource(R.string.semua))
                        }
                    }
                }
                if (filtered == "Makanan"){
                    items(dataMakanan){
                        ListItem(menu = it){
                            navController.navigate(Screen.FormUbah.withId(it.id))
                        }
                        HorizontalDivider()
                    }
                }else if (filtered == "Minuman"){
                    items(dataMinuman){
                        ListItem(menu = it){
                            navController.navigate(Screen.FormUbah.withId(it.id))
                        }
                        HorizontalDivider()
                    }
                }else{
                    items(data){
                        ListItem(menu = it){
                            navController.navigate(Screen.FormUbah.withId(it.id))
                        }
                        HorizontalDivider()
                    }
                }
            }
        }else{
            LazyVerticalStaggeredGrid(
                modifier = modifier.fillMaxSize(),
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(8.dp, 8.dp, 8.dp, 84.dp)
            ) {
                item(
                    span = StaggeredGridItemSpan.FullLine
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {filtered = "Semua"},
                            modifier = Modifier.padding(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.SettingsInputComponent,
                                contentDescription = ""
                            )
                        }
                        Button(
                            onClick = {filtered = "Makanan"},
                            modifier = Modifier.padding(8.dp),
                        ) {
                            Text(text = stringResource(R.string.makanan))
                        }
                        Button(
                            onClick = {filtered = "Minuman"},
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(text = stringResource(R.string.minuman))
                        }
                        Button(
                            onClick = {filtered = "Semua"},
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(text = stringResource(R.string.semua))
                        }
                    }
                }
                if (filtered == "Makanan"){
                    items(dataMakanan){
                        GridItem(menu = it){
                            navController.navigate(Screen.FormUbah.withId(it.id))
                        }
                    }
                }else if (filtered == "Minuman"){
                    items(dataMinuman){
                        GridItem(menu = it){
                            navController.navigate(Screen.FormUbah.withId(it.id))
                        }
                    }
                }else{
                    items(data){
                        GridItem(menu = it){
                            navController.navigate(Screen.FormUbah.withId(it.id))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ListItem(menu: Menu, onClick: () -> Unit){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable{onClick()},
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Column (
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ){
                Text(
                    text = menu.nama,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = menu.deskripsi,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.secondary,
                    maxLines = 2
                )
                Text(
                    text = "Rp " + menu.harga.toString(),
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
            ){
                Image(
                    painter = painterResource(menu.gambar),
                    contentDescription = menu.nama,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

    }
}

@Composable
fun GridItem(menu: Menu, onClick: () -> Unit){
    Card(
        modifier = Modifier.fillMaxWidth().clickable{onClick()},
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(1.dp, DividerDefaults.color)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .height(100.dp)
                    .width(200.dp)
                    .clip(RoundedCornerShape(8.dp))
            ){
                Image(
                    painter = painterResource(menu.gambar),
                    contentDescription = menu.nama,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Text(
                text = menu.nama,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Rp " + menu.harga.toString(),
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainScreenPreview() {
    Assesment2MobproTheme{
        MainScreen(rememberNavController())
    }
}