package s9.projetandroid.helloble

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import s9.projetandroid.helloble.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val targetIntent = Intent().apply {
        action = android.provider.Settings.ACTION_BLUETOOTH_SETTINGS
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Toast.makeText(this, "This is a toast", Toast.LENGTH_SHORT).show()
        binding.eseoButton.setOnClickListener {
            Toast.makeText(this, "hello ESEO ! This is a toast", Toast.LENGTH_LONG).show()
        }
        binding.settingsButton.setOnClickListener {
            startActivity(targetIntent)
        }
        binding.eseoWebsite.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://eseo.fr")))
        }
        binding.eseoLoc.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=eseo")))
        }

        binding.localisationButton.setOnClickListener {
            startActivity(Intent(this, LocalisationActivity::class.java))
        }

        binding.recyclerButton.setOnClickListener {
            startActivity(Intent(this, RecyclerActivity::class.java))
        }

        Snackbar.make(binding.root, "This is a snackbar", Snackbar.LENGTH_LONG).show()
    }

    companion object {
        private const val IDENTIFIANT_ID = "IDENTIFIANT_ID"

        fun getStartIntent(context: Context, identifiant: String?): Intent {
            return Intent(context, MainActivity::class.java).apply {
                putExtra(IDENTIFIANT_ID, identifiant)
            }
        }
    }

    private fun getIdentifiant(): String? {
        return intent.extras?.getString(IDENTIFIANT_ID, null)
    }
}