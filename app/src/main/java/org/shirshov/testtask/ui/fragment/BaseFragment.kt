package org.shirshov.testtask.ui.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import org.shirshov.testtask.ui.activity.mainActivity
import org.shirshov.testtask.util.extension.Ui
import org.shirshov.testtask.util.extension.toStringRes

abstract class BaseFragment : Fragment() {

    // Avoiding IllegalStateException (impossible to access viewModels of detached fragments)
    protected var onInitBlock: () -> Unit = {}

    override fun onCreate(savedInstanceState: Bundle?) {
        onInitBlock()
        super.onCreate(savedInstanceState)
    }

    protected fun setTitle(titleId: Int) = setTitle(titleId.toStringRes())

    protected fun setTitle(title: String) {
        mainActivity.title = title
    }

    fun showToast(stringId: Int) = Ui.showToast(stringId)

    fun showToast(text: String) = Ui.showToast(text)

    fun showBack(show: Boolean) = mainActivity.showBack(show)

    fun <T : ViewDataBinding> setupLifecycle(observer: LifecycleObserver, binding: T): T {
        lifecycle.addObserver(observer)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding
    }

    fun initLoader(source: LiveData<Boolean>, container: FrameLayout) {
        val progressBar = createProgressBar(container)
        source.observe(viewLifecycleOwner) { progressBar.visibility = if (it) View.VISIBLE else View.GONE }
    }

    private fun createProgressBar(container: FrameLayout): ProgressBar {
        val progressBar = ProgressBar(container.context)
        progressBar.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER)
        container.addView(progressBar)
        return progressBar
    }
}
