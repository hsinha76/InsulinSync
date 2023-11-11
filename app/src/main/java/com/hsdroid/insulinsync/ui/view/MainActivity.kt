package com.hsdroid.insulinsync.ui.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.hsdroid.insulinsync.ui.theme.InsulinSyncTheme
import com.hsdroid.insulinsync.ui.viewmodel.InsulinViewModel
import com.hsdroid.insulinsync.utils.DbWorker
import com.hsdroid.insulinsync.utils.Helper
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val insulinViewModel: InsulinViewModel by viewModels()
    private var isSplash = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition() { isSplash }

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            isSplash = false
        }, 1000)

        val myWorker = PeriodicWorkRequestBuilder<DbWorker>(2, TimeUnit.HOURS).build()
        WorkManager.getInstance(applicationContext).enqueue(myWorker)

        setContent {
            InsulinSyncTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    navGraph()
                }
            }
        }
    }

    @Composable
    fun navGraph() {
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = "profile") {
            composable("profile") { ProfileScreen(navController, insulinViewModel) }
            composable("register") { RegisterScreen(navController, insulinViewModel) }
            composable(
                "home/{uname}",
                arguments = listOf(navArgument("uname") { type = NavType.StringType })
            ) {
                val receivedUname = it.arguments?.getString("uname")
                if (receivedUname != null) {
                    HomeScreen(navController, insulinViewModel, receivedUname)
                }
            }
        }
    }
}