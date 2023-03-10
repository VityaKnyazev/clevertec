<h1>CLEVERTEC (test project)</h1>

<p>CLEVERTEC test project:</p>
<ol>
<li>В данном задании важно показать понимание ООП, умение строить модели (выделять классы, интерфейсы, их связи), разделять функционал между ними, а
также знать синтаксис самого языка. Обязательно применение паттернов проектирования (Factory, Builder, Decorator). Обратить внимание на устойчивость
к изменениям в логике и избегать копипаста.</li>
<li>Использовать Java 17, gradle 7.5.</li>
<li>Приложение запускается java RunnerClassName <набор_параметров>, где набор параметров в формате itemId-quantity (itemId - идентификатор товара, quantity -
его количество. Например: java CheckRunner 3-1 2-5 5-1 card-1234 должен сформировать и вывести в консоль чек содержащий в себе наименование товара с id=3 в
количестве 1шт, то же самое с id=2 в количестве 5 штук, id=5 - одна штука и т. д. Card-1234 означает, что была предъявлена скидочная карта с номером 1234.
Необходимо вывести в консоль сформированный чек (вариант на рисунке), содержащий в себе список товаров и их количество с ценой, а также
рассчитанную сумму с учетом скидки по предъявленной карте (если она есть).</li>
<li>Среди товаров предусмотреть акционные. Если их в чеке больше пяти, то сделать скидку 10% по этой позиции. Данную информацию отразить в чеке.</li>
<li>Набор товаров и скидочных карт может задаваться прямо в коде, массивом или коллекцией объектов. Их количество и номенклатура имеет тестовый характер,
поэтому наименование и количество свободные.</li>
<li>Реализовать обработку исключений (например, товара с id или файла не существует и т. д.).</li>
<li>Реализовать вывод чека в файл.</li>
<li>Описать README.md файл (используемый стек, инструкцию по запуску, описание эндпоинтов, если есть).</li>
<li>Использовать сборщик проекта gradle.</li>
<li>Исходники разместить в любом из публичных репозиториев (bitbucket, github, gitlab).</li>
<li>Организовать чтение исходных данных (товары и скидочные карты, формат тот же) из файлов (в таком случае можно передавать имя файла в набор
параметров команды java).</li>
<li>Покрыть функционал юнит-тестами (не менее 70%).</li>
<li>* Замена хранения исходных данных (п.11) в файлах на PostgreSQL; сделать 2
таблицы (product и discount_card); DDL операции должны храниться в
src/main/resources в файле с расширением *.sql; настройки подключения к БД
хранить в application.properties.</li>
<li>** Развернуть приложение и Postgresql в Docker</li>
</ol>


<h2>Что сделано:</h2>
<ol>
<li>По пп. 1-3, 7, 11: создано простое приложение, позволяющее принимать данные в формате productId-quantity card-123451232 и формировать чек
на основе полученных данных с применением парадигмы ООП в построении классов, интерфейсов и связей между ними. Приложение можно условно 
разделить на слои: entity, dao, service, data. Применены патерны проектирования: Factory (при создании сервисов дисконтных карт), 
Builder (при создании квитанции), Decorator (при чтенни, форматировании валидации входных данных). Предусмотрено получение данных из файлов,
консоли. Предусмотрен вывод чека в консоль или файл.</li>
<li>По п. 4: предусмотрены аукционные товары. Написан сервис, разделяющий товары на группы для применения дисконта по картам и на товарные группы. 
В случае применения скидки на товарную группу, скидка к данной товарной группе по дисконтным картам применяться не будет.</li>
<li>По п. 5: создан тестовый набор товаров и дисконтных карт в классах ProductDAOImpl, StorageDAOImpl, DiscountCardDAOImpl.</li>
<li>По п. 6: в коде реализован механизм обработки исключений при чтении, конвертации, валидации входных данных, а также на уровне сервисов.</li>
<li>По п. 8: описан данный README.md</li>
<li>По п. 9: для сборки проекта в jar-файл используется Gradle</li>
<li>По п. 10: исходники размещены на github.</li>
<li>По п. 12: протестированы отдельные классы с основной логикой. Тетирование производилось средствами Junit5 + Mockito. Добавил анализатор покрытия кода JaCoCo.
После сборки проекта результат по покрытию кода можно посмотреть в файле index.html по пути: /build/reports/jacoco/test/html.</li>
<li>По п. 13: сделан docker-compose.yml файл для поднятия Postgresql. В /src/main/resources добалены sql файлы, подключен liquibase плагин
для создания таблиц и заполнения данными. Реализовано подключение к базе через jdbc, использован Hikari connection pool. Создан класс
для чтения id и количества товаров и кредитных карт из базы данных для последующей их покупки и формирования чека.</li>
<li>В проект также добавлены зависимости для Logback логера, чтобы отключить логирование от HicariCP для красивого
вывода чека в консоль из базы данных. Создан файл настроек для логера logback.xml в src/main/resources</li>
<li>По п. 14: Реализован интерфейс для получения чека по GET-запросу. Использован spring web mvc. Файлы с настройкой конфигурации spring, контроллерами в пакете:
ru.clevertec.knyazev.rest. Добавлен web.xml для перенаправления на страницу обработки ошибок.</li>
<li>По п. 15: В docker-compose.yml добавлены настройка для поднятия Tomcat-сервера и добавления в docker-контейнер собранного с помощью Gradle war-файла.
Теперь получить чек можно с помощью Get-запроса по uri, например: http://HOST:8080/clevertec?purchase=1-3&purchase=3-1&card=card-123456789. Перед этим необходимо запустить из корня проекта в 
командной строке: docker-compose up -d</li>
<li>К предыдущему пункту добавлена база данных для хранения информации о магазине и продаваемых товарах. Добавлен spring-orm, настроено подключение к базе данных, используются Hikari для 
предоставления пула соединений с PostgreSQL, Hibernate (Entity manager) - для построения запросов и связи классов и типов Java c таблцами и тимапими PostgreSQL. Добавлен менеджер транзакций. 
Реализованы интерфейсы в DAO-слое для работы с базой данных, добавлены аннотации для сущностей.</li>
</ol>

<h3>Стек</h3>
<ol>
<li>Java 17, Gradle 7.5, JDBC, HikariCP, PostgreSQL, Spring-WEB MVC, Spring orm, Hibernate, Tomcat 10, Docker</li>
<li>JUnit 5, Mockito, Spring-test, JaCoCo (тесты)</li>
</ol>

<h4>Сборка и запуск приложения c java из командной строки:</h4>
<ol>
<li>Запуск приложения возможен в следующих режимах:</li>
	<ol>
		<li>Использование одного источника чтения данных из: консоли (console), файлов (file), базы данных (database), переданных в main метод аргументов (standard).</li>
		<li>Использование одного источника записи данных в файл (file), консоль (console).</li>
	</ol>
	<li>Для сборки проекта использовать Gradle</li>
	<ol>
		<li>Сборка проект в консоли в корне: .\gradlew build</li>
		<li>Пройти в: cd build/libs</li>
		<li>Запустить из командной строки для чтения из main метода и формирования чека в консоли: java -jar clevertec-0.1.0.jar standard console  1-3 2-3 8-6 4-5.4 card-15fd20182 card-15fd20185</li>
		<li>Запустить из командной строки для чтения из main метода и формирования чека в файл: java -jar clevertec-0.1.0.jar standard file  1-3 2-3 8-6 4-5.4 card-15fd20182 card-15fd20185</li>
		<li>Запустить из командной строки для чтения из файлов и формирования чека в файл: java -jar clevertec-0.1.0.jar file file 1.txt 2.txt . При этом в файле 1.txt хранится информация 
		о покупаемых товарах в формате id-quantity, а в файле 2.txt хранится информация об используемых дисконтных картах.</li>
		<li>Запустить из командной строки для чтения из консоли и формирования чека в консоли: java -jar clevertec-0.1.0.jar console console</li>
		<li>и т.д.</li>
	</ol>
<li>Дополнительно:</li>
	<ol>
		<li>Чтение осуществляться из файлов 1.txt 2.txt, расположенных в /src/main/resources в пакете ru.clevertec.knyazev</li>
		<li>Для успешного запуска чтения из файлов необходимо скопировать целиком папки (src/main/resources/ru/clevertec/knyazev) с файлами 1.txt, 2.txt в папку с jar-файлом.</li>
		<li>Запись чека выполняется в файл в папке src/main/resources/ru/clevertec/knyazev. Там же хранятся файлы для чтения 1.txt, 2.txt</li>
		<li>Для чтения данных из базы данных необходимо поднять docker: в консоле в месте расположения docker-compose.yml файла печатаем: docker-compose up. Далее создаем таблицы
		и заполняем их данными. Для этого там же запускаем liquibase: .\gradlew update. Далее проходим в: cd build/libs, в случае если проект собран, запускаем чтение
		данных из базы данных и формирование чека в консоли: java -jar clevertec-0.1.0.jar database console</li>
	</ol>
</ol>	
<h5>Запуск на сервере</h5>
	<ol>
		<li>Собрать проект в корне из консоли: gradle build или .\gradlew build</li>
		<li>Запустить Tomcat 10 и PoostgreSQL в Docker, в корне в консоли: docker-compose up -d</li>
		<li>Запустить Liquibase для дообавления данных в базу, в корне в консоли: .\gradlew update</li>
		<li>Формируем чек по GET-запросу из браузера или с помощью Postman по uri, например: http://HOST:8080/clevertec?purchase=1-3&purchase=3-1&card=card-15fd20181</li>
	</ol>