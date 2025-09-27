# YouGile-Plugins Platform
___
## Deadlines plugin
___
___
### Description

Плагин, устанавливающий дневные и недельные дедлайны на заданных колонках.
___
### Before using

Для запуска плагина необходим `YouGile Plugins Manager`.

___
### Configuration

Настройки для плагина должны указываться в конфиг-файле `deadlines.config`.

Обязательные параметры:

`token`:
Токен для доступа к YouGile API

[создать токен](https://en.yougile.com/api-v2#/operations/AuthKeyController_create)

`column.daily.id`:
ID колонки с дневными задачами.

`column.weekly.id`: ID колонки с недельными задачами.

[получить список колонок](https://en.yougile.com/api-v2#/operations/ColumnController_search)

`request.frequency`: частота запросов в минуту.
Необходимо указать натуральное число - сколько запросов в минуту может отправлять плагин.

!
> Yougile REST API допускает максимально 50 запросов в минуту для одной компании.
> 
> Соответственно, суммарная частота всех запущенных плагинов не должна превышать 50 
> во избежание временной блокировки запросов со стороны YouGile.
> 
> Рекомендуется рассчитывать нагрузку (сколько задач в минуту необходимо обрабатывать для комфортной работы)
> и устанавливать минимально необходимую частоту.
> В дальнейшем можно повысить частоту запросов в конфиг-файле.


Пример файла-конфига:
```properties
token=lkjdfDSAHDSFLSDKJG39857DFKJGN-dfkjhsdlgjkb
column.daily.id=dfsgkjhlfdk89y7g4kj3b
column.weekly.id=kludghlkgh4i3ulgtkjfb
request.frequency=20
```
___
### Usage

#### Скачать готовый архив:
- Скачать архив с последней версией из [релизов](https://github.com/PatBatTB/yougile-plugins-deadlines/releases) или собрать проект из исходников.
- Распаковать
- Указать значения для параметров в `deadlines.config` согласно секции Configuration.
- Скопировать jar-файл и `deadlines.config` в папку с аддонами `YouGile Plugins Manager`

#### Собрать из исходников:
- Клонировать текущий репозиторий
- Создать и заполнить `deadlines.config` согласно секции Configuration.
- Собрать проект `mvn clean package`
- Скопировать jar-файл и `deadlines.config` в папку с аддонами `YouGile Plugins Manager`

  (jar-файл необходимо брать "толстый" с суффиксом `-full`) 
___
