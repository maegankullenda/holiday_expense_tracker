package com.maegankullenda.holidayexpensetracker.presentation.ui.holiday

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.maegankullenda.holidayexpensetracker.domain.model.Holiday
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HolidayListScreen(
    onNavigateToCreateHoliday: () -> Unit,
    onHolidaySelected: () -> Unit,
    viewModel: HolidayListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    // Holiday theme gradient background - ultra vibrant and exciting
    val holidayGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFF1744), // Bright red
            Color(0xFFFF9100), // Bright orange
            Color(0xFFFFEB3B), // Bright yellow
            Color(0xFF00E676), // Bright green
            Color(0xFF00BCD4), // Bright cyan
            Color(0xFF9C27B0)  // Bright purple
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "My Holidays",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineMedium
                    ) 
                },
                actions = {
                    IconButton(onClick = onNavigateToCreateHoliday) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Create new holiday",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { padding ->
        // Apply the vibrant background directly to the content area
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(holidayGradient)
        ) {
            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color.White
                    )
                }
            } else if (state.holidays.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.98f)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "No holidays yet",
                                style = MaterialTheme.typography.headlineMedium,
                                color = Color(0xFFE91E63), // Bright pink/magenta
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Start planning your next adventure!",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color(0xFF6B7280)
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Button(
                                onClick = onNavigateToCreateHoliday,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFE91E63) // Bright pink/magenta
                                ),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Create Holiday")
                            }
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val sortedHolidays = state.holidays.sortedByDescending { it.isActive }
                    items(sortedHolidays) { holiday ->
                        HolidayItem(
                            holiday = holiday,
                            isActive = holiday.isActive,
                            onSelect = { 
                                viewModel.setActiveHoliday(holiday)
                                onHolidaySelected()
                            },
                            onDelete = {
                                viewModel.onEvent(HolidayListEvent.DeleteHoliday(holiday))
                            }
                        )
                    }
                }
            }

            state.error?.let { error ->
                Card(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.BottomCenter),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFEF4444).copy(alpha = 0.9f)
                    )
                ) {
                    Text(
                        text = error,
                        color = Color.White,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            if (state.showDeleteConfirmation) {
                AlertDialog(
                    onDismissRequest = {
                        viewModel.onEvent(HolidayListEvent.DismissDeleteDialog)
                    },
                    title = { Text("Delete Holiday") },
                    text = {
                        Text(
                            "Are you sure you want to delete '${state.holidayToDelete?.name}'? " +
                            "This action cannot be undone and all associated expenses will be deleted."
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                viewModel.onEvent(HolidayListEvent.ConfirmDelete)
                            }
                        ) {
                            Text("Delete", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                viewModel.onEvent(HolidayListEvent.DismissDeleteDialog)
                            }
                        ) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HolidayItem(
    holiday: Holiday,
    isActive: Boolean,
    onSelect: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onSelect,
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) {
                Color(0xFFFFF3E0) // Warm orange-tinted white for active
            } else {
                Color.White.copy(alpha = 0.98f)
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header with name and active badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = holiday.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE91E63) // Bright pink/magenta
                    )
                    
                    if (isActive) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFF5722), // Bright orange
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Active Holiday",
                                color = Color(0xFFFF5722), // Bright orange
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                
                IconButton(
                    onClick = onDelete,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = Color(0xFFEF4444)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete holiday",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Destination with icon
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color(0xFF6B7280),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = holiday.destination,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF374151),
                    fontWeight = FontWeight.Medium
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Date range with icon
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = Color(0xFF6B7280),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${holiday.startDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))} - " +
                            holiday.endDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy")),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF6B7280)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Budget with icon
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Budget: ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF6B7280),
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${holiday.currency.symbol}${String.format("%.2f", holiday.totalBudget)}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50) // Bright green
                )
            }
        }
    }
} 