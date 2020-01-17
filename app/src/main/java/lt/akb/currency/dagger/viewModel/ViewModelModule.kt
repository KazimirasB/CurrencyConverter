package lt.akb.currency.dagger.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import lt.akb.currency.converter.RatesViewModel

@Module
internal abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    
    @Binds
    @IntoMap
    @ViewModelKey(RatesViewModel::class)
    protected abstract fun rateViewModel(moviesListViewModel: RatesViewModel): ViewModel
}