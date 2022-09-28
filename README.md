# CSC207

Final Project for CSC207

## Brainstorm

Server file/endpoint structure:

`/db` : Database sync
`/db/core.tar.zst` : Core database (compressed)
`/pkgs` : List of packages
`/pkgs/{name}` : One package
`/pkgs/{name}/{version}` : One version
`/pkgs/{name}/{version}/release.jar` : Prebuilt jar for the version of a package
`/pkgs/{name}/{version}/plugin.yml` : Meta info for the version of a package
`/pkgs/{name}/{version}/build.sh` : Build script used to produce the release jar (if they're built)

Internal server file structure:

`/crawler` : Crawler cache / data storage
`/crawler/spiget` : Spiget crawler
`/crawler/spiget/resources.json` : List of all resources on SpigotMC
`/crawler/spiget/backups/resources.{timestamp}.json` : Older resources
`/cralwer/spiget/versions/{resource_id}.json` : Resource versions info

GitHub package repo file structure:

`spiget-plugins.yml` : List of plugins automatically updated from Spiget
`/pkgs` : List of all manually built packages
`/pkgs/{name}` : One package
`/pkgs/{name}/build.sh` : Build script for the latest version (Historical versions are in git history)
