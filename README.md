# JavaMessengerProject
Данный проект представляет собой многофункциональное приложение для общения, обмена сообщениями, и социальных взаимодействий. Ниже приведено описание основных функций и сущностей этого мессенджера:

1) Users (Пользователи):

Модель Users представляет пользователей приложения.
Она содержит информацию о пользователях, такую как имя, адрес электронной почты, пароль и другие учетные данные.
Пользователи могут регистрироваться, аутентифицироваться и управлять своими учетными данными.
Могут отправлять запросы на дружбу.

2) UserProfile (Профиль пользователя):

Модель UserProfile используется для отображения профилей пользователей.
Он содержит информацию о пользователе, такую как аватар, биография и другие персональные данные.
Пользователи могут просматривать свой собственный профиль и профили других пользователей.

3) Chats (Чаты):

Модель Chats предназначена для хранения истории переписки.
В чатах пользователи могут обмениваться текстовыми сообщениями и файлами.
Мессенджер поддерживает как одиночные чаты, так и групповые чаты.

4) Messages (Сообщения):

Модель Messages представляет текстовые и файловые сообщения, отправляемые пользователями.
Сообщения могут содержать текст, изображения и другие медиафайлы.
Пользователи могут отправлять, просматривать и удалять сообщения.

5) FriendsList (Список друзей):

Модель FriendsList содержит информацию о друзьях пользователя.
Пользователи могут добавлять других пользователей в свой список друзей и принимать запросы на дружбу.

6) Posts (Посты):

Модель Posts предназначена для создания, оценивания и комментирования постов.
Пользователи могут создавать текстовые посты, прикреплять изображения и видео, а также ставить лайки и писать комментарии к постам других пользователей.

7) Comments (Комментарии):

Модель Comments используется для хранения комментариев к постам и другим медиа-контенту.
Пользователи могут добавлять комментарии к постам и обсуждать их с другими пользователями.


Проект мессенджера сочетает в себе функциональность мессенджера, социальной сети и платформы для обмена контентом. Пользователи могут общаться, делиться содержанием, заводить друзей, и создавать ленту новостей с помощью постов и комментариев. Это приложение обеспечивает разнообразный опыт общения и социальных взаимодействий для своих пользователей.

## Диаграмма домменой области проекта:
![image](https://github.com/pashtet1092/JavaMessengerProject/assets/93840829/3420840f-dbce-4f2b-aa6a-a11b979b6753)

## Используемые технологии REST проекта:

1) Spring Boot
2) Spring Core
3) AOP
4) Data Jpa
5) Security
6) WebSocket
7) Java 8+ features(stream, lamda)
8) JUnit
9) Flyway
10) Swagger
11) Docker

## Для запуска нужно изменить настройки в application.properties :

DataSource 

Docker  

## Описание функциональности:

