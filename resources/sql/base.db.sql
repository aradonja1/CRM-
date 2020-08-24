BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "report_customer" (
	"id"	INTEGER,
	"first_name"	TEXT,
	"last_name"	TEXT,
	"email"	TEXT,
	"begin_contract"	TEXT,
	"end_contract"	TEXT,
	"service_name"	TEXT,
	"package_name"	TEXT,
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
CREATE TABLE IF NOT EXISTS "package" (
	"id"	INTEGER,
	"name"	TEXT,
	"archived"	INTEGER,
	PRIMARY KEY("id")
);
CREATE TABLE IF NOT EXISTS "service" (
	"id"	INTEGER,
	"name"	TEXT,
	"archived"	INTEGER,
	PRIMARY KEY("id")
);
CREATE TABLE IF NOT EXISTS "connection" (
	"id"	INTEGER,
	"service_id"	INTEGER,
	"package_id"	INTEGER,
	"archived"	INTEGER,
	PRIMARY KEY("id")
);
INSERT INTO "report_customer" VALUES (0,'Rijad','Fejzić','rfejzic1@etf.unsa.ba','06/30/2020','06/30/2025','Paketi prometa interneta','Neo');
INSERT INTO "contract" VALUES (1,1,'20/01/2010','20/01/2015',8);
INSERT INTO "contract" VALUES (2,1,'30/06/2020','30/06/2025',1);
INSERT INTO "customer" VALUES (1,'Rijad','Fejzić','rfejzic1@etf.unsa.ba','Sarajevo','555555','30/06/2020','30/06/2025',1);
INSERT INTO "employee" VALUES (1,'Senid','Hodžić','shodzic4','senid');
INSERT INTO "employee" VALUES (2,'Employee','Employee','employee','employee');
INSERT INTO "admin" VALUES (1,'Adnan','Radonja','aradonja1','aradonja1');
INSERT INTO "admin" VALUES (2,'Admin','Admin','admin','admin');
INSERT INTO "package" VALUES (1,'Neo',0);
INSERT INTO "package" VALUES (2,'Naj',0);
INSERT INTO "package" VALUES (3,'NekiNeki',0);
INSERT INTO "package" VALUES (4,'Mobilna telefonija',0);
INSERT INTO "package" VALUES (5,'Fiksna telefonija',0);
INSERT INTO "package" VALUES (6,'Turbo WiFi',0);
INSERT INTO "package" VALUES (7,'LG K61',0);
INSERT INTO "package" VALUES (8,'Samsung Galaxy A41',0);
INSERT INTO "service" VALUES (1,'Paketi prometa razgovora',0);
INSERT INTO "service" VALUES (2,'Paketi prometa interneta',0);
INSERT INTO "service" VALUES (3,'Prodaja uređaja',0);
INSERT INTO "connection" VALUES (1,2,1,0);
INSERT INTO "connection" VALUES (2,2,2,0);
INSERT INTO "connection" VALUES (3,2,3,0);
INSERT INTO "connection" VALUES (4,1,4,0);
INSERT INTO "connection" VALUES (5,1,5,0);
INSERT INTO "connection" VALUES (6,2,6,0);
INSERT INTO "connection" VALUES (7,3,7,0);
INSERT INTO "connection" VALUES (8,3,8,0);
COMMIT;
