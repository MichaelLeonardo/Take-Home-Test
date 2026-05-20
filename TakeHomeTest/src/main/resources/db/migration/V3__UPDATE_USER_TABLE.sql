-- ===============
-- User Tables
-- ===============

-- Drop Column if exists
ALTER TABLE public.t_user
    DROP column IF EXISTS role_id;

-- Add Column role_id
ALTER TABLE public.t_user
    ADD column IF NOT EXISTS role_id BIGINT;

-- Drop constraint if exists
ALTER TABLE public.t_user
    DROP CONSTRAINT IF EXISTS fk_t_user_role;

-- Adding foreign key constraint
ALTER TABLE public.t_user
    ADD CONSTRAINT fk_t_user_role
        FOREIGN KEY (role_id) REFERENCES public.t_role(id);