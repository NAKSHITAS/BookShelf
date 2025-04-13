package com.example.bookshelf.ui.screens

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.bookshelf.BookshelfApplication
import com.example.bookshelf.data.BooksRepository
import com.example.bookshelf.model.BookItem
import kotlinx.coroutines.launch

/**
 * UI state for the Book list screen.
 */
sealed interface BooksUiState {
    data class Success(val books: List<BookItem>) : BooksUiState
    object Error : BooksUiState
    object Loading : BooksUiState
}

/**
 * ViewModel to handle fetching book data.
 */
class BooksViewModel(private val booksRepository: BooksRepository) : ViewModel() {

    /** Mutable State that stores the status of the most recent request */
    var booksUiState: BooksUiState by mutableStateOf(BooksUiState.Loading)
        private set

    /**
     * Fetches books based on a query. Default category is "Fiction".
     */
    fun searchBooks(query: String = "Fiction") {
        viewModelScope.launch {
            booksUiState = BooksUiState.Loading  // Start loading
            try {
                val books = booksRepository.searchBooks(query)
                Log.d("BooksViewModel", "Fetched books: ${books.size}") // Debugging log

                booksUiState = if (books.isNotEmpty()) {
                    BooksUiState.Success(books)
                } else {
                    BooksUiState.Error
                }
            } catch (e: Exception) {
                booksUiState = BooksUiState.Error
                Log.e("BooksViewModel", "Error fetching books: ${e.message}", e)
            }
        }
    }

    /**
     * Factory for [BooksViewModel] that takes [BooksRepository] as a dependency.
     */
    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = extras[APPLICATION_KEY] as? BookshelfApplication
                    ?: throw IllegalStateException("Application is not of type BookshelfApplication")

                return BooksViewModel(application.container.booksRepository) as T
            }
        }
    }
}
