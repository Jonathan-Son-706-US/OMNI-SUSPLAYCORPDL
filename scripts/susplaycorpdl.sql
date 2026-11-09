--Productos DB
CREATE SCHEMA IF NOT EXISTS productos;

CREATE TABLE IF NOT EXISTS productos."Productos" (
    id_productos SERIAL PRIMARY KEY,
    nombre_producto VARCHAR(150) NOT NULL,
    precio_producto DECIMAL(10,2) NOT NULL,
    descripcion_producto VARCHAR(250) NOT NULL,
    stock_producto INT NOT NULL DEFAULT 0,
    categoria_producto VARCHAR(150) NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT TRUE
);

--PedidosDB
CREATE SCHEMA IF NOT EXISTS pedidos;

CREATE TABLE IF NOT EXISTS pedidos.pedidos (
    id_pedido SERIAL PRIMARY KEY,
    usuario VARCHAR(150) NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    estado VARCHAR(140) NOT NULL,
    fecha_emision TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

--detalles pedido DB
CREATE TABLE IF NOT EXISTS pedidos.detallepedidos (
    id_detalle SERIAL PRIMARY KEY,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    pedido_id INT NOT NULL,
    producto_id INT NOT NULL,
    FOREIGN KEY (pedido_id) REFERENCES pedidos.pedidos(id_pedido) ON DELETE CASCADE
);

--Para superbaseSeadapto db.py para insertar la db junto con sus tablas en postgress.sql // mysql 