package ar.com.wolox.android.cookbook.instagramlogin

import android.util.Log
import ar.com.wolox.android.cookbook.R
import ar.com.wolox.android.cookbook.instagramlogin.adapter.InstagramLoginAuthListener
import ar.com.wolox.android.cookbook.instagramlogin.adapter.InstagramLoginView
import ar.com.wolox.wolmo.core.fragment.WolmoFragment
import kotlinx.android.synthetic.main.fragment_instagram_login.*

class InstagramLoginRecipeFragment : WolmoFragment<InstagramLoginRecipePresenter>(), InstagramLoginRecipeView {

    override fun layout(): Int = R.layout.fragment_instagram_login

    override fun init() {
    }

    override fun setListeners() {
        super.setListeners()

        vLogin.setOnClickListener {
            presenter.onIgLoginRequest()
        }
    }

    override fun showWebView(url: String) {
        if (context != null) {
            InstagramLoginView().showDialog(context!!, url, object : InstagramLoginAuthListener {
                override fun onCodeReceived(authToken: String) {
                    Log.e("IG", "onCodeReceived: $authToken")
                }

                override fun onCodeError() {
                    Log.e("IG", "onCodeError")
                }

                override fun onApiError() {
                    Log.e("IG", "onApiError")
                }
            })
        }
    }
}
