<template>
  <div v-show="loaded.status">
    <line-chart
      ref="chart"
      :chartData="datacollection"
      :options="options"
    ></line-chart>
  </div>
</template>

<script>
import axios from 'axios';
import EventBus from './EventBus';
import LineChart from './LineChart.vue';

const options = {
  responsive: true,
  showAllTooltips: true,
  tooltipEvents: ['mousemove', 'touchstart', 'touchmove'],
  legend: {
    display: true,
    labels: {
      useLineStyle: true,
    },
  },
  title: {
    display: true,
    text: 'Covid 19 Data Points By Region',
  },
  tooltips: {
    mode: 'index',
    intersect: false,
    callbacks: {
      label: function(tooltipItem, data) {
        var label = data.datasets[tooltipItem.datasetIndex].label;
        var datasetLabel =
          data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index];
        return label + ': ' + datasetLabel.y.toLocaleString();
      },
    },
  },
  hover: {
    mode: 'nearest',
    animationDuration: 0,
    intersect: true,
  },
  animation: {
    duration: 1,
  },
  responsiveAnimationDuration: 0,
  scales: {
    yAxes: [
      {
        ticks: {
          beginAtZero: true,
          stacked: false,
          callback: function(label) {
            return label.toLocaleString();
          },
        },
        scaleLabel: {
          display: true,
        },
      },
    ],
    xAxes: [
      {
        type: 'time',
        time: {
          unit: 'day',
        },
      },
    ],
  },
};
export default {
  components: {
    LineChart,
  },
  props: {
    isDailyChart: {
      type: Boolean,
      required: false,
      default: false,
    },
    chartRegions: {
      type: Array,
      required: true,
    },
    metrics: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      options: options,
      loaded: { status: false },
      datacollection: { datasets: [] },
    };
  },
  mounted() {
    EventBus.$on('TOGGLE_METRIC', (metric) => {
      this.toggleMetric(metric);
    });

    console.log(this.$apiData.length + ' ' + this.chartRegions.length);
    if (!this.allRequiredRegionDataLoaded()) {
      console.log('mount and fill data');
      this.fillData(() => this.drawChart());
    } else {
      console.log('mount and draw');
      this.drawChart();
    }
  },
  computed: {
    chartData: function() {
      return this.datacollection;
    },
    isLoaded: function() {
      return this.loaded;
    },
  },
  methods: {
    allRequiredRegionDataLoaded: function() {
      return this.chartRegions.every((v) =>
        this.$apiData.some((a) => a.name.localeCompare(v.name) == 0)
      );
    },
    buildRegionDataSets: function(r, d, m) {
      this.datacollection.datasets.push({
        label: r.name + '-' + m.name,
        fill: false,
        borderColor: r.color,
        backgroundColor: r.color,
        pointHoverBorderColor: m.color,
        pointHoverBackgroundColor: m.color,
        pointRadius: 1,
        pointHitRadius: 1,
        pointBorderWidth: 1,
        pointHoverRadius: 1,
        data: d,
      });
    },
    buildRegionData: function(regionDataPoints, d, metric) {
      regionDataPoints.push({
        t: d.dateRecorded,
        y: this.getMetricValue(d, metric),
      });
    },
    getMetricValue(d, metric) {
      if (metric == this.metrics.CONFIRMED) {
        return d.confirmed.value;
      }
      if (metric == this.metrics.DEATHS) {
        return d.deaths.value;
      }
      if (metric == this.metrics.ACTIVE) {
        return d.active.value;
      }
    },
    getCurrentRegionData: function() {},
    drawChart: function() {
      console.log('drawChart');
      this.$set(this.loaded, 'status', false);
      this.datacollection.datasets = [];
      var regionApiData = {};
      this.chartRegions.forEach((chartRegion) => {
        regionApiData = this.$apiData.filter((apiRegion) => {
          return apiRegion.name.localeCompare(chartRegion.name) == 0;
        })[0];

        Object.values(this.metrics)
          .filter((metric) => metric.status == true)
          .forEach((metric) => {
            var regionDataPoints = [];
            if (!this.isDailyChart) {
              regionApiData.data.forEach((dp) => {
                this.buildRegionData(regionDataPoints, dp, metric);
              });
            } else {
              regionApiData.daily.forEach((dp) => {
                this.buildRegionData(regionDataPoints, dp, metric);
              });
            }

            this.buildRegionDataSets(chartRegion, regionDataPoints, metric);
          });
      });
      this.$set(this.datacollection, 'datasets', this.datacollection.datasets);
      this.$set(this.loaded, 'status', true);
      this.$refs.chart.renderChart(this.datacollection, this.options);
    },
    fillData: function(callback) {
      console.log('fill data started');
      var regionDataLoaded = 0;
      var regionDataToLoad = 0;
      this.chartRegions.forEach((r) => {
        if (!this.$apiData.some((a) => a.name.localeCompare(r.name) == 0)) {
          regionDataToLoad++;
          var dataUrl =
            this.$apiurl + 'c19/data/points/' + r.name + this.$apiurlsuffix;
          axios
            .get(dataUrl)
            .then((response) => {
              this.$apiData.push({
                name: r.name,
                data: response.data,
                daily: this.getDailyDataPoints(response.data),
              });
            })
            .then(() => {
              regionDataLoaded++;
              console.log(
                'data loaded region: ' + r.name + ' count: ' + regionDataLoaded
              );
              if (regionDataToLoad == regionDataLoaded) {
                console.log('call back');
                callback();
              }
            })
            .catch((error) => console.log(error));
        }
      });
    },
    toggleMetric: function(metric) {
      this.metrics[metric.id] = metric;
      this.drawChart();
    },
    getDailyValue: function(d1, d2) {
      let r = d1 - d2;
      if (r < 0) {
        return 0;
      }
      return r;
    },
    getDailyDataPoints: function(data) {
      var arrayLength = data.length;
      var dailyDataPoints = [];
      dailyDataPoints.push({
        deaths: {
          value: 0,
        },
        active: {
          value: 0,
        },
        confirmed: {
          value: 0,
        },
        recovered: {
          value: 0,
        },
        dateRecorded: data[0].dateRecorded,
      });
      for (var i = 1; i < arrayLength; i++) {
        if (i + 1 <= arrayLength) {
          dailyDataPoints.push({
            deaths: {
              value: this.getDailyValue(
                data[i].deaths.value,
                data[i - 1].deaths.value
              ),
            },
            active: {
              value: this.getDailyValue(
                data[i].active.value,
                data[i - 1].active.value
              ),
            },
            confirmed: {
              value: this.getDailyValue(
                data[i].confirmed.value,
                data[i - 1].confirmed.value
              ),
            },
            recovered: {
              value: this.getDailyValue(
                data[i].recovered.value,
                data[i - 1].recovered.value
              ),
            },
            dateRecorded: data[i].dateRecorded,
          });
          continue;
        }

        dailyDataPoints.push({
          deaths: {
            value: this.getDailyValue(
              data[i + 1].deaths.value,
              data[i].deaths.value
            ),
          },
          active: {
            value: this.getDailyValue(
              data[i + 1].active.value,
              data[i].active.value
            ),
          },
          confirmed: {
            value: this.getDailyValue(
              data[i + 1].confirmed.value,
              data[i].confirmed.value
            ),
          },
          recovered: {
            value: this.getDailyValue(
              data[i + 1].recovered.value,
              data[i].recovered.value
            ),
          },
          dateRecorded: data[i].dateRecorded,
        });
      }
      return dailyDataPoints;
    },
  },
};
</script>
