package edu.xww.urchat.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class BaseFragment(private val resource: Int) : Fragment() {

    private var isInit = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(resource, container, false)
    }

    override fun onResume() {
        super.onResume()
        if (!isInit) {
            onInit()
            isInit = true
        }
    }

    abstract fun onInit()

    abstract fun wake()

}