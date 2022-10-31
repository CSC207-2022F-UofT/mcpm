import json
import numpy as np
from hypy_utils.scientific_utils import calc_col_stats
from matplotlib import pyplot as plt
from pathlib import Path

if __name__ == '__main__':
    j = json.loads(Path(".mcpm/crawler/spiget/resources.json").read_text())

    downloads = np.array([v['downloads'] for v in j])
    # print(Counter(downlaods))
    print(calc_col_stats(downloads))

    # hist, bins, _ = plt.hist(downloads, bins=50, log=True)
    # plt.close()
    logbins = np.geomspace(1, max(downloads), 50)
    # logbins = np.logspace(np.log10(bins[0]), np.log10(bins[-1]), len(bins))
    plt.hist(downloads, bins=logbins, log=True)
    plt.xscale('log')
    # plt.xlim(0, 8000)
    # plt.tick(style='plain')
    plt.xlabel("Number of downloads")
    plt.ylabel("Number of occurrences (for each histogram bin)")
    plt.title("Histogram for number of downloads for each plugin")
    plt.show()
    # plt.boxplot(downloads)
    # plt.show()
