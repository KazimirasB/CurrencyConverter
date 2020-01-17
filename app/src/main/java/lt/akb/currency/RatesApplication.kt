package lt.akb.currency

import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import lt.akb.currency.dagger.AppComponent
import lt.akb.currency.dagger.DaggerAppComponent
import javax.inject.Inject

open class RatesApplication : Application(), HasAndroidInjector {

    //for Android.dagger to reduce injecting boiling code
    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    //for ure use, if need get som object from component
    val appComponent: AppComponent by lazy {
        initializeComponent()
    }

    override fun onCreate() {
        super.onCreate()
        //Inject application class into component
        appComponent.inject(this)
    }

    //creates component and provide context for all dependencies in modules
    open fun initializeComponent(): AppComponent {
        return DaggerAppComponent.factory().create(applicationContext)
    }

    //?? this injector later uses in fragments, activity and else to inject dependencies
    override fun androidInjector(): AndroidInjector<Any> = androidInjector

}
