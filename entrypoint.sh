#!/bin/sh
set -e

if [ -n "$DATABASE_URL" ] && [ -z "$JDBC_DATABASE_URL" ]; then
  proto_removed=${DATABASE_URL#postgres://}
  userinfo=${proto_removed%%@*}
  hostpath=${proto_removed#*@}
  user=${userinfo%%:*}
  pass=${userinfo#*:}
  host=${hostpath%%/*}
  path=/${hostpath#*/}
  JDBC_DATABASE_URL="jdbc:postgresql://${host}${path}"
  DB_USER="${user}"
  DB_PASS="${pass}"
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