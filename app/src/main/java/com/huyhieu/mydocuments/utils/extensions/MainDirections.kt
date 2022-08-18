package com.huyhieu.mydocuments.utils.extensions

import com.huyhieu.mydocuments.MotionCardFragmentDirections
import com.huyhieu.mydocuments.ui.GuideFragmentDirections
import com.huyhieu.mydocuments.ui.fragments.multipleapi.MultipleApiFragmentDirections

object MainDirections {
    val toGuide
        get() = GuideFragmentDirections.actionGlobalGuideFragment()

    val toMotionCard
        get() = MotionCardFragmentDirections.actionGlobalMotionCardFragment()

    val toMultipleAPI
        get() =
            MultipleApiFragmentDirections.actionGlobalMultipleAPIFragment()
}