package edu.xww.urchat.ui.fragment

import androidx.recyclerview.widget.RecyclerView
import edu.xww.urchat.R

class ContactFragment() : BaseFragment(R.layout.fragment_contact) {

    private lateinit var recyclerView: RecyclerView

    override fun onInit() {
        setParams()
    }

    private fun setParams() {
        recyclerView = requireView().findViewById(R.id.fragment_contact_body_recycler_view)
    }

}