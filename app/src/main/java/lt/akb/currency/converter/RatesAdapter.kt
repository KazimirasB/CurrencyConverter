package lt.akb.currency.converter

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Picasso.LoadedFrom
import com.squareup.picasso.Target
import lt.akb.currency.R
import lt.akb.currency.database.Rate
import java.math.BigDecimal

@BindingAdapter("setWebImage")
fun setWebImage(view: ImageView, countryCode: String?) {
    countryCode?.let {
        Picasso.get().load("https://www.countryflags.io/${countryCode.toLowerCase()}/flat/64.png")
            .error(R.drawable.ic_error_icon)
            .into(object : Target {

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                }

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                    view.setImageDrawable(errorDrawable)
                }

                override fun onBitmapLoaded(bitmap: Bitmap?, from: LoadedFrom?) {

                    bitmap?.let {
                        val squareBitmap =
                            Bitmap.createBitmap(bitmap, 12, 12, 40, 40)
                        val imageDrawable: RoundedBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(view.resources, squareBitmap)
                        imageDrawable.isCircular = true
                        view.setImageDrawable(imageDrawable)
                    }
                }
            })
    }
}

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
            viewModel.amount = BigDecimal.ZERO
        else if (s.toString().matches(Regex("[0-9]+")))
            viewModel.amount = BigDecimal(s.toString())
//TODO
        updateRates(1)
    }

    fun setBaseRate(rate: Rate) {
        val oldPosition = currencyRates.indexOf(rate)
        rate.orderKey = viewModel.rate.orderKey + 1
        viewModel.setBaseRate(rate)
        currencyRates = currencyRates.sortedWith(compareByDescending { it.orderKey })
        notifyItemMoved(oldPosition, 0)
        updateRates(0)
    }

    fun calculateValue(item: Rate): String {
        return viewModel.calculateValue(item)
    }

    fun updateRates(positionStart: Int) {
        Handler().post { notifyItemRangeChanged(positionStart, currencyRates.size, 0) }
    }

}



