package a.alt.z.weather.ui.onboarding

import a.alt.z.weather.R
import a.alt.z.weather.databinding.FragmentOnboarding1Binding
import a.alt.z.weather.ui.base.BaseFragment
import a.alt.z.weather.utils.extensions.viewBinding
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Onboarding1Fragment : BaseFragment(R.layout.fragment_onboarding1) {

    private val binding by viewBinding(FragmentOnboarding1Binding::bind)

    override fun initView() {
        binding.apply {

        }
    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            delay(250L)

            binding.apply {
                familyImageView.animate()
                    .alpha(1F)
                    .translationY(0F)
                    .setDuration(500L)
                    .start()

                onboardingDescriptionTextView.animate()
                    .alpha(1F)
                    .translationY(0F)
                    .setDuration(500L)
                    .start()

                onboardingTitleTextView.animate()
                    .alpha(1F)
                    .setDuration(500L)
                    .start()
            }
        }
    }
}