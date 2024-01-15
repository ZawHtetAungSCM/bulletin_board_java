-- Create the sequence if it doesn't exist
CREATE SEQUENCE IF NOT EXISTS posts_id_seq;

-- Create the posts table
CREATE TABLE IF NOT EXISTS public.posts
(
    id integer NOT NULL DEFAULT nextval('posts_id_seq'::regclass),
    title character varying(255) COLLATE pg_catalog."default" NOT NULL,
    description character varying(255) COLLATE pg_catalog."default",
    status integer,
    created_user_id integer,
    updated_user_id integer,
    deleted_user_id integer,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone,
    deleted_at timestamp without time zone,
    CONSTRAINT posts_pkey PRIMARY KEY (id),
    CONSTRAINT posts_title_key UNIQUE (title),
    CONSTRAINT fk_created_user_post FOREIGN KEY (created_user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_deleted_user_post FOREIGN KEY (deleted_user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_updated_user_post FOREIGN KEY (updated_user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT posts_status_check CHECK (status = ANY (ARRAY[0, 1]))
);
