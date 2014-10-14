# --- !Ups
CREATE TABLE taskUser (
   id varchar(255)PRIMARY KEY NOT NULL
);

ALTER TABLE task add idUser varchar(255);
ALTER TABLE task add constraint id_user foreign key(idUser) references taskUser(id);

# --- !Downs

DROP TABLE IF EXISTS task;
DROP SEQUENCE IF EXISTS task_id_seq;
DROP TABLE IF EXISTS task-user;

# --- Sample dataset

# --- !Ups

insert into taskUser (id) values ('admin');
insert into taskUser (id) values ('anonimo');

# --- !Downs

delete from taskUser;