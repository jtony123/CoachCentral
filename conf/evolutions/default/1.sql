# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table answer (
  id                            bigint auto_increment not null,
  question_id                   bigint,
  questionnaire_id              bigint,
  answer                        varchar(255),
  answer_value                  integer,
  raise_flag                    tinyint(1) default 0,
  constraint pk_answer primary key (id)
);

create table category (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  constraint pk_category primary key (id)
);

create table name_alias (
  id                            bigint auto_increment not null,
  alias                         varchar(255),
  player_id                     bigint,
  constraint pk_name_alias primary key (id)
);

create table player (
  id                            bigint auto_increment not null,
  playername                    varchar(255),
  height                        varchar(255),
  weight                        varchar(255),
  dob                           varchar(255),
  position                      varchar(255),
  playernumber                  integer,
  dateadded                     datetime(6),
  player_photofilename          varchar(255),
  player_photo                  longblob,
  file                          varbinary(255),
  filename                      varchar(255),
  redox_filename                varchar(255),
  constraint pk_player primary key (id)
);

create table player_user (
  player_id                     bigint not null,
  user_id                       bigint not null,
  constraint pk_player_user primary key (player_id,user_id)
);

create table player_category (
  player_id                     bigint not null,
  category_id                   bigint not null,
  constraint pk_player_category primary key (player_id,category_id)
);

create table player_question (
  player_id                     bigint not null,
  question_id                   bigint not null,
  constraint pk_player_question primary key (player_id,question_id)
);

create table question (
  id                            bigint auto_increment not null,
  question                      varchar(255),
  flag_threshold                integer,
  constraint pk_question primary key (id)
);

create table question_question_category (
  question_id                   bigint not null,
  question_category_id          bigint not null,
  constraint pk_question_question_category primary key (question_id,question_category_id)
);

create table question_category (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  constraint pk_question_category primary key (id)
);

create table questionnaire (
  id                            bigint auto_increment not null,
  player_id                     bigint,
  datetime                      datetime(6),
  notes                         varchar(255),
  injurylog                     varchar(255),
  constraint pk_questionnaire primary key (id)
);

create table rdx_alert_report (
  id                            bigint auto_increment not null,
  stress_status                 varchar(255),
  defence_status                varchar(255),
  result                        varchar(255),
  potentialoutcomes             varchar(255),
  actions                       varchar(255),
  trainingloadadvice            varchar(1024),
  dietaryadvice                 varchar(1024),
  sleepadvice                   varchar(1024),
  constraint pk_rdx_alert_report primary key (id)
);

create table redox (
  id                            bigint auto_increment not null,
  player_id                     bigint,
  rdxalertreport_id             bigint,
  date                          datetime(6),
  eaten                         varchar(255),
  exercised                     varchar(255),
  fever                         varchar(255),
  sorethroat                    varchar(255),
  headache                      varchar(255),
  jointmuscleache               varchar(255),
  diarrhea                      varchar(255),
  exercises                     varchar(255),
  gymweights                    varchar(255),
  practicetraining              varchar(255),
  game                          varchar(255),
  rest                          varchar(255),
  other                         varchar(255),
  illness                       varchar(255),
  injured                       varchar(255),
  additional_notes              varchar(255),
  energy                        double,
  muscle_soreness               double,
  sportscientist_comment        varchar(255),
  orreco_scientist              varchar(255),
  stress                        double,
  defence                       double,
  include_in_crit_diff          tinyint(1) default 0,
  stress_threshold              double,
  defence_threshold             double,
  stress_status                 varchar(255),
  defence_status                varchar(255),
  constraint pk_redox primary key (id)
);

create table security_role (
  id                            bigint auto_increment not null,
  role_name                     varchar(255),
  constraint pk_security_role primary key (id)
);

create table user (
  id                            bigint auto_increment not null,
  email                         varchar(255),
  user_name                     varchar(255),
  password                      varchar(255),
  constraint pk_user primary key (id)
);

create table user_security_role (
  user_id                       bigint not null,
  security_role_id              bigint not null,
  constraint pk_user_security_role primary key (user_id,security_role_id)
);

create table user_user_permission (
  user_id                       bigint not null,
  user_permission_id            bigint not null,
  constraint pk_user_user_permission primary key (user_id,user_permission_id)
);

create table user_permission (
  id                            bigint auto_increment not null,
  permission_value              varchar(255),
  constraint pk_user_permission primary key (id)
);

alter table answer add constraint fk_answer_question_id foreign key (question_id) references question (id) on delete restrict on update restrict;
create index ix_answer_question_id on answer (question_id);

alter table answer add constraint fk_answer_questionnaire_id foreign key (questionnaire_id) references questionnaire (id) on delete restrict on update restrict;
create index ix_answer_questionnaire_id on answer (questionnaire_id);

alter table name_alias add constraint fk_name_alias_player_id foreign key (player_id) references player (id) on delete restrict on update restrict;
create index ix_name_alias_player_id on name_alias (player_id);

alter table player_user add constraint fk_player_user_player foreign key (player_id) references player (id) on delete restrict on update restrict;
create index ix_player_user_player on player_user (player_id);

