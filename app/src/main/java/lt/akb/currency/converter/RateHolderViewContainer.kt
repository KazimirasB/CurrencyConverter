package lt.akb.currency.converter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import lt.akb.currency.R

//Inflates Holder by type view rate item
class RateHolderViewContainer(
    private val adapter: RatesAdapter,
    private val inflater: LayoutInflater
) :
    IHolderContainer {
    override fun getViewTypeKey(): Any {
        return false
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return RateHolderView(
            DataBindingUtil.inflate(inflater, R.layout.rate_view_item, parent, false), adapter
        )
    }
}