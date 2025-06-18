package com.example.travel.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.travel.R
import com.example.travel.ui.theme.ProjectColor
import com.example.travel.ui.theme.ProjectTextStyle
import com.example.travel.view.currency.CurrencyPage
import com.example.travel.view.flight.FlightPage

sealed class Screen(val route: String, val iconKey: Int, val navKey: Int) {
    object Flight : Screen("flight_route", R.drawable.flight_rounded, R.string.nav_flight)
    object Currency : Screen("currency_route", R.drawable.currency_dollar, R.string.nav_currency)
}

sealed class BottomNavItem(val route: String, val iconKey: Int, val navKey: Int) {
    object Flight :
        BottomNavItem(Screen.Flight.route, Screen.Flight.iconKey, Screen.Flight.navKey)

    object Currency :
        BottomNavItem(Screen.Currency.route, Screen.Currency.iconKey, Screen.Currency.navKey)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val bottomNavItems = listOf(
        BottomNavItem.Flight,
        BottomNavItem.Currency
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = ProjectColor.BackgroundGray,
            contentColor = MaterialTheme.colorScheme.onBackground,
            content = { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = Screen.Flight.route,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    composable(Screen.Flight.route) { FlightPage() }
                    composable(Screen.Currency.route) { CurrencyPage() }
                }
            }
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp,
                tonalElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                NavigationBar(
                    containerColor = Color.Transparent
                ) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination

                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    imageVector = ImageVector.vectorResource(item.iconKey),
                                    contentDescription = null
                                )
                            },
                            label = {
                                Text(
                                    stringResource(item.navKey),
                                    style = ProjectTextStyle.H10,
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = ProjectColor.White,
                                unselectedIconColor = ProjectColor.Black20,
                                selectedTextColor = ProjectColor.Black,
                                unselectedTextColor = ProjectColor.Black20,
                                indicatorColor = MaterialTheme.colorScheme.primary
                            ),
                            selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}