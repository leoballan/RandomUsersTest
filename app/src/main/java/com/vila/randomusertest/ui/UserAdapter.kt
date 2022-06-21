package com.vila.randomusertest.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vila.randomusertest.databinding.ItemLayoutBinding
import com.vila.randomusertest.domain.models.User

class UserAdapter (val action :(User)->Unit):ListAdapter<User,UserAdapter.Holder>(MyDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context)
            ,parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binds(getItem(position))
    }

    object MyDiffUtil : DiffUtil.ItemCallback<User>(){
        override fun areItemsTheSame(oldItem: User, newItem: User) :Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.name == newItem.name && oldItem.picture == newItem.picture
        }
    }

    inner class Holder(private val binding: ItemLayoutBinding)
        : RecyclerView.ViewHolder(binding.root){
        fun binds(user: User){
            with(binding){
                this.root.setOnClickListener { action(user) }
                this.userName.text = user.userName
                Glide.with(binding.root.context)
                    .load(user.picture.thumbnail)
                    .circleCrop()
                    .into(this.image)
            }
        }
    }
}