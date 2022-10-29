import os
from collections import Counter
from pathlib import Path
from subprocess import check_output

from hypy_utils.tqdm_utils import pmap


def get_file(fp: Path):
    return check_output(['file', fp]).decode().split(":", 1)[1].strip()


if __name__ == '__main__':
    base = Path(".mcpm/crawler/spiget/dl-cache/latest/")
    files = [base / f for f in os.listdir(base)]
    files = pmap(get_file, files)
    print("\n".join([f"{ty.ljust(100)} {count}" for ty, count in sorted(Counter(files).items(), key=lambda x: -x[1])]))
