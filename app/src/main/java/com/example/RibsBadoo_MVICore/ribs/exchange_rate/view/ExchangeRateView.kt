package com.example.RibsBadoo_MVICore.ribs.exchange_rate.view

import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.viewpager.widget.ViewPager
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.example.RibsBadoo_MVICore.R
import com.example.RibsBadoo_MVICore.common.WrapContentViewPager
import com.example.RibsBadoo_MVICore.common.room.models.Wallet
import com.example.RibsBadoo_MVICore.feature.CalculatedEnteredValue
import com.example.RibsBadoo_MVICore.feature.CalculatedExchangeRate
import com.example.RibsBadoo_MVICore.ribs.RootActivity
import com.example.RibsBadoo_MVICore.ribs.exchange_rate.event.ExchangeRateEvent
import com.example.RibsBadoo_MVICore.ribs.exchange_rate.event.IExchangeRateEvent
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface ExchangeRateView : RibView,
    ObservableSource<ExchangeRateView.Event>,
    Consumer<ExchangeRateView.ViewModel> {

    sealed class Event {
        data class OnViewPagerSwiped(val viewPagersState: Pair<String, String>) : Event()
    }

    sealed class ViewModel {
        object InitialLoading : ViewModel()
        data class LoadedFragments(val fragments: Pair<List<Pair<CurrencyFragment, String>>, List<Pair<CurrencyFragment, String>>>) :
            ViewModel()

        data class WalletSet(val walletList: List<Wallet>?) : ViewModel()
        data class ExchangeRateToOne(val exchangeRatToOne: CalculatedExchangeRate?) : ViewModel()
        object EnteredValue : ViewModel()
        data class CalculatedEnteredMoney(val calculatedEnteredValue: CalculatedEnteredValue?) :
            ViewModel()

        data class IsViewPagerSwipeable(val isSwipeable: Boolean) : ViewModel()
        data class ExchangeError(val throwable: Throwable) : ViewModel()
    }

    interface Factory : ViewFactory<Nothing?, ExchangeRateView>
}

class ExchangeRateViewImpl private constructor(
    override val androidView: ViewGroup,
    private val events: PublishRelay<ExchangeRateView.Event> = PublishRelay.create()
) : AndroidRibView(),
    ExchangeRateView,
    ObservableSource<ExchangeRateView.Event> by events,
    Consumer<ExchangeRateView.ViewModel>, IExchangeRateEvent {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_exchange_rate
    ) : ExchangeRateView.Factory {
        override fun invoke(deps: Nothing?): (RibView) -> ExchangeRateView = {
            ExchangeRateViewImpl(
                it.inflate(layoutRes)
            )
        }
    }

    private val viewpagerTop = androidView.findViewById<WrapContentViewPager>(R.id.viewpagerTop)
    private val viewpagerBottom =
        androidView.findViewById<WrapContentViewPager>(R.id.viewpagerBottom)

    override fun accept(vm: ExchangeRateView.ViewModel) {
        when (vm) {
            is ExchangeRateView.ViewModel.LoadedFragments -> setupAdapters(vm.fragments)
            is ExchangeRateView.ViewModel.ExchangeRateToOne -> {
                sendEvent(ExchangeRateEvent.ExchangeRateToOne(vm.exchangeRatToOne))
            }
            is ExchangeRateView.ViewModel.WalletSet -> {
                sendEvent(ExchangeRateEvent.WalletSet(vm.walletList))
            }
            is ExchangeRateView.ViewModel.CalculatedEnteredMoney -> {
                sendEvent(ExchangeRateEvent.CalculatedEnteredMoney(vm.calculatedEnteredValue))
            }
            is ExchangeRateView.ViewModel.IsViewPagerSwipeable -> {
                setViewPagersSwipeable(vm.isSwipeable)
            }
            is ExchangeRateView.ViewModel.ExchangeError -> {
                Toast.makeText(androidView.context, "${vm.throwable.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupAdapters(fragments: Pair<List<Pair<CurrencyFragment, String>>, List<Pair<CurrencyFragment, String>>>) {
        setupViewPagerTop(fragments.first)
        setupViewPagerBottom(fragments.second)
        setListeners()
    }

    private fun setupViewPagerTop(fragmentList: List<Pair<CurrencyFragment, String>>) {
        val adapter = FragmentPagerAdapter((context as RootActivity).supportFragmentManager)
        fragmentList.forEach {
            adapter.addFragment(fragment = it.first, title = it.second)
        }
        viewpagerTop.adapter = adapter
    }

    private fun setupViewPagerBottom(fragmentList: List<Pair<CurrencyFragment, String>>) {
        val adapter = FragmentPagerAdapter((context as RootActivity).supportFragmentManager)
        fragmentList.forEach {
            adapter.addFragment(
                fragment = it.first, title = it.second
            )
        }
        viewpagerBottom.adapter = adapter
    }

    private fun setListeners() {
        viewpagerTop.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(
                position: Int, positionOffset: Float, positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                events.accept(ExchangeRateView.Event.OnViewPagerSwiped(getViewPagerState()))
            }
        })
        viewpagerBottom.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(
                position: Int, positionOffset: Float, positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                events.accept(ExchangeRateView.Event.OnViewPagerSwiped(getViewPagerState()))
            }
        })
    }

    private fun getViewPagerState() = Pair(
        viewpagerTop.adapter!!.getPageTitle(viewpagerTop.currentItem).toString(),
        viewpagerBottom.adapter!!.getPageTitle(viewpagerBottom.currentItem).toString()
    )

    private fun setViewPagersSwipeable(isSwipeable: Boolean) {
        if (isSwipeable) {
            if (viewpagerTop.isFakeDragging && viewpagerBottom.isFakeDragging) {
                viewpagerTop.endFakeDrag()
                viewpagerBottom.endFakeDrag()
            }
        } else {
            viewpagerTop.beginFakeDrag()
            viewpagerBottom.beginFakeDrag()
        }
    }
}
