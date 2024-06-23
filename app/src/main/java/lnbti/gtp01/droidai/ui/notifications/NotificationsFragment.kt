package lnbti.gtp01.droidai.ui.notifications

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import lnbti.gtp01.droidai.constants.StringConstants
import lnbti.gtp01.droidai.databinding.FragmentNotificationsBinding
import lnbti.gtp01.droidai.models.NotificationData
import lnbti.gtp01.droidai.ui.main.MainActivityViewModel

/**
 * A fragment that displays notifications.
 *
 * This fragment includes custom toolbar behavior to control the appearance and behavior of the
 * toolbar in the context of notifications.
 */
class NotificationsFragment : Fragment() {

    // Binding for the fragment
    private lateinit var binding: FragmentNotificationsBinding

    // Adapter for the notification list RecyclerView
    private lateinit var notificationAdpter: NotificationAdapter

    // List to hold notification data
    private var notificationList: MutableList<NotificationData> = mutableListOf()

    // ViewModel for handling notification-related data and logic
    private lateinit var viewModel: NotificationViewModel

    // Shared ViewModel for communicating with the main activity
    private lateinit var sharedViewModel: MainActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[NotificationViewModel::class.java]

        // Initialize ViewModel instances
        ViewModelProvider(requireActivity())[MainActivityViewModel::class.java].apply {
            sharedViewModel = this
            setFragment(StringConstants.NOTIFICATION_FRAGMENT)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Custom toolbar behavior for this fragment
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setHomeAsUpIndicator(null) // Hide back button indication
        }

        // Setup RecyclerView
        setupRecyclerView()

        // Check if any notification data was sent via intent and add it to the list
        addNotificationFromIntent()

        // Retrieve all existing notifications from the ViewModel
        viewModel.getAllNotification()

        // Set up observers for LiveData in the ViewModel
        viewModelObservers()

    }

    /**
     * Set up RecyclerView to display notifications.
     */
    private fun setupRecyclerView() {
        notificationAdpter = NotificationAdapter()
        binding.notificationRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = notificationAdpter
        }
    }

    /**
     * Add notification data received from the intent to the list.
     */
    private fun addNotificationFromIntent() {

        // Retrieve notification data from the intent
        val notificationData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireActivity().intent.getParcelableExtra(
                "data",
                NotificationData::class.java
            )
        } else {
            requireActivity().intent.getParcelableExtra(
                "data"
            )
        }

        // Add notification data to the list if it exists
        if (notificationData != null) {
            val title = notificationData
                .title ?: requireActivity()
                .intent.getStringExtra("title")
            val message = notificationData
                .message ?: requireActivity()
                .intent.getStringExtra("message")

            val newNotification = NotificationData(
                title,
                message
            )

            // Clear existing notifications before adding new ones
            notificationList.clear()

            // Add the retrieved NotificationData object to the list
            notificationList.add(0, newNotification)

            // Update the adapter
            notificationAdpter.differ.submitList(notificationList)

            // Save the notification data to the ViewModel
            viewModel.saveNotification(notificationData)

            // Clear the intent data to avoid duplication
            requireActivity().intent.removeExtra("data")
        }


    }

    /**
     * Set up observers for LiveData in the ViewModel.
     */
    private fun viewModelObservers() {

        // Observe LiveData for inquiry result data
        viewModel.notificationResult.observe(viewLifecycleOwner) { notifications ->
            notificationAdpter.differ.submitList(notifications)

        }
        viewModel.getAllNotification()
    }

    /**
     * Called when the fragment's view is destroyed.
     *
     * This is mainly used to reset toolbar behavior when the fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()

        // Reset toolbar behavior when the fragment is destroyed
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
        }
    }
}