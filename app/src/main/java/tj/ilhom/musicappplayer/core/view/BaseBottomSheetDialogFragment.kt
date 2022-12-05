package tj.ilhom.musicappplayer.core.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import tj.ilhom.musicappplayer.core.utils.ToolbarUtil
import javax.inject.Inject

abstract class BaseBottomSheetDialogFragment constructor(
    @LayoutRes val layout: Int, val mtheme: Int, val fullScreen: Boolean
) : BottomSheetDialogFragment() {

    protected val toolbar: ToolbarUtil by lazy { ToolbarUtil(requireActivity() as AppCompatActivity) }

    override fun getTheme(): Int {
        return mtheme
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layout, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        if (fullScreen) {
            dialog.setOnShowListener {
                val bottomSheetDialog = it as BottomSheetDialog
                val parentLayout = bottomSheetDialog.findViewById<View>(
                    com.google.android.material.R.id.design_bottom_sheet
                )
                parentLayout?.let { bottomSheet ->
                    val behaviour = BottomSheetBehavior.from(bottomSheet)
                    val layoutParams = bottomSheet.layoutParams
                    layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
                    bottomSheet.layoutParams = layoutParams
                    behaviour.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
        }
        return dialog
    }

    abstract override fun onViewCreated(view: View, savedInstanceState: Bundle?)

    protected companion object {
        val DATA = "DATA"
    }

}