#!/bin/sh
set -e

echo "entrypoint: starting"

if [ -n "$DATABASE_URL" ] && [ -z "$JDBC_DATABASE_URL" ]; then
  DBURL=$(printf '%s' "$DATABASE_URL" | tr -d '\r\n' | sed 's/^[[:space:]]*//;s/[[:space:]]*$//')

  count=$(printf '%s' "$DBURL" | grep -oE 'postgres(ql)?://' | wc -l | tr -d ' ')
  if [ "$count" -gt 1 ]; then
    echo "entrypoint: ERROR: DATABASE_URL parece contener múltiples URLs o estar malformada:"
    echo "entrypoint:   $DBURL"
    echo "entrypoint: Acción requerida: fijá JDBC_DATABASE_URL / DB_USER / DB_PASS en Environment o corregí DATABASE_URL."
    exit 1
  fi

  proto_removed=${DBURL#*://}   # quita esquema
  if echo "$proto_removed" | grep -q "@"; then
    userinfo=${proto_removed%%@*}
    hostpath=${proto_removed#*@}
  else
    userinfo=""
    hostpath=$proto_removed
  fi

  if [ -n "$userinfo" ] && echo "$userinfo" | grep -q ":"; then
    user=${userinfo%%:*}
    pass=${userinfo#*:}
  elif [ -n "$userinfo" ]; then
    user=$userinfo
    pass=""
  else
    user=""
    pass=""
  fi

  if echo "$hostpath" | grep -q "/"; then
    hostport=${hostpath%%/*}
    path="/${hostpath#*/}"
  else
    hostport=$hostpath
    path=""
  fi

  if echo "$hostport" | grep -q ":"; then
    host=${hostport%%:*}
    port=${hostport#*:}
  else
    host=$hostport
    port=5432
  fi

  JDBC_DATABASE_URL="jdbc:postgresql://${host}:${port}${path}"
  DB_USER="${DB_USER:-$user}"
  DB_PASS="${DB_PASS:-$pass}"

  export JDBC_DATABASE_URL DB_USER DB_PASS
  echo "entrypoint: converted DATABASE_URL -> JDBC_DATABASE_URL"
  echo "entrypoint: JDBC_DATABASE_URL=$JDBC_DATABASE_URL"
fi

JVM_D_PROPS=""

if [ -n "$JDBC_DATABASE_URL" ]; then
  JVM_D_PROPS="$JVM_D_PROPS -Dhibernate.connection.url=$JDBC_DATABASE_URL"
  JVM_D_PROPS="$JVM_D_PROPS -Djavax.persistence.jdbc.url=$JDBC_DATABASE_URL"
fi

if [ -n "$DB_USER" ]; then
  JVM_D_PROPS="$JVM_D_PROPS -Dhibernate.connection.username=$DB_USER"
  JVM_D_PROPS="$JVM_D_PROPS -Djavax.persistence.jdbc.user=$DB_USER"
fi

if [ -n "$DB_PASS" ]; then
  JVM_D_PROPS="$JVM_D_PROPS -Dhibernate.connection.password=$DB_PASS"
  JVM_D_PROPS="$JVM_D_PROPS -Djavax.persistence.jdbc.password=$DB_PASS"
fi

JVM_D_PROPS="$JVM_D_PROPS $JAVA_OPTS"

echo "entrypoint: starting java with: java $JVM_D_PROPS -jar /app/app.jar"
exec sh -c "java $JVM_D_PROPS -jar /app/app.jar"