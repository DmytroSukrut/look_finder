create table if not exists user_information (
    user_id bigint primary key,
    favourite vector(384),
    bust text,
    waist text,
    hip text
)