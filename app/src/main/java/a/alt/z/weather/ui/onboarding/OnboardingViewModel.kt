package a.alt.z.weather.ui.onboarding

import a.alt.z.weather.domain.usecase.onboarding.SetSkipOnboardingUseCase
import a.alt.z.weather.utils.result.Result
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val setSkipOnboardingUseCase: SetSkipOnboardingUseCase
): ViewModel() {

    private val _setSkipOnboarding = MutableLiveData<Result<Unit>>()
    val setSkipOnboarding: LiveData<Result<Unit>> = _setSkipOnboarding

    fun skipOnboarding() {
        viewModelScope.launch { setSkipOnboardingUseCase(true, _setSkipOnboarding) }
    }
}