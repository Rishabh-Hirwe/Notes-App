package com.example.only_notes.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.only_notes.databinding.FragmentHomeBinding
import com.example.only_notes.utils.NoteAdapter
import com.example.only_notes.utils.NotesData
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment(), AddNotesPopUpFragment.DialogueNextBtnClickListener,
    NoteAdapter.NotesAdapterClicksInterface {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    private lateinit var navControl: NavController
    private lateinit var binding: FragmentHomeBinding
    private  var popUpFragment: AddNotesPopUpFragment? = null
    private lateinit var adapter: NoteAdapter
    private lateinit var mList: MutableList<NotesData>    //mutable because isski value change hogi throughout the process

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)                // iss fun se hum initialize karenge navController, auth aur databaseRef ko
        registerEvents()
        getDataFromFirebase()
    }

    private fun init(view: View) {

        navControl = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference      //hum apne realtime DB ko refer kar sakte hai
            .child("Notes").child(auth.currentUser?.uid.toString())    //reference k under tasks honge as a child and uske under apne user ki id
                                                                                    //jisse apan users k bich distinguish kar sake
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        mList = mutableListOf()
        adapter = NoteAdapter(mList)
        adapter.setListener(this)
        binding.recyclerView.adapter = adapter
    }

    private fun registerEvents(){
        binding.addBtnHome.setOnClickListener {

            if (popUpFragment != null){
                childFragmentManager.beginTransaction().remove(popUpFragment!!).commit()
            }

            popUpFragment = AddNotesPopUpFragment()
            popUpFragment!!.setListener(this)
            popUpFragment!!.show(
                childFragmentManager,
                AddNotesPopUpFragment.TAG
            )
        }
    }

    private fun getDataFromFirebase(){
        databaseRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                mList.clear()
                for(notesSnapshot in snapshot.children){
                    val notesTask = notesSnapshot.key?.let{
                        NotesData(it, notesSnapshot.value.toString())
                    }

                    if(notesTask != null){
                        mList.add(notesTask)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onsaveTask(notes: String, notesTask: TextInputEditText) {

        databaseRef.push().setValue(notes).addOnCompleteListener {
            if(it.isSuccessful){
                Toast.makeText(context, "Notes Added Successfully !!", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(context, it.exception?.message , Toast.LENGTH_SHORT).show()
            }
            notesTask.text = null   //agli baar taki humme naya Edit text Mile fresh
            popUpFragment!!.dismiss()
        }
    }

    override fun onUpdateTask(notesData: NotesData, notesTask: TextInputEditText) {
         val map = HashMap<String , Any>()
        map[notesData.notesID] = notesData.notesData
        databaseRef.updateChildren(map).addOnCompleteListener {
            if(it.isSuccessful){
                Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context,it.exception.toString(),Toast.LENGTH_SHORT ).show()
            }
            notesTask.text = null   //agli baar taki humme naya Edit text Mile fresh
            popUpFragment!!.dismiss()
        }
    }

    override fun onDeleteTaskBtnClick(notesData: NotesData) {

        databaseRef.child(notesData.notesID ).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onEditTaskBtnClick(notesData: NotesData) {
         if(popUpFragment != null)
             childFragmentManager.beginTransaction().remove(popUpFragment!!).commit()

        popUpFragment = AddNotesPopUpFragment.newInstance(notesData.notesID , notesData.notesData)
        popUpFragment!!.setListener(this)
        popUpFragment!!.show(childFragmentManager , AddNotesPopUpFragment.TAG)

    }

}