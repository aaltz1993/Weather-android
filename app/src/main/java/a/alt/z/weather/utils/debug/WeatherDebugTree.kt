package a.alt.z.weather.utils.debug

import timber.log.Timber

class WeatherDebugTree: Timber.DebugTree() {

    override fun createStackElementTag(element: StackTraceElement): String {
        return "aaltz::${element.fileName}:${element.lineNumber}::${element.methodName}"
    }
}