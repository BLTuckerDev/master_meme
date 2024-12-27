package dev.bltucker.mastermeme

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import dev.bltucker.mastermeme.common.MemeRepository
import dev.bltucker.mastermeme.common.templates.MemeTemplateInitializer
import dev.bltucker.mastermeme.common.templates.MemeTemplatesRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class MasterMemeApp : Application(){

    @Inject
    lateinit var memeRepository: MemeRepository

    @Inject
    lateinit var memeTemplateRepository: MemeTemplatesRepository

    @Inject
    lateinit var memeTemplatesInitializer: MemeTemplateInitializer

    override fun onCreate() {
        super.onCreate()

        val templates = memeTemplatesInitializer.createTemplateList()
        memeTemplateRepository.loadTemplates(templates)

        GlobalScope.launch {
            memeRepository.cleanupOrphanedEntries()
        }
    }
}