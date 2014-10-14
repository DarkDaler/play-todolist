# --- !Ups
ALTER TABLE task add fecha date;


# --- !Downs
DROP TABLE IF EXISTS task;
DROP SEQUENCE IF EXISTS task_id_seq;
DROP TABLE IF EXISTS task-user;
