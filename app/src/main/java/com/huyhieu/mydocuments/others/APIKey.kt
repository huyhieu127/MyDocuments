package com.huyhieu.mydocuments.others

import androidx.annotation.StringDef

@Target(AnnotationTarget.CLASS, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPE)
@StringDef(
    APIKey.API_1,
    APIKey.API_2,
    APIKey.API_3,
)
annotation class APIKey {
    companion object {
        const val API_1 = "API_1"
        const val API_2 = "API_2"
        const val API_3 = "API_3"
    }

}