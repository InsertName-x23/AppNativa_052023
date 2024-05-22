package com.example.entregaappmoviles

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class FragmentAddItemToFile : Fragment(R.layout.fragment_add_item_to_file) {

    lateinit var nameEditText: EditText
    lateinit var dateEditText: EditText
    lateinit var amountEditText: EditText
    lateinit var descriptionEditText: EditText
    lateinit var Add: Button
    var mainActivity: MainActivity? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Add = view.findViewById(R.id.button_add)
        nameEditText = view.findViewById(R.id.editTextName)
        dateEditText = view.findViewById(R.id.editTextDate)
        amountEditText = view.findViewById(R.id.editTextAmount)
        descriptionEditText = view.findViewById(R.id.editTextDescripcion)

        // Obtener la referencia de MainActivity
        mainActivity = activity as? MainActivity

        // Mostrar los items del botón seleccionado actualmente
        displayCurrentButtonItems()

        Add.setOnClickListener {
            onClickAdd()
        }
    }

    private fun displayCurrentButtonItems() {
        val selectedButton = mainActivity?.currentSelectedButton
        if (selectedButton != null) {
            val selectedButtonData = selectedButton.tag as? ButtonData
            if (selectedButtonData != null) {
                updateItemsList(selectedButtonData)
            }
        }
    }

    fun onClickAdd() {
        // Verificar si se ha seleccionado un botón
        val selectedButton = mainActivity?.currentSelectedButton
        if (selectedButton != null) {
            // Obtener los valores ingresados en los campos de entrada
            val name = nameEditText.text.toString()
            val date = dateEditText.text.toString()
            val amount = amountEditText.text.toString().toIntOrNull() ?: 0
            val description = descriptionEditText.text.toString()

            // Crear un nuevo objeto Item
            val newItem = Item(name, date, amount, description)

            // Obtener la referencia al botón data asociado al botón seleccionado
            val selectedButtonData = selectedButton.tag as? ButtonData

            // Verificar si la referencia al botón data está disponible
            if (selectedButtonData != null) {
                // Agregar el nuevo elemento a la lista del botón seleccionado
                selectedButtonData.items.add(newItem)

                // Guardar los botones actualizados en el archivo JSON
                mainActivity?.saveButtonsToFile()

                // Limpiar los campos de entrada después de agregar el elemento
                nameEditText.text.clear()
                dateEditText.text.clear()
                amountEditText.text.clear()
                descriptionEditText.text.clear()

                // Notificar al usuario que el elemento se agregó correctamente
                Toast.makeText(requireContext(), "Item added successfully", Toast.LENGTH_SHORT).show()

                // Actualizar la lista de items en el layout
                updateItemsList(selectedButtonData)
            } else {
                // Notificar al usuario que debe seleccionar un botón antes de agregar un elemento
                Toast.makeText(requireContext(), "Please select a button first", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Notificar al usuario que debe seleccionar un botón antes de agregar un elemento
            Toast.makeText(requireContext(), "Please select a button first", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateItemsList(selectedButtonData: ButtonData) {
        // Limpiar la lista de items existentes en el layout
        val itemsContainer = view?.findViewById<LinearLayout>(R.id.itemsContainer)
        itemsContainer?.removeAllViews()

        // Agregar cada nombre de item como un nuevo TextView al layout
        for (item in selectedButtonData.items) {
            val textView = TextView(requireContext()).apply {
                text = item.name
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }
            itemsContainer?.addView(textView)
        }
    }

}










