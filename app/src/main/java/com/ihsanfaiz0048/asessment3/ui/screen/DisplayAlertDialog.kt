package com.ihsanfaiz0048.asessment3.ui.screen


import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ihsanfaiz0048.asessment3.R
import com.ihsanfaiz0048.asessment3.ui.theme.Asessment3Theme
import com.ihsanfaiz0048.asessment3.ui.theme.MainGreen

@Composable
fun DisplaySuccessDialog(
    onConfirmation: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            onConfirmation()
        },
        icon = {
            Icon(
                imageVector = Icons.Outlined.CheckCircleOutline,
                contentDescription = "",
                tint = Color.MainGreen,
                modifier = Modifier
                    .size(100.dp)
            )
        },
        text = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = stringResource(R.string.pesanan_diterima),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },
        confirmButton = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {onConfirmation()},
                    shape = RoundedCornerShape(100.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.MainGreen
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.ok))
                }
            }
        }
    )
}

@Composable
fun DisplayUpdateDialog(
    onConfirmation: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            onConfirmation()
        },
        icon = {
            Icon(
                imageVector = Icons.Outlined.CheckCircleOutline,
                contentDescription = "",
                tint = Color.MainGreen,
                modifier = Modifier
                    .size(100.dp)
            )
        },
        text = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = stringResource(R.string.update_berhasil),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },
        confirmButton = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {onConfirmation()},
                    shape = RoundedCornerShape(100.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.MainGreen
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.ok))
                }
            }
        }
    )
}

@Composable
fun DisplayAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    AlertDialog(
        icon = {
            Icon(
                imageVector = Icons.Outlined.Cancel,
                contentDescription = "",
                tint = Color.Red,
                modifier = Modifier
                    .size(100.dp)
            )
        },
        text = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = stringResource(R.string.pesan_hapus),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },
        confirmButton = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { onDismissRequest() },
                    shape = RoundedCornerShape(100.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Gray
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(R.string.tombol_batal))
                }

                Button(
                    onClick = { onConfirmation() },
                    shape = RoundedCornerShape(100.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(R.string.tombol_hapus))
                }
            }
        },
        onDismissRequest = {onDismissRequest()}
    )
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DialogPreview() {
    Asessment3Theme(){
        DisplayAlertDialog(
            onDismissRequest = {},
            onConfirmation = {}
        )
    }
}