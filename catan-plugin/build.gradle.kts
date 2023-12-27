val core = project(":${rootProject.name}-core")

repositories {
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    implementation(core)
    implementation("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
    implementation("io.github.monun:heartbeat-coroutines:0.0.5")
    implementation("io.github.monun:tap-api:4.9.8")
    implementation("io.github.monun:kommand-api:3.1.7")
}

val pluginName = rootProject.name.split('-').joinToString("")
val packageName = rootProject.name.replace("-", "")
extra.set("pluginName", pluginName)
extra.set("packageName", packageName)

tasks {
    processResources {
        filesMatching("*.yml") {
            expand(project.properties)
            expand(extra.properties)
        }
    }

    create<Jar>("paperJar") {
        archiveBaseName.set(pluginName)
        archiveVersion.set("")

        (listOf(project, core) + core.subprojects).forEach {
            from(it.sourceSets["main"].output)
        }

        doLast {
            copy {
                from(archiveFile)
                val plugins = File(rootDir, ".server/plugins-reobf/")
                val runPlugins = File(rootDir, "run/plugins/")
                into(if (File(plugins, archiveFileName.get()).exists()) File(plugins, "update") else plugins)
                into(runPlugins)
            }

            val update = File(".server/plugins-reobf/update")

            update.mkdirs()
            File(update, "RELOAD").delete()
        }
    }
}