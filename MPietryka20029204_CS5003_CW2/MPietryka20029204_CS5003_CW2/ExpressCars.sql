--CREATE TABLE CAR
CREATE TABLE CAR
(
CAR_ID VARCHAR (4) NOT NULL,
CAR_BRAND VARCHAR (20) NOT NULL,
CAR_MODEL VARCHAR (20) NOT NULL,
CAR_TYPE VARCHAR (20) NOT NULL,
DAILY_RATE INT NOT NULL,
AVAILABLE CHAR (1) NOT NULL,
PRIMARY KEY(CAR_ID)
);

--CREATE TABLE BOOKING
CREATE TABLE BOOKING
(BOOKING_ID INT NOT NULL GENERATED ALWAYS AS IDENTITY,
CAR_ID VARCHAR(4) NOT NULL,
FIRST_NAME VARCHAR(30) NOT NULL,
LAST_NAME VARCHAR(30) NOT NULL,
EMAIL VARCHAR(30) NOT NULL,
RENTAL_DATE DATE NOT NULL,
NO_OF_DAYS INT NOT NULL,
TOTAL INT,
PRIMARY KEY(BOOKING_ID),
FOREIGN KEY(CAR_ID) REFERENCES CAR(CAR_ID)
);

--POPULATE TABLE CAR
INSERT INTO CAR (CAR_ID, CAR_BRAND, CAR_MODEL, CAR_TYPE, DAILY_RATE, AVAILABLE)
VALUES ('0119', 'FORD', 'FIESTA', 'ECONOMY', 29, 'N')
;
INSERT INTO CAR (CAR_ID, CAR_BRAND, CAR_MODEL, CAR_TYPE, DAILY_RATE, AVAILABLE)
VALUES ('0619', 'VAUXHALL', 'CORSA', 'ECONOMY', 35, 'Y')
;
INSERT INTO CAR (CAR_ID, CAR_BRAND, CAR_MODEL, CAR_TYPE, DAILY_RATE, AVAILABLE)
VALUES ('0610', 'SEAT', 'IBIZA', 'ECONOMY', 29, 'Y')
;
INSERT INTO CAR (CAR_ID, CAR_BRAND, CAR_MODEL, CAR_TYPE, DAILY_RATE, AVAILABLE)
VALUES ('0666', 'CITROEN', 'C3', 'ECONOMY', 39, 'Y')
;
INSERT INTO CAR (CAR_ID, CAR_BRAND, CAR_MODEL, CAR_TYPE, DAILY_RATE, AVAILABLE)
VALUES ('0622', 'VAUXHAL', 'ASTRA', 'COMPACT', 39, 'Y')
;
INSERT INTO CAR (CAR_ID, CAR_BRAND, CAR_MODEL, CAR_TYPE, DAILY_RATE, AVAILABLE)
VALUES ('0621', 'FORD', 'FOCUS', 'COMPACT', 39, 'Y')
;
INSERT INTO CAR (CAR_ID, CAR_BRAND, CAR_MODEL, CAR_TYPE, DAILY_RATE, AVAILABLE)
VALUES ('0117', 'PEUGOT', '308', 'COMPACT', 45, 'Y')
;
INSERT INTO CAR (CAR_ID, CAR_BRAND, CAR_MODEL, CAR_TYPE, DAILY_RATE, AVAILABLE)
VALUES ('0150', 'HYUNDAI', 'I30', 'COMPACT', 49, 'Y')
;
INSERT INTO CAR (CAR_ID, CAR_BRAND, CAR_MODEL, CAR_TYPE, DAILY_RATE, AVAILABLE)
VALUES ('0149', 'AUDI', 'A5', 'PREMIUM', 79, 'Y')
;
INSERT INTO CAR (CAR_ID, CAR_BRAND, CAR_MODEL, CAR_TYPE, DAILY_RATE, AVAILABLE)
VALUES ('0144', 'BMW', '5 SERIES', 'PREMIUM', 89, 'Y')
;
INSERT INTO CAR (CAR_ID, CAR_BRAND, CAR_MODEL, CAR_TYPE, DAILY_RATE, AVAILABLE)
VALUES ('0142', 'MERCEDES', 'E CLASS', 'PREMIUM', 99, 'Y')
;
INSERT INTO CAR (CAR_ID, CAR_BRAND, CAR_MODEL, CAR_TYPE, DAILY_RATE, AVAILABLE)
VALUES ('0121', 'JAGUAR', 'XE', 'PREMIUM', 119, 'Y')
;

--POPULATE TABLE BOOKING WITH TEST DATA
INSERT INTO BOOKING ( CAR_ID, FIRST_NAME, LAST_NAME, EMAIL, RENTAL_DATE, NO_OF_DAYS, TOTAL)
VALUES ( '0119', 'Liam','Manning', 'lmanning@gmail.com', '02.03.2022',  3, 87)
;