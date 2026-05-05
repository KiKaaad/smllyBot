```
changelogs <--- Здесь лежат прошлые чендж-логи
 v0.1.0.md
 v0.1.1.md
 v0.1.2.md
src
 main
  java
   com
    database <--- Тут лежат базы данных
     s3
     postgresql
      servers <--- Таблица servers
       Servers.java
       ServersTable.java
      users
    listeners <--- Я пока что не придумал для чего она нужна
    modules <--- Тут модули, например модуль модерации будет разбит на бан, мут, кик
     ping
      PingData.java
      PingService.java
      PrefixPing.java
      SlashPing.java
     user
    styles <--- Тут стили, есть компонентные, а есть обычные текстовые
     component
      ping <--- Модуль
       PingComponent.java
     text
    utils <--- Утилиты, например сплит аргументов
     formatting
      Colors.java
      Formatting.java
     localization
      I18n.java
      I18nRequest.java
     logger
     PrefixUtil.java
   testing <--- Тут я обучаюсь Java, не более
    OtherProgram.java
  resources
   language <--- Тут лежит локализация
    by
    ru
     modules
      ping
       ping.json
    en
    otherlang

    
.env
CHANGELOG.md
README.md
```