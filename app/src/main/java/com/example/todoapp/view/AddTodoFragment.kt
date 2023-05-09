package com.example.todoapp.view

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentAddTodoBinding
import com.example.todoapp.model.ToDoModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.text.DateFormat
import java.util.*

class AddTodoFragment : DialogFragment() {


    private lateinit var binding: FragmentAddTodoBinding
    private lateinit var listener:DialogNextBtnClickListeners
    private lateinit var auth: FirebaseAuth
    private lateinit var authId: String
    private lateinit var database: DatabaseReference

    private var toDoModel : ToDoModel? = null
    var imageUrl: String?=null
    var uri: Uri? = null

    fun setListener(listener:DialogNextBtnClickListeners){
        this.listener = listener
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTodoBinding.inflate(inflater,container,false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activityResultLauncher = registerForActivityResult<Intent,ActivityResult>(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK){
                val data = result.data
                uri = data!!.data
                binding.todoIw.setImageURI(uri)
            }else{
                Toast.makeText(context, "No Image Selected", Toast.LENGTH_SHORT).show()
            }



        }

        binding.todoIw.setOnClickListener {
            val photoPicker = Intent(Intent.ACTION_PICK)
            photoPicker.type = "image/*"
            activityResultLauncher.launch(photoPicker)
        }
        binding.todoNextBtn.setOnClickListener {
            saveData()
        }

        binding.todoClose.setOnClickListener {
            dismiss()
        }


       /*

        if(arguments != null){
            toDoModel = ToDoModel(
                arguments?.getString("taskId").toString(),
                arguments?.getString("task").toString(),
                arguments?.getString("taskDate").toString(),
                arguments?.getString("image").toString(),
            )
            binding.todoEt.setText(toDoModel?.task)
            binding.todoDt.setText(toDoModel?.taskPriority)

        }
        registerEvents()
    }

    private fun registerEvents() {
        binding.todoNextBtn.setOnClickListener {
            val todoTask = binding.todoEt.text.toString()
            val todoDate = binding.todoDt.text.toString()

            if (todoTask.isNotEmpty() && todoDate.isNotEmpty()){
                if(toDoModel == null){
                    listener.onSaveTask(todoTask,binding.todoEt,binding.todoDt)
                }else{
                    toDoModel?.task = todoTask
                    listener.onUpdateTask(toDoModel!!,binding.todoEt,binding.todoDt)

                }
            }else{
                Toast.makeText(context,"Please type some task",Toast.LENGTH_SHORT).show()
            }

        }



        */
    }
    private fun init(){
        auth = FirebaseAuth.getInstance()
        authId = auth.currentUser!!.uid
        database = Firebase.database.reference.child("TodoList").child(authId)

    }

    private fun saveData(){

        val storageReference = FirebaseStorage.getInstance().reference.child("Task Images")
            .child(uri!!.lastPathSegment!!)

        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        builder.setView(R.layout.progress_layout)
        val dialog = builder.create()
        dialog.show()
        storageReference.putFile(uri!!).addOnSuccessListener { taskSnaphot->
            val uriTask = taskSnaphot.storage.downloadUrl
            while (!uriTask.isComplete);
            val urlImage = uriTask.result
            imageUrl = urlImage.toString()
            init()
            registerEvents()
            dialog.dismiss()

         }.addOnFailureListener{
             dialog.dismiss()
        }





    }

    /*
    private fun uploadData(){
        val title = binding.todoEt.text.toString()
        val priority = binding.todoPt.text.toString()

        val dataClass= ToDoModel(,title, priority,imageUrl)
        val currentDate= DateFormat.getDateTimeInstance().format(Calendar.getInstance().time)

        FirebaseDatabase.getInstance().getReference("Todo List").child(currentDate)
            .setValue(dataClass).addOnCompleteListener { task->
                if (task.isSuccessful){
                    Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()


                }
            }.addOnFailureListener { e->
                Toast.makeText(context,e.message.toString(),Toast.LENGTH_SHORT).show()

            }


    }

     */

    private fun registerEvents(){
        val title = binding.todoEt.text.toString()
        val priority = binding.todoPt.text.toString()
        val dataClass = ToDoModel(title,priority,imageUrl)
        val currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().time)

        binding.todoNextBtn.setOnClickListener {
            if(title.isNotEmpty() && priority.isNotEmpty()){
                listener.onSaveTask(title,priority,binding.todoEt,binding.todoPt)

            }else{
                Toast.makeText(context,"Please type some task" , Toast.LENGTH_SHORT).show()
            }
        }

        binding.todoClose.setOnClickListener {
            dismiss()
        }
        database
            .push().setValue(dataClass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
                    binding.todoEt.text = null
                    binding.todoPt.text = null
                }
            }.addOnFailureListener { e ->
                Toast.makeText(
                    context, e.message.toString(), Toast.LENGTH_SHORT).show()
            }


    }

    interface DialogNextBtnClickListeners{
        fun onSaveTask(todo:String,todoP:String, todoEt: TextInputEditText, todoPt: TextInputEditText)
    }

}