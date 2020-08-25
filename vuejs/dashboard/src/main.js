import Vue from 'vue';
import Yacovida from './Yacovida.vue';

import '@fortawesome/fontawesome-free/css/all.css';
import '@fortawesome/fontawesome-free/js/all.js';

Vue.config.cached = false;
Vue.prototype.$apiurl = Vue.config.cached ? '' : 'http://localhost:8082/';
Vue.prototype.$apiurlsuffix = Vue.config.cached ? '.json' : '';

Vue.prototype.$apiData = [];

new Vue({
  render: (h) => h(Yacovida),
}).$mount('#app');
