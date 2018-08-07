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

## Using a different database schema

If you want to use a different database schema and you are using PostgreSql you should define the
schema to use in the URL, for example
`jdbc:postgresql://vlbicore:15432/dev_spring_batch?currentSchema=schema_name`,
where `schema_name` is the schema to use. Be aware that the schema should exist.

In order to create automatically the meta-data tables required by the batch you should configure
the property `spring.batch.initialize-schema` with the value `always`.

**Be aware that if the schema does not exist the tables will be created in the `default` schema silently**
