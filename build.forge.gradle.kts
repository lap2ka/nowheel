plugins {
    id("java-library")
    id("net.neoforged.moddev.legacyforge")
    id("me.modmuss50.mod-publish-plugin")
}

fun prop(key: String): String = project.property(key) as String

val modId = prop("mod_id")
val modName = prop("mod_name")
val modLicense = prop("mod_license")
val modVersion = prop("mod_version")
val modDescription = prop("mod_description")
val modGroupId = prop("mod_group_id")
val loader = prop("loader")

val minecraftVersion = prop("minecraft_version")
val minecraftVersionRange = prop("minecraft_version_range")
val loaderVersionRange = prop("loader_version_range")
val forgeVersion = prop("forge_version")
val forgeVersionRange = prop("forge_version_range")

val createVersion = prop("create_version")
val ponderVersion = prop("ponder_version")
val flywheelVersion = prop("flywheel_version")
val registrateVersion = prop("registrate_version")
val entitycullingVersion = prop("entityculling_version")
val clothConfigVersion = prop("cloth_config_version")
val embeddiumVersion = prop("embeddium_version")
val radiumVersion = prop("radium_version")

val modrinthId = prop("modrinth_id")
val curseforgeId = prop("curseforge_id")

version = "$modVersion+$minecraftVersion$loader"
group = modGroupId

base { archivesName = modId }

java.toolchain.languageVersion = JavaLanguageVersion.of(prop("java_version").toInt())

val forgeArtifact = "$minecraftVersion-$forgeVersion"
val clientRunName = "Forge Client ($minecraftVersion)"

legacyForge {
    setVersion(forgeArtifact)

    accessTransformers.from(file("src/main/resources/META-INF/accesstransformer.cfg"))
    validateAccessTransformers = true

    parchment {
        minecraftVersion = prop("parchment_minecraft")
        mappingsVersion = prop("parchment_mappings")
    }

    runs {
        register("client") {
            client()
            gameDirectory = file("run")
            ideName = clientRunName
            systemProperty("forge.logging.markers", "REGISTRIES")
            systemProperty("forge.logging.console.level", "debug")
        }
    }

    mods {
        register(modId) {
            sourceSet(sourceSets["main"])
        }
    }
}

mixin {
    add(sourceSets["main"], "nowheel.refmap.json")
    config("nowheel.mixins.json")
}

repositories {
    maven("https://maven.createmod.net/")
    maven("https://maven.ithundxr.dev/mirror") {
        content { includeGroup("com.tterrag.registrate") }
    }
    maven("https://api.modrinth.com/maven") {
        content { includeGroup("maven.modrinth") }
    }
    mavenCentral()
}

dependencies {
    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")
    compileOnly("io.github.llamalad7:mixinextras-common:0.3.5")
    annotationProcessor("io.github.llamalad7:mixinextras-common:0.3.5")

    modImplementation("com.simibubi.create:create-$minecraftVersion:$createVersion:slim") { isTransitive = false }
    modImplementation("net.createmod.ponder:Ponder-Forge-$minecraftVersion:$ponderVersion")
    compileOnly("dev.engine-room.flywheel:flywheel-forge-api-$minecraftVersion:$flywheelVersion")
    modImplementation("dev.engine-room.flywheel:flywheel-forge-$minecraftVersion:$flywheelVersion")
    modImplementation("com.tterrag.registrate:Registrate:$registrateVersion")

    modImplementation("maven.modrinth:entityculling:$entitycullingVersion-forge,$minecraftVersion")
    modImplementation("maven.modrinth:cloth-config:$clothConfigVersion+forge")

    modRuntimeOnly("maven.modrinth:embeddium:$embeddiumVersion+mc$minecraftVersion")
    modRuntimeOnly("maven.modrinth:radium:$radiumVersion")
}

tasks.withType<ProcessResources>().configureEach {
    val replaceProperties = mapOf(
        "minecraft_version" to minecraftVersion,
        "minecraft_version_range" to minecraftVersionRange,
        "forge_version" to forgeVersion,
        "forge_version_range" to forgeVersionRange,
        "loader_version_range" to loaderVersionRange,
        "mod_id" to modId,
        "mod_name" to modName,
        "mod_license" to modLicense,
        "mod_version" to modVersion,
        "mod_description" to modDescription,
        "ec_min_version" to entitycullingVersion,
    )
    inputs.properties(replaceProperties)
    filesMatching(listOf("META-INF/mods.toml")) { expand(replaceProperties) }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.named<Jar>("jar") {
    manifest {
        attributes("MixinConfigs" to "nowheel.mixins.json")
    }
}

tasks.named("createMinecraftArtifacts") {
    dependsOn(tasks.named("stonecutterGenerate"))
}

publishMods {
    file = tasks.named<Jar>("reobfJar").flatMap { it.archiveFile }
    type = STABLE
    modLoaders.add(loader)
    changelog = providers.environmentVariable("CHANGELOG").orElse("No changelog provided.")

    displayName = "Nowheel $modVersion for $loader $minecraftVersion"
    version = project.version.toString()

    modrinth {
        projectId = modrinthId
        accessToken = providers.environmentVariable("MODRINTH_TOKEN")
        minecraftVersions.add(minecraftVersion)
        environment = CLIENT_ONLY
        requires { slug = "create" }
        requires { slug = "entityculling" }
    }

    curseforge {
        projectId = curseforgeId
        accessToken = providers.environmentVariable("CURSEFORGE_TOKEN")
        minecraftVersions.add(minecraftVersion)
        client = true
        server = false
        requires { slug = "create" }
        requires { slug = "entityculling" }
    }
}
