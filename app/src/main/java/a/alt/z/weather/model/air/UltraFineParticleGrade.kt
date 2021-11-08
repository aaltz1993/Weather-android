package a.alt.z.weather.model.air

import a.alt.z.weather.R

enum class UltraFineParticleGrade {
    GOOD,
    NORMAL,
    BAD,
    VERY_BAD;

    companion object {
        fun levelOf(level: Int): UltraFineParticleGrade {
            return when (level) {
                in 0..15 -> GOOD
                in 16..35 -> NORMAL
                in 36..75 -> BAD
                in 76..Int.MAX_VALUE -> VERY_BAD
                else -> throw IllegalArgumentException()
            }
        }
    }

    fun percentageOf(level: Int): Float {
        val min = 10F
        val max = 90F

        return when(this) {
            /* 10 - 30 */
            GOOD -> {
                (level / 15F * (80F / 3)) + min
            }
            /* 30 - 50 */
            NORMAL -> {
                ((level - 15) / 20F * (80F / 3)) + min + (80F / 3)
            }
            /* 50 - 70 */
            BAD -> {
                ((level - 35) / 40F * (80F / 3)) + min + (80F / 3 * 2)
            }
            /* 90 */
            VERY_BAD -> {
                max
            }
        }
    }

    fun getColorResId(): Int {
        return when (this) {
            GOOD -> R.color.grade_good
            NORMAL -> R.color.grade_normal
            BAD -> R.color.grade_bad
            VERY_BAD -> R.color.grade_very_bad
        }
    }

    fun getDescription(): String {
        return when (this) {
            GOOD -> "좋음"
            NORMAL -> "보통"
            BAD -> "나쁨"
            VERY_BAD -> "매우 나쁨"
        }
    }
}