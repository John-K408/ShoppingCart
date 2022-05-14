package hu.ait.shoppingcart.data

data class Post(
    var itemName:String = "",
    var quantity:Int = 0,
    var isBought: Boolean = false,
    var Price: Float = 0f)
