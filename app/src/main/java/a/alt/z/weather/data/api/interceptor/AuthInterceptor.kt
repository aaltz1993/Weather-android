package a.alt.z.weather.data.api.interceptor

import a.alt.z.weather.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val httpUrl = chain.request()
            .url
            .newBuilder()
            .addQueryParameter("serviceKey", BuildConfig.SERVICE_KEY)
            .build()

        val request = chain.request()
            .newBuilder()
            .url(url = httpUrl)
            .build()

        return chain.proceed(request)
    }
}