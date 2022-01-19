package com.example.dictionary.feature_dictionary.data.util

import java.lang.reflect.Type

//the reason for creating this interface is that if in future you want to change the parser from gson parser to some other parser, then all you need to do is just change the implementation of this interface
//otherwise you need to manually change the implementation of gson parser everywhere in the code to your new parser
interface JsonParser {

    fun <T> fromJson(json: String, type: Type): T?

    fun <T> toJson(obj: T, type: Type): String?
}