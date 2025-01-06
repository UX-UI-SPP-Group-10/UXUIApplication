package com.group10.uxuiapp.data

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.giphy.sdk.ui.Giphy
import com.giphy.sdk.ui.views.GiphyDialogFragment

class GiphyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Giphy.configure(this, "TfJpapxeqlrKMdtx82hDrPS9RsSCYgDG")
        GiphyDialogFragment.newInstance().show(supportFragmentManager, "giphy_dialog")
    }
}