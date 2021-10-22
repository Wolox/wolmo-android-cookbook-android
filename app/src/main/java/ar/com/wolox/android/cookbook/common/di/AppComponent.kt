package ar.com.wolox.android.cookbook.common.di

import android.app.Application
import ar.com.wolox.android.cookbook.CookbookApplication
import ar.com.wolox.android.cookbook.analytics.AnalyticsRecipeModule
import ar.com.wolox.android.cookbook.analytics.AnalyticsRepositoryModule
import ar.com.wolox.android.cookbook.analytics.core.AnalyticsModule
import ar.com.wolox.android.cookbook.coroutines.CoroutinesExampleModule
import ar.com.wolox.android.cookbook.coroutines.football.modules.FootballModule
import ar.com.wolox.android.cookbook.common.di.modules.ChartModule
import ar.com.wolox.android.cookbook.common.di.modules.InstagramModule
import ar.com.wolox.android.cookbook.common.di.modules.RoomModule
import ar.com.wolox.android.cookbook.common.di.modules.LottieModule
import ar.com.wolox.android.cookbook.common.di.modules.TwitterModule
import ar.com.wolox.android.cookbook.common.di.room.PersistenceModule
import ar.com.wolox.android.cookbook.common.di.room.ServiceModule
import ar.com.wolox.android.cookbook.datasync.DataSyncRecipeModule
import ar.com.wolox.android.cookbook.mercadopago.di.MercadoPagoModule
import ar.com.wolox.android.cookbook.navigation.NavigationRecipeModule
import ar.com.wolox.android.cookbook.notifications.di.NotificationRecipeModule
import ar.com.wolox.android.cookbook.tests.TestLoginRecipeModule
import ar.com.wolox.wolmo.core.di.modules.ContextModule
import ar.com.wolox.wolmo.core.di.modules.DefaultModule
import ar.com.wolox.wolmo.core.di.scopes.ApplicationScope
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import io.reactivex.Scheduler
import javax.inject.Named

@ApplicationScope
@Component(
    dependencies = [CookbookNetworkingComponent::class],
    modules = [
        AndroidSupportInjectionModule::class,
        DefaultModule::class,
        ContextModule::class,
        CoroutineDispatchersModule::class,
        TwitterModule::class,
        InstagramModule::class,
        RoomModule::class,
        ChartModule::class,
        AppModule::class,
        NavigationRecipeModule::class,
        DataSyncRecipeModule::class,
        RxJava2Module::class,
        TestLoginRecipeModule::class,
        NotificationRecipeModule::class,
        CoroutinesExampleModule::class,
        FootballModule::class,
        PersistenceModule::class,
        ServiceModule::class,
        AnalyticsModule::class,
        AnalyticsRecipeModule::class,
        AnalyticsRepositoryModule::class,
        MercadoPagoModule::class,
        LottieModule::class
    ]
)
interface AppComponent : AndroidInjector<CookbookApplication> {

    @Named("main")
    fun mainThreadScheduler(): Scheduler

    @Named("background")
    fun backgroundThreadScheduler(): Scheduler

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<CookbookApplication>() {

        @BindsInstance
        abstract fun application(application: Application): Builder

        @BindsInstance
        abstract fun sharedPreferencesName(sharedPreferencesName: String): Builder

        abstract fun networkingComponent(networkingComponent: CookbookNetworkingComponent): Builder
    }
}
