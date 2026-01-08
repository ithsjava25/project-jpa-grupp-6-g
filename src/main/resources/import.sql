insert into Hotel (name)
values ('Real Hotel');


insert into Room (priceClass, roomCapacity, roomNumber, hotel_id)
values (300, 2, 1, 1),
       (300, 2, 2, 1),
       (300, 2, 3, 1),
       (300, 2, 4, 1),
       (300, 2, 5, 1),
       (300, 2, 6, 1),
       (300, 2, 7, 1),
       (300, 2, 8, 1),
       (300, 2, 9, 1),
       (300, 4, 10, 1),
       (300, 4, 11, 1),
       (300, 4, 12, 1),
       (300, 4, 13, 1),
       (300, 4, 14, 1);

Select * from Room;

insert into Guest (email, firstName, lastName)
values
    ('anna.svensson@example.se', 'Anna', 'Svensson'),
    ('erik.nilsson@example.se', 'Erik', 'Nilsson'),
    ('klara.larsson@example.se', 'Klara', 'Larsson'),
    ('johan.andersson@example.se', 'Johan', 'Andersson'),
    ('elin.holmberg@example.se', 'Elin', 'Holmberg'),
    ('oscar.lindgren@example.se', 'Oscar', 'Lindgren'),
    ('maria.lundqvist@example.se', 'Maria', 'Lundqvist'),
    ('victor.bergstrom@example.se', 'Victor', 'Bergström'),
    ('sara.nyberg@example.se', 'Sara', 'Nyberg'),
    ('lars.ekstrom@example.se', 'Lars', 'Ekström');

select * from Guest;

-- om booking före mitt datum kolla om slutdatum är samma/tidigare startdatum, om booking efter mitt datum kolla om startdatum är samma/efter slutdatum.

insert into Booking (startDate, endDate, room_id)
values
    ('2026-01-24', '2026-01-28', 2),
    ('2026-01-19', '2026-01-23', 5),
    ('2026-02-02', '2026-02-07', 3),
    ('2026-01-20', '2026-01-21', 8);


select * from Booking;

insert into guestBooking (guest_id, booking_id)
values
    (1, 1),
    (3, 2),
    (6, 3),
    (7,4);

select * from Room r
join Booking b on r.id = b.room_id
where '2026-01-25' not between b.startDate and date_add(b.endDate, interval -1 day) and '2026-01-26' not between date_add(b.startDate, interval 1 day) and b.endDate;

select roomNumber from Room r
left join Booking b on r.id = b.room_id
order by r.id;

select r.*
from Room r
         left join Booking b on r.id = b.room_id
where (b.id is null)
   or ('2026-01-20' not between b.startDate and DATE_ADD(b.endDate, interval -1 day)
    and '2026-01-23' not between DATE_ADD(b.startDate, interval 1 day) and b.endDate)
order by r.id;

drop database booking_db;

create database booking_db;
