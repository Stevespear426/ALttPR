package com.stingers.alttpr.screens.settings

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.all_logs
import alttpr.shared.generated.resources.filter_log_tags
import alttpr.shared.generated.resources.ic_arrow_drop_down
import alttpr.shared.generated.resources.ic_close
import alttpr.shared.generated.resources.ic_delete
import alttpr.shared.generated.resources.ic_more_vert
import alttpr.shared.generated.resources.logs
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.stingers.alttpr.common.components.HeaderPage
import com.stingers.alttpr.model.LogType
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun LogsScreen(viewModel: LogsViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    HeaderPage(
        title = "",
        topBar = { LogsHeader(state.logType, viewModel::processEvent) }
    ) { padding ->
        MainContent(padding, state, viewModel::processEvent)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogsHeader(
    logType: LogType?,
    processEvent: (event: LogsEvent) -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
        title = {
            Text(stringResource(Res.string.logs), maxLines = 1, overflow = TextOverflow.Ellipsis)
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    processEvent(LogsEvent.NavigateBack)
                }
            ) {
                Icon(
                    painterResource(Res.drawable.ic_close),
                    contentDescription = "Close Button"
                )
            }
        },
        actions = {
            IconButton(onClick = {
                processEvent(LogsEvent.ClearLogs)
            }) {
                Icon(
                    painter = painterResource(Res.drawable.ic_delete),
                    contentDescription = "Burn Icon"
                )
            }

            Box {
                var expanded by remember { mutableStateOf(false) }
                IconButton(onClick = {
                    expanded = true
                }) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_more_vert),
                        "More",
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.clip(RoundedCornerShape(25.dp))
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(Res.string.all_logs)) },
                        onClick = {
                            expanded = false
                            processEvent(LogsEvent.SetLogType(null))
                        },
                        leadingIcon = {
                            Checkbox(logType == null, onCheckedChange = {
                                processEvent(LogsEvent.SetLogType(null))
                            })
                        }
                    )

                    LogType.entries.forEach { log ->
                        DropdownMenuItem(
                            text = { Text(stringResource(log.res)) },
                            onClick = {
                                expanded = false
                                processEvent(LogsEvent.SetLogType(log))
                            },
                            leadingIcon = {
                                Checkbox(logType == log, onCheckedChange = {
                                    processEvent(LogsEvent.SetLogType(log))
                                })
                            }
                        )
                    }
                }
            }
        },
    )
}

@Composable
fun MainContent(
    padding: PaddingValues,
    state: LogsState,
    processEvent: (event: LogsEvent) -> Unit
) {
    with(state) {
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            stickyHeader {
                SearchBar(filter, processEvent)
            }
            items(logs) { log ->
                var expanded by mutableStateOf(false)
                with(log) {
                    Card(
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, SolidColor(log.type.color)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        Column(Modifier.padding(10.dp)) {
                            Row(Modifier.fillMaxWidth()) {
                                Text(
                                    tag,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = log.type.color,
                                    modifier = Modifier.weight(1f)
                                )
                                if (stacktrace.isNotEmpty()) {
                                    Icon(
                                        painter = painterResource(Res.drawable.ic_arrow_drop_down),
                                        contentDescription = "Back Arrow",
                                        modifier = Modifier.rotate(if (expanded) 180f else 0f)
                                            .clickable { expanded = !expanded }
                                    )
                                }
                            }
                            Text(
                                message,
                                style = MaterialTheme.typography.bodyLarge,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 20
                            )
                            if (expanded) {
                                Text(
                                    stacktrace,
                                    style = MaterialTheme.typography.bodyLarge,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 20
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(searchText: String, processEvent: (event: LogsEvent) -> Unit) {
    TextField(
        colors = TextFieldDefaults.colors(
//            focusedTextColor = getBarTextColor(),
            cursorColor = MaterialTheme.colorScheme.secondary,
            disabledTextColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            selectionColors = TextSelectionColors(
                MaterialTheme.colorScheme.secondary,
                MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
            )
        ),
        value = searchText,
        onValueChange = {
            processEvent(LogsEvent.SetLogFilter(it))
        },
        placeholder = {
            Text(
                stringResource(Res.string.filter_log_tags),
//                color = getBarTextColor().copy(alpha = 0.65F),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.None,
            capitalization = KeyboardCapitalization.Words
        ),
    )
}
