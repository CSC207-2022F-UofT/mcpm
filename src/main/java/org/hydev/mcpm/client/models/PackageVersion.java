package org.hydev.mcpm.client.models;

import java.util.List;


/**
 * This class describes one version of a package that the package manager can download.
 *
 * @param fileName Name of the file to download
 * @param version Version name (e.g. 3.1.0)
 * @param size Size of compressed archive
 * @param md5 MD5 Hash Checksum (for validation)
 * @param sha256 SHA256 Hash Checksum (for validation)
 * @param arch Supported machine architectures. (e.g. x86_64, aarch64, riscv64)
 * @param buildTime Timestamp of the build in milliseconds
 * @param depends Runtime dependencies
 * @param makeDepends Building dependencies
 * @param checkDepends Testing dependencies
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-09-27
 */
public record PackageVersion(
    String fileName,
    String version,
    long size,
    String md5,
    String sha256,
    List<String> arch,
    long buildTime,

    List<String> depends,
    List<String> makeDepends,
    List<String> checkDepends
)
{
}
