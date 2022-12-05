package tj.ilhom.musicappplayer.module.main.view

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import dagger.hilt.android.AndroidEntryPoint
import tj.ilhom.musicappplayer.R
import tj.ilhom.musicappplayer.core.view.BaseActivity
import tj.ilhom.musicappplayer.extention.checkReadStoragePermission
import tj.ilhom.musicappplayer.extention.showLongToast
import tj.ilhom.musicappplayer.extention.showToolbar
import tj.ilhom.musicappplayer.extention.transaction
import tj.ilhom.musicappplayer.module.main.view.fragment.MusicFragment

@AndroidEntryPoint
class MainActivity : BaseActivity(R.layout.activity_main) {

    private val permission =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (checkReadStoragePermission()) {
                showLongToast(getString(R.string.PERMISSION_GRANTED))
                showMusicFragment()
            } else {
                showLongToast(getString(R.string.PERMISSION_DAINED))
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showToolbar()
        if (savedInstanceState == null) {
            if (!checkReadStoragePermission()) {
                permission()
            } else {
                showMusicFragment()
            }
        }
    }

    private fun showMusicFragment() {
        transaction(R.id.main_fragment_container, MusicFragment())
    }

    private fun permission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.addCategory("android.intent.category.DEFAULT")
                intent.data = Uri.parse(String.format("package:%s", applicationContext.packageName))
                permission.launch(intent)
            } catch (e: Exception) {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                permission.launch(intent)
            }
        } else {
            permission.launch(Intent(READ_EXTERNAL_STORAGE))
        }
    }
}