import { moduleForComponent, test } from 'ember-qunit';
import hbs from 'htmlbars-inline-precompile';

moduleForComponent('simple-todo', 'Integration | Component | simple todo', {
  integration: true
});

test('it renders', function(assert) {

  // Set any properties with this.set('myProperty', 'value');
  // Handle any actions with this.on('myAction', function(val) { ... });
  this.set('todo',{"id":1, "description":"To do something with the server", "completed":false})
  this.render(hbs`{{simple-todo todo=todo}}`);

  var reg = /To do something with the server(.|[\r\n])*Completed\?(.*)false/g

  assert.ok(reg.test(this.$().text()),"Finds the expected content text");
});
