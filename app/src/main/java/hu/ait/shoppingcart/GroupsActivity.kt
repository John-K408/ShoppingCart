package hu.ait.shoppingcart
/*

*This activity is the first activity that shows up after a user logs in.

*This activity receives the signed in user's id as an extra which it will use to retrieve information
from firebase about the groups the user is part of.

* The activity gets the groups a user is part of from our database and shows them in a list view.

*This activity also gives a user the chance to create a group or join an existing one.

*When a user clicks any of these groups, an intent is created with an id from the chosen group as
an extra and this is sent to the GroupDetails activity so that activity can retrieve the details of
this group from the database

* */
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.ait.shoppingcart.databinding.ActivityGroupsBinding

class GroupsActivity : AppCompatActivity() {
    private lateinit var  binding: ActivityGroupsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}