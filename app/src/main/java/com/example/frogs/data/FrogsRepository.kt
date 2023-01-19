package com.example.frogs.data

import com.example.frogs.model.FrogRec

/**
 * Repository retrieves amphibian data from underlying data source.
 */
interface FrogsRepository {
    /** Retrieves list of amphibians from underlying data source */
    suspend fun getFrogsRecords(): List<FrogRec>
}