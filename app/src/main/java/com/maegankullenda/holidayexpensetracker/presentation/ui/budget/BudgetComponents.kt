package com.maegankullenda.holidayexpensetracker.presentation.ui.budget

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.maegankullenda.holidayexpensetracker.domain.model.Currency

@Composable
fun OdometerStyleProgress(
    modifier: Modifier = Modifier,
    progress: Float,
    progressColor: Color,
    trackColor: Color
) {
    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val strokeWidth = 35f
            val startAngle = 135f
            val sweepAngle = 270f

            // Background track arc
            drawArc(
                color = trackColor,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = Offset(
                    x = (canvasWidth - canvasHeight) / 2,
                    y = 0f
                ),
                size = Size(canvasHeight, canvasHeight),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Progress arc
            drawArc(
                color = progressColor,
                startAngle = startAngle,
                sweepAngle = sweepAngle * progress,
                useCenter = false,
                topLeft = Offset(
                    x = (canvasWidth - canvasHeight) / 2,
                    y = 0f
                ),
                size = Size(canvasHeight, canvasHeight),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        // The text in the middle
        Text(
            text = "${(progress * 100).toInt()}%",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = progressColor
        )
    }
}

@Composable
fun getBudgetProgressColor(percentage: Double): Color {
    return when {
        percentage <= 0.5 -> Color(0xFF4CAF50) // Green
        percentage <= 0.75 -> Color(0xFFFFC107) // Amber
        else -> Color(0xFFF44336) // Red
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetCard(
    title: String,
    amount: Double,
    currency: Currency,
    modifier: Modifier = Modifier,
    cardColor: Color = MaterialTheme.colorScheme.surface
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = cardColor
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
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF1E3A8A), // Deep blue (high contrast)
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = formatCurrency(amount, currency),
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF0F1419), // Very dark blue (maximum contrast)
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetCardCity(
    title: String,
    amount: Double,
    currency: Currency,
    modifier: Modifier = Modifier,
    cardColor: Color = MaterialTheme.colorScheme.surface
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = cardColor
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
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFFECF0F1), // Light gray (city lights)
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = formatCurrency(amount, currency),
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFFBDC3C7), // Silver (metallic)
                fontWeight = FontWeight.Bold
            )
        }
    }
}

fun formatCurrency(amount: Double, currency: Currency): String {
    return when (currency) {
        Currency.ZAR -> "R%.2f".format(amount)
        Currency.USD -> "$%.2f".format(amount)
        Currency.EUR -> "€%.2f".format(amount)
        Currency.GBP -> "£%.2f".format(amount)
        Currency.AUD -> "A$%.2f".format(amount)
        Currency.CAD -> "C$%.2f".format(amount)
    }
} 