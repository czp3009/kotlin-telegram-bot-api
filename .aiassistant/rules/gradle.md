---
apply: by file patterns
patterns: gradle/libs.versions.toml,**/*.gradle.kts,**/*.gradle,gradle/wrapper/gradle-wrapper.properties,gradle.properties,local.properties
---

# Gradle

## Version Management

Manage all library versions centrally through the `gradle/libs.versions.toml` file, and reference the libraries managed
in the toml file in build.gradle.kts files

## Dependencies

Always use the latest version when adding dependencies
