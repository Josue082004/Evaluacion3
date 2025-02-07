var nodes = new vis.DataSet([
  {id: 1, label:"RouterA"},
  {id: 2, label:"RouterB"},
  {id: 3, label:"RouterC"},
  {id: 4, label:"RouterD"},
  {id: 5, label:"RouterE"},
  {id: 6, label:"RouterF"},
  {id: 7, label:"RouterG"},
  {id: 8, label:"RouterH"},
  {id: 9, label:"RouterI"},
  ]);
  var edges = new vis.DataSet([
  {from: 1, to: 2, label: "1.4328358E-6"},
  {from: 1, to: 3, label: "8.9663763E-7"},
  {from: 1, to: 6, label: "1.2735471E-6"},
  {from: 1, to: 5, label: "1.3998248E-6"},
  {from: 1, to: 7, label: "1.3998248E-6"},
  {from: 1, to: 4, label: "1.4025519E-6"},
  {from: 2, to: 1, label: "1.4328358E-6"},
  {from: 3, to: 1, label: "8.9663763E-7"},
  {from: 4, to: 1, label: "1.4025519E-6"},
  {from: 5, to: 1, label: "1.3998248E-6"},
  {from: 6, to: 1, label: "1.2735471E-6"},
  {from: 7, to: 1, label: "1.3998248E-6"},
  {from: 8, to: 1, label: "1.4328358E-6"},
  {from: 9, to: 1, label: "1.4328358E-6"},
  ]);
  var container = document.getElementById("mynetwork");
  var data = {
  nodes: nodes,
  edges: edges,
  };
  var options = {
    edges: {
      font: { align: 'middle' },
      arrows: 'to' // Mostrar flechas en las aristas
    }
  };
  var network = new vis.Network(container, data, options);
  