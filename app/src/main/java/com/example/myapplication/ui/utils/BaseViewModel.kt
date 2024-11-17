package com.example.myapplication.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.launch

class BaseViewModel<STATE, OUTPUT_EVENT, MESSAGE>(
    initialState: STATE,
    initialEvents: Set<MESSAGE> = emptySet(),
    processInputEvent: suspend (event: MESSAGE, state: STATE) -> Pair<STATE, Set<OUTPUT_EVENT>>
) : ViewModel() {
    private val _screenState = MutableStateFlow(initialState)
    private val scope: CoroutineScope = viewModelScope

    /** Состояние экрана */
    val screenState = _screenState.asStateFlow()
    private val _outputEvent = MutableSharedFlow<OUTPUT_EVENT>(extraBufferCapacity = 1)

    /** События для экрана */
    val events = _outputEvent.asSharedFlow()

    /** События, выполнякмые [BaseViewModel] */
    private val _processingEvents = MutableSharedFlow<MESSAGE>(extraBufferCapacity = 1)

    init {
        scope.launch {
            _processingEvents
                .onSubscription { initialEvents.forEach { syncDispatch(it) } }
                .collect {
                    CoroutineScope(Dispatchers.Default).launch {
                        processInputEvent(it, _screenState.value)
                            .let { (state, outputs) ->
                                _screenState.emit(state)
                                outputs.forEach { _outputEvent.emit(it) }
                            }
                    }
                }
        }
    }

    /** Отправка собыия */
    fun dispatch(msg: MESSAGE) {
        scope.launch { syncDispatch(msg) }
    }

    private suspend infix fun syncDispatch(msg: MESSAGE) {
        _processingEvents.emit(msg)
    }
}

@Composable
fun <S, E, M> BaseViewModel<S, E, M>.SubscribeEvents(
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    sideEffect: suspend (sideEffect: E) -> Unit
) {
    val sideEffectFlow = this.events
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(sideEffectFlow, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(lifecycleState) {
            sideEffectFlow.collect { sideEffect(it) }
        }
    }
}
