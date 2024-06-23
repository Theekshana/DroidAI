package lnbti.gtp01.droidai.ui.detailswithfilter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rajat.pdfviewer.PdfRendererView
import lnbti.gtp01.droidai.R
import lnbti.gtp01.droidai.constants.PreferenceKeys.HEADER_ICON
import lnbti.gtp01.droidai.constants.StringConstants
import lnbti.gtp01.droidai.databinding.FragmentContentDetailsWithFilterBinding
import lnbti.gtp01.droidai.models.CategoryModel
import lnbti.gtp01.droidai.ui.main.MainActivityViewModel
import lnbti.gtp01.droidai.utils.SharedPreferencesManager

/**
 * A fragment that displays Content Details with filters and image buttons.
 */
class ContentDetailsWithFilterFragment : Fragment() {
    private val args: ContentDetailsWithFilterFragmentArgs by navArgs()
    lateinit var binding: FragmentContentDetailsWithFilterBinding
    private lateinit var sharedViewModel: MainActivityViewModel
    var selectedItem: CategoryModel? = null
    lateinit var selectedButton: CategoryModel
    lateinit var selectedDropDownItem: CategoryModel
    lateinit var selectedImageButton: CategoryModel
    var plantNutrientsList: List<CategoryModel>? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        selectedItem = args.item
        //This Shared view model is using to update Main Activity layout changes from this fragment
        ViewModelProvider(requireActivity())[MainActivityViewModel::class.java].apply {
            sharedViewModel = this
            setFragment(StringConstants.CONTENT_DETAILS_WITH_FILTER_FRAGMENT)
        }

        FragmentContentDetailsWithFilterBinding.inflate(inflater, container, false).apply {
            binding = this
            lifecycleOwner = this@ContentDetailsWithFilterFragment
            val icon = SharedPreferencesManager.getPreference(HEADER_ICON)
            Glide.with(requireActivity()).load(icon)
                .into(headerIcon)
            title.text = selectedItem?.title
            pdfViewActivityPdf.isVisible = true
            plantNutrientsList = selectedItem?.items
            setDropDownValues()
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
    }

    private fun setDropDownValues() {

        plantNutrientsList?.let {
            // Create a custom adapter for your Spinner
            val adapter = CustomSpinnerAdapter(
                requireActivity(),
                android.R.layout.simple_spinner_item,
                plantNutrientsList!!
            )

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerActivityPlantNutrients.adapter = adapter
            binding.spinnerActivityPlantNutrients.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long,
                    ) {
                        plantNutrientsList?.let {
                            selectedDropDownItem = plantNutrientsList!!.get(position)
                            setButtonsValues()
                        }

                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        // Handle case where nothing is selected
                    }
                }
        }
    }

    private fun setButtonsValues() {

        if (selectedDropDownItem.items != null) {
            selectedButton = selectedDropDownItem.items!![0]
            when {
                selectedButton.title != null -> {
                    setImageButtonValues()
                    binding.buttonList.isVisible = true
                    val adapter =
                        ButtonAdapter(selectedDropDownItem.items!!, object : OnItemClickListener {
                            override fun onItemClick(item: CategoryModel) {
                                selectedButton = item
                                setImageButtonValues()
                            }
                        })
                    binding.buttonList.apply {
                        layoutManager = GridLayoutManager(requireActivity(), 4)
                        this.adapter = adapter
                    }
                }

                else -> {
                    binding.buttonList.isVisible = false
                    setImageButtonValues()
                }
            }
        } else {
            viewDetails(selectedDropDownItem)
        }
    }

    private fun setImageButtonValues() {

        selectedButton.items?.let {
            selectedImageButton = selectedButton.items!![0]
            viewDetails(selectedImageButton)
            val adapter = ImageButtonAdapter(it, object : OnItemClickListener {
                override fun onItemClick(item: CategoryModel) {
                    selectedImageButton = item
                    viewDetails(selectedImageButton)
                }
            })

            binding.imageButtonList.apply {
                this.adapter = adapter
            }
        }
    }

    private fun viewDetails(item: CategoryModel) {

        item.link?.let {
            sharedViewModel.setProgressDialogVisible(true)
            loadPDF(it)
        } ?: run {
            sharedViewModel.showErrorDialog(getString(R.string.failed_to_load_details))
        }
    }

    private fun loadPDF(pdfUrl: String) {
        val pdfView = binding.pdfViewActivityPdf
        pdfView.statusListener = object : PdfRendererView.StatusCallBack {
            override fun onPdfLoadSuccess(absolutePath: String) {
                super.onPdfLoadSuccess(absolutePath)
                sharedViewModel.setProgressDialogVisible(false)
            }

            override fun onError(error: Throwable) {
                super.onError(error)
                sharedViewModel.setProgressDialogVisible(false)
                sharedViewModel.showErrorDialog(getString(R.string.failed_to_load_details))
            }

            override fun onPdfLoadProgress(
                progress: Int,
                downloadedBytes: Long,
                totalBytes: Long?,
            ) {
                sharedViewModel.setProgressAmount(progress)
            }
        }
        pdfView.initWithUrl(
            url = pdfUrl,
            lifecycleCoroutineScope = lifecycleScope,
            lifecycle = lifecycle
        )
    }

}

