package a.alt.z.weather.model

enum class UltraFineParticleGrade(level: IntRange) {
    GOOD(IntRange(0, 15)),
    NORMAL(IntRange(16, 35)),
    BAD(IntRange(36, 75)),
    VERY_BAD(IntRange(76, Int.MAX_VALUE))
}