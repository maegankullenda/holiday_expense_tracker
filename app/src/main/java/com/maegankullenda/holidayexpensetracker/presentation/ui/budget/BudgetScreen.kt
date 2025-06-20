package com.maegankullenda.holidayexpensetracker.presentation.ui.budget

import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.maegankullenda.holidayexpensetracker.domain.model.Currency
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAddExpense: () -> Unit,
    onNavigateToDailySpendSummary: () -> Unit,
    viewModel: BudgetViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    
    // Beach theme gradient background
    val beachGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF87CEEB), // Sky blue
            Color(0xFF98D8E8), // Light sky blue
            Color(0xFFB0E0E6), // Powder blue
            Color(0xFFF0F8FF), // Alice blue
            Color(0xFFF5F5DC)  // Beige (sand)
        )
    )
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Budget Overview",
                        color = Color(0xFF2E5A88), // Deep ocean blue
                        fontWeight = FontWeight.Bold
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF2E5A88) // Deep ocean blue
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { padding ->
        // Apply beach background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(beachGradient)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BudgetCard(
                    title = "Total Budget",
                    amount = state.totalBudget,
                    currency = state.currency,
                    modifier = Modifier.fillMaxWidth(),
                    cardColor = Color(0xFFE8F4FD) // Light blue with better contrast
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                BudgetCard(
                    title = "Total Spent",
                    amount = state.spentAmount,
                    currency = state.currency,
                    modifier = Modifier.fillMaxWidth(),
                    cardColor = Color(0xFFFDF6E3) // Light cream with better contrast
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Budget Progress Indicator with beach colors
                val budgetPercentage = if (state.totalBudget > 0) {
                    (state.spentAmount / state.totalBudget).coerceIn(0.0, 1.0)
                } else {
                    0.0
                }
                val progressColor = getBudgetProgressColor(budgetPercentage)
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF0F8FF) // Alice blue
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
                            text = "Budget Progress",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFF1E3A8A), // Deep blue for better contrast
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        OdometerStyleProgress(
                            modifier = Modifier.height(180.dp),
                            progress = budgetPercentage.toFloat(),
                            progressColor = progressColor,
                            trackColor = Color(0xFFE0E6ED) // Light gray-blue
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                BudgetCard(
                    title = "Remaining Budget",
                    amount = state.remainingAmount,
                    currency = state.currency,
                    modifier = Modifier.fillMaxWidth(),
                    cardColor = Color(0xFFE8F4FD) // Light blue with better contrast
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = onNavigateToDailySpendSummary,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6B8E23) // Olive drab
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Daily Summary")
                    }
                    
                    Button(
                        onClick = onNavigateToAddExpense,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4682B4) // Steel blue
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Add Expense")
                    }
                }
            }
        }
    }
} 