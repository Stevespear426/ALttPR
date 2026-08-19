package com.stingers.alttpr.screens.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.stingers.alttpr.model.RomEntity
import com.stingers.alttpr.navigation.NavigationManager
import com.stingers.alttpr.repository.local.RomDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class LibraryViewModel(
    private val romDao: RomDao,
    private val navigationManager: NavigationManager,
) : ViewModel() {

    val romsFlow: Flow<PagingData<RomEntity>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { romDao.getRoms() }
    ).flow.cachedIn(viewModelScope)


    fun processEvent(event: LibraryEvent) {
        viewModelScope.launch {
            when (event) {
                is LibraryEvent.RemoveSeed -> {}
            }
        }
    }
}
