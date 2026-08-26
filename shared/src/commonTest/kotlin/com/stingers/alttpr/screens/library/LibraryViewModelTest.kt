package com.stingers.alttpr.screens.library

import androidx.paging.PagingSource
import com.stingers.alttpr.model.SeedEntity
import com.stingers.alttpr.repository.local.SeedDao
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull

@OptIn(ExperimentalCoroutinesApi::class)
class LibraryViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val seedDao = mock<SeedDao>()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { seedDao.getSeeds() } returns object : PagingSource<Int, SeedEntity>() {
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SeedEntity> {
                return LoadResult.Page(emptyList(), null, null)
            }
            override fun getRefreshKey(state: androidx.paging.PagingState<Int, SeedEntity>): Int? = null
        }
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test romsFlow is created successfully`() = runTest(testDispatcher) {
        val viewModel = LibraryViewModel(seedDao = seedDao)
        assertNotNull(viewModel.romsFlow)
    }
}
