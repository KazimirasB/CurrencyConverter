package lt.akb.currency.converter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    companion object {
        fun newInstance() = RatesFragment()
    }


    private val viewModel: RatesViewModel by lazy {
        ViewModelProviders.of(this).get(RatesViewModel::class.java)
    }

    private val ratesAdapter: RatesAdapter by lazy {
        RatesAdapter(context!!, viewModel)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

         viewModel.ratesLive.observe(this, Observer { rates ->
            viewModel?.let {
                if (rates.isNotEmpty()) {
                    viewModel.rate = rates[0]
                    ratesAdapter.setList(rates)
                    startTimer()
                }
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

        viewModel.getRates()
    }

    private fun startTimer() {
        fixedRateTimer("timer", false, 1000L, 1000L) {
            lifecycleScope.launch{withContext(Dispatchers.Main){updateRates()}}
        }
    }

    private fun updateRates(){
        viewModel.updateRates(ratesAdapter.currencyRates).observe(this, Observer { result ->
            result?.let {
                ratesAdapter.updateRates(1)
            }
        })
    }


}
