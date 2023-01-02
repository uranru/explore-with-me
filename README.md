Для компиляции артефактов использовать команду: mvn clean package.  
Для разворачивания сервисов использовать команду: docker compose up.  

После запуска сервисы доступны по портам 8080,9090.  

Реализация доп функционала:  
Улучшение модерации событий администратором — возможность выгружать все события, ожидающие модерации, делать их проверку и оставлять комментарий для инициатора события, если оно не прошло модерацию.
При этом инициатору дать возможность исправить замечания и отправить событие на повторную модерацию.  

Что было сделано:  
- В БД добавлена таблица comment_events;  
- В пакет event добавлены компоненты работы с комментариями;  
- Доработаны методы.  


1. Admin: Выгружать все события ожидающие модерацию (GET)  
   Реализует запрос:  
   http://localhost:8080/admin/events?states=PENDING  
2. Admin: Отклонять и делать комментарий (PATCH)  
   Доработан существующий запрос, + доработаны геты событий.  
   В существующий patch добавлена возможность отправлять в боди коммент дто:    
   http://localhost:8080/admin/events/1/reject  
   {  
   "text": "Замечание"  
   }  
3. UserPrivate: Получать в событиях комментарий  
   Доработаны геты событий, евент содержит доп поле.  
4. Исправить замечание и отправить на проверку (PATCH)  
   При изменении пользователем события он переходит в состояние PENDING.  

https://github.com/uranru/java-explore-with-me/pull/3  


