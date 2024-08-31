# Company Spring Boot Application

## Запуск приложения:

Run 'ApplicationRunner.java'

### Очистка таблиц и запуск тестов:
Для запуска приложения с пустыми таблицами (например, перед интеграционными тестами) выполните следующие действия:

1. Откройте файл _db/changelog/db.changelog-master.yaml_, лежащий в _resources/db/changelog_.
2. Удалите или закомментируйте строки, которые включают:
   - _db/changelog/db.changelog-2.0.sql_;
   - _db/changelog/db.changelog-2.1.sql_.