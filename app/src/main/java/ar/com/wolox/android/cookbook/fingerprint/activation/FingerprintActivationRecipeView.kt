package ar.com.wolox.android.cookbook.fingerprint.activation

interface FingerprintActivationRecipeView {
    fun showEmptyFieldsError()
    fun showActivateFingerprintDialog()
    fun goToSuccessScreen()
}