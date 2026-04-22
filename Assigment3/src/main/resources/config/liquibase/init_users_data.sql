-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert sample user data
INSERT INTO users (name, email, password, role) VALUES
('John Admin', 'admin@example.com', 'hashedPassword123', 'ADMIN'),
('Jane User', 'jane.user@example.com', 'hashedPassword456', 'USER'),
('Bob Developer', 'bob.dev@example.com', 'hashedPassword789', 'USER'),
('Alice Manager', 'alice.manager@example.com', 'hashedPasswordABC', 'ADMIN'),
('Charlie User', 'charlie@example.com', 'hashedPasswordDEF', 'USER');

