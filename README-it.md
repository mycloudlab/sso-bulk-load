# SSO Bulk Load
Questo progetto è progettato per facilitare la creazione di utenti in massa in Red Hat Single Sign-On (RH-SSO), consentendo test di prestazioni estensive del sistema.

## Importante
Questo progetto non è affiliato a Red Hat e non è un'iniziativa ufficiale. È un'iniziativa personale progettata per aiutarmi a testare RH-SSO. Usa questo progetto a tuo rischio. Non è consigliato per l'uso in un ambiente di produzione.


## Requisiti
* OpenJDK 17 o più recente
* Maven 3.6.3 o più recente
* versione podman 4.8.3
* OpenShift 4.12 o più recente
* Red Hat Single Sign-On 7.6
* client oc
* Database supportati:
    * PostgreSQL
    * MySQL
    * MariaDB
    * Oracle
    * Microsoft SQL Server

## Sommario

## Compilare ed Eseguire
Questo progetto è architettato per funzionare all'interno di un contenitore. Le seguenti istruzioni ti guideranno attraverso il processo di compilazione del codice sorgente, costruzione di un'immagine del contenitore ed esecuzione del contenitore. Se preferisci, puoi prendere l'immagine da quay.io e saltare i passaggi di compilazione del codice sorgente e costruzione dell'immagine del contenitore.


