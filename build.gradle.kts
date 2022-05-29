plugins {
    `java-library`
    id("io.freefair.lombok") version ("6.4.3")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.openrewrite:rewrite-bom:7.23.0"))
    implementation("org.openrewrite:rewrite-xml")

    testImplementation(platform("org.junit:junit-bom:5.8.2"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.assertj:assertj-core") {
        version { strictly("[3.0.0, 4.0.0[") }
    }
    testImplementation("org.openrewrite:rewrite-test")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
