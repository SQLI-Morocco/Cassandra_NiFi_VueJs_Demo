import pandas as pd
import numpy as np
import os

rootdir = '/Users/bibi/Sources/covid19/data/jhp-data-git/csse_covid_19_data/csse_covid_19_daily_reports/'
targetdir = '/Users/bibi/Sources/covid19/data/jhp-data-git/csse_covid_19_data/csse_covid_19_daily_reports/cleaned'
extensions = ('.csv')

for subdir, dirs, files in os.walk(rootdir):
    for file in files:
        ext = os.path.splitext(file)[-1].lower()
        if ext in extensions:
            data = pd.read_csv(os.path.join(subdir, file), dtype=str, encoding='latin-1')
            data.fillna('0', inplace = True)
            data.to_csv(os.path.join(targetdir, file), index=False)