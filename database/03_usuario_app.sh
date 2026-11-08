#!/bin/bash
# Crea el usuario que usa el backend, con permiso SOLO de lectura (mínimo privilegio).
# La contraseña viene de la variable APP_DB_PASSWORD definida en el archivo .env (no se versiona).
set -euo pipefail

mysql --protocol=socket -uroot -p"${MYSQL_ROOT_PASSWORD}" <<SQL
CREATE USER IF NOT EXISTS '${APP_DB_USER}'@'%' IDENTIFIED BY '${APP_DB_PASSWORD}';
GRANT SELECT ON gamer_events.* TO '${APP_DB_USER}'@'%';
FLUSH PRIVILEGES;
SQL
