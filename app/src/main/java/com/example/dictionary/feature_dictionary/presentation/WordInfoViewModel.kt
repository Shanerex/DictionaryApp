package com.example.dictionary.feature_dictionary.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dictionary.core.util.Resource
import com.example.dictionary.feature_dictionary.domain.use_case.GetWordInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WordInfoViewModel @Inject constructor(
    private val getWordInfo: GetWordInfo
): ViewModel() {

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    private val _wordInfoState = mutableStateOf(WordInfoState())
    val wordInfoState: State<WordInfoState> = _wordInfoState

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    //the above are the required state variables

    private var searchJob: Job? = null
    fun onSearch(query: String) {
        _searchQuery.value = query
        searchJob?.cancel() // this is done to prevent calling the API/DB after you type a single character
        //make sure that the call to the API or DB is made only after the word is typed fully in the editText
        searchJob = viewModelScope.launch {
            delay(500L)// basically the search request is made after 5s on typing each character and since typing each character delays the coroutine by 5s, the search is made only after 5s after typing the whole word
            getWordInfo(query)
                .onEach { result ->
                    when(result) {
                        is Resource.Success -> {
                           _wordInfoState.value = wordInfoState.value.copy(
                               wordInfoItems = result.data ?: emptyList(),
                               isLoading = false
                           )
                        }
                        is Resource.Error -> {
                            _wordInfoState.value = wordInfoState.value.copy(
                                wordInfoItems = result.data ?: emptyList(),
                                isLoading = false
                            )

                            _eventFlow.emit(UIEvent.ShowSnackbar(
                                result.message ?: "Unknown error"
                            ))
                        }
                        is Resource.Loading -> {
                            _wordInfoState.value = wordInfoState.value.copy(
                                wordInfoItems = result.data ?: emptyList(),
                                isLoading = true
                            )
                        }
                    }
                }.launchIn(this) // this will launch the flow between the presentation layer and the data layer
        }
    }

    //the one-time events(like showing a snackbar for an error) are handled in the UIEvents class

    sealed class UIEvent {
        data class ShowSnackbar(val message: String): UIEvent()
    }
}