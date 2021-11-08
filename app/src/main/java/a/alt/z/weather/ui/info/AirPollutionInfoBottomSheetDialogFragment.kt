package a.alt.z.weather.ui.info

import a.alt.z.weather.R
import a.alt.z.weather.databinding.FragmentAirPollutionInfoBinding
import a.alt.z.weather.ui.base.BaseFragment
import a.alt.z.weather.utils.extensions.updateWithDelayedTransition
import a.alt.z.weather.utils.extensions.viewBinding
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AirPollutionInfoBottomSheetDialogFragment : BaseFragment(R.layout.fragment_air_pollution_info) {

    private val binding by viewBinding(FragmentAirPollutionInfoBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    override fun initView() {
        binding.apply {
            rootLayout.setOnClickListener { dismissWithTransition() }

            closeButton.setOnClickListener { dismissWithTransition() }

            contentLayout.setOnClickListener { /* block user input */ }
        }
    }

    private fun dismissWithTransition() {
        lifecycleScope.launch {
            binding.rootLayout.updateWithDelayedTransition {
                setGuidelinePercent(R.id.top_guideline, 1F)
                setGuidelinePercent(R.id.bottom_guideline, 2F)
            }

            delay(250L)

            parentFragmentManager.commit { remove(this@AirPollutionInfoBottomSheetDialogFragment) }
        }
    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            delay(100L)

            binding.rootLayout.updateWithDelayedTransition(duration = 500L) {
                setGuidelinePercent(R.id.top_guideline, 0F)
                setGuidelinePercent(R.id.bottom_guideline, 1F)
            }
        }
    }

    override fun onDetach() {
        super.onDetach()

        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }
}