package lt.akb.currency.main

/*
Custom handle used to catch error exceptions. Information about exception can be send to remote
server, to proceed analise of app problems.
 */

class RatesExceptionHandler :
    Thread.UncaughtExceptionHandler {
    override fun uncaughtException(
        thread: Thread,
        ex: Throwable
    ) {
        //Disabled log on production
        //Log.e("RatesExceptionHandler", ex.message!!)
      }
}