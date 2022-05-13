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
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import hu.ait.shoppingcart.adapter.groupsAdapter
import hu.ait.shoppingcart.data.Group
import hu.ait.shoppingcart.databinding.ActivityGroupsBinding

class GroupsActivity : AppCompatActivity() {

    companion object{
        var GROUPS_COLLECTION = "groups"
    }
    private var listenerReg: ListenerRegistration? = null
    private lateinit var  binding: ActivityGroupsBinding
    private lateinit var adapter:groupsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()
        initFirebaseQuery()
    }

    private fun initRecyclerView(){
        adapter = groupsAdapter(this,FirebaseAuth.getInstance().currentUser!!.uid)
        binding.recyclerItem.adapter = adapter
    }

    private fun initFirebaseQuery() {
        val queryRef =
            FirebaseFirestore.getInstance().collection(
                GROUPS_COLLECTION)

        val eventListener = object : EventListener<QuerySnapshot> {
            override fun onEvent(querySnapshot: QuerySnapshot?, e: FirebaseFirestoreException?) {
                if (e != null) {
                    Toast.makeText(
                        this@GroupsActivity, "Error: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                }

                for (docChange in querySnapshot?.getDocumentChanges()!!) {
                    if (docChange.type == DocumentChange.Type.ADDED) {
                        val group = docChange.document.toObject(Group::class.java)
                        adapter.addGroup(group, docChange.document.id)
                    } else if (docChange.type == DocumentChange.Type.REMOVED) {
                        adapter.removePostByKey(docChange.document.id)
                    } else if (docChange.type == DocumentChange.Type.MODIFIED) {

                    }
                }

            }
        }

        listenerReg = queryRef.addSnapshotListener(eventListener)
    }


    override fun onDestroy() {
        super.onDestroy()
        listenerReg?.remove()
    }

}