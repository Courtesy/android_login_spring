package com.courtesy.login_spring_example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.courtesy.login_spring_example.LoginViewModel
import com.courtesy.login_spring_example.R

@Composable
fun EmailLoginScreen(viewModel: LoginViewModel) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 24.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (viewModel.error.value.isNotBlank()) {
            ErrorField(viewModel)
        }
        EmailField(viewModel)
        PasswordField(viewModel)
        ButtonEmailPasswordLogin(viewModel)
        ButtonEmailPasswordCreate(viewModel)
    }
}

@Composable
fun EmailField(viewModel: LoginViewModel) {
    val userEmail = viewModel.userEmail.value

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = userEmail,
        label = { Text(text = stringResource(R.string.email)) },
        onValueChange = { viewModel.setUserEmail(it) }
    )
}

@Composable
fun PasswordField(viewModel: LoginViewModel) {
    val password = viewModel.password.value

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = PasswordVisualTransformation(),
        value = password,
        label = { Text(text = stringResource(R.string.password)) },
        onValueChange = { viewModel.setPassword(it) }
    )
}

@Composable
fun ButtonEmailPasswordLogin(viewModel: LoginViewModel) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        enabled = viewModel.isValidEmailAndPassword(),
        content = { Text(text = stringResource(R.string.login)) },
        onClick = { viewModel.signInWithEmailAndPassword() }
    )
}

@Composable
fun ButtonEmailPasswordCreate(viewModel: LoginViewModel) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        enabled = viewModel.isValidEmailAndPassword(),
        content = { Text(text = stringResource(R.string.create)) },
        onClick = { viewModel.createUserWithEmailAndPassword() }
    )
}
