CREATE TABLE users (
    user_id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('PERSON', 'TRAVEL_AGENCY', 'ADMIN', 'DEVELOPER')),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted_at TIMESTAMPTZ,
    CONSTRAINT uq_users_email UNIQUE (email)
);

CREATE TABLE person_profiles (
    user_id BIGINT PRIMARY KEY REFERENCES users(user_id) ON DELETE CASCADE,
    name VARCHAR(100),
    surname VARCHAR(100),
    date_of_birth DATE
);

CREATE TABLE travel_agency_profiles (
    user_id BIGINT PRIMARY KEY REFERENCES users(user_id) ON DELETE CASCADE,
    company_name VARCHAR(200) NOT NULL,
    company_email VARCHAR(255),
    tax_id VARCHAR(80)
);

CREATE TABLE admin_profiles (
    user_id BIGINT PRIMARY KEY REFERENCES users(user_id) ON DELETE CASCADE,
    admin_name VARCHAR(150) NOT NULL
);

CREATE TABLE developer_profiles (
    user_id BIGINT PRIMARY KEY REFERENCES users(user_id) ON DELETE CASCADE,
    developer_name VARCHAR(150) NOT NULL
);

CREATE TABLE sessions (
    session_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    token_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    expires_at TIMESTAMPTZ NOT NULL,
    terminated_at TIMESTAMPTZ,
    CONSTRAINT uq_sessions_token_hash UNIQUE (token_hash)
);

CREATE TABLE password_reset_tokens (
    reset_token_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    token_hash VARCHAR(255) NOT NULL,
    expires_at TIMESTAMPTZ NOT NULL,
    used_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_password_reset_tokens_token_hash UNIQUE (token_hash)
);

CREATE TABLE airlines (
    airline_id BIGSERIAL PRIMARY KEY,
    airline_name VARCHAR(150) NOT NULL,
    CONSTRAINT uq_airlines_name UNIQUE (airline_name)
);

CREATE TABLE airports (
    airport_id BIGSERIAL PRIMARY KEY,
    iata_code CHAR(3),
    airport_name VARCHAR(200) NOT NULL,
    nation VARCHAR(120) NOT NULL,
    CONSTRAINT uq_airports_iata_code UNIQUE (iata_code)
);

CREATE TABLE planes (
    plane_id BIGSERIAL PRIMARY KEY,
    airline_id BIGINT NOT NULL REFERENCES airlines(airline_id),
    seat_capacity INTEGER NOT NULL CHECK (seat_capacity > 0)
);

CREATE TABLE flight_paths (
    flight_path_id BIGSERIAL PRIMARY KEY,
    departure_airport_id BIGINT NOT NULL REFERENCES airports(airport_id),
    destination_airport_id BIGINT NOT NULL REFERENCES airports(airport_id),
    duration_minutes INTEGER NOT NULL CHECK (duration_minutes > 0),
    distance_km NUMERIC(10, 2) NOT NULL CHECK (distance_km > 0),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    CHECK (departure_airport_id <> destination_airport_id)
);

CREATE TABLE flights (
    flight_id BIGSERIAL PRIMARY KEY,
    plane_id BIGINT NOT NULL REFERENCES planes(plane_id),
    flight_path_id BIGINT NOT NULL REFERENCES flight_paths(flight_path_id),
    departure_time TIMESTAMPTZ NOT NULL,
    check_in_open_at TIMESTAMPTZ,
    check_in_close_at TIMESTAMPTZ,
    status VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED' CHECK (status IN ('SCHEDULED', 'DELAYED', 'CANCELLED', 'COMPLETED'))
);

CREATE TABLE flight_seats (
    flight_seat_id BIGSERIAL PRIMARY KEY,
    flight_id BIGINT NOT NULL REFERENCES flights(flight_id) ON DELETE CASCADE,
    seat_number INTEGER NOT NULL CHECK (seat_number > 0),
    seat_class VARCHAR(20) NOT NULL CHECK (seat_class IN ('ECONOMY', 'BUSINESS', 'FIRST')),
    base_price NUMERIC(10, 2) NOT NULL CHECK (base_price >= 0),
    seat_status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE' CHECK (seat_status IN ('AVAILABLE', 'HELD', 'SOLD', 'BLOCKED')),
    CONSTRAINT uq_flight_seat_number UNIQUE (flight_id, seat_number)
);

CREATE TABLE passengers (
    passenger_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(user_id) ON DELETE SET NULL,
    name VARCHAR(100) NOT NULL,
    surname VARCHAR(100) NOT NULL,
    email VARCHAR(255),
    date_of_birth DATE
);

CREATE TABLE purchases (
    purchase_id BIGSERIAL PRIMARY KEY,
    buyer_user_id BIGINT NOT NULL REFERENCES users(user_id),
    purchase_type VARCHAR(20) NOT NULL CHECK (purchase_type IN ('INDIVIDUAL', 'BULK')),
    purchase_date TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    gross_amount NUMERIC(10, 2) NOT NULL CHECK (gross_amount >= 0),
    discount_percent INTEGER NOT NULL DEFAULT 0 CHECK (discount_percent BETWEEN 0 AND 100),
    final_amount NUMERIC(10, 2) NOT NULL CHECK (final_amount >= 0),
    receipt_number VARCHAR(80) NOT NULL,
    purchase_status VARCHAR(20) NOT NULL DEFAULT 'PENDING' CHECK (purchase_status IN ('PENDING', 'PAID', 'CANCELLED', 'REFUNDED')),
    CONSTRAINT uq_purchases_receipt_number UNIQUE (receipt_number)
);

