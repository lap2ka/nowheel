import org.gradle.api.tasks.bundling.AbstractArchiveTask

plugins {
    id("fabric-loom")
    id("me.modmuss50.mod-publish-plugin")
}

fun prop(key: String): String = project.property(key) as String

val modId = prop("mod_id")
val modName = prop("mod_name")
val modLicense = prop("mod_license")
val modVersion = prop("mod_version")
val modGroupId = prop("mod_group_id")
val loader = prop("loader")

val minecraftVersion = prop("minecraft_version")
val minecraftVersionRange = prop("minecraft_version_range")
val loaderVersionRange = prop("loader_version_range")
val fabricLoaderVersion = prop("fabric_loader_version")
val fabricApiVersion = prop("fabric_api_version")

val createVersion = prop("create_version")
val entitycullingVersion = prop("entityculling_version")
val transitionVersion = prop("transition_version")
val clothConfigVersion = prop("cloth_config_version")
val modmenuVersion = prop("modmenu_version")
val sodiumVersion = prop("sodium_version")
val lithiumVersion = prop("lithium_version")
val indiumVersion = prop("indium_version")

val modrinthId = prop("modrinth_id")
val curseforgeId = prop("curseforge_id")

version = "$modVersion+$minecraftVersion$loader"
group = modGroupId

base { archivesName = modId }

java.toolchain.languageVersion = JavaLanguageVersion.of(prop("java_version").toInt())

loom {
    accessWidenerPath = file("src/main/resources/nowheel.accesswidener")
    mixin {
        useLegacyMixinAp = true
        defaultRefmapName = "nowheel.refmap.json"
    }
    runs.named("client") {
        client()
        runDir = "run"
        ideConfigGenerated(true)
        configName = "Fabric Client ($minecraftVersion)"
        vmArg("-Dsodium.checks.issue2561=false")
    }
}

repositories {
    maven("https://maven.createmod.net/")
    maven("https://mvn.devos.one/releases")
    maven("https://mvn.devos.one/snapshots")
    maven("https://maven.tterrag.com/")
    maven("https://maven.jamieswhiteshirt.com/libs-release")
    maven("https://raw.githubusercontent.com/Fuzss/modresources/main/maven")
    maven("https://jitpack.io/") {
        content { includeGroupAndSubgroups("com.github") }
    }
    maven("https://maven.ithundxr.dev/mirror") {
        content { includeGroup("com.tterrag.registrate") }
    }
    maven("https://api.modrinth.com/maven") {
        content { includeGroup("maven.modrinth") }
    }
    maven("https://maven.tr7zw.dev/repository/maven-snapshots/") {
        content { includeGroup("dev.tr7zw") }
    }
    maven("https://maven.shedaniel.me/") {
        content { includeGroupAndSubgroups("me.shedaniel") }
    }
    mavenCentral()
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")

    modImplementation("com.simibubi.create:create-fabric:$createVersion-mc$minecraftVersion")

    modImplementation("maven.modrinth:entityculling:$entitycullingVersion-fabric,$minecraftVersion")
    modRuntimeOnly("dev.tr7zw:TRansition:$transitionVersion-$minecraftVersion-fabric-SNAPSHOT")

    modImplementation("me.shedaniel.cloth:cloth-config-fabric:$clothConfigVersion") {
        exclude(group = "net.fabricmc.fabric-api")
    }
    modImplementation("maven.modrinth:modmenu:$modmenuVersion")

    modRuntimeOnly("maven.modrinth:sodium:mc$minecraftVersion-$sodiumVersion-fabric")
    modRuntimeOnly("maven.modrinth:lithium:mc$minecraftVersion-$lithiumVersion-fabric")
    modRuntimeOnly("maven.modrinth:indium:$indiumVersion+mc$minecraftVersion")
}

tasks.withType<ProcessResources>().configureEach {
    val replaceProperties = mapOf(
        "minecraft_version" to minecraftVersion,
        "minecraft_version_range" to minecraftVersionRange,
        "loader_version_range" to loaderVersionRange,
        "mod_id" to modId,
        "mod_name" to modName,
        "mod_license" to modLicense,
        "mod_version" to modVersion,
    )
    inputs.properties(replaceProperties)
    filesMatching(listOf("fabric.mod.json")) { expand(replaceProperties) }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release = prop("java_version").toInt()
}

publishMods {
    file = tasks.named<AbstractArchiveTask>("remapJar").flatMap { it.archiveFile }
    type = STABLE
    modLoaders.add(loader)
    changelog = providers.environmentVariable("CHANGELOG").orElse("No changelog provided.")

    displayName = "Nowheel $modVersion for $loader $minecraftVersion"
    version = project.version.toString()

    modrinth {
        projectId = modrinthId
        accessToken = providers.environmentVariable("MODRINTH_TOKEN")
        minecraftVersions.add(minecraftVersion)
        requires { slug = "create-fabric" }
        requires { slug = "entityculling" }
    }

    curseforge {
        projectId = curseforgeId
        accessToken = providers.environmentVariable("CURSEFORGE_TOKEN")
        minecraftVersions.add(minecraftVersion)
        requires { slug = "create-fabric" }
        requires { slug = "entityculling" }
    }
}
