package org.hydev.mcpm.client.arguments

import org.hydev.mcpm.client.installer.IInstaller
import org.hydev.mcpm.client.installer.input.InstallInput
import org.hydev.mcpm.client.installer.output.InstallResult
import org.hydev.mcpm.client.interaction.ILogger

/**
 * Tests both the InstallParser and InstallController classes (since they are invoked in a similar way).
 * Since these classes require a similar setup, I've moved them together into this class.
 */
class InstallParserTest
{
    /**
     * Provides a mock implementation of the InstallBoundary interface for testing.
     */
    class MockInstaller : IInstaller
    {
        private val inputs: MutableList<InstallInput> = ArrayList()

        override suspend fun install(installInput: InstallInput, log: ILogger): List<InstallResult>
        {
            inputs.add(installInput)
            return listOf(InstallResult(InstallResult.Type.SUCCESS_INSTALLED, installInput.names[0]))
        }
    }

    // private lateinit var installer: MockInstaller
    // private lateinit var controller: InstallController
    // private lateinit var args: ArgsParser
    //
    // /**
    //  * Initializes the various fields (controllers, etc.) before a test starts.
    //  */
    // @BeforeEach
    // fun setup()
    // {
    //     installer = MockInstaller()
    //     val presenter = InstallPresenter()
    //     controller = InstallController(installer)
    //     val parser = InstallParser(controller, presenter)
    //     args = ArgsParser(listOf(parser))
    // }
    //
    // fun assertAcceptable(input: InstallInput, name: String?, load: Boolean)
    // {
    //     Assertions.assertEquals(input.name, name)
    //     Assertions.assertEquals(input.type, SearchPackagesType.BY_NAME)
    //     Assertions.assertEquals(input.load, load) // should we keep this?
    //     Assertions.assertTrue(input.isManuallyInstalled)
    // }
    //
    // /**
    //  * Tests if install parser will correctly fail when no arguments are passed.
    //  */
    // @Test
    // fun testNoArguments() = runBlocking {
    //     val exception = assertThrows<ArgumentParserException> { args.parse(arrayOf("install"), NullLogger()) }
    //     Assertions.assertEquals(exception.message, "too few arguments")
    // }
    //
    // /**
    //  * Tests whether the `install` parser will correctly invoke the boundary with one name.
    //  */
    // @Test
    // @Throws(ArgumentParserException::class)
    // fun testInstallSingleName() = runBlocking {
    //     args.parse(arrayOf("install", "JedCore"), NullLogger())
    //     val inputs = installer.inputs
    //     Assertions.assertEquals(inputs.size, 1)
    //     assertAcceptable(inputs[0], "JedCore", true)
    // }
    //
    // /**
    //  * Tests whether the `install` parser will correctly set the noLoad parameter when the --no-load option is provided.
    //  */
    // @Test
    // @Throws(ArgumentParserException::class)
    // fun testInstallNoLoad() = runBlocking {
    //     args.parse(arrayOf("install", "ABC", "--no-load"), NullLogger())
    //     val inputs = installer.inputs
    //     Assertions.assertEquals(inputs.size, 1)
    //     assertAcceptable(inputs[0], "ABC", false)
    // }
    //
    // /**
    //  * Tests whether the `install` controller will correctly invoke the boundary (with different package types).
    //  */
    // @Test
    // fun testInstallController()
    // {
    //     controller.install("MyPlugin", SearchPackagesType.BY_COMMAND, false)
    //     val inputs = installer.inputs
    //     Assertions.assertEquals(inputs.size, 1)
    //     val input = inputs[0]
    //     Assertions.assertEquals(input.name, "MyPlugin")
    //     Assertions.assertEquals(input.type, SearchPackagesType.BY_COMMAND)
    //     Assertions.assertFalse(input.load)
    //     Assertions.assertTrue(input.isManuallyInstalled)
    // }
}
