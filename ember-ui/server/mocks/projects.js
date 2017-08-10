/* eslint-env node */
'use strict';

module.exports = function(app) {
  const express = require('express');
  let projectsRouter = express.Router();

  var projects = [{
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
      description: "First project",
      todos:[
        {"id":2, "projectId":2, "description":"To do something else with the server", "completed":false}
      ]
    }]

  projectsRouter.get('/', function(req, res) {
    res.send(projects);
  });

  projectsRouter.post('/', function(req, res) {
    res.status(201).end();
  });

  projectsRouter.get('/:id', function(req, res) {
    var project = projects.filter(p => p.id == req.params.id);
    res.send(project[0] ? project[0] : {});
  });

  projectsRouter.put('/:id', function(req, res) {
    var project = projects.filter(p => p.id === req.params.id);
    res.send(project[0]);
  });

  projectsRouter.delete('/:id', function(req, res) {
    res.status(204).end();
  });

  // The POST and PUT call will not contain a request body
  // because the body-parser is not included by default.
  // To use req.body, run:

  //    npm install --save-dev body-parser

  // After installing, you need to `use` the body-parser for
  // this mock uncommenting the following line:
  //
  //app.use('/api/projects', require('body-parser').json());
  app.use('/api/projects', projectsRouter);
};
