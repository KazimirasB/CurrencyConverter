package lt.akb.currency.converter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import lt.akb.currency.database.Rate

class RateHolderFactory {
    private val containers = arrayListOf<IHolderContainer>()
    private val viewTypeKeys = hashMapOf<Any, Int>()

    fun registerContainer(container: IHolderContainer) {
        containers.add(container)
        viewTypeKeys[container.getViewTypeKey()] = containers.indexOf(container)
    }

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return containers[viewType].onCreateViewHolder(parent)
    }

    fun getItemViewType(typeKey: Any): Int {
        return if (viewTypeKeys.containsKey(typeKey)) viewTypeKeys[typeKey]!! else 0
    }

    fun onBindViewHolder(holder: RecyclerView.ViewHolder, rate: Rate) {
        (holder as IRatesBind).bind(rate)
    }

    fun onBindViewHolder(holder: RecyclerView.ViewHolder, rate: Rate, payloads: MutableList<Any>) {
        (holder as IRatesBind).bind(rate, payloads)
    }
}