--ProductosDB
create database susplaycorp;
use susplaycorp;

create table if not exists  Productos(
id_productos INT AUTO_INCREMENT PRIMARY KEY,
nombre_producto varchar(150) not null,
precio_producto decimal(10,2) not null,
descripcion_producto varchar(250) not null,
stock_producto int not null,
categoria_producto varchar(150) not null,
estado bit default 1 not null
)

--PedidosDB
create database susplaycorpp;
use susplaycorpp;
create table if not exists Pedidos(
id_pedido INT AUTO_INCREMENT PRIMARY KEY,
usuario varchar(150) not null,
total decimal(10,2) not null,
estado varchar(140) not null,
fecha_emision timestamp default CURRENT_TIMESTAMP() not null
);

create table if not exists DetallePedidos (
id_detalle INT AUTO_INCREMENT PRIMARY KEY,
cantidad INT not null,
precio_unitario Decimal(10,2) not null,
Pedido_ID INT not null,
Producto_ID INT not null,
FOREIGN KEY (Pedido_ID) REFERENCES Pedidos(id_pedido)
);

--UsuariosBrokerDB
create databse susplaycorppb
--las demas tablas lo inserta db.py segun ejemplo del inge :D
