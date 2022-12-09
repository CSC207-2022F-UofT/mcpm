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

# Getting Started
MCPM requires Java 19, which can be installed [here](https://www.oracle.com/java/technologies/javase/jdk19-archive-downloads.html).

MCPM runs in two environments:

 - The **CLI** environment, where you can search, download, update and configure plugins from the command line.
 - The **In-Game** environment, where you can configure your server in-game.

To access the CLI environment on a Unix machine, you can use the `./mcpm` shortcut in the root directory.

```shell
mcpm search JedCore # Look for plugins named JedCore
mcpm install JedCore # Install it to our local directory!
```

On a Windows machine, I recommend executing the shortcut via a bash emulator (Git Bash),
launching the app yourself using `java`, or investigating the scripts in the /tools folder (run.py).

The In-Game environment is more involved and requires a server with our MCPM plugin.

You can quickly set up a test server using the `tools/start_server.py` script.
This script will automatically download the proper JDK to build and run the server environment.

```shell
pip install -r requirements.txt
python -m tools.start_server
```

When in-game, use the `/mcpm ...` slash command to start configuring plugins.

For example, if you wanted to search for plugins named `JedCore`, type `/mcpm search JedCore` in chat.

# Getting Help

If you need a reminder of what mcpm provides, you can use the `-h` or `--help` argument to learn more.

```
> mcpm --help

usage: mcpm [-h] {echo,export} ...

positional arguments:
    echo: This is a dummy echo command that...
    export: This command allows you to export...
    
named arguments:
  -h, --help             show this help message and exit
```

You can also learn more about the options that each subcommand provides by passing `-h` to them!

```
> mcpm export --help

usage: mcpm export [-h] [-c {true,false}] outfile

positional arguments:
  outfile

named arguments:
  -h, --help             show this help message and exit
  -c {true,false}, --cache {true,false}
```

# Search

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

# Install Plugins

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

# Uninstall Plugins

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

# Update Plugins

This is feature is in-progress and does not have a CommandParser yet.

MCPM also enables you to easily update all your locally installed plugins.

You can pass it a list of names if you only want to update certain plugins,
or pass a specific version string to force MCPM to download a specific version of the plugin.

```shell
mcpm update # Updates all plugins to their latest versions
mcpm update JedCore CoreProtect # Update ONLY JedCore & CoreProtect
```

# List Installed Plugins

MCPM allows you to easily see a list of all the plugins you currently have enabled.

To do this, just call the list command:

```shell
mcpm list # Prints a list of all installed commands in a table
mcpm list manual # Prints a list of all manually installed commands (excludes plugins installed as dependencies)
mcpm list outdated # Prints a list of all commands that need updates
```

# Hot Reload Plugins

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

# Export/Import Plugin Configurations

This is feature is in-progress and does not have a CommandParser for all components yet.

MCPM allows you to export your current plugin configuration out into a file that you can then share with your friends.

Sometimes you can find great set of plugins that work for your server, and you want to save it for later.

Using the `export` subcommand command, you can quickly create a list of all installed plugins.
You can then use the `import` command later to quickly reinstall that list of plugins.

This allows plugins to be shared across servers and admins.
You can also keep configurations for later and improve them as you learn more about the plugin landscape.

```
mcpm export # export to a url
mcpm export file plugins.txt # Export a list of all currently installed plugins into a file called plugins.txt.
mcpm import file plugins.txt # Import (install) every plugin contained in the plugins.txt file.
```
