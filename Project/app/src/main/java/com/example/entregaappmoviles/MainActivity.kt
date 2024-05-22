package com.example.entregaappmoviles

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.entregaappmoviles.databinding.MainActivityBinding
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity(), FragmentAddNewFile.OnButtonAddListener {


    lateinit var binding: MainActivityBinding
    private val buttonList = mutableListOf<ButtonData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(savedInstanceState == null) {
            replaceFragment(FragmentHome())
        }

        binding.addFolderBtn.setOnClickListener() {
            replaceFragment(FragmentAddNewFile())

        }

        binding.homeButton.setOnClickListener() {
            replaceFragment(FragmentHome())
        }

        restoreButtons()
    }

    override fun onPause() {
        super.onPause()
        saveButtons()
    }

    override fun onAddButton() {
        addNewButton()
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }

    private fun addNewButton() {
        val buttonData = ButtonData("${buttonList.size + 1}")
        buttonList.add(buttonData)
        createButton(buttonData)
    }

    private fun saveButtons() {
        val sharedPreferences = getSharedPreferences("button_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(buttonList)
        editor.putString("button_list", json)
        editor.apply()
    }

    private var lastSelectedButton: Button? = null

    private fun createButton(buttonData: ButtonData) {
        val newButton = Button(this).apply {
            text = buttonData.text
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                // Agregar márgenes al botón
                setMargins(7.dpToPx(), 7.dpToPx(), 7.dpToPx(), 7.dpToPx())
            }
            setBackgroundResource(R.drawable.buttons_desing) // Aplicar el diseño predeterminado
            setOnClickListener {
                // Cambiar el estilo del botón seleccionado
                selectButton(this)
                // Reemplazar el fragmento actual con FragmentHome
                replaceFragment(FragmentHome())
            }
            setOnLongClickListener {
                removeButton(this)
                true
            }
        }
        binding.buttonContainer.addView(newButton)
    }

    // Extensión para convertir dp a píxeles
    private fun Int.dpToPx(): Int {
        val scale = resources.displayMetrics.density
        return (this * scale + 0.5f).toInt()
    }

    private fun selectButton(button: Button) {
        // Restablecer el estilo del último botón seleccionado
        lastSelectedButton?.setBackgroundResource(R.drawable.buttons_desing)
        // Aplicar el nuevo estilo al botón seleccionado
        button.setBackgroundResource(R.drawable.buttons_selected_desing)
        // Actualizar la referencia al último botón seleccionado
        lastSelectedButton = button
    }

    private fun removeButton(button: Button) {
        binding.buttonContainer.removeView(button)
        buttonList.removeIf {it.text == button.text}
        saveButtons()
    }

    private fun restoreButtons() {
        val sharedPreferences = getSharedPreferences("button_prefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("button_list", null)
        if (json != null) {
            val type = object : TypeToken<MutableList<ButtonData>>() {}.type
            val restoredList: MutableList<ButtonData> = gson.fromJson(json, type)
            buttonList.addAll(restoredList)
            for (buttonData in buttonList) {
                createButton(buttonData)
            }
        }
    }


}



