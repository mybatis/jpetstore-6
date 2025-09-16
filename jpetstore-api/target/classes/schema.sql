-- JPetStore Database Schema for H2
CREATE TABLE IF NOT EXISTS supplier (
    suppid INT NOT NULL,
    name VARCHAR(80),
    status VARCHAR(2) NOT NULL,
    addr1 VARCHAR(80),
    addr2 VARCHAR(80),
    city VARCHAR(80),
    state VARCHAR(80),
    zip VARCHAR(5),
    phone VARCHAR(80),
    PRIMARY KEY (suppid)
);

CREATE TABLE IF NOT EXISTS signon (
    username VARCHAR(25) NOT NULL,
    password VARCHAR(25) NOT NULL,
    PRIMARY KEY (username)
);

CREATE TABLE IF NOT EXISTS account (
    userid VARCHAR(80) NOT NULL,
    email VARCHAR(80) NOT NULL,
    firstname VARCHAR(80) NOT NULL,
    lastname VARCHAR(80) NOT NULL,
    status VARCHAR(2),
    addr1 VARCHAR(80) NOT NULL,
    addr2 VARCHAR(40),
    city VARCHAR(80) NOT NULL,
    state VARCHAR(80) NOT NULL,
    zip VARCHAR(20) NOT NULL,
    country VARCHAR(20) NOT NULL,
    phone VARCHAR(80) NOT NULL,
    PRIMARY KEY (userid)
);

CREATE TABLE IF NOT EXISTS profile (
    userid VARCHAR(80) NOT NULL,
    langpref VARCHAR(80) NOT NULL,
    favcategory VARCHAR(30),
    mylistopt INT,
    banneropt INT,
    PRIMARY KEY (userid)
);

CREATE TABLE IF NOT EXISTS category (
    catid VARCHAR(10) NOT NULL,
    name VARCHAR(80),
    descn VARCHAR(255),
    PRIMARY KEY (catid)
);

CREATE TABLE IF NOT EXISTS product (
    productid VARCHAR(10) NOT NULL,
    category VARCHAR(10) NOT NULL,
    name VARCHAR(80),
    descn VARCHAR(255),
    PRIMARY KEY (productid),
    FOREIGN KEY (category) REFERENCES category (catid)
);

CREATE TABLE IF NOT EXISTS item (
    itemid VARCHAR(10) NOT NULL,
    productid VARCHAR(10) NOT NULL,
    listprice DECIMAL(10,2),
    unitcost DECIMAL(10,2),
    supplier INT,
    status VARCHAR(2),
    attr1 VARCHAR(80),
    attr2 VARCHAR(80),
    attr3 VARCHAR(80),
    attr4 VARCHAR(80),
    attr5 VARCHAR(80),
    PRIMARY KEY (itemid),
    FOREIGN KEY (productid) REFERENCES product (productid),
    FOREIGN KEY (supplier) REFERENCES supplier (suppid)
);

CREATE TABLE IF NOT EXISTS inventory (
    itemid VARCHAR(10) NOT NULL,
    qty INT NOT NULL,
    PRIMARY KEY (itemid)
);

CREATE INDEX IF NOT EXISTS productCat ON product (category);
CREATE INDEX IF NOT EXISTS productName ON product (name);
CREATE INDEX IF NOT EXISTS itemProd ON item (productid);