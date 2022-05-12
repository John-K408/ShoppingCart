package hu.ait.shoppingcart.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.ait.shoppingcart.data.Group
import hu.ait.shoppingcart.databinding.GroupRowBinding

class groupsAdapter: RecyclerView.Adapter<groupsAdapter.ViewHolder> {

    var context: Context
    var  groupList = mutableListOf<Group>()
    var  groupKeys = mutableListOf<String>()

    lateinit var currentUid: String

    constructor(context: Context, uid: String) : super() {
        this.context = context
        this.currentUid = uid
    }




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
    }

    private fun removeGroupFromList(index:Int){
        groupList.removeAt(index)
        groupKeys.removeAt(index)
        notifyItemRemoved(index)
    }

    override fun getItemCount(): Int {
       return groupList.size
    }



    inner class ViewHolder(binding: GroupRowBinding) : RecyclerView.ViewHolder(binding.root){
        var tvGroupName = binding.tvGroupName
        var btnDelete = binding.btnDelete
        var btnLeave = binding.btnLeave
    }
}