package com.example.todoapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.todoapp.DetailFragment
import com.example.todoapp.R
import com.example.todoapp.model.ToDoModel


class ToDoRecyclerviewAdapter(private val context: Context, private val todoList:MutableList<ToDoModel>) : RecyclerView.Adapter<ToDoRecyclerviewAdapter.MyViewHolder>() {

    private var listener: ToDoAdapterClicksInterface?= null

    fun setListener(listener:ToDoAdapterClicksInterface){
        this.listener = listener
    }
    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var recImage: ImageView
        var recTitle: TextView
        var recPriority:TextView
        var recCard: CardView
        var deleteTask: ImageView

        init {
            recImage = itemView.findViewById(R.id.recImage)
            recTitle = itemView.findViewById(R.id.recTitle)
            recPriority= itemView.findViewById(R.id.recPriority)
            recCard = itemView.findViewById(R.id.recCard)
            deleteTask = itemView.findViewById(R.id.deleteTask)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(todoList[position].dataImage)
            .into(holder.recImage)
        holder.recTitle.text = todoList[position].dataTitle
        holder.recPriority.text = todoList[position].dataPriority
        holder.recCard.setOnClickListener {
            val intent = Intent(context, DetailFragment::class.java)
            intent.putExtra("Image",todoList[holder.adapterPosition].dataImage)
            intent.putExtra("Title",todoList[holder.adapterPosition].dataTitle)
            intent.putExtra("Priority",todoList[holder.adapterPosition].dataPriority)
            context.startActivity(intent)
        }
        with(todoList[position]){
                holder.deleteTask.setOnClickListener {
                    listener?.onDeleteTaskBtnClicked(this,position)
                }
            }


    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    interface ToDoAdapterClicksInterface{
        fun onDeleteTaskBtnClicked(toDoModel: ToDoModel,position:Int)
    }


}





