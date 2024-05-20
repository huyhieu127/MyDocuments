package com.huyhieu.mydocuments.models

data class User(
    val id: Long = 0,
    val name: String,
    val age: Int,

    var isSelected: Boolean = false,
) {
    override fun equals(other: Any?): Boolean {
        return (other as? User)?.id == id
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + age
        return result
    }
}