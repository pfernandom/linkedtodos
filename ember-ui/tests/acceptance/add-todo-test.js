import { test } from 'qunit';
import moduleForAcceptance from 'todo-app/tests/helpers/module-for-acceptance';

moduleForAcceptance('Acceptance | add todo');

test('Adding a ToDo', function(assert) {

  visit('/');
  click('.new-todo a')
  fillIn('.new-todo textarea', 'My new post');
  click('.new-todo button[type="submit"]');


  andThen(function() {
    //assert.equal(currentURL(), '/');
    var panel = find('.todos');
    var reg = /My new post/g

    var content = panel.text();
    var result = reg.exec(content);

    assert.deepEqual(result[0],"My new post", 'Finds the new todo');
  });
});
