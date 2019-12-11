package lt.akb.currency.database

import androidx.room.TypeConverter
import java.math.BigDecimal

class DatabaseConverters {

    @TypeConverter
    fun bigDecimalToString(value: BigDecimal): String {
        return value.toString()
    }

    @TypeConverter
    fun stringToBigDecimal(value: String): BigDecimal {
        return BigDecimal(value)
    }

}