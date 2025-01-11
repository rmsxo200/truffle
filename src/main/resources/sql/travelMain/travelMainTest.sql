insert into travel_main
    (create_dttm, create_user_id, start_date, end_date, travel_title, travel_seq)
values
    (NOW(), 'user1', '2024-11-01', '2024-11-02', 'test1', 1);

insert into travel_main
    (create_dttm, create_user_id, start_date, end_date, travel_title, travel_seq)
values
    (NOW(), 'user2', '2024-11-01', '2024-11-02', 'test2', 2);

insert into travel_main
    (create_dttm, create_user_id, start_date, end_date, travel_title, travel_seq)
values
    (NOW(), 'user3', '2024-11-01', '2024-11-02', 'test3', 3);

insert into destination
    (destination_name, destination_cd)
values
    ('테스트구', '12345');

insert into destination
    (destination_name, destination_cd)
values
    ('테스트시', '99999');

insert into destination_mapping
    (travel_seq, destination_cd)
values
    (1, '12345');