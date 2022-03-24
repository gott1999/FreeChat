package edu.xww.urchat.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.xww.urchat.R
import edu.xww.urchat.fragment.MessageFragment

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var page: ViewPager2
    private val fragments = SparseArray<Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    private fun init() {
        bottomNavigationView = findViewById(R.id.main_navigation)
        page = findViewById(R.id.main_view_pager2)

        // add fragments
        fragments.put(0, MessageFragment())
        fragments.put(1, MessageFragment())
        fragments.put(2, MessageFragment())

        // 获取 adapter
        page.adapter = edu.xww.urchat.adapter.MainViewPager2Adapter(fragments, this)

        // 横向滑动允许，offscreenPageLimit允许加载4个fragment。防止fragment被频繁回收
        page.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        page.offscreenPageLimit = 3
        page.isUserInputEnabled = true

        // 设置viewPage2滑动监听
        // 改变底部导航栏选中状态
        page.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                bottomNavigationView.menu.getItem(position).isChecked = true
            }
        })

        // 底部导航栏按钮监听
        // 设置当前fragment
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