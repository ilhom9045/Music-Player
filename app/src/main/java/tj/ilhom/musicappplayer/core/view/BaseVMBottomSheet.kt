package tj.ilhom.musicappplayer.core.view

import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModelProvider
import tj.ilhom.musicappplayer.R
import tj.ilhom.musicappplayer.core.vm.BaseViewModel
import javax.inject.Inject

abstract class BaseVMBottomSheet<VM : BaseViewModel>(
    clazz: Class<VM>,
    @LayoutRes layout: Int,
    fullScreen: Boolean = false
) : BaseBottomSheetDialogFragment(layout, fullScreen) {

    protected val viewModel: VM by lazy {
        ViewModelProvider(this)[clazz]
    }

    protected companion object {
        val DATA = "DATA"
    }

}