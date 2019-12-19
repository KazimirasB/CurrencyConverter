package lt.akb.currency.converter

import androidx.recyclerview.widget.RecyclerView
import lt.akb.currency.database.Rate
import lt.akb.currency.databinding.RateEditItemBinding

class RateHolderEdit(private val binding: RateEditItemBinding, adapter: RatesAdapter) :
    RecyclerView.ViewHolder(binding.root), IRatesBind {
    init {
        binding.adapter = adapter
    }

    override fun bind(item: Rate) {
        binding.item = item
    }

    override fun bind(rate: Rate, payloads: MutableList<Any>) {
        bind(rate)
    }
}