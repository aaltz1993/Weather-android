package a.alt.z.weather.domain.usecase.onboarding

import a.alt.z.weather.data.preferences.PreferencesStorage
import a.alt.z.weather.di.IODispatcher
import a.alt.z.weather.domain.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class SetSkipOnboardingUseCase @Inject constructor(
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val preferencesStorage: PreferencesStorage
): UseCase<Boolean, Unit>(coroutineDispatcher) {

    override suspend fun execute(parameters: Boolean) {
        preferencesStorage.skipOnboarding = parameters
    }
}