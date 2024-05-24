DROP TABLE IF EXISTS community, neighbor, item, wish, comment, muted, nearby;

CREATE TABLE community(
    id integer NOT NULL,
    PRIMARY KEY (id),
    name text COLLATE pg_catalog."default",
    zip text,
    rules text COLLATE pg_catalog."default"
);

CREATE TABLE neighbor(
    id integer NOT NULL,
    PRIMARY KEY (id),
    community_id integer,
    name text COLLATE pg_catalog."default",
    mod boolean NOT NULL,
    birthday date
);

CREATE TABLE wish(
    id integer NOT NULL,
    PRIMARY KEY (id),
    neighbor_id integer,
    expiration_date date,
    fulfilled_date date
);

CREATE TABLE comment(
    id integer NOT NULL,
    PRIMARY KEY (id),
    neighbor_id integer,
    wish_id integer,
    message text COLLATE pg_catalog.default
);

CREATE TABLE nearby(
    community_a integer,
    community_b integer,
    UNIQUE(community_a, community_b)
);

CREATE TABLE muted(
    neighbor_a integer,
    neighbor_b integer,
    UNIQUE(neighbor_a, neighbor_b)
);

CREATE TABLE item(
    id integer NOT NULL,
    PRIMARY KEY (id),
    name text COLLATE pg_catalog.default,
    date_visible date,
    wish_id integer,
    neighbor_id integer,
    released boolean,
	post_date date,
    UNIQUE(name, wish_id)
);
