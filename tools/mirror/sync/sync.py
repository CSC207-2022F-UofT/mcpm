import os
import shlex
import time
from subprocess import Popen

import schedule

RSYNC_ARGS = shlex.split("-rlptH --safe-links --delete-delay --delay-updates --timeout=600 --contimeout=60 --no-motd")


if __name__ == '__main__':
    print("Mirror starting...")

    # Check environment variables
    SOURCE = os.environ.get("RSYNC_SOURCE")
    INTERVAL = os.environ.get("RSYNC_INTERVAL")
    assert SOURCE is not None, "Please set RSYNC_SOURCE environment variable: " \
                               "The source you want to sync from."
    assert INTERVAL is not None, "Please set RSYNC_INTERVAL environment variable: " \
                                 "How many hours should rsync wait between updates."
    INTERVAL = int(INTERVAL)
    assert INTERVAL >= 6, f"Error: RSYNC_INTERVAL should not be set to {INTERVAL} < 6 hours. " \
                          f"Please don't update that frequently, it occupies a lot of server bandwidth"

    # Optional environment variables
    EXTRA_ARGS = os.environ.get("RSYNC_EXTRA_ARGS") or ""

    # Check write permission
    assert os.access('/data', os.W_OK), "/data is not writable. Please check your docker volume config.\n" \
                                        "If SELinux is enabled, you might need to set proper permissions or disable it."

    print(f"Mirror Initialized! Sync from {SOURCE} to /data every {INTERVAL} hours")

    # Schedule task
    def rsync():
        cmd = ["rsync", *RSYNC_ARGS, SOURCE, *shlex.split(EXTRA_ARGS), "/data"]
        print(f"Starting rsync with {' '.join(cmd)}")
        Popen(cmd).wait()
        print("rsync finished.")

    schedule.every(INTERVAL).hours.do(rsync).run()

    print("Starting scheduler...")

    while 1:
        schedule.run_pending()
        time.sleep(10)

