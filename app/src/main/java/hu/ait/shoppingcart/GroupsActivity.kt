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

*Martina: You can send me your email so i give you access to the same firestore project I created there
 so we have one place for all the data.

 *Lastly, the join feature only works when the original group was created on a different device (or laptop in this case)

* */
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import hu.ait.shoppingcart.adapter.groupsAdapter
import hu.ait.shoppingcart.data.AppDatabase
import hu.ait.shoppingcart.data.Group
import hu.ait.shoppingcart.data.Post
import hu.ait.shoppingcart.data.groupId
import hu.ait.shoppingcart.databinding.ActivityGroupsBinding
import hu.ait.shoppingcart.dialog.addGroupDialog
import kotlin.concurrent.thread

class GroupsActivity : AppCompatActivity(),addGroupDialog.CartHandler {

    companion object{
        var GROUPS_COLLECTION = "groups"
    }
    private var listenerReg: ListenerRegistration? = null
    private lateinit var  binding: ActivityGroupsBinding
    private lateinit var adapter:groupsAdapter
    private lateinit var document:DocumentSnapshot
    lateinit var groupIds:MutableList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()
        thread{
            groupIds = getGroupIds(AppDatabase.getInstance(this@GroupsActivity).groupDao().getAllGroups())
            initFirebaseQuery()
            binding.fab.setOnClickListener {
                addGroupDialog().show(supportFragmentManager,"ADD_GROUP_DIALOG_TAG")
            }
        }

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
                        if((group.hostId == FirebaseAuth.getInstance().currentUser!!.uid) && !(docChange.document.id in groupIds) ){
                            thread{
                                AppDatabase.getInstance(this@GroupsActivity).groupDao().insertGroup(groupId(null,docChange.document.id))
                                runOnUiThread {
                                    groupIds.add(docChange.document.id)
                                    adapter.filter(groupIds)
                                }
                            }


                        }
                        else{
                            thread{
                                runOnUiThread { adapter.filter(groupIds)
                                }
                            }

                        }


                    } else if (docChange.type == DocumentChange.Type.REMOVED) {
                        adapter.removePostByKey(docChange.document.id)
                        thread{
                            AppDatabase.getInstance(this@GroupsActivity).groupDao().deleteGroup(groupId(null,docChange.document.id))
                        }

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

    override fun groupCreated(groupName: String) {
        val newGroup = Group(groupName,FirebaseAuth.getInstance().currentUser!!.uid)
        val groupsCollection = FirebaseFirestore.getInstance().collection(GROUPS_COLLECTION)
        groupsCollection.add(newGroup)
            .addOnSuccessListener {
                Toast.makeText(this@GroupsActivity,
                    "Group SAVED", Toast.LENGTH_LONG).show()

            }
            .addOnFailureListener{
                Toast.makeText(this@GroupsActivity,
                    "Error ${it.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun getGroupIds(groups:List<groupId>):MutableList<String>{
        var newgroupIds = mutableListOf<String>()
        for(id in groups){
            newgroupIds.add(id.groupId)
        }
        return newgroupIds

    }

    override fun groupJoined(groupId: String) {
        thread{
            AppDatabase.getInstance(this@GroupsActivity).groupDao().insertGroup(groupId(null,groupId))

             var group: Group? = adapter.findGroupInMaster(groupId)
            if(group == null){
                //group does not exist
                    runOnUiThread {
                        Toast.makeText(this@GroupsActivity,
                            "No Group With Such An  ID Exists", Toast.LENGTH_LONG).show()
                    }


            }
            else{
                runOnUiThread {
                    adapter.addGroup(group,groupId)
                }

            }

        }
    }

}