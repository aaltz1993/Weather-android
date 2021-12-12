package a.alt.z.weather.ui.location

import a.alt.z.weather.R
import a.alt.z.weather.databinding.FragmentPreviewBinding
import a.alt.z.weather.model.weather.elements.PrecipitationType
import a.alt.z.weather.model.weather.elements.Sky
import a.alt.z.weather.ui.base.BaseFragment
import a.alt.z.weather.utils.extensions.updateWithDelayedTransition
import a.alt.z.weather.utils.extensions.viewBinding
import a.alt.z.weather.utils.transformations.PrecipitationSnowTransformation
import a.alt.z.weather.utils.result.successOrNull
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
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
                            PrecipitationType.SNOW -> {
                                precipitationOrSnowImageView.setImageResource(R.drawable.icon_snowflake)
                                precipitationOrSnowUnitTextView.text = context?.getString(R.string.unit_snow)

                                val percentage = when (presentWeather.precipitation) {
                                    in 0F..1F -> 0.1F
                                    in 1F..5F -> {
                                        0.1F + (0.6F * (presentWeather.precipitation - 1F) / 4F)
                                    }
                                    else -> 0.7F
                                }

                                Glide.with(requireContext())
                                    .load(R.drawable.foreground_precipitation_snow)
                                    .transform(PrecipitationSnowTransformation(percentage))
                                    .into(precipitationOrSnowForegroundView)
                            }
                            else -> {
                                precipitationOrSnowImageView.setImageResource(R.drawable.icon_rainfall)
                                precipitationOrSnowUnitTextView.text = context?.getString(R.string.unit_rain)

                                val percentage = when (presentWeather.precipitation) {
                                    in 0F..1F -> 0.1F
                                    in 1F..30F -> 0.3F + (0.2F * (presentWeather.precipitation - 1F) / 29F)
                                    in 30F..50F -> 0.5F
                                    else -> 0.7F
                                }

                                Glide.with(requireContext())
                                    .load(R.drawable.foreground_precipitation_snow)
                                    .transform(PrecipitationSnowTransformation(percentage))
                                    .into(precipitationOrSnowForegroundView)
                            }
                        }
                        /* TODO */
                        precipitationOrSnowTextView.text = "${presentWeather.precipitation}"
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

    private fun imageResIdOf(sky: Sky, precipitationType: PrecipitationType, temperature: Int): Int {
        return when (precipitationType) {
            PrecipitationType.NONE -> {
                when (sky) {
                    Sky.CLEAR -> {
                        when {
                            temperature <= 5 -> R.drawable.image_clear_5
                            temperature in 6..11 -> R.drawable.image_clear_10_11
                            temperature in 12..22 -> R.drawable.image_clear_17_19
                            else -> R.drawable.image_clear_17_19
                        }
                    }
                    Sky.CLOUDY -> {
                        when {
                            temperature <= 5 -> R.drawable.image_cloudy_5
                            temperature in 6..11 -> R.drawable.image_cloudy_10_11
                            temperature in 12..22 -> R.drawable.image_cloudy_17_19
                            else -> R.drawable.image_cloudy_17_19
                        }
                    }
                    Sky.OVERCAST -> {
                        when {
                            temperature <= 5 -> R.drawable.image_overcast_5
                            temperature in 6..11 -> R.drawable.image_overcast_10_11
                            temperature in 12..22 -> R.drawable.image_overcast_17_19
                            else -> R.drawable.image_overcast_17_19
                        }
                    }
                }
            }
            PrecipitationType.RAIN -> {
                when {
                    temperature <= 5 -> R.drawable.image_rain_very_cold
                    temperature in 6..11 -> R.drawable.image_rain_cold
                    temperature in 12..22 -> R.drawable.image_rain_warm
                    else -> R.drawable.image_rain_warm
                }
            }
            PrecipitationType.SNOW -> R.drawable.image_snow
            PrecipitationType.RAIN_SNOW -> R.drawable.image_rain_snow
            PrecipitationType.SHOWER -> {
                when {
                    temperature <= 5 -> R.drawable.image_shower_very_cold
                    temperature in 6..11 -> R.drawable.image_shower_cold
                    temperature in 12..22 -> R.drawable.image_shower_warm
                    else -> R.drawable.image_shower_warm
                }
            }
        }
    }

    private fun descriptionOf(sky: Sky, precipitationType: PrecipitationType): String {
        return when (precipitationType) {
            PrecipitationType.NONE -> {
                when (sky) {
                    Sky.CLEAR -> "맑음"
                    Sky.CLOUDY -> "구름 많음"
                    Sky.OVERCAST -> "흐림"
                }
            }
            PrecipitationType.RAIN -> "비"
            PrecipitationType.SNOW -> "눈"
            PrecipitationType.RAIN_SNOW -> "비, 눈"
            PrecipitationType.SHOWER -> "소나기"
        }
    }

    private fun iconResIdOf(sky: Sky, precipitationType: PrecipitationType): Int {
        return when (precipitationType) {
            PrecipitationType.NONE -> {
                when (sky) {
                    Sky.CLEAR -> R.drawable.icon_clear_outlined
                    Sky.CLOUDY -> R.drawable.icon_cloudy_outlined
                    Sky.OVERCAST -> R.drawable.icon_overcast_outlined
                }
            }
            PrecipitationType.RAIN -> R.drawable.icon_rain_outlined
            PrecipitationType.SNOW -> R.drawable.icon_snow_outlined
            PrecipitationType.RAIN_SNOW -> R.drawable.icon_rain_snow_outlined
            PrecipitationType.SHOWER -> R.drawable.icon_shower_outlined
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