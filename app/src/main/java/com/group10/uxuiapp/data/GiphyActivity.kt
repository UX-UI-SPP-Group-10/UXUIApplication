package com.group10.uxuiapp.data

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.giphy.sdk.core.models.Media
import com.giphy.sdk.ui.GPHContentType
import com.giphy.sdk.ui.Giphy
import com.giphy.sdk.ui.views.GiphyDialogFragment
import com.group10.uxuiapp.data.data_class.TodoList
import com.group10.uxuiapp.view_model.ListViewModel
import com.group10.uxuiapp.view_model.ListViewModelFactory

class GiphyActivity : AppCompatActivity() {

    private lateinit var listViewModel: ListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Giphy.configure(this, "TfJpapxeqlrKMdtx82hDrPS9RsSCYgDG")

        // Create the database and repository
        val database = DatabaseProvider.getDatabase(this)
        val taskRepository = TaskRepository(database.taskDao())

        // Initialize ViewModelFactory and ViewModel
        val viewModelFactory = ListViewModelFactory(taskRepository)
        listViewModel = ViewModelProvider(this, viewModelFactory).get(ListViewModel::class.java)

        // Existing Giphy setup
        val giphyDialog = GiphyDialogFragment.newInstance()
        giphyDialog.gifSelectionListener = object : GiphyDialogFragment.GifSelectionListener {
            override fun onGifSelected(media: Media, searchTerm: String?, selectedContentType: GPHContentType) {
                val gifUrl = media.images.original?.gifUrl

                // Assuming taskListId is passed to this activity
                val todoListId = intent.getIntExtra("todoListId", -1)
                if (todoListId != -1 && gifUrl != null) {
                    listViewModel.updateGifUrl(todoListId, gifUrl)

                    Toast.makeText(this@GiphyActivity, "GIF updated successfully", Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(this@GiphyActivity, "Failed to update GIF", Toast.LENGTH_SHORT).show()
                    finish()
                }

                // Close the GiphyActivity
                finish()
            }


            override fun didSearchTerm(term: String) {
                // Not yet implemented
            }

            override fun onDismissed(selectedContentType: GPHContentType) {
                finish()
            }
        }
        giphyDialog.show(supportFragmentManager, "giphy_dialog")
    }
}
