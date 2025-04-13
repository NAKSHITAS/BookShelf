package com.example.bookshelf.data

import com.example.bookshelf.model.BookItem
import com.example.bookshelf.network.BooksApiService
import retrofit2.HttpException
import java.io.IOException

/**
 * Repository interface for Books data handling.
 */
interface BooksRepository {
    suspend fun searchBooks(query: String?): List<BookItem>
    suspend fun getBookDetails(bookId: String): BookItem?
}

/**
 * Network implementation of BooksRepository using Retrofit.
 */
class NetworkBooksRepository(
    private val apiService: BooksApiService,
    private val defaultCategory: String = "Fiction" // Default category fallback
) : BooksRepository {

    override suspend fun searchBooks(query: String?): List<BookItem> {
        val searchQuery = query?.takeIf { it.isNotBlank() } ?: defaultCategory

        return try {
            apiService.searchBooks(searchQuery).items.orEmpty() // ✅ Prevents null issues
        } catch (e: IOException) {
            // Network error (e.g., no internet)
            emptyList()
        } catch (e: HttpException) {
            // API error (e.g., 404, 500)
            emptyList()
        }
    }

    override suspend fun getBookDetails(bookId: String): BookItem? {
        return try {
            apiService.getBookDetails(bookId)
        } catch (e: IOException) {
            null
        } catch (e: HttpException) {
            null
        }
    }
}
