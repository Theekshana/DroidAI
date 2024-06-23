package lnbti.gtp01.droidai.bindadapters

import android.widget.Button
import android.widget.TextView
import androidx.databinding.BindingAdapter
import lnbti.gtp01.droidai.R

/**
 * This object provides custom Data Binding adapters that can be used to bind data and behavior
 * to views in your Android application layout XML files. These adapters are used to simplify
 * the process of working with custom attributes defined in your XML layouts.
 */
object BindingAdapters {

    /**
     * A Data Binding adapter for setting the background and icon of a view based on a boolean flag.
     *
     * @param view The View for which the background and icon should be set.
     * @param shouldSelected A boolean flag indicating if the view should be selected.
     */
    @BindingAdapter("itemBackground")
    @JvmStatic
    fun setLanguageButtonBackground(view: Button, shouldSelected: Boolean) {
        when {
            shouldSelected -> {
                view.setBackgroundResource(R.drawable.brown_button_background)
            }

            else -> {
                view.setBackgroundResource(R.drawable.grey_button_background)
            }
        }
    }

    @BindingAdapter("selected")
    @JvmStatic
    fun languageSelectedTextView(textView: TextView, shouldSelected: Boolean) {
        when {
            shouldSelected -> {
                textView.setTextAppearance(R.style.languageSelectedTextStyle)
            }

            else -> {
                textView.setTextAppearance(R.style.languageUnSelectedTextStyle)
            }
        }
    }
}