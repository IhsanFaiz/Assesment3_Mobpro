package com.ihsanfaiz0048.asessment3.ui.screen

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.outlined.SettingsInputComponent
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.ClearCredentialException
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.ihsanfaiz0048.asessment3.BuildConfig
import com.ihsanfaiz0048.asessment3.model.Menu
import com.ihsanfaiz0048.asessment3.navigation.Screen
import com.ihsanfaiz0048.asessment3.R
import com.ihsanfaiz0048.asessment3.model.User
import com.ihsanfaiz0048.asessment3.network.ApiStatus
import com.ihsanfaiz0048.asessment3.network.UserDataStore
import com.ihsanfaiz0048.asessment3.ui.theme.Asessment3Theme
import com.ihsanfaiz0048.asessment3.ui.theme.MainGreen
import com.ihsanfaiz0048.asessment3.ui.theme.TextGreen
import com.ihsanfaiz0048.asessment3.util.SettingsDataStore
import com.ihsanfaiz0048.asessment3.util.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController){
    val dataStore = SettingsDataStore(LocalContext.current)
    val showList by dataStore.layoutFlow.collectAsState(true)
    val context = LocalContext.current
    val dataStoreUser = UserDataStore(context)
    var showDialog by remember { mutableStateOf(false) }
    val user by dataStoreUser.userFlow.collectAsState(User())

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Coffee,
                            contentDescription = "",
                            tint = Color.TextGreen,
                            modifier = Modifier.size(30.dp)
                        )
                        Text(
                            text = stringResource(id = R.string.app_name),
                            fontWeight = FontWeight.ExtraBold,
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.MainGreen,
                    titleContentColor = Color.TextGreen
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
                            tint = Color.TextGreen
                        )
                    }
                    IconButton(onClick = {
                        if (user.email.isEmpty()){
                            CoroutineScope(Dispatchers.IO).launch{
                                signIn(
                                    context,
                                    dataStoreUser
                                )
                            }
                        }
                        else{
                            navController.navigate(Screen.HistoryScreen.route)
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ReceiptLong,
                            contentDescription = stringResource(R.string.history),
                            tint = Color.TextGreen
                        )
                    }
                    IconButton(onClick = {
                        if (user.email.isEmpty()){
                            CoroutineScope(Dispatchers.IO).launch{signIn(context, dataStoreUser)}
                        } else {
                            showDialog = true
                        }
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_account_circle_24),
                            contentDescription = stringResource(R.string.profil),
                            tint = Color.TextGreen
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        ScreenContent(showList, Modifier.padding(innerPadding),user.email, navController)
        if (showDialog){
            ProfileDialog(
                user = user,
                onDismissRequest = {showDialog = false}
            ){
                CoroutineScope(Dispatchers.IO).launch { signOut(context, dataStoreUser) }
                showDialog = false
            }
        }
    }
}

