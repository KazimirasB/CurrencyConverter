package lt.akb.currency.converter

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.converter_fragment.*
import lt.akb.currency.R

class RatesFragment : Fragment() {

    companion object {
        fun newInstance() = RatesFragment()
    }


    private val viewModel: RatesViewModel by lazy {
        ViewModelProviders.of(this).get(RatesViewModel::class.java)
    }

    private val ratesAdapter: RatesAdapter  by lazy {
        RatesAdapter(context!!, viewModel)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        viewModel.ratesLive.observe(this, Observer { rates ->
            viewModel?.let {
                if(!viewModel.isStop)
                    ratesAdapter.setList(rates)
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


}
