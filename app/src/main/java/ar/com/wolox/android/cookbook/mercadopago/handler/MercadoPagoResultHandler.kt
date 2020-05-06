package ar.com.wolox.android.cookbook.mercadopago.handler

import android.app.Activity
import android.content.Intent
import com.mercadopago.android.px.core.MercadoPagoCheckout
import com.mercadopago.android.px.model.Payment
import com.mercadopago.android.px.model.exceptions.MercadoPagoError
import javax.inject.Inject

class MercadoPagoResultHandler @Inject constructor() {

    fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        callback: MercadoPagoResultListener
    ) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == MercadoPagoCheckout.PAYMENT_RESULT_CODE) {
                val payment = data!!.getSerializableExtra(MercadoPagoCheckout.EXTRA_PAYMENT_RESULT) as Payment
                callback.onSuccess(payment)
            } else if (resultCode == Activity.RESULT_CANCELED) {
                if (data != null && data.extras != null && data.extras!!.containsKey(MercadoPagoCheckout.EXTRA_ERROR)) {
                    val mercadoPagoError = data.getSerializableExtra(MercadoPagoCheckout.EXTRA_ERROR) as MercadoPagoError
                    callback.onMercadoPagoError(mercadoPagoError)
                } else {
                    callback.onCanceled()
                }
            } else {
                callback.onError(resultCode.toString())
            }
        }
    }

    companion object {
        private const val REQUEST_CODE = 1
    }
}