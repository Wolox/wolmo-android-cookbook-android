package ar.com.wolox.android.cookbook.facebooklogin.helper

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Base64
import android.util.Log
import android.view.View
import ar.com.wolox.android.cookbook.facebooklogin.model.FacebookAccount
import ar.com.wolox.wolmo.core.di.scopes.ApplicationScope
import com.facebook.AccessToken
import com.facebook.AccessTokenTracker
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.Arrays
import javax.inject.Inject

@ApplicationScope
class FacebookHelper @Inject constructor(context: Context) {

    private val applicationContext: Context = context.applicationContext

    private var _callbackManager: CallbackManager? = null
    private val callbackManager: CallbackManager
        get() {
            if (_callbackManager == null) {
                _callbackManager = CallbackManager.Factory.create()
            }
            return _callbackManager ?: throw AssertionError("Set to null by another thread")
        }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) =
            callbackManager.onActivityResult(requestCode, resultCode, data)

    /**
     * This method is for the button that Facebook API provides.
     * This button already has the login and logout actions.
     */
    fun setFacebookOriginalButtonAction(
            loginButton: LoginButton,
            fragment: Fragment,
            loginListener: LoginListener,
            logoutListener: LogoutListener
    ) {

        loginButton.setReadPermissions(Arrays.asList("public_profile,email"))
        loginButton.fragment = fragment
        loginButton.registerCallback(callbackManager, getFacebookCallback(loginListener))

        setLogoutListener(logoutListener)
    }

    /**
     * This method is to give the login action to a personal button or any clickable view.
     */
    fun setFacebookLoginAction(view: View, fragment: Fragment, listener: LoginListener) {
        view.setOnClickListener {
            if (isLoggedIn()) {
                onTokenSuccess(AccessToken.getCurrentAccessToken(), listener)
            } else {
                LoginManager.getInstance().logInWithReadPermissions(fragment, Arrays.asList("public_profile,email"))
            }
        }

        LoginManager.getInstance().registerCallback(callbackManager, getFacebookCallback(listener))
    }

    /**
     * This method is to give the logout action to a personal button or any clickable view.
     */
    fun setFacebookLogoutAction(view: View, listener: LogoutListener) {
        setLogoutListener(listener)

        view.setOnClickListener {
            LoginManager.getInstance().logOut()
        }
    }

    private fun setLogoutListener(listener: LogoutListener) {
        object : AccessTokenTracker() {
            override fun onCurrentAccessTokenChanged(accessToken: AccessToken?, accessToken2: AccessToken?) {
                if (accessToken2 == null) {
                    listener.onLogout()
                }
            }
        }
    }

    private fun isLoggedIn(): Boolean {
        val accessToken = AccessToken.getCurrentAccessToken()
        return accessToken != null
    }

    private fun onTokenSuccess(accessToken: AccessToken, listener: LoginListener) {
        val request = GraphRequest.newMeRequest(accessToken) { obj, _ ->
            listener.onLoginSuccess(FacebookAccount(obj))
        }
        val parameters = Bundle()
        parameters.putString("fields", "id,name,email,picture.width(200)")
        request.parameters = parameters
        request.executeAsync()
    }

    private fun getFacebookCallback(listener: LoginListener) =
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) = onTokenSuccess(result.accessToken, listener)

                override fun onCancel() = listener.onLoginCancel()

                override fun onError(exception: FacebookException) = listener.onLoginError(exception)
            }

    /**
     * If you have troubles with the key hash, use this method to get the key hash that will work.
     */
    @SuppressLint("PackageManagerGetSignatures")
    fun logKeyHash(packageManager: PackageManager) {
        try {
            val info = packageManager.getPackageInfo(PACKAGE_NAME, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val sign = Base64.encodeToString(md.digest(), Base64.DEFAULT)
                Log.i(TAG, "My key hash is: $sign")
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, "Name not found exception")
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            Log.e(TAG, "No such algorithm exception")
            e.printStackTrace()
        }
    }

    interface LoginListener {

        fun onLoginSuccess(account: FacebookAccount)

        fun onLoginCancel()

        fun onLoginError(exception: FacebookException)
    }

    interface LogoutListener {
        fun onLogout()
    }

    companion object {

        private const val PACKAGE_NAME = "ar.com.wolox.android.cookbook"
        private const val TAG = "FacebookHelper"
    }
}