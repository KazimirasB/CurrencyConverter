package lt.akb.currency.converter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import lt.akb.currency.R

class RateHolderEditContainer(
    private val adapter: RatesAdapter,
    private val inflater: LayoutInflater
) :
    IHolderContainer {
    override fun getViewTypeKey(): Any {
        return true
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return RateHolderEdit(
            DataBindingUtil.inflate(inflater, R.layout.rate_edit_item, parent, false), adapter
        )
    }
}