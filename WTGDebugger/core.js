$(function () {
  var cy = window.cy = cytoscape({
    container: document.getElementById('cy'),

    // boxSelectionEnabled: false,
    // autounselectify: true,

    layout: {
      name: 'dagre'
    },

    style: cytoscape.stylesheet()
      .selector('node')
        .css({
        'shape': 'rectangle',
        'content': 'data(id)',
        'text-valign': 'center',
        'text-outline-width': 2,
        'text-outline-color': '#B3767E',
        'background-color': '#B3767E',
        'color': '#fff'
      })
      .selector('edge')
        .css({
          'curve-style': 'bezier',
          'opacity': 0.8,
          'target-arrow-shape': 'triangle',
          'line-color': '#F2B1BA',
          'target-arrow-color': '#F2B1BA',
          'content': 'data(content)'
      })
      .selector(':selected')
        .css({
          'background-color': 'black',
          'line-color': 'black',
          'target-arrow-color': 'black',
          'opacity': 1
      })
      .selector('.faded')
        .css({
          'opacity': 0.25,
          'text-opacity': 0
      }),

    elements: {
      nodes: wtg_DB.nodes,
      edges: wtg_DB.edges
    }
  });

  var cy_part = window.cy_part = cytoscape({
    container: document.getElementById('cy_part'),

    boxSelectionEnabled: true,
    autounselectify: true,

    layout: {
      name: 'dagre'
    },

    style: cytoscape.stylesheet()
      .selector('node')
        .css({
        'shape': 'rectangle',
        'content': 'data(id)',
        'text-valign': 'center',
        'text-outline-width': 2,
        'text-outline-color': '#6272A3',
        'background-color': '#6272A3',
        'color': '#fff'
      })
      .selector('edge')
        .css({
          'curve-style': 'bezier',
          'opacity': 0.8,
          'target-arrow-shape': 'triangle',
          'line-color': '#B1C1F2',
          'target-arrow-color': '#B1C1F2',
          'content': 'data(content)'
      })
      .selector(':selected')
        .css({
          'background-color': 'black',
          'line-color': 'black',
          'target-arrow-color': 'black',
          'opacity': 1
      })
      .selector('.faded')
        .css({
          'opacity': 0.25,
          'text-opacity': 0
      }),

    elements: {
      nodes: [],
      edges: []
    }
  });


  cy.on('tap', 'node', function(evt){
    var node = evt.cyTarget;
    var edges = node.connectedEdges().filter(function (i, e) { return e.source() == node});

    var nodes = edges.map(function (e, i) { return {data: e.target().data()}; });
    nodes.push({data: node.data()});


    cy_part.json({ elements:
      {
        nodes: nodes,
         edges: edges.map(function (e, i) { return {data: e.data()}; })
      }
    });
    cy_part.elements().layout({name: 'dagre'});
  });

  var socket = io.connect('http://127.0.0.1:8000');
  var convertData = function (x, i) { return x.data(); };
  $("#synthesis-part").click(function () {
    socket.emit('Program Synthesis', { nodes: cy_part.nodes().map(convertData), edges: cy_part.edges().map(convertData) });
  });

  // TODO: How to synthesize the entire graph?
  $("#synthesis-all").click(function () {
    socket.emit('Program Synthesis', { nodes: cy.nodes().map(convertData), edges: cy.edges().map(convertData) });
  });

});
