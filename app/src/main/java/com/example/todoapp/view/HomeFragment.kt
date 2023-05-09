package com.example.todoapp.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.R
import com.example.todoapp.adapter.ToDoRecyclerviewAdapter
import com.example.todoapp.databinding.FragmentHomeBinding
import com.example.todoapp.model.ToDoModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class HomeFragment : Fragment(), AddTodoFragment.DialogNextBtnClickListeners,
    ToDoRecyclerviewAdapter.ToDoAdapterClicksInterface {


    private lateinit var auth: FirebaseAuth
    private var databaseReference: DatabaseReference? = null
    var eventListener: ValueEventListener? = null
    private lateinit var navController: NavController
    private lateinit var binding: FragmentHomeBinding
    private lateinit var todoList:ArrayList<ToDoModel>
    private lateinit var dataClass:ArrayList<ToDoModel>
    private lateinit var adapter: ToDoRecyclerviewAdapter
    private lateinit var addTodoFragment: AddTodoFragment
    private lateinit var id: String
    private var nightMode : Boolean? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        builder.setView(R.layout.progress_layout)
        val dialog = builder.create()
        init(view)
        databaseReference!!.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                todoList.clear()
                for (itemSnapshot in snapshot.children){
                    val dataClass = itemSnapshot.key?.let {
                        itemSnapshot.getValue(ToDoModel::class.java)
                    }
                    todoList.add(dataClass!!)
                    adapter.notifyDataSetChanged()
                    dialog.dismiss()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,error.message,Toast.LENGTH_SHORT).show()
            }

        })
        registerEvents()


        /*
        getImages()

         */


    }


    private fun registerEvents() {


        binding.logBtnHome.setOnClickListener {
            auth.signOut()
            Toast.makeText(context, "LogOut Successfully", Toast.LENGTH_SHORT).show()
            navController.navigate(R.id.action_homeFragment_to_signInFragment)
        }

        binding.addBtnHome.setOnClickListener {
            addTodoFragment = AddTodoFragment()
            addTodoFragment.setListener(this)
            addTodoFragment.show(
                childFragmentManager,
                "AddTodoFragment"
            )

        }

        



    }
    private fun init(view: View) {
        navController = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference
            .child("TodoList").child(auth.currentUser?.uid.toString())
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        todoList = arrayListOf()
        adapter = ToDoRecyclerviewAdapter(requireContext(),todoList)
        adapter.setListener(this)
        binding.recyclerView.adapter = adapter
    }

    override fun onSaveTask(todo: String, todoP: String, todoEt: TextInputEditText, todoPt: TextInputEditText) {

        databaseReference?.push()?.setValue(todo,todoP)?.addOnCompleteListener {
            todoList.clear()
            if (it.isSuccessful){
                Toast.makeText(context,"Todo Saved successfully",Toast.LENGTH_SHORT).show()
                todoEt.text = null
                todoPt.text = null
            }else{
                Toast.makeText(context,it.exception?.message,Toast.LENGTH_SHORT).show()
            }

            addTodoFragment.dismiss()

        }



    }

    override fun onDeleteTaskBtnClicked(toDoModel: ToDoModel,position:Int) {

        databaseReference?.ref?.removeValue()?.addOnCompleteListener {
            if (it.isSuccessful){
                Toast.makeText(context,"Todo Saved successfully",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context,it.exception?.message,Toast.LENGTH_SHORT).show()
            }
        }

        /*

        databaseReference?.ref?.child("TodoList")?.key.let {
            databaseReference!!.child(auth.currentUser?.uid.toString())?.key?.let {
                databaseReference!!.child(auth.currentUser?.uid.toString()).removeValue().addOnCompleteListener{
                if(it.isSuccessful){
                    Toast.makeText(context,"Deleted successfully",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context,it.exception?.message,Toast.LENGTH_SHORT).show()
                }
            }
        }

         */



            /*
            databaseReference?.ref?.child("TodoList")?.child(auth.currentUser?.uid.toString())?.removeValue()?.addOnCompleteListener{
                    if(it.isSuccessful){
                        Toast.makeText(context,"Deleted successfully",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(context,it.exception?.message,Toast.LENGTH_SHORT).show()
                    }
            }
             */








    }




}
    /*
    private fun alert(context: Context, text: String) {
        binding.logBtnHome.setOnClickListener {
            auth.signOut()
            startActivity(
                Intent(
                    context, SignInFragment::class.java
                )
            )

        }





    }

     */

    /*

    private fun init(view: View) {
        navController = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference
            .child("Tasks").child(auth.currentUser?.uid.toString())
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        list = mutableListOf()
        adapter = ToDoRecyclerviewAdapter(list)
        adapter.setListener(this)
        binding.recyclerView.adapter = adapter

    }


    /*
    private fun getImages() {
        firebaseFirestore.collection("images")
            .get().addOnSuccessListener {
                for(i in it){
                    list.add(i.data[i])
                }
                adapter.notifyDataSetChanged()

            }

    }

     */

    private fun initVars() {
        firebaseFirestore = FirebaseFirestore.getInstance()
    }

    override fun onSaveTask(todo: String, todoEt: TextInputEditText,todoDt: TextInputEditText) {

        databaseReference.push().setValue(todo).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "Todo saved succesfully !!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
            todoEt.text = null
            todoDt.text = null
            addTodoFragment!!.dismiss()
        }

    }

    override fun onUpdateTask(toDoModel: ToDoModel, todoEt: TextInputEditText,todoDt: TextInputEditText) {
        val map = HashMap<String, Any>()
        map[toDoModel.taskId] = toDoModel.task
        databaseReference.updateChildren(map).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
            todoEt.text = null
            todoDt.text = null
            addTodoFragment!!.dismiss()
        }
    }

    private fun getDataFromFirebase() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (taskSnaphot in snapshot.children) {
                    val todoTask = taskSnaphot.key?.let {
                        ToDoModel(it, taskSnaphot.value.toString(), taskSnaphot.value.toString(),it)
                    }

                    if (todoTask != null ) {
                        list.add(todoTask)

                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()

            }


        }


        )
    }

    override fun onDeleteTaskBtnClicked(toDoModel: ToDoModel) {
        databaseReference.child(toDoModel.taskId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()

            }
        }
    }

    override fun onEditTaskBtnClicked(toDoModel: ToDoModel) {
        if (addTodoFragment != null) {
            childFragmentManager.beginTransaction().remove(addTodoFragment!!).commit()
        }

        addTodoFragment = AddTodoFragment.newInstance(toDoModel.taskId, toDoModel.task,toDoModel.taskDate)
        addTodoFragment!!.setListener(this)
        addTodoFragment!!.show(childFragmentManager, AddTodoFragment.TAG)
    }


    override fun onListAddTaskBtnClicked(toDoModel: ToDoModel) {

        databaseReference.child(toDoModel.taskId).setValue(listToDoFragment!!).toString()


    }




        /*
        val childItem = ArrayList<ToDoModel>()

        listToDoFragment = ListToDoFragment().apply {
            if(listToDoFragment != null){
                childFragmentManager.beginTransaction().add(listToDoFragment!!,"Tasks")
                childItem.add(listToDoFragment as ToDoModel)
            }

        }
        }

         */
    }

     */








