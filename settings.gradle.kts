pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

<<<<<<< HEAD
rootProject.name = "prueba_kt_bd"
include(":app")
include(":crudrealtimeadmin")
=======
rootProject.name = "proyectoAgenda"
include(":app")
 
>>>>>>> 488b164a06a7be2ed7c9159c36100d5bfa872c4f
