#!/usr/bin/env bash
SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )

# Create links
sudo ln -sf "$SCRIPT_DIR/mcprs-crawl.service" "/etc/systemd/system/mcprs-crawl.service"
sudo ln -sf "$SCRIPT_DIR/mcprs-crawl.timer" "/etc/systemd/system/mcprs-crawl.timer"

# Enable sctl
sudo systemctl daemon-reload
sudo systemctl enable mcprs-crawl.timer