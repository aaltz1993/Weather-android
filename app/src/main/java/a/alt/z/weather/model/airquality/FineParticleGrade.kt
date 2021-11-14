package a.alt.z.weather.model.airquality

import a.alt.z.weather.R

enum class FineParticleGrade {
    GOOD,
    NORMAL,
    BAD,
    VERY_BAD;

    companion object {
        fun levelOf(level: Int): FineParticleGrade {
            return when (level) {
                in 0..30 -> GOOD
                in 31..80 -> NORMAL
                in 81..150 -> BAD
                in 151..Int.MAX_VALUE -> VERY_BAD
                else -> throw IllegalArgumentException()
            }
        }

        fun codeOf(code: Int): FineParticleGrade {
            return when (code) {
                1 -> GOOD
                2 -> NORMAL
                3 -> BAD
                4 -> VERY_BAD
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
                (level / 30F * (80F / 3)) + min
            }
            /* 30 - 50 */
            NORMAL -> {
                ((level - 30) / 50F * (80F / 3)) + min + (80F / 3)
            }
            /* 50 - 70 */
            BAD -> {
                ((level - 80) / 70F * (80F / 3)) + min + (80F / 3 * 2)
            }
            /* 90 */
            VERY_BAD -> {
                max
            }
        }
    }

    fun getColorResId(): Int {
        return when(this) {
            GOOD -> R.color.grade_good
            NORMAL -> R.color.grade_normal
            BAD -> R.color.grade_bad
            VERY_BAD -> R.color.grade_very_bad
        }
    }

    fun getDescription(): String {
        return when(this) {
            GOOD -> "좋음"
            NORMAL -> "보통"
            BAD -> "나쁨"
            VERY_BAD -> "매우 나쁨"
        }
    }
}