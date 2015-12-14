Java version - 1.8.

--COMMANDS--

setActions:
IRT  insert into request table
IRLT insert into routeListTable
IIT  insert into invoices table
UIT  update invoice statuses

start - launch timer with period depicted in prefs.property file
help - print out all avaliable commands
exit - shut down app


<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
<properties>
<comment>prefs</comment>
<entry key="generatePeriod">10</entry> --period in seconds
<entry key="user">root</entry> 
<entry key="url">jdbc:mysql://localhost:3306/</entry>
<entry key="password">rtbyg7895otlgorit</entry>
<entry key="dbName">transmaster_transport_db</entry>
<entry key="mode">generate</entry>
</properties>