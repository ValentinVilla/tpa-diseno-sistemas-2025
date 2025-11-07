#!/bin/sh
set -e

echo "entrypoint: starting"

if [ -n "$DATABASE_URL" ] && [ -z "$JDBC_DATABASE_URL" ]; then
  proto_removed=${DATABASE_URL#*://}

  # separar userinfo y hostpath
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

  hostport=${hostpath%%/*}
  path="/${hostpath#*/}"

  if [ "$hostpath" = "$hostport" ]; then
    path=""
  fi

  # separar host y puerto (si existe)
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
  echo "entrypoint: converted DATABASE_URL to JDBC_DATABASE_URL"
fi

JVM_D_PROPS=""

if [ -n "$JDBC_DATABASE_URL" ]; then
  JVM_D_PROPS="$JVM_D_PROPS -Dhibernate.connection.url=$JDBC_DATABASE_URL"
fi
if [ -n "$DB_USER" ]; then
  JVM_D_PROPS="$JVM_D_PROPS -Dhibernate.connection.username=$DB_USER"
fi
if [ -n "$DB_PASS" ]; then
  JVM_D_PROPS="$JVM_D_PROPS -Dhibernate.connection.password=$DB_PASS"
fi

JVM_D_PROPS="$JVM_D_PROPS $JAVA_OPTS"

echo "entrypoint: starting java with: java $JVM_D_PROPS -jar /app/app.jar"

exec sh -c "java $JVM_D_PROPS -jar /app/app.jar"