package model

import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.opengl.GL46
import org.lwjgl.system.MemoryStack
import java.nio.FloatBuffer

class Shader(vertexShaderCode: String, fragmentShaderCode: String) {
    private val programID: Int

    init {
        val vertexShaderProgram = compileShaderFromString(GL46.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShaderProgram = compileShaderFromString(GL46.GL_FRAGMENT_SHADER, fragmentShaderCode)
        programID = GL46.glCreateProgram()
        linkShaders(programID, vertexShaderProgram, fragmentShaderProgram)
    }

    fun use() {
        GL46.glUseProgram(programID)
    }

    fun getUniformLocation(name: String?): Int {
        return GL46.glGetUniformLocation(programID, name)
    }

    fun uniformMatrix4fv(name: String?, matrix: Matrix4f) {
        MemoryStack.stackPush().use { stack -> uniformMatrix4fv(name, matrix, stack) }
    }

    fun uniformMatrix4fv(name: String?, matrix: Matrix4f, stack: MemoryStack) {
        val buffer = matrix[stack.mallocFloat(16)]
        uniformMatrix4fv(name, buffer)
    }

    fun uniformMatrix4fv(name: String?, buffer: FloatBuffer?) {
        val position = getUniformLocation(name)
        GL46.glUniformMatrix4fv(position, false, buffer)
    }

    fun uniform3fv(name: String?, vector: Vector3f, stack: MemoryStack) {
        val buffer = vector[stack.mallocFloat(3)]
        uniform3fv(name, buffer)
    }

    fun uniform3fv(name: String?, buffer: FloatBuffer?) {
        val position = getUniformLocation(name)
        GL46.glUniform3fv(position, buffer)
    }

    companion object {
        @Throws(Exception::class)
        private fun compileShaderFromString(shaderType: Int, shaderProgram: String): Int {
            val shaderID = GL46.glCreateShader(shaderType)
            if (shaderID == 0) {
                throw Exception("Unable to glCreateShader")
            }
            GL46.glShaderSource(shaderID, shaderProgram)
            GL46.glCompileShader(shaderID)
            if (GL46.glGetShaderi(shaderID, GL46.GL_COMPILE_STATUS) == 0) {
                val log = GL46.glGetShaderInfoLog(shaderID)
                System.out.printf("%s\n", log)
                throw Exception(String.format("shader compile failed: %s", log))
            }
            return shaderID
        }

        @Throws(Exception::class)
        private fun linkShaders(programID: Int, vertexShaderProgram: Int, fragmentShaderProgram: Int) {
            GL46.glAttachShader(programID, vertexShaderProgram)
            GL46.glAttachShader(programID, fragmentShaderProgram)
            GL46.glLinkProgram(programID)
            GL46.glDetachShader(programID, vertexShaderProgram)
            GL46.glDetachShader(programID, fragmentShaderProgram)
            GL46.glDeleteShader(vertexShaderProgram)
            GL46.glDeleteShader(fragmentShaderProgram)
            if (GL46.glGetProgrami(programID, GL46.GL_LINK_STATUS) == 0) {
                System.out.printf("Shader program link error: %s\n", GL46.glGetProgramInfoLog(programID))
                throw Exception("Shader program link error")
            }
        }
    }
}