DROP TABLE Diagnostic     CASCADE CONSTRAINTS;
DROP TABLE Service     CASCADE CONSTRAINTS;
DROP TABLE States_visible     CASCADE CONSTRAINTS;
DROP TABLE Bid_final     CASCADE CONSTRAINTS;
DROP TABLE Bid_state     CASCADE CONSTRAINTS;
DROP TABLE State_info     CASCADE CONSTRAINTS;
DROP TABLE Order_component     CASCADE CONSTRAINTS;
DROP TABLE Order_info     CASCADE CONSTRAINTS;
DROP TABLE User_info     CASCADE CONSTRAINTS;
DROP TABLE User_right     CASCADE CONSTRAINTS;
DROP TABLE Work_economics     CASCADE CONSTRAINTS;
DROP TABLE Component_economics 	CASCADE CONSTRAINTS;
DROP TABLE Bid_main     CASCADE CONSTRAINTS;
DROP TABLE Product_type     CASCADE CONSTRAINTS;
DROP TABLE Client     CASCADE CONSTRAINTS;
DROP TABLE Work_type     CASCADE CONSTRAINTS;
DROP TABLE Component     CASCADE CONSTRAINTS;

DROP SEQUENCE user_seq;
DROP SEQUENCE product_seq;
DROP SEQUENCE client_seq;
DROP SEQUENCE order_seq;
DROP SEQUENCE bid_seq;
DROP SEQUENCE u_right_seq;
DROP SEQUENCE state_seq;
DROP SEQUENCE component_seq;
DROP SEQUENCE work_seq;

CREATE TABLE Component
(	component_id NUMBER PRIMARY KEY,
	component_name VARCHAR2(100) NOT NULL,
	component_description VARCHAR2(255),
	price NUMBER(5, 2) NOT NULL
);

CREATE TABLE Work_type
(	type_id NUMBER PRIMARY KEY,
	type_name VARCHAR2(100) NOT NULL,
	price NUMBER(5, 2)
);

CREATE TABLE Client
(	client_id NUMBER PRIMARY KEY,
	fullname VARCHAR2(100) NOT NULL,
	city VARCHAR2(100) NOT NULL,
	telephone VARCHAR2(20) NOT NULL,
	email VARCHAR2(100) NOT NULL
);

CREATE TABLE Product_type
(	type_id NUMBER PRIMARY KEY,
	type_name VARCHAR2(100) NOT NULL
);

CREATE TABLE Bid_main
(	bid_id NUMBER PRIMARY KEY,
	register_date DATE NOT NULL,
	client_id NUMBER REFERENCES Client(client_id) ON DELETE SET NULL,
	product_type NUMBER REFERENCES Product_type(type_id) ON DELETE CASCADE,
	sn1 VARCHAR2(100) NOT NULL,
	defect VARCHAR2(255)
);

CREATE TABLE Work_economics
(	bid_id  NUMBER REFERENCES Bid_main(bid_id) ON DELETE CASCADE,
	work_id NUMBER REFERENCES Work_type(type_id) ON DELETE CASCADE
);

CREATE TABLE User_right
(	right_id NUMBER PRIMARY KEY,
	right_name VARCHAR2(100) NOT NULL
);

CREATE TABLE User_info
(	user_id NUMBER PRIMARY KEY,
	login VARCHAR2(100) NOT NULL,
	password VARCHAR2(100) NOT NULL,
	fullname VARCHAR2(100) NOT NULL,
	email VARCHAR2(100) NOT NULL,
	user_type NUMBER NOT NULL REFERENCES User_right(right_id) ON DELETE CASCADE,
	UNIQUE(login)
);

CREATE TABLE Order_info
(	order_id NUMBER PRIMARY KEY,
	order_date DATE NOT NULL,
	details VARCHAR2(255),
	user_id NUMBER REFERENCES User_info(user_id) ON DELETE SET NULL
);

CREATE TABLE Order_component
(	order_id NUMBER REFERENCES Order_info(order_id) ON DELETE CASCADE,
	component_id NUMBER REFERENCES Component(component_id) ON DELETE SET NULL
);

CREATE TABLE State_info
(	state_id NUMBER PRIMARY KEY,
	state_name VARCHAR2(20) NOT NULL,
	rate NUMBER(2) NOT NULL
);

CREATE TABLE Bid_state
(	bid_id NUMBER REFERENCES Bid_main(bid_id) ON DELETE CASCADE,
	state_id NUMBER REFERENCES State_info(state_id) ON DELETE SET NULL,
	date_changed DATE NOT NULL,
	user_changed NUMBER REFERENCES User_info(user_id) ON DELETE SET NULL,
	UNIQUE(bid_id)
);

CREATE TABLE Bid_final
(	final_bid_id NUMBER REFERENCES Bid_main(bid_id) ON DELETE CASCADE,
	client_result VARCHAR2(255),
	declaration_name VARCHAR2(255),
	office_name VARCHAR2(100),
	notes VARCHAR2(255),
	UNIQUE(final_bid_id)
);

CREATE TABLE States_visible
(	state_id NUMBER REFERENCES State_info(state_id) ON DELETE CASCADE,
	user_right NUMBER REFERENCES User_right(right_id) ON DELETE CASCADE
);

CREATE TABLE Component_economics
(	bid_id NUMBER REFERENCES Bid_main(bid_id) ON DELETE CASCADE,
	component_id NUMBER REFERENCES Component(component_id) ON DELETE SET NULL
);

CREATE TABLE Service
(	service_bid_id NUMBER REFERENCES Bid_main(bid_id) ON DELETE CASCADE,
	service_issue_date DATE,
	service_return_date DATE,
	service_result VARCHAR2(255),
	UNIQUE(service_bid_id)
);

CREATE TABLE Diagnostic
(	diag_bid_id NUMBER REFERENCES Bid_main(bid_id) ON DELETE CASCADE,
	diag_issue_date DATE,
	diag_return_date DATE,
	diagnostics_result VARCHAR2(255),
	is_guarantee NUMBER(2),
	term_in_days NUMBER(4),
	UNIQUE(diag_bid_id)
);

CREATE SEQUENCE user_seq
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;

CREATE SEQUENCE product_seq
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;

CREATE SEQUENCE client_seq
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;

CREATE SEQUENCE order_seq
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;

CREATE SEQUENCE bid_seq
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;

CREATE SEQUENCE u_right_seq
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;

CREATE SEQUENCE state_seq
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;

CREATE SEQUENCE component_seq
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;

CREATE SEQUENCE work_seq
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;

INSERT INTO User_right VALUES(u_right_seq.NEXTVAL, 'admin');
INSERT INTO User_right VALUES(u_right_seq.NEXTVAL, 'store');
INSERT INTO User_right VALUES(u_right_seq.NEXTVAL, 'service');
INSERT INTO User_right VALUES(u_right_seq.NEXTVAL, 'head');
INSERT INTO User_right VALUES(u_right_seq.NEXTVAL, 'guest');
INSERT INTO User_info VALUES(user_seq.NEXTVAL, 'VIAB', '05f78b2a9b842d722047037c05171eb3', 'Viktor Rotar', 'viab92@outlook.com', u_right_seq.CURRVAL);









