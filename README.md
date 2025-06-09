# Holiday Expense Tracker

A modern Android app to help you manage and track your holiday expenses efficiently. Built with Jetpack Compose and following clean architecture principles.

## Features

- **Multiple Holiday Management**

  - Create and manage multiple holidays
  - Set holiday details including name, destination, dates, and budget
  - Switch between different holidays
  - Sort holidays by start date

- **Budget Management**

  - Set total budget for each holiday
  - Track daily budget allocation
  - Real-time remaining budget calculation
  - Support for multiple currencies (ZAR, USD, EUR, GBP, AUD, CAD)

- **Expense Tracking**

  - Add expenses with descriptions and categories
  - Categorize expenses (Food, Accommodation, Transportation, etc.)
  - View expense history
  - Delete individual expenses
  - Daily spending overview

- **Smart Budget Calculations**
  - Automatic daily budget adjustments based on spending
  - Remaining days calculation
  - Total spent vs budget comparison
  - Daily spending limits

## Technology Stack

- **Architecture**

  - Clean Architecture
  - MVVM Pattern
  - Repository Pattern
  - Use Case Pattern

- **Libraries & Frameworks**
  - Jetpack Compose for UI
  - Hilt for Dependency Injection
  - Kotlin Coroutines & Flow for async operations
  - Material 3 Design Components
  - AndroidX Navigation

## Project Structure

```
app/
├── data/
│   ├── repository/    # Repository implementations
│   └── local/         # Local database and data sources
├── domain/
│   ├── model/         # Domain models
│   ├── repository/    # Repository interfaces
│   └── usecase/       # Business logic use cases
└── presentation/
    ├── components/    # Reusable UI components
    ├── navigation/    # Navigation setup
    └── ui/
        ├── budget/    # Budget screen and viewmodel
        ├── expense/   # Expense screens and viewmodels
        └── holiday/   # Holiday management screens
```

## Getting Started

### Prerequisites

- Android Studio Arctic Fox or later
- Android SDK 24 or higher
- Kotlin 1.8.0 or higher

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/yourusername/HolidayExpenseTracker.git
   ```

2. Open the project in Android Studio

3. Sync project with Gradle files

4. Run the app on an emulator or physical device

## Usage

1. **Creating a Holiday**

   - Tap the "+" button on the holiday list screen
   - Enter holiday details (name, destination, dates, budget)
   - Select currency
   - Save to create the holiday

2. **Managing Expenses**

   - Select a holiday from the list
   - Use "Add Expense" to record new expenses
   - View all expenses in "View History"
   - Delete expenses as needed

3. **Budget Tracking**
   - View daily budget allocation
   - Monitor total spending
   - Track remaining budget
   - See spending by category

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- Material Design 3 for modern UI components
- Android Jetpack libraries
- Kotlin and Coroutines for modern Android development
