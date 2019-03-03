drop table test_wannabe;
create table test_wannabe (
    id VARCHAR(255) PRIMARY KEY,
    some_attribute VARCHAR(255),
    some_integer_attribute NUMBER(1),
    timestamp_test TIMESTAMP,
    date_test DATE,
    another_one VARCHAR(255)
);
drop sequence seq_test_wannabe;
create sequence seq_test_wannabe;
create trigger trg_test_wannabe
before insert on test_wannabe
for each row
begin
  select 'T'||seq_test_wannabe.nextval
  into :new.id
  from dual;
end;
/

INSERT INTO test_wannabe (some_attribute, some_integer_attribute, timestamp_test, date_test, another_one) VALUES ('michael', 1, TO_TIMESTAMP('1997-11-11 11:11:11', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('10-05-2002', 'DD-MM-YYYY'), 'a');
INSERT INTO test_wannabe (some_attribute, some_integer_attribute, timestamp_test, date_test, another_one) VALUES ('elodie', 5, TO_TIMESTAMP('2000-05-17 10:05:10', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('08-05-2007', 'DD-MM-YYYY'), 'b');
INSERT INTO test_wannabe (some_attribute, some_integer_attribute, timestamp_test, date_test, another_one) VALUES ('mialy', 7, TO_TIMESTAMP('1997-12-11 12:12:12', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('13-04-1971', 'DD-MM-YYYY'), 'c');
INSERT INTO test_wannabe (some_attribute, some_integer_attribute, timestamp_test, date_test, another_one) VALUES ('bary', 8, TO_TIMESTAMP('2001-01-05 14:10:55', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('08-08-1968', 'DD-MM-YYYY'), 'd');
INSERT INTO test_wannabe (some_attribute, some_integer_attribute, timestamp_test, date_test, another_one) VALUES ('jourdan', 6, TO_TIMESTAMP('2000-08-06 13:00:05', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('12-02-2000', 'DD-MM-YYYY'), 'e');


create table another (
  first_id VARCHAR(255),
  second_id VARCHAR(255),
  not_id VARCHAR(255),
  PRIMARY KEY(first_id, second_id)
);