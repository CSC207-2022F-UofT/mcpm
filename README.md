# CSC207

Final Project for CSC207

## Links

* [Course Overview](https://q.utoronto.ca/courses/278453/pages/course-project)
* [Task List (Projects)](https://github.com/orgs/CSC207-2022F-UofT/projects/18)
* [Repo in CSC207 Organization](https://github.com/CSC207-2022F-UofT/mcpm)
* [File Server Backend](https://mcpm.hydev.org)

## Brainstorm

Server file/endpoint structure:

`/db` : Database sync  
`/db/core.tar.zst` : Core database (compressed)  
`/pkgs` : List of packages  
`/pkgs/spiget` : Raw Spiget packages indexed by resource ids and version ids  
`/pkgs/spiget/{resource-id}` : One Spiget resource  
`/pkgs/spiget/{resource-id}/{version-id}` : One Spiget version  
`/pkgs/spiget/{resource-id}/{version-id}/release.jar` : Jar published by the developer   
`/pkgs/spiget/{resource-id}/{version-id}/plugin.yml` : Meta info  
`/pkgs/links` : Generated symbolic links indexed by names and version names  
`/pkgs/links/{name}` : One package  
`/pkgs/links/{name}/{version}` : One version  
`/pkgs/links/{name}/{version}/release.jar` : Prebuilt jar for the version of a package  
`/pkgs/links/{name}/{version}/plugin.yml` : Meta info for the version of a package  
`/pkgs/links/{name}/{version}/build.sh` : Build script used to produce the release jar (if they're built)  

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
