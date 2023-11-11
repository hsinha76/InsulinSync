package com.hsdroid.insulinsync.ui.view

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.hsdroid.insulinsync.R
import com.hsdroid.insulinsync.models.Insulin
import com.hsdroid.insulinsync.models.Profile
import com.hsdroid.insulinsync.ui.theme.Background
import com.hsdroid.insulinsync.ui.theme.Pink
import com.hsdroid.insulinsync.ui.theme.Red
import com.hsdroid.insulinsync.ui.theme.nasteFontFamily
import com.hsdroid.insulinsync.ui.viewmodel.InsulinViewModel
import com.hsdroid.insulinsync.utils.ApiState
import com.hsdroid.insulinsync.utils.Helper.Companion.formatDosageTime
import com.hsdroid.insulinsync.utils.Helper.Companion.storeTimeinMiliseconds
import com.vsnappy1.timepicker.TimePicker
import com.vsnappy1.timepicker.ui.model.TimePickerConfiguration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavHostController, insulinViewModel: InsulinViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var name by remember {
        mutableStateOf("")
    }

    var showContinueBtn by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
        contentAlignment = Alignment.Center
    ) {
        ConstraintLayout(
            modifier = Modifier.wrapContentSize()
        ) {

            val (headerText, etName, continueBtn) = createRefs()

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 10.dp)
                    .constrainAs(headerText) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                    },
                text = "What's your name?",
                fontSize = 30.sp,
                textAlign = TextAlign.Start,
                color = Color.White,
                fontFamily = nasteFontFamily,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it.also {
                        if (it.length > 1) {
                            showContinueBtn = true
                        } else {
                            showContinueBtn = false
                        }
                    }
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.White, textColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .constrainAs(etName) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(headerText.bottom)
                    },
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )

            Box(modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 20.dp)
                .size(48.dp)
                .constrainAs(continueBtn) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(etName.bottom)
                }) {

                if (showContinueBtn) {
                    Button(
                        onClick = {
                            if (name.isEmpty()) {
                                Toast.makeText(
                                    context, "Please enter name to continue", Toast.LENGTH_SHORT
                                ).show()
                                return@Button
                            }

                            var usernameExists = false

                            coroutineScope.launch(Dispatchers.IO) {
                                if (insulinViewModel.checkUsernameExists(name)) {
                                    usernameExists = true
                                }

                                if (usernameExists) {
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(
                                            context, "User already exists.", Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    insulinViewModel.addDataToProfile(Profile(name))
                                    withContext(Dispatchers.Main) {
                                        navController.navigate("home/$name")
                                    }

                                }
                            }
                        },
                        shape = CircleShape,
                        modifier = Modifier.size(48.dp),
                        contentPadding = PaddingValues(1.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Pink)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = Color.Red
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(
    navController: NavHostController, insulinViewModel: InsulinViewModel = hiltViewModel()
) {

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

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavHostController,
    insulinViewModel: InsulinViewModel = hiltViewModel(),
    receivedUname: String
) {

    val context = LocalContext.current
    val showAddAlert = remember {
        mutableStateOf(false)
    }

    var getAllInsulinData by remember {
        mutableStateOf(emptyList<Insulin>())
    }

    val isProgress = remember {
        mutableStateOf(true)
    }

    val isListEmpty = remember {
        mutableStateOf(false)
    }

    val showLazyColumn = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(true) {
        insulinViewModel.getAllData(receivedUname)

        delay(1000)

        insulinViewModel._response.collect {
            when (it) {
                is ApiState.SUCCESS -> if (it.data.isEmpty()) {
                    isProgress.value = false
                    isListEmpty.value = true
                    showLazyColumn.value = false
                } else {
                    getAllInsulinData = it.data
                    isListEmpty.value = false
                    isProgress.value = false
                    showLazyColumn.value = true
                }

                is ApiState.FAILURE -> {
                    Toast.makeText(
                        context, "Something went wrong!", Toast.LENGTH_SHORT
                    ).show()
                    isProgress.value = false
                }

                else -> "do nothing"
            }
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
            title = {
                Text(
                    text = "Home",
                    color = Color.Black,
                    fontFamily = nasteFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Pink),
            navigationIcon = {
                Icon(imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        navController.navigate("profile")
                    })
            })
    }, content = {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Pink)
        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .weight(3f),
                colors = CardDefaults.cardColors(containerColor = Pink)
            ) {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(Red),
                        modifier = Modifier
                            .size(128.dp)
                            .padding(top = 38.dp)
                    )
                    Text(
                        text = buildAnnotatedString {
                            append("Welcome back, ")
                            withStyle(
                                SpanStyle(
                                    fontFamily = nasteFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = Color.Black
                                )
                            ) {
                                append("$receivedUname 😎")
                            }
                        },
                        fontFamily = nasteFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        color = Color.Black,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Card(
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .weight(5f),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                ConstraintLayout(modifier = Modifier.fillMaxSize()) {

                    val (insulinLogTxt, listLazyColumn, addBtn) = createRefs()

                    Text(text = "Your Insulin",
                        fontFamily = nasteFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 22.sp,
                        modifier = Modifier
                            .padding(horizontal = 14.dp, vertical = 18.dp)
                            .constrainAs(insulinLogTxt) {
                                start.linkTo(parent.start)
                                top.linkTo(parent.top)
                            })

                    Button(onClick = {
                        showAddAlert.value = true
                    },
                        shape = CircleShape,
                        modifier = Modifier
                            .size(48.dp)
                            .constrainAs(addBtn) {
                                end.linkTo(parent.end)
                                top.linkTo(parent.top)
                            }
                            .padding(top = 16.dp, end = 14.dp),
                        contentPadding = PaddingValues(1.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Pink)) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = Color.Red
                        )
                    }

                    Box(contentAlignment = Alignment.Center,
                        modifier = Modifier.constrainAs(listLazyColumn) {
                            start.linkTo(parent.start)
                            top.linkTo(insulinLogTxt.bottom)
                            end.linkTo(parent.end)
                        }) {

                        if (isProgress.value) {
                            showCircularProgress()
                        }

                        if (isListEmpty.value) {
                            showEmptyText()
                        }

                        if (showLazyColumn.value) {
                            LazyColumn(
                                Modifier.padding(bottom = 100.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(getAllInsulinData) {
                                    insulinCard(it, insulinViewModel)
                                }
                            }
                        }
                    }


                }
            }

        }
    })

    if (showAddAlert.value) {
        AddAlertDialog(showAddAlert, insulinViewModel, receivedUname)
    }

    BackHandler(enabled = true, onBack = {
        navController.navigate("profile")
    })
}

