select messageheadercategoryid,count(*) as c from newsplainrow group by messageheadercategoryid order by c desc

select * from newsplainrow where messageheadercategoryid=31

select a.date,cast(messageheadertypeid as text),fields from newsplainrow a where a.messageheadercategoryid=31
union
select b.date,b.type,NULL from alphaflashcalendar b where b.category=31
order by date asc

select * from alphaflashcalendar where category=31

select * from alphaflashcalendar where date > '2015-07-30 00:00:00.000' and date < '2015-07-30 23:59:59.999' order by date



select messageheadercategoryid,count(*) as c from newsplainrow group by messageheadercategoryid order by c desc

select * from newsplainrow where messageheadercategoryid=48

select a.date,cast(messageheadertypeid as text),fields from newsplainrow a where a.messageheadercategoryid=31
union
select b.date,b.type,NULL from alphaflashcalendar b where b.category=31
order by date asc

select * from alphaflashcalendar where category=48

select * from alphaflashcalendar where date > '2015-08-04 00:00:00.000' and date < '2015-08-04 23:59:59.999' order by date


select count(*) from csvhistoricrecord

select categoryid,datetime,values from csvhistoricrecord where categoryid='47' order by datetime desc

select * from csvhistoricrecord

truncate table csvhistoricrecord

ALTER TABLE csvhistoricrecord
DROP COLUMN time

select category,date from alphaflashcalendar where type='RELEASE' and date>'2015-08-04' and date<'2015-08-05'

select category,date from alphaflashcalendar where type='RELEASE'and date>'2015-08-04' and date<'2015-08-05'

select * from alphaflashflow where category=29004 and type=1 order by date asc limit 1

select * from categoriesplexer