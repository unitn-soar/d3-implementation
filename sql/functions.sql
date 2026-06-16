CREATE OR REPLACE FUNCTION search_direct_flights(
    p_destination_airport_id BIGINT DEFAULT NULL,
    p_departure_airport_id BIGINT DEFAULT NULL,
    p_flight_date DATE DEFAULT CURRENT_DATE,
    p_earliest_departure TIME DEFAULT NULL,
    p_latest_departure TIME DEFAULT NULL,
    p_max_price NUMERIC DEFAULT NULL,
    p_seat_class VARCHAR DEFAULT NULL,
    p_airline_id BIGINT DEFAULT NULL,
    p_sort_by VARCHAR DEFAULT 'DEPARTURE_TIME'
)
RETURNS TABLE (
    flight_id BIGINT,
    departure_airport VARCHAR,
    destination_airport VARCHAR,
    airline_name VARCHAR,
    departure_time TIMESTAMPTZ,
    arrival_time TIMESTAMPTZ,
    duration_minutes INTEGER,
    lowest_price NUMERIC,
    available_seats BIGINT
)
LANGUAGE sql
AS $$
    SELECT
        f.flight_id,
        dep.airport_name,
        dst.airport_name,
        al.airline_name,
        f.departure_time,
        f.departure_time + (fp.duration_minutes || ' minutes')::INTERVAL,
        fp.duration_minutes,
        MIN(fs.base_price),
        COUNT(fs.flight_seat_id)
    FROM flights f
    JOIN flight_paths fp ON fp.flight_path_id = f.flight_path_id
    JOIN airports dep ON dep.airport_id = fp.departure_airport_id
    JOIN airports dst ON dst.airport_id = fp.destination_airport_id
    JOIN planes pl ON pl.plane_id = f.plane_id
    JOIN airlines al ON al.airline_id = pl.airline_id
    JOIN flight_seats fs ON fs.flight_id = f.flight_id
    WHERE f.status IN ('SCHEDULED', 'DELAYED')
      AND fp.is_active = TRUE
      AND fs.seat_status = 'AVAILABLE'
      AND (p_destination_airport_id IS NULL OR dst.airport_id = p_destination_airport_id)
      AND (p_departure_airport_id IS NULL OR dep.airport_id = p_departure_airport_id)
      AND f.departure_time >= COALESCE(p_flight_date, CURRENT_DATE)
      AND f.departure_time < COALESCE(p_flight_date, CURRENT_DATE) + INTERVAL '1 day'
      AND (p_earliest_departure IS NULL OR f.departure_time::TIME >= p_earliest_departure)
      AND (p_latest_departure IS NULL OR f.departure_time::TIME <= p_latest_departure)
      AND (p_airline_id IS NULL OR al.airline_id = p_airline_id)
      AND (p_seat_class IS NULL OR fs.seat_class = p_seat_class)
      AND NOT EXISTS (
          SELECT 1
          FROM tickets t
          WHERE t.flight_seat_id = fs.flight_seat_id
            AND t.ticket_status IN ('ISSUED', 'CHECKED_IN')
      )
    GROUP BY f.flight_id, dep.airport_name, dst.airport_name, al.airline_name, f.departure_time, fp.duration_minutes
    HAVING p_max_price IS NULL OR MIN(fs.base_price) <= p_max_price
    ORDER BY
        CASE WHEN p_sort_by = 'PRICE' THEN MIN(fs.base_price) END ASC,
        CASE WHEN p_sort_by = 'DURATION' THEN fp.duration_minutes END ASC,
        CASE WHEN p_sort_by = 'DEPARTURE_TIME' THEN f.departure_time END ASC,
        f.departure_time ASC
$$;

CREATE OR REPLACE FUNCTION search_one_stop_routes(
    p_departure_airport_id BIGINT,
    p_destination_airport_id BIGINT,
    p_flight_date DATE DEFAULT CURRENT_DATE,
    p_max_layover_minutes INTEGER DEFAULT 360,
    p_max_price NUMERIC DEFAULT NULL,
    p_seat_class VARCHAR DEFAULT NULL,
    p_airline_id BIGINT DEFAULT NULL,
    p_sort_by VARCHAR DEFAULT 'DEPARTURE_TIME'
)
RETURNS TABLE (
    first_flight_id BIGINT,
    second_flight_id BIGINT,
    departure_airport_id BIGINT,
    stopover_airport_id BIGINT,
    destination_airport_id BIGINT,
    departure_time TIMESTAMPTZ,
    arrival_time TIMESTAMPTZ,
    total_flight_minutes INTEGER,
    layover_time INTERVAL,
    total_lowest_price NUMERIC,
    route_available_seats BIGINT
)
LANGUAGE sql
AS $$
    WITH available_flights AS (
        SELECT
            f.flight_id,
            fp.departure_airport_id,
            fp.destination_airport_id,
            f.departure_time,
            f.departure_time + (fp.duration_minutes || ' minutes')::INTERVAL AS arrival_time,
            fp.duration_minutes,
            MIN(fs.base_price) AS lowest_price,
            COUNT(fs.flight_seat_id) AS available_seats
        FROM flights f
        JOIN flight_paths fp ON fp.flight_path_id = f.flight_path_id
        JOIN flight_seats fs ON fs.flight_id = f.flight_id
        JOIN planes pl ON pl.plane_id = f.plane_id
        WHERE f.status IN ('SCHEDULED', 'DELAYED')
          AND fp.is_active = TRUE
          AND fs.seat_status = 'AVAILABLE'
          AND (p_airline_id IS NULL OR pl.airline_id = p_airline_id)
          AND (p_seat_class IS NULL OR fs.seat_class = p_seat_class)
          AND f.departure_time >= p_flight_date
          AND f.departure_time < p_flight_date + INTERVAL '1 day'
          AND NOT EXISTS (
              SELECT 1
              FROM tickets t
              WHERE t.flight_seat_id = fs.flight_seat_id
                AND t.ticket_status IN ('ISSUED', 'CHECKED_IN')
          )
        GROUP BY f.flight_id, fp.departure_airport_id, fp.destination_airport_id, f.departure_time, fp.duration_minutes
    )
    SELECT
        first_leg.flight_id,
        second_leg.flight_id,
        first_leg.departure_airport_id,
        first_leg.destination_airport_id,
        second_leg.destination_airport_id,
        first_leg.departure_time,
        second_leg.arrival_time,
        first_leg.duration_minutes + second_leg.duration_minutes,
        second_leg.departure_time - first_leg.arrival_time,
        first_leg.lowest_price + second_leg.lowest_price,
        LEAST(first_leg.available_seats, second_leg.available_seats)
    FROM available_flights first_leg
    JOIN available_flights second_leg ON second_leg.departure_airport_id = first_leg.destination_airport_id
    WHERE first_leg.departure_airport_id = p_departure_airport_id
      AND second_leg.destination_airport_id = p_destination_airport_id
      AND second_leg.departure_time >= first_leg.arrival_time + INTERVAL '45 minutes'
      AND second_leg.departure_time <= first_leg.arrival_time + (p_max_layover_minutes || ' minutes')::INTERVAL
      AND (p_max_price IS NULL OR first_leg.lowest_price + second_leg.lowest_price <= p_max_price)
    ORDER BY
        CASE WHEN p_sort_by = 'PRICE' THEN first_leg.lowest_price + second_leg.lowest_price END ASC,
        CASE WHEN p_sort_by = 'DURATION' THEN second_leg.arrival_time - first_leg.departure_time END ASC,
        CASE WHEN p_sort_by = 'DEPARTURE_TIME' THEN first_leg.departure_time END ASC
