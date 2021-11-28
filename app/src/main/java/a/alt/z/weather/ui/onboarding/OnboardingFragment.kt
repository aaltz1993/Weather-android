package a.alt.z.weather.ui.onboarding

import a.alt.z.weather.R
import a.alt.z.weather.databinding.FragmentOnboardingBinding
import a.alt.z.weather.ui.base.BaseFragment
import a.alt.z.weather.utils.extensions.update
import a.alt.z.weather.utils.extensions.viewBinding
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

class OnboardingFragment: BaseFragment(R.layout.fragment_onboarding) {

    private val binding by viewBinding(FragmentOnboardingBinding::bind)

    override fun initView() {
        binding.apply {
            (viewPager.getChildAt(0) as? RecyclerView)?.overScrollMode = View.OVER_SCROLL_NEVER
//            viewPager.isUserInputEnabled = false
            viewPager.adapter = OnboardingPageAdapter(this@OnboardingFragment)
            viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    binding.apply {
                        if (position == 0) {
                            skipButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.onboarding_skip_text_color))
                            nextButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                        } else {
                            skipButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.onboarding_skip_night_text_color))
                            nextButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                        }

                        pageIndicator1.isSelected = position == 0
                        pageIndicator2.isSelected = position == 1
                        pageIndicator3.isSelected = position == 2

                        if (position > 0) {
                            val selectedBackgroundTint = ContextCompat.getColorStateList(requireContext(), R.color.onboarding_selected_background_color)
                            val notSelectedBackgroundTint = ContextCompat.getColorStateList(requireContext(), R.color.onboarding_not_selected_background_color)
                            pageIndicator2.backgroundTintList = if (position == 1) selectedBackgroundTint else notSelectedBackgroundTint
                            pageIndicator3.backgroundTintList = if (position == 2) selectedBackgroundTint else notSelectedBackgroundTint
                        }

                        pageIndicatorsLayout.update {
                            val selectedWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 28F, resources.displayMetrics)
                            val notSelectedWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4F, resources.displayMetrics)

                            constrainWidth(R.id.page_indicator1, if (position == 0) selectedWidth.toInt() else notSelectedWidth.toInt())
                            constrainWidth(R.id.page_indicator2, if (position == 1) selectedWidth.toInt() else notSelectedWidth.toInt())
                            constrainWidth(R.id.page_indicator3, if (position == 2) selectedWidth.toInt() else notSelectedWidth.toInt())
                        }
                    }
                }
            })

            pageIndicator1.isSelected = true

            nextButton.setOnClickListener {
                viewPager.setCurrentItem(viewPager.currentItem + 1, true)
            }
        }
    }
}