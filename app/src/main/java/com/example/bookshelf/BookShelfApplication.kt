package com.example.bookshelf

import android.app.Application
import com.example.bookshelf.data.AppContainer
import com.example.bookshelf.data.DefaultAppContainer

class BookshelfApplication : Application() {
    /** Dependency Injection Container used throughout the app */
    val container: AppContainer by lazy { DefaultAppContainer() }

    override fun onCreate() {
        super.onCreate()

    }
}
