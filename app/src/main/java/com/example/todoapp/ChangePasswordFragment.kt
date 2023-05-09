package com.example.todoapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.todoapp.databinding.FragmentChangePasswordBinding
import com.example.todoapp.model.ToDoModel
import com.example.todoapp.view.AddTodoFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth


class ChangePasswordFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentChangePasswordBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChangePasswordBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerEvents()

    }

    private fun registerEvents() {

        auth = FirebaseAuth.getInstance()
        binding.todoNextPasswordBtn.setOnClickListener {
            val sPassword = binding.todoChangeEt.text.toString()
            auth.sendPasswordResetEmail(sPassword)
                .addOnSuccessListener {
                    Toast.makeText(context,"Please check your email", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context,it.toString(), Toast.LENGTH_SHORT).show()

                }
        }

        binding.todoClose.setOnClickListener {
            dismiss()
        }


    }



}