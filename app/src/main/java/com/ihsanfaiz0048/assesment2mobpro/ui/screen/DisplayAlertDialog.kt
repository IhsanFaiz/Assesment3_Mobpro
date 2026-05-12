//package com.ihsanfaiz0048.assesment2mobpro.ui.screen
//
//
//import androidx.compose.material3.AlertDialog
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextButton
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.res.stringResource
//import com.ihsanfaiz0048.assesment2mobpro.R
//
//@Composable
//fun DisplayAlertDialog(
//    onDismissRequest: () -> Unit,
//    onConfirmation: () -> Unit
//) {
//    AlertDialog(
//        text = {Text(text = stringResource(R.string.pesan_hapus))},
//        confirmButton = {
//            TextButton(onClick = {onConfirmation()}) {
//                Text(text = stringResource(R.string.tombol_hapus))
//            }
//        },
//        dismissButton = {
//            TextButton(onClick = {onDismissRequest()} ) {
//                Text(text = stringResource(R.string.tombol_batal))
//            }
//        },
//        onDismissRequest = {onDismissRequest()}
//    )
//}