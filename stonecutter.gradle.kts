plugins {
    id("dev.kikugie.stonecutter")
    id("me.modmuss50.mod-publish-plugin") version "0.8.4" apply false
    id("net.neoforged.moddev") version "2.0.146" apply false
    id("net.neoforged.moddev.legacyforge") version "2.0.146" apply false
    id("fabric-loom") version "1.15.5" apply false
}

stonecutter active "1.21.1-neoforge" /* [SC] DO NOT EDIT */

stonecutter parameters {
    val loader = current.project.substringAfterLast('-')
    constants.match(loader, "fabric", "forge", "neoforge")
    constants["forgelike"] = loader != "fabric"

    dependencies["minecraft"] = current.version
}

stonecutter tasks {
    order("publishModrinth")
    order("publishCurseforge")
}

tasks.register("chiseledBuild") {
    group = "stonecutter"
    dependsOn(stonecutter.tasks.named("build"))
}

tasks.register("chiseledPublishMods") {
    group = "stonecutter"
    dependsOn(stonecutter.tasks.named("publishMods"))
}

tasks.register("runActiveClient") {
    group = "stonecutter"
    dependsOn("${stonecutter.current!!.project}:runClient")
}
