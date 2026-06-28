@echo off
echo === Generando JARs de los 11 microservicios ===
echo.

set MS=usuario cliente vendedor vehiculo disponibilidad inspeccion alquiler pago reserva multa gateway

for %%m in (%MS%) do (
    echo [%%m] Compilando...
    cd /d "%~dp0%%m"
    call mvnw clean package -DskipTests -q
    cd /d "%~dp0"
    echo [%%m] OK
)

echo.
echo === Todos listos ===
echo Ejecutar: docker compose up -d
