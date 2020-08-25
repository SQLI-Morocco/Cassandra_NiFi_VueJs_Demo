<template>
  <div id="app">
    <div
      id="YacovidaContainer"
      class="container top-data-box"
      :class="{ 'is-fluid': isWide }"
    >
      <div class="notification">
        <section class="hero is-primary is-small is-bold">
          <div class="hero-body">
            <div class="container is-pulled-left">
              <h1 class="title">
                Yacovida
              </h1>
              <h4 class="subtitle">
                Yet Another Covid 19 Dashboard!
              </h4>
              <ul>
                <li>
                  Basic Demo of Data Dashboard using Cassandra, Nifi, VueJS,
                  SpringBoot/SpringData and Bulma
                </li>
                <br />
                <li>
                  This demo is for education/training purpose, for official data
                  and information on COVID19 please refer to
                  <a href="https://www.who.int/" target="_blank">WHO</a> and
                  <a href="https://coronavirus.jhu.edu/map.html" target="_blank"
                    >JHU Dashboard</a
                  >
                </li>
              </ul>
              <div class="container">
                <div class="is-pulled-right">Data as of: {{ lastUpdate }}</div>
              </div>
            </div>
          </div>
        </section>
        <GlobalSummary :globalSummary="globalSummary"></GlobalSummary>
        <div class="tile is-ancestor reverse-display ">
          <div
            class="tile is-vertical is-parent is-3"
            style="min-height:100%; overflow: scroll"
          >
            <p class="title">Chart Countries</p>
            <div class="tile is-child box">
              <article
                v-for="region in selectedRegionsSummary"
                :key="region.name"
                class="media"
              >
                <RegionLineSummary
                  :name="region.name"
                  :confirmed="region.summary.confirmed.value"
                  :deaths="region.summary.deaths.value"
                  :active="region.summary.active.value"
                  :recovered="region.summary.recovered.value"
                  :selected="true"
                ></RegionLineSummary>
              </article>
              <br />
            </div>
            <i id="countryFilterBlock"></i>
            <br />
            <p class="title">
              All Countries
            </p>
            <p class="control has-icons-left">
              <input
                class="input"
                type="text"
                placeholder="Search Country..."
                v-model="countryFilterInput"
                v-on:input="searchCountry"
              />
              <span class="icon is-left">
                <i class="fas fa-search" aria-hidden="true"></i>
              </span>
            </p>
            <p class="title"></p>
            <div
              class="tile is-child box country-selector"
              style="min-height:100%; overflow: scroll"
            >
              <article
                ref="allCountries"
                v-for="region in regionsSummary"
                :key="region.name"
                :data-key="region.name"
                class="media"
              >
                <RegionLineSummary
                  :name="region.name"
                  :confirmed="region.summary.confirmed.value"
                  :deaths="region.summary.deaths.value"
                  :active="region.summary.active.value"
                  :recovered="region.summary.recovered.value"
                ></RegionLineSummary>
              </article>
            </div>
          </div>
          <div ref="chartBlock" class="tile is-vertical">
            <p class="title">Chart</p>
            <div
              class="tile is-horizontal is-12"
              :class="{ 'is-parent': isWide }"
            >
              <div
                class="tile is-child box chart-box"
                :class="{ 'is-10': isWide }"
              >
                <nav class="panel">
                  <p class="panel-tabs">
                    <a
                      key="overTime"
                      :class="{ 'is-active': isOverTimePanel }"
                      v-on:click="selectOverTimePanel"
                      >Cumulative Data</a
                    >
                    <a
                      key="daily"
                      :class="{ 'is-active': isDailyPanel }"
                      v-on:click="selectDailyPanel"
                      >Daily Data</a
                    >
                  </p>
                  <a
                    class="panel-block"
                    :class="{ 'is-active': isOverTimePanel }"
                  >
                    <div
                      v-if="loaded && isOverTimePanel"
                      class="container"
                      :class="{ 'is-fluid': isWide }"
                    >
                      <RegionChart
                        :key="c1"
                        :metrics="metrics"
                        :chartRegions="selectedRegionsSummary"
                      ></RegionChart>
                    </div>
                  </a>
                  <a class="panel-block" :class="{ 'is-active': isDailyPanel }">
                    <div
                      v-if="loaded && isDailyPanel"
                      class="container"
                      :class="{ 'is-fluid': isWide }"
                    >
                      <RegionChart
                        :key="c2"
                        :isDailyChart="true"
                        :metrics="metrics"
                        :chartRegions="selectedRegionsSummary"
                      ></RegionChart>
                    </div>
                  </a>
                </nav>
              </div>

              <div class="tile is-vertical is-2 metric-filter">
                <div class="tile is-child box" style="min-width: 175px;">
                  <div class="field">
                    <input
                      id="switchRoundedInfo"
                      type="checkbox"
                      name="switchRoundedInfo"
                      class="switch is-rounded is-info"
                      :checked="metrics.ACTIVE.status"
                      v-on:click="toggleMetric(metrics.ACTIVE)"
                    />
                    <label for="switchRoundedInfo">Active</label>
                  </div>
                  <div class="field">
                    <input
                      id="switchRoundedSuccess"
                      type="checkbox"
                      name="switchRoundedSuccess"
                      class="switch is-rounded is-success"
                      :checked="metrics.CONFIRMED.status"
                      v-on:click="toggleMetric(metrics.CONFIRMED)"
                    />
                    <label for="switchRoundedSuccess">Confirmed</label>
                  </div>
                  <div class="field">
                    <input
                      id="switchRoundedDanger"
                      type="checkbox"
                      name="switchRoundedDanger"
                      class="switch is-rounded is-danger"
                      :checked="metrics.DEATHS.status"
                      v-on:click="toggleMetric(metrics.DEATHS)"
                    />
                    <label for="switchRoundedDanger">Deaths</label>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <Footer />
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import Footer from './components/Footer';
import EventBus from './components/EventBus';
import RegionChart from './components/RegionChart.vue';
import GlobalSummary from './components/GlobalSummary.vue';
import RegionLineSummary from './components/RegionLineSummary.vue';

