package lnbti.gtp01.droidai.ui.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import lnbti.gtp01.droidai.R
import lnbti.gtp01.droidai.constants.DialogConstants
import lnbti.gtp01.droidai.constants.PreferenceKeys
import lnbti.gtp01.droidai.constants.StringConstants.ACTION_NOTIFICATION_RECEIVED
import lnbti.gtp01.droidai.constants.StringConstants.ADD_CALENDAR_EVENT_FRAGMENT
import lnbti.gtp01.droidai.constants.StringConstants.AGRI_INSPECTOR
import lnbti.gtp01.droidai.constants.StringConstants.AI_HOME_FRAGMENT
import lnbti.gtp01.droidai.constants.StringConstants.AI_RESPONSE_FRAGMENT
import lnbti.gtp01.droidai.constants.StringConstants.CATEGORY_LIST_FRAGMENT
import lnbti.gtp01.droidai.constants.StringConstants.CHANGE_PASSWORD_FRAGMENT
import lnbti.gtp01.droidai.constants.StringConstants.CONTENT_DETAILS_FRAGMENT
import lnbti.gtp01.droidai.constants.StringConstants.CONTENT_DETAILS_WITH_FILTER_FRAGMENT
import lnbti.gtp01.droidai.constants.StringConstants.CROP_CALENDAR_FRAGMENT
import lnbti.gtp01.droidai.constants.StringConstants.DEALER_LOCATIONS_FRAGMENT
import lnbti.gtp01.droidai.constants.StringConstants.EXPERTS_FRAGMENT
import lnbti.gtp01.droidai.constants.StringConstants.HOME_FRAGMENT
import lnbti.gtp01.droidai.constants.StringConstants.INQUIRY_FRAGMENT
import lnbti.gtp01.droidai.constants.StringConstants.LOGIN_FRAGMENT
import lnbti.gtp01.droidai.constants.StringConstants.MENU_LIST_FRAGMENT
import lnbti.gtp01.droidai.constants.StringConstants.NOTIFICATION_FRAGMENT
import lnbti.gtp01.droidai.constants.StringConstants.RESPONSE_VIEW_FRAGMENT
import lnbti.gtp01.droidai.constants.StringConstants.SETTINGS_FRAGMENT
import lnbti.gtp01.droidai.constants.StringConstants.SUB_CATEGORY_LIST_FRAGMENT
import lnbti.gtp01.droidai.constants.StringConstants.WEATHER_INFO_FRAGMENT
import lnbti.gtp01.droidai.constants.StringConstants.WELCOME_FRAGMENT
import lnbti.gtp01.droidai.databinding.ActivityMainBinding
import lnbti.gtp01.droidai.interfaces.ConfirmDialogButtonClickListener
import lnbti.gtp01.droidai.interfaces.SuccessListener
import lnbti.gtp01.droidai.ui.signin.LoginViewModel
import lnbti.gtp01.droidai.utils.DialogUtils.Companion.showAlertDialogWithoutAction
import lnbti.gtp01.droidai.utils.DialogUtils.Companion.showConfirmAlertDialog
import lnbti.gtp01.droidai.utils.DialogUtils.Companion.showProgressDialog
import lnbti.gtp01.droidai.utils.LanguageManager
import lnbti.gtp01.droidai.utils.NetworkUtils
import lnbti.gtp01.droidai.utils.SharedPreferencesManager
import lnbti.gtp01.droidai.utils.SharedPreferencesManager.Companion.clearAllPref
import lnbti.gtp01.droidai.utils.Utils.Companion.getLoggedInUser

