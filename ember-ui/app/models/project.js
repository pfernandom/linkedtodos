import DS from 'ember-data';

export default DS.Model.extend({
  name: DS.attr(),
  description: DS.attr(),
  todos: DS.hasMany('todo', { async: false })
});
