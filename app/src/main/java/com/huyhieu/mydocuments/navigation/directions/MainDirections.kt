package com.huyhieu.mydocuments.navigation.directions

import com.huyhieu.mydocuments.ui.fragments.navigation.NavigationFragmentDirections

object MainDirections {

    val toMultipleAPI
        get() =
            NavigationFragmentDirections.actionGlobalMultipleAPIFragment()

    val toSwipe
        get() =
            NavigationFragmentDirections.actionGlobalSwipeRefreshFragment()

    val toCalendar
        get() =
            NavigationFragmentDirections.actionGlobalCalendarFragment()

    val toBannerInfinite
        get() =
            NavigationFragmentDirections.actionGlobalBannerInfiniteFragment()

    val toFingerprint
        get() =
            NavigationFragmentDirections.actionGlobalFingerprintFragment()

    val toPinCode
        get() =
            NavigationFragmentDirections.actionGlobalPinCodeFragment()

    val toLineChart
        get() =
            NavigationFragmentDirections.actionGlobalLineChartFragment()

    val toMap
        get() =
            NavigationFragmentDirections.actionGlobalMapFragment()

    val toStaticMap
        get() =
            NavigationFragmentDirections.actionGlobalStaticMapFragment()

    val toCountdown
        get() =
            NavigationFragmentDirections.actionGlobalCountdownFragment()

    val toNavigation
        get() = NavigationFragmentDirections.actionGlobalNavigationFragment()

    val toIntroduce
        get() = NavigationFragmentDirections.actionGlobalIntroduceFragment()

    val toSteps
        get() = NavigationFragmentDirections.actionGlobalStepsFragment()

    val toGuide
        get() = NavigationFragmentDirections.actionGlobalGuideFragment()

    val toMotionCard
        get() = NavigationFragmentDirections.actionGlobalMotionCardFragment()

    val toRecallAPI
        get() = NavigationFragmentDirections.actionGlobalRecallAPIFragment()
}