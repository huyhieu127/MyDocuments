package com.huyhieu.mydocuments.solid

fun main() {
    val square = Shape.Square(5.0)
    val rectangle = Shape.Rectangle(4.0, 6.0)
    println("Square area: ${square.area()}")
    println("Rectangle area: ${rectangle.area()}")
}

/**
 * GOOD EXAMPLE
 * */
interface Calculate {
    fun area(): Double
}

sealed class Shape : Calculate {
    class Square(private val side: Double) : Shape() {
        override fun area(): Double {
            return side * side
        }
    }

    class Rectangle(val width: Double, val height: Double) : Shape() {
        override fun area(): Double {
            return width * height
        }
    }
}

/**
 * BAD EXAMPLE
 * */
open class Rectangle(open var width: Double, open var height: Double) {
    open fun area(): Double {
        return width * height
    }
}

class Square(side: Double) : Rectangle(side, side) {
    override var width: Double
        get() = super.width
        set(value) {
            super.width = value
            super.height = value
        }
    override var height: Double
        get() = super.height
        set(value) {
            super.width = value
            super.height = value
        }
}