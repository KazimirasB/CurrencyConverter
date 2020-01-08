package lt.akb.currency.custom
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

open class CustomFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getCustomViewModel().liveAction.observe(this, Observer { action ->
         proceedActon(action)
        })
    }

    open fun proceedActon(action: ILiveAction) {
        action.proceed(activity)
    }

    open fun getCustomViewModel() =
        ViewModelProviders.of(this).get(CustomViewModel::class.java)

}