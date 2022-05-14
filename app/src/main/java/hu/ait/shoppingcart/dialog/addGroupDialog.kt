package hu.ait.shoppingcart.dialog



import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.fragment.app.DialogFragment
import hu.ait.shoppingcart.R
import hu.ait.shoppingcart.data.Group
import hu.ait.shoppingcart.databinding.AddOrJoinGroupBinding
import java.time.temporal.Temporal
import java.util.*

class addGroupDialog: DialogFragment() {
    interface CartHandler{
        fun groupCreated(groupName:String)
        fun groupJoined(groupId:String)
    }

    lateinit var cartHandler: CartHandler


    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is CartHandler){
            cartHandler = context
        } else {
            throw RuntimeException(
                getString(R.string.ACTIVITY_NOT_CART_HANDLER))
        }
    }



    lateinit var binding: AddOrJoinGroupBinding
    private var isJoin = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(requireContext())

        dialogBuilder.setTitle(getString(R.string.ADD_OR_JOIN_GROUP))


        binding = AddOrJoinGroupBinding.inflate(requireActivity().layoutInflater)
        binding.cbJoin.setOnClickListener {
            if(binding.cbJoin.isChecked){
                isJoin = true
            }
            else{
                isJoin = false
            }

        }

        dialogBuilder.setView(binding.root)

        dialogBuilder.setPositiveButton(getString(R.string.ADD_ITEM)) {
                dialog, which ->

        }
        dialogBuilder.setNegativeButton(getString(R.string.CANCEL)) {
                dialog, which ->
        }

        return dialogBuilder.create()
    }

    override fun onResume() {
        super.onResume()
        val dialog = dialog as AlertDialog
        val positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE)

        positiveButton.setOnClickListener {
            if(binding.etGroupNameId.text.isEmpty()){
                binding.etGroupNameId.error = getString(R.string.EMPTY_FIELD_ERROR)
            }
            else{
                if(isJoin){
                    handleJoin()
                }
                else{
                    //Use command option m to extract code into a method
                    handleCreate()
                }
                //handleCreate()
                dialog.dismiss()
            }

        }
    }





    private fun handleCreate() {
        cartHandler.groupCreated(binding.etGroupNameId.text.toString()
        )
    }

    private fun handleJoin() {
        cartHandler.groupJoined(binding.etGroupNameId.text.toString())
    }



}