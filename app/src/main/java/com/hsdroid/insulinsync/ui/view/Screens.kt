package com.hsdroid.insulinsync.ui.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ParagraphStyle
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
import androidx.navigation.NavHostController
import com.hsdroid.insulinsync.R
import com.hsdroid.insulinsync.models.Insulin
import com.hsdroid.insulinsync.models.Profile
import com.hsdroid.insulinsync.ui.theme.Background
import com.hsdroid.insulinsync.ui.theme.Pink
import com.hsdroid.insulinsync.ui.theme.nasteFontFamily
import com.hsdroid.insulinsync.ui.viewmodel.InsulinViewModel
import com.hsdroid.insulinsync.utils.ApiState
import com.hsdroid.insulinsync.utils.Helper.Companion.formatDosageTime
import com.vsnappy1.timepicker.TimePicker
import com.vsnappy1.timepicker.ui.model.TimePickerConfiguration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavHostController, insulinViewModel: InsulinViewModel) {
    val coroutineScope = rememberCoroutineScope()
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
                            coroutineScope.launch(Dispatchers.IO) {
                                insulinViewModel.addDataToProfile(Profile(name))
                                withContext(Dispatchers.Main) {
                                    navController.navigate("home/$name")
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
fun HomeScreen(
    navController: NavHostController,
    insulinViewModel: InsulinViewModel,
    receivedUname: String
) {

    val context = LocalContext.current
    val listData = insulinViewModel._response.collectAsState().value
    val showAddAlert = remember {
        mutableStateOf(false)
    }

    var getAllInsulinData = remember {
        listOf<Insulin>()
    }

    when (listData) {
        is ApiState.SUCCESS -> getAllInsulinData = listData.data
        else -> Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Pink)
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .weight(2.5f),
            colors = CardDefaults.cardColors(containerColor = Pink)
        ) {
            Text(
                text = buildAnnotatedString {
                    append("Your drug\ncabinet")
                    withStyle(style = ParagraphStyle(lineHeight = 5.sp)) {
                        withStyle(style = SpanStyle(fontSize = 54.sp)) {
                            append("$receivedUname 😁")
                        }
                    }
                },
                lineHeight = 50.sp,
                fontFamily = nasteFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(18.dp, top = 40.dp),
                textAlign = TextAlign.Start
            )
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

                Text(text = "Your Insulin's",
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
                LazyColumn(
                    Modifier
                        .padding(bottom = 100.dp)
                        .constrainAs(listLazyColumn) {
                            start.linkTo(parent.start)
                            top.linkTo(insulinLogTxt.bottom)
                            end.linkTo(parent.end)
                        }, verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(getAllInsulinData) {
                        insulinCard(it, insulinViewModel)
                    }
                }

            }
        }

    }

    if (showAddAlert.value) {
        AddAlertDialog(showAddAlert, insulinViewModel, receivedUname)
    }
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
                        text = list.dosageTime,
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
                        DropdownMenuItem(text = { Text(text = "Delete") },
                            onClick = {
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
    showAddAlert: MutableState<Boolean>,
    insulinViewModel: InsulinViewModel,
    receivedUname: String
) {

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
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = nasteFontFamily
                ),
                value = insulinName,
                onValueChange = { insulinName = it },
                label = {
                    Text(
                        text = "Enter Insulin name",
                        fontFamily = nasteFontFamily,
                        fontWeight = FontWeight.Light
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
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
                    insulinTotalUnit.toInt().toString() + " Units"
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
                    dosageUnit.toInt().toString() + " Units"
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
                        coroutineScope.launch(Dispatchers.IO) {
                            insulinViewModel.addData(
                                Insulin(
                                    receivedUname,
                                    insulinName,
                                    insulinTotalUnit,
                                    insulinTotalUnit,
                                    dosageUnit,
                                    formatDosageTime(dosageTime)
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