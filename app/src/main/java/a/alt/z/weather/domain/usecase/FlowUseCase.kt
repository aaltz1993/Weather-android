package a.alt.z.weather.domain.usecase

import a.alt.z.weather.utils.result.Result
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*

abstract class FlowUseCase<in Param, Type>(private val coroutineDispatcher: CoroutineDispatcher) {

    suspend operator fun invoke(
        parameters: Param,
        result: MutableLiveData<Result<Type>>
    ) {
        execute(parameters)
            .onStart { result.postValue(Result.Loading) }
            .catch {
                Firebase.crashlytics.recordException(it)
                result.postValue(Result.Failure(Exception(it)))
            }
            .flowOn(coroutineDispatcher)
            .collect { result.postValue(Result.Success(it)) }
    }

    suspend operator fun invoke(parameters: Param): LiveData<Result<Type>> {
        val result: MutableLiveData<Result<Type>> = MutableLiveData()
        this(parameters, result)
        return result
    }

    protected abstract suspend fun execute(parameters: Param): Flow<Type>
}

suspend operator fun <Type> FlowUseCase<Unit, Type>.invoke(): LiveData<Result<Type>> = this(Unit)

suspend operator fun <Type> FlowUseCase<Unit, Type>.invoke(result: MutableLiveData<Result<Type>>) = this(Unit, result)