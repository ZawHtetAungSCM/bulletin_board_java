INSERT INTO users (
    name,
    email,
    password,
    profile,
    type,
    phone,
    address,
    dob,
    created_user_id,
    updated_user_id,
    deleted_user_id)
VALUES
(
    'Admin User',
    'admin@gmail.com',
    '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', -- Hash value of "password"
    'Software Engineer',
    '1',
    '123456789',
    '123 Main St,
    Cityville',
    '1990-01-15',
    NULL,
    NULL,
    NULL),
(
    'Jane Smith',
    'jane@example.com',
    '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', -- Hash value of "password"
    'Data Scientist',
    '2',
    '987654321',
    '456 Broad St,
    Townsville',
    '1985-08-22',
    NULL,
    NULL,
    NULL),
(
    'Bob Johnson',
    'bob@example.com',
    '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', -- Hash value of "password"
    'UX Designer',
    '1',
    '5551234567',
    '789 Oak St,
    Villagetown',
    '1995-03-10',
    NULL,
    NULL,
    NULL);
