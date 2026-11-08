const   {DataTypes} = require('sequelize')
const sequelize = require('../config/database')

const Producto = sequelize.define('Producto',
    {
        id_productos:{
            type: DataTypes.INTEGER,
            primaryKey: true,
            autoIncrement : true
        },
        nombre_producto:{
            type: DataTypes.STRING,
            allowNull: false
        },
        precio_producto: {
            type: DataTypes.DECIMAL(10,2),
            allowNull : false
        },
        descripcion_producto: {
            type : DataTypes.STRING,
            allowNull: false
        },
        stock_producto:{
            type: DataTypes.INTEGER,
            allowNull: false,
            defaultValue: 0
        },
        categoria_producto:{
            type: DataTypes.STRING,
            allowNull: false,

        },
        estado: {
            type: DataTypes.BOOLEAN, 
            defaultValue: 1,
            allowNull: false
        }

    },
    {
        tableName: 'Productos',
        timestamps: false
    }


)
module.exports = Producto