@Composable
fun ScreenContent(showList: Boolean,modifier: Modifier = Modifier, userId: String, navController: NavHostController){
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: MainViewModel = viewModel(factory = factory)
    val data by viewModel.data.collectAsState()
    val dataMakanan by viewModel.dataMakanan.collectAsState()
    val dataMinuman by viewModel.dataMinuman.collectAsState()
    val status by viewModel.status.collectAsState()
    var filtered by remember { mutableStateOf("Semua") }

    if (status == ApiStatus.LOADING) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color.MainGreen)
        }
    } else if (data.isEmpty()){
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
                    CategoryButtons { filtered = it }
                }
                when (filtered) {
                    "Makanan" -> {
                        items(dataMakanan) {
                            ListItem(menu = it) {
                                navController.navigate(Screen.DetailMenu.create(it.id.toLong()))
                            }
                            HorizontalDivider()
                        }
                    }
                    "Minuman" -> {
                        items(dataMinuman) {
                            ListItem(menu = it) {
                                navController.navigate(Screen.DetailMenu.create(it.id.toLong()))
                            }
                            HorizontalDivider()
                        }
                    }
                    else -> {
                        item {
                            Text(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 23.dp),
                                text = stringResource(R.string.minuman),
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }
                        items(dataMinuman) {
                            ListItem(menu = it) {
                                navController.navigate(Screen.DetailMenu.create(it.id.toLong()))
                            }
                            HorizontalDivider()
                        }
                        item {
                            Text(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 23.dp),
                                text = stringResource(R.string.makanan),
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }
                        items(dataMakanan) {
                            ListItem(menu = it) {
                                navController.navigate(Screen.DetailMenu.create(it.id.toLong()))
                            }
                            HorizontalDivider()
                        }
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
                    CategoryButtons { filtered = it }
                }
                when (filtered) {
                    "Makanan" -> {
                        items(dataMakanan) {
                            GridItem(menu = it) {
                                navController.navigate(Screen.DetailMenu.create(it.id.toLong()))
                            }
                        }
                    }
                    "Minuman" -> {
                        items(dataMinuman) {
                            GridItem(menu = it) {
                                navController.navigate(Screen.DetailMenu.create(it.id.toLong()))
                            }
                        }
                    }
                    else -> {
                        items(data) {
                            GridItem(menu = it) {
                                navController.navigate(Screen.DetailMenu.create(it.id.toLong()))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryButtons(onCategorySelected: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = {onCategorySelected("Semua")},
            modifier = Modifier.padding(8.dp).border(2.dp, Color.MainGreen, RoundedCornerShape(100.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = Color.MainGreen
            )
        ) {
            Icon(
                imageVector = Icons.Outlined.SettingsInputComponent,
                contentDescription = ""
            )
        }
        Button(
            onClick = {onCategorySelected("Makanan")},
            modifier = Modifier.padding(8.dp).border(2.dp, Color.MainGreen, RoundedCornerShape(100.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = Color.MainGreen
            )
        ) {
            Text(text = stringResource(R.string.makanan), fontWeight = FontWeight.Bold)
        }
        Button(
            onClick = {onCategorySelected("Minuman")},
            modifier = Modifier.padding(8.dp).border(2.dp, Color.MainGreen, RoundedCornerShape(100.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = Color.MainGreen
            )
        ) {
            Text(text = stringResource(R.string.minuman), fontWeight = FontWeight.Bold)
        }
        Button(
            onClick = {onCategorySelected("Semua")},
            modifier = Modifier.padding(8.dp).border(2.dp, Color.MainGreen, RoundedCornerShape(100.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = Color.MainGreen
            )
        ) {
            Text(text = stringResource(R.string.semua), fontWeight = FontWeight.Bold)
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
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.width(200.dp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = "Rp " + formatHarga(menu.harga),
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Box(
                modifier = Modifier.size(100.dp)
            ){
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(menu.gambar)
                        .crossfade(true)
                        .build(),
                    contentDescription = menu.nama,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp))
                )
                Button(
                    onClick = {onClick()},
                    modifier = Modifier
                        .fillMaxWidth()
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
                        text = stringResource(R.string.pesan),
                    )
                }
            }
        }
    }
}

@Composable
fun GridItem(menu: Menu, onClick: () -> Unit){
    Card(
        modifier = Modifier.fillMaxWidth().clickable{onClick()},
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .height(100.dp)
                    .width(200.dp)
                    .clip(RoundedCornerShape(8.dp))
            ){
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(menu.gambar)
                        .crossfade(true)
                        .build(),
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
                text = "Rp " + formatHarga(menu.harga),
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Button(
                onClick = {onClick()},
                modifier = Modifier
                    .height(40.dp)
                    .border(3.dp, Color.MainGreen, RoundedCornerShape(100.dp)).fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    MaterialTheme.colorScheme.surface,
                    Color.MainGreen
                )
            ) {
                Text(
                    text = stringResource(R.string.pesan),
                )
            }
        }
    }
}

private suspend fun signIn(context: Context, dataStore: UserDataStore){
    val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(BuildConfig.API_KEY)
        .build()

    val request: GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    try {
        val credentialManager = CredentialManager.create(context)
        val result = credentialManager.getCredential(context, request)
        handleSignIn(result, dataStore)
    }catch (e: GetCredentialException){
        Log.e("SIGN-IN", "Error: ${e.errorMessage}" )
    }
}

private suspend fun handleSignIn(result: GetCredentialResponse, dataStore: UserDataStore){
    val credential = result.credential
    if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL){
        try {
            val googleId = GoogleIdTokenCredential.createFrom(credential.data)
            val nama = googleId.displayName ?: ""
            val email = googleId.id
            val photoUrl = googleId.profilePictureUri.toString()
            dataStore.saveData(User(nama, email, photoUrl))
        }catch (e: GoogleIdTokenParsingException){
            Log.e("SIGN-IN", "Error: ${e.message}")
        }
    }else{
        Log.e("SIGN-IN", "Error: unrecognized custom credential type.")
    }
}

private suspend fun signOut(context: Context, dataStore: UserDataStore) {
    try {
        val credentialManager = CredentialManager.create(context)

        credentialManager.clearCredentialState(
            ClearCredentialStateRequest()
        )

        dataStore.saveData(User())
    } catch (e: ClearCredentialException) {
        Log.e("SIGN-IN", "Error: ${e.errorMessage}")
    }
}


@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainScreenPreview() {
    Asessment3Theme(){
        MainScreen(rememberNavController())
    }
}