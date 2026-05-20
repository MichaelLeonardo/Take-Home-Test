-- ===============
-- Leave Tables
-- ===============

-- Create a new table t_leave
CREATE TABLE t_leave (
    id bigint PRIMARY KEY NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    deleted_at timestamp without time zone,
    is_deleted boolean,
    version bigint,
    date_from date,
    date_to date,
    leave_status INTEGER,
    user_id bigint,
    total_days bigint,
    leave_reason TEXT,
    CONSTRAINT fk_t_user_leave_id FOREIGN KEY (user_id) REFERENCES t_user(id)
);

CREATE SEQUENCE IF NOT EXISTS public.t_leave_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.t_leave_id_seq OWNED BY public.t_leave.id;

ALTER TABLE ONLY public.t_leave ALTER COLUMN id SET DEFAULT nextval('public.t_leave_id_seq'::regclass);