package hu.ait.shoppingcart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.ait.shoppingcart.databinding.ActivityAddShoppingBinding

class AddShoppingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddShoppingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddShoppingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}