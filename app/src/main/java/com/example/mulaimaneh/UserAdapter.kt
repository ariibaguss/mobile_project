package com.example.mulaimaneh

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.mulaimaneh.databinding.ItemListUserBinding
import com.example.mulaimaneh.model.ResponseUser

class UserAdapter(
    private val data: MutableList<ResponseUser.Item> = mutableListOf(),
    private val listener: (ResponseUser.Item) -> Unit
) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    fun setData(data: MutableList<ResponseUser.Item>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    class UserViewHolder(private val v: ItemListUserBinding) : RecyclerView.ViewHolder(v.root) {
        fun bind(item: ResponseUser.Item) {
            v.itemAvatar.load(item.avatar_url) {
                transformations(CircleCropTransformation())
            }

            v.itemName.text = item.login
            v.itemEmail.text = item.html_url
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder =
        UserViewHolder(ItemListUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            listener(item)
        }
    }

    override fun getItemCount(): Int = data.size
}