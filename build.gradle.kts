val greetingProvider = providers.environmentVariable("TASK_GREETING")

tasks.register<FooTask>("foo") {
    greeting.set(greetingProvider)
    targetFile.set(layout.buildDirectory.file("target.txt"))
}

@CacheableTask
abstract class FooTask : DefaultTask() {
    @get:Input
    abstract val greeting: Property<String>

    @get:OutputFile
    abstract val targetFile: RegularFileProperty

    @TaskAction
    fun execute() {
        val msg = greeting.get()
        targetFile.get().asFile.writeText(msg)
        println(msg)
    }
}