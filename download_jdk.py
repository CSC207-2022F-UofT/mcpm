import os
import platform
import shutil
import tarfile
import zipfile
from pathlib import Path
from tempfile import TemporaryDirectory
from typing import Literal

import requests
import tqdm
from cpuinfo import get_cpu_info
from hypy_utils import ensure_dir, printc
from hypy_utils.downloader import download_file

# Windows / Linux / Darwin
sys = platform.system()

# "X86_32", "X86_64", "ARM_8", "ARM_7", "PPC_32", "PPC_64", "SPARC_32",
# "SPARC_64", "S390X", "MIPS_32", "MIPS_64", "RISCV_32", "RISCV_64"
arch = get_cpu_info()['arch']

print('Detected system:', sys, arch)

JAVA = Literal['19', '18', '17', '16', '15', '14', '13', '12', '11', '10', '9', '1.8', '1.7']


def download_oracle(java_ver: JAVA, path: str | Path):
    path = Path(path)

    # Normalize OS and architecture
    s = sys.lower()
    if s == 'darwin':
        s = 'macos'
    assert s in ['linux', 'macos', 'windows'], f'Unsupported OS for Oracle JDK: {s}'

    a = arch.lower()
    if a == 'x86_64' or a == 'x86_32':
        a = 'x64'
    if a == 'ARM_8' or a == 'ARM_7':
        a = 'aarch64'
    assert a in ['x64', 'aarch64'], f'Unsupported CPU architecture for Oracle JDK: {a}'

    # Create URL
    ext = 'tar.gz' if s in ['linux', 'macos'] else 'zip'
    fname = f'jdk-{java_ver}_{s}-{a}_bin.{ext}'
    url = f'https://download.oracle.com/java/{java_ver}/latest/{fname}'

    # Download
    with TemporaryDirectory() as tmp:
        tmp = Path(tmp)
        file = tmp / fname
        extract = ensure_dir(tmp / 'extract')

        print('Downloading JDK...')
        download_file(url, file)

        print('Extracting JDK...')
        if fname.endswith('tar.gz'):
            with tarfile.open(file) as f:
                f.extractall(extract)
        elif fname.endswith('zip'):
            with zipfile.ZipFile(file, 'r') as f:
                f.extractall(extract)

        print('Checking bin')
        if not (extract / 'bin').is_dir():
            dirs = [extract / f for f in os.listdir(extract) if (extract / f).is_dir()]
            assert len(dirs) == 1, 'Error: Unknown file structure.'
            extract = dirs[0]

        print('Installing JDK...')
        ensure_dir(path.parent)
        shutil.move(extract, path)

        print(f'Done! Oracle JDK {java_ver} downloaded to {path}')

