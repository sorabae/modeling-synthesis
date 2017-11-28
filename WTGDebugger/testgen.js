$(function () {
  var gen_btn = document.getElementById('generate_test');

  gen_btn.addEventListener('click', () => {
    var selected = document.getElementById('cy_test_path');
    var cy = cy_test_path;

    var nodes = cy.nodes();
    for (var i=0; i<nodes.length; i++) {
      console.log(nodes[i].data());
    }

    var edges = cy.edges();
    for (var i=0; i<edges.length; i++) {
      console.log(edges[i].data());
    }

    writeJSON('selected.json', nodes, edges);
  });

  function writeJSON (filename, nodes, edges) {
    // var file = new File([], filename);

    // nodes.forEach((node) => { file.write(JSON.stringify(node.id)); });
    // file.close();
  }
});
