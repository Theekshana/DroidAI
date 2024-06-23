package lnbti.gtp01.droidai.utils

import android.R
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.DisplayMetrics
import android.view.View
import com.google.android.material.textfield.TextInputLayout

/**
 * Utils class for common UI-related methods.
 */
class UIUtils {
    /**
     * Companion object containing utility methods for UI-related operations.
     */
    companion object {

        /**
         * Convert dp values to pixels.
         *
         * @param dp The value in dp to be converted to pixels.
         * @param context The context of the application.
         * @return The converted value in pixels.
         */
        private fun convertDpToPixel(dp: Float, context: Context?): Float {
            return context?.run {
                dp * (resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
            } ?: 0.0f
        }

        /**
         * Change the size of a UI element according to the screen size.
         *
         * @param context The context of the view.
         * @param view The layout element to be resized.
         * @param widthRatio View width according to widthRatio*(display/width).
         * @param width Screen divide into (display/width).
         * @param margin Horizontal margin.
         * Ex: width=4 and widthRatio=3 means the actual element width= ((Screen Width/4)*3)-(2*margin).
         *
         */
        fun changeUiSize(context: Context?, view: View, widthRatio: Int, width: Int, margin: Int) {
            context?.let { ctx ->
                val display = ctx.resources.displayMetrics
                view.layoutParams?.let { params ->
                    params.width =
                        ((display.widthPixels * widthRatio) / width - convertDpToPixel(
                            margin.toFloat(),
                            ctx
                        ) * 2).toInt()
                    view.layoutParams = params
                }
            }
        }

        /**
         * Change the size of a UI element according to the screen size.
         * Element has no margin.
         *
         * @param context The context of the view.
         * @param view The layout element to be resized.
         * @param widthRatio View width according to widthRatio*(display/width).
         * @param width Screen divide into (display/width).
         * Ex: width=4 and widthRatio=3 means the actual element width= (Screen Width/4)*3.
         */
        fun changeUiSize(context: Context?, view: View, widthRatio: Int, width: Int) {
            context?.let { ctx ->
                val display = ctx.resources.displayMetrics
                view.layoutParams?.let { params ->
                    params.width =
                        display.widthPixels * widthRatio / width
                    view.layoutParams = params
                }
            }
        }

        /**
         * Sets the input layout to a valid state by disabling error, setting custom end icon mode,
         * and customizing the end icon drawable and its tint list.
         *
         * @param inputLayout The TextInputLayout to be validated.
         * @param icon The resource ID of the drawable to be set as the end icon.
         */
        fun validState(context: Context,inputLayout: TextInputLayout?, icon: Int) {
            // Define a ColorStateList for customizing the end icon tint
            val myColorStateList = ColorStateList(
                arrayOf(intArrayOf(R.attr.state_activated), intArrayOf(R.attr.state_enabled)),
                intArrayOf(
                    Color.parseColor(context.getString(lnbti.gtp01.droidai.R.string.input_activated_color)),  // Active and enabled state color
                    Color.parseColor(context.getString(lnbti.gtp01.droidai.R.string.input_default_color))   // Default state color
                )
            )

            // Disable error if the input layout is not null
            inputLayout?.isErrorEnabled = false

            // Set end icon mode to custom if the input layout is not null
            inputLayout?.endIconMode = TextInputLayout.END_ICON_CUSTOM

            // Set the end icon drawable if the input layout is not null
            inputLayout?.setEndIconDrawable(icon)

            // Set the end icon tint list if the input layout is not null
            inputLayout?.setEndIconTintList(myColorStateList)
        }
    }
}