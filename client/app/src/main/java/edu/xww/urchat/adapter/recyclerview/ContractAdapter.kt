package edu.xww.urchat.adapter.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import edu.xww.urchat.R
import edu.xww.urchat.data.struct.Contract
import edu.xww.urchat.helper.ImgHelper
import java.lang.IllegalArgumentException

class ContractAdapter(private val contracts: List<Contract>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    open inner class ContractViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.fragment_message_items_icon)
        val displayName: TextView = view.findViewById(R.id.fragment_message_items_display_name)
        val layout: ConstraintLayout = itemView.findViewById(R.id.fragment_message_items_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_contact_items, parent, false)
        return ContractViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ContractViewHolder) {
            setItems(holder, position)
        } else {
            throw IllegalArgumentException("Unknown 'ViewHolder' during creating 'BindHolder'!")
        }
    }

    override fun getItemCount(): Int = contracts.size


    private fun setItems(holder: ContractViewHolder, position: Int) {
        val data = contracts[position]
        // icon
        ImgHelper.setImageBitmap(holder.icon, data.m_Icon)
        // display name
        holder.displayName.text = data.m_DisplaceName
        // onclick
        holder.layout.setOnClickListener {
            // TODO setOnClickListener
        }
    }

}