package com.stingers.alttpr.screens.licenses

import com.stingers.alttpr.BuildKonfig
import com.stingers.alttpr.model.Licence
import com.stingers.alttpr.model.LicenceType

val licences = listOf(
    Licence(
        name = "ALTTPR",
        type = LicenceType.MIT,
        url = "https://github.com/sporchia/alttp_vt_randomizer",
        version = "v31.2.0"
    ),
    Licence(
        name = "Ktor",
        type = LicenceType.APACHE,
        url = "https://www.github.com/ktorio/ktor",
        version = BuildKonfig.KTOR_VERSION
    ),
    Licence(
        name = "Ktorfit",
        type = LicenceType.APACHE,
        url = "https://github.com/Foso/Ktorfit",
        version = BuildKonfig.KTORFIT_VERSION
    ),
    Licence(
        name = "Koin",
        type = LicenceType.APACHE,
        url = "https://www.github.com/InsertKoinIO/koin",
        version = BuildKonfig.KOIN_VERSION
    ),
    Licence(
        name = "FileKit",
        type = LicenceType.MIT,
        url = "https://github.com/vinceglb/FileKit",
        version = BuildKonfig.FILEKIT_VERSION
    ),
    Licence(
        name = "Coil",
        type = LicenceType.APACHE,
        url = "https://www.github.com/coil-kt/coil",
        version = BuildKonfig.COIL_VERSION
    ),
)