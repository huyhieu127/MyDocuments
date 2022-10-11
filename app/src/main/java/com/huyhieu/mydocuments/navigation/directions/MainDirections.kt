package com.huyhieu.mydocuments.navigation.directions


import com.huyhieu.mydocuments.ui.fragments.GuideFragmentDirections
import com.huyhieu.mydocuments.ui.fragments.MotionCardFragmentDirections
import com.huyhieu.mydocuments.ui.fragments.introduce.IntroduceFragmentDirections
import com.huyhieu.mydocuments.ui.fragments.multipleapi.MultipleApiFragmentDirections
import com.huyhieu.mydocuments.ui.fragments.navigation.NavigationFragmentDirections
import com.huyhieu.mydocuments.ui.fragments.steps.StepsFragmentDirections

object MainDirections {

    val toNavigation
        get() = NavigationFragmentDirections.actionGlobalNavigationFragment()

    val toIntroduce
        get() = IntroduceFragmentDirections.actionGlobalIntroduceFragment()

    val toSteps
        get() = StepsFragmentDirections.actionGlobalStepsFragment()

    val toGuide
        get() = GuideFragmentDirections.actionGlobalGuideFragment()

    val toMotionCard
        get() = MotionCardFragmentDirections.actionGlobalMotionCardFragment()

    val toMultipleAPI
        get() =
            MultipleApiFragmentDirections.actionGlobalMultipleAPIFragment()
}