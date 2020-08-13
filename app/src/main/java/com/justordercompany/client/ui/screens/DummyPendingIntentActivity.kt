package com.justordercompany.client.ui.screens

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.justordercompany.client.extensions.makeClearAllPrevious
import com.justordercompany.client.ui.screens.act_main.ActMain

//This activity made only as dummy start point after pending intent.
//It can restart app if needed or show last activity
class DummyPendingIntentActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        //Here we check if there any activities in stack
        if (isTaskRoot)
        {
            //No previous activity running will restart app
            val intent = Intent(this, ActMain::class.java)
            intent.makeClearAllPrevious()
            startActivity(intent)
        }
        else
        {
            //Has some previous activity so will finish to show it
            finish()
        }
    }
}