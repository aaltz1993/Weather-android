package a.alt.z.weather.ui.location

import a.alt.z.weather.R
import a.alt.z.weather.databinding.FragmentPreviewBinding
import a.alt.z.weather.model.location.Location
import a.alt.z.weather.model.weather.elements.Precipitation
import a.alt.z.weather.model.weather.elements.Sky
import a.alt.z.weather.ui.base.BaseFragment
import a.alt.z.weather.utils.extensions.updateWithDelayedTransition
import a.alt.z.weather.utils.extensions.viewBinding
import a.alt.z.weather.utils.glide.PrecipitationSnowTransformation
import a.alt.z.weather.utils.result.successOrNull
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PreviewFragment : BaseFragment(R.layout.fragment_preview) {

    private val viewModel: LocationViewModel by viewModels(ownerProducer = { requireParentFragment() })

    private val binding by viewBinding(FragmentPreviewBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    override fun initView() {
        binding.apply {
            cancelButton.setOnClickListener { dismissWithTransition() }

            contentLayout.setOnClickListener { /* block user input */ }
        }
    }

    private fun dismissWithTransition() {
        lifecycleScope.launch {
            binding.rootLayout.updateWithDelayedTransition {
                setGuidelinePercent(R.id.top_guideline, 1F)
                setGuidelinePercent(R.id.bottom_guideline, 1.9F)
            }

            delay(250L)

            parentFragmentManager.commit { remove(this@PreviewFragment) }
        }
    }

    override fun setupObserver() {
        viewModel.previewPresentWeather.observe(viewLifecycleOwner) { result ->
            result.successOrNull()?.let {
                val address = it.address
                val presentWeather = it.weather

                binding.apply {
                    rootLayout.updateWithDelayedTransition(duration = 500L) {
                        setGuidelinePercent(R.id.top_guideline, 0.1F)
                        setGuidelinePercent(R.id.bottom_guideline, 1F)
                    }

                    loadingIndicator.isVisible = false

                    addButton.setOnClickListener { viewModel.addLocation(address) }

                    addressTextView.text = address.name
                    descriptionTextView.text = descriptionOf(presentWeather.sky, presentWeather.precipitationType)
                    weatherIconImageView.setImageResource(iconResIdOf(presentWeather.sky, presentWeather.precipitationType))

                    currentTemperatureTextView.text = presentWeather.temperature.toString()

                    fineParticleGradeTextView.text = presentWeather.fineParticleGrade.getDescription()
                    fineParticleGradeTextView.setTextColor(ContextCompat.getColor(requireContext(), presentWeather.fineParticleGrade.getColorResId()))

                    ultraFineParticleGradeTextView.text = presentWeather.ultraFineParticleGrade.getDescription()
                    ultraFineParticleGradeTextView.setTextColor(ContextCompat.getColor(requireContext(), presentWeather.ultraFineParticleGrade.getColorResId()))

                    val resId = imageResIdOf(
                        presentWeather.sky,
                        presentWeather.precipitationType,
                        presentWeather.temperature
                    )

                    weatherImageView.setImageResource(resId)

                    if (presentWeather.precipitation > 0F) {
                        precipitationOrSnowLayout.isVisible = true

                        when (presentWeather.precipitationType) {
                            Precipitation.RAIN, Precipitation.RAIN_SNOW, Precipitation.SHOWER -> {
                                precipitationOrSnowImageView.setImageResource(R.drawable.icon_rainfall)
                                precipitationOrSnowUnitTextView.text = "(mm)"
                            }
                            Precipitation.SNOW -> {
                                precipitationOrSnowImageView.setImageResource(R.drawable.icon_snowflake)
                                precipitationOrSnowUnitTextView.text = "(cm)"
                            }
                        }

                        precipitationOrSnowTextView.text = presentWeather.precipitation.toInt().toString()

                        Glide.with(requireContext())
                            .load(R.drawable.foreground_precipitation_snow)
                            .transform(PrecipitationSnowTransformation())
                            .into(precipitationOrSnowForegroundView)
                    } else {
                        precipitationOrSnowLayout.isGone = true
                    }
                }
            }
        }

        viewModel.addLocationResult.observe(viewLifecycleOwner) { result ->
            result.successOrNull()?.let {
                dismissWithTransition()
            }
        }
    }

    private fun imageResIdOf(sky: Sky, precipitation: Precipitation, temperature: Int): Int {
        return when (precipitation) {
            Precipitation.NONE -> {
                when (sky) {
                    Sky.CLEAR -> {
                        when {
                            temperature <= 5 -> R.drawable.image_clear_very_cold
                            temperature in 6..11 -> R.drawable.image_clear_cold
                            temperature in 12..22 -> R.drawable.image_clear_warm
                            else -> R.drawable.image_clear_warm
                        }
                    }
                    Sky.CLOUDY -> {
                        when {
                            temperature <= 5 -> R.drawable.image_cloudy_very_cold
                            temperature in 6..11 -> R.drawable.image_cloudy_cold
                            temperature in 12..22 -> R.drawable.image_cloudy_warm
                            else -> R.drawable.image_cloudy_warm
                        }
                    }
                    Sky.OVERCAST -> {
                        when {
                            temperature <= 5 -> R.drawable.image_overcast_very_cold
                            temperature in 6..11 -> R.drawable.image_overcast_cold
                            temperature in 12..22 -> R.drawable.image_overcast_warm
                            else -> R.drawable.image_overcast_warm
                        }
                    }
                }
            }
            Precipitation.RAIN -> {
                when {
                    temperature <= 5 -> R.drawable.image_rain_very_cold
                    temperature in 6..11 -> R.drawable.image_rain_cold
                    temperature in 12..22 -> R.drawable.image_rain_warm
                    else -> R.drawable.image_rain_warm
                }
            }
            Precipitation.SNOW -> R.drawable.image_snow
            Precipitation.RAIN_SNOW -> R.drawable.image_rain_snow
            Precipitation.SHOWER -> {
                when {
                    temperature <= 5 -> R.drawable.image_shower_very_cold
                    temperature in 6..11 -> R.drawable.image_shower_cold
                    temperature in 12..22 -> R.drawable.image_shower_warm
                    else -> R.drawable.image_shower_warm
                }
            }
        }
    }

    private fun descriptionOf(sky: Sky, precipitation: Precipitation): String {
        return when (precipitation) {
            Precipitation.NONE -> {
                when (sky) {
                    Sky.CLEAR -> "맑음"
                    Sky.CLOUDY -> "구름 많음"
                    Sky.OVERCAST -> "흐림"
                }
            }
            Precipitation.RAIN -> "비"
            Precipitation.SNOW -> "눈"
            Precipitation.RAIN_SNOW -> "비, 눈"
            Precipitation.SHOWER -> "소나기"
        }
    }

    private fun iconResIdOf(sky: Sky, precipitation: Precipitation): Int {
        return when (precipitation) {
            Precipitation.NONE -> {
                when (sky) {
                    Sky.CLEAR -> R.drawable.icon_clear_outlined
                    Sky.CLOUDY -> R.drawable.icon_cloudy_outlined
                    Sky.OVERCAST -> R.drawable.icon_overcast_outlined
                }
            }
            Precipitation.RAIN -> R.drawable.icon_rain_outlined
            Precipitation.SNOW -> R.drawable.icon_snow_outlined
            Precipitation.RAIN_SNOW -> R.drawable.icon_rain_snow_outlined
            Precipitation.SHOWER -> R.drawable.icon_shower_outlined
        }
    }

    override fun onDetach() {
        super.onDetach()

        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    override fun onBackPressed(): Boolean {
        dismissWithTransition()
        return true
    }
}