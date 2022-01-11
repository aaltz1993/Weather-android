package a.alt.z.weather.ui.weather

import a.alt.z.weather.R
import a.alt.z.weather.databinding.FragmentPresentWeatherBinding
import a.alt.z.weather.domain.usecase.weather.BackwardServerException
import a.alt.z.weather.model.weather.PresentWeather
import a.alt.z.weather.model.weather.elements.PrecipitationType
import a.alt.z.weather.model.weather.elements.Sky
import a.alt.z.weather.ui.base.BaseFragment
import a.alt.z.weather.ui.location.LocationFragment
import a.alt.z.weather.utils.extensions.pixelsOf
import a.alt.z.weather.utils.extensions.showToast
import a.alt.z.weather.utils.extensions.updateWithDelayedTransition
import a.alt.z.weather.utils.extensions.viewBinding
import a.alt.z.weather.utils.result.Result
import a.alt.z.weather.utils.transformations.PrecipitationSnowTransformation
import a.alt.z.weather.utils.result.successOrNull
import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import timber.log.Timber

@AndroidEntryPoint
class PresentWeatherFragment : BaseFragment(R.layout.fragment_present_weather) {

    private val binding by viewBinding(FragmentPresentWeatherBinding::bind)

    private val viewModel: WeatherViewModel by viewModels(ownerProducer = { parentFragment as Fragment })

    private val notificationMessages = listOf(
        "코로나로 힘겨운 요즘인데\n평온한 하루 보내세요 \uD83D\uDE4F",
        "\uD83D\uDE37 마스크로 답답하지만,\n마음만은 상쾌한 한 주 보내세요",
        "항상 감기 조심하고\n따뜻한 하루 보내세요!"
    )

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
            when (result) {
                Result.Loading -> {}
                is Result.Success -> bindPresentWeather(result.data)
                is Result.Failure -> {
                    if (result.exception is BackwardServerException) {
                        showToast { "네트워크 오류로 인해 기상 데이터를 불러오는데 실패하였습니다 :(" }
                    } else {
                        val location = requireNotNull(viewModel.location.value)

                        viewModel.getPresentWeatherBackward(location)
                    }
                }
            }
        }

        viewModel.forecastWeather.observe(viewLifecycleOwner) { result ->
            result.successOrNull()?.let { forecastWeather ->
                val dailyWeather = forecastWeather.dailyWeathers.find { it.date == LocalDate.now(ZoneId.of("Asia/Seoul")) }
                if (dailyWeather != null) {
                    binding.apply {
                        minTemperatureTextView.text = "${dailyWeather.minTemperature}°"
                        maxTemperatureTextView.text = "${dailyWeather.maxTemperature}°"
                    }
                }
            }
        }
    }

    private fun bindPresentWeather(presentWeather: PresentWeather) {
        binding.apply {
            descriptionTextView.text = presentWeather.description
            weatherIconImageView.setImageResource(presentWeather.iconResourceId)

            currentTemperatureTextView.text = presentWeather.temperature.toString()
            Timber.d("${currentTemperatureTextView.typeface}, ${ResourcesCompat.getFont(requireContext(), R.font.lato_light)}")

            fineParticleGradeTextView.text = presentWeather.fineParticleGrade.getDescription()
            fineParticleGradeTextView.setTextColor(ContextCompat.getColor(requireContext(), presentWeather.fineParticleGrade.getColorResId()))

            ultraFineParticleGradeTextView.text = presentWeather.ultraFineParticleGrade.getDescription()
            ultraFineParticleGradeTextView.setTextColor(ContextCompat.getColor(requireContext(), presentWeather.ultraFineParticleGrade.getColorResId()))

            weatherImageView.setImageResource(presentWeather.imageResourceId)

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