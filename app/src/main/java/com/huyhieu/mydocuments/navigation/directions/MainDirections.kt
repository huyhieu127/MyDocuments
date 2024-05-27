package com.huyhieu.mydocuments.navigation.directions

import com.huyhieu.mydocuments.ui.fragments.navigation.NavigationFragmentDirections

object MainDirections {

    val toInAppUpdate
        get() =
            NavigationFragmentDirections.actionGlobalInAppUpdateFragment()

    val toMultipleAPI
        get() =
            NavigationFragmentDirections.actionGlobalMultipleAPIFragment()

    val toSwipe
        get() =
            NavigationFragmentDirections.actionGlobalSwipeRefreshFragment()

    val toCalendar
        get() =
            NavigationFragmentDirections.actionGlobalCalendarFragment()

    val toFingerprint
        get() =
            NavigationFragmentDirections.actionGlobalFingerprintFragment()


    //UI/UX
    val toSwipeToRefresh
        get() = NavigationFragmentDirections.actionGlobalSwipeToRefreshFragment()
    val toPinCode
        get() = NavigationFragmentDirections.actionGlobalPinCodeFragment()
    val toSwipeRcv
        get() = NavigationFragmentDirections.actionGlobalNotificationSwipeFragment()
    val toGuide
        get() = NavigationFragmentDirections.actionGlobalGuideFragment()
    val toSteps
        get() = NavigationFragmentDirections.actionGlobalStepsFragment()
    val toBannerInfinite
        get() =
            NavigationFragmentDirections.actionGlobalBannerInfiniteFragment()
    val toIntroduce
        get() = NavigationFragmentDirections.actionGlobalIntroduceFragment()

    //Firebase
    val toCloudMessaging
        get() =
            NavigationFragmentDirections.actionGlobalCloudMessagingFragment()

    //Charts
    val toRadarChart
        get() = NavigationFragmentDirections.actionGlobalRadarChartFragment()
    val toCubicChart
        get() = NavigationFragmentDirections.actionGlobalCubicChartFragment()
    val toLineChart
        get() =
            NavigationFragmentDirections.actionGlobalLineChartFragment()

    //Features
    val toPowerConnection
        get() =
            NavigationFragmentDirections.actionGlobalPowerConnectionReceiverFragment()

    val toVolume
        get() =
            NavigationFragmentDirections.actionGlobalVolumeBroadcastReceiverFragment()

    val toContentProvider
        get() =
            NavigationFragmentDirections.actionGlobalContentProviderFragment()

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

    val toMotionCard
        get() = NavigationFragmentDirections.actionGlobalMotionCardFragment()

    val toRecallAPI
        get() = NavigationFragmentDirections.actionGlobalRecallAPIFragment()

    //Dynamic features Fragment
    val toHelpCenter
        get() = NavigationFragmentDirections.actionGlobalHelpCenterFragment()
    val toHelpCenterGraph
        get() = NavigationFragmentDirections.actionGlobalHelpCenterGraph()

}