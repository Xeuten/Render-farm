CREATE TABLE IF NOT EXISTS "public"."users" (
    "username"  TEXT,
    PRIMARY KEY ("username")

);

CREATE TABLE IF NOT EXISTS "public"."user_tasks" (
    "task_id"     TEXT,
    "username"    TEXT NOT NULL,
    "task_name"   TEXT NOT NULL,
    "start_time"  TIMESTAMP WITH TIME ZONE NOT NULL,
    "end_time"    TIMESTAMP WITH TIME ZONE,
    "status"      TEXT NOT NULL,
    PRIMARY KEY ("task_id")

);