/**
 * Main activity of the application responsible for hosting the navigation graph
 * and handling interactions with the bottom menu and toolbar.
 *
 * This activity initializes the [NavController], binds the layout using Data Binding,
 * and sets up the navigation with the toolbar.
 *
 * @property binding The binding object for the main activity layout.
 * @property navController The NavController responsible for managing navigation within the app.
 * @property sharedViewModel The shared ViewModel used by the main activity.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var sharedViewModel: MainActivityViewModel
    private lateinit var loginViewModel: LoginViewModel
    private var dialog: DialogFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LanguageManager(this).loadLanguage()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(null)
        // Initialize NetworkUtils with the system's connectivity service.
        getSystemService(Context.CONNECTIVITY_SERVICE)
            .let { it as ConnectivityManager }
            .let { NetworkUtils.init(it) }

        // Set up Data Binding
        DataBindingUtil.setContentView<ActivityMainBinding?>(this, R.layout.activity_main).apply {
            binding = this
            // Set up NavHostFragment and NavController
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            navController = navHostFragment.navController

            // Set the NavController for the toolbar
            setSupportActionBar(toolbar)
            setupWithNavController(toolbar, navController)

            ViewModelProvider(this@MainActivity)[LoginViewModel::class.java].apply {
                loginViewModel = this
            }

            // Initialize shared ViewModel
            ViewModelProvider(this@MainActivity)[MainActivityViewModel::class.java].apply {
                sharedViewModel = this
            }

            bottomMenu.apply {
                // Set click listener for the notifications button in the bottom menu
                notificationsButton.setOnClickListener {
                    goToNotificationFragment()
                }

                // Set click listener for the calendar button in the bottom menu
                calendarButton.setOnClickListener {
                    goToCropCalendarFragment()
                }
                // Set click listener for the camera button in the bottom menu
                cameraButton.setOnClickListener {
                    goToInquiresFragment()
                }
            }

            drawerSideMenu.apply {

                binding.apply {
                    drawerLayout.apply {
                        closeBtn.setOnClickListener {
                            closeDrawer(GravityCompat.START)
                        }

                        homeButtonLayout.setOnClickListener {
                            closeDrawer(GravityCompat.START)
                            if (getLoggedInUser()?.userRole == AGRI_INSPECTOR)
                                goToAIHomeFragment()
                            else
                                goToHomeFragment()
                        }

                        settingsLayout.setOnClickListener {
                            closeDrawer(GravityCompat.START)
                            goToSettingsFragment()
                        }

                        categoryLayout.setOnClickListener {
                            closeDrawer(GravityCompat.START)
                            goToCategoryFragment()
                        }

                        dealerLocationLayout.setOnClickListener {
                            closeDrawer(GravityCompat.START)
                            navController.navigate(R.id.dealerLocationsFragment)
                        }

                        logoutButton.setOnClickListener {
                            closeDrawer(GravityCompat.START)
                            showConfirmAlertDialog(this@MainActivity,
                                getString(R.string.logout_confirmation_message),
                                object : ConfirmDialogButtonClickListener {
                                    override fun onPositiveButtonClick() {

                                        clearAllPref(object : SuccessListener {
                                            override fun onSuccess() {
                                                SharedPreferencesManager.savePreferenceBool(
                                                    PreferenceKeys.APP_LAUNCHED_STATUS,
                                                    true,
                                                    object : SuccessListener {
                                                        override fun onSuccess() {
                                                            loginViewModel.resetLiveData()
                                                            goToLoginFragment()
                                                        }
                                                    })
                                            }
                                        })

                                    }

                                    override fun onNegativeButtonClick() {
                                    }
                                }
                            )
                        }

                        leftButton.setOnClickListener {
                            if (sharedViewModel.showHamburgerMenu.value == true) {
                                openDrawer(GravityCompat.START)
                            } else {
                                navController.popBackStack()
                            }
                        }

                        // Find the weather info item in the navigation drawer and set a click listener
                        val weatherInfoItem = findViewById<ConstraintLayout>(R.id.weather_info_item)
                        weatherInfoItem.setOnClickListener {
                            // Close the navigation drawer
                            closeDrawer(GravityCompat.START)
                            navController.navigate(R.id.weatherInfoFragment)
                        }

                        // Find the weather info item in the navigation drawer and set a click listener
                        val contactExpertsItem =
                            findViewById<ConstraintLayout>(R.id.contact_experts_item)
                        contactExpertsItem.setOnClickListener {
                            closeDrawer(GravityCompat.START)
                            navController.navigate(R.id.agricInspectorsFragment)
                        }

                    }
                }
            }

            homeButton.setOnClickListener {
                if (getLoggedInUser()?.userRole == AGRI_INSPECTOR)
                    goToAIHomeFragment()
                else
                    goToHomeFragment()
            }

        }

        viewModelObservers()

        // Handle back button press for Home Fragment
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showConfirmAlertDialog(this@MainActivity,
                    getString(R.string.exit_confirmation_message),
                    object : ConfirmDialogButtonClickListener {
                        override fun onPositiveButtonClick() {
                            finish()
                        }

                        override fun onNegativeButtonClick() {
                        }
                    }
                )
            }
        }.apply {
            onBackPressedDispatcher.addCallback(this@MainActivity, this)
        }

    }

    /**
     * Navigates to the settings fragment when the settings button is clicked.
     */
    private fun goToSettingsFragment() {
        navController.navigate(R.id.settingsFragment)
    }

    /**
     * Navigates to the inquiries fragment when the notifications button is clicked.
     */
    private fun goToInquiresFragment() {
        navController.navigate(R.id.inquiriesFragment)
    }

    /**
     * Navigates to the notifications fragment when the notifications button is clicked.
     */
    private fun goToNotificationFragment() {
        navController.navigate(R.id.notificationsFragment)
    }

    /**
     * Navigates to the crop calendar fragment when the calendar button is clicked.
     */
    private fun goToCropCalendarFragment() {
        navController.navigate(R.id.cropCalendarFragment)
    }

    /**
     * Navigates to the crop category fragment when the side menu category button is clicked.
     */
    private fun goToCategoryFragment() {
        navController.navigate(R.id.categoriesFragment)
    }

    /**
     * Navigates to the login fragment
     */
    private fun goToLoginFragment() {
        navController.popBackStack(R.id.loginFragment, false)
    }

    /**
     * Navigates to the home fragment and clears the back stack.
     */
    private fun goToHomeFragment() {
        navController.popBackStack(R.id.homeFragment, false)
    }

    private fun goToAIHomeFragment() {
        navController.popBackStack(R.id.inspectorHomeFragment, false)
    }

    /**
     * Handles the navigation up action.
     *
     * @return True if the navigation was handled by the NavController, false otherwise.
     */
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    /**
     * Observes LiveData updates from the ViewModel and updates the UI accordingly.
     */
    private fun viewModelObservers() {

        sharedViewModel.apply {
            binding.apply {
                fragment.observe(this@MainActivity) {
                    // Set the title text based on the observed fragment
                    when (it) {
                        WELCOME_FRAGMENT -> {
                            toolbar.isVisible = false
                            bottomMenu.mainLayout.isVisible = false
                        }

                        LOGIN_FRAGMENT -> {
                            toolbar.isVisible = false
                            bottomMenu.mainLayout.isVisible = false
                        }

                        HOME_FRAGMENT -> {
                            toolbar.isVisible = true
                            showHamburgerMenu(true)
                            bottomMenu.mainLayout.isVisible = true
                        }

                        AI_HOME_FRAGMENT -> {
                            toolbar.isVisible = true
                            showHamburgerMenu(false)
                            bottomMenu.mainLayout.isVisible = false
                        }

                        CROP_CALENDAR_FRAGMENT -> {
                            toolbar.isVisible = true
                            showHamburgerMenu(false)
                            bottomMenu.mainLayout.isVisible = false
                        }

                        ADD_CALENDAR_EVENT_FRAGMENT -> {
                            toolbar.isVisible = true
                            showHamburgerMenu(false)
                            bottomMenu.mainLayout.isVisible = false
                        }

                        MENU_LIST_FRAGMENT -> {
                            toolbar.isVisible = true
                            showHamburgerMenu(false)
                            bottomMenu.mainLayout.isVisible = false
                        }

                        INQUIRY_FRAGMENT -> {
                            toolbar.isVisible = true
                            showHamburgerMenu(false)
                            bottomMenu.mainLayout.isVisible = false
                        }

                        CHANGE_PASSWORD_FRAGMENT -> {
                            toolbar.isVisible = true
                            showHamburgerMenu(false)
                            bottomMenu.mainLayout.isVisible = false
                        }

                        CONTENT_DETAILS_FRAGMENT -> {
                            toolbar.isVisible = true
                            showHamburgerMenu(false)
                            bottomMenu.mainLayout.isVisible = false
                        }

                        CONTENT_DETAILS_WITH_FILTER_FRAGMENT -> {
                            toolbar.isVisible = true
                            showHamburgerMenu(false)
                            bottomMenu.mainLayout.isVisible = false
                        }

                        AI_RESPONSE_FRAGMENT -> {
                            toolbar.isVisible = true
                            showHamburgerMenu(false)
                            bottomMenu.mainLayout.isVisible = false
                        }

                        RESPONSE_VIEW_FRAGMENT -> {
                            toolbar.isVisible = true
                            showHamburgerMenu(false)
                            bottomMenu.mainLayout.isVisible = false
                        }

                        NOTIFICATION_FRAGMENT -> {
                            toolbar.isVisible = true
                            showHamburgerMenu(false)
                            bottomMenu.mainLayout.isVisible = false

                        }

                        CATEGORY_LIST_FRAGMENT -> {
                            toolbar.isVisible = true
                            showHamburgerMenu(true)
                            bottomMenu.mainLayout.isVisible = false
                        }

                        SETTINGS_FRAGMENT -> {
                            toolbar.isVisible = true
                            showHamburgerMenu(true)
                            bottomMenu.mainLayout.isVisible = false
                        }

                        EXPERTS_FRAGMENT -> {
                            toolbar.isVisible = true
                            showHamburgerMenu(true)
                            bottomMenu.mainLayout.isVisible = false
                        }

                        WEATHER_INFO_FRAGMENT -> {
                            toolbar.isVisible = true
                            showHamburgerMenu(true)
                            bottomMenu.mainLayout.isVisible = false
                        }

                        SUB_CATEGORY_LIST_FRAGMENT -> {
                            toolbar.isVisible = true
                            showHamburgerMenu(false)
                            bottomMenu.mainLayout.isVisible = false
                        }

                        DEALER_LOCATIONS_FRAGMENT -> {
                            toolbar.isVisible = true
                            showHamburgerMenu(true)
                            bottomMenu.mainLayout.isVisible = false
                        }
                    }
                }

            }

            /* Live data observer to show/hide progress dialog */
            isProgressDialogVisible.observe(this@MainActivity) { isVisible ->
                isVisible?.let { showDialog ->
                    when {
                        // If the showDialog == true, show the dialog else dismiss the dialog
                        showDialog -> {
                            dialog?.dismiss()
                            dialog = showProgressDialog(
                                this@MainActivity, getString(R.string.progressing)
                            )
                        }

                        else -> dialog?.dismiss()
                    }
                }
            }

            errorMessage.observe(this@MainActivity) { errorMessage ->
                errorMessage?.let {
                    dialog?.dismiss()
                    showAlertDialogWithoutAction(
                        this@MainActivity,
                        DialogConstants.FAIL.value,
                        errorMessage
                    )
                }
            }

            successMessage.observe(this@MainActivity) { message ->
                message?.let {
                    dialog?.dismiss()
                    showAlertDialogWithoutAction(
                        this@MainActivity,
                        DialogConstants.SUCCESS.value,
                        message
                    )
                }
            }

            warnMessage.observe(this@MainActivity) { message ->
                message?.let {
                    dialog?.dismiss()
                    showAlertDialogWithoutAction(
                        this@MainActivity,
                        DialogConstants.WARN.value,
                        message
                    )
                }
            }

            /**
             * Observe changes in a LiveData and update the bottom menu of the MainActivity accordingly.
             *
             * @param @MainActivity The current MainActivity instance where this code is executed.
             */
            updateLabels.observe(this@MainActivity) {
                updateMenuValues(this@MainActivity, binding.bottomMenu.mainLayout)
                updateSideValues(this@MainActivity, binding.drawerSideMenu.root)
            }

            showHamburgerMenu.observe(this@MainActivity) { isVisible ->
                if (isVisible) {
                    binding.leftButton.setImageResource(R.drawable.hamburger)
                } else {
                    binding.leftButton.setImageResource(R.drawable.left_arrow)
                }
            }

            loggedInUser.observe(this@MainActivity) {
                binding.drawerSideMenu.apply {

                    it?.let { user ->
                        userName.text = user.getName()
                        tvUserRole.text = user.userRole
                    }

                }
            }
        }

    }

    private fun updateMenuValues(context: Context?, menu: View) {
        context?.let {
            menu.let {
                //Localize according to the selected app language
                menu.findViewById<TextView>(R.id.cropCaledarLabel)?.text =
                    getString(R.string.crop_calendar)
                menu.findViewById<TextView>(R.id.notificationsLabel)?.text =
                    getString(R.string.notifications)
                menu.findViewById<TextView>(R.id.cameraLabel)?.text =
                    getString(R.string.camera)
            }
        }
    }

    private fun updateSideValues(context: Context?, menu: View) {
        context?.let {
            menu.let {
                //Localize according to the selected app language
                menu.findViewById<TextView>(R.id.logoutButtonLabel)?.text =
                    getString(R.string.log_out)
                menu.findViewById<TextView>(R.id.homeLabel)?.text = getString(R.string.home)
                menu.findViewById<TextView>(R.id.contactOurExpertsLabel)?.text =
                    getString(R.string.contact_our_experts)

                menu.findViewById<TextView>(R.id.weatherInfoLabel)?.text =
                    getString(R.string.weather_info_title)

                menu.findViewById<TextView>(R.id.dealerLocationLabel)?.text =
                    getString(R.string.dealer_locations)

                menu.findViewById<TextView>(R.id.categoriesLabel)?.text =
                    getString(R.string.cropcategories)

                menu.findViewById<TextView>(R.id.settingsLabel)?.text = getString(R.string.settings)
            }
        }
    }

    /**
     * BroadcastReceiver to handle notification arrival.
     * When a notification is received, it makes the notification indicator visible.
     */
    private val notificationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == ACTION_NOTIFICATION_RECEIVED) {
                val notificationIndicator =
                    binding.root.findViewById<View>(R.id.notificationIndicator)
                notificationIndicator.visibility = View.VISIBLE
            }
        }
    }

    /**
     * Registers the notification receiver when the activity is resumed.
     */
    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            notificationReceiver,
            IntentFilter(ACTION_NOTIFICATION_RECEIVED)
        )

    }

    /**
     * Unregisters the notification receiver when the activity is destroyed.
     */
    override fun onDestroy() {
        super.onDestroy()
        // Unregister BroadcastReceiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(notificationReceiver)
    }
}
