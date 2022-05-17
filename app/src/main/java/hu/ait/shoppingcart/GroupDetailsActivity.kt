package hu.ait.shoppingcart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import hu.ait.shoppingcart.data.Group
import hu.ait.shoppingcart.data.Post
import hu.ait.shoppingcart.databinding.ActivityGroupDetailsBinding
import hu.ait.shoppingcart.databinding.ActivityGroupsBinding

class GroupDetailsActivity : AppCompatActivity() {
    /**
     * Posts and keys are retrieved into postsList and postsKey.
     * Use that for making adapter.
     * Check addPost method on hints on adding/creating a new post.
     **/
    private lateinit var  binding: ActivityGroupDetailsBinding
    companion object{
        var SUB_COLLECTION = "posts"
        var DOCUMENT_NAME = "group_id"
    }

    private lateinit var groupId:String
    private lateinit var postsList:MutableList<Post>
    private lateinit var postsKey:MutableList<String>
    private var listenerReg: ListenerRegistration? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent != null && intent.hasExtra(DOCUMENT_NAME)){
            groupId = intent.getStringExtra(DOCUMENT_NAME).toString()
        }

        initKeyAndPostList()
        initFirebaseQuery()
        addPost()

    }



    private fun initFirebaseQuery() {
        val queryRef =
            FirebaseFirestore.getInstance().collection(
                GroupsActivity.GROUPS_COLLECTION
            ).document(groupId).collection(SUB_COLLECTION)

        val eventListener = object : EventListener<QuerySnapshot> {
            override fun onEvent(querySnapshot: QuerySnapshot?, e: FirebaseFirestoreException?) {
                if (e != null) {
                    Toast.makeText(
                        this@GroupDetailsActivity, "Error: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                }

                for (docChange in querySnapshot?.getDocumentChanges()!!) {
                    if (docChange.type == DocumentChange.Type.ADDED) {
                        val post = docChange.document.toObject(Post::class.java)
                        addPost(post,docChange.document.id)
                        //notify item added - adapter
                    } else if (docChange.type == DocumentChange.Type.REMOVED) {
                        removePostByKey(docChange.document.id)
                        //notify item removed - adapter
                    } else if (docChange.type == DocumentChange.Type.MODIFIED) {

                    }
                }

            }
        }

        listenerReg = queryRef.addSnapshotListener(eventListener)
    }


    private fun initKeyAndPostList(){
        postsList = mutableListOf()
        postsKey = mutableListOf()
    }

    private fun addPost(post:Post, key:String){
        postsList.add(post)
        postsKey.add(key)
    }

    private fun removePostByKey(key:String){
        var index = postsKey.indexOf(key)
        postsList.removeAt(index)
        postsKey.removeAt(index)
    }

    private fun addPost(){
        val newPost = Post()
        val postsCollection = FirebaseFirestore.getInstance().collection(GroupsActivity.GROUPS_COLLECTION).document(groupId)
            .collection(SUB_COLLECTION)
        postsCollection.add(newPost)
            .addOnSuccessListener {
                Toast.makeText(this@GroupDetailsActivity,
                    "Post SAVED", Toast.LENGTH_LONG).show()
                binding.tvSampleText.text = "There are " + postsList.size +  " posts at the moment"


            }
            .addOnFailureListener{
                Toast.makeText(this@GroupDetailsActivity,
                    "Error ${it.message}", Toast.LENGTH_LONG).show()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        listenerReg?.remove()
    }
}