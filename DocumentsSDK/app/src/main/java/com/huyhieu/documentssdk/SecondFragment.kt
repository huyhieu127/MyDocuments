package com.huyhieu.documentssdk

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.huyhieu.documentssdk.databinding.FragmentSecondBinding


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFinishCallback.setOnClickListener {
            pushDataCallBack()
            requireActivity().finish()
        }
        binding.buttonFinishBroadcast.setOnClickListener {
            pushDataBroadcastIntent()
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun pushDataCallBack() {
        DocSdkInstance.onData?.invoke("Callback successfully!")
    }

    private fun pushDataBroadcastIntent() {
        val intent = Intent(DocSdkInstance.packageAction)
        intent.putExtra("data", "BroadcastIntent successfully!")
        requireContext().sendBroadcast(intent)
    }
}