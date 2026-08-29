package com.stingers.alttpr.screens.edit

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.generate_rom_failed
import alttpr.shared.generated.resources.generate_seed_failed
import alttpr.shared.generated.resources.generating_seed
import alttpr.shared.generated.resources.save_rom_failed
import alttpr.shared.generated.resources.save_seed_failed
import alttpr.shared.generated.resources.save_seed_success
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stingers.alttpr.model.HeartColor
import com.stingers.alttpr.model.HeartSpeed
import com.stingers.alttpr.model.MenuSpeed
import com.stingers.alttpr.model.SeedEntity
import com.stingers.alttpr.model.Sprite
import com.stingers.alttpr.model.api.PaletteAlgorithm
import com.stingers.alttpr.navigation.NavigationManager
import com.stingers.alttpr.navigation.Screen
import com.stingers.alttpr.repository.local.RomPrefs
import com.stingers.alttpr.repository.local.SeedDao
import com.stingers.alttpr.repository.usecase.ExportRomUseCase
import com.stingers.alttpr.repository.usecase.GetRandomizerSeedUseCase
import com.stingers.alttpr.repository.usecase.GetSavedSpriteUseCase
import com.stingers.alttpr.repository.usecase.PlayRomUseCase
import com.stingers.alttpr.repository.usecase.SaveSeedUseCase
import com.stingers.alttpr.utils.combine
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class EditRomViewModel(
    @InjectedParam seed: SeedEntity,
    getSavedSpriteUseCase: GetSavedSpriteUseCase,
    private val seedDao: SeedDao,
    private val romPrefs: RomPrefs,
    private val saveSeedUseCase: SaveSeedUseCase,
    private val exportRomUseCase: ExportRomUseCase,
    private val playRomUseCase: PlayRomUseCase,
    private val getRandomizerSeedUseCase: GetRandomizerSeedUseCase,
    private val navigationManager: NavigationManager
) : ViewModel() {


    val state: StateFlow<EditRomState> = combine(
        romPrefs.quickSwap,
        romPrefs.reduceFlashing,
        romPrefs.enableMusic,
        romPrefs.msuResume,
        romPrefs.heartSpeed,
        romPrefs.menuSpeed,
        romPrefs.heartColor,
        romPrefs.shuffleSfx,
        romPrefs.paletteShuffle,
        romPrefs.paletteAlgorithm,
        getSavedSpriteUseCase(),
        seedDao.getSeedFlow(seed.hash)
    ) { quickSwap,
        reduceFlashing,
        enableMusic,
        msuResume,
        heartSpeed,
        menuSpeed,
        heartColor,
        shuffleSfx,
        paletteShuffle,
        paletteAlgorithm,
        sprite,
        savedSeed ->
        EditRomState(
            seed = seed,
            quickSwap = quickSwap,
            reduceFlashing = reduceFlashing,
            enableMusic = enableMusic,
            msuResume = msuResume,
            heartSpeed = heartSpeed,
            menuSpeed = menuSpeed,
            heartColor = heartColor,
            shuffleSfx = shuffleSfx,
            paletteShuffle = paletteShuffle,
            paletteAlgorithm = paletteAlgorithm,
            selectedSprite = sprite,
            isSaved = savedSeed != null
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = EditRomState(seed = seed, loading = true)
    )

    fun processEvent(event: EditRomEvent) {
        viewModelScope.launch {
            when (event) {
                is EditRomEvent.SetEnableMusic -> romPrefs.setEnableMusic(event.value)
                is EditRomEvent.SetHeartColor -> romPrefs.setHeartColor(event.value)
                is EditRomEvent.SetHeartSpeed -> romPrefs.setHeartSpeed(event.value)
                is EditRomEvent.SetMenuSpeed -> romPrefs.setMenuSpeed(event.value)
                is EditRomEvent.SetQuickSwap -> romPrefs.setQuickSwap(event.value)
                is EditRomEvent.SetReduceFlashing -> romPrefs.setReduceFlashing(event.value)
                is EditRomEvent.SetMsuResume -> romPrefs.setMsuResume(event.value)
                is EditRomEvent.SetSprite -> romPrefs.setSprite(event.value.name)
                is EditRomEvent.SetShuffleSfx -> romPrefs.setShuffleSfx(event.value)
                is EditRomEvent.SetPaletteShuffle -> romPrefs.setPaletteShuffle(event.value)
                is EditRomEvent.SetPaletteAlgorithm -> romPrefs.setPaletteAlgorithm(event.value)
                is EditRomEvent.ExportRom -> exportRom()
                is EditRomEvent.PlaySeed -> playRom()
                is EditRomEvent.ReRollSeed -> rerollSeed()
                is EditRomEvent.SaveSeed -> saveSeed()
                is EditRomEvent.DeleteSeed -> seedDao.deleteSeed(state.value.seed.hash)
                is EditRomEvent.OpenSpriteSelector -> navigationManager.navigateTo(Screen.Sprites)
            }
        }
    }

    private suspend fun saveSeed(onSuccess: (seed: SeedEntity) -> Unit = {}) {
        saveSeedUseCase(state.value.seed)
            .onSuccess {
                onSuccess(it)
                navigationManager.showToast(getString(Res.string.save_seed_success))
            }
            .onFailure {
                navigationManager.showToast(getString(Res.string.save_seed_failed))
            }
    }

    private suspend fun playRom() {
        playRomUseCase(state.value.seed).onFailure {
            navigationManager.showToast(getString(Res.string.save_rom_failed))
        }
    }

    private suspend fun exportRom() {
        exportRomUseCase(state.value.seed).onFailure {
            navigationManager.showToast(getString(Res.string.generate_rom_failed))
        }
    }

    suspend fun rerollSeed() {
        state.value.seed.request?.let {
            navigationManager.showToast(getString(Res.string.generating_seed))
            getRandomizerSeedUseCase(it)
                .onSuccess {
                    navigationManager.pop()
                    navigationManager.navigateTo(Screen.EditRom(it))
                }
                .onFailure {
                    navigationManager.showToast(getString(Res.string.generate_seed_failed))
                }
        }
    }
}

data class EditRomState(
    val seed: SeedEntity,
    val loading: Boolean = false,
    val heartSpeed: HeartSpeed = HeartSpeed.NORMAL,
    val menuSpeed: MenuSpeed = MenuSpeed.NORMAL,
    val heartColor: HeartColor = HeartColor.RED,
    val quickSwap: Boolean = true,
    val reduceFlashing: Boolean = false,
    val enableMusic: Boolean = true,
    val msuResume: Boolean = true,
    val shuffleSfx: Boolean = false,
    val paletteShuffle: Boolean = false,
    val paletteAlgorithm: PaletteAlgorithm = PaletteAlgorithm.Maseya,
    val selectedSprite: Sprite? = null,
    val isSaved: Boolean = false
)
