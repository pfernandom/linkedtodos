import Ember from 'ember';

/**
 * Component to display single todos instances
 */
export default Ember.Component.extend({
  active:false,
  store: Ember.inject.service(),
  doubleClick() {
    this.get('actions').markComplete.bind(this)();
    return true;
  },
  keyPress(event){
    if(event.charCode === 13){
      this.get('actions').markComplete.bind(this)();
    }
    return true;
  },
  actions:{
    deleteTodo(){
      var r = confirm("Delete the Todo?");
      if (r == true) {
        var store = this.get('store');
        store.findRecord('todo', this.get('todo').id, { reload: true }).then(function(todo) {
          todo.destroyRecord();
        });
      }
    },
    markComplete(){
      var store = this.get('store');

      store.findRecord('todo', this.get('todo').id).then(function(todo) {
        todo.set('completed',!todo.get('completed'))
        todo.save();
      });
    }
  }

});
