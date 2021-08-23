package a.alt.z.weather.model

enum class FineParticleGrade(level: IntRange) {
    GOOD(IntRange(0, 30)),
    NORMAL(IntRange(31, 80)),
    BAD(IntRange(81, 150)),
    VERY_BAD(IntRange(151, Int.MAX_VALUE))
}