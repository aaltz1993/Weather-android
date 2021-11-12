package a.alt.z.weather.ui.splash

import a.alt.z.weather.R
import a.alt.z.weather.databinding.FragmentSplashBinding
import a.alt.z.weather.ui.base.BaseFragment
import a.alt.z.weather.utils.extensions.updateWithDelayedTransition
import a.alt.z.weather.utils.extensions.viewBinding
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintSet.*
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : BaseFragment(R.layout.fragment_splash) {

    private val binding by viewBinding(FragmentSplashBinding::bind)

    private var animationEnded = false
    private var dataLoaded = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    override fun initView() {
    }

    override fun setupObserver() {
        parentFragmentManager.setFragmentResultListener("dataLoadedRequestKey", viewLifecycleOwner) { _, result ->
            dataLoaded = result.getBoolean("dataLoaded")

            if (animationEnded && dataLoaded) {
                parentFragmentManager.commit { remove(this@SplashFragment) }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            delay(100L)

            binding.rootLayout.updateWithDelayedTransition(duration = 1000L) {
                connect(R.id.sun_image_view, START, R.id.root_layout, START)
                connect(R.id.sun_image_view, END, R.id.root_layout, END)
                clear(R.id.sun_image_view, TOP)
                connect(R.id.sun_image_view, BOTTOM, R.id.center_y_guideline, TOP, 4)

                connect(R.id.cloud_image_view, START, R.id.root_layout, START)
                connect(R.id.cloud_image_view, END, R.id.root_layout, END)
                connect(R.id.cloud_image_view, TOP, R.id.center_y_guideline, BOTTOM, 4)
                clear(R.id.cloud_image_view, BOTTOM)
            }

            delay(1100L)

            binding.loadingLayout.isVisible = true

            animationEnded = true

            if (animationEnded && dataLoaded) {
                parentFragmentManager.commit { remove(this@SplashFragment) }
            }
        }
    }

    override fun onDetach() {
        super.onDetach()

        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }
}