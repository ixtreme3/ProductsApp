# Product App

Android-приложение для просмотра списка товаров полученных от API DummyJSON. Приложение динамически загружает данные из сети и отображает их в виде прокручивамого списка. Элементы списка содержат информацию о товарах, включая название, описание, цену и миниатюрные изображения. Нажатие по элементу записка позволяет перейти на страницу товара для просмотра более подробной информации (рейтинг, наличие, категория товара и т.д.). Дизайн приложения соответствует рекомендациям Material Design и поддеживает DynamicColors.


## Использованные технологии
* Язык программирования: Kotlin
* Архитектурный паттерн: MVVM
* Retrofit для работы с сетью
* Paging3 для пагинации
* Kotlin Coroutines, Flow, LiveData для асинхронной работы 
* Hilt для внедрения зависимостей
* Navigation Component для реализации навигации
* Glide для загрузки изображений
* Material Components и DynamicColors для дизайна пользовательского интерфейса
* Разметка: XML, ConstraintLayout

## Скриншоты и видео
<img src="screenshots/1.gif" alt="Запуск поиск просмотр списка" width=300> <img src="screenshots/2.gif" alt="Клик по карточке" width=300> 

<img src="screenshots/3.gif" alt="Демонстраниця обработки ошибки" width=300> <img src="screenshots/4.gif" alt="Демонстраниця обработки ошибки" width=300> 

<img src="screenshots/5.gif" alt="Демонстраниця обработки ошибки" width=300>

