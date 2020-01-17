package lt.akb.currency.dagger

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import lt.akb.currency.RatesApplication
import lt.akb.currency.dagger.android.FragmentModule
import lt.akb.currency.dagger.viewModel.ViewModelModule
import javax.inject.Singleton

//TODO Use subcomponets @Component(modules = [AndroidInjectionModule::class, FragmentModule::class, ViewModelModule::class, WebApiModule::class, AppSubComponents::class])  - https://dagger.dev/android.html
@Singleton
@Component(modules = [AndroidInjectionModule::class, FragmentModule::class, ViewModelModule::class, WebApiModule::class, RoomModule::class, RetrofitModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(appComponent: RatesApplication)

}