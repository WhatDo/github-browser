package utils

import org.gradle.api.artifacts.VersionConstraint
import java.util.Optional

internal val Optional<VersionConstraint>.value: String
    get() = get().let { version ->
        version.requiredVersion
            .ifEmpty { version.strictVersion }
            .ifEmpty { version.preferredVersion }
    }

internal val Optional<VersionConstraint>.intValue: Int
    get() = value.toInt()
