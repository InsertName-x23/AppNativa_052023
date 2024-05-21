package com.example.entregaappmoviles

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.entregaappmoviles.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {

<<<<<<< HEAD
    private lateinit var binding: MainActivityBinding
=======
    lateinit var binding: MainActivityBinding
>>>>>>> main
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

<<<<<<< HEAD
=======
        if(savedInstanceState == null) {
            replaceFragment(FragmentHome())
        }

>>>>>>> main
        binding.addFolderBtn.setOnClickListener() {
            replaceFragment(FragmentAddFolder())
        }

        binding.homeButton.setOnClickListener() {
<<<<<<< HEAD

=======
            replaceFragment(FragmentHome())
>>>>>>> main
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }
}