@Composable
private fun insulinCard(list: Insulin, insulinViewModel: InsulinViewModel) {

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var expandedState by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(start = 18.dp, end = 18.dp)
            .height(200.dp),
        colors = CardDefaults.cardColors(containerColor = Background)
    ) {

        ConstraintLayout(modifier = Modifier.padding(18.dp)) {

            val (space, icon, details, progress, options) = createRefs()

            Spacer(modifier = Modifier
                .height(14.dp)
                .constrainAs(space) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                })

            Image(painter = painterResource(id = R.drawable.vaccine),
                contentDescription = null,
                modifier = Modifier
                    .size(62.dp)
                    .constrainAs(icon) {
                        start.linkTo(parent.start)
                        top.linkTo(space.bottom)
                    })

            Column(
                Modifier
                    .padding(start = 12.dp)
                    .constrainAs(details) {
                        start.linkTo(icon.end)
                        top.linkTo(parent.top)
                    }) {

                Text(
                    text = list.insulinName,
                    fontFamily = nasteFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp,
                    color = Color.White,
                    maxLines = 1
                )

                Text(
                    text = "${list.dosageUnit.toInt()} units/Day",
                    fontFamily = nasteFontFamily,
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp,
                    color = Color.LightGray
                )

                Row(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(top = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        painter = painterResource(id = R.drawable.baseline_access_time_filled_24),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.White
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = formatDosageTime(list.dosageTime),
                        fontFamily = nasteFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp,
                        color = Color.LightGray
                    )
                }
            }

            Box(modifier = Modifier.constrainAs(options) {
                end.linkTo(parent.end)
                top.linkTo(parent.top)
            }) {
                IconButton(onClick = { expandedState = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null,
                        tint = Color.White
                    )
                }

                if (expandedState) {
                    DropdownMenu(
                        expanded = expandedState,
                        onDismissRequest = { expandedState = false },
                        modifier = Modifier
                            .width(200.dp)
                            .background(
                                Color.White
                            )
                    ) {
                        DropdownMenuItem(text = { Text(text = "Delete") }, onClick = {
                            coroutineScope.launch(Dispatchers.IO) {
                                insulinViewModel.deleteData(list)

                                withContext(Dispatchers.Main) {
                                    expandedState = false
                                }

                            }
                        })
                    }
                }
            }

            Column(modifier = Modifier
                .padding(vertical = 50.dp)
                .constrainAs(progress) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(details.bottom)
                }) {
                Text(
                    text = "${list.quantityTotal.toInt()} / ${list.quantityLeft?.toInt()}",
                    fontFamily = nasteFontFamily,
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    color = Color.LightGray,
                    modifier = Modifier.fillMaxWidth()
                )

                LinearProgressIndicator(
                    progress = 0f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(14.dp)
                        .padding(horizontal = 14.dp, vertical = 4.dp)
                )
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddAlertDialog(
    showAddAlert: MutableState<Boolean>, insulinViewModel: InsulinViewModel, receivedUname: String
) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var insulinName by remember {
        mutableStateOf("")
    }
    var insulinTotalUnit by remember {
        mutableFloatStateOf(0f)
    }
    var dosageUnit by remember {
        mutableFloatStateOf(0f)
    }
    var dosageTime by remember {
        mutableStateOf("")
    }

    AlertDialog(onDismissRequest = { /*TODO*/ }, confirmButton = { /*TODO*/ }, title = {
        Column(verticalArrangement = Arrangement.Center) {
            OutlinedTextField(modifier = Modifier.fillMaxWidth(), textStyle = TextStyle(
                fontSize = 16.sp, fontWeight = FontWeight.Medium, fontFamily = nasteFontFamily
            ), value = insulinName, onValueChange = { insulinName = it }, label = {
                Text(
                    text = "Enter Insulin name",
                    fontFamily = nasteFontFamily,
                    fontWeight = FontWeight.Light
                )
            }, singleLine = true, keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )
            Text(
                text = "Select your time",
                fontSize = 16.sp,
                fontFamily = nasteFontFamily,
                fontWeight = FontWeight.Light
            )

            TimePicker(
                is24Hour = false,
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth(),
                onTimeSelected = { hour, minute ->
                    dosageTime = "$hour:$minute"
                },
                configuration = TimePickerConfiguration.Builder()
                    .numberOfTimeRowsDisplayed(count = 1)
                    .selectedTimeScaleFactor(scaleFactor = 1.4f).selectedTimeTextStyle(
                        TextStyle(
                            fontFamily = nasteFontFamily,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    ).build()
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )

            Text(
                text = if (insulinTotalUnit.toInt() >= 0.5) {
                    insulinTotalUnit.toInt().toString() + " Units (Total units)"
                } else "Total units",
                fontSize = 16.sp,
                fontFamily = nasteFontFamily,
                fontWeight = FontWeight.Light
            )
            Slider(
                value = insulinTotalUnit,
                onValueChange = { insulinTotalUnit = it.roundToInt().toFloat() },
                valueRange = 0f..500f
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(18.dp)
            )
            Text(
                text = if (dosageUnit.toInt() >= 0.5) {
                    dosageUnit.toInt().toString() + " Units (Per day)"
                } else "Dosage unit (Per day)",
                fontSize = 16.sp,
                fontFamily = nasteFontFamily,
                fontWeight = FontWeight.Light
            )
            Slider(
                value = dosageUnit,
                onValueChange = { dosageUnit = it.roundToInt().toFloat() },
                valueRange = 0f..150f
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(18.dp)
            )

            Row {
                OutlinedButton(
                    onClick = { showAddAlert.value = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Text(text = "Cancel")
                }

                Spacer(modifier = Modifier.width(6.dp))
                OutlinedButton(
                    onClick = {

                        if (insulinName.isEmpty() || insulinTotalUnit <= 1 || dosageUnit <= 1) {
                            Toast.makeText(
                                context, "Please enter all the details", Toast.LENGTH_SHORT
                            ).show()
                            return@OutlinedButton
                        }

                        if (insulinTotalUnit < dosageUnit) {
                            Toast.makeText(
                                context,
                                "Total unit should be less than dosage unit",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@OutlinedButton
                        }
                        coroutineScope.launch(Dispatchers.IO) {
                            insulinViewModel.addData(
                                Insulin(
                                    null,
                                    receivedUname,
                                    insulinName,
                                    insulinTotalUnit,
                                    insulinTotalUnit,
                                    dosageUnit,
                                    storeTimeinMiliseconds(dosageTime)
                                )
                            )

                            withContext(Dispatchers.Main) {
                                showAddAlert.value = false
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Background)
                ) {
                    Text(text = "Add")
                }

            }
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

@Composable
private fun showCircularProgress() {
    CircularProgressIndicator(modifier = Modifier.size(48.dp))
}

@Composable
private fun showEmptyText() {

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.empty_anim))

    Column(
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
    ) {

        LottieAnimation(
            modifier = Modifier.size(250.dp),
            composition = composition,
            iterations = LottieConstants.IterateForever
        )

        Text(
            text = "There's nothing here 😢",
            fontWeight = FontWeight.Light,
            fontFamily = nasteFontFamily,
            fontSize = 14.sp
        )

    }
}