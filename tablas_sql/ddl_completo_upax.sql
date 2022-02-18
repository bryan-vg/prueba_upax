--
-- PostgreSQL database dump
--

-- Dumped from database version 13.5 (Debian 13.5-0+deb11u1)
-- Dumped by pg_dump version 13.5 (Debian 13.5-0+deb11u1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: databasechangelog; Type: TABLE; Schema: public; Owner: miuser
--

CREATE TABLE public.databasechangelog (
    id character varying(255) NOT NULL,
    author character varying(255) NOT NULL,
    filename character varying(255) NOT NULL,
    dateexecuted timestamp without time zone NOT NULL,
    orderexecuted integer NOT NULL,
    exectype character varying(10) NOT NULL,
    md5sum character varying(35),
    description character varying(255),
    comments character varying(255),
    tag character varying(255),
    liquibase character varying(20),
    contexts character varying(255),
    labels character varying(255),
    deployment_id character varying(10)
);


ALTER TABLE public.databasechangelog OWNER TO miuser;

--
-- Name: databasechangeloglock; Type: TABLE; Schema: public; Owner: miuser
--

CREATE TABLE public.databasechangeloglock (
    id integer NOT NULL,
    locked boolean NOT NULL,
    lockgranted timestamp without time zone,
    lockedby character varying(255)
);


ALTER TABLE public.databasechangeloglock OWNER TO miuser;

--
-- Name: employee_worked_hours; Type: TABLE; Schema: public; Owner: miuser
--

CREATE TABLE public.employee_worked_hours (
    id bigint NOT NULL,
    worked_hours integer,
    worked_date date,
    employee_id bigint
);


ALTER TABLE public.employee_worked_hours OWNER TO miuser;

--
-- Name: employees; Type: TABLE; Schema: public; Owner: miuser
--

CREATE TABLE public.employees (
    id bigint NOT NULL,
    name character varying(255),
    last_name character varying(255),
    birth_date date,
    job_id bigint,
    gender_id bigint
);


ALTER TABLE public.employees OWNER TO miuser;

--
-- Name: genders; Type: TABLE; Schema: public; Owner: miuser
--

CREATE TABLE public.genders (
    id bigint NOT NULL,
    name character varying(255)
);


ALTER TABLE public.genders OWNER TO miuser;

--
-- Name: jhi_authority; Type: TABLE; Schema: public; Owner: miuser
--

CREATE TABLE public.jhi_authority (
    name character varying(50) NOT NULL
);


ALTER TABLE public.jhi_authority OWNER TO miuser;

--
-- Name: jhi_user; Type: TABLE; Schema: public; Owner: miuser
--

CREATE TABLE public.jhi_user (
    id bigint NOT NULL,
    login character varying(50) NOT NULL,
    password_hash character varying(60) NOT NULL,
    first_name character varying(50),
    last_name character varying(50),
    email character varying(191),
    image_url character varying(256),
    activated boolean NOT NULL,
    lang_key character varying(10),
    activation_key character varying(20),
    reset_key character varying(20),
    created_by character varying(50) NOT NULL,
    created_date timestamp without time zone,
    reset_date timestamp without time zone,
    last_modified_by character varying(50),
    last_modified_date timestamp without time zone
);


ALTER TABLE public.jhi_user OWNER TO miuser;

--
-- Name: jhi_user_authority; Type: TABLE; Schema: public; Owner: miuser
--

CREATE TABLE public.jhi_user_authority (
    user_id bigint NOT NULL,
    authority_name character varying(50) NOT NULL
);


ALTER TABLE public.jhi_user_authority OWNER TO miuser;

--
-- Name: jobs; Type: TABLE; Schema: public; Owner: miuser
--

CREATE TABLE public.jobs (
    id bigint NOT NULL,
    name character varying(255),
    salary numeric(21,2)
);


ALTER TABLE public.jobs OWNER TO miuser;

--
-- Name: sequence_generator; Type: SEQUENCE; Schema: public; Owner: miuser
--

CREATE SEQUENCE public.sequence_generator
    START WITH 1050
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.sequence_generator OWNER TO miuser;

--
-- Name: databasechangeloglock databasechangeloglock_pkey; Type: CONSTRAINT; Schema: public; Owner: miuser
--

ALTER TABLE ONLY public.databasechangeloglock
    ADD CONSTRAINT databasechangeloglock_pkey PRIMARY KEY (id);


--
-- Name: employee_worked_hours employee_worked_hours_pkey; Type: CONSTRAINT; Schema: public; Owner: miuser
--

ALTER TABLE ONLY public.employee_worked_hours
    ADD CONSTRAINT employee_worked_hours_pkey PRIMARY KEY (id);


--
-- Name: employees employees_pkey; Type: CONSTRAINT; Schema: public; Owner: miuser
--

ALTER TABLE ONLY public.employees
    ADD CONSTRAINT employees_pkey PRIMARY KEY (id);


--
-- Name: genders genders_pkey; Type: CONSTRAINT; Schema: public; Owner: miuser
--

ALTER TABLE ONLY public.genders
    ADD CONSTRAINT genders_pkey PRIMARY KEY (id);


--
-- Name: jhi_authority jhi_authority_pkey; Type: CONSTRAINT; Schema: public; Owner: miuser
--

ALTER TABLE ONLY public.jhi_authority
    ADD CONSTRAINT jhi_authority_pkey PRIMARY KEY (name);


--
-- Name: jhi_user_authority jhi_user_authority_pkey; Type: CONSTRAINT; Schema: public; Owner: miuser
--

ALTER TABLE ONLY public.jhi_user_authority
    ADD CONSTRAINT jhi_user_authority_pkey PRIMARY KEY (user_id, authority_name);


--
-- Name: jhi_user jhi_user_pkey; Type: CONSTRAINT; Schema: public; Owner: miuser
--

ALTER TABLE ONLY public.jhi_user
    ADD CONSTRAINT jhi_user_pkey PRIMARY KEY (id);


--
-- Name: jobs jobs_pkey; Type: CONSTRAINT; Schema: public; Owner: miuser
--

ALTER TABLE ONLY public.jobs
    ADD CONSTRAINT jobs_pkey PRIMARY KEY (id);


--
-- Name: jhi_user ux_user_email; Type: CONSTRAINT; Schema: public; Owner: miuser
--

ALTER TABLE ONLY public.jhi_user
    ADD CONSTRAINT ux_user_email UNIQUE (email);


--
-- Name: jhi_user ux_user_login; Type: CONSTRAINT; Schema: public; Owner: miuser
--

ALTER TABLE ONLY public.jhi_user
    ADD CONSTRAINT ux_user_login UNIQUE (login);


--
-- Name: jhi_user_authority fk_authority_name; Type: FK CONSTRAINT; Schema: public; Owner: miuser
--

ALTER TABLE ONLY public.jhi_user_authority
    ADD CONSTRAINT fk_authority_name FOREIGN KEY (authority_name) REFERENCES public.jhi_authority(name);


--
-- Name: employee_worked_hours fk_employee_worked_hours__employee_id; Type: FK CONSTRAINT; Schema: public; Owner: miuser
--

ALTER TABLE ONLY public.employee_worked_hours
    ADD CONSTRAINT fk_employee_worked_hours__employee_id FOREIGN KEY (employee_id) REFERENCES public.employees(id);


--
-- Name: employees fk_employees__gender_id; Type: FK CONSTRAINT; Schema: public; Owner: miuser
--

ALTER TABLE ONLY public.employees
    ADD CONSTRAINT fk_employees__gender_id FOREIGN KEY (gender_id) REFERENCES public.genders(id);


--
-- Name: employees fk_employees__job_id; Type: FK CONSTRAINT; Schema: public; Owner: miuser
--

ALTER TABLE ONLY public.employees
    ADD CONSTRAINT fk_employees__job_id FOREIGN KEY (job_id) REFERENCES public.jobs(id);


--
-- Name: jhi_user_authority fk_user_id; Type: FK CONSTRAINT; Schema: public; Owner: miuser
--

ALTER TABLE ONLY public.jhi_user_authority
    ADD CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES public.jhi_user(id);


--
-- PostgreSQL database dump complete
--

