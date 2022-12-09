# mcpm

MCPM is a package manager for Minecraft Server plugins.
The goal of this project is to enable users to quickly set up and configure mods on their new Minecraft Server.

On our team, we've had some rough experiences trying to get the
right combination of plugins and mods that gets our server working just right.
MCPM provides a bunch of utilities to get just the right plugins
you need to deliver a great Minecraft experience to your players.

MCPM allows you to:

 - Install many plugins super quickly with one simple command!
 - Keep track of and uninstall unnecessary plugins easily!
 - Make sure all your plugins are patched and up to their latest version!
 - Take a look under the hood and quickly see a list of installed plugins!
 - Search for plugins by relevant commands and purposes, instead of just names and descriptions!
 - Hot Reload your plugins while everyone is playing. No need to restart your server to test something new! 
 - Export and share your best plugin configurations with others!

## Getting Started

MCPM requires Java 19, which can be installed [here](https://www.oracle.com/java/technologies/javase/jdk19-archive-downloads.html).

MCPM runs in two environments:

- The **In-Game** environment, where you can configure your server in-game. (Recommended)
 - The **CLI** environment, where you can search, download, update and configure plugins from the command line.

### In-Game Environment

The In-Game environment is more involved and requires a server with our MCPM plugin.

To install MCPM as a plugin on an existing Bukkit-API Minecraft server (e.g. Spigot / Paper / Purpur / Pufferfish), you can go to [GitHub Releases](https://github.com/CSC207-2022F-UofT/mcpm/releases), download the latest version's jar, drag it into the `plugins` folder of your server, and restart the server.

However, if you are someone grading the course project, you probably don't have an existing minecraft server. You can easily set up a testing Minecraft server using the `tools/start_server.py` script on **Unix** (Linux / macOS).
This script will automatically download the proper JDK to build and run the server environment.
If you are using **Windows**, please run this script in the Windows Subsystem for Linux (WSL) environment.

```shell
pip install -r requirements.txt
python -m tools.start_server    
```

When in-game, use the `/mcpm ...` slash command to start configuring plugins.

For example, if you wanted to search for plugins named `JedCore`, type `/mcpm search JedCore` in chat.

### CLI Environment

The CLI environment provides easy access across directories without having to install MCPM to a server. However, CLI usage provides a reduced set of features because hot reloading requires the server JVM environment. To access the CLI environment on a **Unix** machine, you can use the `./mcpm` shortcut in the root directory.

```shell
mcpm search JedCore # Look for plugins named JedCore
mcpm install JedCore # Install it to our local directory!
```

On a Windows machine, I recommend running MCPM in the minecraft server environment. Alternatively, you can setup Windows Subsystem for Linux (WSL) to run everything as if you're using Linux.

## Getting Help

If you need a reminder of what mcpm provides, you can type in `mcpm` without arguments or use the `mcpm help` command.

```
> mcpm help

mcpm: Minecraft Plugin Package Manager
/mcpm export - Export plugin configuration
/mcpm import - Import a plugins config from a previous export
/mcpm list - List installed plugins
/mcpm search - Search for a plugin in the database
/mcpm mirror - Select a source (mirror) to download plugins from
/mcpm info - Show the info of an installed plugin
/mcpm install - Download and install a plugin from the database
/mcpm refresh - Refresh cached plugin database
/mcpm uninstall - Uninstall a plugin from file system
/mcpm update - Updates plugins to the latest version.
/mcpm load - Load a plugin in the plugins folder
/mcpm reload - Reload a currently loaded plugin
/mcpm unload - Unload a currently loaded plugin
To view the help message of a command, use /mcpm <command> -h
```

You can also learn more about the options that each subcommand provides by passing `-h` to them!

```
> mcpm export --help

usage: mcpm export [-h] [{pastebin,file,literal}] [out]

positional arguments:
  {pastebin,file,literal}
  out

named arguments:
  -h, --help
```

## Search

With MCPM you can search for new plugins using the search command.
There's 3 different ways you can search for new plugins:

 - By plugin name. The simplest and most precise way to search.
 - By keyword. Searches plugin names and descriptions for matches.
 - By command. For when you're looking for a plugin with the one command you need.

You can execute these commands in any environment using the `search` subcommand:
```shell
mcpm search JedCore # Search for a plugin with the name "JedCore"
mcpm search --keyword "protect land" # Search for a plugin that has protect in its description.
mcpm search --command claim # Search for a plugin that provides a /claim command.
```

## Install Plugins

This is feature is in-progress and does not have a CommandParser yet.

With MCPM you can install a variety of plugins from the Spigot database.

This is done via our host mirrors that intermittently scrape and provide a list of plugins to download. 
You can take a look at the underlying structure of the server [here](https://mcpm.hydev.org).

You can install plugins using the same method you use to search plugins!
For example, you can install a plugin that provides a `claim` command using the `--by-command` option!

```shell
mcpm install JedCore # Installs the latest version of the JedCore plugin.
```

We recommend you use the search command before installing anything,
however the option to install by keyword and command are still provided.

## Uninstall Plugins

This is feature is in-progress and does not have a CommandParser yet.

MCPM keeps track of locally installed plugins for you so you
don't need to worry about accidentally breaking your plugin installs.

You can easily uninstall a plugin by name using the `uninstall` subcommand.

The `uninstall` command will automatically get rid of all dependencies if possible.

If you are attempting to get rid of a plugin that's required for another
plugin to work, then MCPM will ask you to uninstall the other plugin first.

This is to prevent you from creating invalid setups with your server
(e.g. cases where your server can't start because a plugin is missing a dependency).

```shell
mcpm uninstall JedCore # Remove JedCore and its dependencies.
```

## Update Plugins

This is feature is in-progress and does not have a CommandParser yet.

MCPM also enables you to easily update all your locally installed plugins.

You can pass it a list of names if you only want to update certain plugins,
or pass a specific version string to force MCPM to download a specific version of the plugin.

```shell
mcpm update # Updates all plugins to their latest versions
mcpm update JedCore CoreProtect # Update ONLY JedCore & CoreProtect
```

## List Installed Plugins

MCPM allows you to easily see a list of all the plugins you currently have enabled.

To do this, just call the list command:

```shell
mcpm list # Prints a list of all installed commands in a table
mcpm list manual # Prints a list of all manually installed commands (excludes plugins installed as dependencies)
mcpm list outdated # Prints a list of all commands that need updates
```

## Hot Reload Plugins

MCPM allows you to quickly reload plugins on the fly.

Often admins need to restart their server between tweaking plugins in order for the tweaks to take effect.

With MCPM, you're able to skip this step and reload and unload plugins while in game.

These commands are only accessible in the _In-Game_ environment. You must run them in a valid MCPM server.

```shell
mcpm load JedCore # Loads the JedCore plugin on the fly.
mcpm reload JedCore # Unloads the JedCore plugin and loads it again.
mcpm unload JedCore # Unload the JedCore plugin on the fly.
```

After a plugin is loaded, the plugin should start taking effect without the need for restarting your server.

## Export/Import Plugin Configurations

This is feature is in-progress and does not have a CommandParser for all components yet.

MCPM allows you to export your current plugin configuration out into a file that you can then share with your friends.

Sometimes you can find great set of plugins that work for your server, and you want to save it for later.

Using the `export` subcommand command, you can quickly create a list of all installed plugins.
You can then use the `import` command later to quickly reinstall that list of plugins.

This allows plugins to be shared across servers and admins.
You can also keep configurations for later and improve them as you learn more about the plugin landscape.

```shell
mcpm export # export to a url
mcpm export file plugins.txt # Export a list of all currently installed plugins into a file called plugins.txt.
mcpm import file plugins.txt # Import (install) every plugin contained in the plugins.txt file.
```

## MCPRS - Plugin Repository Server

The downloadable Spigot plugins and their meta info are stored on our server, hosted in Toronto 🇨🇦. If you live far from Canada, please consider switching to one of the mirrors below:

**North America** 🇺🇸

| Mirror URL (HTTPS)   | Hosted By | Provider    | Location      | Speed    | Update |
|----------------------|-----------|-------------|---------------|----------|--------|
| mcprs.hydev.org      | HyDEV     | OVH Hosting | 🇨🇦 Montreal | 100 Mbps | 1 day  |
| mcprs-bell.hydev.org | HyDEV     | Bell Canada | 🇨🇦 Toronto  | 750 Mbps | 1 day  |

**Europe** 🇪🇺

| Mirror URL (HTTPS)  | Hosted By | Provider   | Location        | Speed    | Update |
|---------------------|-----------|------------|-----------------|----------|--------|
| mcprs-lux.hydev.org | HyDEV     | GCore Labs | 🇱🇺 Luxembourg | 200 Mbps | 1 day  |

**Asia**

| Mirror URL (HTTPS)    | Hosted By | Provider | Location   | Speed    | Update |
|-----------------------|-----------|----------|------------|----------|--------|
| mcprs-tokyo.hydev.org | HyDEV     | Vultr    | 🇯🇵 Tokyo | 200 Mbps | 1 day  |

If you want to contribute your network traffic by setting up a mirror, feel free to check out [How to set up a mirror](#How to set up a mirror)

### How to set up a mirror

The MCPRS server is hosted with a plain file server that supports both http and rsync. The official server is hosted using Nginx, but any file server with such compatibility would work. You can follow one of the approaches below to set up a mirror.

After setting up a mirror, if you want to add it to our mirror list, you can submit a pull request to this repo editing the [mirrorlist.yml](mirrorlist.yml) file.

#### Setup Mirror using Docker Compose

For convenience, we created a docker image so that you can set up a mirror using Docker. It will automatically set up:

1. `mcprs-sync`: Script to automatically sync updates every 24 hours (configurable)
2. `mcprs-rsyncd`: rsync server
3. `mcprs-nginx`: HTTP server (without SSL). This is only recommended if you don't have any other HTTP services set up

You need to install docker and docker-compose, then you need to run:

```bash
git clone https://github.com/CSC207-2022F-UofT/mcpm
cd mcpm/tools/mirror

# Then, you should review or edit the docker-compose.yml script. After that:

sudo mkdir -p /data/mcprs

# If you want to start everything (including nginx):
sudo docker-compose up -d

# Or if you want to start sync and rsyncd but want to use your own HTTP server, do:
sudo docker-compose up mcprs-sync mcprs-rsyncd -d
```

Note: If `docker-compose` says command not found, try `docker compose` instead.

#### Setup Mirror Manually

You can sync all files from an existing mirror by using `rsync`, run rsync automatically using `crontab` or systemd timer, and hosting the synchronized local directory using `nginx`.

```bash
# Use rsync to sync 
alias rsync1="rsync -rlptH --info=progress2 --safe-links --delete-delay --delay-updates --timeout=600 --contimeout=60 --no-motd"
rsync1 "SOURCE_URL" "LOCAL_DIR"
```

```nginx.conf
# /etc/nginx/conf.d/mcprs.conf
# Make sure to include this sub-config in your /etc/nginx/nginx.conf
# You can do "include /etc/nginx/conf.d/*.conf;"
# After testing the http server works, you can use certbot to obtain a HTTPS certificate
server
{
    listen 443 ssl;
    listen [::]:443 ssl;
    server_name mcprs.example.com; # TODO: Change this to your domain

    root LOCAL_DIR; # TODO: Change this to your filesystem location

    location / {
        autoindex on;
    }
}

# HTTPS Redirect
server
{
    listen 80 default_server;
    listen [::]:80 default_server;
    server_name default;
    return 301 https://$host$request_uri;
}
```

```rsyncd.conf
# /etc/rsyncd.conf
uid = nobody
gid = nobody
use chroot = no
max connections = 4
syslog facility = local5
pid file = /run/rsyncd.pid

[mcprs]
        path = /ws/mcpm/.mcpm
        comment = MCPM Plugin Repository Server
```

### How does the server work?

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
