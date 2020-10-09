# Hotel Reservation service

To build:

    ./gradlew build -x test

To run an individual module build (examples):

    ./gradlew app:bootJar
    ./gradlew api:package

To run api tests:

    ./gradlew api-test:build