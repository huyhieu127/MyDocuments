package com.huyhieu.mydocuments

fun main() {
    val list = listOf(Obj(id = 2), Obj(id = 2), Obj(name = "Y"), Obj(), Obj(id = 2, name = "Y"))
    println(list)
    println(list.distinct())
    val set = list.toSet()
    println(set)
}

data class Obj(
    var id: Int = 1,
    var name: String = "X"
)