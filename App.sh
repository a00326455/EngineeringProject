#!/bin/bash

java -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -classpath target/classes:~/.m2/repository/org/bouncycastle/bcutil-jdk18on/1.78/bcutil-jdk18on-1.78.jar:~/.m2/repository/org/bouncycastle/bcprov-jdk18on/1.78/bcprov-jdk18on-1.78.jar tus.teamproject.app.App