package com.example.RibsBadoo_MVICore.ribs.exchange_rate.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.badoo.binder.Binder
import com.elyeproj.loaderviewlibrary.LoaderTextView
import com.example.RibsBadoo_MVICore.R
import com.example.RibsBadoo_MVICore.common.createDestroyLifecycle
import com.example.RibsBadoo_MVICore.common.getEnum
import com.example.RibsBadoo_MVICore.common.getExchangeRateToIne
import com.example.RibsBadoo_MVICore.common.putEnum
import com.example.RibsBadoo_MVICore.common.room.models.Wallet
import com.example.RibsBadoo_MVICore.feature.CalculatedEnteredValue
import com.example.RibsBadoo_MVICore.feature.CalculatedExchangeRate
import com.example.RibsBadoo_MVICore.ribs.exchange_rate.event.ExchangeRateEvent
import com.example.RibsBadoo_MVICore.ribs.exchange_rate.event.IExchangeRateEvent
import com.example.RibsBadoo_MVICore.ribs.exchange_rate.event.IExchangeRateEvent.Companion.exchangeRateEventSource
import io.reactivex.functions.Consumer

private const val CURRENCY = "CurrencyFragment.CURRENCY"
private const val FRAGMENT_TYPE = "CurrencyFragment.FRAGMENT_TYPE"

class CurrencyFragment : Fragment(), IExchangeRateEvent {

    companion object {
        fun newInstance(
            currency: String,
            fragmentCurrencyType: FragmentCurrencyType,
        ): CurrencyFragment {
            val currencyFragment = CurrencyFragment()
            val bundle = Bundle(1)
            bundle.putString(CURRENCY, currency)
            bundle.putEnum(FRAGMENT_TYPE, fragmentCurrencyType)
            currencyFragment.arguments = bundle
            return currencyFragment
        }
    }

    private lateinit var txtCurrency: LoaderTextView
    private lateinit var txtBalance: LoaderTextView
    private lateinit var etxtMoney: EditText
    private lateinit var txtRate: LoaderTextView

    private var isEditTextFocusable: Boolean = false
    private var currency: String? = null
    private var fragmentCurrencyType: FragmentCurrencyType? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View? = inflater.inflate(R.layout.fragment_currency, container, false);
        getArgumentsFromBundle()
        setupView(view!!)
        setupBinder()
        return view
    }

    private fun getArgumentsFromBundle() {
        currency = arguments!!.getString(CURRENCY)
        fragmentCurrencyType = arguments!!.getEnum(FRAGMENT_TYPE, FragmentCurrencyType.SALE)
    }

    private fun setupView(view: View) {
        txtCurrency = view.findViewById(R.id.txtCurrency)
        txtBalance = view.findViewById(R.id.txtBalance)
        txtRate = view.findViewById(R.id.txtRate)
        etxtMoney = view.findViewById(R.id.etxtMoney)
        txtCurrency.text = currency
        setupListeners()
    }

    private fun setupListeners() {
        etxtMoney.setOnFocusChangeListener { _, b -> isEditTextFocusable = b }
        etxtMoney.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (isEditTextFocusable) {
                    if (s.toString().isNotEmpty()) {
                        sendEnteredValue(s.toString())
                    } else {
                        sendEvent(ExchangeRateEvent.ClearEditText)
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun sendEnteredValue(value: String) {
        sendEvent(
            ExchangeRateEvent.SetEnteredAmount(
                EnteredAmount(value, fragmentCurrencyType!!)
            )
        )
    }

    private fun setupBinder() {
        Binder(lifecycle.createDestroyLifecycle()).apply {
            bind(exchangeRateEventSource to exchangeRateContainerOutputConsumer)
        }
    }

    private val exchangeRateContainerOutputConsumer: Consumer<ExchangeRateEvent> =
        Consumer {
            when (it) {
                is ExchangeRateEvent.WalletSet -> {
                    it.walletList?.let { walletList -> showWallet(walletList) }
                }
                is ExchangeRateEvent.ExchangeRateToOne -> {
                    it.exchangeRateToOne?.let { calculatedExchangeRate ->
                        showRateToOne(calculatedExchangeRate)
                    }
                }
                is ExchangeRateEvent.CalculatedEnteredMoney -> {
                    it.calculatedEnteredValue?.let { calculatedEnteredValue ->
                        showCalculatedEnteredValueForSale(calculatedEnteredValue)
                    }
                }
                is ExchangeRateEvent.ClearEditText -> clearEditText()
            }
        }

    private fun showWallet(walletList: List<Wallet>) {
        txtBalance.text = walletList.first { it.currency == currency }.balance.toString()
    }

    private fun showRateToOne(calculatedExchangeRate: CalculatedExchangeRate) {
        txtRate.text = getExchangeRateToIne(fragmentCurrencyType!!, calculatedExchangeRate)
    }

    private fun showCalculatedEnteredValueForSale(calculatedEnteredValue: CalculatedEnteredValue) {
        when (fragmentCurrencyType) {
            FragmentCurrencyType.SALE -> showForSale(calculatedEnteredValue)
            FragmentCurrencyType.PURCHASE -> showForPurchase(calculatedEnteredValue)
        }
    }

    private fun showForSale(calculatedEnteredValue: CalculatedEnteredValue) {
        if (!isEditTextFocusable && currency == calculatedEnteredValue.calculatedExchangeRate.currencySale) {
            etxtMoney.setText(calculatedEnteredValue.calculatedExchangeRate.ratePurchase.toString())
            isEnteringValue(true)
        }
    }

    private fun showForPurchase(calculatedEnteredValue: CalculatedEnteredValue) {
        if (!isEditTextFocusable && currency == calculatedEnteredValue.calculatedExchangeRate.currencyPurchase) {
            etxtMoney.setText(calculatedEnteredValue.calculatedExchangeRate.rateSale.toString())
            isEnteringValue(true)
        }
    }

    private fun isEnteringValue(isEntering: Boolean) {
        etxtMoney.isEnabled = !isEntering
        sendEvent(ExchangeRateEvent.SetIsViewPagersSwipeable(!isEntering))
    }

    private fun clearEditText() {
        if (!isEditTextFocusable) {
            etxtMoney.setText("")
            isEnteringValue(false)
        }
    }
}