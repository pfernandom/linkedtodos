import DS from 'ember-data';

export default DS.JSONSerializer.extend(DS.EmbeddedRecordsMixin,{
  attrs: {
    todos: { embedded: 'always' }
  }
});