$$;

CREATE OR REPLACE FUNCTION get_available_seats(
    p_flight_id BIGINT,
    p_seat_class VARCHAR DEFAULT NULL
)
RETURNS TABLE (
    flight_seat_id BIGINT,
    seat_number INTEGER,
    seat_class VARCHAR,
    base_price NUMERIC
)
LANGUAGE sql
AS $$
    SELECT fs.flight_seat_id, fs.seat_number, fs.seat_class, fs.base_price
    FROM flight_seats fs
    WHERE fs.flight_id = p_flight_id
      AND fs.seat_status = 'AVAILABLE'
      AND (p_seat_class IS NULL OR fs.seat_class = p_seat_class)
      AND NOT EXISTS (
          SELECT 1
          FROM tickets t
          WHERE t.flight_seat_id = fs.flight_seat_id
            AND t.ticket_status IN ('ISSUED', 'CHECKED_IN')
      )
    ORDER BY fs.seat_class, fs.seat_number
$$;

CREATE OR REPLACE FUNCTION create_individual_purchase(
    p_buyer_user_id BIGINT,
    p_passenger_user_id BIGINT,
    p_passenger_name VARCHAR,
    p_passenger_surname VARCHAR,
    p_passenger_email VARCHAR,
    p_passenger_date_of_birth DATE,
    p_flight_seat_id BIGINT,
    p_payment_provider VARCHAR,
    p_external_payment_ref VARCHAR,
    p_currency CHAR(3) DEFAULT 'EUR'
)
RETURNS BIGINT
LANGUAGE plpgsql
AS $$
DECLARE
    v_passenger_id BIGINT;
    v_purchase_id BIGINT;
    v_ticket_id BIGINT;
    v_price NUMERIC(10, 2);
    v_receipt_number VARCHAR(80);
BEGIN
    SELECT fs.base_price INTO v_price
    FROM flight_seats fs
    WHERE fs.flight_seat_id = p_flight_seat_id
      AND fs.seat_status = 'AVAILABLE'
    FOR UPDATE;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'seat not available';
    END IF;

    INSERT INTO passengers (user_id, name, surname, email, date_of_birth)
    VALUES (p_passenger_user_id, p_passenger_name, p_passenger_surname, p_passenger_email, p_passenger_date_of_birth)
    RETURNING passenger_id INTO v_passenger_id;

    v_receipt_number = 'R-' || TO_CHAR(CLOCK_TIMESTAMP(), 'YYYYMMDDHH24MISSMS') || '-' || SUBSTRING(MD5(RANDOM()::TEXT), 1, 8);

    INSERT INTO purchases (buyer_user_id, purchase_type, gross_amount, discount_percent, final_amount, receipt_number, purchase_status)
    VALUES (p_buyer_user_id, 'INDIVIDUAL', v_price, 0, v_price, v_receipt_number, 'PAID')
    RETURNING purchase_id INTO v_purchase_id;

    INSERT INTO payments (purchase_id, provider, external_payment_ref, amount, currency, payment_status, paid_at)
    VALUES (v_purchase_id, p_payment_provider, p_external_payment_ref, v_price, p_currency, 'CAPTURED', NOW());

    UPDATE flight_seats
    SET seat_status = 'SOLD'
    WHERE flight_seat_id = p_flight_seat_id;

    INSERT INTO tickets (purchase_id, passenger_id, flight_seat_id, ticket_price, ticket_status)
    VALUES (v_purchase_id, v_passenger_id, p_flight_seat_id, v_price, 'ISSUED')
    RETURNING ticket_id INTO v_ticket_id;

    INSERT INTO audit_logs (actor_user_id, action_type, target_table, target_id, metadata)
    VALUES (p_buyer_user_id, 'TICKET_PURCHASE', 'purchases', v_purchase_id, JSONB_BUILD_OBJECT('ticket_id', v_ticket_id));

    INSERT INTO notifications (user_id, ticket_id, notification_type, channel, notification_status, scheduled_at)
    VALUES (p_buyer_user_id, v_ticket_id, 'INVOICE', 'EMAIL', 'PENDING', NOW());

    RETURN v_purchase_id;
END;
$$;

CREATE OR REPLACE FUNCTION create_bulk_purchase(
    p_agency_user_id BIGINT,
    p_passengers JSONB,
    p_flight_seat_ids BIGINT[],
    p_discount_percent INTEGER,
    p_payment_provider VARCHAR,
    p_external_payment_ref VARCHAR,
    p_currency CHAR(3) DEFAULT 'EUR'
)
RETURNS BIGINT
LANGUAGE plpgsql
AS $$
DECLARE
    v_count INTEGER;
    v_locked_count INTEGER;
    v_purchase_id BIGINT;
    v_passenger_id BIGINT;
    v_ticket_id BIGINT;
    v_receipt_number VARCHAR(80);
    v_gross NUMERIC(10, 2);
    v_final NUMERIC(10, 2);
    v_index INTEGER;
    v_seat_id BIGINT;
    v_price NUMERIC(10, 2);
    v_ticket_price NUMERIC(10, 2);
    v_passenger JSONB;
    v_passenger_user_id BIGINT;
