SELECT DISTINCT
--warehouses.warehousename AS "Склад",
suppliers.inn AS "Поставщик", 
"Склад доставки".warehousename AS "Город",
--"Все склады".warehousename,
  doc_periods.periodbegin,
  to_char(doc_periods.periodbegin ,'DD.MM.YYYY') AS "Дата прибытия", 
  to_char(doc_periods.periodbegin+interval '3 hour' ,'hh24:mi')||'-'||  to_char(doc_periods.periodend+interval '3 hour' ,'hh24:mi') AS "Период разгрузки",
--doc_periods.periodend AS "Окончание разгрузки", 
  donut_doc_periods.licenseplate AS "Номер автомашины",
  donut_doc_periods.palletsqty AS "Паллет всего",
  --orders.ordernumber
  
  --warehouses.warehousename,
--suppliers.inn AS "Поставщик", 
 -- doc_periods.periodbegin AS "Начало разгрузки", 
  --doc_periods.periodend AS "Окончание разгрузки", 
  --donut_doc_periods.licenseplate AS "Номер автомашины", 
  --donut_doc_periods.palletsqty AS "Паллет всего", 
  orders.ordernumber, 
  SUM(orders.orderpalletsqty) OVER (PARTITION BY "Склад доставки".warehousename,orders.ordernumber,doc_periods.periodend) AS "Паллеты",
  SUM(orders.boxqty) AS "Коробки"
  --"Склад доставки".warehousename, 
  --warehouses.warehousename
FROM 
  public.doc_periods, 
  --public.donut_doc_periods, 
  public.docs, 
  public.warehouses, 
  public.suppliers, 
  public.supplier_users, 
  public.orders RIGHT JOIN public.donut_doc_periods ON orders.donutdocperiodid= donut_doc_periods.donutdocperiodid,
  public.warehouses "Склад доставки"
WHERE 
  donut_doc_periods.donutdocperiodid = doc_periods.docperiodid AND
  docs.docid = doc_periods.docid AND
  docs.warehouseid = warehouses.warehouseid AND
  --to_char(doc_periods.periodbegin ,'DD.MM.YYYY')=to_char(now() ,'DD.MM.YYYY') AND
  supplier_users.userid = donut_doc_periods.supplieruserid AND
  warehouses.warehousename='Красноармейск' AND
  --suppliers.inn='AZ' AND
  supplier_users.supplierid = suppliers.supplierid AND
  orders.donutdocperiodid = donut_doc_periods.donutdocperiodid AND
  orders.finaldestinationwarehouseid = "Склад доставки".warehouseid
  GROUP BY "Склад доставки".warehousename,doc_periods.periodbegin,orders.ordernumber,warehouses.warehousename,donut_doc_periods.palletsqty,suppliers.inn,doc_periods.periodend,donut_doc_periods.licenseplate,orders.orderpalletsqty
  ORDER BY doc_periods.periodbegin;
