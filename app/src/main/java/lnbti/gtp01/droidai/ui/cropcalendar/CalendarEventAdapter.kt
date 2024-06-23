package lnbti.gtp01.droidai.ui.cropcalendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import lnbti.gtp01.droidai.BR
import lnbti.gtp01.droidai.databinding.CalendarEventItemBinding
import lnbti.gtp01.droidai.models.CalendarEventObject

class CalendarEventAdapter(
    private var eventList: List<CalendarEventObject>,
    private val onItemLongClickListener: OnItemLongClickListener
) : RecyclerView.Adapter<CalendarEventAdapter.ViewHolder>() {
    class ViewHolder(val binding: CalendarEventItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(calendarEventObject: CalendarEventObject) {
            binding.apply {
                setVariable(BR.item, calendarEventObject)
                executePendingBindings()
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CalendarEventItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val calendarEventObject = eventList[position]
        holder.apply {
            bind(calendarEventObject)
            binding.apply {
                root.setOnLongClickListener {
                    onItemLongClickListener.onLongClick(calendarEventObject)
                    true
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    // Update data method
    fun updateData(newEventList: List<CalendarEventObject>) {
        this.eventList = newEventList
        notifyDataSetChanged()
    }

    interface OnItemLongClickListener {
        fun onLongClick(item: CalendarEventObject)
    }
}
