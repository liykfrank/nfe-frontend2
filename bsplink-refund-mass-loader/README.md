# Bsplink Refund Mass Loader

Loads a file of refunds into the `bsplink-refund-management` service.

# Execution

The execution requires the name of the refund file to be load, for example:

```bash
java -jar bsplink-refund-mass-loader.jar file=FRe9EARS_20150602_0302_003
```

# Execution parameters

- file: defines the refund file to load. **This argument is required**.
- outputPath: by default the report file resulting of the process is generated in the same location
  where the refund file is located but it can be change setting the `outputPath` with the name of
  a directory where the report file should be created.

# Job continuation

The batch job can continue a failing execution of a file, this is achieved using the file basename
as the identifier of the job instance, this means that you cannot process the same file more than
once if it was process successfully before.

# Batch configuration

The batch process requires some configuration to work, it is necessary to set the database to work
with and the URL where the refund management service is listening.

The file [config/application-dev.yml.example](config/application-dev.yml.example) contains a
configuration example that you can use as base to configure the batch process.

## Meta-data tables

The meta-data tables of the batch are created with the prefix `REFUND_MASS_LOADER_BATCH_` (it is
not possible to use a different schema that the default).

The SQL script for PostgreSql is located in
[src/main/resources/sql/schema-postgresql.sql](src/main/resources/sql/schema-postgresql.sql). The
creation of the tables can be managed automatically by the batch process if you set the property
`spring.batch.initialize-schema` with the value `always`.
