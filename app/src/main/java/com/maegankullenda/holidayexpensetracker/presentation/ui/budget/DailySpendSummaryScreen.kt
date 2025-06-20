package com.maegankullenda.holidayexpensetracker.presentation.ui.budget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.maegankullenda.holidayexpensetracker.domain.model.Currency

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailySpendSummaryScreen(
    navController: NavController,
    onNavigateToExpenseHistory: () -> Unit = { navController.navigate("expense_history") },
    viewModel: BudgetViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val dailyAllocation = if (state.daysRemaining > 0) state.remainingAmount / state.daysRemaining else 0.0
    val dailySpendPercentage = if (dailyAllocation > 0) (state.dailySpend / dailyAllocation).coerceIn(0.0, 1.0) else 0.0
    val progressColor = getBudgetProgressColor(dailySpendPercentage)

    // Cosmopolitan city theme gradient background
    val cityGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1A1A2E), // Deep navy (night sky)
            Color(0xFF16213E), // Dark blue-gray (city skyline)
            Color(0xFF0F3460), // Midnight blue (urban depth)
            Color(0xFF2C3E50), // Steel blue (metropolitan)
            Color(0xFF34495E)  // Dark slate (concrete)
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Daily Spend Summary",
                        color = Color(0xFFECF0F1), // Light gray (city lights)
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFFECF0F1) // Light gray (city lights)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(cityGradient)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                BudgetCardCity(
                    title = "Total Spent Today",
                    amount = state.dailySpend,
                    currency = state.currency,
                    modifier = Modifier.fillMaxWidth(),
                    cardColor = Color(0xFF34495E) // Dark slate (concrete)
                )

                BudgetCardCity(
                    title = "Daily Allocation",
                    amount = dailyAllocation,
                    currency = state.currency,
                    modifier = Modifier.fillMaxWidth(),
                    cardColor = Color(0xFF2C3E50) // Steel blue (metropolitan)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF34495E) // Dark slate (concrete)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Daily Budget Progress",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFFECF0F1), // Light gray (city lights)
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OdometerStyleProgress(
                            modifier = Modifier.height(180.dp),
                            progress = dailySpendPercentage.toFloat(),
                            progressColor = progressColor,
                            trackColor = Color(0xFF2C3E50) // Steel blue (metropolitan)
                        )
                    }
                }

                Text(
                    text = if (state.daysRemaining > 0) {
                        "${state.daysRemaining} days remaining"
                    } else {
                        "Last day of holiday!"
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFFECF0F1), // Light gray (city lights)
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onNavigateToExpenseHistory,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF5F9EA0) // Cadet blue (matching the original)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "View History",
                        color = Color(0xFFECF0F1), // Light gray (city lights)
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
} 