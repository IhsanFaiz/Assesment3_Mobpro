package com.ihsanfaiz0048.assesment2mobpro.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.ihsanfaiz0048.assesment2mobpro.R
import com.ihsanfaiz0048.assesment2mobpro.ui.theme.MainGreen
import com.ihsanfaiz0048.assesment2mobpro.ui.theme.TextGreen
import com.ihsanfaiz0048.assesment2mobpro.util.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryDetail(navController: NavHostController, id: Long){
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: HistoryDetailViewModel = viewModel(factory = factory)

    var id by remember { mutableLongStateOf(id) }
    var nama by remember { mutableStateOf("") }
    var totalHarga by remember { mutableIntStateOf(0) }
    var quantity by remember { mutableIntStateOf(0) }
    var harga by remember { mutableIntStateOf(0) }
    var catatan: String? by remember { mutableStateOf("") }
    var showAlert by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val data = viewModel.getDetailHistory(id) ?: return@LaunchedEffect

        nama = data.menu.nama
        harga = data.menu.harga
        totalHarga = data.order.totalBayar
        quantity = data.order.quantity
        catatan = data.order.catatan
    }
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.kembali),
                            tint = Color.TextGreen
                        )
                    }
                },
                title = {
                    Text(
                        text = stringResource(R.string.detail_pesanan),
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
        HistoryDetailContent(
            Modifier.padding(innerPadding),
            harga,
            catatan,
            nama,
            totalHarga,
            quantity,
            onShowAlert = {
                showAlert = true
            }
        )
        if (showAlert){
            DisplayAlertDialog(onDismissRequest = {showAlert = false}) {
                showAlert = false
                viewModel.deleteOrder(id)
                navController.popBackStack()
            }
        }
    }
}

@Composable
fun HistoryDetailContent(
    modifier: Modifier = Modifier,
    harga: Int,
    catatan: String?,
    nama: String,
    totalHarga: Int,
    quantity: Int,
    onShowAlert: () -> Unit
){
    val grayColor = if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .padding(16.dp)
            .fillMaxHeight()
    ) {
        Column{
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, grayColor, RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.pembelian),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = nama,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = quantity.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                if (catatan != "" && catatan != null){
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.catatan),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = catatan,
                            modifier = Modifier
                                .width(200.dp),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
                    .border(1.dp, grayColor, RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.detail_pembelian),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                ) {
                    val totalHargaAsli = harga * quantity
                    Text(
                        text = stringResource(R.string.harga),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = formatHarga(totalHargaAsli),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                val biayaPenanganan = 3000 * quantity
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.biaya_penanganan),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = formatHarga(biayaPenanganan),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                HorizontalDivider()
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.total_bayar),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = formatHarga(totalHarga),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
        Button(
            onClick = {
                onShowAlert()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(top = 16.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = stringResource(R.string.hapus_history),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}