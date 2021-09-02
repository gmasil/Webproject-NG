# Webproject NG

## Development

Build:

```
mvn clean package
```

Build docker image to local daemon:

```
mvn -f webproject-backend jib:dockerBuild
```

### Dev mode

To activate the `dev` mode set the Java VM argument `-Dspring.profiles.active=dev` or the environment variable `SPRING_PROFILES_ACTIVE=dev`.

Dev mode functions:

- Disable CORS

### Frontend development with backend docker container

You can simply start the backend running inside a Docker container:

```
dev/startBackendDevContainer.sh
```

and stop that container using:

```
dev/stopBackendDevContainer.sh
```

The Dev container will automatically enable `dev` mode and import data from the file `webproject-backend/webproject-data.yml` if it exists. See next section for further information.

### Data Import

Webproject can import data from a YAML file upon startup.

The format of the YAML file can be found here: [test-data-import.yml](webproject-backend/src/test/resources/test-data-import.yml)

The configuration parameter for `application.yml` are:

```yml
dataimport:
  enabled: true
  file: data.yml
  clean: true
```

Using environment variables:

```properties
DATAIMPORT_ENABLED=true
DATAIMPORT_FILE=data.yml
DATAIMPORT_CLEAN=true
```

Using Java VM arguments:

```properties
-Ddataimport.enabled=true
-Ddataimport.file=data.yml
-Ddataimport.clean=true
```
