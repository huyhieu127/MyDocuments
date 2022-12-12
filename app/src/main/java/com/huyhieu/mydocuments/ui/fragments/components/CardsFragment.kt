package com.huyhieu.mydocuments.ui.fragments.components

import android.os.Bundle
import com.huyhieu.mydocuments.base.BaseFragmentOld
import com.huyhieu.mydocuments.databinding.FragmentCardsBinding

class CardsFragment : BaseFragmentOld<FragmentCardsBinding>() {
    override fun initializeBinding() = FragmentCardsBinding.inflate(layoutInflater)

    override fun FragmentCardsBinding.addControls(savedInstanceState: Bundle?) {
    }

    override fun FragmentCardsBinding.addEvents(savedInstanceState: Bundle?) {
    }
}