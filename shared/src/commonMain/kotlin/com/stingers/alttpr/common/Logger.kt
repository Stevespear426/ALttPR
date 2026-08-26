package com.stingers.alttpr.common

import com.stingers.alttpr.model.LogEntity
import com.stingers.alttpr.model.LogType
import com.stingers.alttpr.repository.local.AppPrefs
import com.stingers.alttpr.repository.local.LoggerDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import org.koin.core.annotation.Singleton

@Singleton
class Logger(
    private val appPrefs: AppPrefs,
    private val loggerDao: LoggerDao
) {

    val scope = CoroutineScope(Dispatchers.IO + Job())

    private val isLoggingEnabled = appPrefs.debugMode.stateIn(scope, SharingStarted.Eagerly, false)

    fun d(tag: String, message: String) {
        if (isLoggingEnabled.value) {
            debug(tag, message)
            loggerDao.insertLog(
                LogEntity(
                    LogType.DEBUG,
                    tag,
                    message,
                )
            )
        }
    }

    fun e(tag: String, message: String, error: Throwable) {
        if (isLoggingEnabled.value) {
            exception(tag, message, error)
            loggerDao.insertLog(
                LogEntity(
                    LogType.ERROR,
                    tag,
                    message,
                    error.stackTraceToString().take(STACKTRACE_LENGTH)
                )
            )
        }

    }

    fun i(tag: String, message: String) {
        if (isLoggingEnabled.value) {
            info(tag, message)
            loggerDao.insertLog(
                LogEntity(
                    LogType.INFO,
                    tag,
                    message,
                )
            )
        }
    }

    fun w(tag: String, message: String) {
        if (isLoggingEnabled.value) {
            warn(tag, message)
            loggerDao.insertLog(
                LogEntity(
                    LogType.WARN,
                    tag,
                    message,
                )
            )
        }
    }

    fun wtf(tag: String, message: String) {
        if (isLoggingEnabled.value) {
            whatTheF(tag, message)
            loggerDao.insertLog(
                LogEntity(
                    LogType.WTF,
                    tag,
                    message,
                )
            )
        }
    }
}

expect fun debug(tag: String, message: String)
expect fun exception(tag: String, message: String, error: Throwable)
expect fun info(tag: String, message: String)
expect fun warn(tag: String, message: String)
expect fun whatTheF(tag: String, message: String)
