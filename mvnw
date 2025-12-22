#!/bin/sh
# Maven Wrapper script

set -e

MAVEN_PROJECTBASEDIR="${MAVEN_BASEDIR:-$(cd "$(dirname "$0")" && pwd)}"
MAVEN_WRAPPER_JAR="$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.jar"
MAVEN_WRAPPER_PROPERTIES="$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.properties"

# Download maven-wrapper.jar if not present
if [ ! -f "$MAVEN_WRAPPER_JAR" ]; then
    echo "Downloading Maven Wrapper..."
    if command -v wget > /dev/null; then
        wget -q -O "$MAVEN_WRAPPER_JAR" "https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"
    elif command -v curl > /dev/null; then
        curl -s -o "$MAVEN_WRAPPER_JAR" "https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"
    else
        echo "Error: wget or curl is required to download maven-wrapper.jar"
        exit 1
    fi
fi

# Run Maven
exec java \
    -classpath "$MAVEN_WRAPPER_JAR" \
    org.apache.maven.wrapper.MavenWrapperMain "$@"
