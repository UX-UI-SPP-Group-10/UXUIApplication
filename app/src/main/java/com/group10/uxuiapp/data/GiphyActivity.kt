package com.group10.uxuiapp.data

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.giphy.sdk.core.models.Media
import com.giphy.sdk.ui.GPHContentType
import com.giphy.sdk.ui.Giphy
import com.giphy.sdk.ui.views.GiphyDialogFragment
import com.group10.uxuiapp.data.data_class.TodoList
import com.group10.uxuiapp.view_model.ListViewModel

class GiphyActivity : AppCompatActivity() {

    private val listViewModel: ListViewModel by viewModels() // Obtain the ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configure Giphy with your API key
        Giphy.configure(this, "TfJpapxeqlrKMdtx82hDrPS9RsSCYgDG")

        val todoListId = intent.getIntExtra("todoListId", -1)

        val giphyDialog = GiphyDialogFragment.newInstance()
        giphyDialog.gifSelectionListener = object : GiphyDialogFragment.GifSelectionListener {
            override fun onGifSelected(
                media: Media,
                searchTerm: String?,
                selectedContentType: GPHContentType
            ) {
                val gifUrl = media.images.original?.gifUrl
                if (todoListId != -1 && gifUrl != null) {
                    listViewModel.updateGifUrl(todoListId, gifUrl)
                    finish() // Close the activity
                } else {
                    Toast.makeText(this@GiphyActivity, "Failed to update GIF", Toast.LENGTH_SHORT).show()
                    finish()
                }
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
