package lt.akb.currency.converter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.converter_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lt.akb.currency.R
import lt.akb.currency.databinding.ConverterFragmentBinding
import kotlin.concurrent.fixedRateTimer

class RatesFragment : Fragment() {
    private lateinit var binding: ConverterFragmentBinding
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
                ratesAdapter.setList(rates)
                startTimer()
            } else observeRates()
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
        binding.fragment = this

        observeRates()
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
        Toast.makeText(context, R.string.error_message, Toast.LENGTH_LONG).show()
        t?.let { throw it }
    }

    @SuppressLint("CheckResult")
    fun observeRates() {
        viewModel.appRepository.apiClient.observeRates()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                progressBar.visibility = View.VISIBLE
                reloadImageButton.visibility = View.GONE
            }
            .doOnSuccess { progressBar.visibility = View.GONE }
            .doOnError {
                progressBar.visibility = View.GONE
                reloadImageButton.visibility = View.VISIBLE
            }
            .subscribe(viewModel::handleResponse, this::handelError)
    }
}
