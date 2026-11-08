const app = require('./src/app')
const PUERTO = 3000
const sequelize = require('./src/config/database')

async function iniciarAplicacion() {
    try {
        await sequelize.authenticate()
        console.log('Conexión a la base de datos establecida')
        app.listen(PUERTO, () => { console.log('Ahora estamos en Sequelize en el puerto ' + PUERTO)})
        
    } catch (e) {
        console.error("Hay un error: " + e)
    }
}
iniciarAplicacion()