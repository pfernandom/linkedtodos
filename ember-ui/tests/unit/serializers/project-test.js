import { moduleForModel, test } from 'ember-qunit';
import Pretender from 'pretender';

var server;

moduleForModel('project', 'Unit | Serializer | project', {
  needs: ['adapter:application','serializer:project','model:todo'],
  beforeEach() {
    server = new Pretender(function() {
      this.get('/api/projects', function() {
        var response = [
          {
            id: 1,
            name: "Project 1",
            description: "First project",
            todos:[
              {"id":1, "projectId":1, "description":"To do something with the server", "completed":false}
            ]
          },
          {
            id: 2,
            name: "Project 2",
            description: "Second project",
            todos:[
              {"id":2, "projectId":2, "description":"To do something with the server", "completed":false}
            ]
          }
        ];

        return [200, { "Content-Type": "application/json" }, JSON.stringify(response)];
      });
    });
  },
  afterEach() {
    if (server) {
      server.shutdown();
    }
    server = null;
  }
});

// Replace this with your real tests.
test('it serializes records', function(assert) {
  let record = this.subject();

  let serializedRecord = record.serialize();
  //http://localhost:4200/api/projects
  assert.ok(serializedRecord);
});


test('it serializes array responses', function(assert) {
  return this.store().findAll('project').then((projects) => {
    var projArr = projects.toArray();

    console.log(projArr)

    var firstTodo = projArr[0].get('todos').toArray()[0];

    assert.equal(projects.get('length'), 2, "The expected projecs are 2");
    assert.equal(projArr[0].get('name'),'Project 1')
    assert.equal(projArr[0].get('description'),'First project')
    assert.equal(firstTodo.get('description'),'To do something with the server');
  });
});
