package lt.akb.currency.dagger.android

import dagger.Module
import dagger.android.ContributesAndroidInjector
import lt.akb.currency.main.MainActivity

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun contributeMainActivity(): MainActivity
}