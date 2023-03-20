-- name: insert-one
-- fn: first
INSERT INTO todo (title, description, completed_at)
VALUES (:title, :description, :completed_at)
RETURNING id, title, description, created_at, completed_at;

-- name: find-by-id
-- fn: first
SELECT id, title, description, created_at, completed_at
FROM todo
WHERE id = :id
LIMIT 1;

-- name: update-one
-- fn: first
UPDATE todo
SET title        = COALESCE(:title, title),
    description  = COALESCE(:description, description),
    completed_at = COALESCE(:completed_at, completed_at)
WHERE id = :id
RETURNING id, title, description, created_at, completed_at;

-- name: delete-one
-- fn: first
DELETE
FROM todo
WHERE id = :id
RETURNING id, title, description, created_at, completed_at;