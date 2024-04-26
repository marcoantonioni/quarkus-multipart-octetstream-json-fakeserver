# quarkus-multipart-octetstream-json-fakeserver

Fake server used to test BAW BPMRESTRequest API

Documentation
1. https://www.ibm.com/docs/en/baw/23.x?topic=30-restrictions-invoking-rest-api-service
2. https://www.ibm.com/support/pages/using-openapi-30-rest-services-ibm-business-automation-workflow-and-ibm-integration-designer
3. https://www.ibm.com/docs/en/baw/23.x?topic=service-invoking-rest-by-using-javascript
4. https://www.ibm.com/docs/en/baw/23.x?topic=javascript-passing-files
5. (very useful) https://bpmtricks.wordpress.com/

BAW 23.0.2 - OpenAPI The only media types supported are
```
application/json
application/xml
text/xml
text/plain
```

## Build image

```
./mvnw package

podman build -f src/main/docker/Dockerfile.jvm -t quay.io/marco_antonioni/quarkus-multipart-octetstream-json-fakeserver-jvm:latest .

podman login -u $QUAY_USER -p $QUAY_PWD quay.io

podman push quay.io/marco_antonioni/quarkus-multipart-octetstream-json-fakeserver-jvm:latest

podman run -i --rm -p 8080:8080 quay.io/marco_antonioni/quarkus-multipart-octetstream-json-fakeserver-jvm:latest
```

## Deploy in OCP


```
TNS=fakeserver
oc new-project ${TNS}

oc delete deployment fakeserver
oc delete service fakeserver

cat <<EOF | oc create -f -
apiVersion: apps/v1
kind: Deployment
metadata:
  labels:
    app: fakeserver
  name: fakeserver
  namespace: ${TNS}
spec:
  replicas: 1
  selector:
    matchLabels:
      app: fakeserver
  template:
    metadata:
      labels:
        app: fakeserver
    spec:
      containers:
      - image: quay.io/marco_antonioni/quarkus-multipart-octetstream-json-fakeserver-jvm:latest
        imagePullPolicy: Always
        name: quarkus-multipart-octetstream-json-fakeserver-jvm
EOF

oc expose deployment fakeserver --port=8080

oc create route edge fakeserver --service=fakeserver

URL="https://"$(oc get routes fakeserver | grep -v NAME | awk '{print $2}')


# wait for pod ready

curl -v -k -H 'Content-Type: application/json' -d '{"name":"Marco","address":"viavai","level":1}' -X POST ${URL}/api/jsondata | jq .

curl -v -k -H 'accept: */*' -H 'Content-Type: multipart/form-data' -F 'files=@./files/file1.txt;type=text/plain' -F 'files=@/home/marco/Downloads/file2.txt;type=text/plain' -X 'POST' ${URL}/upload

```


## Test commands

https://quarkus.io/guides/resteasy-client-multipart

### Files

```
# ok
curl -v -k -H 'accept: */*' -H 'Content-Type: multipart/form-data' -F 'files=@./files/file1.txt;type=text/plain' -X 'POST' 'http://localhost:8080/upload'
```

```
# ok
curl -v -k -H 'accept: */*' -H 'Content-Type: multipart/form-data' -F 'files=@./files/file1.txt;type=text/plain' -F 'files=@/home/marco/Downloads/file2.txt;type=text/plain' -X 'POST' 'http://localhost:8080/upload'
```

```
# ok
curl -v -k -H 'accept: */*' -H 'Content-Type: multipart/form-data' -F 'files=@./files/ibm.gif;type=application/octet-stream' -X 'POST' 'http://localhost:8080/upload'
```

```
# ok
curl -v -k -H 'accept: */*' -H 'Content-Type: multipart/form-data' -F 'files=@./files/file-1k.txt;type=application/octet-stream' -X 'POST' 'http://localhost:8080/upload'
```

```
# ko 413 Request Entity Too Large (application.properties: quarkus.http.limits.max-body-size)
curl -v -k -H 'accept: */*' -H 'Content-Type: multipart/form-data' -F 'files=@./files/file-5k.txt;type=application/octet-stream' -X 'POST' 'http://localhost:8080/upload'
```

```
# ko per 415 Unsupported Media Type (Content-Type: application/octet-stream)
curl -v -k -H 'accept: */*' -H 'Content-Type: application/octet-stream' -F 'files=@/home/marco/Downloads/Test.dmn;type=application/octet-stream' -X 'POST' 'http://localhost:8080/upload'
```


### json data

```
# ok
curl -v -k -X GET http://localhost:8080/api/jsondata | jq .
```

```
# ok
curl -v -k -H 'Content-Type: application/json' -d '{"name":"Marco","address":"viavai","level":1}' -X POST http://localhost:8080/api/jsondata | jq .
```

### text data

```
# ok
curl -v -k -H 'Content-Type: text/plain' -d 'This is a message' -X POST http://localhost:8080/api/textdata
```

```
# ok pass json as string
curl -v -k -H 'Content-Type: text/plain' -d '{"name":"Marco","address":"viavai","level":1}' -X POST http://localhost:8080/api/textdata
```




This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/quarkus-multipart-octetstream-json-fakeserver-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Related Guides

- Eclipse Vert.x ([guide](https://quarkus.io/guides/vertx)): Write reactive applications with the Vert.x API
- ArC ([guide](https://quarkus.io/guides/cdi-reference)): Build time CDI dependency injection
- SmallRye OpenAPI ([guide](https://quarkus.io/guides/openapi-swaggerui)): Document your REST APIs with OpenAPI - comes with Swagger UI
- RESTEasy Reactive ([guide](https://quarkus.io/guides/resteasy-reactive)): A Jakarta REST implementation utilizing build time processing and Vert.x. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it.

## Provided Code

### REST

Easily start your REST Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)
