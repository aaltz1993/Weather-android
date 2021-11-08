package a.alt.z.weather.ui.weather

import a.alt.z.weather.R
import a.alt.z.weather.databinding.FragmentWeatherBinding
import a.alt.z.weather.model.location.Location
import a.alt.z.weather.ui.base.BaseFragment
import a.alt.z.weather.ui.location.PreviewFragment
import a.alt.z.weather.utils.extensions.setFragmentResult
import a.alt.z.weather.utils.extensions.viewBinding
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherFragment : BaseFragment(R.layout.fragment_weather) {

    private val binding by viewBinding(FragmentWeatherBinding::bind)

    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val location = requireArguments().getParcelable<Location>(ARG_LOCATION)

        requireNotNull(location)

        viewModel.setLocation(location)
    }

    override fun initView() {
        binding.apply {
            (viewPager.getChildAt(0) as? RecyclerView)?.overScrollMode = View.OVER_SCROLL_NEVER
            viewPager.adapter = WeatherPageAdapter(this@WeatherFragment)
            viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)

                    setFragmentResult(
                        "currentItemRequestKey",
                        "currentItem",
                        if (positionOffset > 0) 1
                        else position
                    )
                }
            })
        }
    }

    override fun setupObserver() {
        super.setupObserver()

        setFragmentResultListener("isPageableRequestKey") { _, result ->
            binding.viewPager.isUserInputEnabled = result.getBoolean("isPageable", false)
        }

        viewModel.isDataReady.observe(viewLifecycleOwner) { isDataReady ->
            setFragmentResult("isDataReadyRequestKey", "isDataReady", isDataReady)
        }
    }

    companion object {
        private const val ARG_LOCATION = "arg_location"

        fun newInstance(location: Location)  = WeatherFragment().apply {
            arguments = bundleOf(Pair(ARG_LOCATION, location))
        }
    }
}