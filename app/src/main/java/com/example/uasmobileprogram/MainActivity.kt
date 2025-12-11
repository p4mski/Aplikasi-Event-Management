package com.example.uasmobileprogram

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import com.example.uasmobileprogram.ui.theme.UASMobileProgramTheme
import com.example.uasmobileprogram.viewmodel.EventViewModel
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.uasmobileprogram.ui.screen.EventListScreen
import com.example.uasmobileprogram.ui.screen.EventDetailScreen
import com.example.uasmobileprogram.ui.screen.EventFormScreen

class MainActivity : ComponentActivity() {
    private val vm: EventViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UASMobileProgramTheme {
                Surface {
                    AppNav(vm = vm)
                }
            }
        }
    }
}

@Composable
fun AppNav(vm: EventViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            EventListScreen(
                viewModel = vm,
                onOpenDetail = { id -> navController.navigate("detail/$id") },
                onAdd = { navController.navigate("form/new") }
            )
        }
        composable(
            route = "detail/{eventId}",
            arguments = listOf(navArgument("eventId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("eventId") ?: 0
            EventDetailScreen(
                eventId = id,
                viewModel = vm,
                onEdit = { navController.navigate("form/$id") },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = "form/{eventId}",
            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
        ) { backStackEntry ->
            val idStr = backStackEntry.arguments?.getString("eventId") ?: "new"
            val isNew = idStr == "new"
            val id = if (isNew) null else idStr.toIntOrNull()
            EventFormScreen(
                viewModel = vm,
                eventId = id,
                onSaved = { navController.popBackStack() },
                onCancel = { navController.popBackStack() }
            )
        }
    }
}
