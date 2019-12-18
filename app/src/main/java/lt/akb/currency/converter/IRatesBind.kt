package lt.akb.currency.converter

import lt.akb.currency.database.Rate

interface IRatesBind {
    fun bind(rate: Rate)
    fun bind(rate: Rate, payloads: MutableList<Any>)
}