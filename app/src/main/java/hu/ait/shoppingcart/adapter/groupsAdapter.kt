package hu.ait.shoppingcart.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import hu.ait.shoppingcart.GroupDetailsActivity
import hu.ait.shoppingcart.GroupsActivity
import hu.ait.shoppingcart.data.AppDatabase
import hu.ait.shoppingcart.data.Group
import hu.ait.shoppingcart.data.groupId
import hu.ait.shoppingcart.databinding.GroupRowBinding
import kotlin.concurrent.thread

class groupsAdapter(var context: Context, uid: String) :
    RecyclerView.Adapter<groupsAdapter.ViewHolder>() {

    var masterGroupList = mutableListOf<Group>()
    var masterKeysList = mutableListOf<String>()

    var  groupList = mutableListOf<Group>()
    var  groupKeys = mutableListOf<String>()

     var currentUid: String = uid


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GroupRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var group = groupList[position]
        var key  = groupKeys[position]
        holder.tvGroupName.text = group.groupName
        if(currentUid == group.hostId){
            holder.btnDelete.visibility = View.VISIBLE
            holder.btnDelete.setOnClickListener {
                FirebaseFirestore.getInstance().collection(GroupsActivity.GROUPS_COLLECTION).document(
                   key
                ).delete()

            }

        }

        holder.btnLeave.setOnClickListener {
            //set read and write capability to smth that can be enabled when user joins or adds a group.
            //disable this capability when user leaves group
            if(currentUid == group.hostId){
                FirebaseFirestore.getInstance().collection(GroupsActivity.GROUPS_COLLECTION).document(
                    key
                ).delete()
            }
            else{
                thread{
                    AppDatabase.getInstance((context as GroupsActivity)).groupDao().deleteGroup(groupId(null,key))
                }

                removeGroupFromList(position)

            }

        }
        holder.cvCardView.setOnClickListener{
            val intent = Intent()
            intent.setClass((context as GroupsActivity),GroupDetailsActivity::class.java)
            intent.putExtra(GroupDetailsActivity.DOCUMENT_NAME,key)
            (context as GroupsActivity).startActivity(intent)
        }
    }

    private fun removeGroupFromList(index:Int){
        groupList.removeAt(index)
        groupKeys.removeAt(index)
        notifyItemRemoved(index)
    }

    override fun getItemCount(): Int {
       return groupList.size
    }

    fun addGroup(group:Group,key:String){
        groupList.add(group)
        groupKeys.add(key)
        masterKeysList.add(key)
        masterGroupList.add(group)
        notifyItemInserted(groupList.lastIndex)
    }

    fun filter(groupIds:MutableList<String>){
        var keyduplicate = mutableListOf<String>()
        keyduplicate.addAll(groupKeys)
        val iterator = keyduplicate.iterator()
        while(iterator.hasNext()){
            var key = iterator.next()
            if(!(key in groupIds)){
                removePostByKey(key,true)
            }
        }
    }

    fun removePostByKey(id: String,isFilter : Boolean  = false) {
        val index = groupKeys.indexOf(id)

        if(index != -1){
            groupKeys.removeAt(index)
            groupList.removeAt(index)

            notifyItemRemoved(index)
        }
        if(!isFilter){
            val masterIndex = masterKeysList.indexOf(id)
            if(masterIndex != -1){
                masterGroupList.removeAt(masterIndex)
                masterKeysList.removeAt(masterIndex)
            }
        }


    }

    fun findGroupInMaster(groupId: String):Group? {
        var index = masterKeysList.indexOf(groupId)
        if(index != -1){
            return masterGroupList[index]
        }
        return null
    }


    inner class ViewHolder(binding: GroupRowBinding) : RecyclerView.ViewHolder(binding.root){
        var tvGroupName = binding.tvGroupName
        var btnDelete = binding.btnDelete
        var btnLeave = binding.btnLeave
        var cvCardView = binding.cvCardView
    }
}