import java.io.ByteArrayOutputStream

plugins {
    application
    alias(libs.plugins.taskTree) // Helps debugging dependencies among gradle tasks
    scala
    // lastest version of kotlin
    kotlin("jvm") version "1.7.0"
}

repositories {
    mavenCentral()
}

dependencies {
    // Check the catalog at gradle/libs.versions.gradle
    implementation(libs.bundles.alchemist)
    // Scala deps
    implementation(libs.upickle)
    implementation(libs.smile)
    implementation(libs.oslib)
    implementation(libs.breezelib)

    // Scalapy
    implementation(libs.scalapy)// https://mvnrepository.com/artifact/org.scala-lang/scala-library
    implementation("com.outr:scribe_2.13:3.11.1")
    implementation("com.outr:scribe-file_2.13:3.11.1")
}
val batch: String by project
val maxTime: String by project
val variables: String by project
val alchemistGroup = "Run Alchemist"
/*
 * This task is used to run all experiments in sequence
 */

// Heap size estimation for batches
val maxHeap: Long? by project
val heap: Long = maxHeap ?: if (System.getProperty("os.name").toLowerCase().contains("linux")) {
    ByteArrayOutputStream().use { output ->
        exec {
            executable = "bash"
            args = listOf("-c", "cat /proc/meminfo | grep MemAvailable | grep -o '[0-9]*'")
            standardOutput = output
        }
        output.toString().trim().toLong() / 1024
    }.also { println("Detected ${it}MB RAM available.") } * 9 / 10
} else {
    // Guess 16GB RAM of which 2 used by the OS
    14 * 1024L
}
val taskSizeFromProject: Int? by project
val taskSize = taskSizeFromProject ?: 512
val threadCount = maxOf(1, minOf(Runtime.getRuntime().availableProcessors(), heap.toInt() / taskSize))

/*
 * This task is used to run all experiments in sequence
 */
val runAllGraphic by tasks.register<DefaultTask>("runAllGraphic") {
    group = alchemistGroup
    description = "Launches all simulations with the graphic subsystem enabled"
}
val runAllBatch by tasks.register<DefaultTask>("runAllBatch") {
    group = alchemistGroup
    description = "Launches all experiments"
}

fun pythonBin(): String = ByteArrayOutputStream().use { outputStream ->
    project.exec {
        commandLine("pyenv",  "which", "python")
        standardOutput = outputStream
    }
    outputStream.toString()
}
/*
 * Scan the folder with the simulation files, and create a task for each one of them.
 */
File(rootProject.rootDir.path + "/src/main/yaml").listFiles()
        ?.filter { it.extension == "yml" }
        ?.sortedBy { it.nameWithoutExtension }
        ?.forEach {
            fun basetask(name: String, additionalConfiguration: JavaExec.() -> Unit = {}) = tasks.register<JavaExec>(name) {
                group = alchemistGroup
                description = "Launches graphic simulation ${it.nameWithoutExtension}"
                mainClass.set("it.unibo.alchemist.Alchemist")
                classpath = sourceSets["main"].runtimeClasspath
                args("-y", it.absolutePath)
                if (System.getenv("CI") == "true") {
                    args("-hl", "-t", "2")
                } else {
                    args("-g", "effects/standard-effect.json")
                }
                jvmArgs(
                        "-Dscalapy.python.library=python3.9",
                        "-Dscalapy.python.programname=${pythonBin().trim()}"
                        //Other required parameters...
                )
                this.additionalConfiguration()
            }
            val capitalizedName = it.nameWithoutExtension.capitalize()
            val graphic by basetask("run${capitalizedName}Graphic")
            runAllGraphic.dependsOn(graphic)
            val batch by basetask("run${capitalizedName}Batch") {
                description = "Launches batch experiments for $capitalizedName"
                maxHeapSize = "${minOf(heap.toInt(), Runtime.getRuntime().availableProcessors() * taskSize)}m"
                File("data").mkdirs()
                args(
                        "-e", "data/${it.nameWithoutExtension}",
                        "-b",
                        "-var", "random",
                        "-t", 20000,
                        "-p", threadCount,
                        "-i", 1
                )
            }
            runAllBatch.dependsOn(batch)
        }
