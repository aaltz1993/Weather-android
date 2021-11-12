package a.alt.z.weather.ui.location

import a.alt.z.weather.R
import a.alt.z.weather.databinding.FragmentLocationBinding
import a.alt.z.weather.model.location.Address
import a.alt.z.weather.model.location.Location
import a.alt.z.weather.ui.base.BaseFragment
import a.alt.z.weather.utils.extensions.hideKeyboard
import a.alt.z.weather.utils.extensions.permissionsGranted
import a.alt.z.weather.utils.extensions.showKeyboard
import a.alt.z.weather.utils.extensions.viewBinding
import a.alt.z.weather.utils.result.Result
import a.alt.z.weather.utils.result.successOrNull
import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import timber.log.debug
import java.util.*

@AndroidEntryPoint
@SuppressLint("MissingPermission")
class LocationFragment : BaseFragment(R.layout.fragment_location) {

    private val binding by viewBinding(FragmentLocationBinding::bind)

    private val viewModel: LocationViewModel by viewModels()

    private val locationProvider: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireContext())
    }

    private val permissions = arrayOf(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION)

    private val requestPermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions(), ::onActivityResult)

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
                if (queryEditText.hasFocus()) {
                    idleState()
                } else {
                    requireActivity().onBackPressed()
                }
            }

            editButton.setOnClickListener {
                viewModel.toggleEditState()
            }

            queryEditText.setOnFocusChangeListener { _, hasFocus ->
                val isEditing = viewModel.isEditing.value

                if (isEditing == true) {
                    idleState()
                } else {
                    if (hasFocus) searchAddressState()
                    else queryEditText.hideKeyboard()
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
                idleState()
            }

            locationRecyclerView.adapter = locationAdapter
            locationRecyclerView.layoutManager = LinearLayoutManager(requireContext())

            addressRecyclerView.adapter = addressAdapter
            addressRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun idleState() {
        binding.apply {
            backButton.isVisible = true
            editButton.text = requireContext().getText(R.string.edit)
            editButton.isVisible = true
            guideMessageTextView.isVisible = true
            queryEditText.clearFocus()
            queryEditText.text.clear()
            cancelButton.isVisible = false
            locationAdapter.isEditing = false
            locationRecyclerView.isVisible = true
            addressAdapter.submitList(emptyList())
            addressRecyclerView.isVisible = false
            locationServiceLayout.isVisible = false

            TransitionManager.beginDelayedTransition(rootLayout)
        }
    }

    private fun searchAddressState() {
        binding.apply {
            backButton.isVisible = false
            editButton.isVisible = false
            guideMessageTextView.isVisible = false
            cancelButton.isVisible = true
            locationRecyclerView.isVisible = false
            addressRecyclerView.isVisible = true

            TransitionManager.beginDelayedTransition(rootLayout)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun setupObserver() {
        super.setupObserver()

        viewModel.isEditing.observe(viewLifecycleOwner) { isEditing ->
            binding.apply {
                backButton.isVisible = !isEditing
                editButton.text = requireContext().getString(
                    if (isEditing) R.string.done
                    else R.string.edit
                )
                guideMessageTextView.isVisible = !isEditing

                locationAdapter.isEditing = isEditing
                locationServiceLayout.isVisible = isEditing
            }
        }

        viewModel.locations.observe(viewLifecycleOwner) { result ->
            result.successOrNull()?.let { locations ->
                locationAdapter.submitList(locations.sortedByDescending { it.isDeviceLocation })
                viewModel.getPresentWeathersByLocations(locations)
            }
        }

        viewModel.searchAddressResult.observe(viewLifecycleOwner) { result ->
            result.successOrNull()?.let {
                addressAdapter.submitList(it.ifEmpty { listOf(Address.INVALID) })
            }
        }

        viewModel.addLocationResult.observe(viewLifecycleOwner) { result ->
            result.successOrNull()?.let {
                idleState()
            }
        }

        viewModel.presentWeathersByLocations.observe(viewLifecycleOwner) { result ->
            result.successOrNull()?.let { presentWeathersByLocations ->
                locationAdapter.presentWeathersByLocations = presentWeathersByLocations.sortedByDescending { it.location.isDeviceLocation }
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

                    binding.locationServiceSwitchButton.isChecked = false
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

        if (binding.queryEditText.hasFocus()) {
            idleState()
            return true
        }

        if (viewModel.locations.value?.successOrNull().orEmpty().isEmpty()) {
            Timber.debug { "emptyLocations" }
            /* exit application */
            return false
        }

        parentFragmentManager.commit { remove(this@LocationFragment) }

        return true
    }
}