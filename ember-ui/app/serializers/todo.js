import DS from 'ember-data';

export default DS.JSONSerializer.extend({
  extractDeleteRecord: function(store, type, payload) {
    return null;
  }
});
