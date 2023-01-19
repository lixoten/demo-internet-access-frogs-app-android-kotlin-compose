package com.example.frogs.data

import com.example.frogs.model.FrogRec
import com.example.frogs.network.FrogsApiService

/**
 * Default Implementation of repository that retrieves amphibian data from underlying data source.
 */
class DefaultFrogsRepository(
    private val frogsApiService: FrogsApiService
) : FrogsRepository {
    /** Retrieves list of Frogs from underlying data source */
    override suspend fun getFrogsRecords(): List<FrogRec> {
        return frogsApiService.getFrogsRecords()
    }
}