repositories {
    maven {
        url = uri("https://my.repo.org")
        credentials {
            username = "user"
            password = System.getenv("REPO_PASSWORD")
        }
    }
}

val configEnvProvider = providers.environmentVariable("CONFIG_SECRET")
val taskEnvProvider = providers.environmentVariable("TASK_SECRET")

tasks.register<FooTask>("foo") {
    greetingConf.set(configEnvProvider.get()) // Calling 'get()' forces this to be a configuration input
    greetingTask.set(taskEnvProvider) // Not a configuration input
    targetFile.set(layout.buildDirectory.file("target.txt"))
}

@CacheableTask
abstract class FooTask : DefaultTask() {
    @get:Input
    abstract val greetingConf: Property<String>

    @get:Input
    abstract val greetingTask: Property<String>

    @get:OutputFile
    abstract val targetFile: RegularFileProperty

    @TaskAction
    fun execute() {
        val msg = greetingConf.get() + ':' + greetingTask.get()
        targetFile.get().asFile.writeText(msg)
        println(msg)
    }
}