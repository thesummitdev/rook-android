load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

_COMPOSE_VERSION = "1.2.1"

_COMPOSE_COMPILER_VERSION = "1.3.2"

_KOTLIN_COMPILER_VERSION = "1.7.20"

_KOTLIN_COMPILER_SHA = "5e3c8d0f965410ff12e90d6f8dc5df2fc09fd595a684d514616851ce7e94ae7d"

# Setup Kotlin
rules_kotlin_version = "1.7.1"
rules_kotlin_sha = "fd92a98bd8a8f0e1cdcb490b93f5acef1f1727ed992571232d33de42395ca9b3"

http_archive(
    name = "io_bazel_rules_kotlin",
    urls = ["https://github.com/bazelbuild/rules_kotlin/releases/download/v%s/rules_kotlin_release.tgz" % rules_kotlin_version],
    sha256 = rules_kotlin_sha,
)

load("@io_bazel_rules_kotlin//kotlin:repositories.bzl", "kotlin_repositories", "kotlinc_version", "versions")

kotlin_repositories(
    compiler_release = kotlinc_version(
        release = _KOTLIN_COMPILER_VERSION,
        sha256 = _KOTLIN_COMPILER_SHA,
    ),
)

register_toolchains("//:kotlin_toolchain")

## JVM External

http_archive(
    name = "rules_jvm_external",
    sha256 = versions.RULES_JVM_EXTERNAL_SHA,
    strip_prefix = "rules_jvm_external-%s" % versions.RULES_JVM_EXTERNAL_TAG,
    url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % versions.RULES_JVM_EXTERNAL_TAG,
)

load("@rules_jvm_external//:defs.bzl", "maven_install")

maven_install(
    artifacts = [
        "org.jetbrains.kotlin:kotlin-stdlib:{}".format(_KOTLIN_COMPILER_VERSION),
        "androidx.core:core-ktx:1.7.0",
        "androidx.appcompat:appcompat:1.4.1",
        "androidx.activity:activity-compose:1.4.0",
        "androidx.compose.material:material:{}".format(_COMPOSE_VERSION),
        "androidx.compose.ui:ui:{}".format(_COMPOSE_VERSION),
        "androidx.compose.ui:ui-tooling:{}".format(_COMPOSE_VERSION),
        "androidx.compose.compiler:compiler:{}".format(_COMPOSE_COMPILER_VERSION),
        "androidx.compose.runtime:runtime:{}".format(_COMPOSE_VERSION),
    ],
    repositories = [
        "https://maven.google.com",
        "https://repo1.maven.org/maven2",
    ],
)

http_archive(
    name = "bazel_skylib",
    sha256 = versions.SKYLIB_SHA,
    urls = ["https://github.com/bazelbuild/bazel-skylib/releases/download/%s/bazel-skylib-%s.tar.gz" % (
        versions.SKYLIB_VERSION,
        versions.SKYLIB_VERSION,
    )],
)

## Android

http_archive(
    name = "build_bazel_rules_android",
    sha256 = versions.ANDROID.SHA,
    strip_prefix = "rules_android-%s" % versions.ANDROID.VERSION,
    urls = ["https://github.com/bazelbuild/rules_android/archive/v%s.zip" % versions.ANDROID.VERSION],
)

load("@build_bazel_rules_android//android:rules.bzl", "android_sdk_repository")

android_sdk_repository(name = "androidsdk")
