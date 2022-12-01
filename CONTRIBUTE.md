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

You can use `python3 -m tools.run <class reference>` to build and run a specific main class externally.

<details>
    <summary>How does it work</summary>

For this, I've set up a custom gradle task `printCp` that will print out the classpath needed to run the classes with dependencies. It will print in stderr instead of stdout in order for bash to easily separate out the classpath. You can obtain the classpath in a bash variable by:

`cp="$(./gradlew classes testClasses printCp 2>&1 > /dev/null)" && echo "$cp"`

(Unfortunately since Windows doesn't support Bash, you'll need to use a Bash-compatible environment on Windows, either cygwin / git bash or WSL)

Then, you can run your class with:

`java19 -cp "$cp" org.hydev.mcpm.<class>`

For example, you can test the progress bar with:

`java19 -cp "$cp" org.hydev.mcpm.client.display.progress.ProgressBar`

</details>


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

If you want to contribute your network traffic by setting up a mirror, feel free to check out [How to setup a mirror](#How to setup a mirror)

### How to setup a mirror

The MCPRS server is hosted with a plain file server that supports both http and rsync. The official server is hosted using Nginx, but any file server with such compatibility would work. You can follow one of the approaches below to set up a mirror.

After setting up a mirror, if you want to add it to our mirror list, you can submit a pull request to this repo editing the [mirrorlist.yml](mirrorlist.yml) file.

#### Setup Mirror using Docker Compose

For convenience, we created a docker image so that you can setup a mirror using Docker. It will automatically set up:

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
