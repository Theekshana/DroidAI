package lnbti.gtp01.droidai.ui.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import lnbti.gtp01.droidai.databinding.FragmentCustomConfirmAlertDialogBinding
import lnbti.gtp01.droidai.interfaces.ConfirmDialogButtonClickListener
import lnbti.gtp01.droidai.utils.LanguageManager
import lnbti.gtp01.droidai.utils.UIUtils.Companion.changeUiSize

/**
 * A custom implementation of a confirmation dialog fragment with customizable buttons.
 *
 * This dialog provides a simple confirmation UI with two buttons: "Yes" and "No".
 * It prevents dismissal on touch outside and disables the back button to ensure user interaction
 * with the provided buttons only. The appearance of the dialog is customizable, and it allows
 * setting a message to be displayed.
 *
 * @constructor Creates a new instance of [CustomConfirmAlertDialogFragment].
 * @property binding The binding class responsible for inflating the dialog's layout.
 */
class CustomConfirmAlertDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentCustomConfirmAlertDialogBinding

    /**
     * Companion object to provide a static factory method [newInstance] for creating instances
     * of [CustomConfirmAlertDialogFragment].
     */
    companion object {
        private const val ARG_MESSAGE = "message"
        lateinit var dialogButtonClickListener: ConfirmDialogButtonClickListener

        /**
         * Creates a new instance of [CustomConfirmAlertDialogFragment] with the specified message
         * and button click listener.
         *
         * @param message The message to be displayed in the dialog.
         * @param dialogButtonClickListener The listener for button click events.
         * @return A new instance of [CustomConfirmAlertDialogFragment].
         */
        fun newInstance(
            message: String?,
            dialogButtonClickListener: ConfirmDialogButtonClickListener
        ): CustomConfirmAlertDialogFragment {
            val fragment = CustomConfirmAlertDialogFragment()
            Companion.dialogButtonClickListener = dialogButtonClickListener
            val args = Bundle().apply {
                putString(ARG_MESSAGE, message)
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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        //Disable back button pressed dialog dismiss event
        isCancelable = false
        binding =
            FragmentCustomConfirmAlertDialogBinding.inflate(inflater, container, false).apply {
                lifecycleOwner = this@CustomConfirmAlertDialogFragment
            }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the message from arguments and set it to the data binding variables
        arguments?.getString(ARG_MESSAGE).let {

            binding.apply {
                //Dialog Width with horizontal margin
                changeUiSize(context, dialogMainLayout, 1, 1, 30)
                //Icon width=(Device Width/5)
                changeUiSize(context, icon, 1, 5)

                dialogMessage = it

                buttonYes.setOnClickListener {
                    dialogButtonClickListener.onPositiveButtonClick()
                    dismiss()
                }

                buttonNo.setOnClickListener {
                    dialogButtonClickListener.onNegativeButtonClick()
                    dismiss()
                }
            }
        }
    }
}