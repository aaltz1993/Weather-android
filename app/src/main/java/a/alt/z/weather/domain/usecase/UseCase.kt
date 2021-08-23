package a.alt.z.weather.domain.usecase

import a.alt.z.weather.utils.result.Result
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

abstract class UseCase<in Param, Type>(private val coroutineDispatcher: CoroutineDispatcher)  {

    suspend operator fun invoke(
        parameters: Param,
        result: MutableLiveData<Result<Type>>
    ) {
        result.postValue(Result.Loading)

        try {
            val data = withContext(coroutineDispatcher) { execute(parameters) }
            result.postValue(Result.Success(data))
        } catch (exception: Exception) {
            result.postValue(Result.Failure(exception))
        }
    }

    suspend operator fun invoke(parameters: Param): LiveData<Result<Type>> {
        val result: MutableLiveData<Result<Type>> = MutableLiveData()
        this(parameters, result)
        return result
    }

    protected abstract suspend fun execute(parameters: Param): Type
}