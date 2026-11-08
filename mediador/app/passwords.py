import hashlib
import hmac
import os

ITERACION = 200_000

def crear_password(password: str) -> str:
    decoy = os.urandom(16)
    hash_bytes = hashlib.pbkdf2_hmac(
        "sha256",
        password.encode(),
        decoy,
        ITERACION)

    password_completa = decoy.hex() + "$" + hash_bytes.hex()
    return password_completa

def verificar_password(passwordGuardada: str, password: str) -> bool:
    decoy_hex, hash_hex = passwordGuardada.split("$")

    hash_nuevo = hashlib.pbkdf2_hmac(
        "sha256",
        password.encode(),
        bytes.fromhex(decoy_hex),
        ITERACION)

    return hmac.compare_digest(hash_nuevo.hex(), hash_hex)
