package renderer.functional_experiments

import common.GameMap
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL46
import renderer.MainWindow
import renderer.Shader
import renderer.TextureAtlas
import utils.ResourceFile

fun main() {
    val mainWindow = MainWindow()
    val resourceManager = ResourceFile()

    GL46.glClearColor(0.0f, 0.2f, 0.0f, 0.0f)
    GL46.glEnable(GL46.GL_DEPTH_TEST)
    GL46.glEnable(GL46.GL_BLEND);
    GL46.glBlendFunc(GL46.GL_SRC_ALPHA, GL46.GL_ONE_MINUS_SRC_ALPHA)

    val shader = Shader(
        resourceManager.getAsString("shaders/vs.glsl"),
        resourceManager.getAsString("shaders/fs.glsl")
    )
    shader.setUp()
    shader.use()

    val ortho = Matrix4f().ortho(
        -(16f / 9f),
        (16f / 9f),
        -1f,
        1f,
        1f,
        -1f
    )
    val view = Matrix4f().scale(0.3f).translate(
        Vector3f(0.0f, 0.0f, 0f).negate()
    )
    shader.uniformMatrix4fv("projection", ortho)
    shader.uniformMatrix4fv("view", view)

    val textureAtlas = TextureAtlas("tiles.png")
    textureAtlas.setUp()
    textureAtlas.bind(shader)

    val map = GameMap("test_map.png")
    map.setUpRenderData()

    while (true) {
        GL46.glClear(GL46.GL_COLOR_BUFFER_BIT or GL46.GL_DEPTH_BUFFER_BIT)
        map.render(shader)

        mainWindow.swapBuffers()
        GLFW.glfwPollEvents()
    }
}