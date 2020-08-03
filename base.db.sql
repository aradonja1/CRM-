BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "service" (
	"id"	INTEGER,
	"name"	TEXT,
	PRIMARY KEY("id")
);
CREATE TABLE IF NOT EXISTS "package" (
	"id"	INTEGER,
	"name"	TEXT,
	PRIMARY KEY("id")
);
CREATE TABLE IF NOT EXISTS "connection" (
	"id"	INTEGER,
	"service_id"	INTEGER,
	"package_id"	INTEGER,
	PRIMARY KEY("id")
);
CREATE TABLE IF NOT EXISTS "contract" (
	"id"	INTEGER,
	"customer_id"	INTEGER,
	"begin_contract"	TEXT,
	"end_contract"	TEXT,
	"connection"	INTEGER,
	PRIMARY KEY("id")
);
CREATE TABLE IF NOT EXISTS "customer" (
	"id"	INTEGER,
	"first_name"	TEXT,
	"last_name"	TEXT,
	"email"	TEXT,
	"adress"	TEXT,
	"contact"	TEXT,
	"begin_contract"	TEXT,
	"end_contract"	TEXT,
	"connection"	INTEGER,
	PRIMARY KEY("id")
);
CREATE TABLE IF NOT EXISTS "employee" (
	"id"	INTEGER,
	"first_name"	TEXT,
	"last_name"	TEXT,
	"username"	TEXT,
	"password"	TEXT,
	PRIMARY KEY("id")
);
CREATE TABLE IF NOT EXISTS "admin" (
	"id"	INTEGER,
	"first_name"	TEXT,
	"last_name"	TEXT,
	"username"	TEXT,
	"password"	TEXT,
	PRIMARY KEY("id")
);
INSERT INTO "service" VALUES (1,'Paketi prometa razgovora');
INSERT INTO "service" VALUES (2,'Paketi prometa interneta');
INSERT INTO "service" VALUES (3,'Prodaja uređaja');
INSERT INTO "package" VALUES (1,'Neo');
INSERT INTO "package" VALUES (2,'Naj');
INSERT INTO "package" VALUES (3,'NekiNeki');
INSERT INTO "package" VALUES (4,'Mobilna telefonija');
INSERT INTO "package" VALUES (5,'Fiksna telefonija');
INSERT INTO "package" VALUES (6,'Turbo WiFi');
INSERT INTO "package" VALUES (7,'LG K61');
INSERT INTO "package" VALUES (8,'Samsung Galaxy A41');
INSERT INTO "connection" VALUES (1,2,1);
INSERT INTO "connection" VALUES (2,2,2);
INSERT INTO "connection" VALUES (3,2,3);
INSERT INTO "connection" VALUES (4,1,4);
INSERT INTO "connection" VALUES (5,1,5);
INSERT INTO "connection" VALUES (6,2,6);
INSERT INTO "connection" VALUES (7,3,7);
INSERT INTO "connection" VALUES (8,3,8);
INSERT INTO "contract" VALUES (1,1,'20/01/2010','20/01/2015',8);
INSERT INTO "contract" VALUES (2,1,'30/06/2020','30/06/2025',1);
INSERT INTO "customer" VALUES (1,'Rijad','Fejzić','rfejzic1@etf.unsa.ba','Sarajevo','555555','30/06/2020','30/06/2025',1);
INSERT INTO "employee" VALUES (1,'Senid','Hodžić','shodzic4','senid');
INSERT INTO "admin" VALUES (1,'Adnan','Radonja','aradonja1','123456');
COMMIT;
