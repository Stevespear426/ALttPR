package com.stingers.alttpr.screens.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stingers.alttpr.model.HeartColor
import com.stingers.alttpr.model.HeartSpeed
import com.stingers.alttpr.model.MenuSpeed
import com.stingers.alttpr.model.RomEntity
import com.stingers.alttpr.repository.RomManager
import com.stingers.alttpr.repository.RomPrefs
import com.stingers.alttpr.repository.local.RomDao
import com.stingers.alttpr.utils.combine
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.openFileSaver
import io.github.vinceglb.filekit.write
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class EditRomViewModel(
    @InjectedParam private val hash: String,
    private val romDao: RomDao,
    private val romManager: RomManager,
    private val romPrefs: RomPrefs
) : ViewModel() {

    val state: StateFlow<EditRomState> = combine(
        romPrefs.quickSwap,
        romPrefs.reduceFlashing,
        romPrefs.enableMusic,
        romPrefs.msuResume,
        romPrefs.heartSpeed,
        romPrefs.menuSpeed,
        romPrefs.heartColor,
        romDao.getRomFlow(hash)
    ) { quickSwap,
        reduceFlashing,
        enableMusic,
        msuResume,
        heartSpeed,
        menuSpeed,
        heartColor,
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
            }
        }
    }

    private suspend fun saveRom() {
        val romEntity = romDao.getRom(hash) ?: return
        val patchedBytes = getRomBytes(romEntity) ?: return
        val filename = romEntity.localFileName.removeSuffix(".bps") + ".sfc"
        val file = FileKit.openFileSaver(suggestedName = filename, defaultExtension = "sfc")
        file?.write(patchedBytes)
    }

    private suspend fun shareRom() {
        val romEntity = romDao.getRom(hash) ?: return
        val patchedBytes = getRomBytes(romEntity) ?: return
        val filename = romEntity.localFileName.removeSuffix(".bps") + ".sfc"
        val file = FileKit.openFileSaver(suggestedName = filename, defaultExtension = "sfc")
        file?.write(patchedBytes)
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
            msuResume = state.value.msuResume
        )
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
    val romEntity: RomEntity? = null
)
