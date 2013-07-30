-- Create table
create table T_CMS_USER
(
  ID           NUMBER(10) not null,
  USER_NAME         VARCHAR2(20),
  PASSWORD          VARCHAR2(50),
  ADDDATE           DATE,
  AUTHORIZED_PERSON NUMBER(10),
  STATE             NUMBER(10),
  OWN_MENU          VARCHAR2(1024),
  OWN_COLUMN        VARCHAR2(1024),
  LAST_LOGIN_TIME   DATE,
  LAST_LOGIN_IP     VARCHAR2(100),
  REALNAME          VARCHAR2(30)
)

alter table T_CMS_USER
  add constraint PK_T_CMS_USER primary key (ID)

create sequence SEQ_T_CMS_USER
minvalue 1
maxvalue 9999999999999999999999999999
start with 16
increment by 1
cache 20;

