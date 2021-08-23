package a.alt.z.weather.ui.main

import a.alt.z.weather.R
import a.alt.z.weather.databinding.FragmentMainBinding
import a.alt.z.weather.model.HourlyForecast
import a.alt.z.weather.ui.base.BaseFragment
import a.alt.z.weather.utils.extensions.viewBinding
import a.alt.z.weather.utils.result.successOrNull
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber
import timber.log.debug
import java.text.SimpleDateFormat

@AndroidEntryPoint
class MainFragment : BaseFragment(R.layout.fragment_main) {

    private val binding by viewBinding(FragmentMainBinding::bind)

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun initView() {
        binding.viewPager.adapter = MainViewPagerAdapter(this)
    }

    override fun setupObserver() {

    }
}