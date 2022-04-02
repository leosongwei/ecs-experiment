package renderer

import common.ResourceFile
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL11

fun main() {
    val mainWindow = MainWindow()
    val resourceManager = ResourceFile()

    val shader = Shader(
        resourceManager.getAsString("shaders/vs.glsl"),
        resourceManager.getAsString("shaders/fs.glsl")
    )

    while (true) {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)
        mainWindow.swapBuffers()
        GLFW.glfwPollEvents()
    }
}