class ButtonAdapter(
    private val items: List<CategoryModel>,
    private val listener: OnItemClickListener,
) :
    RecyclerView.Adapter<ButtonViewHolder>() {
    private var selectedItemPosition: Int = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ButtonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.button_header_view_layout, parent, false)
        return ButtonViewHolder(view)
    }

    override fun onBindViewHolder(holder: ButtonViewHolder, position: Int) {
        holder.bind(items[position])

        // Update the background based on the selected item
        if (position == selectedItemPosition) {
            holder.itemView.setBackgroundResource(R.drawable.nutrient_select_button_bg)
        } else {
            holder.itemView.setBackgroundResource(R.drawable.nutrient_default_button_bg)
        }

        holder.itemView.setOnClickListener {
            // Update the selected item position
            val previousSelectedItemPosition = selectedItemPosition
            selectedItemPosition = holder.adapterPosition

            // Notify item changes for proper background update
            notifyItemChanged(previousSelectedItemPosition)
            notifyItemChanged(selectedItemPosition)

            listener.onItemClick(items[position])
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class ButtonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val textView: TextView = itemView.findViewById(R.id.textView)

    fun bind(item: CategoryModel) {
        textView.text = item.title
    }
}

class ImageButtonAdapter(
    private val items: List<CategoryModel>,
    private val listener: OnItemClickListener,
) :
    RecyclerView.Adapter<ImageButtonViewHolder>() {

    private var selectedItemPosition: Int = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageButtonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.image_button_header_view_layout, parent, false)
        return ImageButtonViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageButtonViewHolder, position: Int) {
        holder.bind(items[position])

        // Update the background based on the selected item
        if (position == selectedItemPosition) {
            holder.mainLayout.setBackgroundResource(R.mipmap.selected_img)
        } else {
            holder.mainLayout.setBackgroundResource(R.mipmap.default_img_bg)
        }

        holder.itemView.setOnClickListener {
            // Update the selected item position
            val previousSelectedItemPosition = selectedItemPosition
            selectedItemPosition = holder.adapterPosition

            // Notify item changes for proper background update
            notifyItemChanged(previousSelectedItemPosition)
            notifyItemChanged(selectedItemPosition)

            listener.onItemClick(items[position])
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class ImageButtonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val imageView: ImageView = itemView.findViewById(R.id.imageView)
    val mainLayout: ConstraintLayout = itemView.findViewById(R.id.mainLayout)

    fun bind(item: CategoryModel) {
        Glide.with(itemView.context).load(item.image).into(imageView)
    }
}

class CustomSpinnerAdapter(
    context: Context,
    resource: Int,
    objects: List<CategoryModel>,
) : ArrayAdapter<CategoryModel>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(
            android.R.layout.simple_spinner_item,
            parent,
            false
        )
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = getItem(position)?.title
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(
            android.R.layout.simple_spinner_dropdown_item,
            parent,
            false
        )
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = getItem(position)?.title
        return view
    }
}

interface OnItemClickListener {
    fun onItemClick(item: CategoryModel)
}