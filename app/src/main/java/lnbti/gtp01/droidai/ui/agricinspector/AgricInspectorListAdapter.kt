package lnbti.gtp01.droidai.ui.agricinspector

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import lnbti.gtp01.droidai.R
import lnbti.gtp01.droidai.databinding.ItemAgricInspectorBinding
import lnbti.gtp01.droidai.models.AgricInspector

/**
 * Adapter for displaying a list of agricultural inspectors.
 *
 * This adapter is responsible for managing the display of a list of agricultural inspectors
 * in a RecyclerView. It uses data binding to efficiently display inspector information.
 *
 * @param itemClickListener A lambda function to handle click events on inspector items.
 */
class AgricInspectorListAdapter(
    private val itemClickListener: (AgricInspector) -> Unit
) : ListAdapter<AgricInspector, AgricInspectorListAdapter.InspectorViewHolder>(DIFF_CALLBACK) {

    /**
     * Creates a new ViewHolder by inflating the layout for an item view.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new InspectorViewHolder that holds a View of the given view type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InspectorViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemAgricInspectorBinding>(
            inflater, R.layout.item_agric_inspector, parent, false)
        return InspectorViewHolder(binding)
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the item at the given position
     * in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: InspectorViewHolder, position: Int) {
        val inspector = getItem(position)
        holder.bind(inspector, itemClickListener)
    }

    /**
     * ViewHolder for displaying individual agricultural inspector items.
     *
     * @param binding The data binding instance for the item view.
     */
    class InspectorViewHolder(private val binding: ItemAgricInspectorBinding) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds the inspector data to the item view.
         *
         * @param inspector The agricultural inspector to bind.
         * @param clickListener The click listener to handle click events on the item view.
         */
        fun bind(inspector: AgricInspector, clickListener: (AgricInspector) -> Unit) {
            binding.inspector = inspector
            binding.executePendingBindings()
            binding.root.setOnClickListener { clickListener(inspector) }
        }
    }


    companion object {

        /**
         * Callback for calculating the difference between two lists of agricultural inspectors.
         */
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AgricInspector>() {

            /**
             * Called to check whether two objects represent the same item.
             *
             * @param oldItem The old item to compare.
             * @param newItem The new item to compare.
             * @return True if the items have the same identifier, false otherwise.
             */
            override fun areItemsTheSame(oldItem: AgricInspector, newItem: AgricInspector): Boolean {
                return oldItem.id == newItem.id
            }

            /**
             * Called to check whether two items have the same data.
             *
             * @param oldItem The old item to compare.
             * @param newItem The new item to compare.
             * @return True if the items have the same data, false otherwise.
             */
            override fun areContentsTheSame(oldItem: AgricInspector, newItem: AgricInspector): Boolean {
                return oldItem == newItem
            }
        }
    }

}
