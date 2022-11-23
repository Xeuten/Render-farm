CREATE TABLE IF NOT EXISTS "public"."users" (
    "user_id"     BIGINT,
    "password"    TEXT NOT NULL,
    "is_logged_in" BOOLEAN,
    PRIMARY KEY ("user_id")

);