package com.maegankullenda.holidayexpensetracker.presentation.di

import android.app.Application
import androidx.room.Room
import com.maegankullenda.holidayexpensetracker.data.local.dao.ExpenseDao
import com.maegankullenda.holidayexpensetracker.data.source.remote.ExpenseApi
import com.maegankullenda.holidayexpensetracker.domain.model.BudgetCalculator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideExpenseApi(client: OkHttpClient): ExpenseApi {
        return Retrofit.Builder()
            .baseUrl("https://api.example.com/") // Replace with your actual API URL
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(ExpenseApi::class.java)
    }

    @Provides
    @Singleton
    fun provideBudgetCalculator(): BudgetCalculator {
        return BudgetCalculator()
    }
}