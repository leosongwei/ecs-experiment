package renderer

import common.ResourceFile
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL11

fun main() {
    val mainWindow = MainWindow()
    val resourceManager = ResourceFile()

    GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

    val texture = Texture("tiles.png")

    val shader = Shader(
        resourceManager.getAsString("shaders/vs.glsl"),
        resourceManager.getAsString("shaders/fs.glsl")
    )
    shader.use()

    val vertices = floatArrayOf( // x, y, u, v
        -0.9f, 0.9f, 0.0f, 1.0f,
        0.9f, 0.9f, 1.0f, 1.0f,
        -0.9f, -0.9f, 0.0f, 0.0f,
        0.9f, -0.9f, 1.0f, 0.0f
    )
    val eboIndices = intArrayOf(0, 3, 1, 0, 2, 3)


    while (true) {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)
        mainWindow.swapBuffers()
        GLFW.glfwPollEvents()
    }
}