package com.example.bookshelf.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bookshelf.R
import com.example.bookshelf.model.BookItem
import com.example.bookshelf.model.VolumeInfo
import com.example.bookshelf.ui.theme.BookshelfTheme


@Composable
fun HomeScreen(
    booksUiState: BooksUiState,
    retryAction: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        Log.d("HomeScreen", "Triggering book search on launch")
    }

    Column(modifier = modifier.fillMaxSize()) {
        when (booksUiState) {
            is BooksUiState.Loading -> BooksGridScreen(
                books = List(10) {
                    BookItem(
                        id = "$it",
                        volumeInfo = VolumeInfo(
                            title = "Loading...",
                            authors = listOf(),
                            description = "",
                            imageLinks = null,
                            categories = listOf()
                        )
                    )
                },
                isLoading = true
            )
            is BooksUiState.Success -> BooksGridScreen(books = booksUiState.books, isLoading = false)
            is BooksUiState.Error -> ErrorScreen(retryAction)
        }
    }
}

@Composable
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error),
            contentDescription = null
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Composable
fun BooksGridScreen(
    books: List<BookItem>,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.padding(horizontal = 8.dp),
        contentPadding = contentPadding,
    ) {
        items(items = books, key = { book -> book.id }) { book ->
            BookCard(book, isLoading)
        }
    }
}

@Composable
fun BookCard(book: BookItem, isLoading: Boolean, modifier: Modifier = Modifier) {
    val imageUrl = book.volumeInfo.imageLinks?.thumbnail?.replace("http://", "https://") ?: ""

    Card(
        modifier = modifier
            .padding(1.dp) // ✅ Adds 1dp spacing between images
            .fillMaxWidth(0.48f) // ✅ Ensures 2 cards per row
            .aspectRatio(3f / 4f), // ✅ Keeps book cover ratio without distortion
        shape = RectangleShape, // ✅ Removes rounded corners
    ) {
        if (isLoading) {
            LoadingImage(modifier = Modifier.fillMaxSize())
        } else {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = book.volumeInfo.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop // ✅ Crops the image to fill card
            )
        }
    }
}



@Composable
fun LoadingImage(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(100.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

@Preview(showBackground = true)
@Composable
fun BooksGridScreenPreview() {
    BookshelfTheme {
        val mockData = List(10) {
            BookItem(
                id = "$it",
                volumeInfo = VolumeInfo(
                    title = "Title $it",
                    authors = listOf("Author $it"),
                    description = "Description $it",
                    imageLinks = null,
                    categories = listOf()
                )
            )
        }
        BooksGridScreen(mockData, isLoading = false)
    }
}
