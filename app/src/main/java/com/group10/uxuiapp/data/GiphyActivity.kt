package com.group10.uxuiapp.data

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.giphy.sdk.core.models.Media
import com.giphy.sdk.ui.GPHContentType
import com.giphy.sdk.ui.Giphy
import com.giphy.sdk.ui.views.GiphyDialogFragment

class GiphyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configure Giphy with your API key
        Giphy.configure(this, "TfJpapxeqlrKMdtx82hDrPS9RsSCYgDG")

        // Show the Giphy dialog and set listeners
        val giphyDialog = GiphyDialogFragment.newInstance()

        giphyDialog.gifSelectionListener = object : GiphyDialogFragment.GifSelectionListener {

            override fun onGifSelected(
                media: Media,
                searchTerm: String?,
                selectedContentType: GPHContentType
            ) {
                // Handle GIF selection
                val gifUrl = media.images.original?.gifUrl
                Toast.makeText(this@GiphyActivity, "Selected GIF URL: $gifUrl", Toast.LENGTH_LONG).show()
                finish() // Close the activity after GIF selection
            }

            override fun didSearchTerm(term: String) {
                TODO("Not yet implemented")
            }

            override fun onDismissed(selectedContentType: GPHContentType) {
                finish() // Close the activity when the dialog is dismissed
            }
        }

        giphyDialog.show(supportFragmentManager, "giphy_dialog")
    }
}
