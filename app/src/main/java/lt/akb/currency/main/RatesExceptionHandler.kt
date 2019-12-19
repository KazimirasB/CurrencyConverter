package lt.akb.currency.main

import android.content.Context
import android.util.Log
import android.widget.Toast

class RatesExceptionHandler(private val context: Context?) :
    Thread.UncaughtExceptionHandler {
    override fun uncaughtException(
        thread: Thread,
        ex: Throwable
    ) {
        Log.e("RateError", ex.message)
        Toast.makeText(context, ex.message, Toast.LENGTH_LONG).show()
    }

}