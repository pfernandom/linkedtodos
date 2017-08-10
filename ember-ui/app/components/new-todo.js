import Ember from 'ember';

export default Ember.Component.extend({
  store: Ember.inject.service(),
  submit(e){
    var newTodo = e.target.elements.newTodo.value;
    var store = this.get('store');

    console.log(this.get("projectId"))

    let todo = store.createRecord('todo', {
      description: newTodo,
      projectId: this.get("projectId"),
      completed: false,
      project: this.get("projectId")
    });

    todo.save().then(todo => {
      try{
        this.sendAction('refreshRoute');
      }
      catch(err){
        console.error(err)
      }
    });


    e.target.reset();
    e.preventDefault();
  }
});
