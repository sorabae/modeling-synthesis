var io = require('socket.io').listen(8000);
var fs = require('fs');
var python = require('python-shell');

io.sockets.on('connection', function (socket) {
  socket.on('Program Synthesis', function (data) {
    var json = JSON.stringify(data);
    fs.writeFile('subgraph.json', json, 'utf8', function (err) {
      if (err) throw err;

      var scriptPath = '/Users/sorabae/Research/gator-3.3/AndroidBench';
      var options = {
        scriptPath: scriptPath,
        args: ['-j', 'cgo.json',
               '-p', 'apv',
               '--base_dir', scriptPath,
               '--base_client', 'ProgramSynthesisClient']
      }
      python.run('runGatorClient.py', options, function (err, result) {
        if (err) throw err;
        console.log(result);
      });
    });
    // console.log(data.nodes);
    // console.log(data.edges);
  });
});
