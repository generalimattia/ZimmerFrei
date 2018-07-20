package com.generals.zimmerfrei.overview.inject

import com.generals.zimmerfrei.overview.view.OverviewFragment
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface OverviewFragmentSubcomponent : AndroidInjector<OverviewFragment> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<OverviewFragment>()
}