import * as Utils from './components/Utils';

const defaultRegions = ['Morocco', 'Bahrain', 'Brazil', 'India', 'France'];
const metrics = {
  ACTIVE: { id: 'ACTIVE', name: 'Active', status: true, color: '#209cee' },
  CONFIRMED: {
    id: 'CONFIRMED',
    name: 'Confirmed',
    status: false,
    color: '#23d160',
  },
  DEATHS: { id: 'DEATHS', name: 'Deaths', status: false, color: '#ff3860' },
};

export default {
  name: 'App',
  components: {
    Footer,
    RegionChart,
    GlobalSummary,
    RegionLineSummary,
  },
  data() {
    return {
      globalSummary: {
        confirmed: {
          value: 0,
        },
        active: {
          value: 0,
        },
        deaths: {
          value: 0,
        },
        recovered: {
          value: 0,
        },
        lastUpdate: 0,
      },
      c1: Math.round(Math.random() * 888),
      c2: Math.round(Math.random() * 888),
      loaded: false,
      metrics: metrics,
      regionsSummary: {},
      chartParentNode: null,
      countryFilterInput: '',
      activePanel: 'OverTime',
      selectedRegionsSummary: [],
      defaultRegions: defaultRegions,
      windowWidth: window.innerWidth,
    };
  },
  created() {
    axios
      .get(this.$apiurl + 'c19/data/summary' + this.$apiurlsuffix)
      .then((response) => {
        this.globalSummary = response.data;
      })
      .catch((error) => console.log(error));
    axios
      .get(this.$apiurl + 'c19/data/summary/regions' + this.$apiurlsuffix)
      .then((response) => {
        this.regionsSummary = response.data;
        this.regionsSummary.forEach((r) => {
          r.color = this.randomColor();
        });
        Utils.sort(
          this.regionsSummary,
          (r1, r2) => r2.summary.confirmed.value - r1.summary.confirmed.value
        );
        defaultRegions.forEach((c) => {
          var sr = this.regionsSummary.splice(
            this.regionsSummary.findIndex((r) => {
              return c == r.name;
            }),
            1
          );
          this.selectedRegionsSummary = this.selectedRegionsSummary.concat(sr);
        });
      })
      .then(() => (this.loaded = true))
      .catch((error) => console.log(error));

    EventBus.$on('ADD_REGION', (region) => {
      this.selectedRegionsSummary.push(
        this.regionsSummary.splice(
          this.regionsSummary.findIndex((r) => {
            return r.name == region;
          }),
          1
        )[0]
      );
      this.clearSearch();
      EventBus.$emit('FILL_REDRAW_CHART');
      this.forceRemount();
    });
    EventBus.$on('REMOVE_REGION', (region) => {
      this.regionsSummary.push(
        this.selectedRegionsSummary.splice(
          this.selectedRegionsSummary.findIndex((r) => {
            return r.name == region;
          }),
          1
        )[0]
      );
      Utils.sort(
        this.regionsSummary,
        (r1, r2) => r2.summary.confirmed.value - r1.summary.confirmed.value
      );
      this.clearSearch();
      EventBus.$emit('REDRAW_CHART');
      this.forceRemount();
    });
  },
  mounted() {
    this.$nextTick(() => {
      window.addEventListener('resize', this.onResize);
    });

    this.resetChartPosition();
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.onResize);
  },
  computed: {
    lastUpdate: function() {
      return new Date(this.globalSummary.lastUpdate).toUTCString();
    },
    isOverTimePanel: function() {
      return this.activePanel == 'OverTime';
    },
    isDailyPanel: function() {
      return this.activePanel == 'Daily';
    },
    isWide: function() {
      return this.windowWidth > 960;
    },
  },
  methods: {
    onResize() {
      this.windowWidth = window.innerWidth;

      this.resetChartPosition();
    },
    clearSearch: function() {
      this.countryFilterInput = '';
      this.searchCountry();
    },
    toggleMetric: function(metric) {
      for (var key of Object.keys(this.metrics)) {
        if (this.metrics[key] == metric) {
          this.metrics[key].status = !metric.status;
        } else {
          this.metrics[key].status = false;
        }
      }
      EventBus.$emit('TOGGLE_METRIC', metric);
      this.forceRemount();
    },
    forceRemount: function() {
      this.c1 = !this.c1;
      this.c2 = !this.c2;
    },
    selectOverTimePanel: function() {
      this.activePanel = 'OverTime';
    },
    selectDailyPanel: function() {
      this.activePanel = 'Daily';
    },
    randomColor: function() {
      var letters = '0123456789ABCDEF';
      var color = '#';
      for (var i = 0; i < 6; i++) {
        color += letters[Math.floor(Math.random() * 16)];
      }
      return color;
    },
    searchCountry: function() {
      this.$refs.allCountries.forEach((r) => {
        if (
          r.dataset['key']
            .toLowerCase()
            .indexOf(this.countryFilterInput.toLowerCase()) > -1
        ) {
          r.style.display = 'block';
        } else {
          r.style.display = 'none';
        }
      });
    },
    resetChartPosition: function() {
      if (!this.chartParentNode) {
        this.chartParentNode = this.$refs.chartBlock.parentNode;
      }

      if (this.windowWidth < 960) {
        this.$refs.chartBlock.parentNode.removeChild(this.$refs.chartBlock);
        var referenceNode = document.getElementById('countryFilterBlock');
        referenceNode.parentNode.insertBefore(
          this.$refs.chartBlock,
          referenceNode.nextSibling
        );
      } else {
        this.chartParentNode.appendChild(this.$refs.chartBlock);
      }
    },
  },
};
</script>

<style>
@import './assets/bulma.css';
@import './assets/bulma-extensions.min.css';
@import './assets/styles.css';
</style>
