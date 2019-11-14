package me.jamilalrasyidis.mvvmbasic.ui.quotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import me.jamilalrasyidis.mvvmbasic.R
import me.jamilalrasyidis.mvvmbasic.data.Quote
import me.jamilalrasyidis.mvvmbasic.utils.InjectorUtils
import java.lang.StringBuilder

class QuotesMakerActivity : AppCompatActivity() {

    private val quoteText by lazy { findViewById<TextView>(R.id.quote_list) }
    private val editTextQuote by lazy { findViewById<EditText>(R.id.quote_text) }
    private val editTextAuthor by lazy { findViewById<EditText>(R.id.author) }
    private val btnAdd by lazy { findViewById<Button>(R.id.add_quote_btn) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quotes_maker)
        initializeUi()
    }

    @Suppress("DEPRECATION")
    private fun initializeUi() {
        val factory = InjectorUtils.provideQuotesViewModelFactory()
        val viewModel = ViewModelProviders.of(this, factory).get(QuotesViewModel::class.java)

        viewModel.getQuotes().observe(this, Observer {
            val stringBuilder = StringBuilder()
            it.forEach { quote ->
                stringBuilder.append("$quote\n\n")
            }
            quoteText.text = stringBuilder.toString()
        })

        btnAdd.setOnClickListener {
            val quote = Quote(editTextQuote.text.toString(), editTextAuthor.text.toString())
            viewModel.addQuote(quote)
            editTextQuote.setText("")
            editTextAuthor.setText("")
        }
    }
}
