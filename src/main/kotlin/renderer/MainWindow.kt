package renderer

import org.lwjgl.glfw.*
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil

class MainWindow(width: Int = 1920, height: Int = 1080) {
    private var width: Int
    private var height: Int
    private var window: Long

    init {
        this.width = width
        this.height = height

        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set()

        // Configure GLFW

        // Configure GLFW
        GLFW.glfwDefaultWindowHints() // optional, the current window hints are already the default

        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4)
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 6)
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE) // the window will stay hidden after creation

        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE) // the window will be resizable

        // Create the window
        window = GLFW.glfwCreateWindow(this.width, this.height, "Hello World!", MemoryUtil.NULL, MemoryUtil.NULL)
        if (window == MemoryUtil.NULL) throw RuntimeException("Failed to create the GLFW window")

        // Set up a key callback. It will be called every time a key is pressed, repeated or released.
        GLFW.glfwSetKeyCallback(
            window
        ) { window: Long, key: Int, scancode: Int, action: Int, mods: Int ->
            if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) GLFW.glfwSetWindowShouldClose(window,
                true) // We will detect this in the rendering loop
        }

        GLFW.glfwSetFramebufferSizeCallback(window) { window: Long, w: Int, h: Int ->
            GL11.glViewport(0, 0, w, h)
            this.width = w
            this.height = h
        }

        // Get the thread stack and push a new frame
        MemoryStack.stackPush().use { stack ->
            val pWidth = stack.mallocInt(1) // int*
            val pHeight = stack.mallocInt(1) // int*

            // Get the window size passed to glfwCreateWindow
            GLFW.glfwGetWindowSize(window, pWidth, pHeight)

            // Get the resolution of the primary monitor
            val vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor())

            // Center the window
            GLFW.glfwSetWindowPos(
                window,
                (vidmode!!.width() - pWidth[0]) / 2,
                (vidmode.height() - pHeight[0]) / 2
            )
        }

        // Make the OpenGL context current
        GLFW.glfwMakeContextCurrent(this.window)
        // Enable v-sync
        GLFW.glfwSwapInterval(1)

        // Make the window visible
        GLFW.glfwShowWindow(this.window)

        GL.createCapabilities()

        GL11.glClearColor(0.1f, 0.1f, 0.0f, 1.0f)
        GL11.glViewport(0, 0, this.width, this.height)
        GL11.glEnable(GL11.GL_DEPTH_TEST)
    }

    fun swapBuffer(){
        GLFW.glfwSwapBuffers(this.window)
    }
}

