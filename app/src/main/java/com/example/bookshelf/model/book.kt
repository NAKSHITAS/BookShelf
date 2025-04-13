package com.example.bookshelf.model

import com.google.gson.annotations.SerializedName

/**
 * Data class representing a single book retrieved from the Google Books API.
 */
data class BookResponse(
    val items: List<BookItem>?
)

/**
 * Data class representing a book item in the API response.
 */
data class BookItem(
    val id: String,
    @SerializedName("volumeInfo") val volumeInfo: VolumeInfo
)

/**
 * Data class containing detailed book information.
 */
data class VolumeInfo(
    val title: String,
    val authors: List<String>?,
    val description: String?,
    @SerializedName("imageLinks") val imageLinks: ImageLinks?,
    val categories: List<String>?
)

/**
 * Data class containing book image URLs.
 */
data class ImageLinks(
    @SerializedName("thumbnail") val thumbnail: String?,
    @SerializedName("smallThumbnail") val smallThumbnail: String?,
)

