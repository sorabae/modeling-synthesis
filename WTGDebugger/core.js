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

  // Graph for choosing test path
  var cy_test_select = window.cy_test_select = cytoscape({
    container: document.getElementById('cy_test_select'),

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

  // Selected result of given WTG
  var cy_test_path = window.cy_test_path = cytoscape({
    container: document.getElementById('cy_test_path'),

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

  var selectedGraph = { nodes: [], edges: [] };

  cy_test_select.on('tap', 'node', function(evt) {
    var node = evt.cyTarget._private.data;

    var index = selectedGraph.nodes.indexOf(node);
    // Check if the node is already in graph
    if (index == -1)
      selectedGraph.nodes.push({ data: node });

    cy_test_path.json({ elements: selectedGraph });
    cy_test_path.elements().layout({ name:'dagre' });
  });

  cy_test_select.on('tap', 'edge', function(evt) {
    var edge = evt.cyTarget._private.data;
    selectedGraph.edges.push({ data: edge });

    var index = selectedGraph.edges.indexOf(edge);
    // Check if the edge is already in graph 
    if (index == -1)
      selectedGraph.edges.push({ data: edge });

    cy_test_path.json({ elements: selectedGraph });
    cy_test_path.elements().layout({ name:'dagre' });
  });

  cy_test_path.on('tap', 'node', function(evt) {
    var node = evt.cyTarget._private.data;
    var id = node.id;
  });

  // var socket = io.connect('http://127.0.0.1:8000');
  // var convertData = function (x, i) { return x.data(); };
  // $("#synthesis-part").click(function () {
  //   socket.emit('Program Synthesis',
  //     {project: $("title").text(), graph: { nodes: cy_part.nodes().map(convertData), edges: cy_part.edges().map(convertData) }});
  // });

  // // TODO: How to synthesize the entire graph?
  // $("#synthesis-all").click(function () {
  //   socket.emit('Program Synthesis',
  //     {project: $("title").text(), graph: { nodes: cy.nodes().map(convertData), edges: cy.edges().map(convertData) }});
  // });
});
