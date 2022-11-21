package s9.projetandroid.helloble

import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import s9.projetandroid.helloble.databinding.ActivityLocalisationBinding
import s9.projetandroid.helloble.databinding.ActivityRecyclerBinding

class RecyclerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecyclerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = ExempleAdapter(
            listOf("Voiture", "Abre", "Maison", "Bateau", "Avion", "Train", "Bus", "Velo", "Moto", "Trotinette").toTypedArray()
        ) {
                string -> Toast.makeText(this@RecyclerActivity, "String : $string", Toast.LENGTH_SHORT).show()
        }

    }
}