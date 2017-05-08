CREATE TABLE user_course(
	user_id varchar(255),
	course_id varchar(255)
);

CREATE TABLE course(
	course_id varchar(255),
	course_name varchar(255),
	dept_name varchar(255),
	unique(course_id)
);

CREATE TABLE course_float(
	course_id varchar(255),
	prof_name varchar(255),
	course_term varchar(255)
);

CREATE TABLE users(
	user_id varchar(255),
	name varchar(255),
	year varchar(255),
	grade varchar(255),
	encrypted_password varchar(255),
	salt varchar(255),
	created_at varchar(255),
	unique(user_id)
);

CREATE TABLE admin(
	admin_id varchar(255),
	name varchar(255),
	password varchar(255)
);

CREATE TABLE course_tag(
	course_id varchar(255),
	course_tag varchar(255)
);

CREATE TABLE notes( course_id varchar(255), notes_id int AUTO_INCREMENT, notes_title varchar(255),unique(notes_id) );

CREATE TABLE notes_location(
	notes_id varchar(255),
	notes_obj_loc varchar(255)
);

CREATE TABLE notes_tag(
	notes_id varchar(255),
	notes_tag varchar(255)
);

CREATE TABLE notes_rating(
	course_id varchar(255),
	notes_id varchar(255),
	ratings int
);

CREATE TABLE course_rating(
	course_id varchar(255),
	course_tag varchar(255),
	numb_ratings int,
	ratings double
);


