package com.stingers.alttpr.screens.sprites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stingers.alttpr.model.Sprite
import com.stingers.alttpr.navigation.NavigationManager
import com.stingers.alttpr.repository.AlttprRepository
import com.stingers.alttpr.repository.local.RomPrefs
import com.stingers.alttpr.repository.usecase.GetSavedSpriteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class SpriteViewModel(
    private val alttprRepository: AlttprRepository,
    private val romPrefs: RomPrefs,
    getSavedSpriteUseCase: GetSavedSpriteUseCase,
    private val navigationManager: NavigationManager
) : ViewModel() {

    private val loading = MutableStateFlow(true)
    private val sprites = MutableStateFlow(emptyList<Sprite>())
    private val searchQuery = MutableStateFlow("")

    val state: StateFlow<SpriteState> = combine(
        loading,
        sprites,
        searchQuery,
        getSavedSpriteUseCase()
    ) { loading, sprites, searchQuery, selectedSprite ->
        val filteredSprites = if (searchQuery.isBlank()) {
            sprites
        } else {
            sprites.filter { sprite ->
                sprite.name.contains(searchQuery, ignoreCase = true) ||
                        sprite.author.contains(searchQuery, ignoreCase = true) ||
                        sprite.tags.any { it.contains(searchQuery, ignoreCase = true) }
            }
        }
        SpriteState(
            loading = loading,
            sprites = filteredSprites,
            selectedSprite = selectedSprite,
            searchQuery = searchQuery
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = SpriteState(loading = true)
    )

    init {
        fetchSprites()
    }

    private fun fetchSprites() {
        viewModelScope.launch {
            loading.value = true
            alttprRepository.getSprites().onSuccess {
                sprites.value = it
            }
            loading.value = false
        }
    }

    fun processEvent(event: SpriteEvent) {
        viewModelScope.launch {
            when (event) {
                is SpriteEvent.SelectSprite -> {
                    romPrefs.setSprite(event.sprite.name)
                    navigationManager.pop()
                }
                is SpriteEvent.UpdateSearchQuery -> {
                    searchQuery.value = event.query
                }
                is SpriteEvent.OnBackClick -> {
                    navigationManager.pop()
                }
            }
        }
    }
}

data class SpriteState(
    val loading: Boolean = false,
    val sprites: List<Sprite> = emptyList(),
    val selectedSprite: Sprite? = null,
    val searchQuery: String = ""
)