BEGIN
    PERFORM 1
    FROM users u
    JOIN travel_agency_profiles ta ON ta.user_id = u.user_id
    WHERE u.user_id = p_agency_user_id
      AND u.role = 'TRAVEL_AGENCY'
      AND u.deleted_at IS NULL;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'travel agency not valid';
    END IF;

    v_count = COALESCE(JSONB_ARRAY_LENGTH(p_passengers), 0);

    IF v_count < 5 OR v_count <> COALESCE(ARRAY_LENGTH(p_flight_seat_ids, 1), 0) THEN
        RAISE EXCEPTION 'bulk purchase requires matching passenger and seat counts of at least five';
    END IF;

    IF p_discount_percent < 0 OR p_discount_percent > 100 THEN
        RAISE EXCEPTION 'discount not valid';
    END IF;

    SELECT COUNT(*), COALESCE(SUM(locked_seats.base_price), 0) INTO v_locked_count, v_gross
    FROM (
        SELECT fs.flight_seat_id, fs.base_price
        FROM flight_seats fs
        WHERE fs.flight_seat_id = ANY(p_flight_seat_ids)
          AND fs.seat_status = 'AVAILABLE'
        FOR UPDATE
    ) locked_seats;

    IF v_locked_count <> v_count THEN
        RAISE EXCEPTION 'one or more seats are not available';
    END IF;

    v_final = ROUND(v_gross * (100 - p_discount_percent) / 100, 2);
    v_receipt_number = 'BR-' || TO_CHAR(CLOCK_TIMESTAMP(), 'YYYYMMDDHH24MISSMS') || '-' || SUBSTRING(MD5(RANDOM()::TEXT), 1, 8);

    INSERT INTO purchases (buyer_user_id, purchase_type, gross_amount, discount_percent, final_amount, receipt_number, purchase_status)
    VALUES (p_agency_user_id, 'BULK', v_gross, p_discount_percent, v_final, v_receipt_number, 'PAID')
    RETURNING purchase_id INTO v_purchase_id;

    INSERT INTO payments (purchase_id, provider, external_payment_ref, amount, currency, payment_status, paid_at)
    VALUES (v_purchase_id, p_payment_provider, p_external_payment_ref, v_final, p_currency, 'CAPTURED', NOW());

    FOR v_index IN 0..v_count - 1 LOOP
        v_passenger = p_passengers -> v_index;
        v_seat_id = p_flight_seat_ids[v_index + 1];
        v_passenger_user_id = NULLIF(v_passenger ->> 'user_id', '')::BIGINT;

        SELECT fs.base_price INTO v_price
        FROM flight_seats fs
        WHERE fs.flight_seat_id = v_seat_id;

        v_ticket_price = ROUND(v_price * (100 - p_discount_percent) / 100, 2);

        INSERT INTO passengers (user_id, name, surname, email, date_of_birth)
        VALUES (
            v_passenger_user_id,
            v_passenger ->> 'name',
            v_passenger ->> 'surname',
            v_passenger ->> 'email',
            NULLIF(v_passenger ->> 'date_of_birth', '')::DATE
        )
        RETURNING passenger_id INTO v_passenger_id;

        UPDATE flight_seats
        SET seat_status = 'SOLD'
        WHERE flight_seat_id = v_seat_id;

        INSERT INTO tickets (purchase_id, passenger_id, flight_seat_id, ticket_price, ticket_status)
        VALUES (v_purchase_id, v_passenger_id, v_seat_id, v_ticket_price, 'ISSUED')
        RETURNING ticket_id INTO v_ticket_id;

        IF v_passenger_user_id IS NOT NULL THEN
            INSERT INTO notifications (user_id, ticket_id, notification_type, channel, notification_status, scheduled_at)
            VALUES (v_passenger_user_id, v_ticket_id, 'INVOICE', 'EMAIL', 'PENDING', NOW());
        END IF;
    END LOOP;

    INSERT INTO notifications (user_id, notification_type, channel, notification_status, scheduled_at)
    VALUES (p_agency_user_id, 'INVOICE', 'EMAIL', 'PENDING', NOW());

    INSERT INTO audit_logs (actor_user_id, action_type, target_table, target_id, metadata)
    VALUES (p_agency_user_id, 'BULK_TICKET_PURCHASE', 'purchases', v_purchase_id, JSONB_BUILD_OBJECT('ticket_count', v_count, 'discount_percent', p_discount_percent));

    RETURN v_purchase_id;
END;
$$;

CREATE OR REPLACE FUNCTION get_purchase_history(
    p_buyer_user_id BIGINT,
    p_from_date TIMESTAMPTZ DEFAULT NULL,
    p_to_date TIMESTAMPTZ DEFAULT NULL,
    p_purchase_status VARCHAR DEFAULT NULL
)
RETURNS TABLE (
    purchase_id BIGINT,
    purchase_date TIMESTAMPTZ,
    purchase_type VARCHAR,
    final_amount NUMERIC,
    purchase_status VARCHAR,
    payment_status VARCHAR,
    ticket_id BIGINT,
    ticket_status VARCHAR,
    passenger_name VARCHAR,
    passenger_surname VARCHAR,
    departure_airport VARCHAR,
    destination_airport VARCHAR,
    airline_name VARCHAR,
    departure_time TIMESTAMPTZ,
    seat_class VARCHAR,
    seat_number INTEGER,
    ticket_price NUMERIC
)
LANGUAGE sql
AS $$
    SELECT
        p.purchase_id,
        p.purchase_date,
        p.purchase_type,
        p.final_amount,
        p.purchase_status,
        pay.payment_status,
        t.ticket_id,
        t.ticket_status,
        ps.name,
        ps.surname,
        dep.airport_name,
        dst.airport_name,
        al.airline_name,
        f.departure_time,
        fs.seat_class,
        fs.seat_number,
        t.ticket_price
    FROM purchases p
    LEFT JOIN LATERAL (
        SELECT py.payment_status
        FROM payments py
        WHERE py.purchase_id = p.purchase_id
        ORDER BY py.paid_at DESC NULLS LAST, py.payment_id DESC
        LIMIT 1
    ) pay ON TRUE
    JOIN tickets t ON t.purchase_id = p.purchase_id
    JOIN passengers ps ON ps.passenger_id = t.passenger_id
    JOIN flight_seats fs ON fs.flight_seat_id = t.flight_seat_id
    JOIN flights f ON f.flight_id = fs.flight_id
    JOIN flight_paths fp ON fp.flight_path_id = f.flight_path_id
    JOIN airports dep ON dep.airport_id = fp.departure_airport_id
    JOIN airports dst ON dst.airport_id = fp.destination_airport_id
    JOIN planes pl ON pl.plane_id = f.plane_id
    JOIN airlines al ON al.airline_id = pl.airline_id
    WHERE p.buyer_user_id = p_buyer_user_id
      AND (p_from_date IS NULL OR p.purchase_date >= p_from_date)
      AND (p_to_date IS NULL OR p.purchase_date < p_to_date)
      AND (p_purchase_status IS NULL OR p.purchase_status = p_purchase_status)
    ORDER BY p.purchase_date DESC, p.purchase_id DESC, t.ticket_id
