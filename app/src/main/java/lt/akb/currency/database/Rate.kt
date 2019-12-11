package lt.akb.currency.database

import java.math.BigDecimal
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "currency_rate", indices = [Index("isBase")])
data class Rate(
    @PrimaryKey
    var currency: String, //Currency key
    var country: String?, //Country code for flag
    var name: String, //Currency extended name
    var value: BigDecimal, //Currency rate value to base currency
    var isBase: Boolean //Base currency indicator
)


