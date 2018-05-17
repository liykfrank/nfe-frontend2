# BSPlink File Manager.

`BSPlink File Manager` centralizes user's files operations: uploads, downloads,
file list, etc...

## Spring profiles.

### dev

Activates `SQL` output, `h2-console` and loads some
[fixture data](src/main/resources/db/data/dev/R__example_data.sql).

**IMPORTANT:** for running `dev` profile in local mode it is necessary to define locally the 
app.yade.host.sftp properties in order to connect with the sftp server.

### test

This profile is activated when running tests.

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

app.local_uploaded_files_directory: directory where uploaded files are stored temporarily
app.local_downloaded_files_directory: directory where downloaded files are stored temporarily

- no default value applied
- for uploading/downloading files it is necessary to configure existing directories

### Configuration for yade-utils transfer file
Yade-utils allows to establish connections through different protocols such us ftp, sftp, http, https, webdav... etc.

In order to transfer files a source and target hosts must be set in application[env].yml configuration file.

For example: in order to establish a connection with a sftp server the following properties must be set (all properties are mandatory except directory):

- app.yade.host.sftp.name: Sets the name of the sftp server.
- app.yade.host.sftp.user: User to connect with the sftp sever.
- app.yade.host.sftp.password: Password to connect with the sftp sever.
- app.yade.host.sftp.directory: Directory where the files will be pulled/pushed.
- app.yade.host.sftp.protocol: sftp
- app.yade.host.sftp.port: It is possible to customized port connection.

To establish a local connection (Only name and protocol are mandatory):

- app.yade.host.local.name: localhost
- app.yade.host.local.user: Only sets if required.
- app.yade.host.local.password: Only sets if required.
- app.yade.host.local.directory: Directory where the files will be pulled/pushed.
- app.yade.host.local.protocol: local
- app.yade.host.local.port: Could be void if required.

