package lt.akb.currency.converter

import androidx.recyclerview.widget.RecyclerView
import lt.akb.currency.database.Rate
import lt.akb.currency.databinding.RateViewItemBinding

class RateHolderView(private val binding: RateViewItemBinding, private val adapter: RatesAdapter) :
    RecyclerView.ViewHolder(binding.root), IRatesBind {
    init {
        binding.adapter = adapter
    }

    override fun bind(item: Rate) {
        binding.item = item
    }

    override fun bind(rate: Rate, payloads: MutableList<Any>) {
        if (payloads.isEmpty())
            bind(rate)
        else
            binding.valueTextView.text = adapter.calculateValue(rate)
    }
}