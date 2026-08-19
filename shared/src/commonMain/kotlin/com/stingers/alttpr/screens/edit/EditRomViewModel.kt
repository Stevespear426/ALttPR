package com.stingers.alttpr.screens.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stingers.alttpr.model.GameMode
import com.stingers.alttpr.model.HeartColor
import com.stingers.alttpr.model.HeartSpeed
import com.stingers.alttpr.model.MenuSpeed
import com.stingers.alttpr.model.RomEntity
import com.stingers.alttpr.model.Sprite
import com.stingers.alttpr.repository.AlttprRepository
import com.stingers.alttpr.repository.RomManager
import com.stingers.alttpr.repository.local.RomPrefs
import com.stingers.alttpr.repository.local.RomDao
import com.stingers.alttpr.utils.combine
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.openFileSaver
import io.github.vinceglb.filekit.write
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.toLocalDateTime
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class EditRomViewModel(
    @InjectedParam private val hash: String,
    private val romDao: RomDao,
    private val romManager: RomManager,
    private val romPrefs: RomPrefs,
    private val alttprRepository: AlttprRepository
) : ViewModel() {

    val sprites = MutableStateFlow(emptyList<Sprite>())

    val sprite = MutableStateFlow<Sprite?>(null)

    val state: StateFlow<EditRomState> = combine(
        romPrefs.quickSwap,
        romPrefs.reduceFlashing,
        romPrefs.enableMusic,
        romPrefs.msuResume,
        romPrefs.heartSpeed,
        romPrefs.menuSpeed,
        romPrefs.heartColor,
        sprite,
        sprites,
        romDao.getRomFlow(hash)
    ) { quickSwap,
        reduceFlashing,
        enableMusic,
        msuResume,
        heartSpeed,
        menuSpeed,
        heartColor,
        sprite,
        sprites,
        romEntity ->
        EditRomState(
            hash = hash,
            quickSwap = quickSwap,
            reduceFlashing = reduceFlashing,
            enableMusic = enableMusic,
            msuResume = msuResume,
            heartSpeed = heartSpeed,
            menuSpeed = menuSpeed,
            heartColor = heartColor,
            selectedSprite = sprite,
            availableSprites = sprites,
            romEntity = romEntity
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = EditRomState(hash = hash, loading = true)
    )

    fun processEvent(event: EditRomEvent) {
        viewModelScope.launch {
            when (event) {
                is EditRomEvent.SaveFile -> saveRom()
                is EditRomEvent.ShareFile -> shareRom()
                is EditRomEvent.SetEnableMusic -> romPrefs.setEnableMusic(event.value)
                is EditRomEvent.SetHeartColor -> romPrefs.setHeartColor(event.value)
                is EditRomEvent.SetHeartSpeed -> romPrefs.setHeartSpeed(event.value)
                is EditRomEvent.SetMenuSpeed -> romPrefs.setMenuSpeed(event.value)
                is EditRomEvent.SetQuickSwap -> romPrefs.setQuickSwap(event.value)
                is EditRomEvent.SetReduceFlashing -> romPrefs.setReduceFlashing(event.value)
                is EditRomEvent.SetMsuResume -> romPrefs.setMsuResume(event.value)
                is EditRomEvent.SetSprite -> {
                    sprite.value = event.value
                }
            }
        }
    }

    private suspend fun saveRom() {
        val romEntity = romDao.getRom(hash) ?: return
        val patchedBytes = getRomBytes(romEntity) ?: return
        val file = FileKit.openFileSaver(suggestedName = getFileName(romEntity), defaultExtension = "sfc")
        file?.write(patchedBytes)
    }

    private suspend fun shareRom() {
        val romEntity = romDao.getRom(hash) ?: return
        val patchedBytes = getRomBytes(romEntity) ?: return
        val filename = getFileName(romEntity) + ".sfc"
        val file = FileKit.openFileSaver(suggestedName = filename, defaultExtension = "sfc")
        file?.write(patchedBytes)
    }

    private fun getFileName(romEntity: RomEntity): String {
       return if (romEntity.gameMode == GameMode.DAILY_CHALLENGE) {
             "alttpr - ${romEntity.meta?.logic.orEmpty()}-${romEntity.meta?.mode.orEmpty()}-${romEntity.meta?.goal.orEmpty()}_$hash"
        } else {
            val instant = kotlinx.datetime.Instant.fromEpochMilliseconds(romEntity.createdAt)
            val datetime = instant.toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault())
            val dateString = "${datetime.month.name.take(3).lowercase().replaceFirstChar { it.uppercase() }} ${datetime.day}, ${datetime.year}"
           "alttpr - Daily Challenge_${dateString}_$hash"
        }
    }

    private suspend fun getRomBytes(romEntity: RomEntity): ByteArray? {
        return romManager.getPatchedRomBytes(
            romEntity = romEntity,
            hash = hash,
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

    fun getSprites() {
        viewModelScope.launch {
            val result = alttprRepository.getSprites()
            result.onSuccess { newSprites ->
                sprites.value = newSprites
            }
        }
    }

    init {
        getSprites()
    }
}

data class EditRomState(
    val hash: String,
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
    val romEntity: RomEntity? = null
)
