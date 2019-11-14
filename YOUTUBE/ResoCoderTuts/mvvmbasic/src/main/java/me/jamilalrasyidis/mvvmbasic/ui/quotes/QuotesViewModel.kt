package me.jamilalrasyidis.mvvmbasic.ui.quotes

import androidx.lifecycle.ViewModel
import me.jamilalrasyidis.mvvmbasic.data.QuoteRepository
import me.jamilalrasyidis.mvvmbasic.data.Quote

class QuotesViewModel(private val quotesRepository: QuoteRepository) : ViewModel() {

    fun getQuotes() = quotesRepository.getQuotes()

    fun addQuote(quote: Quote) = quotesRepository.addQuote(quote)
}