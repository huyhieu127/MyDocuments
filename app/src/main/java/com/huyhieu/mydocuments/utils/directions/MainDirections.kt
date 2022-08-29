package com.huyhieu.mydocuments.utils.directions


import com.huyhieu.mydocuments.ui.fragments.GuideFragmentDirections
import com.huyhieu.mydocuments.ui.fragments.MotionCardFragmentDirections
import com.huyhieu.mydocuments.ui.fragments.QRCodeFragmentDirections
import com.huyhieu.mydocuments.ui.fragments.home.HomeFragmentDirections
import com.huyhieu.mydocuments.ui.fragments.introduce.IntroduceFragmentDirections
import com.huyhieu.mydocuments.ui.fragments.multipleapi.MultipleApiFragmentDirections
import com.huyhieu.mydocuments.ui.fragments.steps.StepsFragmentDirections

object MainDirections {

    val toScan
        get() = QRCodeFragmentDirections.actionGlobalQRCodeFragment()

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

    val toHome
        get() = HomeFragmentDirections.actionGlobalHomeFragment()
}