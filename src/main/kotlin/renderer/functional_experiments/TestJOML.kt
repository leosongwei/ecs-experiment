package renderer.functional_experiments

import org.joml.Matrix4f
import org.joml.Vector4f

fun main() {
    val vector = Vector4f(0f, 0f, 2f, 1f)
    val ortho = Matrix4f().ortho(
        -(16f / 9f),
        (16f / 9f),
        -1f,
        1f,
        1f,
        -1f
    )
    val out = vector.mul(ortho)
    println(out)
}