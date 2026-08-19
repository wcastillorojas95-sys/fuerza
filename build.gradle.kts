// Archivo de configuracion raiz del proyecto.
//
// Mismas versiones exactas que Foco, y a proposito: son las que ya se sabe que
// compilan en GitHub Actions. Un proyecto nuevo no es el sitio para estrenar
// version de AGP.
plugins {
    id("com.android.application") version "8.7.3" apply false
    id("org.jetbrains.kotlin.android") version "2.0.21" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21" apply false
}
