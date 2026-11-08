from pydantic_settings import BaseSettings, SettingsConfigDict

class Config(BaseSettings):
    secret_key: str
    algoritmo: str = "HS256"
    minutos_expiracion: int = 60
    url_ms_productos: str = "http://localhost:3000"
    url_ms_pedidos: str = "http://localhost:8080"
    db_host: str = "127.0.0.1"
    db_port: int = 3306
    db_usuario: str = "root"
    db_password: str = ""
    db_name: str = "susplaycorpp"

    model_config = SettingsConfigDict(env_file=".env", extra="ignore")

config = Config()