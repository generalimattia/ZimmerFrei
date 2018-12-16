package com.generals.zimmerfrei.common.resources

import android.content.Context
import javax.inject.Inject

class StringResourcesProviderImpl @Inject constructor(
        private val context: Context
) : StringResourcesProvider {

    override fun provide(key: Int): String = context.getString(key)
}