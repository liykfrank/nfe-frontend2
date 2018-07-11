# Bsplink Refund Mass Loader

Loads a file of refunds into the `bsplink-refund-management` service.

# Execution

The execution requires the name of the refund file to be load, for example:

```bash
java -jar bsplink-refund-mass-loader.jar file=FRe9EARS_20150602_0302_003
```

# Job continuation

The batch job can continue a failing execution of a file, this is achieved using the file basename
as the identifier of the job instance, this means that you cannot process the same file more than
once if it was process successfully before.
 