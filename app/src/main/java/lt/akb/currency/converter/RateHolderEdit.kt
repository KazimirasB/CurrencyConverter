package lt.akb.currency.converter

import androidx.recyclerview.widget.RecyclerView
import lt.akb.currency.database.Rate
import lt.akb.currency.databinding.RateEditItemBinding

//Holder to edit currency item amount
class RateHolderEdit(private val binding: RateEditItemBinding, adapter: RatesAdapter) :
    RecyclerView.ViewHolder(binding.root), IRatesBind {
    init {
        binding.adapter = adapter
    }

    override fun bind(rate: Rate) {
        binding.item = rate
    }

    override fun bind(rate: Rate, payloads: MutableList<Any>) {
        binding.item = rate
    }
}