$$;

CREATE OR REPLACE FUNCTION self_check_in(
    p_ticket_id BIGINT,
    p_requesting_user_id BIGINT
)
RETURNS TABLE (
    ticket_id BIGINT,
    ticket_status VARCHAR,
    checked_in_at TIMESTAMPTZ
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    UPDATE tickets t
    SET ticket_status = 'CHECKED_IN',
        checked_in_at = NOW()
    FROM passengers ps, flight_seats fs, flights f, purchases p
    WHERE t.ticket_id = p_ticket_id
      AND ps.passenger_id = t.passenger_id
      AND fs.flight_seat_id = t.flight_seat_id
      AND f.flight_id = fs.flight_id
      AND p.purchase_id = t.purchase_id
      AND (ps.user_id = p_requesting_user_id OR p.buyer_user_id = p_requesting_user_id)
      AND t.ticket_status = 'ISSUED'
      AND f.status IN ('SCHEDULED', 'DELAYED')
      AND NOW() BETWEEN COALESCE(f.check_in_open_at, f.departure_time - INTERVAL '24 hours')
                    AND COALESCE(f.check_in_close_at, f.departure_time - INTERVAL '2 hours')
    RETURNING t.ticket_id, t.ticket_status, t.checked_in_at;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'check-in not allowed';
    END IF;

    INSERT INTO audit_logs (actor_user_id, action_type, target_table, target_id, metadata)
    VALUES (p_requesting_user_id, 'SELF_CHECK_IN', 'tickets', p_ticket_id, NULL);

    INSERT INTO notifications (user_id, ticket_id, notification_type, channel, notification_status, scheduled_at)
    VALUES (p_requesting_user_id, p_ticket_id, 'BOARDING_PASS', 'EMAIL', 'PENDING', NOW());
END;
$$;

CREATE OR REPLACE FUNCTION update_fare(
    p_admin_user_id BIGINT,
    p_flight_id BIGINT,
    p_new_base_price NUMERIC,
    p_seat_class VARCHAR DEFAULT NULL,
    p_seat_number INTEGER DEFAULT NULL
)
RETURNS TABLE (
    flight_seat_id BIGINT,
    seat_number INTEGER,
    seat_class VARCHAR,
    base_price NUMERIC
)
LANGUAGE plpgsql
AS $$
BEGIN
    PERFORM 1
    FROM users
    WHERE user_id = p_admin_user_id
      AND role = 'ADMIN'
      AND deleted_at IS NULL;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'admin not valid';
    END IF;

    RETURN QUERY
    UPDATE flight_seats fs
    SET base_price = p_new_base_price
    WHERE fs.flight_id = p_flight_id
      AND (p_seat_class IS NULL OR fs.seat_class = p_seat_class)
      AND (p_seat_number IS NULL OR fs.seat_number = p_seat_number)
      AND fs.seat_status IN ('AVAILABLE', 'HELD')
    RETURNING fs.flight_seat_id, fs.seat_number, fs.seat_class, fs.base_price;

    INSERT INTO audit_logs (actor_user_id, action_type, target_table, target_id, metadata)
    VALUES (p_admin_user_id, 'FARE_UPDATE', 'flights', p_flight_id, JSONB_BUILD_OBJECT('seat_class', p_seat_class, 'seat_number', p_seat_number, 'new_base_price', p_new_base_price));
END;
$$;

CREATE OR REPLACE FUNCTION create_flight_path(
    p_admin_user_id BIGINT,
    p_departure_airport_id BIGINT,
    p_destination_airport_id BIGINT,
    p_duration_minutes INTEGER,
    p_distance_km NUMERIC
)
RETURNS BIGINT
LANGUAGE plpgsql
AS $$
DECLARE
    v_flight_path_id BIGINT;
BEGIN
    PERFORM 1
    FROM users
    WHERE user_id = p_admin_user_id
      AND role = 'ADMIN'
      AND deleted_at IS NULL;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'admin not valid';
    END IF;

    INSERT INTO flight_paths (departure_airport_id, destination_airport_id, duration_minutes, distance_km)
    VALUES (p_departure_airport_id, p_destination_airport_id, p_duration_minutes, p_distance_km)
    RETURNING flight_path_id INTO v_flight_path_id;

    INSERT INTO audit_logs (actor_user_id, action_type, target_table, target_id, metadata)
    VALUES (p_admin_user_id, 'FLIGHT_PATH_CREATE', 'flight_paths', v_flight_path_id, NULL);

    RETURN v_flight_path_id;
END;
$$;

CREATE OR REPLACE FUNCTION update_flight_path(
    p_admin_user_id BIGINT,
    p_flight_path_id BIGINT,
    p_duration_minutes INTEGER,
    p_distance_km NUMERIC
)
RETURNS BIGINT
LANGUAGE plpgsql
AS $$
BEGIN
    PERFORM 1
    FROM users
    WHERE user_id = p_admin_user_id
      AND role = 'ADMIN'
      AND deleted_at IS NULL;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'admin not valid';
    END IF;

    UPDATE flight_paths
    SET duration_minutes = p_duration_minutes,
        distance_km = p_distance_km
    WHERE flight_path_id = p_flight_path_id;

    INSERT INTO audit_logs (actor_user_id, action_type, target_table, target_id, metadata)
    VALUES (p_admin_user_id, 'FLIGHT_PATH_UPDATE', 'flight_paths', p_flight_path_id, NULL);

    RETURN p_flight_path_id;
END;
$$;

CREATE OR REPLACE FUNCTION create_flight_with_seats(
    p_admin_user_id BIGINT,
    p_plane_id BIGINT,
    p_flight_path_id BIGINT,
    p_departure_time TIMESTAMPTZ,
    p_economy_price NUMERIC,
    p_business_price NUMERIC DEFAULT NULL,
    p_first_price NUMERIC DEFAULT NULL,
    p_business_from_seat INTEGER DEFAULT NULL,
    p_first_from_seat INTEGER DEFAULT NULL
)
RETURNS BIGINT
LANGUAGE plpgsql
AS $$
DECLARE
    v_flight_id BIGINT;
    v_capacity INTEGER;
    v_seat_number INTEGER;
    v_seat_class VARCHAR(20);
    v_price NUMERIC(10, 2);
BEGIN
    PERFORM 1
    FROM users
    WHERE user_id = p_admin_user_id
      AND role = 'ADMIN'
      AND deleted_at IS NULL;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'admin not valid';
    END IF;

    SELECT seat_capacity INTO v_capacity
    FROM planes
    WHERE plane_id = p_plane_id;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'plane not found';
    END IF;

    INSERT INTO flights (plane_id, flight_path_id, departure_time, status)
    VALUES (p_plane_id, p_flight_path_id, p_departure_time, 'SCHEDULED')
    RETURNING flight_id INTO v_flight_id;

    FOR v_seat_number IN 1..v_capacity LOOP
        IF p_first_from_seat IS NOT NULL AND v_seat_number >= p_first_from_seat THEN
            v_seat_class = 'FIRST';
            v_price = COALESCE(p_first_price, p_economy_price);
        ELSIF p_business_from_seat IS NOT NULL AND v_seat_number >= p_business_from_seat THEN
            v_seat_class = 'BUSINESS';
            v_price = COALESCE(p_business_price, p_economy_price);
        ELSE
            v_seat_class = 'ECONOMY';
            v_price = p_economy_price;
        END IF;

        INSERT INTO flight_seats (flight_id, seat_number, seat_class, base_price, seat_status)
        VALUES (v_flight_id, v_seat_number, v_seat_class, v_price, 'AVAILABLE');
    END LOOP;

    INSERT INTO audit_logs (actor_user_id, action_type, target_table, target_id, metadata)
    VALUES (p_admin_user_id, 'FLIGHT_CREATE', 'flights', v_flight_id, JSONB_BUILD_OBJECT('plane_id', p_plane_id, 'flight_path_id', p_flight_path_id));

    RETURN v_flight_id;
END;
$$;

CREATE OR REPLACE FUNCTION delay_flight(
    p_admin_user_id BIGINT,
    p_flight_id BIGINT,
    p_new_departure_time TIMESTAMPTZ
)
RETURNS BIGINT
LANGUAGE plpgsql
AS $$
BEGIN
    PERFORM 1
    FROM users
    WHERE user_id = p_admin_user_id
      AND role = 'ADMIN'
      AND deleted_at IS NULL;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'admin not valid';
    END IF;

    UPDATE flights
    SET departure_time = p_new_departure_time,
        status = 'DELAYED'
    WHERE flight_id = p_flight_id;

    INSERT INTO notifications (user_id, ticket_id, notification_type, channel, notification_status, scheduled_at)
    SELECT COALESCE(ps.user_id, p.buyer_user_id), t.ticket_id, 'FLIGHT_DELAY', 'EMAIL', 'PENDING', NOW()
    FROM tickets t
    JOIN purchases p ON p.purchase_id = t.purchase_id
    JOIN passengers ps ON ps.passenger_id = t.passenger_id
    JOIN flight_seats fs ON fs.flight_seat_id = t.flight_seat_id
    WHERE fs.flight_id = p_flight_id
      AND t.ticket_status IN ('ISSUED', 'CHECKED_IN');

    INSERT INTO audit_logs (actor_user_id, action_type, target_table, target_id, metadata)
    VALUES (p_admin_user_id, 'FLIGHT_DELAY', 'flights', p_flight_id, JSONB_BUILD_OBJECT('new_departure_time', p_new_departure_time));

    RETURN p_flight_id;
END;
$$;

CREATE OR REPLACE FUNCTION cancel_flight(
    p_admin_user_id BIGINT,
    p_flight_id BIGINT
)
RETURNS BIGINT
LANGUAGE plpgsql
AS $$
BEGIN
    PERFORM 1
    FROM users
    WHERE user_id = p_admin_user_id
      AND role = 'ADMIN'
      AND deleted_at IS NULL;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'admin not valid';
    END IF;

    UPDATE flights
    SET status = 'CANCELLED'
    WHERE flight_id = p_flight_id;

    UPDATE flight_seats
    SET seat_status = 'BLOCKED'
    WHERE flight_id = p_flight_id
      AND seat_status IN ('AVAILABLE', 'HELD');

    INSERT INTO notifications (user_id, ticket_id, notification_type, channel, notification_status, scheduled_at)
    SELECT COALESCE(ps.user_id, p.buyer_user_id), t.ticket_id, 'FLIGHT_CANCELLED', 'EMAIL', 'PENDING', NOW()
    FROM tickets t
    JOIN purchases p ON p.purchase_id = t.purchase_id
    JOIN passengers ps ON ps.passenger_id = t.passenger_id
    JOIN flight_seats fs ON fs.flight_seat_id = t.flight_seat_id
    WHERE fs.flight_id = p_flight_id
      AND t.ticket_status IN ('ISSUED', 'CHECKED_IN');

    INSERT INTO audit_logs (actor_user_id, action_type, target_table, target_id, metadata)
    VALUES (p_admin_user_id, 'FLIGHT_CANCEL', 'flights', p_flight_id, NULL);

    RETURN p_flight_id;
END;
$$;

CREATE OR REPLACE FUNCTION get_timetable(
    p_window_hours INTEGER DEFAULT 24,
    p_departure_airport_id BIGINT DEFAULT NULL
)
RETURNS TABLE (
    flight_id BIGINT,
    departure_time TIMESTAMPTZ,
    status VARCHAR,
    departure_airport VARCHAR,
    destination_airport VARCHAR,
    airline_name VARCHAR,
    plane_id BIGINT
)
LANGUAGE sql
AS $$
    SELECT
        f.flight_id,
        f.departure_time,
        f.status,
        dep.airport_name,
        dst.airport_name,
        al.airline_name,
        pl.plane_id
    FROM flights f
    JOIN flight_paths fp ON fp.flight_path_id = f.flight_path_id
    JOIN airports dep ON dep.airport_id = fp.departure_airport_id
    JOIN airports dst ON dst.airport_id = fp.destination_airport_id
    JOIN planes pl ON pl.plane_id = f.plane_id
    JOIN airlines al ON al.airline_id = pl.airline_id
    WHERE f.departure_time >= NOW()
      AND f.departure_time < NOW() + (p_window_hours || ' hours')::INTERVAL
      AND (p_departure_airport_id IS NULL OR fp.departure_airport_id = p_departure_airport_id)
      AND fp.is_active = TRUE
    ORDER BY f.departure_time ASC
$$;

CREATE OR REPLACE FUNCTION get_popular_destinations(
    p_from_date TIMESTAMPTZ,
    p_to_date TIMESTAMPTZ,
    p_airline_id BIGINT DEFAULT NULL,
    p_limit_count INTEGER DEFAULT 10
)
RETURNS TABLE (
    airport_id BIGINT,
    airport_name VARCHAR,
    nation VARCHAR,
    tickets_sold BIGINT,
    revenue NUMERIC
)
LANGUAGE sql
AS $$
    SELECT
        dst.airport_id,
        dst.airport_name,
        dst.nation,
        COUNT(t.ticket_id),
        SUM(t.ticket_price)
    FROM tickets t
    JOIN purchases p ON p.purchase_id = t.purchase_id
    JOIN flight_seats fs ON fs.flight_seat_id = t.flight_seat_id
    JOIN flights f ON f.flight_id = fs.flight_id
    JOIN flight_paths fp ON fp.flight_path_id = f.flight_path_id
    JOIN airports dst ON dst.airport_id = fp.destination_airport_id
    JOIN planes pl ON pl.plane_id = f.plane_id
    WHERE t.ticket_status IN ('ISSUED', 'CHECKED_IN')
      AND p.purchase_status = 'PAID'
      AND p.purchase_date >= p_from_date
      AND p.purchase_date < p_to_date
      AND (p_airline_id IS NULL OR pl.airline_id = p_airline_id)
    GROUP BY dst.airport_id, dst.airport_name, dst.nation
    ORDER BY COUNT(t.ticket_id) DESC, SUM(t.ticket_price) DESC
    LIMIT p_limit_count
$$;

CREATE OR REPLACE FUNCTION get_popular_months(
    p_from_date TIMESTAMPTZ,
    p_to_date TIMESTAMPTZ
)
RETURNS TABLE (
    travel_month TIMESTAMPTZ,
    tickets_sold BIGINT,
    revenue NUMERIC
)
LANGUAGE sql
AS $$
    SELECT
        DATE_TRUNC('month', f.departure_time),
        COUNT(t.ticket_id),
        SUM(t.ticket_price)
    FROM tickets t
    JOIN purchases p ON p.purchase_id = t.purchase_id
    JOIN flight_seats fs ON fs.flight_seat_id = t.flight_seat_id
    JOIN flights f ON f.flight_id = fs.flight_id
    WHERE t.ticket_status IN ('ISSUED', 'CHECKED_IN')
      AND p.purchase_status = 'PAID'
      AND p.purchase_date >= p_from_date
      AND p.purchase_date < p_to_date
    GROUP BY DATE_TRUNC('month', f.departure_time)
    ORDER BY COUNT(t.ticket_id) DESC, SUM(t.ticket_price) DESC
$$;

DROP FUNCTION IF EXISTS create_person_account(VARCHAR, VARCHAR, VARCHAR, VARCHAR, DATE);

CREATE OR REPLACE FUNCTION create_person_account(
    p_email VARCHAR,
    p_password_hash VARCHAR
)
RETURNS BIGINT
LANGUAGE plpgsql
AS $$
DECLARE
    v_user_id BIGINT;
BEGIN
    INSERT INTO users (email, password_hash, role)
    VALUES (p_email, p_password_hash, 'PERSON')
    RETURNING user_id INTO v_user_id;

    INSERT INTO person_profiles (user_id)
    VALUES (v_user_id);

    INSERT INTO notification_preferences (user_id)
    VALUES (v_user_id);

    INSERT INTO audit_logs (actor_user_id, action_type, target_table, target_id, metadata)
    VALUES (v_user_id, 'REGISTRATION', 'users', v_user_id, JSONB_BUILD_OBJECT('role', 'PERSON'));

    RETURN v_user_id;
END;
$$;

CREATE OR REPLACE FUNCTION create_travel_agency_account(
    p_email VARCHAR,
    p_password_hash VARCHAR,
    p_company_name VARCHAR,
    p_company_email VARCHAR DEFAULT NULL,
    p_tax_id VARCHAR DEFAULT NULL
)
RETURNS BIGINT
LANGUAGE plpgsql
AS $$
DECLARE
    v_user_id BIGINT;
BEGIN
    INSERT INTO users (email, password_hash, role)
    VALUES (p_email, p_password_hash, 'TRAVEL_AGENCY')
    RETURNING user_id INTO v_user_id;

    INSERT INTO travel_agency_profiles (user_id, company_name, company_email, tax_id)
    VALUES (v_user_id, p_company_name, p_company_email, p_tax_id);

    INSERT INTO notification_preferences (user_id)
    VALUES (v_user_id);

    INSERT INTO audit_logs (actor_user_id, action_type, target_table, target_id, metadata)
    VALUES (v_user_id, 'REGISTRATION', 'users', v_user_id, JSONB_BUILD_OBJECT('role', 'TRAVEL_AGENCY'));

    RETURN v_user_id;
END;
$$;

CREATE OR REPLACE FUNCTION create_admin_account(
    p_actor_user_id BIGINT,
    p_email VARCHAR,
    p_password_hash VARCHAR,
    p_admin_name VARCHAR
)
RETURNS BIGINT
LANGUAGE plpgsql
AS $$
DECLARE
    v_user_id BIGINT;
BEGIN
    IF p_actor_user_id IS NOT NULL THEN
        PERFORM 1
        FROM users
        WHERE user_id = p_actor_user_id
          AND role = 'ADMIN'
          AND deleted_at IS NULL;

        IF NOT FOUND THEN
            RAISE EXCEPTION 'admin not valid';
        END IF;
    END IF;

    INSERT INTO users (email, password_hash, role)
    VALUES (p_email, p_password_hash, 'ADMIN')
    RETURNING user_id INTO v_user_id;

    INSERT INTO admin_profiles (user_id, admin_name)
    VALUES (v_user_id, p_admin_name);

    INSERT INTO audit_logs (actor_user_id, action_type, target_table, target_id, metadata)
    VALUES (p_actor_user_id, 'ADMIN_CREATE', 'users', v_user_id, NULL);

    RETURN v_user_id;
END;
$$;

CREATE OR REPLACE FUNCTION create_developer_account(
    p_actor_user_id BIGINT,
    p_email VARCHAR,
    p_password_hash VARCHAR,
    p_developer_name VARCHAR
)
RETURNS BIGINT
LANGUAGE plpgsql
AS $$
DECLARE
    v_user_id BIGINT;
BEGIN
    IF p_actor_user_id IS NOT NULL THEN
        PERFORM 1
        FROM users
        WHERE user_id = p_actor_user_id
          AND role = 'DEVELOPER'
          AND deleted_at IS NULL;

        IF NOT FOUND THEN
            RAISE EXCEPTION 'developer not valid';
        END IF;
    END IF;

    INSERT INTO users (email, password_hash, role)
    VALUES (p_email, p_password_hash, 'DEVELOPER')
    RETURNING user_id INTO v_user_id;

    INSERT INTO developer_profiles (user_id, developer_name)
    VALUES (v_user_id, p_developer_name);

    INSERT INTO notification_preferences (user_id)
    VALUES (v_user_id);

    INSERT INTO audit_logs (actor_user_id, action_type, target_table, target_id, metadata)
    VALUES (p_actor_user_id, 'DEVELOPER_CREATE', 'users', v_user_id, NULL);

    RETURN v_user_id;
END;
$$;

CREATE OR REPLACE FUNCTION update_person_profile(
    p_user_id BIGINT,
    p_name VARCHAR,
    p_surname VARCHAR,
    p_date_of_birth DATE DEFAULT NULL
)
RETURNS BIGINT
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE person_profiles
    SET name = p_name,
        surname = p_surname,
        date_of_birth = p_date_of_birth
    WHERE user_id = p_user_id;

    INSERT INTO audit_logs (actor_user_id, action_type, target_table, target_id, metadata)
    VALUES (p_user_id, 'PROFILE_UPDATE', 'person_profiles', p_user_id, NULL);

    RETURN p_user_id;
END;
$$;

CREATE OR REPLACE FUNCTION update_travel_agency_profile(
    p_user_id BIGINT,
    p_company_name VARCHAR,
    p_company_email VARCHAR DEFAULT NULL,
    p_tax_id VARCHAR DEFAULT NULL
)
RETURNS BIGINT
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE travel_agency_profiles
    SET company_name = p_company_name,
        company_email = p_company_email,
        tax_id = p_tax_id
    WHERE user_id = p_user_id;

    INSERT INTO audit_logs (actor_user_id, action_type, target_table, target_id, metadata)
    VALUES (p_user_id, 'PROFILE_UPDATE', 'travel_agency_profiles', p_user_id, NULL);

    RETURN p_user_id;
END;
$$;

CREATE OR REPLACE FUNCTION soft_delete_account(
    p_user_id BIGINT
)
RETURNS BIGINT
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE users
    SET deleted_at = NOW()
    WHERE user_id = p_user_id
      AND deleted_at IS NULL;

    UPDATE sessions
    SET terminated_at = NOW()
    WHERE user_id = p_user_id
      AND terminated_at IS NULL;

    INSERT INTO audit_logs (actor_user_id, action_type, target_table, target_id, metadata)
    VALUES (p_user_id, 'ACCOUNT_DELETE', 'users', p_user_id, NULL);

    RETURN p_user_id;
END;
$$;
CREATE OR REPLACE FUNCTION change_password(
    p_user_id           BIGINT,
    p_new_password_hash VARCHAR
)
RETURNS VOID
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE users
    SET password_hash = p_new_password_hash
    WHERE user_id = p_user_id
      AND deleted_at IS NULL;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'user not found';
    END IF;

    UPDATE sessions
    SET terminated_at = NOW()
    WHERE user_id = p_user_id
      AND terminated_at IS NULL;

    INSERT INTO audit_logs (actor_user_id, action_type, target_table, target_id, metadata)
    VALUES (p_user_id, 'PASSWORD_CHANGE', 'users', p_user_id, NULL);
END;
$$;
CREATE OR REPLACE FUNCTION change_email(
    p_user_id   BIGINT,
    p_new_email VARCHAR
)
RETURNS VOID
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE users
    SET email = p_new_email
    WHERE user_id = p_user_id
      AND deleted_at IS NULL;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'user not found';
    END IF;

    INSERT INTO audit_logs (actor_user_id, action_type, target_table, target_id, metadata)
    VALUES (p_user_id, 'EMAIL_CHANGE', 'users', p_user_id, NULL);
END;
$$;
CREATE OR REPLACE FUNCTION create_password_reset_token(
    p_email VARCHAR,
    p_token_hash VARCHAR,
    p_expires_at TIMESTAMPTZ
)
RETURNS BIGINT
LANGUAGE plpgsql
AS $$
DECLARE
    v_user_id BIGINT;
    v_reset_token_id BIGINT;
BEGIN
    SELECT user_id INTO v_user_id
    FROM users
    WHERE email = p_email
      AND deleted_at IS NULL;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'user not found';
    END IF;

    INSERT INTO password_reset_tokens (user_id, token_hash, expires_at)
    VALUES (v_user_id, p_token_hash, p_expires_at)
    RETURNING reset_token_id INTO v_reset_token_id;

    INSERT INTO audit_logs (actor_user_id, action_type, target_table, target_id, metadata)
    VALUES (v_user_id, 'ACCOUNT_RECOVERY_REQUEST', 'password_reset_tokens', v_reset_token_id, NULL);

    RETURN v_reset_token_id;
END;
$$;

CREATE OR REPLACE FUNCTION consume_password_reset_token(
    p_token_hash VARCHAR,
    p_new_password_hash VARCHAR
)
RETURNS BIGINT
LANGUAGE plpgsql
AS $$
DECLARE
    v_user_id BIGINT;
    v_reset_token_id BIGINT;
BEGIN
    SELECT reset_token_id, user_id INTO v_reset_token_id, v_user_id
    FROM password_reset_tokens
    WHERE token_hash = p_token_hash
      AND used_at IS NULL
      AND expires_at > NOW()
    FOR UPDATE;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'reset token not valid';
    END IF;

    UPDATE users
    SET password_hash = p_new_password_hash
    WHERE user_id = v_user_id;

    UPDATE password_reset_tokens
    SET used_at = NOW()
    WHERE reset_token_id = v_reset_token_id;

    UPDATE sessions
    SET terminated_at = NOW()
    WHERE user_id = v_user_id
      AND terminated_at IS NULL;

    INSERT INTO audit_logs (actor_user_id, action_type, target_table, target_id, metadata)
    VALUES (v_user_id, 'PASSWORD_RESET', 'users', v_user_id, JSONB_BUILD_OBJECT('reset_token_id', v_reset_token_id));

    RETURN v_user_id;
END;
$$;

CREATE OR REPLACE FUNCTION update_notification_preferences(
    p_user_id BIGINT,
    p_invoice_enabled BOOLEAN,
    p_check_in_enabled BOOLEAN,
    p_delay_enabled BOOLEAN,
    p_new_device_enabled BOOLEAN
)
RETURNS BIGINT
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO notification_preferences (
        user_id,
        invoice_enabled,
        check_in_enabled,
        delay_enabled,
        new_device_enabled
    )
    VALUES (
        p_user_id,
        p_invoice_enabled,
        p_check_in_enabled,
        p_delay_enabled,
        p_new_device_enabled
    )
    ON CONFLICT (user_id) DO UPDATE
    SET invoice_enabled = EXCLUDED.invoice_enabled,
        check_in_enabled = EXCLUDED.check_in_enabled,
        delay_enabled = EXCLUDED.delay_enabled,
        new_device_enabled = EXCLUDED.new_device_enabled;

    INSERT INTO audit_logs (actor_user_id, action_type, target_table, target_id, metadata)
    VALUES (p_user_id, 'NOTIFICATION_PREFERENCES_UPDATE', 'notification_preferences', p_user_id, NULL);

    RETURN p_user_id;
END;
$$;

CREATE OR REPLACE FUNCTION cancel_ticket(
    p_requesting_user_id BIGINT,
    p_ticket_id BIGINT
)
RETURNS BIGINT
LANGUAGE plpgsql
AS $$
DECLARE
    v_flight_seat_id BIGINT;
    v_purchase_id BIGINT;
    v_total_tickets INTEGER;
    v_cancelled_tickets INTEGER;
BEGIN
    SELECT t.flight_seat_id, t.purchase_id INTO v_flight_seat_id, v_purchase_id
    FROM tickets t
    JOIN purchases p ON p.purchase_id = t.purchase_id
    JOIN passengers ps ON ps.passenger_id = t.passenger_id
    JOIN flight_seats fs ON fs.flight_seat_id = t.flight_seat_id
    JOIN flights f ON f.flight_id = fs.flight_id
    WHERE t.ticket_id = p_ticket_id
      AND t.ticket_status = 'ISSUED'
      AND f.departure_time > NOW()
      AND (p.buyer_user_id = p_requesting_user_id OR ps.user_id = p_requesting_user_id)
    FOR UPDATE OF t;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'ticket cancellation not allowed';
    END IF;

    UPDATE tickets
    SET ticket_status = 'CANCELLED'
    WHERE ticket_id = p_ticket_id;

    UPDATE flight_seats
    SET seat_status = 'AVAILABLE'
    WHERE flight_seat_id = v_flight_seat_id;

    SELECT COUNT(*), COUNT(*) FILTER (WHERE ticket_status = 'CANCELLED')
    INTO v_total_tickets, v_cancelled_tickets
    FROM tickets
    WHERE purchase_id = v_purchase_id;

    IF v_total_tickets = v_cancelled_tickets THEN
        UPDATE purchases
        SET purchase_status = 'CANCELLED'
        WHERE purchase_id = v_purchase_id;
    END IF;

    INSERT INTO notifications (user_id, ticket_id, notification_type, channel, notification_status, scheduled_at)
    VALUES (p_requesting_user_id, p_ticket_id, 'TICKET_CANCELLED', 'EMAIL', 'PENDING', NOW());

    INSERT INTO audit_logs (actor_user_id, action_type, target_table, target_id, metadata)
    VALUES (p_requesting_user_id, 'TICKET_CANCEL', 'tickets', p_ticket_id, JSONB_BUILD_OBJECT('purchase_id', v_purchase_id));

    RETURN p_ticket_id;
END;
$$;

CREATE OR REPLACE FUNCTION create_session(
    p_email VARCHAR,
    p_token_hash VARCHAR,
    p_expires_at TIMESTAMPTZ,
    p_new_device BOOLEAN DEFAULT FALSE
)
RETURNS TABLE (
    session_id BIGINT,
    user_id BIGINT,
    user_role VARCHAR
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_user_id BIGINT;
    v_role VARCHAR(20);
    v_session_id BIGINT;
BEGIN
    SELECT u.user_id, u.role INTO v_user_id, v_role
    FROM users u
    WHERE u.email = p_email
      AND u.deleted_at IS NULL;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'user not found';
    END IF;

    INSERT INTO sessions (user_id, token_hash, expires_at)
    VALUES (v_user_id, p_token_hash, p_expires_at)
    RETURNING sessions.session_id INTO v_session_id;

    INSERT INTO audit_logs (actor_user_id, action_type, target_table, target_id, metadata)
    VALUES (v_user_id, 'LOGIN', 'sessions', v_session_id, JSONB_BUILD_OBJECT('new_device', p_new_device));

    IF p_new_device THEN
        INSERT INTO notifications (user_id, notification_type, channel, notification_status, scheduled_at)
        VALUES (v_user_id, 'NEW_DEVICE_LOGIN', 'EMAIL', 'PENDING', NOW());
    END IF;

    RETURN QUERY SELECT v_session_id, v_user_id, v_role;
END;
$$;

CREATE OR REPLACE FUNCTION create_backup_run(
    p_actor_user_id BIGINT,
    p_storage_uri TEXT,
    p_backup_status VARCHAR DEFAULT 'COMPLETED'
)
RETURNS BIGINT
LANGUAGE plpgsql
AS $$
DECLARE
    v_backup_run_id BIGINT;
BEGIN
    PERFORM 1
    FROM users
    WHERE user_id = p_actor_user_id
      AND role = 'DEVELOPER'
      AND deleted_at IS NULL;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'developer not valid';
    END IF;

    INSERT INTO backup_runs (actor_user_id, backup_status, storage_uri, completed_at)
    VALUES (p_actor_user_id, p_backup_status, p_storage_uri, CASE WHEN p_backup_status = 'COMPLETED' THEN NOW() ELSE NULL END)
    RETURNING backup_run_id INTO v_backup_run_id;

    INSERT INTO audit_logs (actor_user_id, action_type, target_table, target_id, metadata)
    VALUES (p_actor_user_id, 'BACKUP_CREATE', 'backup_runs', v_backup_run_id, NULL);

    RETURN v_backup_run_id;
END;
$$;

CREATE OR REPLACE FUNCTION get_airports(
    p_nation VARCHAR DEFAULT NULL
)
RETURNS TABLE (
    airport_id BIGINT,
    airport_name VARCHAR,
    nation VARCHAR
)
LANGUAGE sql
AS $$
    SELECT airport_id, airport_name, nation
    FROM airports
    WHERE (p_nation IS NULL OR nation = p_nation)
    ORDER BY nation, airport_name;
$$;
CREATE OR REPLACE FUNCTION check_credentials(
    p_email         VARCHAR,
    p_password_hash VARCHAR
)
RETURNS BIGINT
LANGUAGE plpgsql
AS $$
DECLARE
    v_user_id BIGINT;
BEGIN
    SELECT user_id
    INTO v_user_id
    FROM users
    WHERE email = p_email
      AND password_hash = p_password_hash
      AND deleted_at IS NULL;
    RETURN v_user_id; -- returns NULL if not found
END;
$$;
