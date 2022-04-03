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
    var scale = 2f / 256f
    val viewCenter = Vector3f(128f, 128f, 0f)
    val textureAtlas = TextureAtlas("tiles.png")
    textureAtlas.setUp()
    textureAtlas.bind(shader)

    val map = GameMap("test_map.png")
    map.setUpRenderData()

    fun handleKeys() {
        if (mainWindow.getKeyPressed(GLFW.GLFW_KEY_KP_ADD)) {
            scale *= 1.1f
        }
        if (mainWindow.getKeyPressed(GLFW.GLFW_KEY_KP_SUBTRACT)) {
            scale *= 0.9f
        }
        if (mainWindow.getKeyPressed(GLFW.GLFW_KEY_UP)) {
            viewCenter.y += (1f / scale) * 0.05f
        }
        if (mainWindow.getKeyPressed(GLFW.GLFW_KEY_DOWN)) {
            viewCenter.y -= (1f / scale) * 0.05f
        }
        if (mainWindow.getKeyPressed(GLFW.GLFW_KEY_RIGHT)) {
            viewCenter.x += (1f / scale) * 0.05f
        }
        if (mainWindow.getKeyPressed(GLFW.GLFW_KEY_LEFT)) {
            viewCenter.x -= (1f / scale) * 0.05f
        }
    }

    while (!mainWindow.shouldClose()) {
        handleKeys()
        val view = Matrix4f().scale(scale).translate(
            Vector3f(viewCenter).negate()
        )
        shader.uniformMatrix4fv("projection", ortho)
        shader.uniformMatrix4fv("view", view)

        GL46.glClear(GL46.GL_COLOR_BUFFER_BIT or GL46.GL_DEPTH_BUFFER_BIT)
        map.render(shader)

        mainWindow.swapBuffers()
        GLFW.glfwPollEvents()
    }
}

