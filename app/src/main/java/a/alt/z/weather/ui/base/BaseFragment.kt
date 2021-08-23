package a.alt.z.weather.ui.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment {

    constructor(): super()

    constructor(@LayoutRes contentLayoutId: Int): super(contentLayoutId)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        setupObserver()
    }

    protected abstract fun initView()

    protected open fun setupObserver() {}

    /**
     * Returns: return true if fragment consumed backPressed
     */
    open fun onBackPressed(): Boolean {
        return false
    }
}