package com.ilhom.core_ui.view

import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModelProvider
import com.ilhom.core_ui.vm.BaseViewModel

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