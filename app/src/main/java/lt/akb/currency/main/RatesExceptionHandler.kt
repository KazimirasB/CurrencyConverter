package lt.akb.currency.main

import android.util.Log

class RatesExceptionHandler :
    Thread.UncaughtExceptionHandler {
    override fun uncaughtException(
        thread: Thread,
        ex: Throwable
    ) {
        Log.e("RatesExceptionHandler", ex.message!!)
      }
}