import { module } from 'qunit';
import Ember from 'ember';
import startApp from '../helpers/start-app';
import destroyApp from '../helpers/destroy-app';
import Pretender from 'pretender';

var server;

const { RSVP: { resolve } } = Ember;

var projects = [
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

export default function(name, options = {}) {
  module(name, {
    beforeEach() {
      this.application = startApp();

      if (options.beforeEach) {
        return options.beforeEach.apply(this, arguments);
      }

      server = new Pretender(function() {
        this.get('/api/projects', function() {
          return [200, { "Content-Type": "application/json" }, JSON.stringify(projects)];
        });

        this.post('/api/todos', function(req) {
          console.log("Req",req)
          var result = JSON.parse(req.requestBody);
          result.todo.id = Math.random()* 200;

          for(var i in projects){
            if(projects.hasOwnProperty(i)){
              var p = projects[i];
              if(p.id == result.todo.projectId){
                p.todos.push(result.todo)
              }
            }
          }

          return [200, { "Content-Type": "application/json" }, JSON.stringify(result)];
        });

      });

    },

    afterEach() {

      if (server) {
        server.shutdown();
      }
      server = null;


      let afterEach = options.afterEach && options.afterEach.apply(this, arguments);
      return resolve(afterEach).then(() => destroyApp(this.application));
    }
  });
}
