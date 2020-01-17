package lt.akb.currency.dagger.android

import dagger.Module
import dagger.android.ContributesAndroidInjector
import lt.akb.currency.converter.RatesFragment

@Module
abstract class FragmentModule {
    /*
     * We define the name of the Fragment we are going
     * to inject the ViewModelFactory into. i.e. in our case
     * The name of the fragment: MovieListFragment
     */
    @ContributesAndroidInjector
    abstract fun contributeRatesFragment(): RatesFragment
}