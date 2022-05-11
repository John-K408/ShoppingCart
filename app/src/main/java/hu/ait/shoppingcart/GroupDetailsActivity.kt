package hu.ait.shoppingcart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.ait.shoppingcart.databinding.ActivityGroupDetailsBinding
import hu.ait.shoppingcart.databinding.ActivityGroupsBinding

class GroupDetailsActivity : AppCompatActivity() {
    private lateinit var  binding: ActivityGroupDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}