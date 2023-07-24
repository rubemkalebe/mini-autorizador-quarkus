CREATE TABLE card (
  car_code varchar(36) NOT NULL,
  car_number varchar(36) NOT NULL,
  car_password varchar(264) NOT NULL,
  car_creation_date datetime NOT NULL,
  car_balance DECIMAL(13,2),
  PRIMARY KEY (car_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
