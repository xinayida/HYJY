package com.futuretech.base.demo.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

sealed class Screen(val route: String, val title: String) {
    object Home : Screen("home", "Home")
    object Favorite : Screen("favorite", "Favorite")
    object Profile : Screen("profile", "Profile")
    object Cart : Screen("cart", "Cart")
}

val items = listOf(
    Screen.Home,
    Screen.Favorite,
    Screen.Profile,
    Screen.Cart
)

@Composable
fun WorkWithBottomNavigationExample() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigation {
                // 从 NavHost 函数中获取 navController 状态，并与 BottomNavigation 组件共享此状态。
                // 这意味着 BottomNavigation 会自动拥有最新状态。
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination // 这个目的是为了下面比较获得当前的选中状态
                items.forEach { screen ->
                    BottomNavigationItem(
                        icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            // 加这个可解决问题：按back键会返回2次，第一次先返回home, 第二次才会退出
                            navController.popBackStack()
                            navController.navigate(screen.route) {
                                // 点击item时，清空栈内 popUpTo ID到栈顶之间的所有节点，避免站内节点持续增加
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true // 用于页面状态的恢复
                                }
                                // 避免多次重复点击按钮时产生多个实例
                                launchSingleTop = true
                                // 再次点击之前选中的Item时，恢复之前的状态
                                restoreState = true
                                // 通过使用 saveState 和 restoreState 标志，当您在底部导航项之间切换时，
                                // 系统会正确保存并恢复该项的状态和返回堆栈。
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = Screen.Home.route, Modifier.padding(innerPadding)) {
            composable(Screen.Home.route) { HomeScreen2(navController) }
            composable(Screen.Favorite.route) { FavoriteScreen(navController) }
            composable(Screen.Profile.route) { ProfileScreen(navController) }
            composable(Screen.Cart.route) { CartScreen2(navController) }
        }
    }
}

@Composable
fun HomeScreen2(navController: NavHostController) {
    Text(text = "HomeScreen2")
}

@Composable
fun FavoriteScreen(navController: NavHostController) {
    Text(text = "FavoriteScreen")
}

@Composable
fun ProfileScreen(navController: NavHostController) {
    Text(text = "ProfileScreen")
}

@Composable
fun CartScreen2(navController: NavHostController) {
    Text(text = "CartScreen2")
}