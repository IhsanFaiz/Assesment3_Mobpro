package com.ihsanfaiz0048.assesment2mobpro.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Textsms
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.ihsanfaiz0048.assesment2mobpro.R
import com.ihsanfaiz0048.assesment2mobpro.ui.theme.MainGreen
import com.ihsanfaiz0048.assesment2mobpro.ui.theme.TextGreen
import com.ihsanfaiz0048.assesment2mobpro.util.ViewModelFactory
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, id: Long){
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: DetailViewModel = viewModel(factory = factory)

    var id by remember { mutableLongStateOf(id) }
    var nama by remember{ mutableStateOf("") }
    var deskripsi by remember { mutableStateOf("") }
    var harga by remember { mutableIntStateOf(0) }
    var gambar by remember { mutableIntStateOf(R.drawable.baseline_coffee_24) }
    var showDialog by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        val data = viewModel.getMenu(id) ?: return@LaunchedEffect

        id = data.id
        nama = data.nama
        deskripsi = data.deskripsi
        harga = data.harga
        gambar = data.gambar
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
                        text = nama,
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.MainGreen,
                    titleContentColor = Color.TextGreen,
                ),
                actions = {

                }
            )
        }
    ) { padding ->
        OrderMenu(
            id = id,
            nama = nama,
            deskripsi = deskripsi,
            hargaAsli = harga,
            harga = formatHarga(harga),
            gambar = gambar,
            onShowDialog = {
                showDialog = true
            },
            modifier = Modifier.padding(padding)
        )
        if (showDialog){
            DisplaySuccessDialog(
                onConfirmation = {
                    showDialog = false
                    navController.popBackStack()
                }
            )
        }
    }
}

//@Composable
//fun DeleteAction(delete: () -> Unit){
//    var expanded by remember { mutableStateOf(false) }
//
//    IconButton(onClick = {expanded = true}) {
//        Icon(
//            imageVector = Icons.Filled.MoreVert,
//            contentDescription = stringResource(R.string.hapus),
//            tint = MaterialTheme.colorScheme.primary
//        )
//        DropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false }
//        ) {
//            DropdownMenuItem(
//                text = {
//                    Text(text = stringResource(id = R.string.hapus))
//                },
//                onClick = {
//                    expanded = false
//                    delete()
//                }
//            )
//        }
//    }
//}

@Composable
fun OrderMenu(
    id: Long,
    nama: String,
    deskripsi: String,
    hargaAsli: Int,
    harga: String,
    gambar: Int,
    onShowDialog: () -> Unit,
    modifier: Modifier = Modifier
){
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: DetailViewModel = viewModel(factory = factory)

    var idMenu by remember { mutableLongStateOf(id) }
    var catatan by remember { mutableStateOf("") }
    var quantity by remember { mutableIntStateOf(0) }
    var totalBayar by remember { mutableIntStateOf(0) }

    val grayColor = if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ){
            var quantity by remember { mutableIntStateOf(1) }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .border(1.dp, grayColor, RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxHeight()
                ){
                    Column{
                        Text(
                            text = nama,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.headlineSmall,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.width(200.dp)
                        )
                        Text(
                            text = deskripsi,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.width(150.dp)
                        )
                    }
                    Text(
                        text = "Rp $harga",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    var disableButton by remember { mutableStateOf(true) }
                    Box(
                        modifier = Modifier.size(150.dp)
                    ){
                        Image(
                            painter = painterResource(gambar),
                            contentDescription = nama,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(32.dp))
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(32.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                    ) {
                        disableButton = quantity > 1
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                disabledContainerColor = MaterialTheme.colorScheme.surface
                            ),
                            onClick = {quantity -= 1},
                            enabled = disableButton,
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier
                                .size(30.dp)
                                .border(2.dp, Color.MainGreen, RoundedCornerShape(200.dp))
                        ) {
                            Text(
                                text = "-",
                                color = Color.MainGreen,
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                        Text(
                            text = quantity.toString(),
                            color = Color.MainGreen,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            contentPadding = PaddingValues(0.dp),
                            onClick = {quantity += 1},
                            modifier = Modifier
                                .size(30.dp)
                                .border(2.dp, Color.MainGreen, RoundedCornerShape(200.dp))
                        ) {
                            Text(
                                text = "+",
                                color = Color.MainGreen,
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                    }
                }
            }
            Text(
                text = stringResource(R.string.ringkasan_pembayaran),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, grayColor, RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                val totalHarga = hargaAsli * quantity
                val biayaPenaganan = 3000 * quantity
                val totalBayar = totalHarga + biayaPenaganan
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.harga),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = formatHarga(totalHarga),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.biaya_penanganan),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = formatHarga(biayaPenaganan),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                HorizontalDivider()
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.total_bayar),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = formatHarga(totalBayar),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, grayColor, RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.catatan),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = stringResource(R.string.opsional),
                    style = MaterialTheme.typography.titleMedium
                )
                OutlinedTextField(
                    value = catatan,
                    onValueChange = {catatan = it},
                    placeholder = { Text(text = stringResource(R.string.contoh_catatan)) },
                    shape = RoundedCornerShape(100.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.MainGreen,
                        unfocusedBorderColor = grayColor
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Textsms,
                            contentDescription = stringResource(R.string.contoh_catatan)
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        Button(
            onClick = {
                viewModel.insertOrder(
                    idMenu,
                    catatan,
                    quantity,
                    totalBayar
                )
                onShowDialog()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.MainGreen,
                contentColor = MaterialTheme.colorScheme.surface
            ),
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.pesan),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

fun formatHarga(harga: Int): String {
    val symbols = DecimalFormatSymbols(Locale("id", "ID")) // Memastikan pemisah adalah titik
    val formatter = DecimalFormat("#,###", symbols)

    val hargaFormatted = formatter.format(harga)
    return hargaFormatted
}