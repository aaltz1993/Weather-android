package a.alt.z.weather.ui.weather

import a.alt.z.weather.R
import a.alt.z.weather.databinding.FragmentPresentWeatherBinding
import a.alt.z.weather.model.weather.elements.PrecipitationType
import a.alt.z.weather.model.weather.elements.Sky
import a.alt.z.weather.ui.base.BaseFragment
import a.alt.z.weather.ui.location.LocationFragment
import a.alt.z.weather.utils.extensions.pixelsOf
import a.alt.z.weather.utils.extensions.updateWithDelayedTransition
import a.alt.z.weather.utils.extensions.viewBinding
import a.alt.z.weather.utils.glide.PrecipitationSnowTransformation
import a.alt.z.weather.utils.result.successOrNull
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId

@AndroidEntryPoint
class PresentWeatherFragment : BaseFragment(R.layout.fragment_present_weather) {

    private val binding by viewBinding(FragmentPresentWeatherBinding::bind)

    private val viewModel: WeatherViewModel by viewModels(ownerProducer = { parentFragment as Fragment })

    private val notificationMessages = listOf(
        "코로나로 힘겨운 요즘인데\n평온한 하루 보내세요 \uD83D\uDE4F",
        "\uD83D\uDE37 마스크로 답답하지만,\n마음만은 상쾌한 한 주 보내세요",
        "항상 감기 조심하고\n따뜻한 하루 보내세요!"
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun initView() {
        binding.apply {
            plusButton.setOnClickListener {
                requireActivity()
                    .supportFragmentManager
                    .commit { replace(R.id.fragment_container, LocationFragment()) }
            }

            notificationOnImageView.setOnClickListener {
                if (notificationContentTextView.isVisible) {
                    notificationContentTextView.animate()
                        .setDuration(500L)
                        .alpha(0F)
                        .withEndAction {
                            notificationContentTextView.isVisible = false

                            notificationOnImageView.setImageDrawable(
                                AppCompatResources.getDrawable(requireContext(), R.drawable.icon_notification)
                            )
                        }
                        .start()

                    rootLayout.updateWithDelayedTransition(duration = 500L) {
                        setMargin(
                            R.id.notification_on_image_view,
                            ConstraintSet.END,
                            0
                        )
                    }
                } else {
                    notificationContentTextView.animate()
                        .withStartAction { notificationContentTextView.isVisible = true }
                        .setDuration(500L)
                        .alpha(1F)
                        .start()

                    rootLayout.updateWithDelayedTransition(duration = 500L) {
                        setMargin(
                            R.id.notification_on_image_view,
                            ConstraintSet.END,
                            requireContext().pixelsOf(48).toInt()
                        )

                        notificationOnImageView.setImageDrawable(
                            AppCompatResources.getDrawable(requireContext(), R.drawable.icon_notification_gray)
                        )
                    }
                }
            }

            notificationContentTextView.text = notificationMessages.random()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun setupObserver() {
        viewModel.location.observe(viewLifecycleOwner) { location ->
            binding.addressTextView.text = location.name
        }

        viewModel.presentWeather.observe(viewLifecycleOwner) { result ->
            result.successOrNull()?.let { presentWeather ->
                binding.apply {
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
                            PrecipitationType.RAIN, PrecipitationType.RAIN_SNOW, PrecipitationType.SHOWER -> {
                                precipitationOrSnowImageView.setImageResource(R.drawable.icon_rainfall)
                                precipitationOrSnowUnitTextView.text = "(mm)"
                            }
                            PrecipitationType.SNOW -> {
                                precipitationOrSnowImageView.setImageResource(R.drawable.icon_snowflake)
                                precipitationOrSnowUnitTextView.text = "(cm)"
                            }
                        }

                        precipitationOrSnowTextView.text = "${presentWeather.precipitation}"

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

        viewModel.forecastWeather.observe(viewLifecycleOwner) { result ->
            result.successOrNull()?.let { forecastWeather ->
                forecastWeather.dailyWeathers
                    .find { it.date == LocalDate.now(ZoneId.of("Asia/Seoul")) }
                    ?.let {
                        binding.apply {
                            minTemperatureTextView.text = "${it.minTemperature}°"
                            maxTemperatureTextView.text = "${it.maxTemperature}°"
                        }
                    }
            }
        }
    }

    private fun imageResIdOf(sky: Sky, precipitationType: PrecipitationType, temperature: Int): Int {
        return when (precipitationType) {
            PrecipitationType.NONE -> {
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
}