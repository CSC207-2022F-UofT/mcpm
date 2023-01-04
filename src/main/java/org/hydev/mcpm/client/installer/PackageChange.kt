package org.hydev.mcpm.client.installer

import org.hydev.mcpm.client.display.presenters.Table
import org.hydev.mcpm.client.models.PluginVersion
import org.hydev.mcpm.client.models.PluginYml
import org.hydev.mcpm.utils.UnitConverter.sizeFmt
import java.io.File

/**
 * Package change object
 *
 * Install: Define only new,
 * Uninstall: Define only original,
 * Upgrade: Define both new and original
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2023-01-03
 */
class PackageChange(
    val new: PluginVersion? = null,
    val original: PluginYml? = null,
    val originalFile: File? = null,
    val manual: Boolean = false)
{
    enum class Type(val symbol: String) { ADD("+"), REMOVE("-"), UPGRADE("U") }

    /**
     * Compute type
     */
    val type get() = if (new != null) if (original != null) Type.UPGRADE else Type.ADD else Type.REMOVE

    /**
     * Format package change list
     */
    val List<PackageChange>.table get() = Table(listOf("Plugin", "Current", "Latest", "Size"), map {
        val ty = it.type
        listOf(
            ty.symbol + (it.new?.meta?.name ?: it.original?.name ?: it.originalFile?.name),
            it.original?.version ?: "&7-",
            it.new?.meta?.version ?: "&7-",
            it.originalFile?.length()?.sizeFmt().toString()
        )
    })

    val List<PackageChange>.downloadSize get() = sumOf { it.new?.size ?: 0 }

    val List<PackageChange>.fmt get() = "Found $size plugins changes\n\n" +
        "$table\n\nTotal download size: $downloadSize"
}
