
create table sync_origin
(
    id     int primary key auto_increment,
    type   varchar(30) not null default 'github',
    config text        not null default '{}',
    status tinyint              default 1 comment '1正常0禁止'
);

create table job_schedule
(
    id     int primary key auto_increment,
    syncId int         not null,
    rule   varchar(15) not null default '* * * 1 *',
);


create table job_log
(
    id   int primary key auto_increment,
    time timestamp default current_timestamp(),
    isSync tinyint default 1 comment '1正在同步 0 同步失败 2 同步完成',
    errLog text not null ,
    jobId int not null
);

