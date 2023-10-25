TRUNCATE TABLE users CASCADE;
TRUNCATE TABLE items CASCADE;
TRUNCATE TABLE bookings CASCADE;
TRUNCATE TABLE comments CASCADE;
TRUNCATE TABLE requests CASCADE;

ALTER TABLE users ALTER COLUMN id RESTART with 1;
ALTER TABLE items ALTER COLUMN id RESTART with 1;
ALTER TABLE bookings ALTER COLUMN id RESTART with 1;
ALTER TABLE comments ALTER COLUMN id RESTART with 1;
ALTER TABLE requests ALTER COLUMN id RESTART with 1;
