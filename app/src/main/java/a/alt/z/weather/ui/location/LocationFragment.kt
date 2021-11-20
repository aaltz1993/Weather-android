package a.alt.z.weather.ui.location

import a.alt.z.weather.R
import a.alt.z.weather.databinding.FragmentLocationBinding
import a.alt.z.weather.model.location.Address
import a.alt.z.weather.model.location.Location
import a.alt.z.weather.ui.base.BaseFragment
import a.alt.z.weather.utils.extensions.*
import a.alt.z.weather.utils.result.Result
import a.alt.z.weather.utils.result.successOrNull
import android.Manifest.permission.*
import android.animation.LayoutTransition
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import timber.log.debug
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class LocationFragment : BaseFragment(R.layout.fragment_location) {

    private val binding by viewBinding(FragmentLocationBinding::bind)

    private val viewModel: LocationViewModel by viewModels()

    @Inject lateinit var locationProvider: FusedLocationProviderClient

    private val permissions = arrayOf(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION)

    private val requestPermissions = registerForActivityResult(RequestMultiplePermissions(), ::onActivityResult)

    @SuppressLint("MissingPermission")
    private fun onActivityResult(result: Map<String, Boolean>) {
        if (requireContext().permissionsGranted(permissions.toList())) {
            locationProvider.lastLocation
                .addOnSuccessListener { viewModel.addDeviceLocation(it.latitude, it.longitude) }
                .addOnFailureListener { /* TODO */ }
        } else {
            binding.locationServiceSwitchButton.isChecked = false
        }
    }

    private val locationAdapter = LocationAdapter(::onLocationDeleteClick)

    private fun onLocationDeleteClick(location: Location) {
        viewModel.deleteLocation(location)
    }

    private val addressAdapter = AddressAdapter(::onAddressClick)

    private fun onAddressClick(address: Address) {
        viewModel.getPreviewPresentWeather(address)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun initView() {
        binding.apply {
            backButton.setOnClickListener {
                requireActivity().onBackPressed()
            }

            editButton.setOnClickListener {
                if (viewModel.uiState.value == UIState.EDIT) {
                    viewModel.onIdleState()
                } else {
                    viewModel.onEditLocation()
                }
            }

            queryEditText.setOnFocusChangeListener { _, hasFocus ->
                Timber.debug { "hasFocus::$hasFocus, uiState::${viewModel.uiState.value}" }
                if (viewModel.uiState.value == UIState.SEARCH) {
                    if (hasFocus) queryEditText.showKeyboard()
                    else queryEditText.hideKeyboard()
                } else {
                    if (hasFocus) {
                        viewModel.onSearchAddress()
                    } else {
                        queryEditText.hideKeyboard()
                    }
                }
            }

            queryEditText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    viewModel.searchAddress(queryEditText.text.toString())
                    queryEditText.clearFocus()
                }

                actionId == EditorInfo.IME_ACTION_SEARCH
            }

            queryEditText.doAfterTextChanged {
                clearQueryImageView.isVisible = it.toString().isNotEmpty()
            }

            clearQueryImageView.setOnClickListener {
                queryEditText.text.clear()
                queryEditText.showKeyboard()
            }

            cancelButton.setOnClickListener {
                viewModel.onIdleState()
            }

            locationRecyclerView.adapter = locationAdapter
            locationRecyclerView.layoutManager = LinearLayoutManager(requireContext())

            addressRecyclerView.adapter = addressAdapter
            addressRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    @SuppressLint("MissingPermission")
    override fun setupObserver() {
        super.setupObserver()

        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            binding.apply {
                rootLayout.layoutTransition = if (uiState != UIState.SEARCH) {
                    LayoutTransition()
                } else {
                    null
                }

                backButton.isVisible = uiState == UIState.IDLE
                editButton.isVisible = uiState != UIState.SEARCH
                editButton.text = if (uiState == UIState.EDIT) {
                    requireContext().getText(R.string.done)
                } else {
                    requireContext().getText(R.string.edit)
                }
                guideMessageTextView.isVisible = uiState == UIState.IDLE

                if (uiState == UIState.IDLE) {
                    queryEditText.clearFocus()
                    queryEditText.text.clear()
                } else if (uiState == UIState.SEARCH) {
                    queryEditText.showKeyboard()
                }

                cancelButton.isVisible = uiState == UIState.SEARCH

                locationAdapter.isEditing = uiState == UIState.EDIT
                locationRecyclerView.isVisible = uiState != UIState.SEARCH

                if (uiState == UIState.IDLE) addressAdapter.submitList(emptyList())
                addressRecyclerView.isVisible = uiState == UIState.SEARCH

                locationServiceLayout.isVisible = uiState == UIState.EDIT
            }
        }

        viewModel.locations.observe(viewLifecycleOwner) { result ->
            result.successOrNull()?.let { locations ->
                Timber.debug { "deviceLocation?${locations.any { it.isDeviceLocation }}" }
                locations
                    .sortedByDescending { it.isDeviceLocation }
                    .let { locationAdapter.submitList(it) }

                viewModel.getPresentWeathersByLocations(locations)

                if (!locations.any { it.isDeviceLocation }) {
                    viewModel.toggleLocationService(false)
                }
            }
        }

        viewModel.searchAddressResult.observe(viewLifecycleOwner) { result ->
            result.successOrNull()?.let { addresses ->
                addresses
                    .ifEmpty { listOf(Address.INVALID) }
                    .let { addressAdapter.submitList(it) }
            }
        }

        viewModel.addLocationResult.observe(viewLifecycleOwner) { result ->
            result.successOrNull()?.let {
                viewModel.onIdleState()
            }
        }

        viewModel.presentWeathersByLocations.observe(viewLifecycleOwner) { result ->
            result.successOrNull()?.let { presentWeathersByLocations ->
                presentWeathersByLocations
                    .sortedByDescending { it.location.isDeviceLocation }
                    .let { locationAdapter.presentWeathersByLocations = it }
            }
        }

        viewModel.previewPresentWeather.observe(viewLifecycleOwner) { result ->
            when (result) {
                Result.Loading -> {
                    childFragmentManager.commit { replace(R.id.fragment_container, PreviewFragment()) }
                }
                is Result.Success -> {
                }
                is Result.Failure -> {
                    /* TODO */
                    childFragmentManager
                        .findFragmentById(R.id.fragment_container)
                        ?.let { childFragmentManager.commit { remove(it) } }
                }
            }
        }

        viewModel.addDeviceLocationResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                Result.Loading -> {
                }
                is Result.Success -> {
                    viewModel.toggleLocationService(true)
                }
                is Result.Failure -> {
                    viewModel.toggleLocationService(false)
                }
            }
        }

        viewModel.locationServiceOn.observe(viewLifecycleOwner) { result ->
            result.successOrNull()?.let { locationServiceOn ->
                binding.apply {
                    locationServiceSwitchButton.isChecked = locationServiceOn

                    locationServiceSwitchButton.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            if (requireContext().permissionsGranted(permissions.toList())) {
                                locationProvider.lastLocation
                                    .addOnSuccessListener { viewModel.addDeviceLocation(it.latitude, it.longitude) }
                                    .addOnFailureListener { /* TODO() */ }
                            } else {
                                requestPermissions.launch(permissions)
                            }
                        } else {
                            viewModel.deleteDeviceLocation()
                            viewModel.toggleLocationService(false)
                        }
                    }
                }
            }
        }
    }

    override fun onBackPressed(): Boolean {
        childFragmentManager.findFragmentById(R.id.fragment_container)
            ?.let { it as? BaseFragment }
            ?.takeIf { it.onBackPressed() }
            ?.let { return true }

        if (viewModel.uiState.value != UIState.IDLE) {
            viewModel.onIdleState()
            return true
        }

        if (viewModel.locations.value?.successOrNull().orEmpty().isEmpty()) {
            /* exit application */
            return false
        }

        parentFragmentManager.commit { remove(this@LocationFragment) }

        return true
    }
}