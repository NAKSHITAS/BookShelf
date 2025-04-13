package com.example.bookshelf.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookshelf.R
import com.example.bookshelf.ui.screens.BooksViewModel
import com.example.bookshelf.ui.screens.HomeScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookshelfApp() {
    val categories = listOf("All", "Fiction", "Non-fiction", "Science", "History")
    var selectedCategory by remember { mutableStateOf(categories.first()) }

    val booksViewModel: BooksViewModel = viewModel(factory = BooksViewModel.Factory)

    // Update books when category changes
    LaunchedEffect(selectedCategory) {
        booksViewModel.searchBooks(selectedCategory)
    }

    Scaffold(
        topBar = { BookshelfTopAppBar(categories, selectedCategory) { category ->
            selectedCategory = category
            booksViewModel.searchBooks(category)
        }}
    ) { paddingValues ->
        Surface(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            HomeScreen(
                booksUiState = booksViewModel.booksUiState,
                retryAction = { booksViewModel.searchBooks(selectedCategory) },
                contentPadding = paddingValues,
                onCategorySelected = { category ->
                    selectedCategory = category
                    booksViewModel.searchBooks(category)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookshelfTopAppBar(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    TopAppBar(
        title = { Text("Bookshelf", style = MaterialTheme.typography.headlineSmall) },
        actions = {
            CategoryDropdown(categories, selectedCategory, onCategorySelected)
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun CategoryDropdown(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.padding(end = 16.dp)) {
        OutlinedButton(
            onClick = { expanded = true },
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
        ) {
            Text(selectedOption, color = Color.Black)
            Icon(
                painter = painterResource(R.drawable.ic_dropdown_arrow),
                contentDescription = "Dropdown"
            )
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
