package lt.akb.currency.converter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.converter_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lt.akb.currency.R
import lt.akb.currency.dagger.viewModel.ViewModelFactory
import lt.akb.currency.databinding.ConverterFragmentBinding
import lt.akb.currency.main.bones.RateResource
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer

class RatesFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var binding: ConverterFragmentBinding
    private var isStop: Boolean = false
  //  private val actionMap = hashMapOf<RatesAction, KFunction<Any>>()


    private val viewModel: RatesViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(RatesViewModel::class.java)
    }

    private val ratesAdapter: RatesAdapter by lazy {
        RatesAdapter(LayoutInflater.from(context), viewModel)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }
//
//    private fun addRates() {
//        if (viewModel.rates.size > 1) {
//            ratesAdapter.setList(viewModel.rates)
//            startTimer()
//        } else viewModel.observeRates()
//    }

//    private fun refreshRates() {
//        if (!ratesRecyclerView.isAnimating) ratesAdapter.refreshRates(1)
//    }
//
//    private fun crateActions() {
//        actionMap[RatesAction.LOAD] = this::addRates
//        actionMap[RatesAction.REFRESH] = this::refreshRates
//    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)

//        crateActions()
        //Observe currency rates from database, then starts periodic updates
//        viewModel.actions.observe(this, Observer { action ->
//            actionMap[action]?.let { it.call() }
//        })

//        viewModel.ratesLive.observe(this, Observer { rates ->
//            if (rates.size > 1) {
//                ratesAdapter.setList(rates)
//                startTimer()
//            } else viewModel.observeRates()
//        })

//        ratesLiveRateReadable
//        viewModel.ratesLiveSource.observe(this, Observer { source ->
//                if (source.rates!=null && source.rates.size > 1) {
//                    ratesAdapter.setList(source.rates)
//                    startTimer()
//                } else
//                    viewModel.observeRates()
//        })


//    }



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
        binding.fragment = this

        refreshRatesLive(false)

   //     viewModel.observeRates()
    }

    override fun onDestroy() {
        super.onDestroy()
        isStop = true
    }

    //TODO visibility change in xml bind

    fun refreshRatesLive(isRefresh: Boolean) {
        viewModel.ratesLiveRate(isRefresh).observe(viewLifecycleOwner, Observer { source ->
            when (source) {
                is RateResource.Progress -> {
                    progressBar.visibility = View.VISIBLE
                }
                is RateResource.Load -> {
                    ratesAdapter.setList(source.data)
                    progressBar.visibility = View.GONE
                    reloadImageButton.visibility = View.GONE
                    startTimer()
                }
                is RateResource.Refresh -> {
                    progressBar.visibility = View.GONE
                    reloadImageButton.visibility = View.GONE
                    ratesAdapter.updateRates(source.map)
                    if (!ratesRecyclerView.isAnimating)
                        ratesAdapter.refreshRates(1)
                }
                is RateResource.Error -> {
                    progressBar.visibility = View.GONE
                    reloadImageButton.visibility = View.VISIBLE
                    Toast.makeText(context, R.string.error_message, Toast.LENGTH_LONG).show()
                    source.message?.let { throw Throwable(source.message) }
                }
            }
        })
    }

    //Run periodic rates update every 1 second
    private fun startTimer() {
        isStop = false
        fixedRateTimer("timer", false, 1000L, 1000L) {
            if (isStop) cancel()
            lifecycleScope.launch { withContext(Dispatchers.Main) {  refreshRatesLive(true)
            //    updateRates()
            } }

        }
    }

//    //Updated currency rates
//    private fun updateRates() {
//        viewModel.updateRates(ratesAdapter.currencyRates).observe(this, Observer { result ->
//            result?.let {
//                if (!ratesRecyclerView.isAnimating) ratesAdapter.refreshRates(1)
//            }
//        })
//    }


}