alter table player_user add constraint fk_player_user_user foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_player_user_user on player_user (user_id);

alter table player_category add constraint fk_player_category_player foreign key (player_id) references player (id) on delete restrict on update restrict;
create index ix_player_category_player on player_category (player_id);

alter table player_category add constraint fk_player_category_category foreign key (category_id) references category (id) on delete restrict on update restrict;
create index ix_player_category_category on player_category (category_id);

alter table player_question add constraint fk_player_question_player foreign key (player_id) references player (id) on delete restrict on update restrict;
create index ix_player_question_player on player_question (player_id);

alter table player_question add constraint fk_player_question_question foreign key (question_id) references question (id) on delete restrict on update restrict;
create index ix_player_question_question on player_question (question_id);

alter table question_question_category add constraint fk_question_question_category_question foreign key (question_id) references question (id) on delete restrict on update restrict;
create index ix_question_question_category_question on question_question_category (question_id);

alter table question_question_category add constraint fk_question_question_category_question_category foreign key (question_category_id) references question_category (id) on delete restrict on update restrict;
create index ix_question_question_category_question_category on question_question_category (question_category_id);

alter table questionnaire add constraint fk_questionnaire_player_id foreign key (player_id) references player (id) on delete restrict on update restrict;
create index ix_questionnaire_player_id on questionnaire (player_id);

alter table redox add constraint fk_redox_player_id foreign key (player_id) references player (id) on delete restrict on update restrict;
create index ix_redox_player_id on redox (player_id);

alter table redox add constraint fk_redox_rdxalertreport_id foreign key (rdxalertreport_id) references rdx_alert_report (id) on delete restrict on update restrict;
create index ix_redox_rdxalertreport_id on redox (rdxalertreport_id);

alter table user_security_role add constraint fk_user_security_role_user foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_user_security_role_user on user_security_role (user_id);

alter table user_security_role add constraint fk_user_security_role_security_role foreign key (security_role_id) references security_role (id) on delete restrict on update restrict;
create index ix_user_security_role_security_role on user_security_role (security_role_id);

alter table user_user_permission add constraint fk_user_user_permission_user foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_user_user_permission_user on user_user_permission (user_id);

alter table user_user_permission add constraint fk_user_user_permission_user_permission foreign key (user_permission_id) references user_permission (id) on delete restrict on update restrict;
create index ix_user_user_permission_user_permission on user_user_permission (user_permission_id);


# --- !Downs

alter table answer drop foreign key fk_answer_question_id;
drop index ix_answer_question_id on answer;

alter table answer drop foreign key fk_answer_questionnaire_id;
drop index ix_answer_questionnaire_id on answer;

alter table name_alias drop foreign key fk_name_alias_player_id;
drop index ix_name_alias_player_id on name_alias;

alter table player_user drop foreign key fk_player_user_player;
drop index ix_player_user_player on player_user;

alter table player_user drop foreign key fk_player_user_user;
drop index ix_player_user_user on player_user;

alter table player_category drop foreign key fk_player_category_player;
drop index ix_player_category_player on player_category;

alter table player_category drop foreign key fk_player_category_category;
drop index ix_player_category_category on player_category;

alter table player_question drop foreign key fk_player_question_player;
drop index ix_player_question_player on player_question;

alter table player_question drop foreign key fk_player_question_question;
drop index ix_player_question_question on player_question;

alter table question_question_category drop foreign key fk_question_question_category_question;
drop index ix_question_question_category_question on question_question_category;

alter table question_question_category drop foreign key fk_question_question_category_question_category;
drop index ix_question_question_category_question_category on question_question_category;

alter table questionnaire drop foreign key fk_questionnaire_player_id;
drop index ix_questionnaire_player_id on questionnaire;

alter table redox drop foreign key fk_redox_player_id;
drop index ix_redox_player_id on redox;

alter table redox drop foreign key fk_redox_rdxalertreport_id;
drop index ix_redox_rdxalertreport_id on redox;

alter table user_security_role drop foreign key fk_user_security_role_user;
drop index ix_user_security_role_user on user_security_role;

alter table user_security_role drop foreign key fk_user_security_role_security_role;
drop index ix_user_security_role_security_role on user_security_role;

alter table user_user_permission drop foreign key fk_user_user_permission_user;
drop index ix_user_user_permission_user on user_user_permission;

alter table user_user_permission drop foreign key fk_user_user_permission_user_permission;
drop index ix_user_user_permission_user_permission on user_user_permission;

drop table if exists answer;

drop table if exists category;

drop table if exists name_alias;

drop table if exists player;

drop table if exists player_user;

drop table if exists player_category;

drop table if exists player_question;

drop table if exists question;

drop table if exists question_question_category;

drop table if exists question_category;

drop table if exists questionnaire;

drop table if exists rdx_alert_report;

drop table if exists redox;

drop table if exists security_role;

drop table if exists user;

drop table if exists user_security_role;

drop table if exists user_user_permission;

drop table if exists user_permission;

