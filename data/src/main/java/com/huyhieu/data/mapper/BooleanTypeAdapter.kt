package com.huyhieu.data.mapper

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import java.lang.reflect.Type


class BooleanTypeAdapter : JsonDeserializer<Boolean> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Boolean {
        if ((json as JsonPrimitive).isBoolean) {
            return json.getAsBoolean()
        }
        if (json.isString) {
            val jsonValue = json.getAsString()
            return when {
                jsonValue.equals("true", ignoreCase = true) -> {
                    true
                }
                jsonValue.equals("false", ignoreCase = true) -> {
                    false
                }
                else -> {
                    false
                }
            }
        }

        val code = json.getAsInt()
        return if (code == 0) false else (code == 1)
    }

}
