package lt.akb.currency.converter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.converter_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lt.akb.currency.R
import kotlin.concurrent.fixedRateTimer

class RatesFragment : Fragment() {
    private var isStop: Boolean = false
    private val viewModel: RatesViewModel by lazy {
        ViewModelProviders.of(this).get(RatesViewModel::class.java)
    }

    private val ratesAdapter: RatesAdapter by lazy {
        RatesAdapter(LayoutInflater.from(context), viewModel)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.ratesLive.observe(this, Observer { rates ->
            if (rates.size > 1) {
                setRefresh(false)
                ratesAdapter.setList(rates)
                progress.visibility=View.GONE
                startTimer()
            } else {
                progress.visibility=View.VISIBLE
                viewModel.observeRates(this::handelError)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.converter_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ratesRecyclerView.apply {
            adapter = ratesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isStop = true
    }

    private fun startTimer() {
        isStop = false
        fixedRateTimer("timer", false, 1000L, 1000L) {
            if (isStop) cancel()
            lifecycleScope.launch { withContext(Dispatchers.Main) { updateRates() } }
        }
    }

    private fun updateRates() {
        viewModel.updateRates(ratesAdapter.currencyRates).observe(this, Observer { result ->
            result?.let {
                if (!ratesRecyclerView.isAnimating) ratesAdapter.refreshRates(1)
            }
        })
    }

    private fun handelError(t: Throwable?) {
        t?.let {
            Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            progress.visibility=View.GONE
            setRefresh(true)
        }
    }

    private fun setRefresh(isRefresh : Boolean) {
        when (isRefresh) {
            true -> {
                refreshImageView.visibility = View.VISIBLE
                refreshImageView.setOnClickListener {
                    progress.visibility=View.VISIBLE
                    viewModel.observeRates(this::handelError)
                }
            }
            false -> {
                refreshImageView.visibility = View.GONE
                refreshImageView.setOnClickListener(null)
            }
        }
    }
}
