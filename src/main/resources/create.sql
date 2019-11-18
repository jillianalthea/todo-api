CREATE TABLE todo (
  id VARCHAR(MAX) NOT NULL AUTO_INCREMENT,
  text VARCHAR(MAX),
  complete INT,
  creationDate DATE ,
  completeDate DATE ,
  priority INT,
  PRIMARY KEY (id)
);
