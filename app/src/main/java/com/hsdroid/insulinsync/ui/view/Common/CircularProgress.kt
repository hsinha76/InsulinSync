package com.hsdroid.insulinsync.ui.view.Common

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun showCircularProgress() {
    CircularProgressIndicator(modifier = Modifier.size(48.dp))
}