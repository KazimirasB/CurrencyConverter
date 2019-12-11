package lt.akb.currency.converter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import lt.akb.currency.R
import lt.akb.currency.database.Rate
import lt.akb.currency.databinding.ConverterListItemBinding

@BindingAdapter("setWebImage")
fun setWebImage(view: ImageView, countryCode: String?) {
    countryCode?.let {
        Picasso.get().load("https://www.countryflags.io/${countryCode.toLowerCase()}/flat/64.png").fit().centerCrop().into(view)}
}

class RatesAdapter(
    private val context: Context,
    private val viewModel: RatesViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    var currencyRates = emptyList<Rate>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateHolder {
        val binding: ConverterListItemBinding =
            DataBindingUtil.inflate(inflater, R.layout.converter_list_item, parent, false);
        return RateHolder(binding)
    }

    override fun getItemCount(): Int {
        return currencyRates.size
    }

    fun setList(currencyRates: List<Rate>) {
        this.currencyRates = currencyRates
        // currencyRates.forEach { notifyItemChanged(currencyRates.indexOf(it)) }
        notifyItemRangeChanged(0, currencyRates.size)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        (holder as RateHolder).bind(currencyRates[position])
    }

    inner class RateHolder(private val binding: ConverterListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Rate) {

            if (binding.currencyTextView.text.isEmpty()) {
                binding.item = item
                binding.valueEditText.setText(viewModel.calculateValue(item))
                binding.clickView.setOnClickListener {
                    viewModel.setSelectedRate(item)
                }
            } else if(!item.isBase){
                val value = viewModel.calculateValue(item)
                if(value!=binding.valueEditText.text.toString())
                    binding.valueEditText.setText(value)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }


}