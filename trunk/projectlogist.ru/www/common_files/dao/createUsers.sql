# ��������� �� ������ ������ ������, �� � ��������� ����� �������, ���������, ... DDL
SET PASSWORD FOR 'root'@'localhost' = PASSWORD('rtbyg7895otlgorit');
# ��������� ������ ������ ������, ��������� ���������� ������ ������������ ������ ����� ������������ ��� ������ � ��.
CREATE USER 'andy'@'localhost' IDENTIFIED BY 'andyandy';
GRANT SELECT, UPDATE, INSERT, DELETE, EXECUTE on `project_database`.* TO 'andy'@'localhost' WITH GRANT OPTION;
