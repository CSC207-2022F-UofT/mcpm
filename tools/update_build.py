from pathlib import Path

from tools.start_server import update_build

if __name__ == '__main__':
    mc_path = Path('build/mc-server')
    update_build(mc_path)
