package lnbti.gtp01.droidai.interfaces

/**
 * An interface to handle button click events for a confirmation dialog.
 * Implement this interface to define actions when the positive and negative
 * buttons of the dialog are clicked.
 */
interface ConfirmDialogButtonClickListener {
    /**
     * Callback triggered when the positive button of the confirmation dialog is clicked.
     * Implement this function to define the action to be taken when the user confirms.
     */
    fun onPositiveButtonClick()
    /**
     * Callback triggered when the negative button of the confirmation dialog is clicked.
     * Implement this function to define the action to be taken when the user cancels or dismisses the dialog.
     */
    fun onNegativeButtonClick()

}