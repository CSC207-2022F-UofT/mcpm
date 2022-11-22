#!/usr/bin/env bash
set -e

# Crawl packages
python3 -m tools.run org.hydev.mcpm.server.crawlers.SpigetCrawler

# Create database
python3 -m tools.run org.hydev.mcpm.server.crawlers.spiget.CreateDatabase

# Zstd compression
rm -rf .mcpm/db.zst
zstd -f -19 -T36 .mcpm/db
