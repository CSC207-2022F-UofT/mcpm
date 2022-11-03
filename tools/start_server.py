#!/usr/bin/env python3
from __future__ import annotations

import os.path
import re
import select
import shutil
import subprocess
import sys
import tarfile
from pathlib import Path
from subprocess import check_call
from tempfile import TemporaryDirectory

from hypy_utils import ensure_dir
from hypy_utils.downloader import download_file

from .download_jdk import download_oracle, ensure_java

MAX_RAM = '16384M'
MIN_RAM = MAX_RAM
TIME = 5

version_re = re.compile(r'[0-9].[0-9]+.[0-9]+')


def start(java: Path, mc_path: Path):
    # Find server jar
    jar = ''
    for f in [str(f) for f in os.listdir(mc_path) if f.endswith('.jar')]:
        ver = version_re.findall(f)
        if any(k in f.lower() for k in ['craftbukkit', 'spigot', 'paper', 'purpur']) or len(ver) != 0:
            jar = f
            break

    # Auto Restart
    while True:
        cmd = f"{java.absolute()} -Xmx{MAX_RAM} -Xms{MIN_RAM} --add-modules=jdk.incubator.vector -jar {jar} nogui"
        print(f'Executing {cmd}...')
        subprocess.Popen(cmd, shell=True, cwd=mc_path).wait()

        print('Server stopped, restarting in 5s\nPress any key to stop the server.')
        has_input, _, _ = select.select([sys.stdin], [], [], 5)
        if has_input:
            print('Server stops.')
            exit(0)


def update_build(mc_path: Path):
    # Build project
    print('Building project...')
    build_jar = Path('build/libs')
    shutil.rmtree(build_jar, ignore_errors=True)
    check_call('./gradlew shadow', shell=True)

    # Install plugin
    print('Installing our MCPM plugin...')
    plugin_path = mc_path / 'plugins/mcpm.jar'
    shutil.rmtree(plugin_path, ignore_errors=True)
    ensure_dir(plugin_path.parent)
    shutil.copy2(build_jar / str([f for f in os.listdir(build_jar) if f.endswith("-all.jar")][0]), plugin_path)


if __name__ == '__main__':
    # Download JDK
    java = ensure_java("19")

    # Download server tar
    mc_path = Path('build/mc-server')
    if not mc_path.is_dir():
        with TemporaryDirectory() as tmp:
            tmp = Path(tmp)
            tgz = tmp / 'server.tgz'
            ext = tmp / 'ext'

            # Download tarball
            print('Downloading server tarball...')
            download_file('https://github.com/hykilpikonna/mcpm-test-server/tarball/master', tgz)

            # Extract
            print('Extracting server...')
            with tarfile.open(tgz) as f:
                f.extractall(ext)

            print('Copying server...')
            shutil.move(ext / str(os.listdir(ext)[0]), mc_path)

            print(f'Server installed to {mc_path}')

    # Update built jar file
    update_build(mc_path)

    # Start server
    print('Starting server...')
    start(java, mc_path)
