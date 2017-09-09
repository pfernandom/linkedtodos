import { moduleForComponent, test } from 'ember-qunit';
import hbs from 'htmlbars-inline-precompile';

moduleForComponent('new-todo', 'Integration | Component | new todo', {
  integration: true
});

test('it renders', function(assert) {

  // Set any properties with this.set('myProperty', 'value');
  // Handle any actions with this.on('myAction', function(val) { ... });

  this.render(hbs`{{new-todo}}`);

  var regEmpty = /(.|[\n\r])*Add TODO(.|[\n\r])*Thing to do:(.|[\n\r])*/g

  var content = this.$().text().trim();

  assert.deepEqual(regEmpty.exec(content)[0], content, 'Component displays correctly');

});
