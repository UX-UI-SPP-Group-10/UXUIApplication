package com.group10.uxuiapp.data

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.giphy.sdk.core.models.Media
import com.giphy.sdk.ui.GPHContentType
import com.giphy.sdk.ui.Giphy
import com.giphy.sdk.ui.views.GiphyDialogFragment
import com.group10.uxuiapp.ui.todolist.viewmodel.TodoListViewModel
import com.group10.uxuiapp.ui.todolist.viewmodel.TodoListViewModelFactory

class GiphyActivity : AppCompatActivity() {

    private lateinit var todoListViewModel: TodoListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Giphy.configure(this, "TfJpapxeqlrKMdtx82hDrPS9RsSCYgDG")

        // Create the database and repository
        val database = DatabaseProvider.getDatabase(this)
        val taskDatasource = TaskDataSource(database.taskDao())

        // Initialize ViewModelFactory and ViewModel
        val viewModelFactory = TodoListViewModelFactory(taskDatasource)
        todoListViewModel = ViewModelProvider(this, viewModelFactory).get(TodoListViewModel::class.java)

        // Existing Giphy setup
        val giphyDialog = GiphyDialogFragment.newInstance()
        giphyDialog.gifSelectionListener = object : GiphyDialogFragment.GifSelectionListener {
            override fun onGifSelected(media: Media, searchTerm: String?, selectedContentType: GPHContentType) {
                val gifUrl = media.images.original?.gifUrl

                // Assuming taskListId is passed to this activity
                val todoListId = intent.getIntExtra("todoListId", -1)
                if (todoListId != -1 && gifUrl != null) {
                    todoListViewModel.updateGifUrl(todoListId, gifUrl)

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
