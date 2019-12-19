package lt.akb.currency.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import lt.akb.currency.R
import lt.akb.currency.converter.RatesFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, RatesFragment())
                .commitNow()
        }
      //  Thread.setDefaultUncaughtExceptionHandler(RatesExceptionHandler(this))
    }

}
