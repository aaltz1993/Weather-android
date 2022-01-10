package a.alt.z.weather.ui.splash

import a.alt.z.weather.R
import a.alt.z.weather.databinding.FragmentSplashBinding
import a.alt.z.weather.ui.base.BaseFragment
import a.alt.z.weather.ui.main.MainViewModel
import a.alt.z.weather.utils.constants.RequestKeys
import a.alt.z.weather.utils.constants.ResultKeys
import a.alt.z.weather.utils.extensions.updateWithDelayedTransition
import a.alt.z.weather.utils.extensions.viewBinding
import a.alt.z.weather.utils.result.successOrNull
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintSet.*
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class SplashFragment : BaseFragment(R.layout.fragment_splash) {

    private val binding by viewBinding(FragmentSplashBinding::bind)

    private val viewModel: MainViewModel by viewModels(ownerProducer = { requireActivity() })

    private var dataLoaded = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    override fun initView() {
    }

    override fun setupObserver() {
        parentFragmentManager.setFragmentResultListener(RequestKeys.DATA_LOADED_SPLASH, viewLifecycleOwner) { _, _ ->
            lifecycleScope.launchWhenResumed {
                delay(500L)

                parentFragmentManager.commit { remove(this@SplashFragment) }
            }
        }

        viewModel.locations.observe(viewLifecycleOwner) { result ->
            result.successOrNull()?.let { locations ->
            }
        }
    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            delay(500L)

            binding.loadingLayout.isVisible = true

            parentFragmentManager.setFragmentResultListener(RequestKeys.DATA_LOADED_SPLASH, viewLifecycleOwner) { _, _ ->
                lifecycleScope.launchWhenResumed {
                    delay(500L)
                    parentFragmentManager.commit { remove(this@SplashFragment) }
                }
            }
        }
    }

    override fun onDetach() {
        super.onDetach()

        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }
}