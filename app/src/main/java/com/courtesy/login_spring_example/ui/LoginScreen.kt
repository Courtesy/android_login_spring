package com.courtesy.login_spring_example.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.courtesy.login_spring_example.LoginViewModel
import com.courtesy.login_spring_example.R

private const val TAG = "LoginScreen"

@Composable
fun LoginScreen(emailLoginClick: () -> Unit, viewModel: LoginViewModel = hiltViewModel()) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 24.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val buttonWidth = 300.dp

        Spacer(modifier = Modifier.height(18.dp))

        if (viewModel.error.value.isNotBlank()) {
            ErrorField(viewModel)
        }
        SignInWithEmailButton(buttonWidth, emailLoginClick)
    }
}

@Composable
fun ErrorField(viewModel: LoginViewModel) {
    Text(
        text = viewModel.error.value,
        modifier = Modifier.fillMaxWidth(),
        color = Color.Red,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    )
}


@Composable
fun SignInWithEmailButton(buttonWidth: Dp, emailLoginClick: () -> Unit) {
    OutlinedButton(
        onClick = { emailLoginClick() },
        modifier = Modifier.width(buttonWidth),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colorResource(R.color.ui_bgEmail),
            contentColor = colorResource(R.color.white)
        )
    ) {
        SignInButtonRow(iconId = R.drawable.ic_mail_white_24dp, buttonTextId = R.string.sign_in_with_email)
    }
}

@Composable
fun SignInButtonRow(@DrawableRes iconId: Int, @StringRes buttonTextId: Int) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 40.dp)
    ) {
        LoginButtonIcon(iconId)
        LoginButtonText(buttonTextId)
    }
}

@Composable
fun LoginButtonIcon(@DrawableRes painterResourceId: Int) {
    Icon(
        tint = Color.Unspecified,
        painter = painterResource(painterResourceId),
        contentDescription = null
    )
}

@Composable
fun LoginButtonText(@StringRes stringResourceId: Int) {
    Text(
        text = stringResource(stringResourceId),
        textAlign = TextAlign.Start,
        style = MaterialTheme.typography.button,
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
            .fillMaxWidth()
    )
}