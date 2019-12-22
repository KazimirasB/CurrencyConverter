package lt.akb.currency

import lt.akb.currency.converter.RatesViewModel
import lt.akb.currency.converter.calculateAmount
import lt.akb.currency.converter.toDecimalString
import lt.akb.currency.database.DatabaseConverters
import lt.akb.currency.database.Rate
import org.junit.Test

import org.junit.Assert.*
import java.math.BigDecimal

class AppUnitTest {
    @Test
    fun databaseConverterBigDecimalToString() {
        val bigDecimal = 12345.6789.toBigDecimal()
        val bigDecimalToString = DatabaseConverters().bigDecimalToString(bigDecimal)
        assertEquals("12345.6789", bigDecimalToString)
    }

    @Test
    fun databaseConverterStringToBigDecimal() {
        val bigDecimalFromString = DatabaseConverters().stringToBigDecimal("12345.6789")
        assertEquals(BigDecimal("12345.6789"), bigDecimalFromString)
    }

    @Test
    fun currencyCalculations() {
        val rateBase =  Rate("EUR", "EU", "EU euro", BigDecimal.ONE, 1)
        val rate =  Rate("TST", "Test", "test", 5.0.toBigDecimal(), 1)
        val amount = 100.0.toBigDecimal()
        val expectation = 500.0.toBigDecimal().stripTrailingZeros()
        val calculateAmount = rate.calculateAmount(rateBase, amount).stripTrailingZeros()
        assertEquals(expectation, calculateAmount)
    }

    @Test
    fun toDecimalString() {
        assertEquals("100.01", 100.01000.toBigDecimal().toDecimalString())
    }
}
