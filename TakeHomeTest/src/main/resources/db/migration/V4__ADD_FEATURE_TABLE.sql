-- ===============
-- Feature Tables
-- ===============

-- Create a new table t_feature
CREATE TABLE t_feature (
    id bigint PRIMARY KEY NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    deleted_at timestamp without time zone,
    is_deleted boolean,
    version bigint,
    feature_name character varying(255),
    menu_url character varying(255),
    menu_code character varying(255),
    group_name character varying(255)
);

CREATE SEQUENCE IF NOT EXISTS public.t_feature_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.t_feature_id_seq OWNED BY public.t_feature.id;

ALTER TABLE ONLY public.t_feature ALTER COLUMN id SET DEFAULT nextval('public.t_feature_id_seq'::regclass);

-- ====================
-- Role Feature Tables
-- ====================

-- Create a new table t_feature
CREATE TABLE t_role_feature (
    id bigint PRIMARY KEY NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    deleted_at timestamp without time zone,
    is_deleted boolean,
    version bigint,
    role_id bigint,
    feature_id bigint,
    CONSTRAINT fk_t_role_feature_role_id FOREIGN KEY (role_id) REFERENCES t_role(id),
    CONSTRAINT fk_t_role_feature_feature_id FOREIGN KEY (feature_id) REFERENCES t_feature(id)
);

CREATE SEQUENCE IF NOT EXISTS public.t_role_feature_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.t_role_feature_id_seq OWNED BY public.t_role_feature.id;

ALTER TABLE ONLY public.t_role_feature ALTER COLUMN id SET DEFAULT nextval('public.t_role_feature_id_seq'::regclass);