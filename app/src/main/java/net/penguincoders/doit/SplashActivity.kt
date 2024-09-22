package net.penguincoders.doit

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Hides the action bar
        supportActionBar?.hide()

        // Intent to move to MainActivity
        val intent = Intent(this, MainActivity::class.java)

        // Delays for 2 seconds before transitioning to MainActivity
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(intent)
            finish()
        }, 2000)
    }
}
