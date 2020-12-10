with
    -- add 0 and max+3 to the input
    device(j) as (select max(j) + 3 from input),
    outlet(j) as (values (0)),
    groupedjolts as (
        with joltdiffs as (
            with jolts as (
                select *
                from input
                UNION ALL
                select *
                from device
                UNION ALL
                select *
                from outlet
                order by j asc)
            -- calculate difference to previous adapter
            select j, lag(j, 1) over (order by j desc) - j as difference
            from jolts)
        select difference, count(difference) as counter
        from joltdiffs
        -- group by difference (1,2 or 3)
        group by difference
    ),
    day1(counter, result, difference) as (
        select counter, counter * lag(counter, 2) over (order by difference desc), difference
       -- add defaults of 0, since input values might ot have specific differences inbetween them
        from (select * from groupedjolts
              UNION ALL
              (select *
               from (values (1, 0), (2, 0), (3, 0)) as defaultValues(difference, counter)
               where not exists(select 1 from groupedjolts where difference = defaultValues.difference))
         ) as results
    )
select max(result)
from day1;


CREATE AGGREGATE mul(bigint) ( SFUNC = int8mul, STYPE =bigint );

-- with some pretty specific assumptions about our input, we can calculate day 2
-- for every jump of 3, there can only be one way to include it
-- all other jumps are 1
-- count number of 1s in a row to get possible number of combinations
-- magic numbers figured out on paper, should figure out a way to calculate them in sql
with device(j) as (select max(j) + 3 from input),
     outlet(j) as (values (0)),
     joltdiffs as (
         with jolts as (
             select *
             from input
             UNION ALL
             select *
             from device
             UNION ALL
             select *
             from outlet
             order by j asc)
         select j, lag(j, 1) over (order by j desc) - j as difference
         from jolts),
     combinations as (
         select j,
                difference,
                case
                    when lead(difference, 1) over (ORDER BY j desc) = 1 or difference = 3 or
                         difference is null
                        then 1::bigint
                    when lag(difference, 1) over (ORDER BY j desc) = 3
                        then 1::bigint
                    when lag(difference, 1) over (ORDER BY j desc) = 1 and
                         lag(difference, 2) over (ORDER BY j desc) = 3
                        then 2::bigint
                    when lag(difference, 2) over (ORDER BY j desc) = 1 and
                         lag(difference, 3) over (ORDER BY j desc) = 3
                        then 4::bigint
                    when lag(difference, 3) over (ORDER BY j desc) = 1 and
                         lag(difference, 4) over (ORDER BY j desc) = 3
                        then 7::bigint
                    else 0
                    end
                    as n
         from joltdiffs
         order by j asc)
select mul(n)
from combinations;

