load("@rules_jvm_external//:defs.bzl", "artifact")
load("@io_bazel_rules_kotlin//kotlin:android.bzl", "kt_android_library")
load("@build_bazel_rules_android//android:rules.bzl", "android_library", "android_binary")

PACKAGE = "dev.thesummit.rook"
MANIFEST = "AndroidManifest.xml"

android_library(
    name = "bazel_res",
    custom_package = PACKAGE,
    manifest = MANIFEST,
    resource_files = glob(["res/**"]),
    enable_data_binding = False,
    deps = [
        # artifact("androidx.constraintlayout:constraintlayout"),
        # artifact("com.google.android.material:material"),
    ],
)

kt_android_library(
    name = "lib",
    srcs = glob(["**/*.kt"]),
    custom_package = PACKAGE,
    manifest = MANIFEST,
    resource_files = glob(["res/**"]),
    plugins = ["//:jetpack_compose_compiler_plugin"],
    visibility = ["//visibility:public"],
    deps = [
        ":bazel_res",
        "@maven//:androidx_activity_activity_compose",
        "@maven//:androidx_appcompat_appcompat",
        "@maven//:androidx_compose_material_material",
        "@maven//:androidx_compose_runtime_runtime",
        "@maven//:androidx_compose_ui_ui",
        "@maven//:androidx_compose_ui_ui_tooling",
        "@maven//:androidx_core_core_ktx",
        "@maven//:org_jetbrains_kotlin_kotlin_stdlib",
    ],
)

# An app that consumes android-kt deps
android_binary(
    name = "rook_android",
    custom_package = PACKAGE,
    dex_shards = 5,
    incremental_dexing = 1,
    manifest = MANIFEST,
    multidex = "native",
    visibility = ["//visibility:public"],
    deps = [
        ":bazel_res",
        ":lib",
    ],
)

