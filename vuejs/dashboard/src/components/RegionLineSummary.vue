<template>
  <div class="media-content">
    <div class="content">
      <p>
        <strong>{{ name }}</strong>
        <a class="toggleRegion">
          <span
            v-if="selected"
            class="has-text-danger"
            v-on:click="removeEntry()"
          >
            (-)
          </span>
          <span v-else class="has-text-success" v-on:click="addEntry()">
            (+)
          </span>
        </a>
      </p>
      <table class="table is-centered">
        <thead>
          <tr>
            <th>Active</th>
            <th>Confirmed</th>
            <th>Deaths</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>{{ active.toLocaleString() }}</td>
            <td>{{ confirmed.toLocaleString() }}</td>
            <td>{{ deaths.toLocaleString() }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import EventBus from './EventBus';

export default {
  name: 'RegionLineSummary',
  props: {
    name: {
      type: String,
      required: true,
    },
    confirmed: {
      type: Number,
      required: true,
    },
    deaths: {
      type: Number,
      required: true,
    },
    active: {
      type: Number,
      required: true,
    },
    recovered: {
      type: Number,
      required: true,
    },
    selected: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {};
  },
  methods: {
    addEntry: function() {
      EventBus.$emit('ADD_REGION', this.name);
    },
    removeEntry: function() {
      EventBus.$emit('REMOVE_REGION', this.name);
    },
  },
};
</script>

<style>
.toggleRegion {
  text-decoration: none;
  cursor: pointer;
}

.is-centered {
  text-align: center;
}
</style>
