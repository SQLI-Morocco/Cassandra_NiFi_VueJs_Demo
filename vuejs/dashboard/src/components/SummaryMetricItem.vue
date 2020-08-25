<template>
  <article class="tile is-child notification" :class="isType">
    <p class="subtitle has-text-centered">{{ subtitle }}</p>
    <p class="title has-text-centered">{{ countValue.toLocaleString() }}</p>
  </article>
</template>

<script>
const types = ['danger', 'primary', 'warning', 'success'];
export default {
  name: 'SummaryMetricItem',
  props: {
    countValue: {
      type: Number,
      required: true,
    },
    subtitle: {
      type: String,
      required: true,
    },
    type: {
      type: String,
      required: true,
      validator: (val) => types.includes(val),
    },
  },
  data() {
    return {};
  },
  computed: {
    isType() {
      return 'is-' + this.type;
    },
  },
};
</script>
