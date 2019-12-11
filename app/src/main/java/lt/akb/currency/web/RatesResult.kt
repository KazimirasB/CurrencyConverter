package lt.akb.currency.web

data class RatesResult(

    var base: String,
    var date: String,
    var rates: HashMap<String, Double>

)
