package com.generals.zimmerfrei.inject

import com.generals.zimmerfrei.common.UpdateOverviewEmitter
import com.generals.zimmerfrei.common.UpdateOverviewEmitterImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class EmittersModule {

    @Binds
    @Singleton
    abstract fun bindUpdateOverviewEmitter(impl: UpdateOverviewEmitterImpl): UpdateOverviewEmitter
}