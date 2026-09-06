plugins {
	id("net.fabricmc.fabric-loom")
	id("io.freefair.lombok") version "9.5.0"
	id("maven-publish")
}

version = providers.gradleProperty("mod_version").get()
group = providers.gradleProperty("maven_group").get()

repositories {
	// Add repositories to retrieve artifacts from in here.
	// You should only use this when depending on other mods because
	// Loom adds the essential maven repositories to download Minecraft and libraries from automatically.
	// See https://docs.gradle.org/current/userguide/declaring_repositories.html
	// for more information about repositories.

	exclusiveContent {
		forRepository {
			maven {
				name = "GeckoLib"
				url = uri("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven")
			}
		}
		filter {
			includeGroupAndSubgroups("com.geckolib")
		}
	}
}

loom {
	splitEnvironmentSourceSets()

	mods {
		register("cccr") {
			sourceSet(sourceSets["main"])
			sourceSet(sourceSets["client"])
		}
	}

	runs {
		getByName("client") {
			programArguments.addAll("--username", "Dev")
		}
	}
}

fabricApi {
	configureDataGeneration {
		client = true
	}
}

dependencies {
	val minecraftVersion = providers.gradleProperty("minecraft_version").get()

	// To change the versions see the gradle.properties file
	minecraft("com.mojang:minecraft:$minecraftVersion")
	
	implementation("net.fabricmc:fabric-loader:${providers.gradleProperty("loader_version").get()}")

	// Fabric API. This is technically optional, but you probably want it anyway.
	implementation("net.fabricmc.fabric-api:fabric-api:${providers.gradleProperty("fabric_api_version").get()}")

	implementation("com.geckolib:geckolib-fabric-$minecraftVersion:${providers.gradleProperty("geckolib_version").get()}")

	compileOnly("com.demonwav.mcdev:annotations:2.1.0")
}

tasks.processResources {
	inputs.property("version", version)

	filesMatching("fabric.mod.json") {
		expand("version" to version)
	}
}

tasks.withType<JavaCompile>().configureEach {
	options.release = 25
}

java {
	// Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
	// if it is present.
	// If you remove this line, sources will not be generated.
	withSourcesJar()

	sourceCompatibility = JavaVersion.VERSION_25
	targetCompatibility = JavaVersion.VERSION_25
}

tasks.jar {
	inputs.property("projectName", project.name)

	from("LICENSE") {
		rename { "${it}_${project.name}" }
	}
}

// configure the maven publication
publishing {
	publications {
		register<MavenPublication>("mavenJava") {
			from(components["java"])
		}
	}

	// See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
	repositories {
		// Add repositories to publish to here.
		// Notice: This block does NOT have the same function as the block in the top level.
		// The repositories here will be used for publishing your artifact, not for
		// retrieving dependencies.
	}
}
