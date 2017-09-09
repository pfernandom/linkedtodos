/* eslint-env node */
'use strict';

module.exports = function(app) {
  const express = require('express');
  let todosRouter = express.Router();
  const bodyParser = require('body-parser');
  todosRouter.use(bodyParser.json());

  var todos = [{"id":1, "description":"To do something with the server", "completed":false},
    {"id":2, "projectId":2, "description":"To do something else with the server", "completed":false}]

  todosRouter.get('/', function(req, res) {
    res.send(todos);
  });

  todosRouter.post('/', function(req, res) {
    console.log(req.body)
    var result = req.body;
    result.todo.id = Math.random()* 200;
    todos.push(result.todo)

    res.status(201).send(req.body);
  });

  todosRouter.get('/:id', function(req, res) {
    var model = todos.filter(p => p.id == req.params.id);
    res.send(model[0] ? model[0] : {});
  });

  todosRouter.put('/:id', function(req, res) {
    console.log('------',req.body)
    todos = todos.map(p => {
      console.log(p.id == req.params.id)
      return p.id == req.params.id ? Object.assign( {id:p.id}, req.body )  : p

    });
    var model = todos.filter(p => p.id == req.params.id);
    res.send(model[0] ? model[0] : {});
  });

  todosRouter.delete('/:id', function(req, res) {
    res.status(204).end();
  });

  // The POST and PUT call will not contain a request body
  // because the body-parser is not included by default.
  // To use req.body, run:

  //    npm install --save-dev body-parser

  // After installing, you need to `use` the body-parser for
  // this mock uncommenting the following line:
  //
  //app.use('/api/todos', require('body-parser').json());
  app.use('/api/todos', todosRouter);
};
