INSERT INTO
    gearit_account_info(firstname, middlename, lastname, phone_number, gender, birth_date)
VALUES (
        'Admin',
        'Admin',
        'Admin',
        '+7-(951)-241-94-31',
        'MALE',
        '1975-01-01 12:00:00.000000'
       )
ON CONFLICT DO NOTHING;

INSERT INTO
    gearit_user_provider(password, provider, default_email, is_confirmed, account_id, access_policy_id)
VALUES (
        '$2a$10$Sm84Sv55ksAup6AIH/zoW.WGX4VxrkoSKjncJTgcaqxqAg60d/fQq',
        'INTERNAL',
        'admin@mail.ru',
        TRUE,
        1,
1
       )
ON CONFLICT DO NOTHING
