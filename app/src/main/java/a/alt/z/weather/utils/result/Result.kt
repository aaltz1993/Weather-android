package a.alt.z.weather.utils.result

sealed class Result<out R> {

    object Loading: Result<Nothing>()

    data class Success<out T>(val data: T): Result<T>()

    data class Failure(val exception: Exception): Result<Nothing>()
}

val Result<*>.succeeded get() = this is Result.Success && data != null

fun <T> Result<T>.successOrNull(): T? = (this as? Result.Success<T>)?.data

fun <T> Result<T>.successOr(fallback: T): T = (this as? Result.Success<T>)?.data ?: fallback