### Compilazione del codice sorgente
Ora che hai gli strumenti necessari installati, puoi compilare il codice sorgente. Per farlo, esegui il seguente comando:
```shell
$ mvn clean package -Pnative
```
```console
[INFO] Scanning for projects...
[WARNING] 
[WARNING] Some problems were encountered while building the effective model for org.acme:sso-bulk-load:jar:1.0.0-SNAPSHOT
[WARNING] 'dependencies.dependency.(groupId:artifactId:type:classifier)' must be unique: io.quarkus:quarkus-arc:jar -> duplicate declaration of version (?) @ line 77, column 17
[WARNING] 
[WARNING] It is highly recommended to fix these problems because they threaten the stability of your build.
[WARNING] 
[WARNING] For this reason, future Maven versions might no longer support building such malformed projects.
[WARNING] 
[INFO] 
[INFO] -----------------------< org.acme:sso-bulk-load >-----------------------
[INFO] Building sso-bulk-load 1.0.0-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- maven-clean-plugin:2.5:clean (default-clean) @ sso-bulk-load ---
[INFO] Deleting /home/parraes/sso-bulk-load/target
[INFO] 
[INFO] --- maven-resources-plugin:2.6:resources (default-resources) @ sso-bulk-load ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] Copying 2 resources
[INFO] 
[INFO] --- quarkus-maven-plugin:3.6.7:generate-code (default) @ sso-bulk-load ---
[INFO] 
[INFO] --- maven-compiler-plugin:3.11.0:compile (default-compile) @ sso-bulk-load ---
[INFO] Changes detected - recompiling the module! :source
[INFO] Compiling 4 source files with javac [debug release 17] to target/classes
[INFO] 
[INFO] --- quarkus-maven-plugin:3.6.7:generate-code-tests (default) @ sso-bulk-load ---
[INFO] 
[INFO] --- maven-resources-plugin:2.6:testResources (default-testResources) @ sso-bulk-load ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] skip non existing resourceDirectory /home/parraes/sso-bulk-load/src/test/resources
[INFO] 
[INFO] --- maven-compiler-plugin:3.11.0:testCompile (default-testCompile) @ sso-bulk-load ---
[INFO] No sources to compile
[INFO] 
[INFO] --- maven-surefire-plugin:3.1.2:test (default-test) @ sso-bulk-load ---
[INFO] No tests to run.
[INFO] 
[INFO] --- maven-jar-plugin:2.4:jar (default-jar) @ sso-bulk-load ---
[INFO] Building jar: /home/parraes/sso-bulk-load/target/sso-bulk-load-1.0.0-SNAPSHOT.jar
[INFO] 
[INFO] --- quarkus-maven-plugin:3.6.7:build (default) @ sso-bulk-load ---
[WARNING] [io.quarkus.deployment.steps.NativeImageAllowIncompleteClasspathAggregateStep] The following extensions have required native-image to allow run-time resolution of classes: {quarkus-jdbc-oracle}. This is a global requirement which might have unexpected effects on other extensions as well, and is a hint of the library needing some additional refactoring to better support GraalVM native-image. In the case of 3rd party dependencies and/or proprietary code there is not much we can do - please ask for support to your library vendor. If you incur in any problem with other Quarkus extensions, please try reproducing the problem without these extensions first.
[WARNING] [io.quarkus.deployment.pkg.steps.NativeImageBuildStep] Cannot find the `native-image` in the GRAALVM_HOME, JAVA_HOME and System PATH. Install it using `gu install native-image` Attempting to fall back to container build.
[INFO] [io.quarkus.deployment.pkg.steps.JarResultBuildStep] Building native image source jar: /home/parraes/sso-bulk-load/target/sso-bulk-load-1.0.0-SNAPSHOT-native-image-source-jar/sso-bulk-load-1.0.0-SNAPSHOT-runner.jar
[INFO] [io.quarkus.deployment.pkg.steps.NativeImageBuildStep] Building native image from /home/parraes/sso-bulk-load/target/sso-bulk-load-1.0.0-SNAPSHOT-native-image-source-jar/sso-bulk-load-1.0.0-SNAPSHOT-runner.jar
[INFO] [io.quarkus.deployment.pkg.steps.NativeImageBuildContainerRunner] Using podman to run the native image builder
[INFO] [io.quarkus.deployment.pkg.steps.NativeImageBuildContainerRunner] Pulling builder image 'quay.io/quarkus/ubi-quarkus-mandrel-builder-image:jdk-21'
8b163da0f958271e1509c98cf4e0495a75de4810f44ae30d54b023076019ac47
[INFO] [io.quarkus.deployment.pkg.steps.NativeImageBuildStep] Running Quarkus native-image plugin on MANDREL 23.1.2.0 JDK 21.0.2+13-LTS
[INFO] [io.quarkus.deployment.pkg.steps.NativeImageBuildRunner] podman run --env LANG=C --rm --user 1000:1000 --userns=keep-id -v /home/parraes/sso-bulk-load/target/sso-bulk-load-1.0.0-SNAPSHOT-native-image-source-jar:/project:z --name build-native-PDWaK quay.io/quarkus/ubi-quarkus-mandrel-builder-image:jdk-21 -J-Dsun.nio.ch.maxUpdateArraySize=100 -J-Dlogging.initial-configurator.min-level=500 -J-Djava.util.logging.manager=org.jboss.logmanager.LogManager -J-Dcom.mysql.cj.disableAbandonedConnectionCleanup=true -J-Dvertx.logger-delegate-factory-class-name=io.quarkus.vertx.core.runtime.VertxLogDelegateFactory -J-Dvertx.disableDnsResolver=true -J-Dio.netty.leakDetection.level=DISABLED -J-Dio.netty.allocator.maxOrder=3 -J-Duser.language=en -J-Duser.country=US -J-Dfile.encoding=UTF-8 --features=io.quarkus.runner.Feature,io.quarkus.runtime.graal.DisableLoggingFeature,io.quarkus.jdbc.postgresql.runtime.graal.SQLXMLFeature -J--add-exports=java.security.jgss/sun.security.krb5=ALL-UNNAMED -J--add-opens=java.base/java.text=ALL-UNNAMED -J--add-opens=java.base/java.io=ALL-UNNAMED -J--add-opens=java.base/java.lang.invoke=ALL-UNNAMED -J--add-opens=java.base/java.util=ALL-UNNAMED -H:+UnlockExperimentalVMOptions -H:BuildOutputJSONFile=sso-bulk-load-1.0.0-SNAPSHOT-runner-build-output-stats.json -H:-UnlockExperimentalVMOptions --strict-image-heap -H:+UnlockExperimentalVMOptions -H:+AllowFoldMethods -H:-UnlockExperimentalVMOptions -J-Djava.awt.headless=true --no-fallback -H:+UnlockExperimentalVMOptions -H:+ReportExceptionStackTraces -H:-UnlockExperimentalVMOptions -H:+AddAllCharsets --enable-url-protocols=http --enable-monitoring=heapdump -H:+UnlockExperimentalVMOptions -H:-UseServiceLoaderFeature -H:-UnlockExperimentalVMOptions -J--add-exports=org.graalvm.nativeimage/org.graalvm.nativeimage.impl=ALL-UNNAMED --exclude-config io\.netty\.netty-codec /META-INF/native-image/io\.netty/netty-codec/generated/handlers/reflect-config\.json --exclude-config io\.netty\.netty-handler /META-INF/native-image/io\.netty/netty-handler/generated/handlers/reflect-config\.json --exclude-config com\.oracle\.database\.jdbc /META-INF/native-image/native-image\.properties --exclude-config com\.oracle\.database\.jdbc /META-INF/native-image/reflect-config\.json sso-bulk-load-1.0.0-SNAPSHOT-runner -jar sso-bulk-load-1.0.0-SNAPSHOT-runner.jar
Warning: The option '-H:ReflectionConfigurationResources=META-INF/native-image/io.netty/netty-transport/reflection-config.json' is experimental and must be enabled via '-H:+UnlockExperimentalVMOptions' in the future.
Warning: Please re-evaluate whether any experimental option is required, and either remove or unlock it. The build output lists all active experimental options, including where they come from and possible alternatives. If you think an experimental option should be considered as stable, please file an issue.
========================================================================================================================
GraalVM Native Image: Generating 'sso-bulk-load-1.0.0-SNAPSHOT-runner' (executable)...
========================================================================================================================
For detailed information and explanations on the build output, visit:
https://github.com/oracle/graal/blob/master/docs/reference-manual/native-image/BuildOutput.md
------------------------------------------------------------------------------------------------------------------------
Warning: Feature class oracle.nativeimage.NativeImageFeature is annotated with the deprecated annotation @AutomaticFeature. Support for this annotation will be removed in a future version of GraalVM. Applications should register a feature using the option --features=oracle.nativeimage.NativeImageFeature
[1/8] Initializing...                                                                                    (5.1s @ 0.23GB)
 Java version: 21.0.2+13-LTS, vendor version: Mandrel-23.1.2.0-Final
 Graal compiler: optimization level: 2, target machine: x86-64-v3
 C compiler: gcc (redhat, x86_64, 8.5.0)
 Garbage collector: Serial GC (max heap size: 80% of RAM)
 5 user-specific feature(s):
 - com.oracle.svm.thirdparty.gson.GsonFeature
 - io.quarkus.jdbc.postgresql.runtime.graal.SQLXMLFeature
 - io.quarkus.runner.Feature: Auto-generated class by Quarkus from the existing extensions
 - io.quarkus.runtime.graal.DisableLoggingFeature: Disables INFO logging during the analysis phase
 - oracle.nativeimage.NativeImageFeature
------------------------------------------------------------------------------------------------------------------------
 4 experimental option(s) unlocked:
 - '-H:+AllowFoldMethods' (origin(s): command line)
 - '-H:BuildOutputJSONFile' (origin(s): command line)
 - '-H:-UseServiceLoaderFeature' (origin(s): command line)
 - '-H:ReflectionConfigurationResources' (origin(s): 'META-INF/native-image/io.netty/netty-transport/native-image.properties' in 'file:///project/lib/io.netty.netty-transport-4.1.100.Final.jar')
------------------------------------------------------------------------------------------------------------------------
Build resources:
 - 23.48GB of memory (75.6% of 31.08GB system memory, determined at start)
 - 12 thread(s) (100.0% of 12 available processor(s), determined at start)
[2/8] Performing analysis...  [*****]                                                                   (42.5s @ 2.12GB)
   18,677 reachable types   (89.1% of   20,968 total)
   29,904 reachable fields  (55.7% of   53,729 total)
  100,065 reachable methods (58.4% of  171,293 total)
    5,450 types,   555 fields, and 5,005 methods registered for reflection
       68 types,    89 fields, and    56 methods registered for JNI access
        4 native libraries: dl, pthread, rt, z
[3/8] Building universe...                                                                               (6.5s @ 2.04GB)
[4/8] Parsing methods...      [***]                                                                      (5.1s @ 2.73GB)
[5/8] Inlining methods...     [***]                                                                      (3.3s @ 3.29GB)
[6/8] Compiling methods...    [******]                                                                  (42.4s @ 1.58GB)
[7/8] Layouting methods...    [***]                                                                      (7.6s @ 2.87GB)
[8/8] Creating image...       [***]                                                                      (8.9s @ 3.85GB)
  46.83MB (40.76%) for code area:    67,364 compilation units
  67.68MB (58.89%) for image heap:  665,601 objects and 236 resources
 411.66kB ( 0.35%) for other data
 114.91MB in total
------------------------------------------------------------------------------------------------------------------------
Top 10 origins of code area:                                Top 10 object types in image heap:
  13.52MB java.base                                           14.88MB byte[] for code metadata
   6.57MB m.oracle.database.jdbc.ojdbc11-23.3.0.23.09.jar      7.52MB byte[] for java.lang.String
   3.59MB java.xml                                             6.70MB char[]
   2.23MB com.mysql.mysql-connector-j-8.0.33.jar               6.43MB int[]
   1.81MB c.microsoft.sqlserver.mssql-jdbc-12.4.2.jre11.jar    4.84MB java.lang.Class
   1.74MB c.f.jackson.core.jackson-databind-2.15.3.jar         4.83MB java.lang.String
   1.54MB svm.jar (Native Image)                               2.67MB long[]
   1.41MB org.postgresql.postgresql-42.6.0.jar                 1.85MB byte[] for embedded resources
   1.39MB org.mariadb.jdbc.mariadb-java-client-3.2.0.jar       1.60MB byte[] for general heap data
   1.37MB jdk.proxy4                                           1.57MB com.oracle.svm.core.hub.DynamicHubCompanion
  11.19MB for 99 more packages                                14.79MB for 4988 more object types
------------------------------------------------------------------------------------------------------------------------
Recommendations:
 HEAP: Set max heap for improved and more predictable memory usage.
 CPU:  Enable more CPU features with '-march=native' for improved performance.
------------------------------------------------------------------------------------------------------------------------
                       11.2s (9.1% of total time) in 187 GCs | Peak RSS: 6.02GB | CPU load: 8.88
------------------------------------------------------------------------------------------------------------------------
Produced artifacts:
 /project/sso-bulk-load-1.0.0-SNAPSHOT-runner (executable)
 /project/sso-bulk-load-1.0.0-SNAPSHOT-runner-build-output-stats.json (build_info)
========================================================================================================================
Finished generating 'sso-bulk-load-1.0.0-SNAPSHOT-runner' in 2m 2s.
[INFO] [io.quarkus.deployment.pkg.steps.NativeImageBuildRunner] podman run --env LANG=C --rm --user 1000:1000 --userns=keep-id -v /home/parraes/sso-bulk-load/target/sso-bulk-load-1.0.0-SNAPSHOT-native-image-source-jar:/project:z --entrypoint /bin/bash quay.io/quarkus/ubi-quarkus-mandrel-builder-image:jdk-21 -c objcopy --strip-debug sso-bulk-load-1.0.0-SNAPSHOT-runner
[INFO] [io.quarkus.deployment.QuarkusAugmentor] Quarkus augmentation completed in 131370ms
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  02:15 min
[INFO] Finished at: 2024-02-01T16:18:39-03:00
[INFO] ------------------------------------------------------------------------
```
### Costruire un'immagine del contenitore
Ora costruiamo l'immagine del contenitore utilizzando il seguente comando:
```shell
$ podman build -f src/main/docker/Dockerfile.native -t quay.io/parraes/sso-bulk-load:v0.0.2 .
```
```console
STEP 1/7: FROM registry.access.redhat.com/ubi8/ubi-minimal:8.9
STEP 2/7: WORKDIR /work/
--> Using cache d3f1545552aec950402221f9717f4e11c419ad96f54e257cbe834602361d78fb
--> d3f1545552ae
STEP 3/7: RUN chown 1001 /work     && chmod "g+rwX" /work     && chown 1001:root /work
--> Using cache e3880b5e2d48090aa32a8718da0ac7215ca69228600625c0cd9d0b7b9ba12bfa
--> e3880b5e2d48
STEP 4/7: COPY --chown=1001:root target/*-runner /work/application
--> c4a74cd53b2c
STEP 5/7: EXPOSE 8080
--> 5e0bf1e83d91
STEP 6/7: USER 1001
--> b5dae05ad4f4
STEP 7/7: ENTRYPOINT ["./application", "-Dquarkus.http.host=0.0.0.0"]
COMMIT quay.io/parraes/sso-bulk-load:v0.0.2
--> 67306528776b
Successfully tagged quay.io/parraes/sso-bulk-load:v0.0.2
67306528776b3238341a44e3cc413aebbfaae5b6717923c8f3c1567490db41b3
```
### Esecuzione del contenitore
Ora eseguiamo il contenitore, puoi scegliere tra podman o OpenShift per l'esecuzione.

