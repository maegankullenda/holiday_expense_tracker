package com.maegankullenda.holidayexpensetracker.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.maegankullenda.holidayexpensetracker.domain.model.BudgetSummary

@Composable
fun BudgetCard(
    budgetSummary: BudgetSummary,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Budget Overview",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                BudgetItem(
                    label = "Total Budget",
                    amount = "${budgetSummary.currency.symbol}${String.format("%.2f", budgetSummary.totalBudget)}",
                )
                BudgetItem(
                    label = "Remaining",
                    amount = "${budgetSummary.currency.symbol}${String.format("%.2f", budgetSummary.remainingAmount)}",
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                BudgetItem(
                    label = "Daily Budget",
                    amount = "${budgetSummary.currency.symbol}${String.format("%.2f", budgetSummary.dailyBudget)}",
                )
                BudgetItem(
                    label = "Days Left",
                    amount = "${budgetSummary.remainingDays}",
                )
            }
        }
    }
}

@Composable
private fun BudgetItem(
    label: String,
    amount: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = amount,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
    }
} 