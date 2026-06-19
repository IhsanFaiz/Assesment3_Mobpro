package com.ihsanfaiz0048.asessment3.ui.screen

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.ihsanfaiz0048.asessment3.R
import com.ihsanfaiz0048.asessment3.model.User
import com.ihsanfaiz0048.asessment3.network.ApiStatus
import com.ihsanfaiz0048.asessment3.network.UserDataStore
import com.ihsanfaiz0048.asessment3.ui.theme.MainGreen
import com.ihsanfaiz0048.asessment3.ui.theme.TextGreen
import com.ihsanfaiz0048.asessment3.util.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryDetail(navController: NavHostController, id: Long){
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: HistoryDetailViewModel = viewModel(factory = factory)
    val dataStoreUser = UserDataStore(context)
    val user by dataStoreUser.userFlow.collectAsState(User())
    val status by viewModel.status.collectAsState()

    var nama by remember { mutableStateOf("") }
    var totalHarga by remember { mutableIntStateOf(0) }
    var quantity by remember { mutableIntStateOf(0) }
    var harga by remember { mutableIntStateOf(0) }
    var catatan: String? by remember { mutableStateOf("") }
    var showAlert by remember { mutableStateOf(false) }
    var kategori by remember { mutableStateOf("") }
    var menuId by remember { mutableIntStateOf(0) }
    var orderId by remember { mutableIntStateOf(0) }
    var isReviewed by remember { mutableStateOf(false) }


    LaunchedEffect(user.email) {
        if (user.email.isEmpty()) return@LaunchedEffect
        val data = viewModel.getDetailHistory(user.email, id.toInt()) ?: return@LaunchedEffect

        orderId = data.order.id
        menuId = data.menu.id
        nama = data.menu.nama
        kategori = data.menu.kategori
        harga = data.menu.harga
        totalHarga = data.order.totalBayar
        quantity = data.order.quantity
        catatan = data.order.catatan

        isReviewed = viewModel.checkIfOrderReviewed(orderId, user.email)
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
            orderId,
            harga,
            catatan,
            nama,
            kategori,
            totalHarga,
            quantity,
            user.email,
            onShowAlert = {
                showAlert = true
            },
            status,
            viewModel,
            menuId,
            isReviewed,
            onReviewSuccess = { isReviewed = true }
        )
        if (showAlert){
            DisplayAlertDialog(onDismissRequest = {showAlert = false}) {
                showAlert = false
                viewModel.deleteOrder(user.email, id.toInt())
                navController.popBackStack()
            }
        }
    }
}

@Composable
fun HistoryDetailContent(
    modifier: Modifier = Modifier,
    orderId: Int,
    harga: Int,
    catatan: String?,
    nama: String,
    kategori: String,
    totalHarga: Int,
    quantity: Int,
    userId: String,
    onShowAlert: () -> Unit,
    status: ApiStatus,
    viewModel: HistoryDetailViewModel,
    menuId: Int,
    isReviewed: Boolean,
    onReviewSuccess: () -> Unit
){
    val grayColor = if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray

    when(status){
        ApiStatus.IDLE -> {}
        ApiStatus.LOADING -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.MainGreen)
            }
        }

        ApiStatus.SUCCESS -> {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = modifier
                    .padding(16.dp)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
            ) {
                Column{
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, grayColor, RoundedCornerShape(16.dp))
                            .background(
                                MaterialTheme.colorScheme.surface,
                                RoundedCornerShape(16.dp)
                            )
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
                            .background(
                                MaterialTheme.colorScheme.surface,
                                RoundedCornerShape(16.dp)
                            )
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
                                .padding(top = 16.dp)
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
                    if (!isReviewed){
                        ReviewForm(kategori, userId, viewModel, menuId, orderId, onReviewSuccess)
                    }else{
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Anda sudah memberikan ulasan untuk pesanan ini ✨",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        ApiStatus.FAILED -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
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
                }
            }
        }
    }
}


