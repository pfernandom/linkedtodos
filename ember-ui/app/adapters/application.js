import DS from 'ember-data';
import ENV from "../config/environment";

/**
 * Adapter for Play Framework Gateway.
 * It takes the namespace and host from the environment configurations,
 * and enables CORS.
 */
export default DS.RESTAdapter.extend({
  namespace: ENV.APP.NAMESPACE,
  host: ENV.APP.HOST,
  headers: {
    'Access-Control-Allow-Origin':'*'
  },

  ajax: function(url, method, hash) {
    hash = hash || {}; // hash may be undefined
    hash.crossDomain = true;
    hash.xhrFields = {withCredentials: true};
    return this._super(url, method, hash);
  }

});
