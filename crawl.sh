#!/usr/bin/env bash
set -e

# Get script dir
SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
pushd "$SCRIPT_DIR"

PY="/home/azalea/.conda/envs/310/bin/python3"

# Crawl packages
$PY -m tools.run org.hydev.mcpm.server.SpigetCrawler

# Create database
$PY -m tools.run org.hydev.mcpm.server.spiget.CreateDatabase

# Zstd compression
zstd -f -19 -T36 .mcpm/db -o .mcpm/db.zst1
mv -f .mcpm/db.zst1 .mcpm/db.zst

popd
