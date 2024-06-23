package lnbti.gtp01.droidai.ui.airesponse

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import lnbti.gtp01.droidai.R
import lnbti.gtp01.droidai.databinding.LayoutHomeListBinding
import lnbti.gtp01.droidai.models.Inquiry
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

/**
 * RecyclerView adapter for displaying inquiries in a list.
 * This adapter handles displaying sorted lists of inquiries with specific statuses.
 */
class InquiryResponseDataAdapter :
    RecyclerView.Adapter<InquiryResponseDataAdapter.InquiryViewHolder>() {

    /**
     * ViewHolder for the inquiries displayed in the RecyclerView.
     * @param binding The view binding for the item layout.
     */
    inner class InquiryViewHolder(
        val binding: LayoutHomeListBinding,
    ) : RecyclerView.ViewHolder(binding.root)

    /**
     * Submit a sorted list of inquiries to the adapter.
     * @param inquiryData The list of inquiries to be displayed.
     */
    fun submitSortedList(inquiryData: List<Inquiry?>) {
        val filteredList = inquiryData.filter { it?.status == "Pending" }
        val sortedList = filteredList.sortedByDescending { inquiry ->
            inquiry?.createdDate
        }
        // Submit the sorted list to the adapter's data differ
        differ.submitList(sortedList)
    }

    /**
     * Filter and display a list of completed inquiries in descending order of creation date.
     * @param inquiryData The list of inquiries to be displayed.
     */
    fun filterResponseCompletedList(inquiryData: List<Inquiry?>) {
        val filterRespondedList = inquiryData.filter { it?.status == "Completed" }
        val sortedRespondedList = filterRespondedList.sortedByDescending { inquiry ->
            inquiry?.createdDate
        }
        // Submit the sorted list to the adapter's data differ
        differ.submitList(sortedRespondedList)
    }

    // List differ callback for calculating item differences
    private val differCallBack = object : DiffUtil.ItemCallback<Inquiry>() {
        override fun areItemsTheSame(oldItem: Inquiry, newItem: Inquiry): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Inquiry, newItem: Inquiry): Boolean {
            return oldItem == newItem
        }

    }

    // AsyncListDiffer instance for managing the list data
    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InquiryViewHolder {
        val itemView =
            LayoutHomeListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        return InquiryViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: InquiryViewHolder, position: Int) {
        val inquiryData = differ.currentList[position]

        holder.binding.apply {
            // Bind inquiry data to the views
            txtQuestion.text = inquiryData.problem
            txtDate.text = inquiryData.createdDate?.let { formatDate(it) }

            if (inquiryData.status == "Pending") {
                // Load an image using Glide
                Glide.with(holder.itemView).load(R.mipmap.pending)
                    .into(holder.binding.imageViewLayoutHomeList)
            } else {
                Glide.with(holder.itemView)
                    .load(R.mipmap.check)
                    .into(holder.binding.imageViewLayoutHomeList)
            }

            holder.itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(inquiryData)
                }
            }
        }
    }

    private var onItemClickListener: ((Inquiry) -> Unit)? = null

    /**
     * Set a click listener for items in the RecyclerView.
     * @param listener The function to be called when an item is clicked.
     */
    fun setOnItemClickListener(listener: (Inquiry) -> Unit) {
        onItemClickListener = listener
    }

    /**
     * Format the given date and time string.
     * @param dateAndTime The date and time string to be formatted.
     * @return The formatted date and time string.
     */
    fun formatDate(dateAndTime: String): String {
        //Time format
        val inputFormat = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()
        )
        val outPutFormat = SimpleDateFormat(
            "dd/MM/yyyy hh:mm a", Locale.getDefault()
        )

        // Set input time zone to UTC
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")

        try {
            val date = inputFormat.parse(dateAndTime)
            date?.let {
                outPutFormat.timeZone = TimeZone.getTimeZone("Asia/Tokyo")
                return outPutFormat.format(it)
            }
        } catch (e: Exception) {

            e.printStackTrace()
        }

        // Return the original date and time string if an exception occurs
        return dateAndTime
    }

}