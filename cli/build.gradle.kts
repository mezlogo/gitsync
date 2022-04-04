plugins {
    id("mezlogo.native-build")
    id("mezlogo.cli-build")
}

dependencies {
    implementation(project(":core"))
}

application {
    mainClass.set("mezlogo.gitsync.cli.MainCommand")
    applicationName = "gitsync"
}

graalvmNative {
    toolchainDetection.set(false)
    binaries {
        named("main") {
            imageName.set("gitsync")
            mainClass.set("mezlogo.gitsync.cli.MainCommand")
            debug.set(true)
            sharedLibrary.set(false)
            verbose.set(true)
            useFatJar.set(false)
        }
    }
}
