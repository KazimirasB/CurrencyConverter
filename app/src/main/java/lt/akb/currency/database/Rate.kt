package lt.akb.currency.database

import java.math.BigDecimal
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import lt.akb.currency.converter.toDecimalString

@Entity(tableName = "currency_rate", indices = [Index("orderKey")])
data class Rate(
    @PrimaryKey
    var currency: String, //Currency key
    var country: String?, //Country code for flag
    var name: String, //Currency extended name
    var currencyRate: BigDecimal, //Currency rate value to base currency
    var orderKey: Int, //Order key indicator
    var value: BigDecimal = BigDecimal.ONE //Order key indicator
){
    fun getValueString(): String {
        return value.toDecimalString()
    }
}


