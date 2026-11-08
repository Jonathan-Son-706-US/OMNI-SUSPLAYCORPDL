import pymysql
from pymysql.cursors import DictCursor
from app.config import config
from app.passwords import crear_password

def render_postgres() -> bool:
    return config.db_dialect == "postgres"

def obtener_conexion():
     if render_postgres():
        import psycopg2
        from psycopg2.extras import RealDictCursor
        return psycopg2.connect(
            host = config.db_host,
            port = config.db_port,
            user = config.db_usuario,
            password = config.db_password,
            dbname = config.db_name,
            sslmode = "require",
            cursor_factory = RealDictCursor
        )
     return pymysql.connect(
        host = config.db_host,
        port = config.db_port,
        user = config.db_usuario,
        password = config.db_password,
        database = config.db_name,
        cursorclass = DictCursor  
    )
     
#Como solo copie y pegue su ejemplo de broker me di cuenta que como es con postgrest me toco separar el codigo sql crudo en su version mysql para mi comu
#como a su vez para desplegarlo en render UnU
     
PARA_XAMP_DB = """
    CREATE TABLE IF NOT EXISTS usuarios (
        usuario VARCHAR(50) PRIMARY KEY,
        password_hash VARCHAR(255) NOT NULL,
        rol ENUM('admin', 'usuario') NOT NULL DEFAULT 'usuario',
        estado ENUM('activo', 'baneado') NOT NULL DEFAULT 'activo'
    )
"""

PARA_RENDER = """
    CREATE TABLE IF NOT EXISTS usuarios (
        usuario VARCHAR(50) PRIMARY KEY,
        password_hash VARCHAR(255) NOT NULL,
        rol VARCHAR(20) NOT NULL DEFAULT 'usuario' CHECK (rol IN ('admin', 'usuario')),
        estado VARCHAR(20) NOT NULL DEFAULT 'activo' CHECK (estado IN ('activo', 'baneado'))
    )
"""

def crear_tablas_y_usuarios_iniciales() -> None:
    conexion = obtener_conexion()
    with conexion.cursor() as cursor:
        cursor.execute(PARA_RENDER if render_postgres() else PARA_XAMP_DB)
        cursor.execute("SELECT COUNT(*) AS total FROM usuarios")
        if cursor.fetchone()["total"] == 0:
            iniciales = [
                ("admin", "admin123", "admin"),
                ("jonas", "jonas123", "admin"),
                ("abner", "abner132", "usuario"),
                ("shotniro", "hotniro123", "usuario")
            ]
            for usuario, password, rol in iniciales:
                cursor.execute(
                    "INSERT INTO usuarios (usuario, password_hash, rol) VALUES (%s, %s, %s)",
                    (usuario, crear_password(password), rol)
                )
    conexion.commit()
    conexion.close()

def buscar_usuario(usuario: str):
    conexion = obtener_conexion()
    with conexion.cursor() as cursor:
        cursor.execute("SELECT * FROM usuarios WHERE usuario = %s", (usuario,))
        fila = cursor.fetchone()  
    conexion.close()
    return fila

def listar_usuarios():
    conexion = obtener_conexion()
    with conexion.cursor() as cursor:
        cursor.execute("SELECT usuario, rol, estado FROM usuarios ORDER BY usuario")
        filas = cursor.fetchall()
    conexion.close()
    return filas

def cambiar_estado(usuario: str, estado: str) -> None:
    conexion = obtener_conexion()
    with conexion.cursor() as cursor:
        cursor.execute(
            "UPDATE usuarios SET estado = %s WHERE usuario = %s", (estado, usuario)
        )
    conexion.commit()
    conexion.close()