#### Esecuzione del contenitore utilizzando podman
```shell
$ podman run -d \
    -p 9000:9000 \
    -e CONFIG.DATASOURCE.DRIVER="<driver-name>" \
    -e CONFIG.DATASOURCE.URL="<jdbc_url>" \
    -e CONFIG.DATASOURCE.USERNAME="<db_username>" \
    -e CONFIG.DATASOURCE.PASSWORD="<db_password>" \
    -e CONFIG.KEYCLOAK.REALM="<sso_realm>" \
    --name sso-bulk-load-multi-db  \
    quay.io/parraes/sso-bulk-load:v0.0.2
```
* CONFIG.DATASOURCE.DRIVER è il driver del database, può essere postgres, mysql, mariadb, oracle o mssql.
* CONFIG.DATASOURCE.URL è l'URL del database, può essere jdbc:postgresql://hostname:port_number/database_name, jdbc:mysql://hostname:port_number/database_name, jdbc:mariadb://hostname:port_number/database_name, jdbc:oracle:thin:@hostname:port_number:database_name o jdbc:sqlserver://hostname:port_number;databaseName=database_name.
* CONFIG.DATASOURCE.USERNAME è il nome utente del database.
* CONFIG.DATASOURCE.PASSWORD è la password del database.
* CONFIG.KEYCLOAK.REALM è il realm del RH-SSO.

