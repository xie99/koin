package org.koin.sample.androidx.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.dsl.onRelease
import org.koin.sample.androidx.components.Counter
import org.koin.sample.androidx.components.SCOPE_ID
import org.koin.sample.androidx.components.SCOPE_SESSION
import org.koin.sample.androidx.components.main.DumbServiceImpl
import org.koin.sample.androidx.components.main.RandomId
import org.koin.sample.androidx.components.main.Service
import org.koin.sample.androidx.components.main.ServiceImpl
import org.koin.sample.androidx.components.mvp.FactoryPresenter
import org.koin.sample.androidx.components.mvp.ScopedPresenter
import org.koin.sample.androidx.components.mvvm.SimpleViewModel
import org.koin.sample.androidx.components.scope.Session
import org.koin.sample.androidx.mvp.MVPActivity
import org.koin.sample.androidx.scope.ScopedActivityA

val appModule = module {

    single<Service> { ServiceImpl() }
    single<Service>(named("dumb")) { DumbServiceImpl() }

    factory { RandomId() }
}

val mvpModule = module {
    factory { (id: String) -> FactoryPresenter(id, get()) }

    scope(named<MVPActivity>()) {
        scoped { (id: String) -> ScopedPresenter(id, get()) }
    }
}

val mvvmModule = module {
    viewModel { (id: String) -> SimpleViewModel(id, get()) }
}

val scopeModule = module {
    scope(named(SCOPE_ID)) {
        scoped(named(SCOPE_SESSION)) { Session() } onRelease {
            // onRelease, count it
            Counter.released++
            println("Scoped -SCOPE_SESSION- release = ${Counter.released}")
        }
    }
    scope(named<ScopedActivityA>()) {
        scoped { Session() }
    }
}