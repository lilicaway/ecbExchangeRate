# Schemas for generating xml mapping classes 

Use this command to generate the classes from these schema files:

```
xjc -d ../../java/ -p com.liliana.sample.exchange.datasource.ecb.generated ecb-envelope.xsd
````

The schema files were take from:
https://github.com/iggyzap/exchange-rates/tree/master/play-module/app/models/iggy/zap/schema
