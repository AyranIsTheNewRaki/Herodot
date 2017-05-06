DROP TABLE IF EXISTS User_Authority;
DROP TABLE IF EXISTS Authority;
DROP TABLE IF EXISTS Account;

CREATE TABLE Account (
  id BIGSERIAL PRIMARY KEY,
  username VARCHAR(50) NOT NULL CONSTRAINT UQ_Account_Username UNIQUE,
  password VARCHAR(100) NOT NULL,
  firstname VARCHAR(50),
  lastname VARCHAR(50),
  email VARCHAR(100) NOT NULL,
  enabled BOOLEAN NOT NULL,
  lastPasswordResetDate DATE NOT NULL
);

CREATE TABLE Authority (
  id BIGSERIAL NOT NULL CONSTRAINT UQ_Authority_Id UNIQUE,
  name VARCHAR(50) NOT NULL CONSTRAINT UQ_Authority_Name UNIQUE
);

CREATE TABLE User_Authority (
  account_Id BIGSERIAL,
  authority_Id BIGSERIAL,
  PRIMARY KEY (account_Id, authority_Id),
  CONSTRAINT FK_User_Authority_Account_Id FOREIGN KEY (account_Id) REFERENCES Account (id),
  CONSTRAINT FK_User_Authority_Authority_Id FOREIGN KEY (authority_Id) REFERENCES Authority (id)
);