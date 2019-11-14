package me.jamilalrasyidis.mvvmbasic.utils

import me.jamilalrasyidis.mvvmbasic.data.FakeDatabase
import me.jamilalrasyidis.mvvmbasic.data.QuoteRepository
import me.jamilalrasyidis.mvvmbasic.ui.quotes.QuotesViewModelFactory

object InjectorUtils {

    fun provideQuotesViewModelFactory(): QuotesViewModelFactory {
        val quotesRepository = QuoteRepository.getInstance(FakeDatabase.getInstance().quoteDao)
        return QuotesViewModelFactory(quotesRepository)
    }

}