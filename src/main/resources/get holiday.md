```
https://timor.tech/api/holiday/year/2024/
```

```
select json_keys(da) from (select date_value->'$.holiday' as da from holiday_date where date_year = '2024') t;
```

```
SELECT count(1) FROM holiday_date WHERE date_year='2024' and '01-01' member of (after_value);
```
