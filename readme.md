# Webproject NG [![Jenkins](https://img.shields.io/jenkins/build?jobUrl=https%3A%2F%2Fjenkins.gmasil.de%2Fjob%2Fgithub-gmasil%2Fjob%2FWebproject-NG%2Fjob%2Fmaster%2F)](https://jenkins.gmasil.de/blue/organizations/jenkins/github-gmasil%2FWebproject-NG/branches) [![Quality Gate Status](https://sonar.gmasil.de/api/project_badges/measure?project=de.gmasil%3Awebproject-ng%3Abackend%3Amaster&metric=alert_status)](https://sonar.gmasil.de/dashboard?id=de.gmasil%3Awebproject-ng%3Abackend%3Amaster) [![Coverage](https://sonar.gmasil.de/api/project_badges/measure?project=de.gmasil%3Awebproject-ng%3Abackend%3Amaster&metric=coverage)](https://sonar.gmasil.de/component_measures?id=de.gmasil%3Awebproject-ng%3Abackend%3Amaster&metric=coverage&view=treemap) [![Lines of Code](https://sonar.gmasil.de/api/project_badges/measure?project=de.gmasil%3Awebproject-ng%3Abackend%3Amaster&metric=ncloc)](https://sonar.gmasil.de/code?id=de.gmasil%3Awebproject-ng%3Abackend%3Amaster)

## Development

Build:

```bash
mvn clean package
```

Build docker image to local daemon using jib:

```bash
mvn clean install -P jib
```

Build spring native docker image to local daemon using buildpacks:

```bash
mvn clean install -P native
```

### Dev mode

To activate the `dev` mode set the Java VM argument `-Dspring.profiles.active=dev` or the environment variable `SPRING_PROFILES_ACTIVE=dev`.

Dev mode functions:

- Disable CORS

### Frontend development with backend docker container

You can simply start the backend running inside a Docker container:

```bash
./dev/startBackendDevContainer.sh
```

**Note:** This container will not provide a frontend to prevent any confusion during development.

and stop that container using:

```bash
./dev/stopBackendDevContainer.sh
```

The Dev container will automatically enable `dev` mode and import data from the file `webproject-backend/webproject-data.yml` if it exists. See next section for further information.

There are several options available for the startup script:

| Argument         | Default                                |
| ---------------- | -------------------------------------- |
| `IMAGE_NAME`     | webproject-ng                          |
| `IMAGE_TAG`      | local-dev                              |
| `LOCAL_PORT`     | 6900                                   |
| `CONTAINER_NAME` | webproject                             |
| `DATA_FILE`      | webproject-backend/webproject-data.yml |
| `PUBLIC_ACCESS`  | true                                   |
| `VERBOSE`        | false                                  |
| `NATIVE`         | false                                  |
| `BUILD`          | true                                   |

**Examples**:

Build and start the native application:

```bash
NATIVE=true ./dev/startBackendDevContainer.sh
```

Start the native application without building again, disabled public access, show log in terminal:

```bash
NATIVE=true BUILD=false PUBLIC_ACCESS=false VERBOSE=true ./dev/startBackendDevContainer.sh
```

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
