package com.example.dictionary.feature_dictionary.domain.use_case

import com.example.dictionary.core.util.Resource
import com.example.dictionary.feature_dictionary.domain.model.WordInfo
import com.example.dictionary.feature_dictionary.domain.repository.WordInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

//this use case will be used by the viewmodel to get the wordinfos
//basically the viewmodel will call the use case, forward the result to the UI. Viewmodel takes the result from the use case and maps the result to the corresponding compose state in the UI
class GetWordInfo(
    private val repository: WordInfoRepository
) {

    operator fun invoke(word: String): Flow<Resource<List<WordInfo>>> {

        if(word.isBlank()) {
            return flow {  }
        }

        return repository.getWordInfo(word)
    }
}