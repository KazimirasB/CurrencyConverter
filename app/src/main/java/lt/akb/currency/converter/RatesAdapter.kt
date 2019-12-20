package lt.akb.currency.converter

import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import lt.akb.currency.database.Rate
import java.math.BigDecimal

class RatesAdapter(
    inflater: LayoutInflater,
    private val viewModel: RatesViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var currencyRates = emptyList<Rate>()
    private val factory = RateHolderFactory()

    init {
        factory.registerContainer(RateHolderEditContainer(this, inflater))
        factory.registerContainer(RateHolderViewContainer(this, inflater))
    }

    override fun getItemViewType(position: Int): Int {
        return factory.getItemViewType(position == 0)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return factory.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        factory.onBindViewHolder(holder, currencyRates[position])
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        factory.onBindViewHolder(holder, currencyRates[position], payloads)
    }

    override fun getItemCount(): Int {
        return currencyRates.size
    }

    fun setList(currencyRates: List<Rate>) {
        this.currencyRates = currencyRates.sortedWith(compareByDescending { it.orderKey })
        viewModel.setBaseRate(currencyRates[0])
        notifyDataSetChanged()
    }

    @Suppress("UNUSED_PARAMETER")
    fun setRateValue(s: CharSequence, start: Int, before: Int, count: Int) {
        if (s.isEmpty())
            viewModel.setBaseAmount(BigDecimal.ZERO)
        else if (s.toString().matches(Regex("^\\d*\\.?\\d*\$")))
            viewModel.setBaseAmount(BigDecimal(s.toString()))
        refreshRates(1)
    }

    fun setBaseRate(rate: Rate) {
        val oldPosition = currencyRates.indexOf(rate)
        rate.orderKey = viewModel.rate.orderKey + 1
        viewModel.setBaseRate(rate)
        currencyRates = currencyRates.sortedWith(compareByDescending { it.orderKey })
        notifyItemMoved(oldPosition, 0)
    }

    fun calculateValue(item: Rate): String {
        return viewModel.calculateValue(item)
    }

    fun refreshRates(positionStart: Int) {
        Handler().post {
            notifyItemRangeChanged(positionStart, currencyRates.size, 0)
        }
    }
}