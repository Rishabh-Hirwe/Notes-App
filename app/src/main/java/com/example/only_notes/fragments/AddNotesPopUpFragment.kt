package com.example.only_notes.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.only_notes.databinding.FragmentAddNotesPopUpBinding
import com.example.only_notes.utils.NotesData
import com.google.android.material.textfield.TextInputEditText

class AddNotesPopUpFragment : DialogFragment() {    //yeh dialogue fragmnet se inherit ho rha hai isme yeh taakat hai ki yeh popUp ho sakta

    private lateinit var binding:  FragmentAddNotesPopUpBinding
    private lateinit var listener : DialogueNextBtnClickListener
    private var datanoteska : NotesData? = null

    fun setListener(listener : DialogueNextBtnClickListener){      //yeh ek setter apne listener k liye
        this.listener = listener
    }

    companion object{
        const val TAG = "AddNotesPopUpFragment"

        @JvmStatic
        fun newInstance(notesId : String , notes :String) = AddNotesPopUpFragment().apply{
            arguments = Bundle().apply {
                putString("notesId",notesId)
                putString("notes",notes)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddNotesPopUpBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(arguments != null){
            datanoteska = NotesData(
                arguments?.getString("notesId").toString() ,
                arguments?.getString("notes").toString()
            )

            binding.popUpEt.setText(datanoteska?.notesData)

        }
        registerEvents()
    }

    private fun registerEvents() {
        binding.popUpNextBtn.setOnClickListener {

            val notesTask = binding.popUpEt.text.toString()

            if(notesTask.isNotEmpty()){
                if(datanoteska == null){
                    listener.onsaveTask(notesTask, binding.popUpEt)
                }                                                                   //ab sub home fragment k under aa jaaega,
            else{
                   datanoteska?.notesData =  notesTask
                    listener.onUpdateTask(datanoteska!!, binding.popUpEt)

                }
            }                                                           //humne directly isme hi nhi kyuki directly data fetch karna in the popUp Dialog is not good
            else{
                Toast.makeText(context,"Enter a Notes first",Toast.LENGTH_SHORT).show()
            }
        }

        binding.popUpClose.setOnClickListener {
            dismiss()
        }
    }

    interface DialogueNextBtnClickListener{
        fun onsaveTask(notes : String, notesTask : TextInputEditText)
        fun onUpdateTask(notesData: NotesData,notesTask : TextInputEditText)
    }

}