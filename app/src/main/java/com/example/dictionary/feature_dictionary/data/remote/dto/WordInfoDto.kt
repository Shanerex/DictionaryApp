package com.example.dictionary.feature_dictionary.data.remote.dto

import com.example.dictionary.feature_dictionary.data.local.entity.WordInfoEntity
import com.example.dictionary.feature_dictionary.domain.model.WordInfo

data class WordInfoDto(
    val meanings: List<MeaningDto>,
    val origin: String,
    val phonetic: String,
    val phonetics: List<PhoneticDto>,
    val word: String
) {
    fun toWordInfo(): WordInfo {
        return WordInfo(
            meanings = meanings.map { it.toMeaning() },
            origin = origin,
            phonetic = phonetic,
            word = word
        )
    }

    fun toWordInfoEntity(): WordInfoEntity {
        return WordInfoEntity(
            word = word,
            phonetic = phonetic,
            origin = origin,
            meanings = meanings.map { it.toMeaning() },
        )
    }
}