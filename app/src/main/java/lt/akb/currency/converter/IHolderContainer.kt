package lt.akb.currency.converter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import lt.akb.currency.database.Rate

interface IHolderContainer {
    fun getViewTypeKey():Any
    fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
}