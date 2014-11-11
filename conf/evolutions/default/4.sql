# --- !Ups

CREATE TABLE categoria (
   categoria varchar(50)PRIMARY KEY NOT NULL
);

CREATE TABLE categoria_users (
   user varchar(50),
   categoria varchar(50),
   PRIMARY KEY(user, categoria),
);

ALTER TABLE categoria_users ADD FOREIGN KEY(user) REFERENCES taskUser(id);
ALTER TABLE categoria_users ADD FOREIGN KEY(categoria) REFERENCES categoria(categoria);

ALTER TABLE task ADD categoriaTask varchar(50);
ALTER TABLE task ADD FOREIGN KEY(categoriaTask) REFERENCES categoria(categoria);

# --- !Downs

DROP TABLE IF EXISTS categoria;
DROP TABLE IF EXISTS categoria_users