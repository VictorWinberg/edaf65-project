const express = require('express')
const path = require('path')
const http = require('http')
const WebSocket = require('ws')

const app = express()

app.use(express.static(path.join(__dirname, 'public')))

const server = http.createServer(app)
const wss = new WebSocket.Server({server})

const users = []

wss.broadcast = function broadcast(data) {
  users.forEach(function each(user) {
    user.ws.send(data)
  })
}

wss.on('connection', function connection(ws, req) {
  ws.send('Server: Welcome! Please enter your username:')
  let username = null

  ws.on('message', function incoming(input) {
    try {
      if (username == null) {
          if (input.match("^[a-zA-Z0-9_]{3,14}$")) {
            username = input
            ws.send("Server: Hi " + username + " use /help to see the server commands.")
            users.push({ username, ws })
          } else {
            ws.send("Server: Please only use alphanumeric values with underscore between 3 and 14 values.");
          }
          return;
      }
      input += '  '
      const command = input.split(/ /)[0].trim()
      const message = input.split(/ (.+)/)[1].trim()
      console.log('Client: ' + message)
      switch (command) {
        case '/help':
          ws.send('Server: Available commands: /help, /all, /echo, /show, /quit')
          break
        case '/all':
          wss.broadcast(username + ': ' + message)
          break
        case '/echo':
          ws.send('Server: ' + message)
          break
        case '/quit':
          const index = users.findIndex(user => user.username === username)
          users.splice(index, 1)
          break
        case '/show':
          ws.send('Server: Online users - ' + users.map(user => user.username).join(", "))
          break
      }
    } catch (e) {
      ws.send('Server: Incorrect command, use /help to see the server commands.')
      console.log(e)
    }
  })

  ws.on('close', function close() {
    console.log('WebClient disconnected')
    const index = users.findIndex(user => user.username === username)
    users.splice(index, 1)
  })

  ws.on('error', function error(error) {
    console.log('Error: ', error)
  })
})

server.listen(3006, function listening() {
  console.log('Listening on %d', server.address().port)
})