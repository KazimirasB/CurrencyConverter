package lt.akb.currency.converter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.converter_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lt.akb.currency.R
import lt.akb.currency.custom.CustomFragment
import lt.akb.currency.databinding.ConverterFragmentBinding
import lt.akb.currency.main.RatesRepository
import lt.akb.currency.main.RatesSettings
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer

class RatesFragment : Fragment() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: ConverterFragmentBinding
    private var isStop: Boolean = false

    private val viewModel: RatesViewModel by lazy {
        getCustomViewModel()

    }

   private fun getCustomViewModel():RatesViewModel {
       val vm = ViewModelProviders.of(this).get(RatesViewModel::class.java)
       vm.initRepository(RatesRepository(activity!!.application))
       return vm
   }

    private val ratesAdapter: RatesAdapter by lazy {
        RatesAdapter(LayoutInflater.from(context), viewModel)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

            //TODO
        RatesSettings.init(context!!)

        //Observe currency rates from database, then starts periodic updates
        viewModel.ratesLive.observe(this, Observer { rates ->
          //TODO return object with error handeling in fragment
            if (rates.size > 1) {
                ratesAdapter.setList(rates)
                startTimer()
            } else viewModel.observeRates()
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.converter_fragment,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ratesRecyclerView.apply {
            adapter = ratesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
        binding.viewModel = viewModel

        viewModel.observeRates()
    }

    override fun onDestroy() {
        super.onDestroy()
        isStop = true
     }

    //Run periodic rates update every 1 second
    private fun startTimer() {
        isStop = false
        fixedRateTimer("timer", false, 1000L, 1000L) {
            if (isStop) cancel()
            lifecycleScope.launch { withContext(Dispatchers.Main) { updateRates() } }
        }
    }

    //Updated currency rates
    private fun updateRates() {
        viewModel.updateRates(ratesAdapter.currencyRates).observe(this, Observer { result ->
            result?.let {
                if (!ratesRecyclerView.isAnimating) ratesAdapter.refreshRates(1)
            }
        })
    }



}
