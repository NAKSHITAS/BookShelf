package com.example.bookshelf.network

import com.example.bookshelf.model.BookItem
import com.example.bookshelf.model.BookResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit API service for fetching books from the Google Books API.
 */
interface BooksApiService {

    /**
     * Fetches a list of books based on a search query and optional category.
     * If category is provided, it filters books by that category.
     */
    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String, // Search term
        @Query("subject") category: String? = null, // Optional category filter
        @Query("maxResults") maxResults: Int = 20 // Limit number of books per request
    ): BookResponse

    /**
     * Fetches details of a specific book by its ID.
     */
    @GET("volumes/{bookId}")
    suspend fun getBookDetails(@Path("bookId") bookId: String): BookItem
}
