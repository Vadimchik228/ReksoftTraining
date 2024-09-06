# Company Spring Boot Application

## Запуск приложения:

1. Используя СУБД PostgreSQL, создайте базу данных с названием Company;
2. В файле _.env_ впишите соответствующие значения вашей базы данных (host, username, password);
3. **Run 'ApplicationRunner.java'**.

### Очистка таблиц и запуск тестов:
Для запуска приложения с пустыми таблицами (например, перед интеграционными тестами) выполните следующие действия:

1. Откройте файл _db/changelog/db.changelog-master.yaml_, лежащий в _resources/db/changelog_;
2. Удалите или закомментируйте строки, которые включают:
   - _db/changelog/db.changelog-1.4.sql_;
   - _db/changelog/db.changelog-1.5.sql_;
   - _db/changelog/db.changelog-2.1.sql_;
   - _db/changelog/db.changelog-2.2.sql_.