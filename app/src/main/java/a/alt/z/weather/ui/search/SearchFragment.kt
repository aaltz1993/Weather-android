package a.alt.z.weather.ui.search

import a.alt.z.weather.R
import a.alt.z.weather.databinding.FragmentSearchBinding
import a.alt.z.weather.ui.base.BaseFragment
import a.alt.z.weather.utils.extensions.viewBinding
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment(R.layout.fragment_search) {

    private val binding by viewBinding(FragmentSearchBinding::bind)

    private val viewModel: SearchViewModel by viewModels()

    override fun initView() {

    }

    override fun setupObserver() {

    }

}