const ProductoModel = require('../models/ProductosModels')

async function BuscarPorId(req, res) {
    try {
        const {id} = req.params
        const productoConsultado = await ProductoModel.findByPk(id)
        if(!productoConsultado){
            return res.status(404).json({
                Problema: 'No encontrado'
            })
        }
        res.status(200).json({
            mensaje : 'Producto Encontrado',
            producto : productoConsultado
        })
        
    } catch (Problema) {
        console.error(Problema)
        res.status(500).json({
            Problema : 'Problema Interno del Servidor SUSPLAYCORP'
        })
    }

}
async function BuscarProductosActivos(req, res) {
    try {
        const productoActivo = await ProductoModel.findAll({
            where: {estado : true}
        });
         res.status(200).json(productoActivo);      
    } catch (Problema) {
        console.error(Problema)
        res.status(500).json({
            Problema : 'Problema Interno del Servidor SUSPLAYCORP'
        })        
    }
    
}

async function BuscarProductosInactivos(req, res) {
    try {
        const productoInactivo = await ProductoModel.findAll({
            where: {estado : false}
        });
         res.status(200).json(productoInactivo);      
    } catch (Problema) {
        console.error(Problema)
        res.status(500).json({
            Problema : 'Problema Interno del Servidor SUSPLAYCORP'
        })        
    }
    
}

async function BuscarPorCategoria(req, res) {
    const  {categoria} = req.params
   try {
    const productos = await ProductoModel.findAll({
        where : {categoria_producto: categoria}
    });
    res.status(200).json(productos);
   } catch (Problema) {
        console.error(Problema)
        res.status(500).json({
            Problema : 'Problema Interno del Servidor SUSPLAYCORP'
        })      
   } 
}

async function BuscarTodosLosProductos(req, res) {
    try{
        const TodosLosProductos = await ProductoModel.findAll()
        res.status(200).json(TodosLosProductos)
    }
    catch (Problema) {
        console.error(Problema)
        res.status(500).json({
            Problema : 'Problema Interno del Servidor SUSPLAYCORP'
        })        
    }    
}
async function CrearProducto(req, res) {
    const{
        nombre_producto,
        precio_producto,
        descripcion_producto,
        stock_producto,
        categoria_producto
    } = req.body
    if(!nombre_producto || !precio_producto || !descripcion_producto || !stock_producto || !categoria_producto){
        return res.status(400).json({
            Problema: 'No se agregaron las datos obligatorios para la siguiente gestion'       
    })
    }
    if(precio_producto <= 0||stock_producto < 0){
        return res.status(400).json({
            Problema : 'Estas categorias no Pueden tener Precio u Stock negativo'
        })
    }
    try {
        const ProductoNuevo = await ProductoModel.create({nombre_producto, precio_producto, descripcion_producto, stock_producto, categoria_producto})
        res.status(201).json({
            mensaje : 'Producto Creado Correctamente'
        })
    } catch (Problema) {
        console.error(Problema)
        res.status(500).json({
            Problema : 'Problema Interno del Servidor SUSPLAYCORP'
        })   
    }
}

async function ActualizarProducto(req, res) {
    const {id} = req.params
    const {nombre_producto, precio_producto, descripcion_producto, stock_producto, categoria_producto, estado} = req.body
    if(!nombre_producto || !precio_producto || !descripcion_producto || !stock_producto || !categoria_producto ){
        return res.status(400).json({
            Problema : 'No existe los datos correctos para actulizar'
        })
    }
    if(precio_producto <= 0||stock_producto < 0){
        return res.status(400).json({
            Problema : 'Estas categorias no Pueden tener Precio u Stock negativo'
        })
    }
    try {
        const productoConsultado = await ProductoModel.findByPk(id)
        if(!productoConsultado){
            return res.status(404).json({
                Problema: 'Producto no encontrado con el id: ' + id
            })
        }
        await productoConsultado.update({
            nombre_producto, 
            precio_producto, 
            descripcion_producto, 
            stock_producto, 
            categoria_producto, 
            estado: estado !== undefined ? estado : productoConsultado.estado
             })
        res.status(200).json({
            mensaje : 'Producto Actualizado'
        })

        
    } catch (Problema) {
        console.error(Problema)
        res.status(500).json({
            Problema : 'Problema Interno del Servidor SUSPLAYCORP'
        })    
    }
    
}

async function EliminarProducto(req, res) {
    const {id} = req.params
    try {
        const productoConsultado = await ProductoModel.findByPk(id)
        if(!productoConsultado){
            return res.status(404).json({
                error: 'Producto No Encontrado'
            })
        }
        productoConsultado.estado = 0;
        await productoConsultado.save()
        res.status(200).json({
            mensaje: 'Producto Silenciado exitosamente',
            producto: productoConsultado})
    } catch (Problema) {
        console.error(Problema)
        res.status(500).json({
            Problema : 'Problema Interno del Servidor SUSPLAYCORP'
        })      
    }
}

async function ConsumoStock(req, res) {
    const {id} = req.params
    const {cantidad_en_stock} = req.body

    try {
        const Producto = await ProductoModel.findByPk(id)
        if(!Producto){
            return res.status(400).json({
                Problema: 'No se encuentra el Producto'
            })
        }
        if(Producto.stock_producto < cantidad_en_stock){
            return res.status(400).json({
                Problema : 'No se cuenta con el stock suficiente para el pedido'
            })
        }
        if (Producto.estado !== 1 && Producto.estado !== true) {
            return res.status(400).json({
                Problema: 'El producto está inactivo y no se puede vender'
            })
        }
        Producto.stock_producto -= cantidad_en_stock;
        await Producto.save();
        return res.status(200).json({
            mensaje : 'Stock encontrado',
            producto_actualizado : Producto
        })
    } catch (Problema) {
        console.error(Problema)
        res.status(500).json({
            Problema : 'Problema Interno del Servidor SUSPLAYCORP'
        })            
    }
    
}
async function DevolverStock(req, res) {
    const { id } = req.params;
    const { cantidad_en_stock } = req.body;

    try {
        const Producto = await ProductoModel.findByPk(id);
        if (!Producto) {
            return res.status(404).json({
                Problema: 'No se encuentra el Producto'
            });
        }
        Producto.stock_producto += Number(cantidad_en_stock);
        await Producto.save();

        return res.status(200).json({
            mensaje: 'Stock devuelto exitosamente por cancelación',
            producto_actualizado: Producto
        });
    } catch (Problema) {
        console.error(Problema);
        res.status(500).json({
            Problema: 'Problema Interno del Servidor SUSPLAYCORP'
        });
    }
}

module.exports = {
   BuscarPorId,
   BuscarTodosLosProductos,
   CrearProducto,
   ActualizarProducto,
   EliminarProducto,
   ConsumoStock,
   DevolverStock,
   BuscarPorCategoria,
   BuscarProductosActivos,
   BuscarProductosInactivos
}