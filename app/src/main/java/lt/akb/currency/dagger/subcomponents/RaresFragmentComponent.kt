package lt.akb.currency.dagger.subcomponents

import dagger.Subcomponent
import lt.akb.currency.dagger.RetrofitModule
import lt.akb.currency.database.RoomModule

@FragmentScope
@Subcomponent(modules = [RoomModule::class, RetrofitModule::class])
interface RaresFragmentComponent {

    @Subcomponent.Factory
    interface Factory{
        fun create(): RaresFragmentComponent
    }

//    fun inject(fragment: RatesFragment)
}
