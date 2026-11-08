from contextlib import asynccontextmanager
from fastapi import FastAPI
from app.db import crear_tablas_y_usuarios_iniciales
from app.routes import admin, auth, proxyProductos, proxyPedidos

@asynccontextmanager
async def ciclo_de_vida(app: FastAPI):
    crear_tablas_y_usuarios_iniciales()
    yield

app = FastAPI(title = "Broker Principal - SUSPLAUCORP", lifespan = ciclo_de_vida)

app.include_router(auth.router)
app.include_router(admin.router)
app.include_router(proxyProductos.router)
app.include_router(proxyPedidos.router)

@app.get("/")
def root():
    return {"mensaje": "MediadorSUS funcionando correctamente y redirigiendo servicios"}