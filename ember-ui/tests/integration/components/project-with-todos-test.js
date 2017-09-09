import { moduleForComponent, test } from 'ember-qunit';
import hbs from 'htmlbars-inline-precompile';

moduleForComponent('project-with-todos', 'Integration | Component | project with todos', {
  integration: true
});

test('it renders', function(assert) {

  // Set any properties with this.set('myProperty', 'value');
  // Handle any actions with this.on('myAction', function(val) { ... });

  var regEmpty = /Project:(.|[\n\r])*No TODOs to display/g

  this.render(hbs`{{project-with-todos}}`);

  var content = this.$().text().trim();

  assert.deepEqual(regEmpty.exec(content)[0], content, 'The component has correct empty message');

  // Template block usage:
  this.set('project',{
    id: 1,
    name: "Project 1",
    description: "First project",
    todos:[
      {"id":1, "projectId":1, "description":"To do something with the server", "completed":false}
    ]
  })

  this.render(hbs`{{project-with-todos project=project}}`);

  var regFull = /(.|[\n\r])*Project:Project 1(.|[\n\r])*First project(.|[\n\r])*To do something with the server(.|[\n\r])*Completed\? false(.|[\n\r])*/g
  assert.ok(regFull.exec(this.$().text().trim()), 'The component has correct project information');
});
