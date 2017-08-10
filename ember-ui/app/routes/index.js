import Ember from 'ember';

export default Ember.Route.extend({
  model() {
    return this.get('store').findAll('project');
  },
  actions: {
    refreshRoute() {
      console.log("Refresh route")
      this.refresh();
    },
  }
});
