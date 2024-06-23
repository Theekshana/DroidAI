package lnbti.gtp01.droidai.ui.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import lnbti.gtp01.droidai.databinding.NotificationListBinding
import lnbti.gtp01.droidai.models.NotificationData

/**
 * Adapter for displaying notifications in a RecyclerView.
 */
class NotificationAdapter : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    // Position of the currently expanded item, initialized to RecyclerView.NO_POSITION
    private var expandedPosition: Int = RecyclerView.NO_POSITION

    /**
     * ViewHolder for each notification item.
     */
    inner class NotificationViewHolder(
        val binding: NotificationListBinding,
    ) : RecyclerView.ViewHolder(binding.root)

    // Callback for calculating the difference between two lists
    private val differCallBack =
        object : DiffUtil.ItemCallback<NotificationData>() {
            override fun areItemsTheSame(
                oldItem: NotificationData,
                newItem: NotificationData,
            ): Boolean {
                return oldItem.title == newItem.title && oldItem.message == newItem.message
            }

            override fun areContentsTheSame(
                oldItem: NotificationData,
                newItem: NotificationData,
            ): Boolean {
                // Compare if items have the same title and name
                return oldItem.title == newItem.title &&
                        oldItem.message == newItem.message
            }
        }

    // List differ to compute the differences between two lists
    val differ = AsyncListDiffer(this, differCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {

        val itemView = NotificationListBinding.inflate(
            LayoutInflater.from(
                parent.context
            ), parent,
            false
        )
        return NotificationViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        // Return the size of the current list
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = differ.currentList[position]

        // Check if this item is the currently expanded one
        val isExpanded = position == expandedPosition

        holder.binding.apply {
            txtTitle.text = notification.title
            txtMessage.text = notification.message

            // Update the visibility of phone number and description based on the expanded state
            txtMessage.visibility = if (isExpanded) View.VISIBLE else View.GONE
            rotateArrow.rotation = if (isExpanded) 270F else 0f

            expandedLayout.setOnClickListener {
                // Toggle the expanded state
                expandedPosition = if (isExpanded) RecyclerView.NO_POSITION else position
                // Notify RecyclerView about the change
                notifyItemChanged(position)

            }
        }
    }
}
