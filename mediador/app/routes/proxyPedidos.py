import httpx
from fastapi import APIRouter, Depends, HTTPException, Request, Response

from app.config import config
from app.dependencies import usuario_actual

router = APIRouter(tags=["ms-pedidos"])

METODOS = ["GET", "POST", "PUT", "PATCH", "DELETE"]

@router.api_route("/pedido", methods=METODOS)
@router.api_route("/pedido/{ruta:path}", methods=METODOS)
async def reenviar_a_pedidos(request: Request, ruta: str = "", usuario = Depends(usuario_actual)):
    destino = f"{config.url_ms_pedidos}/Pedido"
    if ruta:
        destino += f"/{ruta}"
    return await ejecutar_proxy(request, destino, usuario)

@router.api_route("/detallepedido", methods=METODOS)
@router.api_route("/detallepedido/{ruta:path}", methods=METODOS)
async def reenviar_a_detalles(request: Request, ruta: str = "", usuario = Depends(usuario_actual)):
    destino = f"{config.url_ms_pedidos}/DetallePedido"
    if ruta:
        destino += f"/{ruta}"
    return await ejecutar_proxy(request, destino, usuario)

async def ejecutar_proxy(request: Request, destino: str, usuario):
    cabeceras = {"X-Usuario": usuario["usuario"]}
    if "content-type" in request.headers:
        cabeceras["Content-Type"] = request.headers["content-type"]

    try:
        async with httpx.AsyncClient(timeout=10) as cliente:
            respuesta = await cliente.request(
                request.method, destino,
                params=request.query_params,
                content=await request.body(),
                headers=cabeceras
            )
    except httpx.ConnectError:
        raise HTTPException(status_code=502, detail="El microservicio de Pedidos no está disponible")
    except httpx.TimeoutException:
        raise HTTPException(status_code=504, detail="El microservicio de Pedidos tardó demasiado")

    return Response(content=respuesta.content, status_code=respuesta.status_code, media_type=respuesta.headers.get("content-type"))
