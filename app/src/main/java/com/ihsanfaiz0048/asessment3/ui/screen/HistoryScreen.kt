package com.ihsanfaiz0048.asessment3.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.ihsanfaiz0048.asessment3.model.OrderWithMenu
import com.ihsanfaiz0048.asessment3.navigation.Screen
import com.ihsanfaiz0048.asessment3.R
import com.ihsanfaiz0048.asessment3.ui.theme.MainGreen
import com.ihsanfaiz0048.asessment3.ui.theme.TextGreen
import com.ihsanfaiz0048.asessment3.util.OrderStatus
import com.ihsanfaiz0048.asessment3.util.ViewModelFactory
import com.ihsanfaiz0048.asessment3.util.getStatus
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavHostController){
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screen.Home.route) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.kembali),
                            tint = Color.TextGreen
                        )
                    }
                },
                title = {
                    Text(
                        text = stringResource(R.string.pesanan),
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.MainGreen,
                    titleContentColor = Color.TextGreen,
                ),
            )
        }
    ) { innerPadding ->
        HistoryContent(Modifier.padding(innerPadding), navController)
    }
}

@Composable
fun HistoryContent(modifier: Modifier = Modifier, navController: NavHostController){
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: HistoryViewModel = viewModel(factory = factory)
    val data by viewModel.dataHistory.collectAsState()

    if (data.isEmpty()){
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.history_kosong)
            )
        }
    }else{
        var currentTime by remember {
            mutableLongStateOf(
                System.currentTimeMillis()
            )
        }

        LaunchedEffect(Unit) {

            while (true) {

                currentTime =
                    System.currentTimeMillis()

                delay(1000)
            }
        }
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 84.dp)
        ){
            items(data){
                ListItemHistory(orderWithMenu = it, currentTime = currentTime, navController = navController, onClick = {
                    navController.navigate(Screen.HistoryDetail.withId(it.order.id))
                })
                HorizontalDivider(thickness = 1.dp)
            }
        }
    }
}

@Composable
fun ListItemHistory(orderWithMenu: OrderWithMenu, onClick: () -> Unit, currentTime: Long, navController: NavHostController){
    val statusOrder = orderWithMenu.getStatus(
        currentTime
    )
    val grayColor = if (isSystemInDarkTheme()) Color.LightGray else Color.DarkGray

    val statusColor = when (statusOrder) {
        OrderStatus.PENDING ->
            Color.Gray

        OrderStatus.PROCESSING ->
            Color.MainGreen

        OrderStatus.COMPLETED ->
            Color.Green

        else -> {
            Color.Red
        }
    }

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
                    text = orderWithMenu.menu.nama,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = orderWithMenu.menu.deskripsi,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.width(250.dp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = "Rp " + formatHarga(orderWithMenu.order.totalBayar),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = statusOrder.toString(),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier
                        .background(statusColor, RoundedCornerShape(100.dp))
                        .padding(8.dp)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ){
                Box(
                    modifier = Modifier.size(100.dp)
                ){
                    Image(
                        painter = painterResource(orderWithMenu.menu.gambar),
                        contentDescription = orderWithMenu.menu.nama,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Button(
                        onClick = {onClick()},
                        modifier = Modifier
                            .height(40.dp)
                            .align(Alignment.BottomCenter)
                            .offset(y = 20.dp)
                            .border(3.dp, Color.MainGreen, RoundedCornerShape(100.dp)).fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            MaterialTheme.colorScheme.surface,
                            Color.MainGreen
                        ),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.detail),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                if (statusOrder == OrderStatus.PENDING){
                    Button(
                        onClick = {
                            navController.navigate(
                                Screen.DetailMenu.edit(
                                orderWithMenu.menu.id,
                                orderWithMenu.order.id
                            ))
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = grayColor,
                            contentColor = MaterialTheme.colorScheme.surface
                        ),
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier
                            .width(100.dp)
                            .height(30.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.edit),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

    }
}