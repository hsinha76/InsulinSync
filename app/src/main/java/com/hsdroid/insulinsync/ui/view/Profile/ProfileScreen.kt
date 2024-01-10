package com.hsdroid.insulinsync.ui.view.Profile

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.hsdroid.insulinsync.models.Profile
import com.hsdroid.insulinsync.ui.theme.Background
import com.hsdroid.insulinsync.ui.theme.Pink
import com.hsdroid.insulinsync.ui.theme.Red
import com.hsdroid.insulinsync.ui.theme.nasteFontFamily
import com.hsdroid.insulinsync.ui.view.Common.showCircularProgress
import com.hsdroid.insulinsync.ui.viewmodel.InsulinViewModel
import com.hsdroid.insulinsync.utils.ApiState

@Composable
fun ProfileScreen(
    navController: NavHostController, insulinViewModel: InsulinViewModel) {

    val context = LocalContext.current

    var profileData by remember {
        mutableStateOf(emptyList<Profile>())
    }

    var loadData by remember {
        mutableStateOf(false)
    }

    val isProgress = remember {
        mutableStateOf(true)
    }

    LaunchedEffect(true) {
        insulinViewModel._profileResponse.collect {
            when (it) {
                is ApiState.SUCCESS -> if (it.data.isEmpty()) {
                    navController.navigate("register") {
                        popUpTo(0)
                    }
                } else {
                    profileData = it.data
                    loadData = true
                    isProgress.value = false
                    insulinViewModel.refreshWorkManager()
                }

                is ApiState.FAILURE -> Toast.makeText(
                    context, "Something went wrong", Toast.LENGTH_SHORT
                ).show()

                else -> ""
            }
        }
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {

        val (userView, newUserTxt) = createRefs()

        Box(modifier = Modifier
            .wrapContentSize()
            .constrainAs(userView) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                bottom.linkTo(newUserTxt.bottom)
            }) {

            if (isProgress.value) {
                showCircularProgress()
            }

            if (loadData) {
                lazygrid(profileData, navController)
            }
        }


        Text(text = buildAnnotatedString {
            append("New User? ")
            withStyle(
                SpanStyle(
                    color = Pink, fontFamily = nasteFontFamily, fontWeight = FontWeight.Medium
                )
            ) {
                append("Register here.")
            }
        },
            fontFamily = nasteFontFamily,
            fontSize = 18.sp,
            color = Color.White,
            fontWeight = FontWeight.Light,
            modifier = Modifier
                .clickable {
                    navController.navigate("register")
                }
                .constrainAs(newUserTxt) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom, margin = 18.dp)
                })
    }

    BackHandler(enabled = true, onBack = {
        val activity = context as Activity
        activity.finish()
    })
}

@Composable
fun lazygrid(profileData: List<Profile>, navController: NavHostController) {

    LazyVerticalGrid(modifier = Modifier
        .padding(horizontal = 8.dp)
        .height(400.dp),
        columns = GridCells.Adaptive(150.dp),
        content = {
            items(profileData) {
                ProfileCard(it.name, navController)
            }
        })

}

@Composable
private fun ProfileCard(uname: String, navController: NavHostController) {

    Box(modifier = Modifier
        .height(200.dp)
        .width(IntrinsicSize.Max)
        .padding(8.dp)
        .background(Color.White, CircleShape)
        .clickable {
            navController.navigate("home/$uname")
        }) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        ) {

            val (userImg, userName) = createRefs()

            Image(imageVector = Icons.Default.Person,
                contentDescription = null,
                colorFilter = ColorFilter.tint(Red),
                modifier = Modifier
                    .size(108.dp)
                    .constrainAs(userImg) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                    })

            Text(text = uname,
                fontSize = 18.sp,
                color = Color.Black,
                fontFamily = nasteFontFamily,
                fontWeight = FontWeight.Light,
                modifier = Modifier.constrainAs(userName) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(userImg.bottom)
                })

        }
    }

}

