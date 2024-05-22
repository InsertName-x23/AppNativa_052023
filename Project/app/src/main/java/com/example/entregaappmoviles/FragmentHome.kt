package com.example.entregaappmoviles

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.entregaappmoviles.databinding.FragmentHomeBinding
import com.example.entregaappmoviles.databinding.MainActivityBinding


class FragmentHome : Fragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        // Establecer el listener de clic para el botón editTextFilterAdd
        binding.editTextFilterAdd.setOnClickListener {
            // Reemplazar este fragmento con otro fragmento
            replaceFragment(FragmentAddItemToFile())
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    // Método para mostrar los ítems
    fun displayItems(items: List<Item>) {
        binding.itemsContainer.removeAllViews() // Limpiar cualquier vista existente
        for (item in items) {
            val itemView = LayoutInflater.from(context).inflate(R.layout.item_view, binding.itemsContainer, false)
            val nameTextView: TextView = itemView.findViewById(R.id.itemName)
            val dateTextView: TextView = itemView.findViewById(R.id.itemDate)
            val amountTextView: TextView = itemView.findViewById(R.id.itemAmount)
            val descriptionTextView: TextView = itemView.findViewById(R.id.itemDescription)

            nameTextView.text = item.name
            dateTextView.text = item.date
            amountTextView.text = item.amount.toString()
            descriptionTextView.text = item.description

            binding.itemsContainer.addView(itemView)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}