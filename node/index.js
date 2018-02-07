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
})

server.listen(3006, function () {
  console.log('Listening on %d', server.address().port)
})