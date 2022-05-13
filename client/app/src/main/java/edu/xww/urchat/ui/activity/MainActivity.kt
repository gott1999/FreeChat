package edu.xww.urchat.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.xww.urchat.R
import edu.xww.urchat.ui.adapter.viewpager.MainViewPager2Adapter
import edu.xww.urchat.ui.fragment.ContactFragment
import edu.xww.urchat.ui.fragment.MessageFragment
import edu.xww.urchat.ui.fragment.MineFragment

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var page: ViewPager2
    private val fragments = SparseArray<Fragment>()

    var messageFragment: MessageFragment? = null
        private set
    var contactFragment: ContactFragment? = null
        private set
    var mineFragment: MineFragment? = null
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        bottomNavigationView = findViewById(R.id.main_navigation)
        page = findViewById(R.id.main_view_pager2)

        if (messageFragment == null) {
            messageFragment = MessageFragment(this)
        }
        if (contactFragment == null) {
            contactFragment = ContactFragment(this)
        }
        if (mineFragment == null) {
            mineFragment = MineFragment(this)
        }

        // add fragments
        fragments.put(0, messageFragment)
        fragments.put(1, contactFragment)
        fragments.put(2, mineFragment)

        page.adapter = MainViewPager2Adapter(fragments, this)

        page.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        page.offscreenPageLimit = 3

        page.isUserInputEnabled = true

        page.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                bottomNavigationView.menu.getItem(position).isChecked = true
            }
        })

        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_nav_message -> page.setCurrentItem(0, false)
                R.id.bottom_nav_contact -> page.setCurrentItem(1, false)
                R.id.bottom_nav_me -> page.setCurrentItem(2, false)
            }
            true
        }
    }


}