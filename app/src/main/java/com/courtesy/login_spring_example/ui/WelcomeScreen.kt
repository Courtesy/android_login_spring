package com.courtesy.login_spring_example.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.courtesy.login_spring_example.LoginViewModel
import com.courtesy.login_spring_example.R

@Composable
fun WelcomeScreen(viewModel: LoginViewModel) {
    Column(modifier = Modifier.padding(top = 8.dp)) {
        WelcomeText()
        GetUserButton(viewModel)
        LogoutButton(viewModel)
    }
}
@Composable
fun WelcomeText() {
    Text(
        text = stringResource(R.string.welcome_logged_in),
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h4
    )
}

@Composable
fun GetUserButton(viewModel: LoginViewModel) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { viewModel.getUser() }) {
            Text(text = stringResource(R.string.get_user))
        }
    }
}

@Composable
fun LogoutButton(viewModel: LoginViewModel) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { viewModel.signOut() }) {
            Text(text = stringResource(R.string.log_out))
        }
    }
}
