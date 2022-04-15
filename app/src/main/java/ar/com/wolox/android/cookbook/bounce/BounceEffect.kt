package ar.com.wolox.android.cookbook.bounce

import android.graphics.Canvas
import android.widget.EdgeEffect
import androidx.dynamicanimation.animation.DynamicAnimation.ViewProperty
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringAnimation.TRANSLATION_X
import androidx.dynamicanimation.animation.SpringAnimation.TRANSLATION_Y
import androidx.dynamicanimation.animation.SpringForce
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.EdgeEffectFactory
import ar.com.wolox.android.cookbook.bounce.BounceOrientation.HORIZONTAL
import ar.com.wolox.android.cookbook.bounce.BounceOrientation.VERTICAL

/** In the RecyclerView.edgeEffectFactory, you only have to add BounceEffect class **/
/** overscrollTranslation: The magnitude of the translation distance while the list scrolls excessively. */
/** flingTranslation: The magnitude of translation distance when the list bounces. */

class BounceEffect(
    var orientation: BounceOrientation,
    var flingTranslation: Float = 0.5f,
    var overscrollTranslation: Float = 0.1f
) : EdgeEffectFactory() {

    override fun createEdgeEffect(recyclerView: RecyclerView, directionEffect: Int): EdgeEffect {

        return object : EdgeEffect(recyclerView.context) {
            var translationAnim: SpringAnimation? = null

            override fun onPull(deltaDistance: Float) {
                super.onPull(deltaDistance)
                handlePull(deltaDistance)
            }

            override fun onPull(deltaDistance: Float, displacement: Float) {
                super.onPull(deltaDistance, displacement)
                handlePull(deltaDistance)
            }

            private fun handlePull(deltaDistance: Float) {
                when (orientation) {
                    VERTICAL -> {
                        val translationYDelta =
                            getSign() * recyclerView.width * deltaDistance * overscrollTranslation
                        recyclerView.translationY += translationYDelta
                    }
                    HORIZONTAL -> {
                        val translationXDelta =
                            getSign() * recyclerView.height * deltaDistance * overscrollTranslation
                        recyclerView.translationX += translationXDelta
                    }
                }

                translationAnim?.cancel()
            }

            override fun onRelease() {
                super.onRelease()

                when (orientation) {
                    VERTICAL -> {
                        if (recyclerView.translationY != 0f) {
                            translationAnim = createAnim()?.also { it.start() }
                        }
                    }
                    HORIZONTAL -> {
                        if (recyclerView.translationX != 0f) {
                            translationAnim = createAnim()?.also { it.start() }
                        }
                    }
                }
            }

            override fun onAbsorb(velocity: Int) {
                super.onAbsorb(velocity)

                val translationVelocity = getSign() * velocity * flingTranslation
                translationAnim?.cancel()
                translationAnim =
                    createAnim().setStartVelocity(translationVelocity)?.also { it.start() }
            }

            override fun draw(canvas: Canvas?): Boolean {
                return false
            }

            override fun isFinished(): Boolean {
                return translationAnim?.isRunning?.not() ?: true
            }

            private fun createAnim() =
                SpringAnimation(recyclerView, getSpringAnimation())
                    .setSpring(
                        SpringForce()
                            .setFinalPosition(0f)
                            .setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY)
                            .setStiffness(SpringForce.STIFFNESS_LOW)
                    )

            private fun getSign(): Int {
                return when (orientation) {
                    VERTICAL -> if (directionEffect == DIRECTION_BOTTOM) -1 else 1
                    HORIZONTAL -> if (directionEffect == DIRECTION_LEFT) 1 else -1
                }
            }

            private fun getSpringAnimation(): ViewProperty {
                return if (orientation == VERTICAL) {
                    TRANSLATION_Y
                } else {
                    TRANSLATION_X
                }
            }
        }
    }
}

enum class BounceOrientation {
    VERTICAL,
    HORIZONTAL
}