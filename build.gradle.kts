plugins {
java
id("org.springframework.boot") version "4.0.8"
id("io.spring.dependency-management") version "1.1.7"
}
group = "com.anish"
version = "0.0.1-SNAPSHOT"
java {
toolchain {
languageVersion.set(JavaLanguageVersion.of(25))
}
}
val artifactoryUrl: String by project.extra
repositories {
maven(url = "$artifactoryUrl/remote-repos") {
credentials {
username = extra["artifactory_user"] as String
password = extra["artifactory_password"] as String
}
}
}
dependencies {
implementation("org.springframework.boot:spring-boot-starter-web")
implementation("org.springframework.boot:spring-boot-starter-actuator")
implementation("org.springframework.boot:spring-boot-starter-validation")
implementation("org.apache.tomcat.embed:tomcat-embed-jasper")
implementation("org.glassfish.web:jakarta.servlet.jsp.jstl:3.0.1")
implementation("jakarta.servlet.jsp.jstl:jakarta.servlet.jsp.jstl-api:3.0.0")
implementation("com.google.code.gson:gson")
developmentOnly("org.springframework.boot:spring-boot-devtools")
testImplementation("org.springframework.boot:spring-boot-starter-test")
testImplementation("org.junit.jupiter:junit-jupiter-api")
testRuntimeOnly("org.junit.platform:junit-platform-launcher")
testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}
tasks.withType<Test> {
useJUnitPlatform()
}
tasks.withType<JavaCompile> {
options.compilerArgs.addAll(listOf("-Xlint:all", "-Xlint:-processing", "-parameters"))
}