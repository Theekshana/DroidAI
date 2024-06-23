package lnbti.gtp01.droidai.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import lnbti.gtp01.droidai.R
import lnbti.gtp01.droidai.models.HomeCategory

class HomeAdapter(
    private val homeModelList: List<HomeCategory>, private val onClick: (HomeCategory) -> Unit
) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View, val onClick: (HomeCategory) -> Unit) : RecyclerView.ViewHolder(view) {
        private val imageView: ImageView
        //private val textView: TextView
        private var currentItem: HomeCategory? = null

        init {
            // Define click listener for the ViewHolder's View
            imageView = view.findViewById(R.id.image_view_layout_home_list)
            //textView = view.findViewById(R.id.text_view_layout_home_list)

            itemView.setOnClickListener {
                currentItem?.let {
                    onClick(it)
                }
            }
        }

        fun bind(homeModel: HomeCategory) {
            currentItem = homeModel
            imageView.setImageResource(homeModel.image)
            //textView.text = homeModel.title
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.layout_home_list, viewGroup, false)


        return ViewHolder(view, onClick)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val homeModel = homeModelList[position]
        viewHolder.bind(homeModel)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = homeModelList.size

}