@Composable
fun ReviewForm(kategori: String, userId: String, viewModel: HistoryDetailViewModel, menuId: Int, orderId: Int, onReviewSuccess: () -> Unit){
    var review by remember { mutableStateOf("") }
    var star by remember { mutableIntStateOf(0) }
    val context = LocalContext.current
    val uploadStatus by viewModel.uploadStatus.collectAsState()

    var bitmap: Bitmap? by remember { mutableStateOf(null) }
    val launcher = rememberLauncherForActivityResult(CropImageContract()) {
        bitmap = getCroppedImage(context.contentResolver, it)
        if (bitmap != null) {
            Toast.makeText(context, "Foto berhasil diupload", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(uploadStatus) {
        if (uploadStatus == ApiStatus.SUCCESS) {
            onReviewSuccess()
        } else if (uploadStatus == ApiStatus.FAILED) {
            Toast.makeText(context, "Gagal mengirim review, coba lagi", Toast.LENGTH_SHORT).show()
        }
    }


    Column(
        modifier = Modifier
            .padding(top = 32.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.gimana, kategori),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge
        )
        RatingBar(
            onRatingSelected = {nilaiRating ->
                star = nilaiRating
            }
        )
        Text(
            text = stringResource(R.string.label_review),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 16.dp)
        )
        OutlinedTextField(
            value = review,
            onValueChange = { review = it},
            placeholder = { Text(text = stringResource(R.string.barista)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            shape = RoundedCornerShape(16.dp)
        )
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            bitmap?.let { croppedBitmap ->
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, Color.LightGray, RoundedCornerShape(16.dp)),
                ) {
                    Image(
                        bitmap = croppedBitmap.asImageBitmap(),
                        contentDescription = "Preview Foto Review",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    val options = CropImageContractOptions(
                        null, CropImageOptions(
                            imageSourceIncludeGallery = false,
                            imageSourceIncludeCamera = true,
                            fixAspectRatio = true
                        )
                    )
                    launcher.launch(options)
                },
                modifier = Modifier.padding(end = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.MainGreen,
                    contentColor = Color.TextGreen
                ),
            ) {
                Text(
                    text = stringResource(R.string.ambil_foto)
                )
            }
            Button(
                onClick = {
                    if (review.trim().isNotEmpty() && star != 0){
                        viewModel.insertReview(
                            review,
                            star,
                            menuId,
                            userId,
                            bitmap,
                            orderId
                        )
                    }else{
                        Toast.makeText(context, "Review atau bintang tidak boleh kosong", Toast.LENGTH_SHORT).show()
                    }
                },
                enabled = uploadStatus != ApiStatus.LOADING,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.MainGreen,
                    contentColor = Color.TextGreen
                ),
                modifier = Modifier
            ) {
                if (uploadStatus == ApiStatus.LOADING) {
                    CircularProgressIndicator(color = Color.TextGreen, modifier = Modifier.size(20.dp))
                } else {
                    Text(text = stringResource(R.string.kirim))
                }
            }
        }
    }
}

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    onRatingSelected: (Int) -> Unit
) {
    var rating by remember { mutableStateOf(0) }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(5) { index ->
            val starNumber = index + 1

            val starColor = if (starNumber <= rating) Color(0xFFFFD700) else Color.LightGray

            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "Bintang ke-$starNumber",
                tint = starColor,
                modifier = Modifier
                    .size(40.dp)
                    .clickable {
                        rating = starNumber
                        onRatingSelected(starNumber)
                    }
            )
        }
    }
}

private fun getCroppedImage(
    resolver: ContentResolver,
    result: CropImageView.CropResult
): Bitmap? {
    if (!result.isSuccessful){
        Log.e("IMAGE", "Error: ${result.error}")
        return null
    }

    val uri = result.uriContent ?: return null

    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P){
        MediaStore.Images.Media.getBitmap(resolver, uri)
    } else {
        val source = ImageDecoder.createSource(resolver, uri)
        ImageDecoder.decodeBitmap(source)
    }
}