CREATE TABLE payments (
    payment_id BIGSERIAL PRIMARY KEY,
    purchase_id BIGINT NOT NULL REFERENCES purchases(purchase_id) ON DELETE CASCADE,
    provider VARCHAR(80) NOT NULL,
    external_payment_ref VARCHAR(120) NOT NULL,
    amount NUMERIC(10, 2) NOT NULL CHECK (amount >= 0),
    currency CHAR(3) NOT NULL DEFAULT 'EUR',
    payment_status VARCHAR(20) NOT NULL CHECK (payment_status IN ('AUTHORIZED', 'CAPTURED', 'FAILED', 'REFUNDED')),
    paid_at TIMESTAMPTZ
);

CREATE TABLE tickets (
    ticket_id BIGSERIAL PRIMARY KEY,
    purchase_id BIGINT NOT NULL REFERENCES purchases(purchase_id) ON DELETE CASCADE,
    passenger_id BIGINT NOT NULL REFERENCES passengers(passenger_id),
    flight_seat_id BIGINT NOT NULL REFERENCES flight_seats(flight_seat_id),
    ticket_price NUMERIC(10, 2) NOT NULL CHECK (ticket_price >= 0),
    ticket_status VARCHAR(20) NOT NULL DEFAULT 'ISSUED' CHECK (ticket_status IN ('ISSUED', 'CHECKED_IN', 'CANCELLED', 'UPGRADED')),
    issued_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    checked_in_at TIMESTAMPTZ
);

CREATE TABLE notifications (
    notification_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    ticket_id BIGINT REFERENCES tickets(ticket_id) ON DELETE CASCADE,
    notification_type VARCHAR(40) NOT NULL CHECK (notification_type IN ('INVOICE', 'CHECK_IN_WINDOW', 'FLIGHT_DELAY', 'FLIGHT_CANCELLED', 'TICKET_CANCELLED', 'NEW_DEVICE_LOGIN', 'BOARDING_PASS')),
    channel VARCHAR(20) NOT NULL DEFAULT 'EMAIL' CHECK (channel IN ('EMAIL', 'IN_APP')),
    notification_status VARCHAR(20) NOT NULL DEFAULT 'PENDING' CHECK (notification_status IN ('PENDING', 'SENT', 'FAILED')),
    scheduled_at TIMESTAMPTZ,
    sent_at TIMESTAMPTZ,
    payload JSONB
);

CREATE TABLE notification_preferences (
    user_id BIGINT PRIMARY KEY REFERENCES users(user_id) ON DELETE CASCADE,
    invoice_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    check_in_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    delay_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    new_device_enabled BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE audit_logs (
    audit_log_id BIGSERIAL PRIMARY KEY,
    actor_user_id BIGINT REFERENCES users(user_id) ON DELETE SET NULL,
    action_type VARCHAR(60) NOT NULL,
    target_table VARCHAR(80),
    target_id BIGINT,
    metadata JSONB,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE backup_runs (
    backup_run_id BIGSERIAL PRIMARY KEY,
    actor_user_id BIGINT REFERENCES users(user_id) ON DELETE SET NULL,
    backup_status VARCHAR(20) NOT NULL CHECK (backup_status IN ('STARTED', 'COMPLETED', 'FAILED', 'CANCELLED')),
    storage_uri TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    completed_at TIMESTAMPTZ
);

CREATE UNIQUE INDEX uq_active_ticket_per_flight_seat ON tickets(flight_seat_id) WHERE ticket_status IN ('ISSUED', 'CHECKED_IN');
CREATE INDEX idx_sessions_user_valid ON sessions(user_id, expires_at) WHERE terminated_at IS NULL;
CREATE INDEX idx_password_reset_tokens_user_valid ON password_reset_tokens(user_id, expires_at) WHERE used_at IS NULL;
CREATE INDEX idx_planes_airline ON planes(airline_id);
CREATE INDEX idx_flight_paths_destination ON flight_paths(destination_airport_id);
CREATE INDEX idx_flight_paths_departure_destination ON flight_paths(departure_airport_id, destination_airport_id);
CREATE INDEX idx_flight_paths_active_destination ON flight_paths(destination_airport_id, departure_airport_id) WHERE is_active = TRUE;
CREATE INDEX idx_flights_plane ON flights(plane_id);
CREATE INDEX idx_flights_departure_time ON flights(departure_time);
CREATE INDEX idx_flights_path_time ON flights(flight_path_id, departure_time);
CREATE INDEX idx_flight_seats_search ON flight_seats(flight_id, seat_status, seat_class, base_price);
CREATE INDEX idx_passengers_user ON passengers(user_id);
CREATE INDEX idx_passengers_email ON passengers(email);
CREATE INDEX idx_purchases_buyer_date ON purchases(buyer_user_id, purchase_date DESC);
CREATE INDEX idx_payments_purchase ON payments(purchase_id);
CREATE INDEX idx_tickets_purchase ON tickets(purchase_id);
CREATE INDEX idx_tickets_passenger ON tickets(passenger_id);
CREATE INDEX idx_tickets_flight_seat ON tickets(flight_seat_id);
CREATE INDEX idx_tickets_status ON tickets(ticket_status);
CREATE INDEX idx_notifications_user_status ON notifications(user_id, notification_status, scheduled_at);
CREATE INDEX idx_notifications_ticket ON notifications(ticket_id);
CREATE INDEX idx_audit_logs_actor_date ON audit_logs(actor_user_id, created_at DESC);
CREATE INDEX idx_audit_logs_action_date ON audit_logs(action_type, created_at DESC);
CREATE INDEX idx_backup_runs_actor_date ON backup_runs(actor_user_id, created_at DESC);

CREATE OR REPLACE FUNCTION touch_updated_at()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$;

CREATE TRIGGER trg_users_touch_updated_at
BEFORE UPDATE ON users
FOR EACH ROW
EXECUTE FUNCTION touch_updated_at();

