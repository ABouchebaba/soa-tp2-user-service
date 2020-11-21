CREATE TABLE IF NOT EXISTS users
(
    id integer auto_increment,
    username character varying(255)  NOT NULL,
    email character varying(255)  NOT NULL,
    created_at timestamp without time zone DEFAULT now()
);