import httpx
from fastapi import APIRouter, Depends, HTTPException, Request, Response

from app.config import config
from app.dependencies import usuario_actual

router = APIRouter(tags = ["ms-productos"])

METODOS = ["GET", "POST", "PUT", "PATCH", "DELETE"]

@router.api_route("/productos", methods = METODOS)
@router.api_route("/productos/{ruta:path}", methods = METODOS)
async def reenviar_a_productos(
    request: Request, ruta: str = "", usuario = Depends(usuario_actual)
):
    destino = f"{config.url_ms_productos}/Productos"
    if ruta:
        destino += f"/{ruta}"

    cabeceras = {"X-Usuario": usuario["usuario"]}
    if "content-type" in request.headers:
        cabeceras["Content-Type"] = request.headers["content-type"]

    try:
        async with httpx.AsyncClient(timeout = 10) as cliente:
            respuesta = await cliente.request(
                request.method,
                destino,
                params = request.query_params,
                content = await request.body(),
                headers = cabeceras
            )
    except httpx.ConnectError:
        raise HTTPException(status_code = 502, detail = "ms-productos no está disponible")
    except httpx.TimeoutException:
        raise HTTPException(status_code = 504, detail = "ms-productos tardó demasiado en responder")

    return Response(
        content = respuesta.content,
        status_code = respuesta.status_code,
        media_type = respuesta.headers.get("content-type")
    )
