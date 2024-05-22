package com.example.entregaappmoviles

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.entregaappmoviles.databinding.FragmentAddNewFileBinding

class FragmentAddNewFile: Fragment(R.layout.fragment_add_new_file) {
    private var _binding: FragmentAddNewFileBinding? = null
    private val binding get() = _binding!!

    private var listener: OnButtonAddListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnButtonAddListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnButtonAddListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddNewFileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonAdd.setOnClickListener() {
            listener?.onAddButton()
        }
    }

    override fun onDetach() {
        super.onDetach()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface OnButtonAddListener {
        fun onAddButton()
    }
}