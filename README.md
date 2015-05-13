# NetCDF to CSV
NetCDF to CSV will read and convert the specified NetCDF file into a series of CSV output files. This is currently very specific to the Met Office's NetCDF data files as it relies upon specific fields being present within the given NetCDF.

### Version
1.0.0

### Tech
NetCDF to CSV uses a number of open source projects to work properly:

* [UNIDATA] - NetCDF file reading
* [joda-time] - Date & time manipulation
* [Logback] - Logging!

### Installation

You will need java & maven installed.

```sh
$ git clone [git-repo-url] NetCDF-to-CSV  
$ cd NetCDF-to-CSV  
$ mvn clean install  
```

### Use
```sh
$ java -jar uber-netcdf-to-csv-<VERSION>.jar <INPUT NET CDF FILE> <OUTPUT FILE PREFIX>  
```

### Data
You can get hold of some data here...

http://ec2-52-5-245-21.compute-1.amazonaws.com:8082/thredds/catalog/modata/catalog.html

### Development
Want to contribute? Great!  
Take a fork and start hacking!

### Todo's
 - Write Tests
 - Make more generic

### Contact Us
For more information on the Informatics Lab take a look on our website...  
http://www.informaticslab.co.uk

License
----
MIT

**Free Software, Hell Yeah!**