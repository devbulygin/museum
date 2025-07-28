setup:
	./gradlew wrapper --gradle-version 8.14.1
	./gradlew build

backend:
	./gradlew bootRun --args='--spring.profiles.active=dev'

clean:
	./gradlew clean

build:
	./gradlew clean build

reload-classes:
	./gradlew -t classes

start-prod:
	./gradlew bootRun --args='--spring.profiles.active=prod'

install:
	./gradlew installDist

# start-dist:
# 	./build/install/app/bin/app

lint:
	./gradlew checkstyleMain checkstyleTest

test:
	./gradlew test

test:
	./gradlew test

update-deps:
	./gradlew refreshVersions

# generate-migrations:
# 	gradle diffChangeLog

# db-migrate:
# 	./gradlew update