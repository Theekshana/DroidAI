package lnbti.gtp01.droidai.ui.weatherinfo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import lnbti.gtp01.droidai.R
import lnbti.gtp01.droidai.apiservices.WeatherApiService
import lnbti.gtp01.droidai.constants.StringConstants
import lnbti.gtp01.droidai.databinding.FragmentWeatherInfoBinding
import lnbti.gtp01.droidai.ui.main.MainActivityViewModel
import lnbti.gtp01.droidai.utils.LocationHelper
import javax.inject.Inject

/**
 * A Fragment that displays weather information based on the user's current location.
 *
 * This fragment is responsible for requesting location permissions, fetching the user's
 * current location, and then using that location to fetch and display weather data.
 *
 * @property viewModel The ViewModel associated with this fragment.
 * @property weatherApiService The API service used to fetch weather data.
 * @property locationHelper A helper class to manage location-related operations.
 */
@AndroidEntryPoint
class WeatherInfoFragment : Fragment() {

    private var _binding: FragmentWeatherInfoBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WeatherInfoViewModel by viewModels()
    private lateinit var forecastAdapter: WeatherForecastAdapter
    private lateinit var locationHelper: LocationHelper
    private lateinit var sharedViewModel: MainActivityViewModel

    @Inject
    lateinit var weatherApiService: WeatherApiService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentWeatherInfoBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        ViewModelProvider(requireActivity())[MainActivityViewModel::class.java].apply {
            sharedViewModel = this
            setFragment(StringConstants.WEATHER_INFO_FRAGMENT)
        }

        return binding.root
    }

    /**
     * Called immediately after onCreateView has returned, but before any saved state has been restored into the view.
     * This method is used to perform initializations that require the view hierarchy.
     *
     * @param view The View returned by onCreateView.
     * @param savedInstanceState A Bundle containing saved state.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Custom toolbar behavior for this fragment
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setHomeAsUpIndicator(null) // Hide back button indication
        }

        setupRecyclerView()
        observeWeatherData()

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewWeather)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Initialize LocationHelper
        locationHelper = LocationHelper(requireActivity()) { location ->
            // Fetch weather data based on the current location
            sharedViewModel.setProgressDialogVisible(true)
            viewModel.fetchWeatherData(location.latitude, location.longitude)
        }

        // Set a callback for when location permissions are granted
        locationHelper.setLocationPermissionGrantedCallback {
            locationHelper.startLocationUpdates()
        }

        // Check for location permission
        checkLocationPermission()
    }

    /**
     * Sets up the RecyclerView for displaying weather forecast data.
     */
    private fun setupRecyclerView() {
        forecastAdapter = WeatherForecastAdapter()
        binding.recyclerViewWeather.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = forecastAdapter
        }
    }

    /**
     * Observes the weather data changes and updates the RecyclerView adapter accordingly.
     */
    private fun observeWeatherData() {
        viewModel.dailyForecasts.observe(viewLifecycleOwner) { forecasts ->
            forecastAdapter.updateData(forecasts)
            sharedViewModel.setProgressDialogVisible(false)
        }
    }

    /**
     * Shows an initial rationale dialog explaining why location permission is needed.
     * This dialog is shown when the user has not yet been asked for the permission.
     */
    private fun showInitialRationaleDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.location_permission_dialog_title)
            .setMessage(R.string.location_permission_initial_dialog_message)
            .setPositiveButton(R.string.ok) { _, _ ->
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            .create()
            .show()
    }

    /**
     * Shows a rationale dialog when location permission has been denied.
     * This dialog is shown when the user has denied the permission request.
     */
    private fun showDeniedRationaleDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.location_permission_dialog_title)
            .setMessage(R.string.location_permission_denied_dialog_message)
            .setPositiveButton(R.string.ok, null) // dismisses the dialog
            .create()
            .show()
    }

    /**
     * A launcher for the result of requesting location permission.
     *
     * This launcher handles the result of the location permission request. If permission is granted,
     * it starts location updates. If permission is denied, it shows a rationale dialog to the user.
     * The launcher uses the ActivityResultContracts.RequestPermission contract to request permission
     * and receive the result.
     */
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Start location update.
                locationHelper.startLocationUpdates()
            } else {
                // Permission is denied.Show a rationale dialog.
                showDeniedRationaleDialog()
            }
        }

    /**
     * Checks if location permission is granted and requests it if not.
     * This function handles the logic for requesting location permission and showing rationale dialogs.
     */
    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                showInitialRationaleDialog()
            } else {
                // No explanation needed; request the permission using the launcher
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        } else {
            locationHelper.startLocationUpdates()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Reset toolbar behavior when the fragment is destroyed
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
        }

        _binding = null
    }
}