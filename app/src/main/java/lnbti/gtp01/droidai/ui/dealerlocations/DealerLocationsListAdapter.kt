package lnbti.gtp01.droidai.ui.dealerlocations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import lnbti.gtp01.droidai.BR
import lnbti.gtp01.droidai.databinding.LayoutDealerListBinding
import lnbti.gtp01.droidai.models.Dealer
import javax.inject.Inject

class DealerLocationsListAdapter @Inject constructor(
    private val itemClickListener: OnItemClickListener,
) :
    RecyclerView.Adapter<DealerLocationsListAdapter.OurDealerListViewHolder>() {

    private var items: List<Dealer> = emptyList()
    private var filteredItems: LiveData<List<Dealer>> = MutableLiveData()

    /**
     * Interface definition for a callback to be invoked when an item in the list is clicked.
     */
    interface OnItemClickListener {
        fun onItemClick(item: Dealer)
    }

    inner class OurDealerListViewHolder(val binding: LayoutDealerListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(obj: Dealer) {

            binding.apply {
                setVariable(BR.item, obj)
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OurDealerListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutDealerListBinding.inflate(inflater, parent, false)
        return OurDealerListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OurDealerListViewHolder, position: Int) {
        val repoObject = items[position]

        holder.apply {
            bind(repoObject)
            binding.apply {
                root.setOnClickListener {
                    itemClickListener.onItemClick(repoObject)
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun setItems(newItems: List<Dealer>) {
        items = newItems
        notifyDataSetChanged()
    }

    fun setFilteredItems(newFilteredItems: LiveData<List<Dealer>>) {
        filteredItems = newFilteredItems
        notifyDataSetChanged()
    }
}