#### Esecuzione del contenitore utilizzando OpenShift
Ora eseguiamo il contenitore utilizzando OpenShift, puoi usare il seguente comando per creare un deployment e un servizio:
```shell
$ oc new-app --name=sso-bulk-load-multi-db \
    -e CONFIG.DATASOURCE.DRIVER="<driver-name>" \
    -e CONFIG.DATASOURCE.URL="<jdbc_url>" \
    -e CONFIG.DATASOURCE.USERNAME="<db_username>" \
    -e CONFIG.DATASOURCE.PASSWORD="<db_password>" \
    -e CONFIG.KEYCLOAK.REALM="<sso_realm>" \
    quay.io/parraes/sso-bulk-load:v0.0.2
```
* CONFIG.DATASOURCE.DRIVER è il driver del database, può essere postgres, mysql, mariadb, oracle o mssql.
* CONFIG.DATASOURCE.URL è l'URL del database, può essere jdbc:postgresql://hostname:port_number/database_name, jdbc:mysql://hostname:port_number/database_name, jdbc:mariadb://hostname:port_number/database_name, jdbc:oracle:thin:@hostname:port_number:database_name o jdbc:sqlserver://hostname:port_number;databaseName=database_name.
* CONFIG.DATASOURCE.USERNAME è il nome utente del database.
* CONFIG.DATASOURCE.PASSWORD è la password del database.
* CONFIG.KEYCLOAK.REALM è il realm del RH-SSO.

Esporre il servizio alla rete esterna:
```shell
oc expose svc/sso-bulk-load-multi-db
```

Ottieni la rotta del servizio:
```shell
oc get route sso-bulk-load-multi-db
```

## Utilizzo dell'API per creare utenti
Ora utilizziamo l'API per creare utenti, puoi usare il seguente comando per creare utenti:
```shell
curl -X 'POST' \
  'http://<hostname>:<port>/bulk/insert/10000' \
  -H 'accept: text/plain' \
  -d ''     
```

Questo comando creerà 10000 utenti nel RH-SSO.

## Utilizzo dell'API per eliminare gli utenti
Ora utilizziamo l'API per eliminare gli utenti, puoi usare il seguente comando per eliminare gli utenti:
```shell
curl -X 'DELETE' \
  'http://<hostname>:<port>bulk/delete/admin' \
  -H 'accept: text/plain'    
```
Tutti gli utenti saranno eliminati dal RH-SSO, ad eccezione dell'utente amministratore.
``
