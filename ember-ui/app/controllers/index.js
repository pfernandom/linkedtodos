import Ember from 'ember';

export default Ember.Controller.extend({
  actions:{
    refreshControler(){
      console.log("Refresh controller")
      this.send('refreshRoute');
    }
  }
});
