package com.ihsanfaiz0048.asessment3.ui.screen

import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Textsms
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ihsanfaiz0048.asessment3.navigation.Screen
import com.ihsanfaiz0048.asessment3.R
import com.ihsanfaiz0048.asessment3.model.Review
import com.ihsanfaiz0048.asessment3.model.User
import com.ihsanfaiz0048.asessment3.network.ApiStatus
import com.ihsanfaiz0048.asessment3.network.UserDataStore
import com.ihsanfaiz0048.asessment3.ui.theme.MainGreen
import com.ihsanfaiz0048.asessment3.ui.theme.TextGreen
import com.ihsanfaiz0048.asessment3.util.ViewModelFactory
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, idMenu: Long, idOrder: Long? = null){
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: DetailViewModel = viewModel(factory = factory)
    val dataStoreUser = UserDataStore(context)
    val user by dataStoreUser.userFlow.collectAsState(User())

    var menuId by remember { mutableIntStateOf(idMenu.toInt()) }
    var nama by remember{ mutableStateOf("") }
    var deskripsi by remember { mutableStateOf("") }
    var harga by remember { mutableIntStateOf(0) }
    var gambar by remember { mutableStateOf("") }
    var quantity by remember { mutableIntStateOf(1) }
    var catatan: String by remember { mutableStateOf("") }

    var showDialogSuccess by remember { mutableStateOf(false) }
    var showDialogUpdate by remember { mutableStateOf(false) }


    LaunchedEffect(user.email) {
        
        if (idOrder == null){
            val data = viewModel.getMenu(menuId, user.email) ?: return@LaunchedEffect

            menuId = data.id
            nama = data.nama
            deskripsi = data.deskripsi
            harga = data.harga
            gambar = data.gambar

        }else{
            val dataOrder = viewModel.getOrderWithMenuEdit(user.email, idOrder.toInt()) ?: return@LaunchedEffect

            menuId = dataOrder.menu.id
            nama = dataOrder.menu.nama
            deskripsi = dataOrder.menu.deskripsi
            harga = dataOrder.menu.harga
            gambar = dataOrder.menu.gambar
            quantity = dataOrder.order.quantity
            catatan = dataOrder.order.catatan ?: ""
        }
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
            )
        }
    ) { padding ->
        OrderMenu(
            userId = user.email,
            idOrder = idOrder?.toInt(),
            idMenu = menuId,
            nama = nama,
            deskripsi = deskripsi,
            hargaAsli = harga,
            harga = formatHarga(harga),
            gambar = gambar,
            catatan = catatan,
            quantity = quantity,
            onCatatanChange = {catatan = it},
            onQuantityChange = {quantity = it},
            onShowDialogSuccess = {
                showDialogSuccess = true
            },
            onShowDialogUpdate = {
                showDialogUpdate = true
            },
            modifier = Modifier.padding(padding)
        )
        if (showDialogSuccess){
            DisplaySuccessDialog(
                onConfirmation = {
                    showDialogSuccess = false
                    navController.navigate(Screen.HistoryScreen.route)
                }
            )
        }
        if (showDialogUpdate){
            DisplayUpdateDialog(
                onConfirmation = {
                    showDialogUpdate = false
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
fun OrderMenu(
    userId: String,
    idOrder: Int?,
    idMenu: Int,
    nama: String,
    deskripsi: String,
    hargaAsli: Int,
    harga: String,
    gambar: String,
    catatan: String,
    quantity: Int,
    onCatatanChange: (String) -> Unit,
    onQuantityChange: (Int) -> Unit,
    onShowDialogSuccess: () -> Unit,
    onShowDialogUpdate: () -> Unit,
    modifier: Modifier = Modifier
){
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: DetailViewModel = viewModel(factory = factory)

    var totalBayar by remember { mutableIntStateOf(0) }

    val grayColor = if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxHeight()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
        ,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ){
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
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
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.width(200.dp)
                        )
                        Text(
                            text = deskripsi,
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.width(150.dp)
                        )
                    }
                    Text(
                        text = "Rp $harga",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.titleSmall,
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
                        modifier = Modifier.size(100.dp)
                    ){
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(gambar)
                                .crossfade(true)
                                .build(),
                            contentDescription = nama,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(32.dp))
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        disableButton = quantity > 1
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.MainGreen,
                                contentColor = MaterialTheme.colorScheme.surface,
                                disabledContainerColor = grayColor
                            ),
                            onClick = {onQuantityChange(quantity - 1)},
                            enabled = disableButton,
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier
                                .size(30.dp)
                        ) {
                            Text(
                                text = "-",
                            )
                        }
                        Text(
                            text = quantity.toString(),
                            color = Color.MainGreen,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.MainGreen,
                                contentColor = MaterialTheme.colorScheme.surface
                            ),
                            contentPadding = PaddingValues(0.dp),
                            onClick = {onQuantityChange(quantity + 1)},
                            modifier = Modifier
                                .size(30.dp)
                        ) {
                            Text(
                                text = "+",
                            )
                        }
                    }
                }
            }
            Text(
                text = stringResource(R.string.ringkasan_pembayaran),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
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
                totalBayar = totalHarga + biayaPenaganan
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.harga),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = formatHarga(totalHarga),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.biaya_penanganan),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = formatHarga(biayaPenaganan),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                HorizontalDivider()
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.total_bayar),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = formatHarga(totalBayar),
                        style = MaterialTheme.typography.titleMedium,
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
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = stringResource(R.string.opsional),
                    style = MaterialTheme.typography.titleSmall
                )
                OutlinedTextField(
                    value = catatan,
                    onValueChange = {onCatatanChange(it)},
                    placeholder = { Text(text = stringResource(R.string.contoh_catatan), style = MaterialTheme.typography.titleSmall) },
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
        LazyReview(idMenu, viewModel, userId)
        Button(
            onClick = {
                if (userId.isEmpty()) {
                    Toast.makeText(
                        context,
                        "Silakan login terlebih dahulu",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@Button
                }
                if (idOrder == null){
                    viewModel.insertOrder(
                        userId,
                        idMenu,
                        catatan,
                        quantity,
                        totalBayar,
                    )
                    onShowDialogSuccess()
                }
                else {
                    viewModel.updateOrder(
                        userId,
                        idOrder,
                        idMenu,
                        catatan,
                        quantity,
                        totalBayar
                    )
                    onShowDialogUpdate()
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.MainGreen,
                contentColor = MaterialTheme.colorScheme.surface
            ),
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if (idOrder == null){
                Text(
                    text = stringResource(R.string.pesan),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
            }else{
                Text(
                    text = stringResource(R.string.update),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}

@Composable
fun LazyReview(menuId: Int, viewModel: DetailViewModel, userId: String){
    val dataReview by viewModel.reviewState.collectAsState()
    val status by viewModel.status.collectAsState()

    LaunchedEffect(key1 = menuId) {
        viewModel.loadReviews(menuId, userId)
    }

    when(status){
        ApiStatus.LOADING -> {
            Column (modifier = Modifier.fillMaxWidth(),horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(color = Color.MainGreen)
            }
        }
        ApiStatus.FAILED -> {
            Column (
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Gagal memuat data",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Periksa koneksi internet Anda lalu coba lagi.",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )

                    Button(
                        onClick = {
                            viewModel.loadReviews(menuId, userId)
                        }
                    ) {
                        Text("Coba Lagi")
                    }
                }
            }
        }
        ApiStatus.SUCCESS -> {
            if (dataReview.isEmpty()){
                Text(
                    text = stringResource(R.string.review_kosong)
                )
            }else{
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 84.dp)
                ){
                    items(dataReview) {
                        ReviewItems(review = it)
                    }
                }
            }
        }
    }


}


@Composable
fun ReviewItems(review: Review){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
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
                    text = review.review,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = review.star.toString(),
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.width(200.dp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Box(
                modifier = Modifier.size(100.dp)
            ){
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(review.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }
    }
}


fun formatHarga(harga: Int): String {
    val symbols = DecimalFormatSymbols(Locale("id", "ID"))
    val formatter = DecimalFormat("#,###", symbols)

    val hargaFormatted = formatter.format(harga)
    return hargaFormatted
}