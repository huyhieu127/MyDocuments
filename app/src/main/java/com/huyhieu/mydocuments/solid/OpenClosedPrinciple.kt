package com.huyhieu.mydocuments.solid

fun main() {
    var product = Product(1, "Laptop", 10_000_000, 2, 2)
    val paymentController = PaymentController()
    paymentController.processPayment(product)

    val sum = 10_000 addX2 20
    println(sum)
}

infix fun Int.addX2(value: Long): Long {
    return this + value * 2
}

data class Product(
    var id: Int = 0,
    var name: String = "",
    var price: Long = 0L,
    var amount: Int = 0,
    var type: Int = PaymentType.CREDIT_CARD
)

object PaymentType {
    const val CREDIT_CARD = 1
    const val PAYPAL = 2
}

/**
 * GOOD EXAMPLE
 * */
interface PaymentProcessor {
    fun payment(product: Product): Long
}

class CreditCardPayment : PaymentProcessor {
    override fun payment(product: Product): Long {
        val total = product.price * product.amount - 100_000
        println("Total CreditCardPayment : $total")
        return total
    }
}

class PaypalPayment : PaymentProcessor {
    override fun payment(product: Product): Long {
        val total = product.price * product.amount
        println("Total PaypalPayment : $total")
        return total
    }
}

object PaymentProcessorFactory {
    fun processPayment(product: Product): PaymentProcessor {
        return when (product.type) {
            PaymentType.CREDIT_CARD -> CreditCardPayment()
            PaymentType.PAYPAL -> PaypalPayment()
            else -> throw IllegalArgumentException("Invalid payment type")
        }
    }
}

class PaymentController {
    fun processPayment(product: Product) {
        val paymentProcessor = PaymentProcessorFactory.processPayment(product)
        val total = paymentProcessor.payment(product)
        println("Total : $total - $paymentProcessor")
    }
}

/**
 * BAD EXAMPLE
 * */
class PaymentProcessorBad {
    fun processPayment(product: Product) {
        when (product.type) {
            PaymentType.CREDIT_CARD -> processCreditCardPayment(product)
            PaymentType.PAYPAL -> processPayPalPayment(product)
            // More payment types...
        }
    }

    private fun processCreditCardPayment(product: Product) {
        // Process credit card payment
    }

    private fun processPayPalPayment(product: Product) {
        // Process PayPal payment
    }

}