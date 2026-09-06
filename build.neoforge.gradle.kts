plugins {
    id("java-library")
    id("net.neoforged.moddev")
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
val neoVersion = prop("neo_version")

val createVersion = prop("create_version")
val ponderVersion = prop("ponder_version")
val flywheelVersion = prop("flywheel_version")
val registrateVersion = prop("registrate_version")
val entitycullingVersion = prop("entityculling_version")
val sableCompanionVersion = prop("sable_companion_version")
val clothConfigVersion = prop("cloth_config_version")
val sodiumVersion = prop("sodium_version")
val lithiumVersion = prop("lithium_version")

val modrinthId = prop("modrinth_id")
val curseforgeId = prop("curseforge_id")

version = "$modVersion+$minecraftVersion$loader"
group = modGroupId

base { archivesName = modId }

java.toolchain.languageVersion = JavaLanguageVersion.of(prop("java_version").toInt())

val clientRunName = "NeoForge Client ($minecraftVersion)"

neoForge {
    setVersion(neoVersion)

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

repositories {
    maven("https://maven.createmod.net/")
    maven("https://maven.ithundxr.dev/snapshots")
    maven("https://api.modrinth.com/maven") {
        content { includeGroup("maven.modrinth") }
    }
    maven("https://maven.ryanhcode.dev/releases") {
        content { includeGroup("dev.ryanhcode.sable-companion") }
    }
    mavenCentral()
}

dependencies {
    implementation("com.simibubi.create:create-$minecraftVersion:$createVersion:slim") { isTransitive = false }
    implementation("net.createmod.ponder:ponder-neoforge:$ponderVersion+mc$minecraftVersion")
    compileOnly("dev.engine-room.flywheel:flywheel-neoforge-api-$minecraftVersion:$flywheelVersion")
    implementation("dev.engine-room.flywheel:flywheel-neoforge-$minecraftVersion:$flywheelVersion")
    implementation("com.tterrag.registrate:Registrate:$registrateVersion")

    implementation("maven.modrinth:entityculling:$entitycullingVersion-neoforge,$minecraftVersion")
    implementation("maven.modrinth:cloth-config:$clothConfigVersion+neoforge")

    compileOnly("dev.ryanhcode.sable-companion:sable-companion-common-$minecraftVersion:$sableCompanionVersion")

    runtimeOnly("maven.modrinth:ok-zoomer:10.0.0-beta.13+neo-neoforge,$minecraftVersion")

    "additionalRuntimeClasspath"("maven.modrinth:sodium:mc$minecraftVersion-$sodiumVersion-neoforge")
    "additionalRuntimeClasspath"("maven.modrinth:lithium:mc$minecraftVersion-$lithiumVersion-neoforge")
}

tasks.withType<ProcessResources>().configureEach {
    val replaceProperties = mapOf(
        "minecraft_version" to minecraftVersion,
        "minecraft_version_range" to minecraftVersionRange,
        "neo_version" to neoVersion,
        "loader_version_range" to loaderVersionRange,
        "mod_id" to modId,
        "mod_name" to modName,
        "mod_license" to modLicense,
        "mod_version" to modVersion,
        "mod_description" to modDescription,
    )
    inputs.properties(replaceProperties)
    filesMatching(listOf("META-INF/neoforge.mods.toml")) { expand(replaceProperties) }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.named("createMinecraftArtifacts") {
    dependsOn(tasks.named("stonecutterGenerate"))
}

publishMods {
    file = tasks.named<Jar>("jar").flatMap { it.archiveFile }
    type = STABLE
    modLoaders.add(loader)
    changelog = providers.environmentVariable("CHANGELOG").orElse("No changelog provided.")

    displayName = "Nowheel $modVersion for $loader $minecraftVersion"
    version = project.version.toString()

    modrinth {
        projectId = modrinthId
        accessToken = providers.environmentVariable("MODRINTH_TOKEN")
        minecraftVersions.add(minecraftVersion)
        requires { slug = "create" }
        requires { slug = "entityculling" }
    }

    curseforge {
        projectId = curseforgeId
        accessToken = providers.environmentVariable("CURSEFORGE_TOKEN")
        minecraftVersions.add(minecraftVersion)
        requires { slug = "create" }
        requires { slug = "entityculling" }
    }
}
