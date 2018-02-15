const express = require('express')
const path = require('path')
const http = require('http')
const WebSocket = require('ws')
const net = require('net')

const PORT = 1337
const app = express()

app.use(express.static(path.join(__dirname, 'public')))

const server = http.createServer(app)
const wss = new WebSocket.Server({server})

wss.on('connection', function connection(ws, req) {
  const socket = new net.Socket()
  socket.connect(PORT)

  ws.isAlive = true
  ws.socket = socket

  socket.on('data', function(response) {
    ws.send(response.toString())
  })

  socket.on('close', function() {
    console.log('Server disconnected')
    ws.close()
  })

  ws.on('message', function (input) {
    socket.write(input + '\n')
  })

  ws.on('close', function () {
    console.log('Client disconnected')
    socket.destroy()
  })

  ws.on('error', function (err) {
    console.error(err);
  })

  ws.on('pong', function () {
    this.isAlive = true
  })
})

setInterval(function ping() {
  wss.clients.forEach(function each(ws) {
    if (ws.isAlive === false) {
      ws.socket.destroy()
      ws.terminate()
    } else {
      ws.isAlive = false
      ws.ping()
    }
  })
}, 30000)

server.listen(3006, function () {
  console.log('Listening on %d', server.address().port)
})