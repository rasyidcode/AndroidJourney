package me.jamilalrasyidis.eatit.data

data class Food(
    var name: String = "",
    var image: String = "",
    var description: String = "",
    var price: Int = 0,
    var discount: Int = 0,
    var categoryId: Int = 0
)