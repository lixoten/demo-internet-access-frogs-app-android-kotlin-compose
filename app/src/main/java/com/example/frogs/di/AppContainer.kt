package com.example.frogs.di

import com.example.frogs.data.FrogsRepository
import com.example.frogs.network.FrogsApiService

/**
 * Dependency Injection container at the application level.
 */
interface AppContainer {
    val frogsApiService: FrogsApiService
    val frogsRepository: FrogsRepository
}