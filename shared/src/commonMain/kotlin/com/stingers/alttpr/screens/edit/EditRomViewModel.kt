package com.stingers.alttpr.screens.edit

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.generate_rom_failed
import alttpr.shared.generated.resources.generate_seed_failed
import alttpr.shared.generated.resources.save_rom_failed
import alttpr.shared.generated.resources.save_seed_failed
import alttpr.shared.generated.resources.save_seed_success
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stingers.alttpr.common.ROM_FILE_EXTENSION
import com.stingers.alttpr.common.ROM_FILE_EXTENSION_DOT
import com.stingers.alttpr.model.HeartColor
import com.stingers.alttpr.model.HeartSpeed
import com.stingers.alttpr.model.MenuSpeed
import com.stingers.alttpr.model.SeedEntity
import com.stingers.alttpr.model.Sprite
import com.stingers.alttpr.navigation.NavigationManager
import com.stingers.alttpr.navigation.Screen
import com.stingers.alttpr.repository.AlttprRepository
import com.stingers.alttpr.repository.RomManager
import com.stingers.alttpr.repository.local.RomPrefs
import com.stingers.alttpr.repository.local.RomStorage
import com.stingers.alttpr.repository.local.SeedDao
import com.stingers.alttpr.repository.usecase.GetRandomizerSeedUseCase
import com.stingers.alttpr.repository.usecase.SaveSeedUseCase
import com.stingers.alttpr.utils.combine
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.openFileSaver
import io.github.vinceglb.filekit.dialogs.openFileWithDefaultApplication
import io.github.vinceglb.filekit.write
import kotlinx.coroutines.flow.MutableStateFlow
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
    seedDao: SeedDao,
    private val romManager: RomManager,
    private val romPrefs: RomPrefs,
    private val alttprRepository: AlttprRepository,
    private val saveSeedUseCase: SaveSeedUseCase,
    private val getRandomizerSeedUseCase: GetRandomizerSeedUseCase,
    private val navigationManager: NavigationManager
) : ViewModel() {

    val sprites = MutableStateFlow(emptyList<Sprite>())

    val loading = MutableStateFlow<Boolean>(true)

    val state: StateFlow<EditRomState> = combine(
        loading,
        romPrefs.quickSwap,
        romPrefs.reduceFlashing,
        romPrefs.enableMusic,
        romPrefs.msuResume,
        romPrefs.heartSpeed,
        romPrefs.menuSpeed,
        romPrefs.heartColor,
        romPrefs.sprite,
        sprites,
        seedDao.getSeedFlow(seed.hash)
    ) { loading,
        quickSwap,
        reduceFlashing,
        enableMusic,
        msuResume,
        heartSpeed,
        menuSpeed,
        heartColor,
        sprite,
        sprites,
        savedSeed ->
        EditRomState(
            loading = loading,
            seed = seed,
            quickSwap = quickSwap,
            reduceFlashing = reduceFlashing,
            enableMusic = enableMusic,
            msuResume = msuResume,
            heartSpeed = heartSpeed,
            menuSpeed = menuSpeed,
            heartColor = heartColor,
            selectedSprite = sprites.find { it.name == sprite },
            availableSprites = sprites,
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
                is EditRomEvent.ExportRom -> exportRom()
                is EditRomEvent.PlaySeed -> playRom()
                is EditRomEvent.ReRollSeed -> rerollSeed()
                is EditRomEvent.SaveSeed -> saveSeed()
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
        saveSeed { seed ->
            viewModelScope.launch {
                val patchedBytes = getRomBytes(seed)
                patchedBytes?.let { rom ->
                    val fileName = getFileName(seed) + ROM_FILE_EXTENSION_DOT
                    RomStorage.saveShareRomBytes(fileName, rom)
                        .onSuccess {
                            RomStorage.getShareRomFile(fileName)?.let {
                                FileKit.openFileWithDefaultApplication(it)
                            }
                        }
                        .onFailure {
                            navigationManager.showToast(getString(Res.string.save_rom_failed))
                        }
                } ?: run {
                    navigationManager.showToast(getString(Res.string.generate_rom_failed))
                }
            }
        }
    }

    //
    private suspend fun exportRom() {
        saveSeed { seed ->
            viewModelScope.launch {
                val patchedBytes = getRomBytes(seed)
                patchedBytes?.let {
                    val file =
                        FileKit.openFileSaver(
                            suggestedName = getFileName(seed),
                            defaultExtension = ROM_FILE_EXTENSION
                        )
                    file?.write(it)
                } ?: run {
                    navigationManager.showToast(getString(Res.string.generate_rom_failed))
                }
            }
        }
    }

    private fun getFileName(seed: SeedEntity): String {
        return "alttpr - ${seed.meta?.getFileName().orEmpty()}_${seed.hash}"
    }

    //
    private suspend fun getRomBytes(seedEntity: SeedEntity): ByteArray? {
        return romManager.getPatchedRomBytes(
            seedEntity = seedEntity,
            heartSpeed = state.value.heartSpeed,
            menuSpeed = state.value.menuSpeed,
            heartColor = state.value.heartColor,
            quickSwap = state.value.quickSwap,
            reduceFlashing = state.value.reduceFlashing,
            enableMusic = state.value.enableMusic,
            msuResume = state.value.msuResume,
            sprite = state.value.selectedSprite
        )
    }

    suspend fun rerollSeed() {
        state.value.seed.request?.let {
            loading.value = true
            getRandomizerSeedUseCase(it)
                .onSuccess {
                    navigationManager.pop()
                    navigationManager.navigateTo(Screen.EditRom(it))
                    loading.value = false
                }
                .onFailure {
                    loading.value = false
                    navigationManager.showToast(getString(Res.string.generate_seed_failed))
                }
        }
    }

    fun getSprites() {
        viewModelScope.launch {
            val result = alttprRepository.getSprites()
            result.onSuccess { newSprites ->
                sprites.value = newSprites
            }
            loading.value = false
        }
    }

    init {
        getSprites()
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
    val availableSprites: List<Sprite> = emptyList(),
    val selectedSprite: Sprite? = null,
    val isSaved: Boolean = false
)
