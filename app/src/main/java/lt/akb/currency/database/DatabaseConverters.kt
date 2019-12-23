package lt.akb.currency.database

import androidx.room.TypeConverter
import java.math.BigDecimal

class DatabaseConverters {

    //Converts BigDecimal data to String for saving in database
    @TypeConverter
    fun bigDecimalToString(value: BigDecimal): String {
        return value.toString()
    }

    //Converts String value of BigData to BigData object
    @TypeConverter
    fun stringToBigDecimal(value: String): BigDecimal {
        return BigDecimal(value)
    }

}