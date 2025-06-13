import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.maegankullenda.holidayexpensetracker.domain.model.BudgetSummary
import com.maegankullenda.holidayexpensetracker.domain.usecase.GetBudgetSummaryUseCase
import com.maegankullenda.holidayexpensetracker.domain.model.Expense
import com.maegankullenda.holidayexpensetracker.domain.model.Currency

class BudgetViewModel(
    private val getBudgetSummaryUseCase: GetBudgetSummaryUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(BudgetSummaryState())
    val state: StateFlow<BudgetSummaryState> = _state.asStateFlow()

    fun loadBudgetSummary() {
        viewModelScope.launch {
            getBudgetSummaryUseCase().collect { summary ->
                summary?.let {
                    println("Updating state with summary: $it")
                    _state.update { state ->
                        state.copy(
                            totalBudget = it.totalBudget,
                            spentAmount = it.spentAmount,
                            remainingAmount = it.remainingAmount,
                            dailyBudget = it.dailyBudget,
                            remainingDays = it.remainingDays,
                            expenses = it.expenses,
                            currency = it.currency
                        )
                    }
                    println("State updated: ${_state.value}")
                }
            }
        }
    }
}

data class BudgetSummaryState(
    val totalBudget: Double = 0.0,
    val spentAmount: Double = 0.0,
    val remainingAmount: Double = 0.0,
    val dailyBudget: Double = 0.0,
    val remainingDays: Int = 0,
    val expenses: List<Expense> = emptyList(),
    val currency: Currency = Currency.ZAR
) 