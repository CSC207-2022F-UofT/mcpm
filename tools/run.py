#!/usr/bin/env python3
# Use this with python3 -m tools.run
import argparse
import subprocess
from subprocess import run, check_call

from tools import download_jdk

ALIAS = {
    "Launcher": "org.hydev.mcpm.client.Launcher",
    "ProgressBar": "org.hydev.mcpm.client.interaction.ProgressBar",
    "ConsoleUtilsTest": "org.hydev.mcpm.utils.ConsoleUtilsTest"
}

if __name__ == '__main__':
    a = argparse.ArgumentParser("run.py", "Java Gradle external runner")
    a.add_argument('classname', help="Full class name (e.g. org.hydev.mcpm.client.Launcher) or alias (e.g. Launcher)")
    cls = a.parse_args().classname

    # Replace alias
    if cls in ALIAS:
        cls = ALIAS[cls]

    # Download JDK
    java = download_jdk.ensure_java("19")

    # Build classes
    check_call("./gradlew classes testClasses", shell=True)

    # Get java runtime classpath
    p = run('./gradlew printCp', shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    cp = p.stderr.decode().strip()

    # Run java
    try:
        cmd = [java, "-cp", cp, cls]
        check_call(cmd)
    except subprocess.CalledProcessError:
        pass


