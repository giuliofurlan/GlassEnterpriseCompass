package com.giufu.compass.model


class Azimuth(_degrees: Float) {

    operator fun plus(degrees: Float) = Azimuth(this.degrees + degrees)

    val degrees = normalizeAngle(_degrees)

    private fun normalizeAngle(angleInDegrees: Float): Float {
        return (angleInDegrees + 360f) % 360f
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Azimuth

        if (degrees != other.degrees) return false

        return true
    }

    override fun hashCode(): Int {
        return degrees.hashCode()
    }

    override fun toString(): String {
        return "Azimuth(degrees=$degrees)"
    }
}

private data class SemiClosedFloatRange(val fromInclusive: Float, val toExclusive: Float)

private operator fun SemiClosedFloatRange.contains(value: Float) = fromInclusive <= value && value < toExclusive
private infix fun Float.until(to: Float) = SemiClosedFloatRange(this, to)
