-- ===============
-- Role Tables
-- ===============

-- Create a new table t_role
CREATE TABLE t_role (
    id bigint PRIMARY KEY NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    deleted_at timestamp without time zone,
    is_deleted boolean,
    version bigint,
    role_name character varying(255),
    description character varying(255),
    is_admin boolean
);

CREATE SEQUENCE IF NOT EXISTS public.t_role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.t_role_id_seq OWNED BY public.t_role.id;

ALTER TABLE ONLY public.t_role ALTER COLUMN id SET DEFAULT nextval('public.t_role_id_seq'::regclass);