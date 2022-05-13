package hu.ait.shoppingcart.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.ait.shoppingcart.GroupDetailsActivity
import hu.ait.shoppingcart.GroupsActivity
import hu.ait.shoppingcart.data.Group
import hu.ait.shoppingcart.databinding.GroupRowBinding

class groupsAdapter(var context: Context, uid: String) :
    RecyclerView.Adapter<groupsAdapter.ViewHolder>() {

    var  groupList = mutableListOf<Group>()
    var  groupKeys = mutableListOf<String>()

     var currentUid: String = uid


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GroupRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var group = groupList[position]
        holder.tvGroupName.text = group.groupName
        if(currentUid != group.hostId){
            holder.btnDelete.visibility = View.GONE
        }
        else{
            holder.btnDelete.setOnClickListener {
                //Delete sub-collection (or document) from firebase
                removeGroupFromList(position)

            }
        }

        holder.btnLeave.setOnClickListener {
            removeGroupFromList(position)
        }
        holder.layoutContainer.setOnClickListener{
            val intent = Intent()
            intent.setClass((context as GroupsActivity),GroupDetailsActivity::class.java)
            intent.putExtra(GroupDetailsActivity.DOCUMENT_NAME,groupKeys[position])
            (context as GroupDetailsActivity).startActivity(intent)
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
        notifyItemInserted(groupList.lastIndex)
    }

    fun removePostByKey(id: String) {
        val index = groupKeys.indexOf(id)
        if(index != -1){
            groupKeys.removeAt(index)
            groupList.removeAt(index)
            notifyItemRemoved(index)
        }
    }


    inner class ViewHolder(binding: GroupRowBinding) : RecyclerView.ViewHolder(binding.root){
        var tvGroupName = binding.tvGroupName
        var btnDelete = binding.btnDelete
        var btnLeave = binding.btnLeave
        var layoutContainer = binding.layoutGroup
    }
}