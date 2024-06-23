package lnbti.gtp01.droidai.constants

/**
 * Enum class representing various constants related to dialog types and tags.
 *
 * This enum provides a set of predefined constants that can be used to identify and manage
 * different types of dialogs in your application. It includes values for success, failure, warning,
 * and custom dialog fragment tags.
 *
 * @property value A string value associated with each constant.
 */
enum class DialogConstants(val value: String) {
    /**
     * Represents a success dialog.
     */
    SUCCESS("success"),

    /**
     * Represents a failure dialog.
     */
    FAIL("fail"),

    /**
     * Represents a warning dialog.
     */
    WARN("warn"),

    /**
     * Represents the tag for a custom progress dialog fragment.
     */
    PROGRESS_DIALOG_FRAGMENT_TAG("CustomProgressDialogFragmentTag"),

    /**
     * Represents the tag for a custom alert dialog fragment.
     */
    ALERT_DIALOG_FRAGMENT_TAG("CustomAlertDialogFragmentTag"),

    /**
     * Represents the tag for a custom confirmation dialog fragment.
     */
    CONFIRM_DIALOG_FRAGMENT_TAG("CustomConfirmDialogFragmentTag"),

    /**
     * Represents the tag for a dealer location view dialog fragment.
     */
    LOCATION_VIEW_DIALOG_FRAGMENT_TAG("LocationViewDialogFragmentTag"),
}