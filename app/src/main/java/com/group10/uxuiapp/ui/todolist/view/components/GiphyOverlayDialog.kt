import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.giphy.sdk.core.models.Media
import com.giphy.sdk.ui.GPHContentType
import com.giphy.sdk.ui.views.GiphyDialogFragment

@Composable
fun GiphyDialog(
    context: Context,
    onGifSelected: (String) -> Unit,
    onDismissed: () -> Unit
) {
    val activity = context as? FragmentActivity
        ?: throw IllegalStateException("GiphyDialog requires a FragmentActivity context")

    DisposableEffect(Unit) {
        val dialogFragment = GiphyDialogFragment.newInstance().apply {
            gifSelectionListener = object : GiphyDialogFragment.GifSelectionListener {
                override fun onGifSelected(
                    media: Media,
                    searchTerm: String?,
                    selectedContentType: GPHContentType
                ) {
                    media.images.original?.gifUrl?.let { gifUrl ->
                        onGifSelected(gifUrl)
                    }
                }

                override fun didSearchTerm(term: String) {
                    // Handle search term if needed
                }

                override fun onDismissed(selectedContentType: GPHContentType) {
                    onDismissed()
                }
            }
        }

        dialogFragment.show(activity.supportFragmentManager, "GiphyDialog")

        onDispose {
            dialogFragment.dismiss()
        }
    }
}
