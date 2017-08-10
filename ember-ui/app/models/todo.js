import DS from 'ember-data';

export default DS.Model.extend({
  description: DS.attr(),
  projectId: DS.attr(),
  completed: DS.attr('boolean'),
  project: DS.belongsTo('project',{ async: false })
});
