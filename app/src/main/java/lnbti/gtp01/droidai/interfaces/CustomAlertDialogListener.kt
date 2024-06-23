package lnbti.gtp01.droidai.interfaces

/**
 * Interface for handling button click events in a custom AlertDialog.
 *
 * This interface defines a callback method that should be implemented to handle button click
 * events triggered within a custom AlertDialog. Classes implementing this interface can provide
 * custom logic to respond to these button clicks.
 */
interface CustomAlertDialogListener {

    /**
     * Called when a button in the custom AlertDialog is clicked.
     *
     * Implementations of this method should define the actions to be taken when a button in
     * a custom AlertDialog is clicked. This allows for custom handling of button click events.
     */
    fun onDialogButtonClicked()
}