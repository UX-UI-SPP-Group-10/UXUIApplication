package com.group10.uxuiapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.work.Configuration
import androidx.work.WorkManager
import com.giphy.sdk.ui.Giphy
import com.group10.uxuiapp.data.DatabaseProvider
import com.group10.uxuiapp.data.TaskDataSource
import com.group10.uxuiapp.data.TaskWorkerFactory
import com.group10.uxuiapp.data.data_class.WorkManagerHelper
import com.group10.uxuiapp.ui.navigation.MainNavigation
import com.group10.uxuiapp.ui.tasks.viewmodel.TaskViewModelFactory
import com.group10.uxuiapp.ui.theme.UXUIApplicationTheme
import com.group10.uxuiapp.ui.todolist.viewmodel.TodoListViewModelFactory
import timber.log.Timber

class MainActivity : FragmentActivity() {  //FragmentActivity
    private lateinit var taskDataSource: TaskDataSource


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create the database and repository
        val database = DatabaseProvider.getDatabase(this)
        val taskDataSource = TaskDataSource(database.taskDao())
        val workerFactory = TaskWorkerFactory(taskDataSource)

        Giphy.configure(this, "TfJpapxeqlrKMdtx82hDrPS9RsSCYgDG")


        // Initialize WorkManager with the custom WorkerFactory
        Log.d("MainActivity", "Before WorkManager initialization")
//        WorkManager.initialize(
//            this,
//            Configuration.Builder()
//                .setWorkerFactory(workerFactory)
//                .build()
//        )
        Timber.tag("MainActivity").d("After WorkManager initialization")

        // Schedule reset worker
        WorkManagerHelper.scheduleResetWorker(this)



        setContent {
            UXUIApplicationTheme {
                // Pass the ViewModelFactory to the MainNavigation
                MainNavigation(
                    taskListViewModelFactory = TaskViewModelFactory(taskDataSource),
                    todoListViewModelFactory = TodoListViewModelFactory(taskDataSource),
                )
            }
        }
    }
}