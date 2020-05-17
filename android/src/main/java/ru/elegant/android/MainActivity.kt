package ru.elegant.android

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ru.elegant.relaxtimer.Configuration

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val configuration = Configuration()
        findViewById<TextView>(R.id.hello).text = "Hello, ${configuration.getAppType().name}"
    }
}
