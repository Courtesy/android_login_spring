package com.courtesy.login_spring_example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.courtesy.login_spring_example.ui.EmailLoginScreen
import com.courtesy.login_spring_example.ui.LoginScreen
import com.courtesy.login_spring_example.ui.WelcomeScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BaseScreen()
        }
    }
}

@Composable
fun BaseScreen(loginViewModel: LoginViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val backstackEntry = navController.currentBackStackEntryAsState()

    val currentScreen = NavigationEnum.fromRoute(backstackEntry.value?.destination?.route, loginViewModel.isLoggedIn)
    Scaffold(
        topBar = { TopBar(navController, currentScreen) }
    ) {
        NavigateBetweenScreen(navController)
    }
}

@Composable
fun TopBar(navController: NavHostController, currentScreen: NavigationEnum) {
    TopAppBar(
        title = { Text(text = stringResource(currentScreen.title)) },
        // To avoid going back to previous screen after login/logout click
        navigationIcon = {
            if (currentScreen != NavigationEnum.Welcome
                && currentScreen != NavigationEnum.Login
            ) {
                NavigateBackButton(navController)
            }
        }
    )
}

@Composable
fun NavigateBackButton(navController: NavHostController) {
    IconButton(onClick = { navController.popBackStack() },
        modifier = Modifier.semantics { contentDescription = "back button" }) {

        Icon(Icons.Filled.ArrowBack, stringResource(R.string.back_icon))
    }
}

@Composable
fun NavigateBetweenScreen(navController: NavHostController, loginViewModel: LoginViewModel = hiltViewModel()) {
    val startDestination = if (loginViewModel.isLoggedIn.value) NavigationEnum.Welcome.name else NavigationEnum.Login.name

    NavHost(navController = navController, startDestination = startDestination) {
        loginPage(this, navController, loginViewModel)
        emailLoginPage(this, loginViewModel)
        welcomePage(this, loginViewModel)
    }
}

fun loginPage(builder: NavGraphBuilder, navController: NavHostController, loginViewModel: LoginViewModel) {
    builder.composable(route = NavigationEnum.Login.name) {
        loginViewModel.setError("")
        LoginScreen(
            emailLoginClick = { navController.navigate(NavigationEnum.EmailLogin.name) },
            viewModel = loginViewModel
        )
    }
}

fun emailLoginPage(builder: NavGraphBuilder, loginViewModel: LoginViewModel) {
    builder.composable(route = NavigationEnum.EmailLogin.name) {
        loginViewModel.setError("")
        EmailLoginScreen(loginViewModel)
    }
}

fun welcomePage(builder: NavGraphBuilder, loginViewModel: LoginViewModel) {
    builder.composable(route = NavigationEnum.Welcome.name) {
        WelcomeScreen(loginViewModel)
    }
}
