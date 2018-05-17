# BSPlink File Manager.

`BSPlink File Manager` centralizes user's files operations: uploads, downloads,
file list, etc...

## Spring profiles.

### dev

Activates `SQL` output, `h2-console` and loads some
[fixture data](src/main/resources/db/data/dev/R__example_data.sql).

## Database.

Without specific database, both `dev` profile and `test suite` use an `h2`
in memory database. The `PostgresSql` driver is also provided so you can
configure a `PostgreSql` database easily, here is an
[example](config/application-dev.yml.example).

If you are using `h2` you can connect to the `h2-console` from the URL
http://localhost:8080/h2-console. Configure the `JDBC URL` value with
`jdbc:h2:mem:testdb` and be sure that the user is `sa` without password.

**IMPORTANT:** executing the `dev` profile or launching the `test suite`
**destroys all the data** in the database so be careful with the database
you use.

## Application Configuration

app.local_uploaded_files_directory: directory where uploaded files are stored

- no default value applied
- for uploading files it is necessary to configure an existing directory   



