package com.stingers.alttpr.screens.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.stingers.alttpr.model.SeedEntity
import com.stingers.alttpr.repository.local.SeedDao
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class LibraryViewModel(
    private val seedDao: SeedDao,
) : ViewModel() {

    val romsFlow: Flow<PagingData<SeedEntity>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { seedDao.getSeeds() }
    ).flow.cachedIn(viewModelScope)
}
