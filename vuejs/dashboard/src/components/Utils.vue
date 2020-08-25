<script>
import Vue from 'vue';
const Utils = new Vue();

const sort = (a, comp) => {
  let start = new Date().getMilliseconds();
  a.sort(comp);
  let end = new Date().getMilliseconds();
  console.log('sort duration: ' + (end - start) / 1000);
};

export { sort };
export default Utils;
</script>
