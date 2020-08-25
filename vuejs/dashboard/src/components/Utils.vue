<script>
import Vue from 'vue';
const Utils = new Vue();

const sort = (a, comp) => {
  let start = new Date().getMilliseconds();
  a.sort(comp);
  let end = new Date().getMilliseconds();
  console.log('sort duration: ' + (end - start) / 1000);
};

const bbsort = (a, comp) => {
  if (!a || a.length < 2) {
    return a;
  }

  var n = a.length;
  for (let i = 0; i < n; i++) {
    for (let j = 0; j < n - 1; j++) {
      if (comp(a[i], a[j]) > 0) {
        swap(a, i, j);
      }
    }
  }
};

const swap = (a, i, j) => {
  let tmp = a[i];
  a[i] = a[j];
  a[j] = tmp;
};

export { sort, bbsort };
export default Utils;
</script>
