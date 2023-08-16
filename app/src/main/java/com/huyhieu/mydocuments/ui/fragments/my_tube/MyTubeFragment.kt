package com.huyhieu.mydocuments.ui.fragments.my_tube

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentMyTubeBinding
import com.huyhieu.mydocuments.models.MyTubeForm
import com.huyhieu.mydocuments.utils.init

class MyTubeFragment : BaseFragment<FragmentMyTubeBinding>() {
    private val adapterLocal = MyTubeAdapter()
    private val adapterServer = MyTubeAdapter()

    private val lstLocal = mutableListOf(
        MyTubeForm(
            title = "Đen x JustaTee - Đi Về Nhà (M/V) 1",
            authorName = "Đen Vâu Official"
        ),
        MyTubeForm(
            title = "Đen x JustaTee - Đi Về Nhà (M/V) 2",
            authorName = "Đen Vâu Official"
        ),
        MyTubeForm(
            title = "Đen x JustaTee - Đi Về Nhà (M/V) 3",
            authorName = "Đen Vâu Official"
        ),
        MyTubeForm(
            title = "Đen x JustaTee - Đi Về Nhà (M/V) 4",
            authorName = "Đen Vâu Official"
        ),
        MyTubeForm(
            title = "Đen x JustaTee - Đi Về Nhà (M/V) 5",
            authorName = "Đen Vâu Official"
        ),
        MyTubeForm(
            title = "Đen x JustaTee - Đi Về Nhà (M/V) 6",
            authorName = "Đen Vâu Official"
        ),
        MyTubeForm(
            title = "Đen x JustaTee - Đi Về Nhà (M/V) 7",
            authorName = "Đen Vâu Official"
        ),
        MyTubeForm(
            title = "Đen x JustaTee - Đi Về Nhà (M/V) 8",
            authorName = "Đen Vâu Official"
        ),
    )

    override fun onMyViewCreated(savedInstanceState: Bundle?) = with(vb) {
        //root.setClickViewsOfMotion(txtLocal, txtServer)
        setClickViews(txtLocal, txtServer)
        initView()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() = with(vb) {
        rcvLocal.init(adapterLocal)
        adapterLocal.fillData(lstLocal)
        rcvLocal.setOnTouchListener { v, event ->
            if (root.currentState == R.id.tabLocal) {
                root.setTransition(R.id.transactionTopLocal)
            }
            return@setOnTouchListener false
        }
        frameBottom.setOnTouchListener { v, event ->
            if (root.currentState != R.id.miniWindow || root.currentState != R.id.fullWindow) {
                root.setTransition(R.id.transactionFullWindow)
            }
            return@setOnTouchListener false
        }
    }

    override fun FragmentMyTubeBinding.onClickViewBinding(v: View, id: Int) {
        when (id) {
            txtLocal.id -> {
                if (root.currentState != R.id.tabLocal) {
                    root.setTransition(R.id.transactionTabLocal)
                    root.transitionToEnd()
                }
            }

            txtServer.id -> {
                if (root.currentState != R.id.tabServer) {
                    root.setTransition(R.id.transactionTabServer)
                    root.transitionToEnd()
                }
            }
        }
    }

}