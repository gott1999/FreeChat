package edu.xww.urchat.ui.fragment

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import edu.xww.urchat.R

class ContactFragment(private val m_Context: Context) : BaseFragment(R.layout.fragment_contact) {

    private lateinit var recyclerView: RecyclerView

    override fun onInit() {
        setParams()
    }

    private fun setParams() {
        recyclerView = requireView().findViewById(R.id.fragment_contact_body_recycler_view)
    }

}