package a.alt.z.weather.ui.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnboardingPageAdapter(
    fragment: Fragment,
    private val fragments: List<Fragment> = listOf(Onboarding1Fragment(), Onboarding2Fragment(), Onboarding3Fragment())
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}