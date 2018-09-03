const jsonServer = require('json-server')
const server = jsonServer.create()
const router = jsonServer.router('./mocks/data.json')
const middlewares = jsonServer.defaults()

server.use(jsonServer.rewriter({
  '/airline/*/:id': '/airline/:id',
  '/currencies/*': '/currencies',
}))

server.use(middlewares)
server.use(router)

server.listen(3000, () => {
  console.log('JSON SERVER  is running in http://localhost:3000')
})
