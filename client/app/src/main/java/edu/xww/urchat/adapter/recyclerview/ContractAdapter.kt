package edu.xww.urchat.adapter.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import edu.xww.urchat.R
import edu.xww.urchat.data.runtime.RunTimeData
import edu.xww.urchat.helper.ImgHelper
import edu.xww.urchat.ui.activity.ChatActivity

class ContractAdapter(private val m_Context: Context) :
    RecyclerView.Adapter<ContractAdapter.ContractViewHolder>() {

    private val contracts = RunTimeData.runTimeContracts

    open inner class ContractViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val gap: TextView = view.findViewById(R.id.common_list_items_gap)
        val icon: ImageView = view.findViewById(R.id.common_list_items_icon)
        val displayName: TextView = view.findViewById(R.id.common_list_items_display_name)
        val layout: ConstraintLayout = itemView.findViewById(R.id.common_list_items_layout)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContractViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.common_list_item, parent, false)
        return ContractViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContractViewHolder, position: Int) {
        val curr = contracts[position]

        // set gap
        if (position == 0 || contracts[position - 1].tag != contracts[position].tag) {
            holder.gap.visibility = View.VISIBLE
            holder.gap.text = contracts[position].tag
        } else {
            holder.gap.visibility = View.GONE
        }

        // set icon
        ImgHelper.setImageBitmap(holder.icon, curr.icon)

        // set display name
        holder.displayName.text = curr.displayName

        // set onclick
        holder.layout.setOnClickListener {
            ChatActivity.startInstance(m_Context, curr.uId, curr.displayName)
        }

        // set long click
        holder.layout.setOnLongClickListener { onLongClicked() }
    }

    override fun getItemCount(): Int = contracts.size

    private fun onLongClicked(): Boolean {
        return true
    }

}