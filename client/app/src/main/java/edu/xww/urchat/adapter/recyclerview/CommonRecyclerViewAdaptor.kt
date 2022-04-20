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
import edu.xww.urchat.data.struct.system.CommonRecyclerViewItem
import edu.xww.urchat.helper.ImgHelper

abstract class CommonRecyclerViewAdaptor(
    private val m_Context: Context,
    private val list: MutableList<CommonRecyclerViewItem>
) :
    RecyclerView.Adapter<CommonRecyclerViewAdaptor.CommonRecyclerViewAdaptorHolder>() {

    inner class CommonRecyclerViewAdaptorHolder(view: View) : RecyclerView.ViewHolder(view) {
        val gap: TextView = view.findViewById(R.id.common_list_items_gap)
        val icon: ImageView = view.findViewById(R.id.common_list_items_icon)
        val displayName: TextView = view.findViewById(R.id.common_list_items_display_name)
        val layout: ConstraintLayout = itemView.findViewById(R.id.common_list_items_layout)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommonRecyclerViewAdaptorHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.common_list_item, parent, false)
        return CommonRecyclerViewAdaptorHolder(view)
    }

    override fun onBindViewHolder(holder: CommonRecyclerViewAdaptorHolder, position: Int) {
        val curr = list[position]

        // set gap
        if (position == 0 || list[position - 1].tag != list[position].tag) {
            holder.gap.visibility = View.VISIBLE
            holder.gap.text = list[position].tag
        } else {
            holder.gap.visibility = View.GONE
        }

        // set icon
        ImgHelper.setImageBitmap(holder.icon, curr.icon)

        // set display name
        holder.displayName.text = curr.displayName

        holder.layout.setOnClickListener { onClick(curr) }
        holder.layout.setOnLongClickListener { onLongClick(curr) }
    }

    override fun getItemCount(): Int = list.size


    abstract fun onClick(item: CommonRecyclerViewItem)

    abstract fun onLongClick(item: CommonRecyclerViewItem): Boolean
}