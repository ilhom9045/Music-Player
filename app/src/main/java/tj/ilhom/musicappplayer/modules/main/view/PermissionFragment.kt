package tj.ilhom.musicappplayer.modules.main.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import tj.ilhom.musicappplayer.R
import tj.ilhom.musicappplayer.core.view.BaseFragment
import tj.ilhom.musicappplayer.extention.checkReadStoragePermission
import tj.ilhom.musicappplayer.extention.showLongToast
import tj.ilhom.musicappplayer.extention.showToolbar
import tj.ilhom.musicappplayer.extention.transaction
import tj.ilhom.musicappplayer.modules.main.view.fragment.MusicFragment

@AndroidEntryPoint
class PermissionFragment : BaseFragment(R.layout.empty_view) {

    private val permission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (requireContext().checkReadStoragePermission()) {
                showLongToast(getString(R.string.PERMISSION_GRANTED))
                showMusicFragment()
            } else {
                showLongToast(getString(R.string.PERMISSION_DAINED))
                requireActivity().finish()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).showToolbar()
        if (!requireContext().checkReadStoragePermission()) {
            permission()
        } else {
            showMusicFragment()
        }
    }

    private fun showMusicFragment() {
        transaction(R.id.start_fragment_container, MusicFragment())
    }

    private fun permission() {
        try {
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            intent.addCategory("android.intent.category.DEFAULT")
            intent.data = Uri.parse(
                String.format(
                    "package:%s",
                    requireActivity().applicationContext.packageName
                )
            )
            permission.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}