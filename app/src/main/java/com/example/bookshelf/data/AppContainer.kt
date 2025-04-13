package com.example.bookshelf.data

import com.example.bookshelf.network.BooksApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Dependency Injection container for the application.
 */
interface AppContainer {
    val booksRepository: BooksRepository
}

/**
 * Implementation of the Dependency Injection container.
 *
 * Ensures that a single instance of Retrofit and repository is shared across the app.
 */
class DefaultAppContainer : AppContainer {
    private val baseUrl = "https://www.googleapis.com/books/v1/"

    /**
     * Retrofit instance with Gson serialization.
     */
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create()) // Using Gson for JSON conversion
        .build()

    /**
     * Retrofit service for fetching book data.
     */
    private val retrofitService: BooksApiService by lazy {
        retrofit.create(BooksApiService::class.java)
    }

    /**
     * Dependency injection implementation for the books repository.
     * Default search category is "Fiction" if none is provided.
     */
    override val booksRepository: BooksRepository by lazy {
        NetworkBooksRepository(retrofitService, defaultCategory = "Fiction")
    }
}
