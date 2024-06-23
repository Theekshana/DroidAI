package lnbti.gtp01.droidai.ui.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import lnbti.gtp01.droidai.R
import lnbti.gtp01.droidai.constants.DialogConstants
import lnbti.gtp01.droidai.databinding.FragmentCustomAlertDialogBinding
import lnbti.gtp01.droidai.interfaces.CustomAlertDialogListener
import lnbti.gtp01.droidai.utils.LanguageManager
import lnbti.gtp01.droidai.utils.UIUtils.Companion.changeUiSize

/**
 * This custom dialog fragment for displaying alert dialogs with customizable messages and icons.
 *
 * This fragment allows you to display alert dialogs with custom messages and icons.
 * It is used to ensure consistent styling and behavior for alert dialogs throughout the app.
 *
 * @constructor Creates a new instance of the CustomAlertDialogFragment.
 * @property binding The binding class responsible for inflating the dialog's layout.
 */
class CustomAlertDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentCustomAlertDialogBinding

    /**
     * A companion object to provide static methods for creating instances of the
     * CustomAlertDialogFragment.
     */
    companion object {
        private const val ARG_MESSAGE = "message"
        private const val ARG_TYPE = "type"
        var dialogButtonClickListener: CustomAlertDialogListener? = null

        /**
         * Creates a new instance of the CustomAlertDialogFragment with a specified message and type.
         *
         * @param message The message to display in the dialog.
         * @param type The type of the dialog (e.g., success, fail, warn).
         * @param dialogButtonClickListener The listener for dialog button click events.
         * @return A new instance of CustomAlertDialogFragment.
         */
        fun newInstance(
            message: String?,
            type: String,
            dialogButtonClickListener: CustomAlertDialogListener,
        ): CustomAlertDialogFragment {
            val fragment = CustomAlertDialogFragment()
            this.dialogButtonClickListener = dialogButtonClickListener
            val args = Bundle().apply {
                putString(ARG_MESSAGE, message)
                putString(ARG_TYPE, type)
            }
            fragment.arguments = args
            return fragment
        }

        fun newInstance(
            message: String?,
            type: String,
        ): CustomAlertDialogFragment {
            val fragment = CustomAlertDialogFragment()
            val args = Bundle().apply {
                putString(ARG_MESSAGE, message)
                putString(ARG_TYPE, type)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LanguageManager(requireContext()).loadLanguage()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), theme).apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setCanceledOnTouchOutside(false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        //Disable back button pressed dialog dismiss event
        isCancelable = false
        binding = FragmentCustomAlertDialogBinding.inflate(inflater, container, false).apply {
            // Set the ViewModel and lifecycle owner
            lifecycleOwner = this@CustomAlertDialogFragment
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            // Extract message from arguments
            val message = it.getString(ARG_MESSAGE)
            // Extract type from arguments
            val type = it.getString(ARG_TYPE)

            binding.apply {
                //Dialog Width with horizontal margin
                changeUiSize(context, dialogMainLayout, 1, 1, 30)
                //Icon width=(Device Width/5)
                changeUiSize(context, icon, 1, 5)
                // Set data to the data binding variables
                dialogMessage = message
                when (type) {
                    DialogConstants.SUCCESS.value -> imageResId = R.mipmap.done
                    DialogConstants.FAIL.value -> imageResId = R.mipmap.cancel
                    DialogConstants.WARN.value -> imageResId = R.mipmap.warning
                }

                button.setOnClickListener {
                    //Error Dialog should not want to return button click listener
                    try {
                        dialogButtonClickListener?.onDialogButtonClicked()
                    } catch (_: Exception) {

                    }
                    dismiss()
                }
            }
        }
    }
}