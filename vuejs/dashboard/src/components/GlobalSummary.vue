<template>
  <div class="tile is-ancestor mt-3">
    <div class="tile">
      <div class="tile is-parent is-horizontal">
        <SummaryMetricItem
          :countValue="globalSummary.confirmed.value"
          subtitle="Confirmed"
          type="warning"
        ></SummaryMetricItem>
      </div>
      <div class="tile is-parent is-horizontal">
        <SummaryMetricItem
          :countValue="globalSummary.deaths.value"
          subtitle="Deaths"
          type="danger"
        ></SummaryMetricItem>
      </div>
      <div class="tile is-parent is-horizontal">
        <SummaryMetricItem
          :countValue="globalSummary.active.value"
          subtitle="Actives"
          type="primary"
        ></SummaryMetricItem>
      </div>
      <div class="tile is-parent is-horizontal">
        <SummaryMetricItem
          :countValue="globalSummary.recovered.value"
          subtitle="Recovered"
          type="success"
        ></SummaryMetricItem>
      </div>
    </div>
  </div>
</template>

<script>
import SummaryMetricItem from './SummaryMetricItem';

export default {
  name: 'GlobalSummary',
  components: {
    SummaryMetricItem,
  },
  props: {
    globalSummary: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {};
  },
};
</script>
