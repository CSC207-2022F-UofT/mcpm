#!/usr/bin/env python3
from pathlib import Path

from ruamel.yaml import YAML


def gen_pyml_deps():
    """
    Generate plugin.yml libraries from build.gradle
    """
    ln = [l.strip() for l in Path("build.gradle").read_text().splitlines()]
    ln = [l.lstrip("implementation ").strip("'").strip('"') for l in ln if l.startswith("implementation ")]
    with open("src/main/resources/plugin.yml") as f:
        yml = YAML().load(f)
    yml["libraries"] = ln
    with open("src/main/resources/plugin.yml", "w") as f:
        YAML().dump(yml, f)


if __name__ == '__main__':
    gen_pyml_deps()
