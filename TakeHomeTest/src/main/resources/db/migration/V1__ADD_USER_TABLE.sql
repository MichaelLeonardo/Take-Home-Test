-- ===============
-- User Tables
-- ===============

-- Create a new table t_user
CREATE TABLE t_user (
   id bigint PRIMARY KEY NOT NULL,
   created_at timestamp without time zone,
   updated_at timestamp without time zone,
   deleted_at timestamp without time zone,
   is_deleted boolean,
   version bigint,
   full_name character varying(255),
   username character varying(255),
   password character varying(255),
   remaining_days_off INTEGER
);

CREATE SEQUENCE IF NOT EXISTS public.t_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.t_user_id_seq OWNED BY public.t_user.id;

ALTER TABLE ONLY public.t_user ALTER COLUMN id SET DEFAULT nextval('public.t_user_id_seq'::regclass);