package com.maegankullenda.holidayexpensetracker.data.source.remote

import com.maegankullenda.holidayexpensetracker.data.dto.ExpenseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ExpenseApi {
    @GET("expenses")
    suspend fun getAllExpenses(): List<ExpenseDto>

    @GET("expenses/{id}")
    suspend fun getExpenseById(@Path("id") id: String): ExpenseDto

    @POST("expenses")
    suspend fun createExpense(@Body expense: ExpenseDto): ExpenseDto

    @PUT("expenses/{id}")
    suspend fun updateExpense(
        @Path("id") id: String,
        @Body expense: ExpenseDto
    ): ExpenseDto

    @DELETE("expenses/{id}")
    suspend fun deleteExpense(@Path("id") id: String)
} 