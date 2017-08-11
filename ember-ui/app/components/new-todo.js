import Ember from 'ember';

/**
 * Component to provide a form to create new todos
 */
export default Ember.Component.extend({
  store: Ember.inject.service(),
  submit(e){
    var newTodo = e.target.elements.newTodo.value;
    var store = this.get('store');

    let todo = store.createRecord('todo', {
      description: newTodo,
      projectId: this.get("projectId"),
      completed: false,
      project: this.get("projectId")
    });

    todo.save().then(todo => {
      this.sendAction('refresh');
    })
      .catch(err => {
        console.error(err);
      });

    e.target.reset();
    e.preventDefault();
  }
});
