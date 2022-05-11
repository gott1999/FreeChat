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
        // 获取组件
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

        // 获取 adapter
        page.adapter = MainViewPager2Adapter(fragments, this)

        // 水平排布
        page.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        // offscreenPageLimit允许加载3个fragment。防止fragment被频繁回收
        page.offscreenPageLimit = 3

        // 关闭用户输入(滑动)
        page.isUserInputEnabled = false

        // 设置viewPage2滑动监听 虽然已经禁用
        page.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // 设置导航栏
                bottomNavigationView.menu.getItem(position).isChecked = true
            }
        })

        // 底部导航栏按钮监听
        bottomNavigationView.setOnItemSelectedListener {
            // 设置view
            when (it.itemId) {
                R.id.bottom_nav_message -> page.setCurrentItem(0, false)
                R.id.bottom_nav_contact -> page.setCurrentItem(1, false)
                R.id.bottom_nav_me -> page.setCurrentItem(2, false)
            }
            true
        }
    }

}