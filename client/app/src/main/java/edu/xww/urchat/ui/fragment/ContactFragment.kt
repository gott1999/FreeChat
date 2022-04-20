package edu.xww.urchat.ui.fragment

import android.content.Context
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import edu.xww.urchat.R
import edu.xww.urchat.adapter.recyclerview.ContractAdapter

class ContactFragment(private val m_Context: Context) : BaseFragment(R.layout.fragment_contact) {

    private lateinit var recyclerView: RecyclerView

    override fun onInit() {
        requireView().findViewById<TextView>(R.id.common_head_title).text = m_Context.getText(R.string.fragment_contact)
        setParams()
    }

    private fun setParams() {
        recyclerView = requireView().findViewById(R.id.fragment_contact_body_recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.adapter = ContractAdapter(m_Context)
    }

}