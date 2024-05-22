package com.example.entregaappmoviles

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.entregaappmoviles.databinding.MainActivityBinding
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.content.Context
import com.example.entregaappmoviles.databinding.FragmentAddNewFileBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken



class MainActivity : AppCompatActivity(), FragmentAddNewFile.OnButtonAddListener {

    lateinit var binding: MainActivityBinding
    private var lastSelectedButton: Button? = null
    var selectedButtonData: ButtonData? = null

    private val Litem = ArrayList<Item>()
    private val buttonList = mutableListOf<ButtonData>()

    // Variable para almacenar la referencia al botón seleccionado actualmente
    var currentSelectedButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Restaurar los botones guardados al iniciar la aplicación
        restoreButtonsFromFile()

        if (savedInstanceState == null) {
            replaceFragment(FragmentHome())
        }

        binding.addFolderBtn.setOnClickListener {
            replaceFragment(FragmentAddNewFile())
        }

        binding.homeButton.setOnClickListener {
            replaceFragment(FragmentHome())
        }
    }

    override fun onPause() {
        super.onPause()
        saveButtonsToFile()
    }

    override fun onAddButton() {
        addNewButton()
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, fragment) // Cambiado de replace a add
            .addToBackStack(null)
            .commit()
    }

    private fun addNewButton() {
        val buttonData = ButtonData("${buttonList.size + 1}", Litem)
        buttonList.add(buttonData)
        createButton(buttonData)
    }

    fun saveButtonsToFile() {
        val gson = Gson()
        val buttonsJson = gson.toJson(buttonList)

        try {
            openFileOutput("buttons.json", Context.MODE_PRIVATE).use {
                it.write(buttonsJson.toByteArray())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun restoreButtonsFromFile() {
        try {
            val buttonsJson = openFileInput("buttons.json").bufferedReader().useLines { lines ->
                lines.fold("") { some, text ->
                    some + text
                }
            }

            val gson = Gson()
            val type = object : TypeToken<MutableList<ButtonData>>() {}.type
            val restoredList: MutableList<ButtonData> = gson.fromJson(buttonsJson, type)
            buttonList.addAll(restoredList)

            for (buttonData in buttonList) {
                createButton(buttonData)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun createButton(buttonData: ButtonData) {
        val newButton = Button(this).apply {
            text = buttonData.text
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(7.dpToPx(), 7.dpToPx(), 7.dpToPx(), 7.dpToPx())
            }
            setBackgroundResource(R.drawable.buttons_desing)
            tag = buttonData // Asignar el ButtonData al tag del botón
            setOnClickListener {
                selectButton(this)
            }
            setOnLongClickListener {
                removeButton(this)
                true
            }
        }
        binding.buttonContainer.addView(newButton)
    }

    private fun Int.dpToPx(): Int {
        val scale = resources.displayMetrics.density
        return (this * scale + 0.5f).toInt()
    }

    private fun selectButton(button: Button) {
        lastSelectedButton?.setBackgroundResource(R.drawable.buttons_desing)
        button.setBackgroundResource(R.drawable.buttons_selected_desing)
        lastSelectedButton = button
        selectedButtonData = button.tag as? ButtonData

        // Actualizar la referencia al botón seleccionado actualmente
        currentSelectedButton = button

        // Solo reemplazar el fragmento si no es FragmentHome
        if (supportFragmentManager.findFragmentById(R.id.fragment_container) !is FragmentHome) {
            replaceFragment(FragmentHome())
        }

        // Actualizar la lista de ítems
        (supportFragmentManager.findFragmentById(R.id.fragment_container) as? FragmentHome)?.displayItems(getSelectedButtonItems().orEmpty())
    }

    private fun removeButton(button: Button) {
        binding.buttonContainer.removeView(button)
        buttonList.removeIf { it.text == button.text }
        saveButtonsToFile() // Guardar los cambios en el archivo JSON
    }

    // Función para obtener los ítems del botón seleccionado
    fun getSelectedButtonItems(): List<Item>? {
        return selectedButtonData?.items
    }
}
