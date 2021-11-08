package a.alt.z.weather.domain.usecase

import a.alt.z.weather.utils.result.Result
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import timber.log.debug

abstract class UseCase<in Param, Type>(private val coroutineDispatcher: CoroutineDispatcher)  {

    suspend operator fun invoke(
        parameters: Param,
        result: MutableLiveData<Result<Type>>
    ) {
        result.postValue(Result.Loading)
        Timber.debug { "${this::class.java.simpleName} loading" }

        try {
            val data = withContext(coroutineDispatcher) { execute(parameters) }
            result.postValue(Result.Success(data))
            Timber.debug { "${this::class.java.simpleName} succeeded" }
        } catch (exception: Exception) {
            result.postValue(Result.Failure(exception))
            Timber.debug(throwable = exception) { "${this::class.java.simpleName} failed" }
        }
    }

    suspend operator fun invoke(parameters: Param): LiveData<Result<Type>> {
        val result: MutableLiveData<Result<Type>> = MutableLiveData()
        this(parameters, result)
        return result
    }

    protected abstract suspend fun execute(parameters: Param): Type
}

suspend operator fun <Type> UseCase<Unit, Type>.invoke(): LiveData<Result<Type>> = this(Unit)

suspend operator fun <Type> UseCase<Unit, Type>.invoke(result: MutableLiveData<Result<Type>>) = this(Unit, result)