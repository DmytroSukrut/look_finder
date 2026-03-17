alter table user_information
    rename column favourite to favourite1;

alter table user_information
    add column favourite2 vector(384),
    add column favourite3 vector(384),
    add column favourite4 vector(384),
    add column favourite5 vector(384);