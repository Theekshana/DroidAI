package lnbti.gtp01.droidai.ui.weatherinfo

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import lnbti.gtp01.droidai.databinding.WeatherCardBinding
import lnbti.gtp01.droidai.models.DailyForecastModel

/**
 * Adapter for displaying weather forecast data in a RecyclerView.
 */
class WeatherForecastAdapter : RecyclerView.Adapter<WeatherForecastAdapter.ForecastViewHolder>() {

    private var forecastList: List<DailyForecastModel> = emptyList()

    /**
     * Creates a ForecastViewHolder instance.
     *
     * @param parent The ViewGroup into which the new View will be added.
     * @param viewType The type of the new View.
     * @return A new ForecastViewHolder that holds a View of the given type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = WeatherCardBinding.inflate(inflater, parent, false)
        return ForecastViewHolder(binding)
    }

    /**
     * Binds the forecast data to the ViewHolder.
     *
     * @param holder The ViewHolder to bind the data to.
     * @param position The position of the forecast data in the list.
     */
    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.bind(forecastList[position])
    }

    /**
     * Returns the total number of forecast items in the list.
     */
    override fun getItemCount() = forecastList.size

    /**
     * ViewHolder class for displaying individual forecast items.
     *
     * @param binding The binding object associated with the ViewHolder.
     */
    class ForecastViewHolder(private val binding: WeatherCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        /**
         * Binds the forecast data to the ViewHolder.
         *
         * @param forecast The DailyForecastModel object containing the forecast data.
         */
        fun bind(forecast: DailyForecastModel) {
            binding.apply {
                binding.forecast = forecast
                binding.executePendingBindings()
            }
        }
    }

    /**
     * Updates the forecast data and refreshes the RecyclerView.
     *
     * @param newForecastList The new list of forecast data.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newForecastList: List<DailyForecastModel>) {
        forecastList = newForecastList
        notifyDataSetChanged()
    }
}
