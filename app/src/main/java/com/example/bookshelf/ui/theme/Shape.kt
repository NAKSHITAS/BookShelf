package com.example.bookshelf.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    small = RoundedCornerShape(8.dp),  // Slightly rounded corners for small elements
    medium = RoundedCornerShape(16.dp), // Moderate rounding for cards & buttons
    large = RoundedCornerShape(24.dp)  // Large rounding for big surfaces
)