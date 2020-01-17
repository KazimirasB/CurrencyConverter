package lt.akb.currency.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import lt.akb.currency.R
import lt.akb.currency.converter.RatesFragment
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

//    @Inject
//    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
     //  AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, RatesFragment())
                .commitNow()
        }

        //Use custom exception handler
        Thread.setDefaultUncaughtExceptionHandler(RatesExceptionHandler())

    }

  //  override fun androidInjector(): AndroidInjector<Any> = androidInjector
}
