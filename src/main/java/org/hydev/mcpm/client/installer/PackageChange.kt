package org.hydev.mcpm.client.installer

import org.hydev.mcpm.client.models.PluginVersion
import org.hydev.mcpm.client.models.PluginYml
import java.io.File

/**
 * Package change
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2023-01-03
 */
class PackageChange(
    val type: ChangeType,
    val new: PluginVersion,
    val original: PluginYml?,
    val originalFile: File?,
    val manual: Boolean = false)
{
    enum class ChangeType {
        ADD, REMOVE, UPGRADE
    }
}
