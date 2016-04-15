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

<entry key="jsonDataDir">C:\Users\Сергей\parser\exchange\data</entry>
<entry key="backupDir">C:\Users\Сергей\parser\exchange\backup</entry>
<entry key="responseDir">C:\Users\Сергей\parser\exchange\response</entry>
<entry key="logsDir">C:\Users\Сергей\parser\exchange\response</entry>

</properties>



все остальные каталоги на которые имеется ссылка в файле config.property также должны быть созданы.

Перед запуском необходмио иметь работающую БД.
Для запуска используйте файл startup.bat
После того, как приложение будет запущено, оно начинает "прослушивать" каталог jsonDataDir на предмет появления новых файлов. 
Файлы могуть иметь формат .zip или .pkg.
как только файл появляется в каталоге запускается код который заливает содержание файла в БД.



