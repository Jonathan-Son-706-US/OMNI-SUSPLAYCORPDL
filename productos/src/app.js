const express = require('express')
const ProductosRoutes = require('./routes/ProductoRoutes')
const { json } = require('sequelize')

const app = express()
app.use(express.json())
app.use('/', ProductosRoutes)
module.exports = app