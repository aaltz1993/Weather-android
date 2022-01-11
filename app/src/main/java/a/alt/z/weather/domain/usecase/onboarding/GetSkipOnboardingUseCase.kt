package a.alt.z.weather.domain.usecase.onboarding

import a.alt.z.weather.data.preferences.PreferencesStorage
import a.alt.z.weather.di.IODispatcher
import a.alt.z.weather.domain.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetSkipOnboardingUseCase @Inject constructor(
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val preferencesStorage: PreferencesStorage
): UseCase<Unit, Boolean>(coroutineDispatcher) {

    override suspend fun execute(parameters: Unit): Boolean {
        return preferencesStorage.skipOnboarding
    }
}