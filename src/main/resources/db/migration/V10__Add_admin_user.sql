INSERT INTO users (id, username, email, password, created_at, user_role)
VALUES (gen_random_uuid(),'admin', 'admin@example.com', '$2a$10$sJ/fNZRizPkO4eLtZXAs2uim4aDxJYllq4wWmiTejCnxx41o0FVQm', CURRENT_TIMESTAMP,'ADMIN');
