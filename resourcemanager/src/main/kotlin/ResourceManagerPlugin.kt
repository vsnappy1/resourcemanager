import com.android.build.gradle.BaseExtension
import dev.randos.resourcemanager.ResourceManagerGenerator
import dev.randos.resourcemanager.manager.ModuleManager
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

/**
 * A Gradle plugin that simplifies accessing Android resources (e.g., strings, colors, drawables)
 * in both Android and non-Android components (e.g., ViewModel) using generated code.
 * This plugin generates a Kotlin file containing resource access code and configures the build
 * process to ensure the generated code is included in the project's source sets.
 */
class ResourceManagerPlugin : Plugin<Project> {
    override fun apply(project: Project) {

        // Define the location of the generated Kotlin file containing resource management code.
        val mainDirectory = File(project.projectDir, "build/generated/source/resourcemanager/main")
        val namespacePath = ModuleManager(project.projectDir).getNamespace()?.replace(".","/")
        val generatedFile = File(mainDirectory, "$namespacePath/ResourceManager.kt")

        // Initialize the ResourceManagerGenerator responsible for generating the Kotlin code.
        val resourceManager = ResourceManagerGenerator(project.projectDir, generatedFile)

        // Register a new Gradle task to generate the ResourceManager code.
        val generateResourceManagerTask = project.tasks.register("generateResourceManager") {

            // Define the input and output files for the task. This ensures that the task is only executed
            // when there is a change in any of the input files or the output file, optimizing the build process
            // by avoiding unnecessary task executions.
            inputs.files(resourceManager.getFilesUnderObservation())
            outputs.files(generatedFile)
            doLast {
                resourceManager.generate()
            }
        }

        // Ensure that the "generateResourceManager" task runs before any compile task.
        project.tasks.matching { it.name.startsWith("compile") }
            .configureEach {
                dependsOn(generateResourceManagerTask)
            }

        // Add the generated file to the Kotlin source sets so that it can be used as part of the build.
        project.extensions.getByType(BaseExtension::class.java)
            .sourceSets
            .getByName("main")
            .kotlin
            .srcDir(mainDirectory)
    }
}