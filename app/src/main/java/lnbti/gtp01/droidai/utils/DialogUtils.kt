package lnbti.gtp01.droidai.utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import lnbti.gtp01.droidai.constants.DialogConstants
import lnbti.gtp01.droidai.interfaces.ConfirmDialogButtonClickListener
import lnbti.gtp01.droidai.interfaces.CustomAlertDialogListener
import lnbti.gtp01.droidai.ui.dialogs.CustomAlertDialogFragment
import lnbti.gtp01.droidai.ui.dialogs.CustomConfirmAlertDialogFragment
import lnbti.gtp01.droidai.ui.dialogs.CustomProgressDialogFragment

/**
 * Utility class for managing custom dialogs in application.
 * This class provides methods to show custom alert dialogs and progress dialogs.
 */
class DialogUtils {
    /**
     * A companion object to provide static methods for creating custom dialogs.
     */
    companion object {
        /**
         * Show a custom alert dialog without any button click event.
         *
         * @param context The context in which the dialog should be shown.
         * @param type The type of the dialog (Success, Fail, or Warn Alert).
         * @param message The message body to be displayed in the dialog.
         */
        fun showAlertDialogWithoutAction(
            context: Context, type: String, message: String?
        ) {
            (context as? AppCompatActivity)?.supportFragmentManager?.let { fragmentManager ->
                CustomAlertDialogFragment.newInstance(message, type).show(
                    fragmentManager,
                    DialogConstants.ALERT_DIALOG_FRAGMENT_TAG.value
                )
            }
        }

        /**
         * Show a custom alert dialogCodeium  without any button click event.
         *
         * @param context The context in which the dialog should be shown.
         * @param type The type of the dialog (Success, Fail, or Warn Alert).
         * @param message The message body to be displayed in the dialog.
         * @param listener OK button click event.
         */
        fun showAlertDialogWithAction(
            context: Context, type: String, message: String?, listener: CustomAlertDialogListener
        ) {
            (context as? AppCompatActivity)?.supportFragmentManager?.let { fragmentManager ->
                CustomAlertDialogFragment.newInstance(message, type, listener).show(
                    fragmentManager,
                    DialogConstants.ALERT_DIALOG_FRAGMENT_TAG.value
                )
            }
        }

        /**
         * Show a custom alert dialog with an icon inside a fragment.
         *
         * @param fragment The fragment in which the dialog should be shown.
         * @param message The message body to be displayed in the dialog.
         * @param type The type of the dialog (Success, Fail, or Warn Alert).
         * @return The created dialog fragment.
         */
        fun showDialogWithoutActionInFragment(
            fragment: Fragment, type: String, message: String?
        ): DialogFragment {

            return fragment.parentFragmentManager.let { fragmentManager ->
                CustomAlertDialogFragment.newInstance(message, type).apply {
                    show(fragmentManager, DialogConstants.ALERT_DIALOG_FRAGMENT_TAG.value)
                }
            }
        }

        /**
         * Show a custom alert dialog with an icon inside and with click event a fragment.
         *
         * @param fragment The fragment in which the dialog should be shown.
         * @param message The message body to be displayed in the dialog.
         * @param type The type of the dialog (Success, Fail, or Warn Alert).
         * @param listener OK button click event.
         * @return The created dialog fragment.
         */
        fun showDialogWithActionInFragment(
            fragment: Fragment, message: String?, type: String, listener: CustomAlertDialogListener
        ): DialogFragment {

            return fragment.parentFragmentManager.let { fragmentManager ->
                CustomAlertDialogFragment.newInstance(message, type, listener).apply {
                    show(fragmentManager, DialogConstants.ALERT_DIALOG_FRAGMENT_TAG.value)
                }
            }
        }

        /**
         * Show a custom confirm alert dialog with an icon inside an activity.
         *
         * @param context The context in which the dialog should be shown.
         * @param message The message body to be displayed in the dialog.
         * @param dialogButtonClickListener The listener for dialog button click events.
         */
        fun showConfirmAlertDialog(
            context: Context,
            message: String?,
            dialogButtonClickListener: ConfirmDialogButtonClickListener
        ) {

            (context as? AppCompatActivity)?.supportFragmentManager?.let { fragmentManager ->
                CustomConfirmAlertDialogFragment.newInstance(message, dialogButtonClickListener)
                    .show(
                        fragmentManager,
                        DialogConstants.CONFIRM_DIALOG_FRAGMENT_TAG.value
                    )
            }
        }

        /**
         * Show a custom confirm alert dialog with an icon inside a fragment.
         *
         * @param fragment The fragment in which the dialog should be shown.
         * @param message The message body to be displayed in the dialog.
         * @param dialogButtonClickListener The listener for dialog button click events.
         */
        fun showConfirmAlertDialogInFragment(
            fragment: Fragment,
            message: String?,
            dialogButtonClickListener: ConfirmDialogButtonClickListener
        ) {

            fragment.parentFragmentManager.let { fragmentManager ->
                CustomConfirmAlertDialogFragment.newInstance(message, dialogButtonClickListener)
                    .show(
                        fragmentManager,
                        DialogConstants.CONFIRM_DIALOG_FRAGMENT_TAG.value
                    )
            }
        }

        /**
         * Show a progress dialog inside a fragment.
         *
         * @param activity The fragment in which the progress dialog should be shown.
         * @param message The progress message to be displayed.
         * @return The created progress dialog fragment.
         */
        fun showProgressDialog(context: Context, message: String?): DialogFragment? {
            return (context as? AppCompatActivity)?.supportFragmentManager?.let { fragmentManager ->
                CustomProgressDialogFragment.newInstance(message).apply {
                    show(fragmentManager, DialogConstants.PROGRESS_DIALOG_FRAGMENT_TAG.value)
                }
            }
        }

    }
}