package com.getmontir.mitra.view.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.getmontir.mitra.R
import com.getmontir.mitra.utils.data.AuthChooserItem
import com.getmontir.mitra.utils.enums.Role
import com.google.android.material.card.MaterialCardView

class RoleChooserAdapter(
    private val context: Context,
    private val list: List<AuthChooserItem>,
    private val callback: Callback
): RecyclerView.Adapter<RoleChooserAdapter.ViewHolder>() {

    companion object {
        private var vhList: ArrayList<ViewHolder> = arrayListOf()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_role_chooser, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        vhList.add( position, holder )
        holder.imgIcon.setImageResource( list[position].icon )
        holder.tvRoleName.text = list[position].name
        holder.tvRoleDescription.text = list[position].description
        holder.card.setOnClickListener {
            vhList.forEach { item ->
                item.card.isChecked = false
            }
            (it as MaterialCardView).apply {
                isChecked = !isChecked
            }
        }
        holder.card.setOnCheckedChangeListener { card, isChecked ->
            if( isChecked ) {
                callback.onItemSelected(list[position])
                card.strokeColor = context.resources.getColor(R.color.secondaryColor)
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    card.elevation = 12F
                }
            } else {
                card.strokeColor = Color.argb(100, 216, 216, 216)
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    card.elevation = 0F
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val card: MaterialCardView = view.findViewById(R.id.card)
        val imgIcon: ImageView = view.findViewById(R.id.icon)
        val tvRoleName: TextView = view.findViewById(R.id.tvRoleName)
        val tvRoleDescription: TextView = view.findViewById(R.id.tvRoleDescription)
    }

    interface Callback {
        fun onItemSelected(role: AuthChooserItem)
    }
}