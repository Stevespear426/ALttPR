package com.stingers.alttpr.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stingers.alttpr.model.LogEntity
import com.stingers.alttpr.model.LogType
import com.stingers.alttpr.navigation.NavigationManager
import com.stingers.alttpr.repository.local.LoggerDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
class LogsViewModel(
    @Provided private val loggerDao: LoggerDao,
    private val navigationManager: NavigationManager
) : ViewModel() {

    private val _logFilter = MutableStateFlow("")
    val logFilter = _logFilter.asStateFlow()

    private val _logType = MutableStateFlow<LogType?>(null)
    val logType = _logType.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val logs = combine(
        logType,
        logFilter
    ) { type, filter ->
        type?.name to filter
    }.flatMapLatest { (typeName, filter) ->
        loggerDao.getLogs(typeName, filter)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val state = combine(logs, logFilter, logType) { logs, filter, type ->
        LogsState(filter, type, logs)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, LogsState())


    fun processEvent(event: LogsEvent) {
        viewModelScope.launch {
            when (event) {
                LogsEvent.ClearLogs -> loggerDao.deleteAllLogs()
                LogsEvent.NavigateBack -> navigationManager.pop()
                is LogsEvent.SetLogFilter -> _logFilter.value = event.value
                is LogsEvent.SetLogType -> _logType.value = event.value
            }
        }

    }
}

data class LogsState(
    val filter: String = "",
    val logType: LogType? = null,
    val logs: List<LogEntity> = emptyList()
)

