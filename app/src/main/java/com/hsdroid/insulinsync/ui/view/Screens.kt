package com.hsdroid.insulinsync.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.hsdroid.insulinsync.models.Insulin
import com.hsdroid.insulinsync.ui.theme.Background
import com.hsdroid.insulinsync.ui.theme.Pink
import com.hsdroid.insulinsync.ui.theme.nasteFontFamily
import com.hsdroid.insulinsync.ui.viewmodel.InsulinViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
                    .padding(8.dp)
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
                                val insulin = Insulin(name, null, null, null, null, null)
                                insulinViewModel.addData(insulin)

                                withContext(Dispatchers.Main) {
                                    navController.navigate("home")
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
fun HomeScreen(navController: NavHostController, insulinViewModel: InsulinViewModel) {

    val dummyList = listOf("Huminsulin", "Ryzodeg")

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
                            append("Harish 😁")
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
            ConstraintLayout {

                val (insulinLogTxt, listLazyColumn) = createRefs()

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

                LazyColumn(
                    Modifier
                        .padding(bottom = 100.dp)
                        .constrainAs(listLazyColumn) {
                            start.linkTo(parent.start)
                            top.linkTo(insulinLogTxt.bottom)
                            end.linkTo(parent.end)
                        }) {
                    items(dummyList) {
                        insulinCard(it)
                    }
                }

            }
        }

    }
}

@Composable
private fun insulinCard(list: String) {
    Card(
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(start = 18.dp, end = 18.dp, top = 14.dp)
            .height(220.dp),
        colors = CardDefaults.cardColors(containerColor = Background)
    ) {

        ConstraintLayout(modifier = Modifier.padding(18.dp)) {

            val (name, dosage, progress, quantity) = createRefs()

            Text(text = list,
                fontFamily = nasteFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                color = Color.White,
                maxLines = 1,
                modifier = Modifier.constrainAs(name) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                })

            Text(text = list,
                fontFamily = nasteFontFamily,
                fontWeight = FontWeight.Light,
                fontSize = 18.sp,
                color = Color.LightGray,
                modifier = Modifier.constrainAs(dosage) {
                    start.linkTo(parent.start)
                    top.linkTo(name.bottom)
                })


            Column(modifier = Modifier
                .padding(vertical = 70.dp)
                .constrainAs(progress) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(dosage.bottom)
                }) {
                Text(
                    text = "90/10",
                    fontFamily = nasteFontFamily,
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    color = Color.LightGray, modifier = Modifier.fillMaxWidth()
                )

                LinearProgressIndicator(
                    progress = 0f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 4.dp)
                )
            }

        }
    }
}