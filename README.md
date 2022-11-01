# CSC207

Final Project for CSC207

## Links

* [Course Overview](https://q.utoronto.ca/courses/278453/pages/course-project)
* [Task List (Projects)](https://github.com/orgs/CSC207-2022F-UofT/projects/18)
* [Repo in CSC207 Organization](https://github.com/CSC207-2022F-UofT/mcpm)
* [File Server Backend](https://mcpm.hydev.org)

## Development

### Setup and start a testing minecraft server:

1. `pip install -r requirements.txt`
2. `python3 -m tools.start_server`

If you want to rebuild & update our MCPM plugin while the server is running:

1. `python3 -m tools.update_build`
2. `plugman reload mcpm` (Inside the server's command prompt)

### Run a specific class in an external terminal

This is very useful to test terminal operations since the Gradle running environment isn't a tty, and IntelliJ IDEA's built-in terminal barely supports Xterm escape sequences.

For this, I've set up a custom gradle task `printCp` that will print out the classpath needed to run the classes with dependencies. It will print in stderr instead of stdout in order for bash to easily separate out the classpath. You can obtain the classpath in a bash variable by:

`cp="$(./gradlew classes testClasses printCp 2>&1 > /dev/null)" && echo "$cp"`

(Unfortunately since Windows doesn't support Bash, you'll need to use a Bash-compatible environment on Windows, either cygwin / git bash or WSL)

Then, you can run your class with:

`java19 -cp "$cp" org.hydev.mcpm.<class>`

For example, you can test the progress bar with:

`java19 -cp "$cp" org.hydev.mcpm.client.interaction.ProgressBar`

If you don't have JDK 19 installed or if you don't know where it's installed, you can use our JDK downloader tool to download a local version of JDK 19 without installing on the system. (TODO: Add tutorial after merging PR #8)

## MCPRS - Plugin Repository Server

The downloadable Spigot plugins and their meta info are stored on our server, hosted in Toronto 🇨🇦. If you live far from Canada, please consider switching to one of the mirrors below:

**North America** 🇺🇸

| Mirror URL (HTTPS)   | Hosted By | Provider    | Location      | Speed    | Update |
|----------------------|-----------|-------------|---------------|----------|--------|
| mcprs.hydev.org      | HyDEV     | OVH Hosting | 🇨🇦 Montreal | 100 Mbps | 1 day  |
| mcprs-bell.hydev.org | HyDEV     | Bell Canada | 🇨🇦 Toronto  | 750 Mbps | 1 day  |

**Europe** 🇪🇺

| Mirror URL (HTTPS)   | Hosted By | Provider   | Location        | Speed    | Update |
|----------------------|-----------|------------|-----------------|----------|--------|
| mcprs-lux.hydev.org  | HyDEV     | GCore Labs | 🇱🇺 Luxembourg | 200 Mbps | 2 days |

**Asia**

| Mirror URL (HTTPS)    | Hosted By | Provider | Location   | Speed    | Update |
|-----------------------|-----------|----------|------------|----------|--------|
| mcprs-tokyo.hydev.org | HyDEV     | Vultr    | 🇯🇵 Tokyo | 200 Mbps | 2 days |

If you want to contribute your network traffic by setting up a mirror, feel free to check out [How to setup a mirror](#How to set up a mirror)

Server file/endpoint structure:

`/db` : Database sync  
`/db/core.json` : Core database (plain text)  
`/db/core.zst` : Core database (compressed)  
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

Internal server file structure:

`/crawler` : Crawler cache / data storage  
`/crawler/spiget` : Spiget crawler  
`/crawler/spiget/resources.json` : List of all resources on SpigotMC  
`/crawler/spiget/backups/resources.{timestamp}.json` : Older resources  
`/cralwer/spiget/versions/{resource_id}.json` : Resource versions info
