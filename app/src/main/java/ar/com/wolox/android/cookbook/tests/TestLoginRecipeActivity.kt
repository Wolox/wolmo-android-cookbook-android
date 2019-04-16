package ar.com.wolox.android.cookbook.tests

import ar.com.wolox.android.cookbook.R
import ar.com.wolox.wolmo.core.activity.WolmoActivity

class TestLoginRecipeActivity : WolmoActivity() {

    val fragment = TestLoginRecipeFragment()

    override fun layout(): Int = R.layout.activity_base

    override fun init() = replaceFragment(R.id.vActivityBaseContent, fragment)
}