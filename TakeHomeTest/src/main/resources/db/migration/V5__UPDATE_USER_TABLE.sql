-- ===============
-- User Tables
-- ===============

-- Add Column role_id
ALTER TABLE public.t_user
    ADD COLUMN IF NOT EXISTS is_active BOOLEAN DEFAULT TRUE;