Java version - 1.8.
для того, чтобы приложение работало в каталоге пользователя должен быть каталог parser и в нем файл с именем config.property
содержание файла должно быть по следующему образцу:

<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">

<properties>

<entry key="url">jdbc:mysql://localhost:3306/</entry>
<entry key="user">parser</entry>
<entry key="password">refka1203</entry>
<entry key="dbName">transmaster_transport_db</entry>
<entry key="encoding">useUnicode=true&amp;characterEncoding=UTF-8</entry>

<entry key="jsonDataDir">C:\Users\Сергей\Desktop\view_transport\_1CParser\trunk\src\main\resources\exchange\data</entry>
<entry key="responseDir">C:\Users\Сергей\Desktop\view_transport\_1CParser\trunk\src\main\resources\exchange\response</entry>
<entry key="logsDir">C:\Users\Сергей\Desktop\view_transport\_1CParser\trunk\src\main\resources\exchange\response</entry>

</properties>
