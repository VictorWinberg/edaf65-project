const net = require('net')

const HOST = localhost
const PORT = 3000

const client = new net.Socket()
const stdin = process.openStdin()
client.connect(PORT, HOST)

stdin.addListener('data', function(data) {
  client.write(data.toString())
})

client.on('data', function(response) {
  process.stdout.write(response)
})

client.on('close', function() {
  console.log('Disconnected')
  process.stdin.destroy()
})