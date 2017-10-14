const WebSocket = require('ws');
const ip = require('ip');

const wss = new WebSocket.Server({ port: 5555 });

console.log("server started in " + ip.address())

// Broadcast to all.
wss.broadcast = function broadcast(data) {
  wss.clients.forEach(function each(client) {
    if (client.readyState === WebSocket.OPEN) {
      client.send(data);
    }
  });
};

wss.on('connection', function connection(ws) {
  ws.on('message', function incoming(data) {
    // Broadcast to everyone else.
    console.log("sending " + data + " to " + JSON.parse(data).device)
    wss.clients.forEach(function each(client) {
      client.send(data);
    });
  });
});
