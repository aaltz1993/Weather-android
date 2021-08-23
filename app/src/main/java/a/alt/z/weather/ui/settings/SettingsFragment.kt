package a.alt.z.weather.ui.settings

import a.alt.z.weather.R
import a.alt.z.weather.databinding.FragmentSettingsBinding
import a.alt.z.weather.ui.base.BaseFragment
import a.alt.z.weather.utils.extensions.viewBinding
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    private val binding by viewBinding(FragmentSettingsBinding::bind)

    private val viewModel: SettingsViewModel by viewModels()

    override fun initView() {

    }

    override fun setupObserver() {

    }
}