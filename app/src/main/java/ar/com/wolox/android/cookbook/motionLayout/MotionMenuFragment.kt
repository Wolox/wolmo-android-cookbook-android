package ar.com.wolox.android.cookbook.motionLayout

import android.support.v4.app.Fragment
import ar.com.wolox.android.cookbook.R
import ar.com.wolox.android.cookbook.motionLayout.viewPager.ViewPagerFragment
import ar.com.wolox.wolmo.core.fragment.WolmoFragment
import kotlinx.android.synthetic.main.fragment_motion_layout_menu.*

class MotionMenuFragment : WolmoFragment<MotionPresenter>(), IMotionView {
    private lateinit var mViewPagerFragment: ViewPagerFragment

    override fun layout(): Int = R.layout.fragment_motion_layout_menu

    override fun init() {
        mViewPagerFragment = ViewPagerFragment()
        setUpListeners()
    }

    fun setUpListeners() {
        bViewPagerMotion.setOnClickListener {
            replaceFragment(layout(), mViewPagerFragment)
        }
    }

    private fun replaceFragment(resId: Int, fragment: Fragment) {
        var fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.replace(resId, fragment)
        fragmentTransaction?.commit()
    }
}