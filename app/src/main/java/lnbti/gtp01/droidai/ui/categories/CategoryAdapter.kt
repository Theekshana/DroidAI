package lnbti.gtp01.droidai.ui.categories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import lnbti.gtp01.droidai.R
import lnbti.gtp01.droidai.models.CategoryModel

class CategoryAdapter(
    private val categoryModels: List<CategoryModel>, private val onClick: (CategoryModel) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View, val onClick: (CategoryModel) -> Unit) : RecyclerView.ViewHolder(view) {
        private val imageView: ImageView
        private val textView: TextView
        private var currentItem: CategoryModel? = null

        init {
            // Define click listener for the ViewHolder's View
            imageView = view.findViewById(R.id.image_view_layout_category_list)
            textView = view.findViewById(R.id.text_view_layout_category_list)

            itemView.setOnClickListener {
                currentItem?.let {
                    onClick(it)
                }
            }
        }

        fun bind(homeModel: CategoryModel) {
            currentItem = homeModel
            Glide.with(itemView.context).load(homeModel.image).into(imageView)
            textView.text = homeModel.title
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.layout_category_list, viewGroup, false)

        return ViewHolder(view, onClick)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val homeModel = categoryModels[position]
        viewHolder.bind(homeModel)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = categoryModels.size

}

