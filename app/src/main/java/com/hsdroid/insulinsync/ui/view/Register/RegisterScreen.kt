package com.hsdroid.insulinsync.ui.view.Register

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.hsdroid.insulinsync.models.Profile
import com.hsdroid.insulinsync.ui.theme.Background
import com.hsdroid.insulinsync.ui.theme.Pink
import com.hsdroid.insulinsync.ui.theme.nasteFontFamily
import com.hsdroid.insulinsync.ui.viewmodel.InsulinViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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