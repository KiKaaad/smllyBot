---
description: Руководство по использованию бота
icon: house
layout:
  width: wide
  title:
    visible: true
  description:
    visible: true
  tableOfContents:
    visible: false
  outline:
    visible: false
  pagination:
    visible: false
  metadata:
    visible: false
  tags:
    visible: true
  actions:
    visible: true
  anchors:
    visible: false
---

# Discord bot - femboy#6593

<figure><img src=".gitbook/assets/618657881-ed7fc82d-fbd1-4be8-ac8a-4d61a87c280f.png" alt=""><figcaption></figcaption></figure>

## Использование готового бота

Добавьте его к себе на сервер по ссылке - [тык](https://discord.com/oauth2/authorize?client_id=873596380659191859\&permissions=8\&integration_type=0\&scope=bot)

Из базы нужно знать:

* Бот поддерживает команды с префиксами - `!`, `.` и `ирис`&#x20;
* Посмотреть список команд бота - `хелп`&#x20;
* Сообщить об ошибках можно в [issues](https://github.com/KiKaaad/smllyBot/issues) на гитхабе. Как правильно создавать сообщения о них [написано в проекте](https://github.com/KiKaaad/smllyBot/issues)
* Получить поддержку по боту можно:
  * У владельца - [t.me/kikaaad](https://t.me/KiKaaad)
  * В нашем дискорд-сервере в канале поддержки - [discord.gg/3JSz5fEeee](https://discord.gg/3JSz5fEeee)

## Собственный хостинг бота

Загрузите последний его релиз со страницы гитхаба  - [github.com/KiKaaad/smllyBot/releases](https://github.com/KiKaaad/smllyBot/releases)

Для успешного запуска вам понадобится:

1. **☕ Java 25** и более
2. **🗂️ PostgreSQL - база данных**

{% stepper %}
{% step %}
### Запустите бота в первый раз

Команда запуска - `java -Xms128M -Xmx4096M название_вашего_файла.jar`\
-Xms это минимум выделяемой ОЗУ\
-Xmx это максимум потребляемой ОЗУ\
Эти значения лучше всего должны быть одинаковыми

Пример полной команды:\
`java -Xms128M -Xmx4096M smllybot-0.7.0-beta.jar`&#x20;
{% endstep %}

{% step %}
### Настройте `config.toml`

При первом запуске возникнет ошибка. Это нормально!

Нужно заполнить файл конфигурации, обязательные поля помечены

Одни из них это:  токен, название базы данных, пароль от юзера базы данных
{% endstep %}

{% step %}
### Вновь повторите запуск

Если бот вывел что-то типо такого:

```log
[14:59:51] [JDA MainWS-ReadThread | INFO]:
Информация JDA:
🐾 Версия: 6.7.0_7c90001
🐾 Гитхаб: https://github.com/discord-jda/JDA
Информация бота:
ℹ️ Префиксы: !][.
💾 Версия: v0.7.1-beta (27.09.2026)
📎 Гитхаб: https://github.com/KiKaaad/smllyBot
🌚 Работает на боте: femboy#6593 | 🆔 ID: 873596380659191859
🔗 Шардов всего (1 шард = 0 - 2.500 серверов): 1
🌃 Серверов: 17
💀 Пользователей: 666
✅ Успешно запущено
```

Все хорошо, можете пользоваться собственным ботом 🎉
{% endstep %}
{% endstepper %}

### ❔ Возможно вы задаетесь вопросами

<details>

<summary>Появились страшные и не понятные символы</summary>

Так как я не знаю английский столь хорошо, чтобы писать на нем, я использую русский язык в логах. Терминалы не любят его, поэтому попробуйте прописать перед запуском команду: `[console]::outputEncoding = [System.Text.Encoding]::UTF8`

Это должно решить проблему

</details>

<details>

<summary>Как часто выходят обновления?</summary>

Примерно раз в 1 - 2 недели. Чем больше у меня свободного времени, тем чаще обновления.&#x20;

Это с учетом что я обучаюсь в школе и дополнительно обучаюсь еще и Java & Kotlin, со всеми его остальными хвостами по типу PosgreSQL для хранения данных и фреймворками

</details>

