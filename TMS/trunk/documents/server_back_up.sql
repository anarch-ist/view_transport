--
-- PostgreSQL database cluster dump
--

-- Started on 2016-11-03 20:03:30

SET default_transaction_read_only = off;

SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;

--
-- Roles
--

CREATE ROLE admin_user;
ALTER ROLE admin_user WITH NOSUPERUSER INHERIT NOCREATEROLE NOCREATEDB LOGIN NOREPLICATION NOBYPASSRLS PASSWORD 'md54661ddbb5a2f805c2894eab5f9f89b92';
CREATE ROLE app_user;
ALTER ROLE app_user WITH NOSUPERUSER INHERIT NOCREATEROLE NOCREATEDB LOGIN NOREPLICATION NOBYPASSRLS PASSWORD 'md51337097ae213d2e72dcb90b85f1f8ba8';
CREATE ROLE postgres;
ALTER ROLE postgres WITH SUPERUSER INHERIT CREATEROLE CREATEDB LOGIN REPLICATION BYPASSRLS PASSWORD 'md5c01787cd910067dc6f07a026d73fd178';






--
-- Database creation
--

REVOKE ALL ON DATABASE postgres FROM PUBLIC;
REVOKE ALL ON DATABASE postgres FROM postgres;
GRANT ALL ON DATABASE postgres TO postgres;
GRANT CONNECT,TEMPORARY ON DATABASE postgres TO PUBLIC;
GRANT CONNECT ON DATABASE postgres TO app_user;
GRANT CONNECT ON DATABASE postgres TO admin_user;
REVOKE ALL ON DATABASE template1 FROM PUBLIC;
REVOKE ALL ON DATABASE template1 FROM postgres;
GRANT ALL ON DATABASE template1 TO postgres;
GRANT CONNECT ON DATABASE template1 TO PUBLIC;


\connect postgres

SET default_transaction_read_only = off;

--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.3
-- Dumped by pg_dump version 9.5.4

-- Started on 2016-11-03 20:03:30

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 2286 (class 1262 OID 12379)
-- Dependencies: 2285
-- Name: postgres; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON DATABASE postgres IS 'default administrative connection database';


--
-- TOC entry 6 (class 2615 OID 21061)
-- Name: audit; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA audit;


ALTER SCHEMA audit OWNER TO postgres;

--
-- TOC entry 1 (class 3079 OID 12361)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2289 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = audit, pg_catalog;

--
-- TOC entry 220 (class 1255 OID 21070)
-- Name: process_doc_periods_audit(); Type: FUNCTION; Schema: audit; Owner: postgres
--

CREATE FUNCTION process_doc_periods_audit() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
  userID INTEGER;
BEGIN
  BEGIN
    -- BINDING DaoFacade.java passUserIdToAuditTrigger()
    userID := (SELECT current_setting('audit.current_user_id'))::INTEGER;
    EXCEPTION WHEN OTHERS THEN
    userID := NULL;
  END;
  IF (userID IS NULL)
  THEN RETURN NULL;
  END IF;
  IF (TG_OP = 'DELETE') THEN
    INSERT INTO audit.doc_periods_audit(operation, stamp, userID, docPeriodID, docID, periodBegin, periodEnd)  SELECT 'D', now(), userID, OLD.*;
    RETURN OLD;
  ELSIF (TG_OP = 'UPDATE') THEN
    INSERT INTO audit.doc_periods_audit(operation, stamp, userID, docPeriodID, docID, periodBegin, periodEnd) SELECT 'U', now(), userID, NEW.*;
    RETURN NEW;
  ELSIF (TG_OP = 'INSERT') THEN
    INSERT INTO audit.doc_periods_audit(operation, stamp, userID, docPeriodID, docID, periodBegin, periodEnd) SELECT 'I', now(), userID, NEW.*;
    RETURN NEW;
  END IF;
  RETURN NULL;
END;
$$;


ALTER FUNCTION audit.process_doc_periods_audit() OWNER TO postgres;

--
-- TOC entry 221 (class 1255 OID 21083)
-- Name: process_donut_doc_periods_audit(); Type: FUNCTION; Schema: audit; Owner: postgres
--

CREATE FUNCTION process_donut_doc_periods_audit() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
  userID INTEGER;
BEGIN
  BEGIN
    -- BINDING DaoFacade.java passUserIdToAuditTrigger()
    userID := (SELECT current_setting('audit.current_user_id'))::INTEGER;
    EXCEPTION WHEN OTHERS THEN
    userID := NULL;
  END;
  IF (userID IS NULL)
  THEN RETURN NULL;
  END IF;
  IF (TG_OP = 'DELETE') THEN
    INSERT INTO audit.donut_doc_periods_audit(operation, stamp, userID, donutDocPeriodID, creationDate, commentForDonut, driver, driverPhoneNumber, licensePlate, palletsQty, supplierUserID, lastModified)      SELECT 'D', now(), userID, OLD.*;
    RETURN OLD;
  ELSIF (TG_OP = 'UPDATE') THEN
    INSERT INTO audit.donut_doc_periods_audit(operation, stamp, userID, donutDocPeriodID, creationDate, commentForDonut, driver, driverPhoneNumber, licensePlate, palletsQty, supplierUserID, lastModified) SELECT 'U', now(), userID, NEW.*;
    RETURN NEW;
  ELSIF (TG_OP = 'INSERT') THEN
    INSERT INTO audit.donut_doc_periods_audit(operation, stamp, userID, donutDocPeriodID, creationDate, commentForDonut, driver, driverPhoneNumber, licensePlate, palletsQty, supplierUserID, lastModified) SELECT 'I', now(), userID, NEW.*;
    RETURN NEW;
  END IF;
  RETURN NULL;
END;
$$;


ALTER FUNCTION audit.process_donut_doc_periods_audit() OWNER TO postgres;

--
-- TOC entry 222 (class 1255 OID 21099)
-- Name: process_orders_audit(); Type: FUNCTION; Schema: audit; Owner: postgres
--

CREATE FUNCTION process_orders_audit() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
  userID INTEGER;
BEGIN
  BEGIN
    -- BINDING DaoFacade.java passUserIdToAuditTrigger()
    userID := (SELECT current_setting('audit.current_user_id'))::INTEGER;
    EXCEPTION WHEN OTHERS THEN
    userID := NULL;
  END;
  IF (userID IS NULL)
  THEN RETURN NULL;
  END IF;
  IF (TG_OP = 'DELETE') THEN
    INSERT INTO audit.orders_audit(operation, stamp, userID, orderID, orderNumber, boxQty, finalDestinationWarehouseID, donutDocPeriodID, orderStatus, commentForStatus, invoiceNumber, goodsCost, orderPalletsQty, orderDate, invoiceDate, deliveryDate, weight, volume, lastModified)  SELECT 'D', now(), userID, OLD.*;
    RETURN OLD;
  ELSIF (TG_OP = 'UPDATE') THEN
    INSERT INTO audit.orders_audit(operation, stamp, userID, orderID, orderNumber, boxQty, finalDestinationWarehouseID, donutDocPeriodID, orderStatus, commentForStatus, invoiceNumber, goodsCost, orderPalletsQty, orderDate, invoiceDate, deliveryDate, weight, volume, lastModified) SELECT 'U', now(), userID, NEW.*;
    RETURN NEW;
  ELSIF (TG_OP = 'INSERT') THEN
    INSERT INTO audit.orders_audit(operation, stamp, userID, orderID, orderNumber, boxQty, finalDestinationWarehouseID, donutDocPeriodID, orderStatus, commentForStatus, invoiceNumber, goodsCost, orderPalletsQty, orderDate, invoiceDate, deliveryDate, weight, volume, lastModified) SELECT 'I', now(), userID, NEW.*;
    RETURN NEW;
  END IF;
  RETURN NULL;
END;
$$;


ALTER FUNCTION audit.process_orders_audit() OWNER TO postgres;

SET search_path = public, pg_catalog;

--
-- TOC entry 219 (class 1255 OID 20970)
-- Name: doc_periods_no_intersections(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION doc_periods_no_intersections() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
  qty INTEGER;
BEGIN
  qty := (SELECT COUNT(*)
          FROM doc_periods
          WHERE docid = NEW.docId AND
                (doc_periods.periodbegin, doc_periods.periodend) OVERLAPS (NEW.periodbegin, NEW.periodend));

  IF qty = 0 THEN
    RETURN NEW;
  ELSEIF (TG_OP = 'INSERT') THEN
    RAISE EXCEPTION 'Период %, % занят частично или полностью.', NEW.periodbegin, NEW.periodend;
  ELSEIF (TG_OP = 'UPDATE') THEN
    IF NEW.periodBegin >= OLD.periodBegin AND NEW.periodEnd <= OLD.periodEnd THEN
      RETURN NEW;
    ELSE
      RAISE EXCEPTION 'Период %, % занят частично или полностью.', NEW.periodbegin, NEW.periodend;
    END IF;
  END IF;
END;
$$;


ALTER FUNCTION public.doc_periods_no_intersections() OWNER TO postgres;

--
-- TOC entry 218 (class 1255 OID 20928)
-- Name: insert_permission_for_role(character varying, character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION insert_permission_for_role(_userroleid character varying, _permissionid character varying) RETURNS void
    LANGUAGE plpgsql
    AS $$
BEGIN
  INSERT INTO permissions_for_roles (userRoleID, permissionID) SELECT
                                                                 user_roles.userRoleID,
                                                                 permissions.permissionID
                                                               FROM user_roles, permissions
                                                               WHERE user_roles.userRoleID = _userRoleID AND
                                                                     permissions.permissionID = _permissionID;
END;
$$;


ALTER FUNCTION public.insert_permission_for_role(_userroleid character varying, _permissionid character varying) OWNER TO postgres;

SET search_path = audit, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 201 (class 1259 OID 21064)
-- Name: doc_periods_audit; Type: TABLE; Schema: audit; Owner: postgres
--

CREATE TABLE doc_periods_audit (
    docperiodsauditid bigint NOT NULL,
    operation character(1) NOT NULL,
    stamp timestamp with time zone NOT NULL,
    userid integer NOT NULL,
    docperiodid bigint NOT NULL,
    docid integer NOT NULL,
    periodbegin timestamp with time zone NOT NULL,
    periodend timestamp with time zone NOT NULL
);


ALTER TABLE doc_periods_audit OWNER TO postgres;

--
-- TOC entry 200 (class 1259 OID 21062)
-- Name: doc_periods_audit_docperiodsauditid_seq; Type: SEQUENCE; Schema: audit; Owner: postgres
--

CREATE SEQUENCE doc_periods_audit_docperiodsauditid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE doc_periods_audit_docperiodsauditid_seq OWNER TO postgres;

--
-- TOC entry 2291 (class 0 OID 0)
-- Dependencies: 200
-- Name: doc_periods_audit_docperiodsauditid_seq; Type: SEQUENCE OWNED BY; Schema: audit; Owner: postgres
--

ALTER SEQUENCE doc_periods_audit_docperiodsauditid_seq OWNED BY doc_periods_audit.docperiodsauditid;


--
-- TOC entry 203 (class 1259 OID 21074)
-- Name: donut_doc_periods_audit; Type: TABLE; Schema: audit; Owner: postgres
--

CREATE TABLE donut_doc_periods_audit (
    donutdocperiodsauditid bigint NOT NULL,
    operation character(1) NOT NULL,
    stamp timestamp with time zone NOT NULL,
    userid integer NOT NULL,
    donutdocperiodid bigint NOT NULL,
    creationdate date NOT NULL,
    commentfordonut text NOT NULL,
    driver character varying(255) NOT NULL,
    driverphonenumber character varying(12) NOT NULL,
    licenseplate character varying(9) NOT NULL,
    palletsqty integer NOT NULL,
    supplieruserid integer NOT NULL,
    lastmodified timestamp with time zone NOT NULL
);


ALTER TABLE donut_doc_periods_audit OWNER TO postgres;

--
-- TOC entry 202 (class 1259 OID 21072)
-- Name: donut_doc_periods_audit_donutdocperiodsauditid_seq; Type: SEQUENCE; Schema: audit; Owner: postgres
--

CREATE SEQUENCE donut_doc_periods_audit_donutdocperiodsauditid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE donut_doc_periods_audit_donutdocperiodsauditid_seq OWNER TO postgres;

--
-- TOC entry 2294 (class 0 OID 0)
-- Dependencies: 202
-- Name: donut_doc_periods_audit_donutdocperiodsauditid_seq; Type: SEQUENCE OWNED BY; Schema: audit; Owner: postgres
--

ALTER SEQUENCE donut_doc_periods_audit_donutdocperiodsauditid_seq OWNED BY donut_doc_periods_audit.donutdocperiodsauditid;


--
-- TOC entry 205 (class 1259 OID 21087)
-- Name: orders_audit; Type: TABLE; Schema: audit; Owner: postgres
--

CREATE TABLE orders_audit (
    ordersauditid bigint NOT NULL,
    operation character(1) NOT NULL,
    stamp timestamp with time zone NOT NULL,
    userid integer NOT NULL,
    orderid bigint,
    ordernumber character varying(16) NOT NULL,
    boxqty smallint NOT NULL,
    finaldestinationwarehouseid integer NOT NULL,
    donutdocperiodid integer NOT NULL,
    orderstatus character varying(32) NOT NULL,
    commentforstatus text NOT NULL,
    invoicenumber character varying(255) NOT NULL,
    goodscost numeric(12,2) NOT NULL,
    orderpalletsqty smallint NOT NULL,
    orderdate date,
    invoicedate date,
    deliverydate timestamp without time zone,
    weight integer,
    volume integer,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    CONSTRAINT positive_box_qty CHECK ((boxqty >= 0)),
    CONSTRAINT positive_orders_pallets_qty CHECK ((orderpalletsqty >= 0))
);


ALTER TABLE orders_audit OWNER TO postgres;

--
-- TOC entry 204 (class 1259 OID 21085)
-- Name: orders_audit_ordersauditid_seq; Type: SEQUENCE; Schema: audit; Owner: postgres
--

CREATE SEQUENCE orders_audit_ordersauditid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE orders_audit_ordersauditid_seq OWNER TO postgres;

--
-- TOC entry 2297 (class 0 OID 0)
-- Dependencies: 204
-- Name: orders_audit_ordersauditid_seq; Type: SEQUENCE OWNED BY; Schema: audit; Owner: postgres
--

ALTER SEQUENCE orders_audit_ordersauditid_seq OWNED BY orders_audit.ordersauditid;


SET search_path = public, pg_catalog;

--
-- TOC entry 192 (class 1259 OID 20956)
-- Name: doc_periods; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE doc_periods (
    docperiodid bigint NOT NULL,
    docid integer NOT NULL,
    periodbegin timestamp with time zone NOT NULL,
    periodend timestamp with time zone NOT NULL,
    CONSTRAINT doc_periods_periodbegin_check CHECK ((date_part('timezone'::text, periodbegin) = '0'::double precision)),
    CONSTRAINT doc_periods_periodend_check CHECK ((date_part('timezone'::text, periodend) = '0'::double precision)),
    CONSTRAINT multiplicity_of_the_period CHECK (((periodend > periodbegin) AND (((date_part('epoch'::text, (periodend - periodbegin)))::integer % 1800) = 0)))
);


ALTER TABLE doc_periods OWNER TO postgres;

--
-- TOC entry 191 (class 1259 OID 20954)
-- Name: doc_periods_docperiodid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE doc_periods_docperiodid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE doc_periods_docperiodid_seq OWNER TO postgres;

--
-- TOC entry 2300 (class 0 OID 0)
-- Dependencies: 191
-- Name: doc_periods_docperiodid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE doc_periods_docperiodid_seq OWNED BY doc_periods.docperiodid;


--
-- TOC entry 190 (class 1259 OID 20943)
-- Name: docs; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE docs (
    docid integer NOT NULL,
    docname character varying(255) NOT NULL,
    warehouseid integer NOT NULL
);


ALTER TABLE docs OWNER TO postgres;

--
-- TOC entry 189 (class 1259 OID 20941)
-- Name: docs_docid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE docs_docid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE docs_docid_seq OWNER TO postgres;

--
-- TOC entry 2303 (class 0 OID 0)
-- Dependencies: 189
-- Name: docs_docid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE docs_docid_seq OWNED BY docs.docid;


--
-- TOC entry 197 (class 1259 OID 21014)
-- Name: donut_doc_periods; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE donut_doc_periods (
    donutdocperiodid bigint NOT NULL,
    creationdate date DEFAULT now() NOT NULL,
    commentfordonut text NOT NULL,
    driver character varying(255) NOT NULL,
    driverphonenumber character varying(12) NOT NULL,
    licenseplate character varying(9) NOT NULL,
    palletsqty integer NOT NULL,
    supplieruserid integer NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    CONSTRAINT positive_pallets_qty CHECK ((palletsqty >= 0))
);


ALTER TABLE donut_doc_periods OWNER TO postgres;

--
-- TOC entry 199 (class 1259 OID 21038)
-- Name: orders; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE orders (
    orderid integer NOT NULL,
    ordernumber character varying(16) NOT NULL,
    boxqty smallint NOT NULL,
    finaldestinationwarehouseid integer NOT NULL,
    donutdocperiodid integer NOT NULL,
    orderstatus character varying(32) NOT NULL,
    commentforstatus text NOT NULL,
    invoicenumber character varying(255) NOT NULL,
    goodscost numeric(12,2) NOT NULL,
    orderpalletsqty smallint NOT NULL,
    orderdate date,
    invoicedate date,
    deliverydate timestamp without time zone,
    weight integer,
    volume integer,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    CONSTRAINT order_statuses CHECK (((orderstatus)::text = ANY ((ARRAY['CREATED'::character varying, 'ARRIVED'::character varying, 'CANCELLED_BY_WAREHOUSE_USER'::character varying, 'CANCELLED_BY_SUPPLIER_USER'::character varying, 'ERROR'::character varying, 'DELIVERED'::character varying])::text[]))),
    CONSTRAINT positive_box_qty CHECK ((boxqty >= 0)),
    CONSTRAINT positive_orders_pallets_qty CHECK ((orderpalletsqty >= 0))
);


ALTER TABLE orders OWNER TO postgres;

--
-- TOC entry 198 (class 1259 OID 21036)
-- Name: orders_orderid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE orders_orderid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE orders_orderid_seq OWNER TO postgres;

--
-- TOC entry 2307 (class 0 OID 0)
-- Dependencies: 198
-- Name: orders_orderid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE orders_orderid_seq OWNED BY orders.orderid;


--
-- TOC entry 185 (class 1259 OID 20908)
-- Name: permissions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE permissions (
    permissionid character varying(32) NOT NULL
);


ALTER TABLE permissions OWNER TO postgres;

--
-- TOC entry 186 (class 1259 OID 20913)
-- Name: permissions_for_roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE permissions_for_roles (
    userroleid character varying(32) NOT NULL,
    permissionid character varying(32) NOT NULL
);


ALTER TABLE permissions_for_roles OWNER TO postgres;

--
-- TOC entry 196 (class 1259 OID 20999)
-- Name: supplier_users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE supplier_users (
    userid integer NOT NULL,
    supplierid integer NOT NULL
);


ALTER TABLE supplier_users OWNER TO postgres;

--
-- TOC entry 195 (class 1259 OID 20990)
-- Name: suppliers; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE suppliers (
    supplierid integer NOT NULL,
    inn character varying(32) NOT NULL,
    clientname character varying(255),
    kpp character varying(64),
    coraccount character varying(64),
    curaccount character varying(64),
    bik character varying(64),
    bankname character varying(128),
    contractnumber character varying(64),
    dateofsigning date,
    startcontractdate date,
    endcontractdate date
);


ALTER TABLE suppliers OWNER TO postgres;

--
-- TOC entry 194 (class 1259 OID 20988)
-- Name: suppliers_supplierid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE suppliers_supplierid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE suppliers_supplierid_seq OWNER TO postgres;

--
-- TOC entry 2313 (class 0 OID 0)
-- Dependencies: 194
-- Name: suppliers_supplierid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE suppliers_supplierid_seq OWNED BY suppliers.supplierid;


--
-- TOC entry 182 (class 1259 OID 20885)
-- Name: user_roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE user_roles (
    userroleid character varying(32) NOT NULL
);


ALTER TABLE user_roles OWNER TO postgres;

--
-- TOC entry 184 (class 1259 OID 20892)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE users (
    userid integer NOT NULL,
    userlogin character varying(255) NOT NULL,
    salt character(16) NOT NULL,
    passandsalt character(32) NOT NULL,
    userroleid character varying(32) NOT NULL,
    username character varying(255) NOT NULL,
    phonenumber character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    "position" character varying(64) NOT NULL
);


ALTER TABLE users OWNER TO postgres;

--
-- TOC entry 183 (class 1259 OID 20890)
-- Name: users_userid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE users_userid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE users_userid_seq OWNER TO postgres;

--
-- TOC entry 2317 (class 0 OID 0)
-- Dependencies: 183
-- Name: users_userid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE users_userid_seq OWNED BY users.userid;


--
-- TOC entry 193 (class 1259 OID 20973)
-- Name: warehouse_users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE warehouse_users (
    userid integer NOT NULL,
    warehouseid integer NOT NULL
);


ALTER TABLE warehouse_users OWNER TO postgres;

--
-- TOC entry 188 (class 1259 OID 20931)
-- Name: warehouses; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE warehouses (
    warehouseid integer NOT NULL,
    warehousename character varying(128) NOT NULL,
    rustimezoneabbr character varying(6) NOT NULL,
    region character varying(128),
    district character varying(64),
    locality character varying(64),
    mailindex character varying(6),
    address text,
    email character varying(255),
    phonenumber character varying(16),
    responsiblepersonid character varying(128)
);


ALTER TABLE warehouses OWNER TO postgres;

--
-- TOC entry 187 (class 1259 OID 20929)
-- Name: warehouses_warehouseid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE warehouses_warehouseid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE warehouses_warehouseid_seq OWNER TO postgres;

--
-- TOC entry 2321 (class 0 OID 0)
-- Dependencies: 187
-- Name: warehouses_warehouseid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE warehouses_warehouseid_seq OWNED BY warehouses.warehouseid;


SET search_path = audit, pg_catalog;

--
-- TOC entry 2086 (class 2604 OID 21067)
-- Name: docperiodsauditid; Type: DEFAULT; Schema: audit; Owner: postgres
--

ALTER TABLE ONLY doc_periods_audit ALTER COLUMN docperiodsauditid SET DEFAULT nextval('doc_periods_audit_docperiodsauditid_seq'::regclass);


--
-- TOC entry 2087 (class 2604 OID 21077)
-- Name: donutdocperiodsauditid; Type: DEFAULT; Schema: audit; Owner: postgres
--

ALTER TABLE ONLY donut_doc_periods_audit ALTER COLUMN donutdocperiodsauditid SET DEFAULT nextval('donut_doc_periods_audit_donutdocperiodsauditid_seq'::regclass);


--
-- TOC entry 2088 (class 2604 OID 21090)
-- Name: ordersauditid; Type: DEFAULT; Schema: audit; Owner: postgres
--

ALTER TABLE ONLY orders_audit ALTER COLUMN ordersauditid SET DEFAULT nextval('orders_audit_ordersauditid_seq'::regclass);


SET search_path = public, pg_catalog;

--
-- TOC entry 2073 (class 2604 OID 20959)
-- Name: docperiodid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY doc_periods ALTER COLUMN docperiodid SET DEFAULT nextval('doc_periods_docperiodid_seq'::regclass);


--
-- TOC entry 2072 (class 2604 OID 20946)
-- Name: docid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY docs ALTER COLUMN docid SET DEFAULT nextval('docs_docid_seq'::regclass);


--
-- TOC entry 2081 (class 2604 OID 21041)
-- Name: orderid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY orders ALTER COLUMN orderid SET DEFAULT nextval('orders_orderid_seq'::regclass);


--
-- TOC entry 2077 (class 2604 OID 20993)
-- Name: supplierid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY suppliers ALTER COLUMN supplierid SET DEFAULT nextval('suppliers_supplierid_seq'::regclass);


--
-- TOC entry 2070 (class 2604 OID 20895)
-- Name: userid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY users ALTER COLUMN userid SET DEFAULT nextval('users_userid_seq'::regclass);


--
-- TOC entry 2071 (class 2604 OID 20934)
-- Name: warehouseid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY warehouses ALTER COLUMN warehouseid SET DEFAULT nextval('warehouses_warehouseid_seq'::regclass);


SET search_path = audit, pg_catalog;

--
-- TOC entry 2276 (class 0 OID 21064)
-- Dependencies: 201
-- Data for Name: doc_periods_audit; Type: TABLE DATA; Schema: audit; Owner: postgres
--

COPY doc_periods_audit (docperiodsauditid, operation, stamp, userid, docperiodid, docid, periodbegin, periodend) FROM stdin;
1	I	2016-07-06 11:21:00.818358+00	16	152	11	2016-07-10 13:00:00+00	2016-07-10 19:00:00+00
2	U	2016-07-06 11:21:03.652103+00	16	152	11	2016-07-10 13:30:00+00	2016-07-10 19:00:00+00
3	I	2016-07-06 11:21:05.42592+00	16	153	11	2016-07-10 13:00:00+00	2016-07-10 13:30:00+00
4	I	2016-07-06 11:21:17.059984+00	16	154	11	2016-07-06 13:00:00+00	2016-07-06 19:00:00+00
5	I	2016-07-06 11:21:26.326098+00	16	155	11	2016-07-07 13:00:00+00	2016-07-07 19:00:00+00
6	I	2016-07-06 11:21:35.895785+00	16	156	11	2016-07-08 13:00:00+00	2016-07-08 19:00:00+00
7	I	2016-07-06 11:21:43.496254+00	16	157	11	2016-07-09 13:00:00+00	2016-07-09 19:00:00+00
8	I	2016-07-06 11:21:55.051721+00	16	158	11	2016-07-10 04:00:00+00	2016-07-10 13:00:00+00
9	I	2016-07-06 11:22:05.291223+00	16	159	11	2016-07-09 04:00:00+00	2016-07-09 13:00:00+00
10	I	2016-07-06 11:22:13.817071+00	16	160	11	2016-07-11 13:00:00+00	2016-07-11 19:00:00+00
11	I	2016-07-06 11:22:24.142472+00	16	161	11	2016-07-12 13:00:00+00	2016-07-12 19:00:00+00
12	I	2016-07-06 11:22:32.507417+00	16	162	11	2016-07-13 13:00:00+00	2016-07-13 19:00:00+00
13	I	2016-07-06 11:22:40.246082+00	16	163	11	2016-07-14 13:00:00+00	2016-07-14 19:00:00+00
14	I	2016-07-06 11:22:48.398657+00	16	164	11	2016-07-15 13:00:00+00	2016-07-15 19:00:00+00
15	I	2016-07-06 11:22:59.246388+00	16	165	11	2016-07-16 10:00:00+00	2016-07-16 11:00:00+00
16	I	2016-07-06 11:22:59.246388+00	16	166	11	2016-07-16 16:00:00+00	2016-07-16 17:00:00+00
17	I	2016-07-06 11:22:59.246388+00	16	167	11	2016-07-16 04:00:00+00	2016-07-16 05:00:00+00
18	I	2016-07-06 11:22:59.246388+00	16	168	11	2016-07-16 07:00:00+00	2016-07-16 08:00:00+00
19	I	2016-07-06 11:22:59.246388+00	16	169	11	2016-07-16 13:00:00+00	2016-07-16 14:00:00+00
20	I	2016-07-06 11:23:08.677224+00	16	170	11	2016-07-16 11:00:00+00	2016-07-16 13:00:00+00
21	I	2016-07-06 11:23:08.677224+00	16	171	11	2016-07-16 14:00:00+00	2016-07-16 16:00:00+00
22	I	2016-07-06 11:23:08.677224+00	16	172	11	2016-07-16 08:00:00+00	2016-07-16 10:00:00+00
23	I	2016-07-06 11:23:08.677224+00	16	173	11	2016-07-16 17:00:00+00	2016-07-16 19:00:00+00
24	I	2016-07-06 11:23:08.677224+00	16	174	11	2016-07-16 05:00:00+00	2016-07-16 07:00:00+00
25	I	2016-07-06 11:23:24.264733+00	16	175	11	2016-07-17 04:00:00+00	2016-07-17 19:00:00+00
26	I	2016-07-06 11:23:32.512054+00	16	176	11	2016-07-18 13:00:00+00	2016-07-18 19:00:00+00
27	I	2016-07-06 12:51:02.953585+00	22	177	12	2016-07-06 10:00:00+00	2016-07-06 16:00:00+00
28	I	2016-07-06 12:51:11.253507+00	22	178	12	2016-07-07 10:00:00+00	2016-07-07 16:00:00+00
29	I	2016-07-06 12:51:19.047938+00	22	179	12	2016-07-08 10:00:00+00	2016-07-08 16:00:00+00
30	I	2016-07-06 12:51:27.028739+00	22	180	12	2016-07-09 10:00:00+00	2016-07-09 16:00:00+00
31	I	2016-07-06 12:51:34.453574+00	22	181	12	2016-07-10 10:00:00+00	2016-07-10 16:00:00+00
32	I	2016-07-06 12:51:42.118947+00	22	182	12	2016-07-11 10:00:00+00	2016-07-11 16:00:00+00
33	I	2016-07-06 12:51:49.881994+00	22	183	12	2016-07-12 10:00:00+00	2016-07-12 16:00:00+00
34	I	2016-07-06 12:51:57.216383+00	22	184	12	2016-07-13 10:00:00+00	2016-07-13 16:00:00+00
35	I	2016-07-06 12:52:04.593256+00	22	185	12	2016-07-14 10:00:00+00	2016-07-14 16:00:00+00
36	I	2016-07-06 12:52:11.958147+00	22	186	12	2016-07-15 10:00:00+00	2016-07-15 16:00:00+00
37	I	2016-07-06 12:52:30.14131+00	22	187	12	2016-07-16 01:00:00+00	2016-07-16 16:00:00+00
38	I	2016-07-06 12:52:47.18057+00	22	188	12	2016-07-17 01:00:00+00	2016-07-17 16:00:00+00
39	I	2016-07-06 12:52:56.109146+00	22	189	12	2016-07-18 10:00:00+00	2016-07-18 16:00:00+00
40	I	2016-07-06 13:24:11.33286+00	17	190	7	2016-07-06 06:30:00+00	2016-07-06 07:00:00+00
41	D	2016-07-06 13:24:13.449203+00	17	190	7	2016-07-06 06:30:00+00	2016-07-06 07:00:00+00
42	I	2016-07-07 09:14:28.146085+00	15	191	7	2016-07-11 06:00:00+00	2016-07-11 07:00:00+00
43	D	2016-07-07 09:14:31.166229+00	15	191	7	2016-07-11 06:00:00+00	2016-07-11 07:00:00+00
44	I	2016-07-07 09:15:30.619245+00	13	192	7	2016-07-07 06:00:00+00	2016-07-07 07:00:00+00
45	D	2016-07-07 09:15:32.949903+00	13	192	7	2016-07-07 06:00:00+00	2016-07-07 07:00:00+00
46	I	2016-07-07 14:48:41.567903+00	17	193	7	2016-07-07 08:30:00+00	2016-07-07 09:00:00+00
47	D	2016-07-07 14:48:44.383382+00	17	193	7	2016-07-07 08:30:00+00	2016-07-07 09:00:00+00
48	U	2016-07-07 14:48:46.612855+00	17	23	7	2016-07-06 21:00:00+00	2016-07-07 05:00:00+00
49	I	2016-07-07 14:48:46.612855+00	17	194	7	2016-07-07 05:30:00+00	2016-07-07 06:00:00+00
51	U	2016-07-09 10:59:14.475064+00	17	22	7	2016-07-05 21:00:00+00	2016-07-06 05:30:00+00
52	I	2016-07-09 10:59:31.164336+00	17	196	7	2016-07-06 06:00:00+00	2016-07-06 06:30:00+00
53	D	2016-07-09 10:59:33.234499+00	17	196	7	2016-07-06 06:00:00+00	2016-07-06 06:30:00+00
54	I	2016-07-09 11:01:00.791788+00	12	197	7	2016-07-09 06:00:00+00	2016-07-09 07:00:00+00
55	D	2016-07-09 13:13:54.505199+00	17	197	7	2016-07-09 06:00:00+00	2016-07-09 07:00:00+00
59	D	2016-07-13 10:29:48.884843+00	17	25	7	2016-07-08 21:00:00+00	2016-07-09 06:00:00+00
60	D	2016-07-13 10:29:48.884843+00	17	116	7	2016-07-09 15:00:00+00	2016-07-09 21:00:00+00
61	I	2016-07-13 10:30:10.134928+00	17	201	7	2016-07-22 12:30:00+00	2016-07-22 13:00:00+00
62	D	2016-07-13 10:30:12.378664+00	17	201	7	2016-07-22 12:30:00+00	2016-07-22 13:00:00+00
63	I	2016-07-13 10:33:48.059204+00	15	202	7	2016-07-18 13:00:00+00	2016-07-18 13:30:00+00
64	D	2016-07-13 10:47:43.283685+00	17	202	7	2016-07-18 13:00:00+00	2016-07-18 13:30:00+00
65	I	2016-07-14 12:17:49.806692+00	12	203	7	2016-07-14 10:00:00+00	2016-07-14 11:00:00+00
66	I	2016-07-14 12:37:24.347308+00	12	204	7	2016-07-14 07:30:00+00	2016-07-14 08:00:00+00
67	D	2016-07-14 12:37:27.135198+00	12	204	7	2016-07-14 07:30:00+00	2016-07-14 08:00:00+00
68	U	2016-07-14 14:33:38.229664+00	17	34	7	2016-07-17 21:00:00+00	2016-07-18 00:00:00+00
69	I	2016-07-14 14:33:38.229664+00	17	205	7	2016-07-18 01:30:00+00	2016-07-18 06:00:00+00
70	I	2016-07-14 14:33:38.229664+00	17	206	7	2016-07-18 00:30:00+00	2016-07-18 01:00:00+00
73	U	2016-07-14 14:34:13.777849+00	17	205	7	2016-07-18 02:00:00+00	2016-07-18 06:00:00+00
74	D	2016-07-14 14:34:13.777849+00	17	206	7	2016-07-18 00:30:00+00	2016-07-18 01:00:00+00
76	I	2016-07-14 14:34:42.845554+00	17	208	7	2016-07-18 10:00:00+00	2016-07-18 11:00:00+00
77	I	2016-07-14 14:34:42.845554+00	17	209	7	2016-07-18 07:00:00+00	2016-07-18 08:00:00+00
78	I	2016-07-14 14:34:42.845554+00	17	210	7	2016-07-18 13:00:00+00	2016-07-18 14:00:00+00
79	I	2016-07-14 14:34:42.845554+00	17	211	7	2016-07-18 16:00:00+00	2016-07-18 17:00:00+00
80	D	2016-07-14 14:34:48.705961+00	17	211	7	2016-07-18 16:00:00+00	2016-07-18 17:00:00+00
81	D	2016-07-14 14:34:48.705961+00	17	209	7	2016-07-18 07:00:00+00	2016-07-18 08:00:00+00
82	D	2016-07-14 14:34:48.705961+00	17	208	7	2016-07-18 10:00:00+00	2016-07-18 11:00:00+00
83	D	2016-07-14 14:34:48.705961+00	17	210	7	2016-07-18 13:00:00+00	2016-07-18 14:00:00+00
84	I	2016-07-14 15:56:18.902227+00	17	212	7	2016-07-15 10:00:00+00	2016-07-15 10:30:00+00
85	I	2016-07-14 15:56:28.603121+00	17	213	7	2016-07-16 10:00:00+00	2016-07-16 10:30:00+00
86	U	2016-07-14 16:00:11.560971+00	17	32	7	2016-07-15 21:00:00+00	2016-07-16 05:30:00+00
87	I	2016-07-14 16:00:18.566361+00	17	214	7	2016-07-16 05:30:00+00	2016-07-16 06:00:00+00
88	I	2016-07-14 16:04:19.784508+00	17	215	7	2016-07-17 15:00:00+00	2016-07-17 21:00:00+00
89	I	2016-07-14 16:04:41.647697+00	17	216	7	2016-07-18 00:00:00+00	2016-07-18 02:00:00+00
90	I	2016-07-14 16:04:48.733877+00	17	217	7	2016-07-18 15:00:00+00	2016-07-18 21:00:00+00
91	I	2016-07-14 16:04:50.86747+00	17	218	7	2016-07-18 10:00:00+00	2016-07-18 10:30:00+00
92	I	2016-07-14 16:05:21.244316+00	17	219	7	2016-07-18 21:00:00+00	2016-07-19 03:00:00+00
93	I	2016-07-14 16:05:21.244316+00	17	220	7	2016-07-19 15:00:00+00	2016-07-19 21:00:00+00
94	I	2016-07-14 16:05:23.075609+00	17	221	7	2016-07-19 10:00:00+00	2016-07-19 10:30:00+00
95	I	2016-07-14 16:05:55.711351+00	17	222	7	2016-07-19 21:00:00+00	2016-07-20 03:00:00+00
96	I	2016-07-14 16:05:55.711351+00	17	223	7	2016-07-20 15:00:00+00	2016-07-20 21:00:00+00
97	I	2016-07-14 16:05:57.287358+00	17	224	7	2016-07-20 10:00:00+00	2016-07-20 10:30:00+00
98	I	2016-07-14 16:07:09.016934+00	17	225	7	2016-07-21 15:00:00+00	2016-07-21 21:00:00+00
99	I	2016-07-14 16:07:09.016934+00	17	226	7	2016-07-20 21:00:00+00	2016-07-21 03:00:00+00
100	I	2016-07-14 16:07:10.655569+00	17	227	7	2016-07-21 10:00:00+00	2016-07-21 10:30:00+00
101	I	2016-07-14 16:09:40.797592+00	17	228	7	2016-07-21 21:00:00+00	2016-07-21 21:30:00+00
103	I	2016-07-14 16:10:42.146597+00	17	229	7	2016-07-13 05:30:00+00	2016-07-13 06:00:00+00
104	I	2016-07-14 16:11:06.026614+00	12	230	7	2016-07-14 07:00:00+00	2016-07-14 07:30:00+00
105	D	2016-07-14 16:11:08.755496+00	12	230	7	2016-07-14 07:00:00+00	2016-07-14 07:30:00+00
106	I	2016-07-14 16:11:42.70342+00	12	231	7	2016-07-14 07:00:00+00	2016-07-14 07:30:00+00
107	D	2016-07-14 16:11:44.652836+00	12	231	7	2016-07-14 07:00:00+00	2016-07-14 07:30:00+00
108	U	2016-07-18 05:45:02.564612+00	17	33	7	2016-07-16 21:00:00+00	2016-07-17 05:30:00+00
109	I	2016-07-18 05:45:04.515905+00	17	232	7	2016-07-17 05:30:00+00	2016-07-17 06:00:00+00
110	I	2016-07-18 09:07:47.585937+00	40	233	7	2016-07-17 10:00:00+00	2016-07-17 11:00:00+00
112	I	2016-07-18 09:08:49.99367+00	40	235	7	2016-07-19 10:30:00+00	2016-07-19 11:00:00+00
113	I	2016-07-18 09:08:49.99367+00	40	236	7	2016-07-19 03:00:00+00	2016-07-19 06:00:00+00
116	I	2016-07-18 09:09:09.585581+00	40	239	7	2016-07-21 03:00:00+00	2016-07-21 06:00:00+00
117	I	2016-07-18 09:09:09.585581+00	40	240	7	2016-07-21 10:30:00+00	2016-07-21 11:00:00+00
102	U	2016-07-14 16:10:32.495812+00	17	29	7	2016-07-12 21:00:00+00	2016-07-13 05:30:00+00
111	I	2016-07-18 09:07:54.068991+00	40	234	7	2016-07-18 10:30:00+00	2016-07-18 11:00:00+00
114	I	2016-07-18 09:08:58.285974+00	40	237	7	2016-07-20 03:00:00+00	2016-07-20 06:00:00+00
115	I	2016-07-18 09:08:58.285974+00	40	238	7	2016-07-20 10:30:00+00	2016-07-20 11:00:00+00
118	I	2016-07-18 09:09:39.006268+00	40	241	7	2016-07-21 21:30:00+00	2016-07-22 06:00:00+00
119	I	2016-07-18 09:09:39.006268+00	40	242	7	2016-07-22 10:30:00+00	2016-07-22 11:30:00+00
120	I	2016-07-18 09:09:39.006268+00	40	243	7	2016-07-22 15:00:00+00	2016-07-22 21:00:00+00
121	I	2016-07-18 09:09:54.408366+00	40	244	7	2016-07-22 21:00:00+00	2016-07-23 06:00:00+00
122	I	2016-07-18 09:09:54.408366+00	40	245	7	2016-07-23 15:00:00+00	2016-07-23 21:00:00+00
123	I	2016-07-18 09:10:11.699328+00	40	246	7	2016-07-24 10:00:00+00	2016-07-24 11:00:00+00
124	I	2016-07-18 09:10:11.699328+00	40	247	7	2016-07-23 21:00:00+00	2016-07-24 06:00:00+00
125	I	2016-07-18 09:10:11.699328+00	40	248	7	2016-07-24 15:00:00+00	2016-07-24 21:00:00+00
126	I	2016-07-18 09:10:42.975429+00	40	249	7	2016-07-24 21:00:00+00	2016-07-25 06:00:00+00
127	I	2016-07-18 09:10:42.975429+00	40	250	7	2016-07-25 10:00:00+00	2016-07-25 11:00:00+00
128	I	2016-07-18 09:10:42.975429+00	40	251	7	2016-07-25 15:00:00+00	2016-07-25 21:00:00+00
129	I	2016-07-18 09:11:06.932524+00	40	252	7	2016-07-26 10:00:00+00	2016-07-26 11:00:00+00
130	I	2016-07-18 09:11:06.932524+00	40	253	7	2016-07-26 15:00:00+00	2016-07-26 21:00:00+00
131	I	2016-07-18 09:11:06.932524+00	40	254	7	2016-07-25 21:00:00+00	2016-07-26 06:00:00+00
132	I	2016-07-18 09:11:33.270186+00	40	255	7	2016-07-26 21:00:00+00	2016-07-27 06:00:00+00
133	I	2016-07-18 09:11:33.270186+00	40	256	7	2016-07-27 10:00:00+00	2016-07-27 11:00:00+00
134	I	2016-07-18 09:11:33.270186+00	40	257	7	2016-07-27 15:00:00+00	2016-07-27 21:00:00+00
135	I	2016-07-18 09:11:56.919113+00	40	258	7	2016-07-27 21:00:00+00	2016-07-28 06:00:00+00
136	I	2016-07-18 09:11:56.919113+00	40	259	7	2016-07-28 10:00:00+00	2016-07-28 11:00:00+00
137	I	2016-07-18 09:11:56.919113+00	40	260	7	2016-07-28 15:00:00+00	2016-07-28 21:00:00+00
138	I	2016-07-18 09:12:33.26703+00	40	261	7	2016-07-28 21:00:00+00	2016-07-29 06:00:00+00
139	I	2016-07-18 09:12:33.26703+00	40	262	7	2016-07-29 10:00:00+00	2016-07-29 11:00:00+00
140	I	2016-07-18 09:12:33.26703+00	40	263	7	2016-07-29 15:00:00+00	2016-07-29 21:00:00+00
141	I	2016-07-18 09:13:00.379154+00	40	264	7	2016-07-30 10:00:00+00	2016-07-30 11:00:00+00
142	I	2016-07-18 09:13:00.379154+00	40	265	7	2016-07-29 21:00:00+00	2016-07-30 06:00:00+00
143	I	2016-07-18 09:13:00.379154+00	40	266	7	2016-07-30 15:00:00+00	2016-07-30 21:00:00+00
144	I	2016-07-18 09:13:18.575987+00	40	267	7	2016-07-30 21:00:00+00	2016-07-31 06:00:00+00
145	I	2016-07-18 09:13:18.575987+00	40	268	7	2016-07-31 15:00:00+00	2016-07-31 21:00:00+00
146	I	2016-07-18 09:13:18.575987+00	40	269	7	2016-07-31 10:00:00+00	2016-07-31 11:00:00+00
147	I	2016-07-18 09:13:39.148855+00	40	270	7	2016-08-01 10:00:00+00	2016-08-01 11:00:00+00
148	I	2016-07-18 09:13:39.148855+00	40	271	7	2016-07-31 21:00:00+00	2016-08-01 06:00:00+00
149	I	2016-07-18 09:13:39.148855+00	40	272	7	2016-08-01 15:00:00+00	2016-08-01 21:00:00+00
150	I	2016-07-18 09:45:25.270206+00	51	273	12	2016-07-18 05:00:00+00	2016-07-18 06:00:00+00
151	I	2016-07-18 09:45:47.470991+00	51	274	12	2016-07-19 05:00:00+00	2016-07-19 06:00:00+00
152	I	2016-07-18 09:45:47.470991+00	51	275	12	2016-07-18 16:00:00+00	2016-07-19 01:00:00+00
153	I	2016-07-18 09:45:47.470991+00	51	276	12	2016-07-19 10:00:00+00	2016-07-19 16:00:00+00
154	I	2016-07-18 09:46:11.814871+00	51	277	12	2016-07-20 10:00:00+00	2016-07-20 16:00:00+00
155	I	2016-07-18 09:46:11.814871+00	51	278	12	2016-07-19 16:00:00+00	2016-07-20 00:00:00+00
156	I	2016-07-18 09:46:11.814871+00	51	279	12	2016-07-20 05:00:00+00	2016-07-20 06:00:00+00
157	I	2016-07-18 09:46:11.814871+00	51	280	12	2016-07-20 00:30:00+00	2016-07-20 01:00:00+00
158	I	2016-07-18 09:46:14.800355+00	51	281	12	2016-07-20 00:00:00+00	2016-07-20 00:30:00+00
159	I	2016-07-18 09:46:33.219031+00	51	282	12	2016-07-21 05:00:00+00	2016-07-21 06:00:00+00
160	I	2016-07-18 09:46:33.219031+00	51	283	12	2016-07-20 16:00:00+00	2016-07-21 01:00:00+00
161	I	2016-07-18 09:46:33.219031+00	51	284	12	2016-07-21 10:00:00+00	2016-07-21 16:00:00+00
162	I	2016-07-18 09:46:54.044503+00	51	285	12	2016-07-21 16:00:00+00	2016-07-22 01:00:00+00
163	I	2016-07-18 09:46:54.044503+00	51	286	12	2016-07-22 05:00:00+00	2016-07-22 06:00:00+00
164	I	2016-07-18 09:46:54.044503+00	51	287	12	2016-07-22 10:00:00+00	2016-07-22 16:00:00+00
165	I	2016-07-18 09:47:13.177594+00	51	288	12	2016-07-23 10:00:00+00	2016-07-23 16:00:00+00
166	I	2016-07-18 09:47:13.177594+00	51	289	12	2016-07-23 05:00:00+00	2016-07-23 06:00:00+00
167	I	2016-07-18 09:47:13.177594+00	51	290	12	2016-07-22 16:00:00+00	2016-07-23 01:00:00+00
168	I	2016-07-18 09:47:28.73713+00	51	291	12	2016-07-23 16:00:00+00	2016-07-24 01:00:00+00
169	I	2016-07-18 09:47:28.73713+00	51	292	12	2016-07-24 05:00:00+00	2016-07-24 06:00:00+00
170	I	2016-07-18 09:47:28.73713+00	51	293	12	2016-07-24 10:00:00+00	2016-07-24 16:00:00+00
171	I	2016-07-18 09:47:45.916925+00	51	294	12	2016-07-24 16:00:00+00	2016-07-25 01:00:00+00
172	I	2016-07-18 09:47:45.916925+00	51	295	12	2016-07-25 05:00:00+00	2016-07-25 06:00:00+00
173	I	2016-07-18 09:47:45.916925+00	51	296	12	2016-07-25 10:00:00+00	2016-07-25 16:00:00+00
174	I	2016-07-18 09:48:08.86718+00	51	297	12	2016-07-25 16:00:00+00	2016-07-26 01:00:00+00
175	I	2016-07-18 09:48:08.86718+00	51	298	12	2016-07-26 10:00:00+00	2016-07-26 16:00:00+00
176	I	2016-07-18 09:48:08.86718+00	51	299	12	2016-07-26 05:00:00+00	2016-07-26 06:00:00+00
177	I	2016-07-18 09:48:29.91431+00	51	300	12	2016-07-26 16:00:00+00	2016-07-27 01:00:00+00
178	I	2016-07-18 09:48:29.91431+00	51	301	12	2016-07-27 05:00:00+00	2016-07-27 06:00:00+00
179	I	2016-07-18 09:48:29.91431+00	51	302	12	2016-07-27 10:00:00+00	2016-07-27 16:00:00+00
180	I	2016-07-18 09:48:46.779713+00	51	303	12	2016-07-28 05:00:00+00	2016-07-28 06:00:00+00
181	I	2016-07-18 09:48:46.779713+00	51	304	12	2016-07-28 10:00:00+00	2016-07-28 16:00:00+00
182	I	2016-07-18 09:48:46.779713+00	51	305	12	2016-07-27 16:00:00+00	2016-07-28 01:00:00+00
183	I	2016-07-18 09:49:03.892915+00	51	306	12	2016-07-29 10:00:00+00	2016-07-29 16:00:00+00
184	I	2016-07-18 09:49:03.892915+00	51	307	12	2016-07-29 05:00:00+00	2016-07-29 06:00:00+00
185	I	2016-07-18 09:49:03.892915+00	51	308	12	2016-07-28 16:00:00+00	2016-07-29 01:00:00+00
186	I	2016-07-18 09:49:30.237241+00	51	309	12	2016-07-29 16:00:00+00	2016-07-30 01:00:00+00
187	I	2016-07-18 09:49:30.237241+00	51	310	12	2016-07-30 05:00:00+00	2016-07-30 06:00:00+00
188	I	2016-07-18 09:49:30.237241+00	51	311	12	2016-07-30 10:00:00+00	2016-07-30 16:00:00+00
189	I	2016-07-18 09:50:04.293515+00	51	312	12	2016-07-30 16:00:00+00	2016-07-31 01:00:00+00
190	I	2016-07-18 09:50:04.293515+00	51	313	12	2016-07-31 05:00:00+00	2016-07-31 06:00:00+00
191	I	2016-07-18 09:50:04.293515+00	51	314	12	2016-07-31 10:00:00+00	2016-07-31 16:00:00+00
192	I	2016-07-18 09:50:54.96145+00	51	315	12	2016-08-01 05:00:00+00	2016-08-01 06:00:00+00
193	I	2016-07-18 09:50:54.96145+00	51	316	12	2016-07-31 16:00:00+00	2016-08-01 01:00:00+00
194	I	2016-07-18 09:50:54.96145+00	51	317	12	2016-08-01 10:00:00+00	2016-08-01 16:00:00+00
195	I	2016-07-18 10:05:41.046363+00	21	318	10	2016-07-18 10:00:00+00	2016-07-18 11:00:00+00
196	I	2016-07-18 10:05:41.046363+00	21	319	10	2016-07-18 15:00:00+00	2016-07-18 21:00:00+00
197	I	2016-07-18 10:05:59.801835+00	21	320	10	2016-07-18 21:00:00+00	2016-07-19 06:00:00+00
198	I	2016-07-18 10:05:59.801835+00	21	321	10	2016-07-19 15:00:00+00	2016-07-19 18:00:00+00
199	I	2016-07-18 10:05:59.801835+00	21	322	10	2016-07-19 10:00:00+00	2016-07-19 11:00:00+00
200	I	2016-07-18 10:05:59.801835+00	21	323	10	2016-07-19 18:30:00+00	2016-07-19 21:00:00+00
201	I	2016-07-18 10:06:01.973284+00	21	324	10	2016-07-19 18:00:00+00	2016-07-19 18:30:00+00
202	I	2016-07-18 10:06:21.034466+00	21	325	10	2016-07-20 15:00:00+00	2016-07-20 21:00:00+00
203	I	2016-07-18 10:06:21.034466+00	21	326	10	2016-07-19 21:00:00+00	2016-07-20 06:00:00+00
204	I	2016-07-18 10:06:21.034466+00	21	327	10	2016-07-20 10:00:00+00	2016-07-20 11:00:00+00
208	I	2016-07-18 10:07:13.710572+00	21	331	10	2016-07-21 21:00:00+00	2016-07-22 06:00:00+00
209	I	2016-07-18 10:07:13.710572+00	21	332	10	2016-07-22 10:00:00+00	2016-07-22 11:00:00+00
210	I	2016-07-18 10:07:13.710572+00	21	333	10	2016-07-22 15:00:00+00	2016-07-22 21:00:00+00
214	I	2016-07-18 10:07:46.747496+00	21	337	10	2016-07-24 15:00:00+00	2016-07-24 21:00:00+00
215	I	2016-07-18 10:07:46.747496+00	21	338	10	2016-07-23 21:00:00+00	2016-07-24 06:00:00+00
216	I	2016-07-18 10:07:46.747496+00	21	339	10	2016-07-24 10:00:00+00	2016-07-24 11:00:00+00
220	I	2016-07-18 10:08:31.220004+00	21	343	10	2016-07-26 10:00:00+00	2016-07-26 11:00:00+00
221	I	2016-07-18 10:08:31.220004+00	21	344	10	2016-07-26 15:00:00+00	2016-07-26 21:00:00+00
222	I	2016-07-18 10:08:31.220004+00	21	345	10	2016-07-25 21:00:00+00	2016-07-26 06:00:00+00
226	I	2016-07-18 10:14:01.510899+00	21	349	10	2016-07-28 10:00:00+00	2016-07-28 11:00:00+00
227	I	2016-07-18 10:14:01.510899+00	21	350	10	2016-07-27 21:00:00+00	2016-07-28 06:00:00+00
228	I	2016-07-18 10:14:01.510899+00	21	351	10	2016-07-28 15:00:00+00	2016-07-28 21:00:00+00
232	I	2016-07-18 10:14:35.041742+00	21	355	10	2016-07-29 21:00:00+00	2016-07-30 06:00:00+00
233	I	2016-07-18 10:14:35.041742+00	21	356	10	2016-07-30 10:00:00+00	2016-07-30 11:00:00+00
234	I	2016-07-18 10:14:35.041742+00	21	357	10	2016-07-30 15:00:00+00	2016-07-30 21:00:00+00
238	I	2016-07-18 10:15:14.202191+00	21	361	10	2016-07-31 21:00:00+00	2016-08-01 06:00:00+00
239	I	2016-07-18 10:15:14.202191+00	21	362	10	2016-08-01 15:00:00+00	2016-08-01 20:30:00+00
240	I	2016-07-18 10:15:14.202191+00	21	363	10	2016-08-01 10:00:00+00	2016-08-01 11:00:00+00
241	I	2016-07-18 10:15:15.735825+00	21	364	10	2016-08-01 20:30:00+00	2016-08-01 21:00:00+00
242	I	2016-07-18 10:21:35.125654+00	19	365	9	2016-07-18 10:00:00+00	2016-07-18 11:00:00+00
243	I	2016-07-18 10:21:35.125654+00	19	366	9	2016-07-18 15:00:00+00	2016-07-18 21:00:00+00
247	I	2016-07-18 10:22:11.667326+00	19	370	9	2016-07-20 15:00:00+00	2016-07-20 21:00:00+00
248	I	2016-07-18 10:22:11.667326+00	19	371	9	2016-07-20 10:00:00+00	2016-07-20 11:00:00+00
249	I	2016-07-18 10:22:11.667326+00	19	372	9	2016-07-19 21:00:00+00	2016-07-20 06:00:00+00
253	I	2016-07-18 10:22:46.754795+00	19	376	9	2016-07-22 10:00:00+00	2016-07-22 11:00:00+00
254	I	2016-07-18 10:22:46.754795+00	19	377	9	2016-07-22 15:00:00+00	2016-07-22 21:00:00+00
255	I	2016-07-18 10:22:46.754795+00	19	378	9	2016-07-21 21:00:00+00	2016-07-22 06:00:00+00
205	I	2016-07-18 10:06:52.654051+00	21	328	10	2016-07-21 10:00:00+00	2016-07-21 11:00:00+00
206	I	2016-07-18 10:06:52.654051+00	21	329	10	2016-07-21 15:00:00+00	2016-07-21 21:00:00+00
207	I	2016-07-18 10:06:52.654051+00	21	330	10	2016-07-20 21:00:00+00	2016-07-21 06:00:00+00
211	I	2016-07-18 10:07:29.651068+00	21	334	10	2016-07-23 10:00:00+00	2016-07-23 11:00:00+00
212	I	2016-07-18 10:07:29.651068+00	21	335	10	2016-07-23 15:00:00+00	2016-07-23 21:00:00+00
213	I	2016-07-18 10:07:29.651068+00	21	336	10	2016-07-22 21:00:00+00	2016-07-23 06:00:00+00
217	I	2016-07-18 10:08:09.668331+00	21	340	10	2016-07-25 10:00:00+00	2016-07-25 11:00:00+00
218	I	2016-07-18 10:08:09.668331+00	21	341	10	2016-07-24 21:00:00+00	2016-07-25 06:00:00+00
219	I	2016-07-18 10:08:09.668331+00	21	342	10	2016-07-25 15:00:00+00	2016-07-25 21:00:00+00
223	I	2016-07-18 10:13:44.975185+00	21	346	10	2016-07-26 21:00:00+00	2016-07-27 06:00:00+00
224	I	2016-07-18 10:13:44.975185+00	21	347	10	2016-07-27 10:00:00+00	2016-07-27 11:00:00+00
225	I	2016-07-18 10:13:44.975185+00	21	348	10	2016-07-27 15:00:00+00	2016-07-27 21:00:00+00
229	I	2016-07-18 10:14:16.314635+00	21	352	10	2016-07-29 15:00:00+00	2016-07-29 21:00:00+00
230	I	2016-07-18 10:14:16.314635+00	21	353	10	2016-07-29 10:00:00+00	2016-07-29 11:00:00+00
231	I	2016-07-18 10:14:16.314635+00	21	354	10	2016-07-28 21:00:00+00	2016-07-29 06:00:00+00
235	I	2016-07-18 10:14:55.237249+00	21	358	10	2016-07-30 21:00:00+00	2016-07-31 06:00:00+00
236	I	2016-07-18 10:14:55.237249+00	21	359	10	2016-07-31 10:00:00+00	2016-07-31 11:00:00+00
237	I	2016-07-18 10:14:55.237249+00	21	360	10	2016-07-31 15:00:00+00	2016-07-31 21:00:00+00
244	I	2016-07-18 10:21:53.517988+00	19	367	9	2016-07-19 10:00:00+00	2016-07-19 11:00:00+00
245	I	2016-07-18 10:21:53.517988+00	19	368	9	2016-07-18 21:00:00+00	2016-07-19 06:00:00+00
246	I	2016-07-18 10:21:53.517988+00	19	369	9	2016-07-19 15:00:00+00	2016-07-19 21:00:00+00
250	I	2016-07-18 10:22:30.235671+00	19	373	9	2016-07-20 21:00:00+00	2016-07-21 06:00:00+00
251	I	2016-07-18 10:22:30.235671+00	19	374	9	2016-07-21 15:00:00+00	2016-07-21 21:00:00+00
252	I	2016-07-18 10:22:30.235671+00	19	375	9	2016-07-21 10:00:00+00	2016-07-21 11:00:00+00
256	I	2016-07-18 10:23:05.636577+00	19	379	9	2016-07-23 15:00:00+00	2016-07-23 21:00:00+00
257	I	2016-07-18 10:23:05.636577+00	19	380	9	2016-07-22 21:00:00+00	2016-07-23 06:00:00+00
258	I	2016-07-18 10:23:05.636577+00	19	381	9	2016-07-23 10:00:00+00	2016-07-23 11:00:00+00
259	I	2016-07-18 10:23:22.293714+00	19	382	9	2016-07-23 21:00:00+00	2016-07-24 06:00:00+00
260	I	2016-07-18 10:23:22.293714+00	19	383	9	2016-07-24 10:00:00+00	2016-07-24 11:00:00+00
261	I	2016-07-18 10:23:22.293714+00	19	384	9	2016-07-24 15:00:00+00	2016-07-24 21:00:00+00
262	I	2016-07-18 10:23:38.872825+00	19	385	9	2016-07-24 21:00:00+00	2016-07-25 06:00:00+00
263	I	2016-07-18 10:23:38.872825+00	19	386	9	2016-07-25 10:00:00+00	2016-07-25 11:00:00+00
264	I	2016-07-18 10:23:38.872825+00	19	387	9	2016-07-25 15:00:00+00	2016-07-25 21:00:00+00
265	I	2016-07-18 10:23:53.23209+00	19	388	9	2016-07-26 15:00:00+00	2016-07-26 21:00:00+00
266	I	2016-07-18 10:23:53.23209+00	19	389	9	2016-07-25 21:00:00+00	2016-07-26 06:00:00+00
267	I	2016-07-18 10:23:53.23209+00	19	390	9	2016-07-26 10:00:00+00	2016-07-26 11:00:00+00
268	I	2016-07-18 10:24:26.661512+00	19	391	9	2016-07-27 10:00:00+00	2016-07-27 11:00:00+00
269	I	2016-07-18 10:24:26.661512+00	19	392	9	2016-07-27 00:00:00+00	2016-07-27 06:00:00+00
270	I	2016-07-18 10:24:26.661512+00	19	393	9	2016-07-26 21:00:00+00	2016-07-26 23:30:00+00
271	I	2016-07-18 10:24:26.661512+00	19	394	9	2016-07-27 15:00:00+00	2016-07-27 21:00:00+00
272	I	2016-07-18 10:24:43.511328+00	19	395	9	2016-07-28 15:00:00+00	2016-07-28 21:00:00+00
273	I	2016-07-18 10:24:43.511328+00	19	396	9	2016-07-27 21:00:00+00	2016-07-28 06:00:00+00
274	I	2016-07-18 10:24:43.511328+00	19	397	9	2016-07-28 10:00:00+00	2016-07-28 11:00:00+00
275	I	2016-07-18 10:25:02.029565+00	19	398	9	2016-07-28 21:00:00+00	2016-07-29 06:00:00+00
276	I	2016-07-18 10:25:02.029565+00	19	399	9	2016-07-29 10:00:00+00	2016-07-29 11:00:00+00
277	I	2016-07-18 10:25:02.029565+00	19	400	9	2016-07-29 15:00:00+00	2016-07-29 21:00:00+00
278	I	2016-07-18 10:29:08.629263+00	19	401	9	2016-07-29 21:00:00+00	2016-07-30 06:00:00+00
279	I	2016-07-18 10:29:08.629263+00	19	402	9	2016-07-30 15:00:00+00	2016-07-30 21:00:00+00
280	I	2016-07-18 10:29:08.629263+00	19	403	9	2016-07-30 10:00:00+00	2016-07-30 11:00:00+00
281	I	2016-07-18 10:29:24.020689+00	19	404	9	2016-07-30 21:00:00+00	2016-07-31 02:00:00+00
282	I	2016-07-18 10:29:24.020689+00	19	405	9	2016-07-31 15:00:00+00	2016-07-31 21:00:00+00
283	I	2016-07-18 10:29:24.020689+00	19	406	9	2016-07-31 02:30:00+00	2016-07-31 06:00:00+00
284	I	2016-07-18 10:29:24.020689+00	19	407	9	2016-07-31 10:00:00+00	2016-07-31 11:00:00+00
285	I	2016-07-18 10:29:30.399294+00	19	408	9	2016-07-31 02:00:00+00	2016-07-31 02:30:00+00
286	I	2016-07-18 10:29:47.398438+00	19	409	9	2016-08-01 10:00:00+00	2016-08-01 11:00:00+00
287	I	2016-07-18 10:29:47.398438+00	19	410	9	2016-07-31 21:00:00+00	2016-08-01 06:00:00+00
288	I	2016-07-18 10:29:47.398438+00	19	411	9	2016-08-01 15:00:00+00	2016-08-01 21:00:00+00
289	I	2016-07-18 10:44:07.60524+00	18	415	8	2016-07-18 15:00:00+00	2016-07-18 21:00:00+00
290	I	2016-07-18 10:44:07.60524+00	18	416	8	2016-07-18 10:00:00+00	2016-07-18 11:00:00+00
291	I	2016-07-18 10:46:29.429289+00	18	418	8	2016-07-18 21:00:00+00	2016-07-19 06:00:00+00
292	I	2016-07-18 10:46:29.429289+00	18	419	8	2016-07-19 15:00:00+00	2016-07-19 21:00:00+00
293	I	2016-07-18 10:46:29.429289+00	18	420	8	2016-07-19 10:00:00+00	2016-07-19 11:00:00+00
294	I	2016-07-18 10:46:45.699986+00	18	421	8	2016-07-19 21:00:00+00	2016-07-20 06:00:00+00
295	I	2016-07-18 10:46:45.699986+00	18	422	8	2016-07-20 10:00:00+00	2016-07-20 11:00:00+00
296	I	2016-07-18 10:46:45.699986+00	18	423	8	2016-07-20 15:00:00+00	2016-07-20 21:00:00+00
297	I	2016-07-18 10:46:59.631186+00	18	424	8	2016-07-21 15:00:00+00	2016-07-21 21:00:00+00
298	I	2016-07-18 10:46:59.631186+00	18	425	8	2016-07-20 21:00:00+00	2016-07-21 06:00:00+00
299	I	2016-07-18 10:46:59.631186+00	18	426	8	2016-07-21 10:00:00+00	2016-07-21 11:00:00+00
300	I	2016-07-18 10:47:36.844668+00	18	427	8	2016-07-21 21:00:00+00	2016-07-22 06:00:00+00
301	I	2016-07-18 10:47:36.844668+00	18	428	8	2016-07-22 10:00:00+00	2016-07-22 11:00:00+00
302	I	2016-07-18 10:47:36.844668+00	18	429	8	2016-07-22 15:00:00+00	2016-07-22 21:00:00+00
303	I	2016-07-18 10:47:52.879534+00	18	430	8	2016-07-22 21:00:00+00	2016-07-23 06:00:00+00
304	I	2016-07-18 10:47:52.879534+00	18	431	8	2016-07-23 10:00:00+00	2016-07-23 11:00:00+00
305	I	2016-07-18 10:47:52.879534+00	18	432	8	2016-07-23 15:00:00+00	2016-07-23 21:00:00+00
306	I	2016-07-18 10:48:11.373407+00	18	433	8	2016-07-23 21:00:00+00	2016-07-24 06:00:00+00
307	I	2016-07-18 10:48:11.373407+00	18	434	8	2016-07-24 15:00:00+00	2016-07-24 21:00:00+00
308	I	2016-07-18 10:48:11.373407+00	18	435	8	2016-07-24 10:00:00+00	2016-07-24 11:00:00+00
309	I	2016-07-18 10:48:25.547567+00	18	436	8	2016-07-25 10:00:00+00	2016-07-25 11:00:00+00
310	I	2016-07-18 10:48:25.547567+00	18	437	8	2016-07-24 21:00:00+00	2016-07-25 06:00:00+00
311	I	2016-07-18 10:48:25.547567+00	18	438	8	2016-07-25 15:00:00+00	2016-07-25 21:00:00+00
312	I	2016-07-18 10:48:43.135662+00	18	439	8	2016-07-25 21:00:00+00	2016-07-26 06:00:00+00
313	I	2016-07-18 10:48:43.135662+00	18	440	8	2016-07-26 10:00:00+00	2016-07-26 11:00:00+00
314	I	2016-07-18 10:48:43.135662+00	18	441	8	2016-07-26 15:00:00+00	2016-07-26 21:00:00+00
315	I	2016-07-18 10:48:58.277173+00	18	442	8	2016-07-27 10:00:00+00	2016-07-27 11:00:00+00
316	I	2016-07-18 10:48:58.277173+00	18	443	8	2016-07-26 21:00:00+00	2016-07-27 06:00:00+00
317	I	2016-07-18 10:48:58.277173+00	18	444	8	2016-07-27 15:00:00+00	2016-07-27 21:00:00+00
318	I	2016-07-18 10:49:13.673606+00	18	445	8	2016-07-28 10:00:00+00	2016-07-28 11:00:00+00
319	I	2016-07-18 10:49:13.673606+00	18	446	8	2016-07-28 15:00:00+00	2016-07-28 21:00:00+00
320	I	2016-07-18 10:49:13.673606+00	18	447	8	2016-07-27 21:00:00+00	2016-07-28 06:00:00+00
324	I	2016-07-18 10:49:50.413309+00	18	451	8	2016-07-30 15:00:00+00	2016-07-30 21:00:00+00
325	I	2016-07-18 10:49:50.413309+00	18	452	8	2016-07-29 21:00:00+00	2016-07-30 06:00:00+00
326	I	2016-07-18 10:49:50.413309+00	18	453	8	2016-07-30 10:00:00+00	2016-07-30 11:00:00+00
330	I	2016-07-18 10:50:29.014684+00	18	457	8	2016-08-01 15:00:00+00	2016-08-01 21:00:00+00
331	I	2016-07-18 10:50:29.014684+00	18	458	8	2016-07-31 21:00:00+00	2016-08-01 06:00:00+00
332	I	2016-07-18 10:50:29.014684+00	18	459	8	2016-08-01 10:00:00+00	2016-08-01 11:00:00+00
333	I	2016-07-18 10:52:16.126746+00	16	460	11	2016-07-18 08:00:00+00	2016-07-18 09:00:00+00
337	I	2016-07-18 10:52:55.453598+00	16	464	11	2016-07-20 08:00:00+00	2016-07-20 09:00:00+00
338	I	2016-07-18 10:52:55.453598+00	16	465	11	2016-07-19 19:00:00+00	2016-07-20 04:00:00+00
339	I	2016-07-18 10:52:55.453598+00	16	466	11	2016-07-20 13:00:00+00	2016-07-20 19:00:00+00
321	I	2016-07-18 10:49:27.253056+00	18	448	8	2016-07-29 15:00:00+00	2016-07-29 21:00:00+00
322	I	2016-07-18 10:49:27.253056+00	18	449	8	2016-07-28 21:00:00+00	2016-07-29 06:00:00+00
323	I	2016-07-18 10:49:27.253056+00	18	450	8	2016-07-29 10:00:00+00	2016-07-29 11:00:00+00
327	I	2016-07-18 10:50:10.895119+00	18	454	8	2016-07-30 21:00:00+00	2016-07-31 06:00:00+00
328	I	2016-07-18 10:50:10.895119+00	18	455	8	2016-07-31 15:00:00+00	2016-07-31 21:00:00+00
329	I	2016-07-18 10:50:10.895119+00	18	456	8	2016-07-31 10:00:00+00	2016-07-31 11:00:00+00
334	I	2016-07-18 10:52:30.987459+00	16	461	11	2016-07-19 08:00:00+00	2016-07-19 09:00:00+00
335	I	2016-07-18 10:52:30.987459+00	16	462	11	2016-07-19 13:00:00+00	2016-07-19 19:00:00+00
336	I	2016-07-18 10:52:30.987459+00	16	463	11	2016-07-18 19:00:00+00	2016-07-19 04:00:00+00
340	I	2016-07-18 10:53:15.144028+00	16	467	11	2016-07-21 08:00:00+00	2016-07-21 09:00:00+00
341	I	2016-07-18 10:53:15.144028+00	16	468	11	2016-07-21 13:00:00+00	2016-07-21 19:00:00+00
342	I	2016-07-18 10:53:15.144028+00	16	469	11	2016-07-20 19:00:00+00	2016-07-21 04:00:00+00
343	I	2016-07-18 10:53:31.467712+00	16	470	11	2016-07-22 08:00:00+00	2016-07-22 09:00:00+00
344	I	2016-07-18 10:53:31.467712+00	16	471	11	2016-07-21 19:00:00+00	2016-07-22 04:00:00+00
345	I	2016-07-18 10:53:31.467712+00	16	472	11	2016-07-22 13:00:00+00	2016-07-22 19:00:00+00
346	I	2016-07-18 10:53:50.604809+00	16	473	11	2016-07-23 13:00:00+00	2016-07-23 19:00:00+00
347	I	2016-07-18 10:53:50.604809+00	16	474	11	2016-07-22 19:00:00+00	2016-07-23 04:00:00+00
348	I	2016-07-18 10:53:50.604809+00	16	475	11	2016-07-23 08:00:00+00	2016-07-23 09:00:00+00
349	I	2016-07-18 10:54:05.224458+00	16	476	11	2016-07-23 19:00:00+00	2016-07-24 04:00:00+00
350	I	2016-07-18 10:54:05.224458+00	16	477	11	2016-07-24 08:00:00+00	2016-07-24 09:00:00+00
351	I	2016-07-18 10:54:05.224458+00	16	478	11	2016-07-24 13:00:00+00	2016-07-24 19:00:00+00
352	I	2016-07-18 10:54:22.722814+00	16	479	11	2016-07-25 08:00:00+00	2016-07-25 09:00:00+00
353	I	2016-07-18 10:54:22.722814+00	16	480	11	2016-07-24 19:00:00+00	2016-07-25 04:00:00+00
354	I	2016-07-18 10:54:22.722814+00	16	481	11	2016-07-25 13:00:00+00	2016-07-25 19:00:00+00
355	I	2016-07-18 10:54:37.645405+00	16	482	11	2016-07-25 19:00:00+00	2016-07-26 04:00:00+00
356	I	2016-07-18 10:54:37.645405+00	16	483	11	2016-07-26 13:00:00+00	2016-07-26 19:00:00+00
357	I	2016-07-18 10:54:37.645405+00	16	484	11	2016-07-26 08:00:00+00	2016-07-26 09:00:00+00
358	I	2016-07-18 10:54:52.02137+00	16	485	11	2016-07-26 19:00:00+00	2016-07-27 04:00:00+00
359	I	2016-07-18 10:54:52.02137+00	16	486	11	2016-07-27 08:00:00+00	2016-07-27 09:00:00+00
360	I	2016-07-18 10:54:52.02137+00	16	487	11	2016-07-27 13:00:00+00	2016-07-27 19:00:00+00
361	I	2016-07-18 10:55:06.804026+00	16	488	11	2016-07-28 13:00:00+00	2016-07-28 19:00:00+00
362	I	2016-07-18 10:55:06.804026+00	16	489	11	2016-07-27 19:00:00+00	2016-07-28 04:00:00+00
363	I	2016-07-18 10:55:06.804026+00	16	490	11	2016-07-28 08:00:00+00	2016-07-28 09:00:00+00
364	I	2016-07-18 10:55:24.920318+00	16	491	11	2016-07-29 13:00:00+00	2016-07-29 19:00:00+00
365	I	2016-07-18 10:55:24.920318+00	16	492	11	2016-07-28 19:00:00+00	2016-07-29 04:00:00+00
366	I	2016-07-18 10:55:24.920318+00	16	493	11	2016-07-29 08:00:00+00	2016-07-29 09:00:00+00
367	I	2016-07-18 10:55:48.718424+00	16	494	11	2016-07-30 08:00:00+00	2016-07-30 09:00:00+00
368	I	2016-07-18 10:55:48.718424+00	16	495	11	2016-07-29 19:00:00+00	2016-07-30 04:00:00+00
369	I	2016-07-18 10:55:48.718424+00	16	496	11	2016-07-30 13:00:00+00	2016-07-30 19:00:00+00
370	I	2016-07-18 10:56:04.317765+00	16	497	11	2016-07-31 08:00:00+00	2016-07-31 09:00:00+00
371	I	2016-07-18 10:56:04.317765+00	16	498	11	2016-07-31 13:00:00+00	2016-07-31 19:00:00+00
372	I	2016-07-18 10:56:04.317765+00	16	499	11	2016-07-30 19:00:00+00	2016-07-31 04:00:00+00
373	I	2016-07-18 10:56:19.957127+00	16	500	11	2016-07-31 19:00:00+00	2016-08-01 04:00:00+00
374	I	2016-07-18 10:56:19.957127+00	16	501	11	2016-08-01 08:00:00+00	2016-08-01 09:00:00+00
375	I	2016-07-18 10:56:19.957127+00	16	502	11	2016-08-01 13:00:00+00	2016-08-01 19:00:00+00
376	I	2016-07-18 11:09:13.883117+00	20	503	13	2016-07-18 06:00:00+00	2016-07-18 07:00:00+00
377	I	2016-07-18 11:09:13.883117+00	20	504	13	2016-07-18 11:00:00+00	2016-07-18 17:00:00+00
378	I	2016-07-18 11:09:29.823405+00	20	505	13	2016-07-18 17:00:00+00	2016-07-19 02:00:00+00
379	I	2016-07-18 11:09:29.823405+00	20	506	13	2016-07-19 06:00:00+00	2016-07-19 07:00:00+00
380	I	2016-07-18 11:09:29.823405+00	20	507	13	2016-07-19 11:00:00+00	2016-07-19 17:00:00+00
381	I	2016-07-18 11:09:42.867081+00	20	508	13	2016-07-19 17:00:00+00	2016-07-20 02:00:00+00
382	I	2016-07-18 11:09:42.867081+00	20	509	13	2016-07-20 11:00:00+00	2016-07-20 17:00:00+00
383	I	2016-07-18 11:09:42.867081+00	20	510	13	2016-07-20 06:00:00+00	2016-07-20 07:00:00+00
384	I	2016-07-18 11:09:57.168082+00	20	511	13	2016-07-21 11:00:00+00	2016-07-21 17:00:00+00
385	I	2016-07-18 11:09:57.168082+00	20	512	13	2016-07-21 06:00:00+00	2016-07-21 07:00:00+00
386	I	2016-07-18 11:09:57.168082+00	20	513	13	2016-07-20 17:00:00+00	2016-07-21 02:00:00+00
387	I	2016-07-18 11:10:12.547508+00	20	514	13	2016-07-22 11:00:00+00	2016-07-22 17:00:00+00
388	I	2016-07-18 11:10:12.547508+00	20	515	13	2016-07-21 17:00:00+00	2016-07-22 02:00:00+00
389	I	2016-07-18 11:10:12.547508+00	20	516	13	2016-07-22 06:00:00+00	2016-07-22 07:00:00+00
390	I	2016-07-18 11:10:31.105002+00	20	517	13	2016-07-23 11:00:00+00	2016-07-23 17:00:00+00
391	I	2016-07-18 11:10:31.105002+00	20	518	13	2016-07-22 17:00:00+00	2016-07-23 02:00:00+00
392	I	2016-07-18 11:10:31.105002+00	20	519	13	2016-07-23 06:00:00+00	2016-07-23 07:00:00+00
393	I	2016-07-18 11:11:05.891092+00	20	520	13	2016-07-24 11:00:00+00	2016-07-24 17:00:00+00
394	I	2016-07-18 11:11:05.891092+00	20	521	13	2016-07-23 17:00:00+00	2016-07-24 02:00:00+00
395	I	2016-07-18 11:11:05.891092+00	20	522	13	2016-07-24 06:00:00+00	2016-07-24 07:00:00+00
396	I	2016-07-18 11:11:27.802891+00	20	523	13	2016-07-25 11:00:00+00	2016-07-25 17:00:00+00
397	I	2016-07-18 11:11:27.802891+00	20	524	13	2016-07-25 06:00:00+00	2016-07-25 07:00:00+00
398	I	2016-07-18 11:11:27.802891+00	20	525	13	2016-07-24 17:00:00+00	2016-07-25 02:00:00+00
399	I	2016-07-18 11:11:46.473373+00	20	526	13	2016-07-25 17:00:00+00	2016-07-26 02:00:00+00
400	I	2016-07-18 11:11:46.473373+00	20	527	13	2016-07-26 06:00:00+00	2016-07-26 07:00:00+00
401	I	2016-07-18 11:11:46.473373+00	20	528	13	2016-07-26 11:00:00+00	2016-07-26 17:00:00+00
402	I	2016-07-18 11:12:02.61285+00	20	529	13	2016-07-27 06:00:00+00	2016-07-27 07:00:00+00
403	I	2016-07-18 11:12:02.61285+00	20	530	13	2016-07-26 17:00:00+00	2016-07-27 02:00:00+00
404	I	2016-07-18 11:12:02.61285+00	20	531	13	2016-07-27 11:00:00+00	2016-07-27 17:00:00+00
405	I	2016-07-18 11:12:30.076477+00	20	532	13	2016-07-27 17:00:00+00	2016-07-28 02:00:00+00
406	I	2016-07-18 11:12:30.076477+00	20	533	13	2016-07-28 11:00:00+00	2016-07-28 17:00:00+00
407	I	2016-07-18 11:12:30.076477+00	20	534	13	2016-07-28 06:00:00+00	2016-07-28 07:00:00+00
408	I	2016-07-18 11:12:44.902753+00	20	535	13	2016-07-28 17:00:00+00	2016-07-29 02:00:00+00
409	I	2016-07-18 11:12:44.902753+00	20	536	13	2016-07-29 06:00:00+00	2016-07-29 07:00:00+00
410	I	2016-07-18 11:12:44.902753+00	20	537	13	2016-07-29 11:00:00+00	2016-07-29 17:00:00+00
411	I	2016-07-18 11:13:02.463708+00	20	538	13	2016-07-30 11:00:00+00	2016-07-30 17:00:00+00
412	I	2016-07-18 11:13:02.463708+00	20	539	13	2016-07-29 17:00:00+00	2016-07-30 02:00:00+00
413	I	2016-07-18 11:13:02.463708+00	20	540	13	2016-07-30 06:00:00+00	2016-07-30 07:00:00+00
414	I	2016-07-18 11:13:22.038365+00	20	541	13	2016-07-31 11:00:00+00	2016-07-31 17:00:00+00
415	I	2016-07-18 11:13:22.038365+00	20	542	13	2016-07-30 17:00:00+00	2016-07-31 02:00:00+00
416	I	2016-07-18 11:13:22.038365+00	20	543	13	2016-07-31 06:00:00+00	2016-07-31 07:00:00+00
417	I	2016-07-18 11:13:38.918199+00	20	544	13	2016-07-31 17:00:00+00	2016-08-01 02:00:00+00
418	I	2016-07-18 11:13:38.918199+00	20	545	13	2016-08-01 06:00:00+00	2016-08-01 07:00:00+00
419	I	2016-07-18 11:13:38.918199+00	20	546	13	2016-08-01 11:00:00+00	2016-08-01 17:00:00+00
421	I	2016-07-19 09:24:02.40633+00	12	548	7	2016-07-21 11:00:00+00	2016-07-21 12:00:00+00
422	D	2016-07-19 09:24:07.014659+00	12	548	7	2016-07-21 11:00:00+00	2016-07-21 12:00:00+00
423	I	2016-07-19 09:24:28.661874+00	12	549	7	2016-07-21 11:00:00+00	2016-07-21 12:00:00+00
424	D	2016-07-19 09:24:35.748124+00	12	549	7	2016-07-21 11:00:00+00	2016-07-21 12:00:00+00
426	I	2016-07-19 09:26:22.142659+00	12	551	7	2016-07-21 11:00:00+00	2016-07-21 12:00:00+00
427	D	2016-07-19 09:26:25.369783+00	12	551	7	2016-07-21 11:00:00+00	2016-07-21 12:00:00+00
428	I	2016-07-19 09:27:49.94574+00	12	552	7	2016-07-21 11:00:00+00	2016-07-21 12:00:00+00
429	D	2016-07-19 09:28:35.250494+00	12	552	7	2016-07-21 11:00:00+00	2016-07-21 12:00:00+00
430	I	2016-07-19 09:35:12.01786+00	12	553	7	2016-07-21 11:00:00+00	2016-07-21 11:30:00+00
431	D	2016-07-19 09:35:19.483652+00	12	553	7	2016-07-21 11:00:00+00	2016-07-21 11:30:00+00
433	I	2016-07-20 09:21:09.953448+00	12	555	7	2016-07-21 09:00:00+00	2016-07-21 10:00:00+00
434	I	2016-07-21 09:17:38.212968+00	12	556	7	2016-07-21 11:00:00+00	2016-07-21 12:00:00+00
435	I	2016-07-21 09:53:52.008757+00	53	557	11	2016-07-21 09:00:00+00	2016-07-21 10:00:00+00
436	D	2016-07-21 09:53:54.145711+00	53	557	11	2016-07-21 09:00:00+00	2016-07-21 10:00:00+00
437	U	2016-07-22 09:17:17.863058+00	40	270	7	2016-08-01 10:30:00+00	2016-08-01 11:00:00+00
438	I	2016-07-22 09:17:21.233066+00	40	558	7	2016-08-01 10:00:00+00	2016-08-01 10:30:00+00
440	I	2016-07-27 09:00:53.971644+00	12	560	7	2016-07-28 09:00:00+00	2016-07-28 10:00:00+00
441	I	2016-07-28 09:28:03.152018+00	57	561	7	2016-08-01 09:00:00+00	2016-08-01 10:00:00+00
442	I	2016-07-28 13:25:28.361821+00	53	562	7	2016-07-29 07:30:00+00	2016-07-29 08:00:00+00
443	I	2016-08-01 10:13:17.396213+00	12	563	7	2016-08-01 07:30:00+00	2016-08-01 09:00:00+00
444	D	2016-08-01 10:14:38.588446+00	12	563	7	2016-08-01 07:30:00+00	2016-08-01 09:00:00+00
445	I	2016-08-02 09:25:22.860618+00	59	564	7	2016-08-02 01:30:00+00	2016-08-02 02:30:00+00
446	D	2016-08-02 09:25:25.802383+00	59	564	7	2016-08-02 01:30:00+00	2016-08-02 02:30:00+00
447	I	2016-08-02 09:32:09.368667+00	60	565	7	2016-08-02 01:30:00+00	2016-08-02 02:30:00+00
448	D	2016-08-02 09:32:11.674057+00	60	565	7	2016-08-02 01:30:00+00	2016-08-02 02:30:00+00
449	I	2016-08-02 09:40:50.392693+00	61	566	7	2016-08-02 04:30:00+00	2016-08-02 05:30:00+00
450	D	2016-08-02 09:40:53.791391+00	61	566	7	2016-08-02 04:30:00+00	2016-08-02 05:30:00+00
451	I	2016-08-02 09:56:24.233893+00	17	567	7	2016-08-02 15:00:00+00	2016-08-02 21:00:00+00
452	I	2016-08-02 09:56:24.233893+00	17	568	7	2016-08-02 10:00:00+00	2016-08-02 11:00:00+00
453	I	2016-08-02 09:56:24.233893+00	17	569	7	2016-08-01 21:00:00+00	2016-08-02 06:00:00+00
454	I	2016-08-02 09:56:47.736773+00	17	570	7	2016-08-02 21:00:00+00	2016-08-03 06:00:00+00
455	I	2016-08-02 09:56:47.736773+00	17	571	7	2016-08-03 15:00:00+00	2016-08-03 21:00:00+00
456	I	2016-08-02 09:56:47.736773+00	17	572	7	2016-08-03 10:00:00+00	2016-08-03 11:00:00+00
457	I	2016-08-02 10:27:01.989026+00	63	798	7	2016-08-02 07:30:00+00	2016-08-02 08:00:00+00
458	D	2016-08-02 10:27:04.820526+00	63	798	7	2016-08-02 07:30:00+00	2016-08-02 08:00:00+00
459	I	2016-08-02 10:43:24.434101+00	65	799	7	2016-08-02 07:30:00+00	2016-08-02 08:00:00+00
460	D	2016-08-02 10:43:27.203733+00	65	799	7	2016-08-02 07:30:00+00	2016-08-02 08:00:00+00
461	I	2016-08-02 11:05:54.366183+00	68	800	12	2016-08-01 23:30:00+00	2016-08-02 00:30:00+00
462	D	2016-08-02 11:05:56.315205+00	68	800	12	2016-08-01 23:30:00+00	2016-08-02 00:30:00+00
463	I	2016-08-03 08:01:57.413198+00	12	801	7	2016-08-04 09:00:00+00	2016-08-04 10:00:00+00
464	I	2016-08-03 08:03:51.275783+00	12	802	7	2016-08-04 10:30:00+00	2016-08-04 11:00:00+00
465	I	2016-08-03 13:13:51.107528+00	12	803	7	2016-08-04 08:00:00+00	2016-08-04 09:00:00+00
466	D	2016-08-03 13:13:55.596943+00	12	802	7	2016-08-04 10:30:00+00	2016-08-04 11:00:00+00
468	I	2016-08-03 14:48:06.92731+00	53	805	7	2016-08-04 07:30:00+00	2016-08-04 08:00:00+00
469	D	2016-08-04 07:12:33.274688+00	53	805	7	2016-08-04 07:30:00+00	2016-08-04 08:00:00+00
470	I	2016-08-04 07:21:25.372286+00	53	806	7	2016-08-04 12:30:00+00	2016-08-04 13:00:00+00
475	I	2016-08-05 11:00:10.573483+00	69	811	7	2016-08-08 06:00:00+00	2016-08-08 07:00:00+00
476	I	2016-08-05 12:03:39.372464+00	66	812	10	2016-08-05 09:00:00+00	2016-08-05 09:30:00+00
477	D	2016-08-05 12:04:06.207694+00	66	812	10	2016-08-05 09:00:00+00	2016-08-05 09:30:00+00
478	I	2016-08-05 12:44:26.657195+00	76	813	7	2016-08-08 07:00:00+00	2016-08-08 09:00:00+00
479	I	2016-08-05 16:20:18.236996+00	12	815	7	2016-08-04 13:00:00+00	2016-08-04 13:30:00+00
480	D	2016-08-05 16:20:27.130869+00	12	815	7	2016-08-04 13:00:00+00	2016-08-04 13:30:00+00
481	I	2016-08-08 07:36:27.230911+00	12	816	7	2016-08-08 10:00:00+00	2016-08-08 11:00:00+00
482	I	2016-08-08 07:37:49.552047+00	12	817	7	2016-08-08 11:00:00+00	2016-08-08 12:00:00+00
483	I	2016-08-08 11:59:54.296635+00	40	818	7	2016-08-09 10:00:00+00	2016-08-09 10:30:00+00
484	I	2016-08-08 11:59:59.168538+00	40	819	7	2016-08-09 10:30:00+00	2016-08-09 11:00:00+00
485	I	2016-08-08 12:00:06.047239+00	40	820	7	2016-08-10 10:00:00+00	2016-08-10 10:30:00+00
486	I	2016-08-08 12:00:07.908578+00	40	821	7	2016-08-10 10:30:00+00	2016-08-10 11:00:00+00
487	I	2016-08-08 12:00:14.488859+00	40	822	7	2016-08-11 10:00:00+00	2016-08-11 10:30:00+00
488	I	2016-08-08 12:00:16.172153+00	40	823	7	2016-08-11 10:30:00+00	2016-08-11 11:00:00+00
489	I	2016-08-08 12:00:20.734568+00	40	824	7	2016-08-12 10:00:00+00	2016-08-12 10:30:00+00
490	I	2016-08-08 12:00:22.394922+00	40	825	7	2016-08-12 10:30:00+00	2016-08-12 11:00:00+00
491	I	2016-08-08 17:41:41.530743+00	17	826	7	2016-08-09 06:00:00+00	2016-08-09 10:00:00+00
492	I	2016-08-08 17:41:41.530743+00	17	827	7	2016-08-09 11:00:00+00	2016-08-09 15:00:00+00
493	I	2016-08-08 17:41:54.409986+00	17	828	7	2016-08-16 06:00:00+00	2016-08-16 15:00:00+00
494	I	2016-08-08 17:42:07.915744+00	17	829	7	2016-08-17 15:00:00+00	2016-08-17 21:00:00+00
495	I	2016-08-08 17:42:21.821191+00	17	830	7	2016-08-18 15:00:00+00	2016-08-18 21:00:00+00
496	I	2016-08-09 14:49:04.000426+00	53	831	7	2016-08-10 08:00:00+00	2016-08-10 08:30:00+00
497	D	2016-08-09 15:07:33.41809+00	53	831	7	2016-08-10 08:00:00+00	2016-08-10 08:30:00+00
498	I	2016-08-10 09:56:27.377546+00	12	832	7	2016-08-11 09:00:00+00	2016-08-11 10:00:00+00
499	I	2016-08-10 09:59:24.142836+00	12	833	7	2016-08-11 08:00:00+00	2016-08-11 09:00:00+00
500	I	2016-08-10 13:35:32.908166+00	59	834	7	2016-08-11 11:00:00+00	2016-08-11 11:30:00+00
503	I	2016-08-12 08:50:32.806255+00	12	837	7	2016-08-15 09:00:00+00	2016-08-15 10:00:00+00
504	I	2016-08-12 08:52:11.401685+00	12	838	7	2016-08-15 10:30:00+00	2016-08-15 11:30:00+00
505	I	2016-08-12 09:35:29.939619+00	67	839	7	2016-08-15 12:00:00+00	2016-08-15 13:00:00+00
506	I	2016-08-12 11:13:36.140292+00	69	840	7	2016-08-15 06:00:00+00	2016-08-15 07:00:00+00
507	I	2016-08-15 06:29:08.887599+00	17	841	7	2016-08-15 10:00:00+00	2016-08-15 10:30:00+00
508	I	2016-08-15 06:29:16.283147+00	17	842	7	2016-08-17 10:00:00+00	2016-08-17 10:30:00+00
509	I	2016-08-15 06:29:19.980785+00	17	843	7	2016-08-18 10:00:00+00	2016-08-18 10:30:00+00
510	I	2016-08-15 06:29:36.37677+00	17	844	7	2016-08-19 10:00:00+00	2016-08-19 10:30:00+00
511	I	2016-08-15 06:29:36.37677+00	17	845	7	2016-08-18 21:00:00+00	2016-08-19 03:00:00+00
512	I	2016-08-15 06:29:36.37677+00	17	846	7	2016-08-19 15:00:00+00	2016-08-19 21:00:00+00
513	I	2016-08-15 06:30:06.715299+00	17	847	7	2016-08-19 21:00:00+00	2016-08-20 21:00:00+00
514	I	2016-08-15 06:30:34.158821+00	17	848	7	2016-08-20 21:00:00+00	2016-08-21 21:00:00+00
515	I	2016-08-15 14:27:28.334626+00	17	849	7	2016-08-19 03:00:00+00	2016-08-19 06:00:00+00
516	I	2016-08-16 07:09:13.516311+00	59	850	7	2016-08-18 10:30:00+00	2016-08-18 11:00:00+00
517	I	2016-08-17 12:36:58.867057+00	12	851	7	2016-08-18 09:00:00+00	2016-08-18 10:00:00+00
518	I	2016-08-19 09:22:58.071092+00	69	852	7	2016-08-22 06:00:00+00	2016-08-22 07:00:00+00
520	I	2016-08-22 07:11:32.38165+00	53	854	7	2016-08-22 08:30:00+00	2016-08-22 09:00:00+00
523	I	2016-08-24 13:20:28.666022+00	12	857	7	2016-08-25 09:00:00+00	2016-08-25 10:00:00+00
524	I	2016-08-24 13:21:28.631958+00	12	858	7	2016-08-25 11:00:00+00	2016-08-25 12:00:00+00
525	D	2016-08-24 13:22:45.677969+00	12	857	7	2016-08-25 09:00:00+00	2016-08-25 10:00:00+00
526	I	2016-08-24 13:24:18.793444+00	12	859	7	2016-08-25 09:00:00+00	2016-08-25 10:30:00+00
527	I	2016-08-25 07:22:53.614589+00	57	860	7	2016-08-26 09:00:00+00	2016-08-26 10:00:00+00
528	I	2016-08-25 12:25:03.373442+00	59	861	7	2016-08-26 10:00:00+00	2016-08-26 10:30:00+00
529	I	2016-08-25 12:36:36.533033+00	69	862	7	2016-08-26 11:30:00+00	2016-08-26 13:00:00+00
530	I	2016-08-26 12:10:08.39229+00	69	863	7	2016-08-29 06:00:00+00	2016-08-29 07:00:00+00
522	I	2016-08-24 12:33:50.854364+00	64	856	11	2016-08-25 04:00:00+00	2016-08-25 09:00:00+00
531	I	2016-08-29 08:14:11.169352+00	12	864	7	2016-08-29 11:00:00+00	2016-08-29 12:00:00+00
532	I	2016-08-29 08:15:19.431734+00	12	865	7	2016-08-29 12:00:00+00	2016-08-29 13:00:00+00
535	I	2016-08-30 07:32:38.746945+00	494	868	11	2016-08-30 04:30:00+00	2016-08-30 05:30:00+00
536	D	2016-08-30 07:32:43.492896+00	494	868	11	2016-08-30 04:30:00+00	2016-08-30 05:30:00+00
537	I	2016-08-30 07:35:10.936691+00	17	869	7	2016-08-22 15:00:00+00	2016-08-22 21:00:00+00
538	I	2016-08-30 07:35:10.936691+00	17	870	7	2016-08-21 21:00:00+00	2016-08-22 06:00:00+00
539	I	2016-08-30 07:35:10.936691+00	17	871	7	2016-08-22 10:00:00+00	2016-08-22 11:00:00+00
540	I	2016-08-30 07:35:26.560494+00	17	872	7	2016-08-22 21:00:00+00	2016-08-23 06:00:00+00
541	I	2016-08-30 07:35:26.560494+00	17	873	7	2016-08-23 10:00:00+00	2016-08-23 11:00:00+00
542	I	2016-08-30 07:35:26.560494+00	17	874	7	2016-08-23 15:00:00+00	2016-08-23 21:00:00+00
543	I	2016-08-30 07:35:44.579703+00	17	875	7	2016-08-24 15:00:00+00	2016-08-24 21:00:00+00
544	I	2016-08-30 07:35:44.579703+00	17	876	7	2016-08-23 21:00:00+00	2016-08-24 06:00:00+00
545	I	2016-08-30 07:35:44.579703+00	17	877	7	2016-08-24 10:00:00+00	2016-08-24 11:00:00+00
546	I	2016-08-30 07:36:11.322699+00	17	878	7	2016-08-25 15:00:00+00	2016-08-25 21:00:00+00
547	I	2016-08-30 07:36:11.322699+00	17	879	7	2016-08-25 10:30:00+00	2016-08-25 11:00:00+00
548	I	2016-08-30 07:36:11.322699+00	17	880	7	2016-08-24 21:00:00+00	2016-08-25 06:00:00+00
549	I	2016-08-30 07:36:27.540013+00	17	881	7	2016-08-25 21:00:00+00	2016-08-26 06:00:00+00
550	I	2016-08-30 07:36:27.540013+00	17	882	7	2016-08-26 15:00:00+00	2016-08-26 21:00:00+00
551	I	2016-08-30 07:36:43.777333+00	17	883	7	2016-08-26 21:00:00+00	2016-08-27 06:00:00+00
552	I	2016-08-30 07:36:43.777333+00	17	884	7	2016-08-27 10:00:00+00	2016-08-27 11:00:00+00
553	I	2016-08-30 07:36:43.777333+00	17	885	7	2016-08-27 15:00:00+00	2016-08-27 21:00:00+00
554	I	2016-08-30 07:36:57.996927+00	17	886	7	2016-08-27 21:00:00+00	2016-08-28 06:00:00+00
555	I	2016-08-30 07:36:57.996927+00	17	887	7	2016-08-28 10:00:00+00	2016-08-28 11:00:00+00
556	I	2016-08-30 07:36:57.996927+00	17	888	7	2016-08-28 15:00:00+00	2016-08-28 21:00:00+00
557	I	2016-08-30 07:37:13.533039+00	17	889	7	2016-08-28 21:00:00+00	2016-08-29 06:00:00+00
558	I	2016-08-30 07:37:13.533039+00	17	890	7	2016-08-29 15:00:00+00	2016-08-29 21:00:00+00
559	I	2016-08-30 07:37:13.533039+00	17	891	7	2016-08-29 10:00:00+00	2016-08-29 11:00:00+00
560	I	2016-08-30 07:37:29.015639+00	17	892	7	2016-08-29 21:00:00+00	2016-08-30 06:00:00+00
561	I	2016-08-30 07:37:29.015639+00	17	893	7	2016-08-30 15:00:00+00	2016-08-30 21:00:00+00
562	I	2016-08-30 07:37:29.015639+00	17	894	7	2016-08-30 10:00:00+00	2016-08-30 11:00:00+00
563	I	2016-08-30 07:37:43.535269+00	17	895	7	2016-08-31 15:00:00+00	2016-08-31 21:00:00+00
564	I	2016-08-30 07:37:43.535269+00	17	896	7	2016-08-30 21:00:00+00	2016-08-31 06:00:00+00
565	I	2016-08-30 07:37:43.535269+00	17	897	7	2016-08-31 10:00:00+00	2016-08-31 11:00:00+00
566	I	2016-08-30 07:38:02.120392+00	17	898	7	2016-08-31 21:00:00+00	2016-09-01 06:00:00+00
567	I	2016-08-30 07:38:02.120392+00	17	899	7	2016-09-01 15:00:00+00	2016-09-01 21:00:00+00
568	I	2016-08-30 07:38:02.120392+00	17	900	7	2016-09-01 10:00:00+00	2016-09-01 11:00:00+00
569	I	2016-08-30 07:38:17.993669+00	17	901	7	2016-09-02 10:00:00+00	2016-09-02 11:00:00+00
570	I	2016-08-30 07:38:17.993669+00	17	902	7	2016-09-02 15:00:00+00	2016-09-02 21:00:00+00
571	I	2016-08-30 07:38:17.993669+00	17	903	7	2016-09-01 21:00:00+00	2016-09-02 06:00:00+00
572	I	2016-08-30 07:38:33.880198+00	17	904	7	2016-09-03 10:00:00+00	2016-09-03 11:00:00+00
573	I	2016-08-30 07:38:33.880198+00	17	905	7	2016-09-02 21:00:00+00	2016-09-03 06:00:00+00
574	I	2016-08-30 07:38:33.880198+00	17	906	7	2016-09-03 15:00:00+00	2016-09-03 21:00:00+00
575	I	2016-08-30 07:38:50.181467+00	17	907	7	2016-09-03 21:00:00+00	2016-09-04 06:00:00+00
576	I	2016-08-30 07:38:50.181467+00	17	908	7	2016-09-04 15:00:00+00	2016-09-04 21:00:00+00
577	I	2016-08-30 07:38:52.511252+00	17	909	7	2016-09-04 10:00:00+00	2016-09-04 11:00:00+00
578	I	2016-08-30 07:39:08.391593+00	17	910	7	2016-09-05 15:00:00+00	2016-09-05 21:00:00+00
579	I	2016-08-30 07:39:08.391593+00	17	911	7	2016-09-04 21:00:00+00	2016-09-05 06:00:00+00
580	I	2016-08-30 07:39:08.391593+00	17	912	7	2016-09-05 10:00:00+00	2016-09-05 11:00:00+00
581	I	2016-08-30 07:39:25.841155+00	17	913	7	2016-09-05 21:00:00+00	2016-09-06 06:00:00+00
582	I	2016-08-30 07:39:25.841155+00	17	914	7	2016-09-06 15:00:00+00	2016-09-06 21:00:00+00
583	I	2016-08-30 07:39:25.841155+00	17	915	7	2016-09-06 10:00:00+00	2016-09-06 11:00:00+00
584	I	2016-08-30 07:39:41.410468+00	17	916	7	2016-09-06 21:00:00+00	2016-09-07 06:00:00+00
585	I	2016-08-30 07:39:41.410468+00	17	917	7	2016-09-07 15:00:00+00	2016-09-07 21:00:00+00
586	I	2016-08-30 07:39:41.410468+00	17	918	7	2016-09-07 10:00:00+00	2016-09-07 11:00:00+00
587	I	2016-08-30 07:39:56.769902+00	17	919	7	2016-09-08 10:00:00+00	2016-09-08 11:00:00+00
588	I	2016-08-30 07:39:56.769902+00	17	920	7	2016-09-07 21:00:00+00	2016-09-08 06:00:00+00
589	I	2016-08-30 07:39:56.769902+00	17	921	7	2016-09-08 15:00:00+00	2016-09-08 21:00:00+00
590	I	2016-08-30 07:40:11.000173+00	17	922	7	2016-09-09 10:00:00+00	2016-09-09 11:00:00+00
591	I	2016-08-30 07:40:11.000173+00	17	923	7	2016-09-08 21:00:00+00	2016-09-09 06:00:00+00
592	I	2016-08-30 07:40:11.000173+00	17	924	7	2016-09-09 15:00:00+00	2016-09-09 21:00:00+00
593	I	2016-08-30 07:40:25.848828+00	17	925	7	2016-09-09 21:00:00+00	2016-09-10 06:00:00+00
594	I	2016-08-30 07:40:25.848828+00	17	926	7	2016-09-10 10:00:00+00	2016-09-10 11:00:00+00
595	I	2016-08-30 07:40:25.848828+00	17	927	7	2016-09-10 15:00:00+00	2016-09-10 21:00:00+00
596	I	2016-08-30 07:40:40.467045+00	17	928	7	2016-09-11 15:00:00+00	2016-09-11 21:00:00+00
597	I	2016-08-30 07:40:40.467045+00	17	929	7	2016-09-10 21:00:00+00	2016-09-11 06:00:00+00
598	I	2016-08-30 07:40:40.467045+00	17	930	7	2016-09-11 10:00:00+00	2016-09-11 11:00:00+00
599	I	2016-08-30 07:41:56.343965+00	17	931	7	2016-08-26 10:30:00+00	2016-08-26 11:00:00+00
600	I	2016-08-30 07:45:00.278092+00	16	932	11	2016-08-22 08:00:00+00	2016-08-22 09:00:00+00
601	I	2016-08-30 07:45:00.278092+00	16	933	11	2016-08-22 13:00:00+00	2016-08-22 19:00:00+00
602	I	2016-08-30 07:45:00.278092+00	16	934	11	2016-08-21 19:00:00+00	2016-08-22 04:00:00+00
603	I	2016-08-30 07:45:14.417641+00	16	935	11	2016-08-23 13:00:00+00	2016-08-23 19:00:00+00
604	I	2016-08-30 07:45:14.417641+00	16	936	11	2016-08-23 08:00:00+00	2016-08-23 09:00:00+00
605	I	2016-08-30 07:45:14.417641+00	16	937	11	2016-08-22 19:00:00+00	2016-08-23 04:00:00+00
606	I	2016-08-30 07:45:30.35796+00	16	938	11	2016-08-24 08:00:00+00	2016-08-24 09:00:00+00
607	I	2016-08-30 07:45:30.35796+00	16	939	11	2016-08-23 19:00:00+00	2016-08-24 04:00:00+00
608	I	2016-08-30 07:45:30.35796+00	16	940	11	2016-08-24 13:00:00+00	2016-08-24 19:00:00+00
609	I	2016-08-30 07:45:46.776329+00	16	941	11	2016-08-25 13:00:00+00	2016-08-25 19:00:00+00
610	I	2016-08-30 07:45:46.776329+00	16	942	11	2016-08-24 19:00:00+00	2016-08-25 04:00:00+00
611	I	2016-08-30 07:46:02.276615+00	16	943	11	2016-08-26 08:00:00+00	2016-08-26 09:00:00+00
612	I	2016-08-30 07:46:02.276615+00	16	944	11	2016-08-26 13:00:00+00	2016-08-26 19:00:00+00
613	I	2016-08-30 07:46:02.276615+00	16	945	11	2016-08-25 19:00:00+00	2016-08-26 04:00:00+00
614	I	2016-08-30 07:46:25.902207+00	16	946	11	2016-08-26 19:00:00+00	2016-08-27 19:00:00+00
615	I	2016-08-30 07:46:54.081038+00	16	947	11	2016-09-03 19:00:00+00	2016-09-04 19:00:00+00
616	I	2016-08-30 07:47:22.655191+00	16	948	11	2016-08-27 19:00:00+00	2016-08-28 19:00:00+00
620	I	2016-08-30 07:47:56.197988+00	16	952	11	2016-08-30 08:00:00+00	2016-08-30 09:00:00+00
621	I	2016-08-30 07:47:56.197988+00	16	953	11	2016-08-29 19:00:00+00	2016-08-30 04:00:00+00
622	I	2016-08-30 07:47:56.197988+00	16	954	11	2016-08-30 13:00:00+00	2016-08-30 19:00:00+00
626	I	2016-08-30 07:48:29.204414+00	16	958	11	2016-08-31 19:00:00+00	2016-09-01 04:00:00+00
627	I	2016-08-30 07:48:29.204414+00	16	959	11	2016-09-01 08:00:00+00	2016-09-01 09:00:00+00
628	I	2016-08-30 07:48:29.204414+00	16	960	11	2016-09-01 13:00:00+00	2016-09-01 19:00:00+00
633	D	2016-08-30 08:42:14.653509+00	117	964	7	2016-08-30 09:00:00+00	2016-08-30 10:00:00+00
634	I	2016-08-30 09:00:14.484567+00	118	965	9	2016-08-30 04:00:00+00	2016-08-30 05:00:00+00
635	D	2016-08-30 09:00:49.520375+00	118	965	9	2016-08-30 04:00:00+00	2016-08-30 05:00:00+00
617	I	2016-08-30 07:47:38.447933+00	16	949	11	2016-08-29 13:00:00+00	2016-08-29 19:00:00+00
618	I	2016-08-30 07:47:38.447933+00	16	950	11	2016-08-28 19:00:00+00	2016-08-29 04:00:00+00
619	I	2016-08-30 07:47:38.447933+00	16	951	11	2016-08-29 08:00:00+00	2016-08-29 09:00:00+00
623	I	2016-08-30 07:48:12.03168+00	16	955	11	2016-08-31 13:00:00+00	2016-08-31 19:00:00+00
624	I	2016-08-30 07:48:12.03168+00	16	956	11	2016-08-30 19:00:00+00	2016-08-31 04:00:00+00
625	I	2016-08-30 07:48:12.03168+00	16	957	11	2016-08-31 08:00:00+00	2016-08-31 09:00:00+00
629	I	2016-08-30 07:48:46.476401+00	16	961	11	2016-09-01 19:00:00+00	2016-09-02 04:00:00+00
630	I	2016-08-30 07:48:46.476401+00	16	962	11	2016-09-02 13:00:00+00	2016-09-02 19:00:00+00
631	I	2016-08-30 07:48:46.476401+00	16	963	11	2016-09-02 08:00:00+00	2016-09-02 09:00:00+00
632	I	2016-08-30 08:42:07.78958+00	117	964	7	2016-08-30 09:00:00+00	2016-08-30 10:00:00+00
636	I	2016-08-30 09:44:35.761593+00	119	966	12	2016-08-30 02:00:00+00	2016-08-30 03:00:00+00
637	D	2016-08-30 09:44:45.998442+00	119	966	12	2016-08-30 02:00:00+00	2016-08-30 03:00:00+00
638	I	2016-08-30 09:48:26.760097+00	120	967	7	2016-08-30 07:00:00+00	2016-08-30 07:30:00+00
639	D	2016-08-30 09:48:30.121966+00	120	967	7	2016-08-30 07:00:00+00	2016-08-30 07:30:00+00
640	I	2016-08-30 09:58:16.000713+00	121	968	13	2016-08-30 03:00:00+00	2016-08-30 04:00:00+00
641	D	2016-08-30 09:58:46.123597+00	121	968	13	2016-08-30 03:00:00+00	2016-08-30 04:00:00+00
642	I	2016-08-30 10:27:01.422216+00	498	969	7	2016-08-30 07:00:00+00	2016-08-30 07:30:00+00
643	D	2016-08-30 10:27:04.617182+00	498	969	7	2016-08-30 07:00:00+00	2016-08-30 07:30:00+00
644	I	2016-08-30 12:03:26.532978+00	53	970	7	2016-08-31 07:30:00+00	2016-08-31 08:00:00+00
645	I	2016-08-30 12:51:06.373393+00	129	971	7	2016-08-30 06:30:00+00	2016-08-30 07:00:00+00
646	D	2016-08-30 12:51:11.461902+00	129	971	7	2016-08-30 06:30:00+00	2016-08-30 07:00:00+00
647	I	2016-08-30 13:05:27.909528+00	123	972	7	2016-08-30 06:30:00+00	2016-08-30 07:30:00+00
648	D	2016-08-30 13:05:35.851402+00	123	972	7	2016-08-30 06:30:00+00	2016-08-30 07:30:00+00
649	I	2016-08-30 13:12:05.980721+00	123	973	7	2016-08-30 07:00:00+00	2016-08-30 08:00:00+00
650	D	2016-08-30 13:12:09.331952+00	123	973	7	2016-08-30 07:00:00+00	2016-08-30 08:00:00+00
651	I	2016-08-30 13:20:46.047054+00	119	974	12	2016-08-29 02:00:00+00	2016-08-29 03:00:00+00
652	D	2016-08-30 13:20:49.193153+00	119	974	12	2016-08-29 02:00:00+00	2016-08-29 03:00:00+00
653	I	2016-08-30 13:36:06.31646+00	124	975	7	2016-08-30 07:30:00+00	2016-08-30 08:30:00+00
654	D	2016-08-30 13:36:10.225657+00	124	975	7	2016-08-30 07:30:00+00	2016-08-30 08:30:00+00
655	I	2016-08-30 13:46:19.76463+00	125	976	12	2016-08-30 02:00:00+00	2016-08-30 03:00:00+00
656	D	2016-08-30 13:46:22.021753+00	125	976	12	2016-08-30 02:00:00+00	2016-08-30 03:00:00+00
657	I	2016-08-30 14:34:02.9399+00	127	977	12	2016-08-30 02:30:00+00	2016-08-30 03:00:00+00
658	D	2016-08-30 14:34:09.978785+00	127	977	12	2016-08-30 02:30:00+00	2016-08-30 03:00:00+00
659	I	2016-08-31 10:17:48.776641+00	509	978	12	2016-08-31 02:00:00+00	2016-08-31 02:30:00+00
660	D	2016-08-31 10:17:52.223711+00	509	978	12	2016-08-31 02:00:00+00	2016-08-31 02:30:00+00
661	I	2016-08-31 10:18:20.448223+00	510	979	12	2016-08-31 02:30:00+00	2016-08-31 03:00:00+00
662	D	2016-08-31 10:18:23.880467+00	510	979	12	2016-08-31 02:30:00+00	2016-08-31 03:00:00+00
663	I	2016-08-31 10:18:47.260408+00	511	980	7	2016-08-31 07:00:00+00	2016-08-31 07:30:00+00
664	D	2016-08-31 10:18:50.33834+00	511	980	7	2016-08-31 07:00:00+00	2016-08-31 07:30:00+00
665	I	2016-08-31 10:19:17.438008+00	512	981	12	2016-08-31 02:00:00+00	2016-08-31 02:30:00+00
666	D	2016-08-31 10:19:25.520668+00	512	981	12	2016-08-31 02:00:00+00	2016-08-31 02:30:00+00
667	I	2016-08-31 10:19:46.486974+00	513	982	7	2016-08-31 07:00:00+00	2016-08-31 07:30:00+00
668	D	2016-08-31 10:19:52.076036+00	513	982	7	2016-08-31 07:00:00+00	2016-08-31 07:30:00+00
669	I	2016-08-31 10:23:28.649595+00	514	983	12	2016-08-30 20:00:00+00	2016-08-30 20:30:00+00
670	D	2016-08-31 10:23:30.96089+00	514	983	12	2016-08-30 20:00:00+00	2016-08-30 20:30:00+00
671	I	2016-08-31 10:43:00.409553+00	17	984	7	2016-09-03 11:00:00+00	2016-09-03 15:00:00+00
672	I	2016-08-31 10:43:00.409553+00	17	985	7	2016-09-03 06:00:00+00	2016-09-03 10:00:00+00
673	I	2016-08-31 10:43:12.248888+00	17	986	7	2016-09-04 06:00:00+00	2016-09-04 10:00:00+00
674	I	2016-08-31 10:43:12.248888+00	17	987	7	2016-09-04 11:00:00+00	2016-09-04 15:00:00+00
675	I	2016-08-31 10:43:26.067385+00	17	988	7	2016-09-06 06:00:00+00	2016-09-06 10:00:00+00
676	I	2016-08-31 10:43:26.067385+00	17	989	7	2016-09-06 11:00:00+00	2016-09-06 15:00:00+00
677	I	2016-08-31 10:43:41.586441+00	17	990	7	2016-09-10 06:00:00+00	2016-09-10 10:00:00+00
678	I	2016-08-31 10:43:41.586441+00	17	991	7	2016-09-10 11:00:00+00	2016-09-10 15:00:00+00
679	I	2016-08-31 10:43:55.886426+00	17	992	7	2016-09-11 06:00:00+00	2016-09-11 10:00:00+00
680	I	2016-08-31 10:43:55.886426+00	17	993	7	2016-09-11 11:00:00+00	2016-09-11 15:00:00+00
681	I	2016-08-31 11:07:35.162425+00	472	994	11	2016-09-02 04:00:00+00	2016-09-02 04:30:00+00
682	I	2016-08-31 12:36:17.506165+00	114	995	12	2016-08-30 20:00:00+00	2016-08-30 20:30:00+00
683	D	2016-08-31 12:36:20.229778+00	114	995	12	2016-08-30 20:00:00+00	2016-08-30 20:30:00+00
684	I	2016-08-31 12:40:24.207338+00	124	996	7	2016-08-30 07:00:00+00	2016-08-30 07:30:00+00
685	D	2016-08-31 12:40:29.046533+00	124	996	7	2016-08-30 07:00:00+00	2016-08-30 07:30:00+00
686	I	2016-08-31 13:00:57.021291+00	59	997	7	2016-09-01 09:30:00+00	2016-09-01 10:00:00+00
687	I	2016-08-31 13:08:06.005252+00	492	998	12	2016-08-31 02:00:00+00	2016-08-31 02:30:00+00
688	D	2016-08-31 13:08:15.852904+00	492	998	12	2016-08-31 02:00:00+00	2016-08-31 02:30:00+00
689	I	2016-08-31 13:25:03.283291+00	122	999	7	2016-08-31 07:00:00+00	2016-08-31 07:30:00+00
690	D	2016-08-31 13:25:10.857652+00	122	999	7	2016-08-31 07:00:00+00	2016-08-31 07:30:00+00
691	I	2016-08-31 13:59:47.87064+00	518	1000	12	2016-08-30 20:30:00+00	2016-08-30 21:00:00+00
692	D	2016-08-31 13:59:54.129547+00	518	1000	12	2016-08-30 20:30:00+00	2016-08-30 21:00:00+00
693	I	2016-08-31 14:35:37.925306+00	523	1001	12	2016-08-31 08:00:00+00	2016-08-31 08:30:00+00
694	D	2016-08-31 14:35:40.704234+00	523	1001	12	2016-08-31 08:00:00+00	2016-08-31 08:30:00+00
695	I	2016-08-31 14:44:09.009524+00	524	1002	7	2016-08-31 07:00:00+00	2016-08-31 07:30:00+00
696	D	2016-08-31 14:44:12.088409+00	524	1002	7	2016-08-31 07:00:00+00	2016-08-31 07:30:00+00
697	I	2016-09-01 08:08:33.794959+00	12	1003	7	2016-09-01 11:00:00+00	2016-09-01 11:30:00+00
698	I	2016-09-01 08:09:15.658327+00	12	1004	7	2016-09-01 11:30:00+00	2016-09-01 12:00:00+00
699	U	2016-09-01 09:41:59.816978+00	17	989	7	2016-09-06 11:30:00+00	2016-09-06 15:00:00+00
700	I	2016-09-01 09:42:20.748119+00	17	1005	7	2016-09-06 11:00:00+00	2016-09-06 11:30:00+00
701	I	2016-09-01 11:05:28.881624+00	16	1006	11	2016-09-04 19:00:00+00	2016-09-05 01:00:00+00
702	I	2016-09-01 11:05:28.881624+00	16	1007	11	2016-09-05 13:00:00+00	2016-09-05 19:00:00+00
703	I	2016-09-01 11:05:31.281326+00	16	1008	11	2016-09-05 08:00:00+00	2016-09-05 09:00:00+00
704	I	2016-09-01 11:05:55.350729+00	16	1009	11	2016-09-05 19:00:00+00	2016-09-06 01:00:00+00
705	I	2016-09-01 11:05:55.350729+00	16	1010	11	2016-09-06 08:00:00+00	2016-09-06 09:00:00+00
706	I	2016-09-01 11:05:55.350729+00	16	1011	11	2016-09-06 13:00:00+00	2016-09-06 19:00:00+00
707	I	2016-09-01 11:06:18.999498+00	526	1012	11	2016-09-05 04:00:00+00	2016-09-05 04:30:00+00
709	I	2016-09-01 11:14:39.909301+00	523	1014	7	2016-09-01 07:30:00+00	2016-09-01 08:30:00+00
710	D	2016-09-01 11:15:25.505016+00	526	1012	11	2016-09-05 04:00:00+00	2016-09-05 04:30:00+00
711	I	2016-09-01 11:15:29.427052+00	526	1015	11	2016-09-05 04:00:00+00	2016-09-05 05:00:00+00
712	I	2016-09-01 11:16:09.026454+00	16	1016	11	2016-09-05 01:00:00+00	2016-09-05 04:00:00+00
713	I	2016-09-01 11:16:17.752999+00	16	1017	11	2016-09-06 01:00:00+00	2016-09-06 04:00:00+00
714	I	2016-09-01 11:23:10.45625+00	526	1018	11	2016-09-05 05:00:00+00	2016-09-05 06:00:00+00
715	D	2016-09-01 11:26:21.608383+00	526	1015	11	2016-09-05 04:00:00+00	2016-09-05 05:00:00+00
716	D	2016-09-01 11:26:23.762696+00	526	1018	11	2016-09-05 05:00:00+00	2016-09-05 06:00:00+00
725	I	2016-09-01 11:43:13.959203+00	526	1023	8	2016-09-02 11:00:00+00	2016-09-02 11:30:00+00
726	I	2016-09-01 11:48:45.23345+00	526	1024	11	2016-09-05 04:00:00+00	2016-09-05 05:30:00+00
727	D	2016-09-01 11:56:24.405609+00	17	1014	7	2016-09-01 07:30:00+00	2016-09-01 08:30:00+00
736	I	2016-09-02 12:47:22.121614+00	465	1032	7	2016-09-05 07:30:00+00	2016-09-05 08:30:00+00
740	I	2016-09-05 06:41:56.9658+00	68	1036	7	2016-09-05 08:30:00+00	2016-09-05 09:00:00+00
745	I	2016-09-05 09:39:22.954828+00	11	1039	9	2016-07-07 10:00:00+00	2016-07-07 10:30:00+00
746	D	2016-09-05 09:39:25.213797+00	11	1039	9	2016-07-07 10:00:00+00	2016-07-07 10:30:00+00
747	I	2016-09-05 09:41:46.489932+00	11	1040	9	2016-07-07 10:30:00+00	2016-07-07 11:00:00+00
748	D	2016-09-05 09:41:53.908085+00	11	1040	9	2016-07-07 10:30:00+00	2016-07-07 11:00:00+00
757	D	2016-09-07 10:04:46.29105+00	472	1048	11	2016-09-08 01:30:00+00	2016-09-08 02:00:00+00
758	I	2016-09-07 10:06:37.335201+00	472	1049	11	2016-09-08 04:00:00+00	2016-09-08 04:30:00+00
761	I	2016-09-09 12:24:07.571651+00	465	1052	7	2016-09-12 07:30:00+00	2016-09-12 08:30:00+00
717	I	2016-09-01 11:31:41.438913+00	526	1019	11	2016-09-05 04:00:00+00	2016-09-05 05:00:00+00
718	D	2016-09-01 11:39:19.497433+00	526	1019	11	2016-09-05 04:00:00+00	2016-09-05 05:00:00+00
719	I	2016-09-01 11:39:28.368127+00	526	1020	11	2016-09-05 04:00:00+00	2016-09-05 05:00:00+00
720	I	2016-09-01 11:39:33.373229+00	526	1021	11	2016-09-05 05:00:00+00	2016-09-05 06:00:00+00
721	I	2016-09-01 11:39:49.062229+00	526	1022	11	2016-09-05 12:00:00+00	2016-09-05 12:30:00+00
722	D	2016-09-01 11:40:04.847078+00	526	1022	11	2016-09-05 12:00:00+00	2016-09-05 12:30:00+00
723	D	2016-09-01 11:40:07.35004+00	526	1021	11	2016-09-05 05:00:00+00	2016-09-05 06:00:00+00
724	D	2016-09-01 11:40:09.909142+00	526	1020	11	2016-09-05 04:00:00+00	2016-09-05 05:00:00+00
728	D	2016-09-01 11:59:22.669383+00	17	838	7	2016-08-15 10:30:00+00	2016-08-15 11:30:00+00
732	I	2016-09-02 08:48:12.452724+00	528	1028	8	2016-09-05 06:00:00+00	2016-09-05 07:00:00+00
735	I	2016-09-02 12:01:22.456837+00	89	1031	7	2016-09-07 06:00:00+00	2016-09-07 06:30:00+00
737	I	2016-09-05 06:09:13.101676+00	69	1033	7	2016-09-05 13:00:00+00	2016-09-05 14:30:00+00
741	I	2016-09-05 09:21:43.784537+00	11	1037	9	2016-07-07 10:00:00+00	2016-07-07 10:30:00+00
742	D	2016-09-05 09:21:46.995706+00	11	1037	9	2016-07-07 10:00:00+00	2016-07-07 10:30:00+00
743	I	2016-09-05 09:34:04.433225+00	11	1038	9	2016-07-07 13:30:00+00	2016-07-07 14:00:00+00
744	D	2016-09-05 09:34:11.266033+00	11	1038	9	2016-07-07 13:30:00+00	2016-07-07 14:00:00+00
750	I	2016-09-06 08:56:50.067962+00	128	1042	7	2016-09-07 12:00:00+00	2016-09-07 13:00:00+00
756	I	2016-09-07 10:04:28.207946+00	472	1048	11	2016-09-08 01:30:00+00	2016-09-08 02:00:00+00
759	I	2016-09-07 12:28:40.414058+00	59	1050	7	2016-09-08 09:00:00+00	2016-09-08 09:30:00+00
760	I	2016-09-09 08:58:33.175062+00	69	1051	7	2016-09-12 06:00:00+00	2016-09-12 07:30:00+00
762	I	2016-09-09 12:59:59.176807+00	68	1053	7	2016-09-12 08:30:00+00	2016-09-12 09:00:00+00
764	I	2016-09-09 13:27:33.639019+00	528	1055	8	2016-09-12 06:00:00+00	2016-09-12 07:00:00+00
765	I	2016-09-09 13:36:52.185657+00	527	1056	11	2016-09-12 04:00:00+00	2016-09-12 05:00:00+00
766	I	2016-09-12 06:04:35.158567+00	94	1057	7	2016-09-12 09:00:00+00	2016-09-12 09:30:00+00
767	D	2016-09-12 07:25:49.802263+00	94	1057	7	2016-09-12 09:00:00+00	2016-09-12 09:30:00+00
768	I	2016-09-12 08:44:31.912666+00	17	1058	7	2016-09-11 21:00:00+00	2016-09-12 06:00:00+00
769	I	2016-09-12 08:44:31.912666+00	17	1059	7	2016-09-12 15:00:00+00	2016-09-12 21:00:00+00
770	I	2016-09-12 08:44:55.554386+00	17	1060	7	2016-09-13 15:00:00+00	2016-09-13 21:00:00+00
771	I	2016-09-12 08:44:55.554386+00	17	1061	7	2016-09-12 21:00:00+00	2016-09-13 06:00:00+00
772	I	2016-09-12 08:44:55.554386+00	17	1062	7	2016-09-13 10:00:00+00	2016-09-13 10:30:00+00
773	I	2016-09-12 08:45:15.809659+00	17	1063	7	2016-09-13 10:30:00+00	2016-09-13 15:00:00+00
774	I	2016-09-12 08:45:15.809659+00	17	1064	7	2016-09-13 06:00:00+00	2016-09-13 10:00:00+00
775	I	2016-09-12 08:45:34.659848+00	17	1065	7	2016-09-14 10:00:00+00	2016-09-14 11:00:00+00
776	I	2016-09-12 08:45:34.659848+00	17	1066	7	2016-09-14 15:00:00+00	2016-09-14 21:00:00+00
777	I	2016-09-12 08:45:34.659848+00	17	1067	7	2016-09-13 21:00:00+00	2016-09-14 06:00:00+00
778	I	2016-09-12 08:45:53.030262+00	17	1068	7	2016-09-15 15:00:00+00	2016-09-15 21:00:00+00
779	I	2016-09-12 08:45:53.030262+00	17	1069	7	2016-09-14 21:00:00+00	2016-09-15 06:00:00+00
780	I	2016-09-12 08:45:53.030262+00	17	1070	7	2016-09-15 10:00:00+00	2016-09-15 11:00:00+00
781	I	2016-09-12 08:46:10.709815+00	17	1071	7	2016-09-15 21:00:00+00	2016-09-16 06:00:00+00
782	I	2016-09-12 08:46:10.709815+00	17	1072	7	2016-09-16 10:00:00+00	2016-09-16 11:00:00+00
783	I	2016-09-12 08:46:10.709815+00	17	1073	7	2016-09-16 15:00:00+00	2016-09-16 21:00:00+00
784	I	2016-09-12 08:46:37.431837+00	17	1074	7	2016-09-16 21:00:00+00	2016-09-17 21:00:00+00
785	I	2016-09-12 08:47:02.344342+00	17	1075	7	2016-09-17 21:00:00+00	2016-09-18 21:00:00+00
786	I	2016-09-12 08:47:20.631086+00	17	1076	7	2016-09-19 10:00:00+00	2016-09-19 11:00:00+00
787	I	2016-09-12 08:47:20.631086+00	17	1077	7	2016-09-18 21:00:00+00	2016-09-19 06:00:00+00
788	I	2016-09-12 08:47:20.631086+00	17	1078	7	2016-09-19 15:00:00+00	2016-09-19 21:00:00+00
789	I	2016-09-12 08:47:38.293338+00	17	1079	7	2016-09-20 15:00:00+00	2016-09-20 21:00:00+00
790	I	2016-09-12 08:47:38.293338+00	17	1080	7	2016-09-19 21:00:00+00	2016-09-20 06:00:00+00
791	I	2016-09-12 08:47:38.293338+00	17	1081	7	2016-09-20 10:00:00+00	2016-09-20 11:00:00+00
792	I	2016-09-12 08:55:57.251604+00	17	1082	7	2016-09-20 06:00:00+00	2016-09-20 07:00:00+00
793	I	2016-09-12 08:55:57.251604+00	17	1083	7	2016-09-20 09:00:00+00	2016-09-20 10:00:00+00
794	I	2016-09-12 08:55:57.251604+00	17	1084	7	2016-09-20 12:00:00+00	2016-09-20 13:00:00+00
795	I	2016-09-12 08:56:22.02085+00	17	1085	7	2016-09-20 13:00:00+00	2016-09-20 15:00:00+00
796	I	2016-09-12 08:56:22.02085+00	17	1086	7	2016-09-20 07:00:00+00	2016-09-20 09:00:00+00
797	I	2016-09-12 08:56:22.02085+00	17	1087	7	2016-09-20 11:00:00+00	2016-09-20 12:00:00+00
800	I	2016-09-12 10:44:28.95551+00	70	1090	7	2016-09-14 08:00:00+00	2016-09-14 08:30:00+00
801	I	2016-09-12 12:19:53.743832+00	17	1211	7	2016-09-12 10:00:00+00	2016-09-12 11:00:00+00
802	I	2016-09-12 12:20:27.039914+00	17	1212	7	2016-09-21 10:00:00+00	2016-09-21 11:00:00+00
803	I	2016-09-12 12:20:27.039914+00	17	1213	7	2016-09-21 15:00:00+00	2016-09-21 21:00:00+00
804	I	2016-09-12 12:20:27.039914+00	17	1214	7	2016-09-20 21:00:00+00	2016-09-21 06:00:00+00
805	I	2016-09-12 12:20:42.803382+00	17	1215	7	2016-09-21 21:00:00+00	2016-09-22 06:00:00+00
806	I	2016-09-12 12:20:42.803382+00	17	1216	7	2016-09-22 15:00:00+00	2016-09-22 21:00:00+00
807	I	2016-09-12 12:20:42.803382+00	17	1217	7	2016-09-22 10:00:00+00	2016-09-22 11:00:00+00
808	I	2016-09-12 12:20:58.248851+00	17	1218	7	2016-09-23 15:00:00+00	2016-09-23 21:00:00+00
809	I	2016-09-12 12:20:58.248851+00	17	1219	7	2016-09-22 21:00:00+00	2016-09-23 06:00:00+00
810	I	2016-09-12 12:20:58.248851+00	17	1220	7	2016-09-23 10:00:00+00	2016-09-23 11:00:00+00
811	I	2016-09-12 12:21:21.873006+00	17	1221	7	2016-09-23 21:00:00+00	2016-09-24 21:00:00+00
812	I	2016-09-12 12:21:47.719384+00	17	1222	7	2016-09-24 21:00:00+00	2016-09-25 21:00:00+00
813	I	2016-09-12 12:22:04.060357+00	17	1223	7	2016-09-26 15:00:00+00	2016-09-26 21:00:00+00
814	I	2016-09-12 12:22:04.060357+00	17	1224	7	2016-09-26 10:00:00+00	2016-09-26 11:00:00+00
815	I	2016-09-12 12:22:04.060357+00	17	1225	7	2016-09-25 21:00:00+00	2016-09-26 06:00:00+00
816	I	2016-09-12 12:22:28.678912+00	17	1226	7	2016-09-26 21:00:00+00	2016-09-27 21:00:00+00
817	I	2016-09-12 12:22:43.949523+00	17	1227	7	2016-09-28 15:00:00+00	2016-09-28 21:00:00+00
818	I	2016-09-12 12:22:43.949523+00	17	1228	7	2016-09-27 21:00:00+00	2016-09-28 06:00:00+00
819	I	2016-09-12 12:22:43.949523+00	17	1229	7	2016-09-28 10:00:00+00	2016-09-28 11:00:00+00
820	I	2016-09-12 12:22:59.796737+00	17	1230	7	2016-09-29 10:00:00+00	2016-09-29 11:00:00+00
821	I	2016-09-12 12:22:59.796737+00	17	1231	7	2016-09-29 15:00:00+00	2016-09-29 21:00:00+00
822	I	2016-09-12 12:22:59.796737+00	17	1232	7	2016-09-28 21:00:00+00	2016-09-29 06:00:00+00
823	I	2016-09-12 12:23:17.236579+00	17	1233	7	2016-09-29 21:00:00+00	2016-09-30 06:00:00+00
824	I	2016-09-12 12:23:17.236579+00	17	1234	7	2016-09-30 15:00:00+00	2016-09-30 21:00:00+00
825	I	2016-09-12 12:23:17.236579+00	17	1235	7	2016-09-30 10:00:00+00	2016-09-30 11:00:00+00
827	I	2016-09-12 12:23:53.538753+00	17	1237	7	2016-10-02 06:00:00+00	2016-10-02 15:00:00+00
829	I	2016-09-12 12:24:11.113909+00	17	1239	7	2016-10-04 06:00:00+00	2016-10-04 15:00:00+00
831	I	2016-09-12 12:24:19.830463+00	17	1241	7	2016-10-06 10:00:00+00	2016-10-06 11:00:00+00
833	I	2016-09-12 12:24:33.747004+00	17	1243	7	2016-10-08 06:00:00+00	2016-10-08 15:00:00+00
835	I	2016-09-12 12:24:49.060386+00	17	1245	7	2016-10-10 10:00:00+00	2016-10-10 11:00:00+00
837	I	2016-09-12 12:24:59.289272+00	17	1247	7	2016-10-11 09:00:00+00	2016-10-11 10:00:00+00
838	I	2016-09-12 12:24:59.289272+00	17	1248	7	2016-10-11 06:00:00+00	2016-10-11 07:00:00+00
839	I	2016-09-12 12:24:59.289272+00	17	1249	7	2016-10-11 12:00:00+00	2016-10-11 13:00:00+00
840	I	2016-09-12 12:25:01.299114+00	17	1250	7	2016-10-11 07:00:00+00	2016-10-11 07:30:00+00
841	I	2016-09-12 12:25:01.299114+00	17	1251	7	2016-10-11 13:00:00+00	2016-10-11 13:30:00+00
842	I	2016-09-12 12:25:03.170797+00	17	1252	7	2016-10-11 13:30:00+00	2016-10-11 14:00:00+00
843	I	2016-09-12 12:25:03.170797+00	17	1253	7	2016-10-11 07:30:00+00	2016-10-11 08:00:00+00
844	I	2016-09-12 12:25:06.909838+00	17	1254	7	2016-10-11 08:00:00+00	2016-10-11 09:00:00+00
845	I	2016-09-12 12:25:06.909838+00	17	1255	7	2016-10-11 11:00:00+00	2016-10-11 12:00:00+00
846	I	2016-09-12 12:25:06.909838+00	17	1256	7	2016-10-11 14:00:00+00	2016-10-11 15:00:00+00
848	I	2016-09-12 12:25:15.152359+00	17	1258	7	2016-10-13 10:00:00+00	2016-10-13 11:00:00+00
850	I	2016-09-12 12:25:29.729704+00	17	1260	7	2016-10-15 06:00:00+00	2016-10-15 15:00:00+00
852	I	2016-09-12 12:25:46.426205+00	17	1262	7	2016-10-17 10:00:00+00	2016-10-17 11:00:00+00
854	I	2016-09-12 12:26:00.229816+00	17	1264	7	2016-10-19 10:00:00+00	2016-10-19 11:00:00+00
856	I	2016-09-12 12:26:07.407593+00	17	1266	7	2016-10-21 10:00:00+00	2016-10-21 11:00:00+00
858	I	2016-09-12 12:26:27.787489+00	17	1268	7	2016-10-23 06:00:00+00	2016-10-23 15:00:00+00
860	I	2016-09-12 12:26:41.661065+00	17	1270	7	2016-10-25 06:00:00+00	2016-10-25 15:00:00+00
862	I	2016-09-12 12:26:50.174006+00	17	1272	7	2016-10-27 10:00:00+00	2016-10-27 11:00:00+00
864	I	2016-09-12 12:27:03.140507+00	17	1274	7	2016-10-29 06:00:00+00	2016-10-29 15:00:00+00
866	I	2016-09-12 12:27:22.947727+00	17	1276	7	2016-10-31 10:00:00+00	2016-10-31 11:00:00+00
867	I	2016-09-12 12:27:22.947727+00	17	1277	7	2016-10-31 15:00:00+00	2016-10-31 21:00:00+00
868	I	2016-09-12 12:43:03.985971+00	17	1426	14	2016-09-12 10:00:00+00	2016-09-12 11:00:00+00
870	I	2016-09-12 12:43:11.765145+00	17	1428	14	2016-09-14 10:00:00+00	2016-09-14 11:00:00+00
874	I	2016-09-12 12:43:32.314284+00	17	1432	14	2016-09-16 10:00:00+00	2016-09-16 11:00:00+00
876	I	2016-09-12 12:43:52.583989+00	17	1434	14	2016-09-18 06:00:00+00	2016-09-18 15:00:00+00
878	I	2016-09-12 12:44:06.003591+00	17	1436	14	2016-09-20 06:00:00+00	2016-09-20 15:00:00+00
880	I	2016-09-12 12:44:14.182832+00	17	1438	14	2016-09-22 10:00:00+00	2016-09-22 11:00:00+00
882	I	2016-09-12 12:44:27.742599+00	17	1440	14	2016-09-24 06:00:00+00	2016-09-24 15:00:00+00
884	I	2016-09-12 12:44:42.291599+00	17	1442	14	2016-09-26 10:00:00+00	2016-09-26 11:00:00+00
886	I	2016-09-12 12:44:55.841689+00	17	1444	14	2016-09-28 10:00:00+00	2016-09-28 11:00:00+00
888	I	2016-09-12 12:45:03.571532+00	17	1446	14	2016-09-30 10:00:00+00	2016-09-30 11:00:00+00
890	I	2016-09-12 12:45:32.149551+00	17	1448	14	2016-10-02 06:00:00+00	2016-10-02 15:00:00+00
892	I	2016-09-12 12:45:42.509708+00	17	1450	14	2016-10-10 10:00:00+00	2016-10-10 11:00:00+00
894	I	2016-09-12 12:45:49.249326+00	17	1452	14	2016-10-24 10:00:00+00	2016-10-24 11:00:00+00
826	I	2016-09-12 12:23:38.679163+00	17	1236	7	2016-09-30 21:00:00+00	2016-10-01 15:00:00+00
828	I	2016-09-12 12:24:00.309422+00	17	1238	7	2016-10-03 10:00:00+00	2016-10-03 11:00:00+00
830	I	2016-09-12 12:24:15.668524+00	17	1240	7	2016-10-05 10:00:00+00	2016-10-05 11:00:00+00
832	I	2016-09-12 12:24:24.268856+00	17	1242	7	2016-10-07 10:00:00+00	2016-10-07 11:00:00+00
834	I	2016-09-12 12:24:44.579647+00	17	1244	7	2016-10-09 06:00:00+00	2016-10-09 15:00:00+00
836	I	2016-09-12 12:24:53.093897+00	17	1246	7	2016-10-11 10:00:00+00	2016-10-11 11:00:00+00
847	I	2016-09-12 12:25:11.304591+00	17	1257	7	2016-10-12 10:00:00+00	2016-10-12 11:00:00+00
849	I	2016-09-12 12:25:18.974155+00	17	1259	7	2016-10-14 10:00:00+00	2016-10-14 11:00:00+00
851	I	2016-09-12 12:25:40.255517+00	17	1261	7	2016-10-16 06:00:00+00	2016-10-16 15:00:00+00
853	I	2016-09-12 12:25:56.407795+00	17	1263	7	2016-10-18 06:00:00+00	2016-10-18 15:00:00+00
855	I	2016-09-12 12:26:03.819305+00	17	1265	7	2016-10-20 10:00:00+00	2016-10-20 11:00:00+00
857	I	2016-09-12 12:26:17.793032+00	17	1267	7	2016-10-22 06:00:00+00	2016-10-22 15:00:00+00
859	I	2016-09-12 12:26:32.090305+00	17	1269	7	2016-10-24 10:00:00+00	2016-10-24 11:00:00+00
861	I	2016-09-12 12:26:45.854745+00	17	1271	7	2016-10-26 10:00:00+00	2016-10-26 11:00:00+00
863	I	2016-09-12 12:26:53.699189+00	17	1273	7	2016-10-28 10:00:00+00	2016-10-28 11:00:00+00
865	I	2016-09-12 12:27:13.129279+00	17	1275	7	2016-10-30 06:00:00+00	2016-10-30 15:00:00+00
869	I	2016-09-12 12:43:07.655235+00	17	1427	14	2016-09-13 10:00:00+00	2016-09-13 11:00:00+00
871	I	2016-09-12 12:43:22.085134+00	17	1429	14	2016-09-13 06:00:00+00	2016-09-13 10:00:00+00
872	I	2016-09-12 12:43:22.085134+00	17	1430	14	2016-09-13 11:00:00+00	2016-09-13 15:00:00+00
873	I	2016-09-12 12:43:28.184482+00	17	1431	14	2016-09-15 10:00:00+00	2016-09-15 11:00:00+00
875	I	2016-09-12 12:43:42.084835+00	17	1433	14	2016-09-17 06:00:00+00	2016-09-17 15:00:00+00
877	I	2016-09-12 12:43:56.753781+00	17	1435	14	2016-09-19 10:00:00+00	2016-09-19 11:00:00+00
879	I	2016-09-12 12:44:10.114331+00	17	1437	14	2016-09-21 10:00:00+00	2016-09-21 11:00:00+00
881	I	2016-09-12 12:44:17.602961+00	17	1439	14	2016-09-23 10:00:00+00	2016-09-23 11:00:00+00
883	I	2016-09-12 12:44:37.162002+00	17	1441	14	2016-09-25 06:00:00+00	2016-09-25 15:00:00+00
885	I	2016-09-12 12:44:51.531541+00	17	1443	14	2016-09-27 06:00:00+00	2016-09-27 15:00:00+00
887	I	2016-09-12 12:44:59.930852+00	17	1445	14	2016-09-29 10:00:00+00	2016-09-29 11:00:00+00
889	I	2016-09-12 12:45:20.600346+00	17	1447	14	2016-09-30 21:00:00+00	2016-10-01 15:00:00+00
891	I	2016-09-12 12:45:37.302044+00	17	1449	14	2016-10-03 10:00:00+00	2016-10-03 11:00:00+00
893	I	2016-09-12 12:45:45.7896+00	17	1451	14	2016-10-17 10:00:00+00	2016-10-17 11:00:00+00
895	I	2016-09-12 12:46:00.648657+00	17	1453	14	2016-10-04 13:00:00+00	2016-10-04 14:30:00+00
896	I	2016-09-12 12:46:02.838985+00	17	1454	14	2016-10-04 08:30:00+00	2016-10-04 09:00:00+00
897	I	2016-09-12 12:46:02.838985+00	17	1455	14	2016-10-04 11:30:00+00	2016-10-04 12:00:00+00
898	I	2016-09-12 12:46:02.838985+00	17	1456	14	2016-10-04 14:30:00+00	2016-10-04 15:00:00+00
899	I	2016-09-12 12:46:09.478925+00	17	1457	14	2016-10-04 06:00:00+00	2016-10-04 08:30:00+00
900	I	2016-09-12 12:46:09.478925+00	17	1458	14	2016-10-04 12:00:00+00	2016-10-04 13:00:00+00
901	I	2016-09-12 12:46:09.478925+00	17	1459	14	2016-10-04 09:00:00+00	2016-10-04 11:30:00+00
902	I	2016-09-12 12:46:15.309555+00	17	1460	14	2016-10-05 10:00:00+00	2016-10-05 11:00:00+00
903	I	2016-09-12 12:46:19.027857+00	17	1461	14	2016-10-06 10:00:00+00	2016-10-06 11:00:00+00
904	I	2016-09-12 12:46:22.848299+00	17	1462	14	2016-10-07 10:00:00+00	2016-10-07 11:00:00+00
905	I	2016-09-12 12:46:32.917873+00	17	1463	14	2016-10-08 06:00:00+00	2016-10-08 15:00:00+00
906	I	2016-09-12 12:46:42.777187+00	17	1464	14	2016-10-09 06:00:00+00	2016-10-09 15:00:00+00
907	I	2016-09-12 12:46:50.087184+00	17	1465	14	2016-10-12 10:00:00+00	2016-10-12 11:00:00+00
908	I	2016-09-12 12:46:59.06654+00	17	1466	14	2016-10-11 06:00:00+00	2016-10-11 15:00:00+00
909	I	2016-09-12 12:47:05.568868+00	17	1467	14	2016-10-13 10:00:00+00	2016-10-13 11:00:00+00
910	I	2016-09-12 12:47:09.686282+00	17	1468	14	2016-10-14 10:00:00+00	2016-10-14 11:00:00+00
911	I	2016-09-12 12:47:19.695562+00	17	1469	14	2016-10-15 06:00:00+00	2016-10-15 15:00:00+00
912	I	2016-09-12 12:47:28.90633+00	17	1470	14	2016-10-16 06:00:00+00	2016-10-16 15:00:00+00
913	I	2016-09-12 12:47:36.795476+00	17	1471	14	2016-10-19 10:00:00+00	2016-10-19 11:00:00+00
914	I	2016-09-12 12:47:46.645175+00	17	1472	14	2016-10-18 06:00:00+00	2016-10-18 15:00:00+00
915	I	2016-09-12 12:47:53.594203+00	17	1473	14	2016-10-20 10:00:00+00	2016-10-20 11:00:00+00
916	I	2016-09-12 12:47:57.204554+00	17	1474	14	2016-10-21 10:00:00+00	2016-10-21 11:00:00+00
917	I	2016-09-12 12:48:06.663994+00	17	1475	14	2016-10-22 06:00:00+00	2016-10-22 15:00:00+00
918	I	2016-09-12 12:48:15.630444+00	17	1476	14	2016-10-23 06:00:00+00	2016-10-23 15:00:00+00
919	I	2016-09-12 12:48:27.793113+00	17	1477	14	2016-10-25 06:00:00+00	2016-10-25 15:00:00+00
920	I	2016-09-12 12:48:32.002811+00	17	1478	14	2016-10-26 10:00:00+00	2016-10-26 11:00:00+00
921	I	2016-09-12 12:48:38.012045+00	17	1479	14	2016-10-27 10:00:00+00	2016-10-27 11:00:00+00
922	I	2016-09-12 12:48:41.762441+00	17	1480	14	2016-10-28 10:00:00+00	2016-10-28 11:00:00+00
923	I	2016-09-12 12:48:51.412628+00	17	1481	14	2016-10-29 06:00:00+00	2016-10-29 15:00:00+00
924	I	2016-09-12 12:49:01.00528+00	17	1482	14	2016-10-30 06:00:00+00	2016-10-30 15:00:00+00
925	I	2016-09-12 12:49:09.803591+00	17	1483	14	2016-10-31 15:00:00+00	2016-10-31 21:00:00+00
926	I	2016-09-12 12:49:09.803591+00	17	1484	14	2016-10-31 10:00:00+00	2016-10-31 11:00:00+00
929	I	2016-09-12 13:34:25.623785+00	528	1487	8	2016-09-14 11:00:00+00	2016-09-14 12:00:00+00
932	I	2016-09-12 14:15:05.914591+00	527	1490	11	2016-09-15 04:00:00+00	2016-09-15 05:00:00+00
933	I	2016-09-13 03:42:13.453697+00	472	1491	11	2016-09-13 04:30:00+00	2016-09-13 05:00:00+00
934	I	2016-09-13 07:20:26.757695+00	503	1492	12	2016-09-12 23:00:00+00	2016-09-12 23:30:00+00
935	D	2016-09-13 07:20:30.699948+00	503	1492	12	2016-09-12 23:00:00+00	2016-09-12 23:30:00+00
936	I	2016-09-13 10:13:50.662822+00	496	1493	12	2016-09-12 20:00:00+00	2016-09-12 20:30:00+00
937	D	2016-09-13 10:13:55.511786+00	496	1493	12	2016-09-12 20:00:00+00	2016-09-12 20:30:00+00
938	I	2016-09-13 10:16:40.117658+00	496	1494	12	2016-09-11 23:00:00+00	2016-09-11 23:30:00+00
939	D	2016-09-13 10:16:44.034225+00	496	1494	12	2016-09-11 23:00:00+00	2016-09-11 23:30:00+00
940	I	2016-09-13 11:09:53.937759+00	62	1495	11	2016-09-14 09:00:00+00	2016-09-14 09:30:00+00
941	I	2016-09-13 11:32:37.080781+00	69	1496	7	2016-09-14 11:30:00+00	2016-09-14 12:30:00+00
942	I	2016-09-13 11:33:49.188434+00	535	1497	14	2016-09-23 11:00:00+00	2016-09-23 11:30:00+00
943	D	2016-09-13 11:33:52.654653+00	535	1497	14	2016-09-23 11:00:00+00	2016-09-23 11:30:00+00
944	I	2016-09-13 11:38:04.688666+00	535	1498	14	2016-09-23 08:00:00+00	2016-09-23 08:30:00+00
945	D	2016-09-13 11:38:07.885528+00	535	1498	14	2016-09-23 08:00:00+00	2016-09-23 08:30:00+00
948	I	2016-09-13 12:57:02.035609+00	538	1501	7	2016-09-14 06:00:00+00	2016-09-14 06:30:00+00
949	D	2016-09-13 12:57:31.065436+00	538	1501	7	2016-09-14 06:00:00+00	2016-09-14 06:30:00+00
950	I	2016-09-13 12:58:37.440527+00	538	1502	7	2016-09-15 06:00:00+00	2016-09-15 06:30:00+00
951	I	2016-09-13 13:04:40.986585+00	538	1503	7	2016-09-15 06:30:00+00	2016-09-15 07:30:00+00
952	I	2016-09-13 13:05:19.719043+00	538	1504	7	2016-09-15 07:30:00+00	2016-09-15 09:00:00+00
953	I	2016-09-13 13:09:56.523089+00	59	1505	7	2016-09-14 08:30:00+00	2016-09-14 09:00:00+00
954	I	2016-09-13 14:06:09.64868+00	542	1506	7	2016-09-15 12:30:00+00	2016-09-15 13:00:00+00
955	D	2016-09-13 14:06:15.739551+00	542	1506	7	2016-09-15 12:30:00+00	2016-09-15 13:00:00+00
957	I	2016-09-14 12:45:31.972502+00	126	1508	7	2016-09-16 13:00:00+00	2016-09-16 14:00:00+00
959	I	2016-09-14 20:11:14.135871+00	16	2209	11	2016-09-06 19:00:00+00	2016-09-07 04:00:00+00
960	I	2016-09-14 20:11:14.135871+00	16	2210	11	2016-09-07 13:00:00+00	2016-09-07 19:00:00+00
963	I	2016-09-14 20:11:54.779776+00	16	2213	11	2016-09-09 13:00:00+00	2016-09-09 19:00:00+00
964	I	2016-09-14 20:11:54.779776+00	16	2214	11	2016-09-08 19:00:00+00	2016-09-09 04:00:00+00
967	I	2016-09-14 20:13:06.228407+00	16	2217	11	2016-09-12 19:00:00+00	2016-09-13 04:00:00+00
968	I	2016-09-14 20:13:06.228407+00	16	2218	11	2016-09-13 13:00:00+00	2016-09-13 19:00:00+00
975	I	2016-09-14 20:14:50.779008+00	16	2225	11	2016-09-19 13:00:00+00	2016-09-19 19:00:00+00
976	I	2016-09-14 20:14:50.779008+00	16	2226	11	2016-09-18 19:00:00+00	2016-09-19 04:00:00+00
979	I	2016-09-14 20:15:39.46621+00	16	2229	11	2016-09-20 19:00:00+00	2016-09-21 04:00:00+00
980	I	2016-09-14 20:15:39.46621+00	16	2230	11	2016-09-21 13:00:00+00	2016-09-21 19:00:00+00
983	I	2016-09-14 20:16:33.413571+00	16	2233	11	2016-09-22 19:00:00+00	2016-09-23 04:00:00+00
984	I	2016-09-14 20:16:33.413571+00	16	2234	11	2016-09-23 13:00:00+00	2016-09-23 19:00:00+00
987	I	2016-09-14 20:17:10.169706+00	16	2237	11	2016-09-27 13:00:00+00	2016-09-27 19:00:00+00
988	I	2016-09-14 20:17:10.169706+00	16	2238	11	2016-09-26 19:00:00+00	2016-09-27 04:00:00+00
991	I	2016-09-14 20:17:56.639081+00	16	2241	11	2016-09-29 13:00:00+00	2016-09-29 19:00:00+00
992	I	2016-09-14 20:17:56.639081+00	16	2242	11	2016-09-28 19:00:00+00	2016-09-29 04:00:00+00
995	I	2016-09-14 20:18:31.250702+00	16	2245	11	2016-09-30 19:00:00+00	2016-10-01 04:00:00+00
997	I	2016-09-15 07:17:48.828681+00	53	2247	14	2016-09-15 08:00:00+00	2016-09-15 08:30:00+00
1007	I	2016-09-15 14:04:18.407818+00	126	2256	7	2016-09-16 13:00:00+00	2016-09-16 14:00:00+00
1008	I	2016-09-16 06:09:18.370296+00	57	2257	7	2016-09-16 09:00:00+00	2016-09-16 10:00:00+00
1014	I	2016-09-16 12:35:51.57417+00	465	2262	7	2016-09-19 07:00:00+00	2016-09-19 08:00:00+00
956	I	2016-09-14 08:08:46.208197+00	15	1507	7	2016-09-15 09:00:00+00	2016-09-15 10:00:00+00
958	I	2016-09-14 14:27:21.445981+00	92	2208	14	2016-09-15 08:30:00+00	2016-09-15 09:00:00+00
961	I	2016-09-14 20:11:34.762098+00	16	2211	11	2016-09-08 13:00:00+00	2016-09-08 19:00:00+00
962	I	2016-09-14 20:11:34.762098+00	16	2212	11	2016-09-07 19:00:00+00	2016-09-08 04:00:00+00
965	I	2016-09-14 20:12:20.556711+00	16	2215	11	2016-09-12 13:00:00+00	2016-09-12 19:00:00+00
966	I	2016-09-14 20:12:20.556711+00	16	2216	11	2016-09-11 19:00:00+00	2016-09-12 04:00:00+00
969	I	2016-09-14 20:13:25.709802+00	16	2219	11	2016-09-13 19:00:00+00	2016-09-14 04:00:00+00
970	I	2016-09-14 20:13:25.709802+00	16	2220	11	2016-09-14 13:00:00+00	2016-09-14 19:00:00+00
971	I	2016-09-14 20:13:58.893535+00	16	2221	11	2016-09-14 19:00:00+00	2016-09-15 04:00:00+00
972	I	2016-09-14 20:13:58.893535+00	16	2222	11	2016-09-15 13:00:00+00	2016-09-15 19:00:00+00
973	I	2016-09-14 20:14:24.36296+00	16	2223	11	2016-09-16 13:00:00+00	2016-09-16 19:00:00+00
974	I	2016-09-14 20:14:24.36296+00	16	2224	11	2016-09-15 19:00:00+00	2016-09-16 04:00:00+00
977	I	2016-09-14 20:15:13.923886+00	16	2227	11	2016-09-20 13:00:00+00	2016-09-20 19:00:00+00
978	I	2016-09-14 20:15:13.923886+00	16	2228	11	2016-09-19 19:00:00+00	2016-09-20 04:00:00+00
981	I	2016-09-14 20:15:58.744949+00	16	2231	11	2016-09-22 13:00:00+00	2016-09-22 19:00:00+00
982	I	2016-09-14 20:15:58.744949+00	16	2232	11	2016-09-21 19:00:00+00	2016-09-22 04:00:00+00
985	I	2016-09-14 20:16:51.764187+00	16	2235	11	2016-09-25 19:00:00+00	2016-09-26 04:00:00+00
986	I	2016-09-14 20:16:51.764187+00	16	2236	11	2016-09-26 13:00:00+00	2016-09-26 19:00:00+00
989	I	2016-09-14 20:17:36.513061+00	16	2239	11	2016-09-27 19:00:00+00	2016-09-28 04:00:00+00
990	I	2016-09-14 20:17:36.513061+00	16	2240	11	2016-09-28 13:00:00+00	2016-09-28 19:00:00+00
993	I	2016-09-14 20:18:14.784917+00	16	2243	11	2016-09-30 13:00:00+00	2016-09-30 19:00:00+00
994	I	2016-09-14 20:18:14.784917+00	16	2244	11	2016-09-29 19:00:00+00	2016-09-30 04:00:00+00
996	I	2016-09-15 05:29:31.958445+00	94	2246	7	2016-09-15 11:00:00+00	2016-09-15 11:30:00+00
999	I	2016-09-15 07:21:03.116368+00	53	2249	14	2016-09-15 09:00:00+00	2016-09-15 09:30:00+00
1000	I	2016-09-15 11:52:15.870827+00	528	2250	8	2016-09-16 11:00:00+00	2016-09-16 12:00:00+00
1001	I	2016-09-15 11:53:55.377724+00	528	2251	8	2016-09-19 06:00:00+00	2016-09-19 07:00:00+00
1004	I	2016-09-15 12:53:32.525217+00	534	2254	7	2016-09-16 07:00:00+00	2016-09-16 07:30:00+00
1005	D	2016-09-15 14:00:01.421574+00	126	1508	7	2016-09-16 13:00:00+00	2016-09-16 14:00:00+00
1009	I	2016-09-16 06:52:14.013704+00	69	2258	7	2016-09-16 11:00:00+00	2016-09-16 12:00:00+00
1010	D	2016-09-16 07:22:56.981917+00	69	2258	7	2016-09-16 11:00:00+00	2016-09-16 12:00:00+00
1011	I	2016-09-16 07:26:32.414548+00	69	2259	7	2016-09-16 12:00:00+00	2016-09-16 13:00:00+00
1012	I	2016-09-16 08:14:55.877575+00	15	2260	7	2016-09-19 08:30:00+00	2016-09-19 10:00:00+00
1013	I	2016-09-16 12:12:07.004932+00	70	2261	7	2016-09-19 08:00:00+00	2016-09-19 08:30:00+00
1015	I	2016-09-16 13:03:20.166236+00	68	2263	7	2016-09-19 11:00:00+00	2016-09-19 11:30:00+00
1016	I	2016-09-19 10:33:59.855074+00	89	2264	7	2016-09-21 14:30:00+00	2016-09-21 15:00:00+00
1017	U	2016-09-20 03:47:04.965525+00	550	2233	11	2016-09-22 19:00:00+00	2016-09-23 01:00:00+00
1018	I	2016-09-20 03:47:04.965525+00	550	2265	11	2016-09-23 02:30:00+00	2016-09-23 04:00:00+00
1019	I	2016-09-20 03:47:04.965525+00	550	2266	11	2016-09-23 01:30:00+00	2016-09-23 02:00:00+00
1020	I	2016-09-20 03:47:10.341374+00	550	2267	11	2016-09-23 02:00:00+00	2016-09-23 02:30:00+00
1021	I	2016-09-20 08:07:07.643231+00	57	2268	7	2016-09-21 09:00:00+00	2016-09-21 10:00:00+00
1024	I	2016-09-20 10:45:31.584481+00	68	2271	7	2016-09-21 07:30:00+00	2016-09-21 08:00:00+00
1025	I	2016-09-20 12:19:58.418607+00	496	2272	11	2016-09-22 07:00:00+00	2016-09-22 07:30:00+00
1026	I	2016-09-20 12:29:06.511571+00	17	2273	7	2016-09-15 14:00:00+00	2016-09-15 15:00:00+00
1027	D	2016-09-20 12:29:09.433271+00	17	2273	7	2016-09-15 14:00:00+00	2016-09-15 15:00:00+00
1028	U	2016-09-20 12:29:53.518662+00	17	1068	7	2016-09-15 15:00:00+00	2016-09-15 19:30:00+00
1029	I	2016-09-20 12:29:53.518662+00	17	2274	7	2016-09-15 20:00:00+00	2016-09-15 21:00:00+00
1030	I	2016-09-20 12:29:56.400569+00	17	2275	7	2016-09-15 19:30:00+00	2016-09-15 20:00:00+00
1033	I	2016-09-20 13:14:32.295606+00	59	2278	7	2016-09-21 08:30:00+00	2016-09-21 09:00:00+00
1035	I	2016-09-20 13:37:40.202317+00	549	2280	7	2016-09-21 06:00:00+00	2016-09-21 07:00:00+00
1036	D	2016-09-20 13:37:44.157726+00	549	2280	7	2016-09-21 06:00:00+00	2016-09-21 07:00:00+00
1037	I	2016-09-20 13:41:24.203781+00	17	2281	7	2016-09-15 14:30:00+00	2016-09-15 15:00:00+00
1038	D	2016-09-20 13:41:27.731953+00	17	2281	7	2016-09-15 14:30:00+00	2016-09-15 15:00:00+00
1039	I	2016-09-21 06:53:18.112685+00	94	2282	7	2016-09-21 11:00:00+00	2016-09-21 11:30:00+00
1040	I	2016-09-21 09:35:10.365741+00	53	2283	14	2016-09-21 11:00:00+00	2016-09-21 11:30:00+00
1041	I	2016-09-21 10:33:16.693284+00	131	2284	7	2016-09-26 09:00:00+00	2016-09-26 09:30:00+00
1042	D	2016-09-21 11:27:13.946023+00	94	2282	7	2016-09-21 11:00:00+00	2016-09-21 11:30:00+00
1043	I	2016-09-21 11:28:28.845961+00	94	2285	7	2016-09-21 13:00:00+00	2016-09-21 13:30:00+00
1044	I	2016-09-22 06:27:33.932928+00	92	2286	7	2016-09-22 11:00:00+00	2016-09-22 11:30:00+00
1045	I	2016-09-22 08:26:16.625102+00	528	2287	8	2016-09-23 11:00:00+00	2016-09-23 12:00:00+00
1046	I	2016-09-22 08:27:00.049678+00	528	2288	8	2016-09-26 06:00:00+00	2016-09-26 07:00:00+00
1047	I	2016-09-23 04:02:21.148685+00	550	2289	11	2016-09-12 10:00:00+00	2016-09-12 11:00:00+00
1048	D	2016-09-23 04:02:36.356549+00	550	2289	11	2016-09-12 10:00:00+00	2016-09-12 11:00:00+00
1049	I	2016-09-23 04:04:49.716951+00	550	2290	11	2016-09-23 01:00:00+00	2016-09-23 01:30:00+00
1050	I	2016-09-23 04:04:58.933831+00	550	2291	11	2016-09-23 06:00:00+00	2016-09-23 07:00:00+00
1051	I	2016-09-23 04:04:58.933831+00	550	2292	11	2016-09-23 07:30:00+00	2016-09-23 08:00:00+00
1052	D	2016-09-23 04:05:08.87451+00	550	2291	11	2016-09-23 06:00:00+00	2016-09-23 07:00:00+00
1053	D	2016-09-23 04:05:10.982611+00	550	2292	11	2016-09-23 07:30:00+00	2016-09-23 08:00:00+00
1054	I	2016-09-23 04:05:23.839088+00	550	2293	11	2016-09-23 06:00:00+00	2016-09-23 06:30:00+00
1055	I	2016-09-23 04:05:35.269892+00	550	2294	11	2016-09-23 06:30:00+00	2016-09-23 07:00:00+00
1056	I	2016-09-23 04:05:51.43275+00	550	2295	11	2016-09-26 06:00:00+00	2016-09-26 07:00:00+00
1057	I	2016-09-23 04:06:03.164655+00	550	2296	11	2016-09-27 06:00:00+00	2016-09-27 07:00:00+00
1058	I	2016-09-23 04:06:03.164655+00	550	2297	11	2016-09-27 09:30:00+00	2016-09-27 10:00:00+00
1059	D	2016-09-23 04:06:05.479994+00	550	2297	11	2016-09-27 09:30:00+00	2016-09-27 10:00:00+00
1060	I	2016-09-23 04:06:26.295029+00	550	2298	11	2016-09-28 06:00:00+00	2016-09-28 07:00:00+00
1061	I	2016-09-23 04:06:32.215033+00	550	2299	11	2016-09-29 06:00:00+00	2016-09-29 07:00:00+00
1062	I	2016-09-23 04:06:37.572616+00	550	2300	11	2016-09-30 06:00:00+00	2016-09-30 07:00:00+00
1063	I	2016-09-23 06:17:20.909715+00	538	2301	7	2016-09-26 06:00:00+00	2016-09-26 09:00:00+00
1064	I	2016-09-23 07:13:33.499048+00	57	2302	7	2016-09-26 11:00:00+00	2016-09-26 12:00:00+00
1065	I	2016-09-23 08:56:52.694308+00	69	2303	7	2016-09-26 13:00:00+00	2016-09-26 14:30:00+00
1066	I	2016-09-23 10:04:23.505423+00	538	2304	14	2016-09-26 14:00:00+00	2016-09-26 15:00:00+00
1067	D	2016-09-23 10:04:54.511898+00	538	2301	7	2016-09-26 06:00:00+00	2016-09-26 09:00:00+00
1068	I	2016-09-23 10:18:12.499893+00	68	2305	7	2016-09-26 08:00:00+00	2016-09-26 08:30:00+00
1069	I	2016-09-23 12:28:12.514192+00	53	2306	7	2016-09-23 13:30:00+00	2016-09-23 14:00:00+00
1071	I	2016-09-23 13:40:54.867869+00	53	2308	7	2016-09-23 14:00:00+00	2016-09-23 14:30:00+00
1072	I	2016-09-26 04:13:09.661224+00	465	2309	14	2016-09-26 08:00:00+00	2016-09-26 08:30:00+00
1073	I	2016-09-26 04:13:47.568497+00	465	2310	14	2016-09-26 08:30:00+00	2016-09-26 09:00:00+00
1074	I	2016-09-26 04:29:01.193509+00	465	2311	14	2016-09-26 11:00:00+00	2016-09-26 12:00:00+00
1075	D	2016-09-26 04:29:07.967462+00	465	2309	14	2016-09-26 08:00:00+00	2016-09-26 08:30:00+00
1076	D	2016-09-26 04:29:10.324769+00	465	2310	14	2016-09-26 08:30:00+00	2016-09-26 09:00:00+00
1077	I	2016-09-26 06:55:14.473361+00	12	2312	7	2016-09-26 09:30:00+00	2016-09-26 10:00:00+00
1078	I	2016-09-26 06:56:17.037864+00	12	2313	7	2016-09-26 12:00:00+00	2016-09-26 12:30:00+00
1079	I	2016-09-26 06:57:08.603208+00	12	2314	7	2016-09-26 12:30:00+00	2016-09-26 13:00:00+00
1080	D	2016-09-26 06:58:48.815546+00	69	2303	7	2016-09-26 13:00:00+00	2016-09-26 14:30:00+00
1081	I	2016-09-26 07:03:15.729337+00	69	2315	7	2016-09-26 08:30:00+00	2016-09-26 09:00:00+00
1082	I	2016-09-26 08:42:53.274911+00	89	2316	7	2016-09-28 06:00:00+00	2016-09-28 06:30:00+00
1083	I	2016-09-26 11:14:55.158876+00	96	2317	7	2016-09-28 06:30:00+00	2016-09-28 07:30:00+00
1084	I	2016-09-26 12:18:14.027133+00	67	2318	7	2016-09-28 12:00:00+00	2016-09-28 13:00:00+00
1085	I	2016-09-27 07:29:25.139097+00	126	2319	7	2016-09-29 13:00:00+00	2016-09-29 14:00:00+00
1086	I	2016-09-27 12:38:14.327517+00	65	2320	14	2016-09-28 11:30:00+00	2016-09-28 12:00:00+00
1088	I	2016-09-27 12:59:43.153819+00	552	2322	7	2016-09-29 11:00:00+00	2016-09-29 11:30:00+00
1089	D	2016-09-27 12:59:45.893125+00	552	2322	7	2016-09-29 11:00:00+00	2016-09-29 11:30:00+00
1090	I	2016-09-27 13:01:54.678721+00	560	2323	12	2016-09-26 06:00:00+00	2016-09-26 06:30:00+00
1091	D	2016-09-27 13:02:03.088097+00	560	2323	12	2016-09-26 06:00:00+00	2016-09-26 06:30:00+00
1092	I	2016-09-27 13:04:34.102909+00	560	2324	14	2016-09-26 09:00:00+00	2016-09-26 09:30:00+00
1093	D	2016-09-27 13:04:36.181528+00	560	2324	14	2016-09-26 09:00:00+00	2016-09-26 09:30:00+00
1094	I	2016-09-27 13:04:43.707091+00	552	2325	14	2016-09-29 09:00:00+00	2016-09-29 09:30:00+00
1096	D	2016-09-28 09:03:30.059989+00	552	2325	14	2016-09-29 09:00:00+00	2016-09-29 09:30:00+00
1097	I	2016-09-28 09:07:18.79933+00	53	2327	7	2016-09-23 09:30:00+00	2016-09-23 10:00:00+00
1098	I	2016-09-28 09:08:57.921239+00	53	2328	14	2016-09-28 09:30:00+00	2016-09-28 10:00:00+00
1099	I	2016-09-28 09:13:11.409079+00	552	2329	7	2016-09-30 09:00:00+00	2016-09-30 09:30:00+00
1100	I	2016-09-28 11:17:47.80888+00	57	2330	7	2016-09-29 09:00:00+00	2016-09-29 10:00:00+00
1101	I	2016-09-28 12:29:47.480599+00	549	2331	7	2016-09-29 12:00:00+00	2016-09-29 13:00:00+00
1102	I	2016-09-28 12:42:27.710008+00	59	2332	7	2016-09-29 11:00:00+00	2016-09-29 11:30:00+00
1103	I	2016-09-29 04:53:04.170134+00	503	2333	14	2016-10-05 06:00:00+00	2016-10-05 06:30:00+00
1104	I	2016-09-29 05:18:40.935085+00	538	2334	7	2016-09-30 12:00:00+00	2016-09-30 15:00:00+00
1105	I	2016-09-29 05:19:48.457099+00	538	2335	7	2016-09-30 06:00:00+00	2016-09-30 09:00:00+00
1106	D	2016-09-29 06:28:52.988557+00	538	2335	7	2016-09-30 06:00:00+00	2016-09-30 09:00:00+00
1107	I	2016-09-29 07:55:16.475475+00	128	2336	7	2016-09-29 11:30:00+00	2016-09-29 12:00:00+00
1112	I	2016-09-29 08:48:11.136142+00	122	2341	7	2016-09-30 06:30:00+00	2016-09-30 07:00:00+00
1113	I	2016-09-29 11:40:41.86842+00	538	2342	14	2016-09-30 06:00:00+00	2016-09-30 09:00:00+00
1114	D	2016-09-29 11:49:07.777293+00	538	2342	14	2016-09-30 06:00:00+00	2016-09-30 09:00:00+00
1115	I	2016-09-29 12:06:34.611691+00	69	2343	7	2016-09-30 08:00:00+00	2016-09-30 09:00:00+00
1116	I	2016-09-30 07:41:13.367519+00	53	2344	14	2016-09-30 09:00:00+00	2016-09-30 09:30:00+00
1117	I	2016-09-30 08:34:09.235536+00	68	2345	7	2016-10-03 07:30:00+00	2016-10-03 08:00:00+00
1119	I	2016-09-30 09:47:56.411152+00	496	2347	11	2016-10-04 08:30:00+00	2016-10-04 09:00:00+00
1120	I	2016-09-30 10:23:27.405118+00	12	2348	14	2016-10-03 09:00:00+00	2016-10-03 10:00:00+00
1121	I	2016-09-30 10:26:07.977302+00	12	2349	14	2016-10-03 11:00:00+00	2016-10-03 12:00:00+00
1124	I	2016-10-03 04:43:45.956831+00	465	2352	14	2016-10-03 07:00:00+00	2016-10-03 08:00:00+00
1125	I	2016-10-03 06:28:58.927012+00	81	2353	7	2016-10-03 12:00:00+00	2016-10-03 13:00:00+00
1126	I	2016-10-03 07:33:14.382637+00	81	2354	7	2016-10-10 12:00:00+00	2016-10-10 13:00:00+00
1127	I	2016-10-03 09:02:49.042649+00	553	2355	7	2016-10-05 11:00:00+00	2016-10-05 14:00:00+00
1128	I	2016-10-04 07:53:34.244058+00	562	2356	14	2016-10-07 11:00:00+00	2016-10-07 12:00:00+00
1129	D	2016-10-04 07:54:07.187008+00	562	2356	14	2016-10-07 11:00:00+00	2016-10-07 12:00:00+00
1130	I	2016-10-04 08:05:06.651455+00	126	2357	7	2016-10-06 13:00:00+00	2016-10-06 14:00:00+00
1131	I	2016-10-04 08:05:43.824007+00	562	2358	14	2016-10-07 11:00:00+00	2016-10-07 12:00:00+00
1132	D	2016-10-04 08:36:30.506921+00	496	2347	11	2016-10-04 08:30:00+00	2016-10-04 09:00:00+00
1133	I	2016-10-04 08:37:37.891055+00	496	2359	11	2016-10-05 09:00:00+00	2016-10-05 09:30:00+00
1134	I	2016-10-04 10:54:04.886233+00	68	2360	7	2016-10-05 08:00:00+00	2016-10-05 08:30:00+00
1135	I	2016-10-04 11:25:06.110431+00	59	2361	7	2016-10-05 09:00:00+00	2016-10-05 09:30:00+00
1136	I	2016-10-04 13:12:55.918939+00	68	2362	7	2016-10-05 07:30:00+00	2016-10-05 08:00:00+00
1137	I	2016-10-05 04:50:54.248083+00	70	2364	7	2016-10-05 08:30:00+00	2016-10-05 09:00:00+00
1138	I	2016-10-05 05:02:54.507721+00	503	2365	8	2016-10-18 06:00:00+00	2016-10-18 06:30:00+00
1139	I	2016-10-05 05:10:32.406182+00	538	2366	7	2016-10-06 06:00:00+00	2016-10-06 09:00:00+00
1140	I	2016-10-05 06:16:02.18637+00	503	2367	7	2016-10-12 06:00:00+00	2016-10-12 06:30:00+00
1141	I	2016-10-05 07:47:29.561929+00	53	2368	7	2016-10-05 09:30:00+00	2016-10-05 10:00:00+00
1142	I	2016-10-06 05:10:01.929415+00	503	2369	11	2016-10-13 04:00:00+00	2016-10-13 04:30:00+00
1143	I	2016-10-06 09:26:14.501754+00	559	2370	10	2016-10-11 11:00:00+00	2016-10-11 13:00:00+00
1144	I	2016-10-06 12:19:44.578494+00	11	2371	7	2016-10-20 13:30:00+00	2016-10-20 14:00:00+00
1145	D	2016-10-06 12:26:39.640548+00	11	2371	7	2016-10-20 13:30:00+00	2016-10-20 14:00:00+00
1146	I	2016-10-06 12:43:49.743016+00	63	2372	7	2016-10-20 06:00:00+00	2016-10-20 06:30:00+00
1147	D	2016-10-06 12:44:36.833267+00	63	2372	7	2016-10-20 06:00:00+00	2016-10-20 06:30:00+00
1148	I	2016-10-06 12:52:00.739289+00	465	2373	14	2016-10-07 09:00:00+00	2016-10-07 10:00:00+00
1149	I	2016-10-06 14:59:52.002893+00	549	2374	7	2016-10-07 09:00:00+00	2016-10-07 10:00:00+00
1150	I	2016-10-07 05:44:50.239552+00	12	2375	7	2016-10-07 11:00:00+00	2016-10-07 11:30:00+00
1151	I	2016-10-07 07:23:14.031161+00	53	2376	7	2016-10-07 08:30:00+00	2016-10-07 09:00:00+00
1152	I	2016-10-07 12:03:10.532441+00	68	2377	7	2016-10-10 08:00:00+00	2016-10-10 08:30:00+00
1153	I	2016-10-07 12:03:15.225367+00	68	2378	7	2016-10-10 08:30:00+00	2016-10-10 09:00:00+00
1154	I	2016-10-10 08:12:12.54367+00	53	2379	7	2016-10-10 09:30:00+00	2016-10-10 10:00:00+00
1155	I	2016-10-10 08:34:57.87472+00	12	2380	7	2016-10-10 09:00:00+00	2016-10-10 09:30:00+00
1156	I	2016-10-10 08:35:51.528603+00	12	2381	7	2016-10-10 11:00:00+00	2016-10-10 11:30:00+00
1157	I	2016-10-11 06:23:54.674808+00	14	2382	12	2016-10-20 04:30:00+00	2016-10-20 05:00:00+00
1158	D	2016-10-11 06:24:14.617824+00	14	2382	12	2016-10-20 04:30:00+00	2016-10-20 05:00:00+00
1159	I	2016-10-11 07:43:51.485711+00	503	2383	8	2016-10-25 06:00:00+00	2016-10-25 06:30:00+00
1161	I	2016-10-11 11:17:07.466074+00	59	2385	7	2016-10-05 07:00:00+00	2016-10-05 07:30:00+00
1166	I	2016-10-12 11:11:18.016314+00	496	2389	11	2016-10-13 06:30:00+00	2016-10-13 07:00:00+00
1168	I	2016-10-12 13:20:32.756345+00	12	2391	14	2016-10-13 09:00:00+00	2016-10-13 10:00:00+00
1169	I	2016-10-12 13:21:36.296107+00	12	2392	14	2016-10-13 11:00:00+00	2016-10-13 11:30:00+00
1172	I	2016-10-13 01:08:05.259237+00	503	2394	7	2016-10-19 06:00:00+00	2016-10-19 06:30:00+00
1173	I	2016-10-13 05:19:55.423094+00	12	2395	14	2016-10-13 08:00:00+00	2016-10-13 09:00:00+00
1174	D	2016-10-13 05:20:01.183045+00	12	2393	14	2016-10-13 09:00:00+00	2016-10-13 09:30:00+00
1177	I	2016-10-13 07:39:43.182041+00	53	2398	7	2016-10-13 09:00:00+00	2016-10-13 09:30:00+00
1178	I	2016-10-13 08:17:53.112906+00	12	2399	14	2016-10-13 11:30:00+00	2016-10-13 12:00:00+00
1183	I	2016-10-14 06:26:42.904606+00	126	2404	7	2016-10-14 11:00:00+00	2016-10-14 12:00:00+00
1185	I	2016-10-14 09:15:30.33591+00	70	2406	7	2016-10-17 07:30:00+00	2016-10-17 08:00:00+00
1188	D	2016-10-14 10:29:50.429977+00	538	2407	7	2016-10-17 06:00:00+00	2016-10-17 07:30:00+00
1190	I	2016-10-14 10:42:00.497403+00	69	2410	7	2016-10-17 06:00:00+00	2016-10-17 07:00:00+00
1191	I	2016-10-14 13:16:50.547413+00	68	2411	7	2016-10-17 08:00:00+00	2016-10-17 10:00:00+00
1192	D	2016-10-17 03:37:17.757322+00	503	2365	8	2016-10-18 06:00:00+00	2016-10-18 06:30:00+00
1194	I	2016-10-17 07:39:30.211302+00	12	2413	14	2016-10-17 11:00:00+00	2016-10-17 12:00:00+00
1195	I	2016-10-17 08:54:46.489185+00	53	2414	14	2016-10-14 09:30:00+00	2016-10-14 10:00:00+00
1198	I	2016-10-18 07:36:02.030184+00	553	2417	7	2016-10-20 11:00:00+00	2016-10-20 14:00:00+00
1201	I	2016-10-18 11:46:39.898596+00	562	2420	14	2016-10-20 11:00:00+00	2016-10-20 12:00:00+00
1207	I	2016-10-19 08:43:43.932915+00	64	2426	7	2016-10-20 07:00:00+00	2016-10-20 09:00:00+00
1212	I	2016-10-19 09:01:02.276875+00	89	2430	7	2016-10-21 06:00:00+00	2016-10-21 06:30:00+00
1219	I	2016-10-19 10:47:48.29029+00	567	2439	15	2016-10-20 10:00:00+00	2016-10-20 11:00:00+00
1220	I	2016-10-19 10:47:48.29029+00	567	2440	15	2016-10-19 21:00:00+00	2016-10-20 06:00:00+00
1221	I	2016-10-19 10:47:48.29029+00	567	2441	15	2016-10-20 15:00:00+00	2016-10-20 21:00:00+00
1162	D	2016-10-12 01:37:33.820017+00	503	2383	8	2016-10-25 06:00:00+00	2016-10-25 06:30:00+00
1163	I	2016-10-12 03:38:13.572167+00	503	2386	8	2016-10-26 06:00:00+00	2016-10-26 06:30:00+00
1164	I	2016-10-12 03:44:49.487765+00	503	2387	9	2016-10-25 06:00:00+00	2016-10-25 06:30:00+00
1165	I	2016-10-12 09:07:25.924292+00	538	2388	7	2016-10-13 06:00:00+00	2016-10-13 09:00:00+00
1167	I	2016-10-12 12:20:43.722847+00	128	2390	7	2016-10-13 11:00:00+00	2016-10-13 11:30:00+00
1170	D	2016-10-12 13:28:10.386341+00	12	2391	14	2016-10-13 09:00:00+00	2016-10-13 10:00:00+00
1171	I	2016-10-12 13:28:55.135927+00	12	2393	14	2016-10-13 09:00:00+00	2016-10-13 09:30:00+00
1175	I	2016-10-13 06:48:10.67703+00	503	2396	11	2016-10-19 08:00:00+00	2016-10-19 08:30:00+00
1176	I	2016-10-13 07:04:52.105851+00	562	2397	14	2016-10-14 11:00:00+00	2016-10-14 12:00:00+00
1179	I	2016-10-13 11:43:21.560235+00	549	2400	7	2016-10-14 12:00:00+00	2016-10-14 13:00:00+00
1182	I	2016-10-14 06:12:24.849601+00	57	2403	7	2016-10-14 09:00:00+00	2016-10-14 10:00:00+00
1184	I	2016-10-14 07:56:03.679406+00	53	2405	14	2016-10-14 09:00:00+00	2016-10-14 09:30:00+00
1186	I	2016-10-14 09:57:03.816674+00	538	2407	7	2016-10-17 06:00:00+00	2016-10-17 07:30:00+00
1187	I	2016-10-14 09:57:15.952322+00	538	2408	7	2016-10-17 12:00:00+00	2016-10-17 15:00:00+00
1189	I	2016-10-14 10:34:12.633069+00	81	2409	7	2016-10-17 11:00:00+00	2016-10-17 12:00:00+00
1193	I	2016-10-17 04:10:33.516446+00	465	2412	14	2016-10-17 08:30:00+00	2016-10-17 10:00:00+00
1196	I	2016-10-17 09:15:58.416326+00	53	2415	14	2016-10-17 12:00:00+00	2016-10-17 12:30:00+00
1197	I	2016-10-17 13:17:32.302399+00	53	2416	14	2016-10-17 14:00:00+00	2016-10-17 14:30:00+00
1199	I	2016-10-18 08:31:26.686853+00	126	2418	7	2016-10-20 14:00:00+00	2016-10-20 15:00:00+00
1200	I	2016-10-18 10:48:21.109163+00	128	2419	7	2016-10-19 11:00:00+00	2016-10-19 11:30:00+00
1202	I	2016-10-18 12:15:15.937455+00	59	2421	7	2016-10-19 08:00:00+00	2016-10-19 08:30:00+00
1208	D	2016-10-19 08:45:21.381794+00	64	2426	7	2016-10-20 07:00:00+00	2016-10-20 09:00:00+00
1209	I	2016-10-19 08:45:37.21022+00	64	2427	7	2016-10-20 07:00:00+00	2016-10-20 10:00:00+00
1210	I	2016-10-19 08:53:54.963754+00	503	2428	7	2016-10-26 06:00:00+00	2016-10-26 06:30:00+00
1213	I	2016-10-19 10:47:05.852406+00	567	2433	15	2016-10-16 21:00:00+00	2016-10-17 06:00:00+00
1214	I	2016-10-19 10:47:13.711448+00	567	2434	15	2016-10-17 15:00:00+00	2016-10-17 21:00:00+00
1215	I	2016-10-19 10:47:13.711448+00	567	2435	15	2016-10-17 10:00:00+00	2016-10-17 11:00:00+00
1216	I	2016-10-19 10:47:31.730773+00	567	2436	15	2016-10-19 10:00:00+00	2016-10-19 11:00:00+00
1217	I	2016-10-19 10:47:31.730773+00	567	2437	15	2016-10-18 21:00:00+00	2016-10-19 06:00:00+00
1218	I	2016-10-19 10:47:31.730773+00	567	2438	15	2016-10-19 15:00:00+00	2016-10-19 21:00:00+00
1222	I	2016-10-19 10:52:04.705997+00	567	2442	15	2016-10-20 21:00:00+00	2016-10-21 06:00:00+00
1223	I	2016-10-19 10:52:04.705997+00	567	2443	15	2016-10-21 15:00:00+00	2016-10-21 21:00:00+00
1224	I	2016-10-19 10:52:04.705997+00	567	2444	15	2016-10-21 10:00:00+00	2016-10-21 11:00:00+00
1225	I	2016-10-19 10:52:28.567259+00	567	2445	15	2016-10-21 21:00:00+00	2016-10-22 21:00:00+00
1226	I	2016-10-19 10:52:52.968897+00	567	2446	15	2016-10-22 21:00:00+00	2016-10-23 21:00:00+00
1227	I	2016-10-19 10:53:09.898049+00	567	2447	15	2016-10-24 10:00:00+00	2016-10-24 11:00:00+00
1228	I	2016-10-19 10:53:09.898049+00	567	2448	15	2016-10-23 21:00:00+00	2016-10-24 06:00:00+00
1229	I	2016-10-19 10:53:09.898049+00	567	2449	15	2016-10-24 15:00:00+00	2016-10-24 21:00:00+00
1230	I	2016-10-19 10:53:24.382206+00	567	2450	15	2016-10-24 21:00:00+00	2016-10-25 06:00:00+00
1231	I	2016-10-19 10:53:24.382206+00	567	2451	15	2016-10-25 10:00:00+00	2016-10-25 11:00:00+00
1232	I	2016-10-19 10:53:24.382206+00	567	2452	15	2016-10-25 15:00:00+00	2016-10-25 21:00:00+00
1233	I	2016-10-19 10:53:38.497599+00	567	2453	15	2016-10-26 15:00:00+00	2016-10-26 21:00:00+00
1234	I	2016-10-19 10:53:38.497599+00	567	2454	15	2016-10-26 10:00:00+00	2016-10-26 11:00:00+00
1235	I	2016-10-19 10:53:38.497599+00	567	2455	15	2016-10-25 21:00:00+00	2016-10-26 06:00:00+00
1236	I	2016-10-19 10:53:54.937106+00	567	2456	15	2016-10-27 15:00:00+00	2016-10-27 21:00:00+00
1237	I	2016-10-19 10:53:54.937106+00	567	2457	15	2016-10-26 21:00:00+00	2016-10-27 06:00:00+00
1238	I	2016-10-19 10:53:54.937106+00	567	2458	15	2016-10-27 10:00:00+00	2016-10-27 11:00:00+00
1239	I	2016-10-19 10:54:09.054678+00	567	2459	15	2016-10-28 10:00:00+00	2016-10-28 11:00:00+00
1240	I	2016-10-19 10:54:09.054678+00	567	2460	15	2016-10-27 21:00:00+00	2016-10-28 06:00:00+00
1241	I	2016-10-19 10:54:09.054678+00	567	2461	15	2016-10-28 15:00:00+00	2016-10-28 21:00:00+00
1242	I	2016-10-19 10:54:30.278573+00	567	2462	15	2016-10-28 21:00:00+00	2016-10-29 21:00:00+00
1243	I	2016-10-19 10:54:54.758208+00	567	2463	15	2016-10-29 21:00:00+00	2016-10-30 21:00:00+00
1244	I	2016-10-19 10:55:16.156967+00	567	2464	15	2016-10-31 15:00:00+00	2016-10-31 21:00:00+00
1245	I	2016-10-19 10:55:16.156967+00	567	2465	15	2016-10-31 10:00:00+00	2016-10-31 11:00:00+00
1246	I	2016-10-19 10:55:16.156967+00	567	2466	15	2016-10-30 21:00:00+00	2016-10-31 06:00:00+00
1247	I	2016-10-19 10:57:54.631579+00	567	2467	15	2016-11-01 10:00:00+00	2016-11-01 11:00:00+00
1248	I	2016-10-19 10:57:54.631579+00	567	2468	15	2016-11-01 15:00:00+00	2016-11-01 21:00:00+00
1249	I	2016-10-19 10:57:54.631579+00	567	2469	15	2016-10-31 21:00:00+00	2016-11-01 06:00:00+00
1250	I	2016-10-19 10:58:11.647408+00	567	2470	15	2016-11-02 10:00:00+00	2016-11-02 11:00:00+00
1251	I	2016-10-19 10:58:11.647408+00	567	2471	15	2016-11-01 21:00:00+00	2016-11-02 06:00:00+00
1252	I	2016-10-19 10:58:11.647408+00	567	2472	15	2016-11-02 15:00:00+00	2016-11-02 21:00:00+00
1253	I	2016-10-19 10:58:26.566546+00	567	2473	15	2016-11-02 21:00:00+00	2016-11-03 06:00:00+00
1254	I	2016-10-19 10:58:26.566546+00	567	2474	15	2016-11-03 10:00:00+00	2016-11-03 11:00:00+00
1255	I	2016-10-19 10:58:26.566546+00	567	2475	15	2016-11-03 15:00:00+00	2016-11-03 21:00:00+00
1256	I	2016-10-19 10:58:44.385293+00	567	2476	15	2016-11-04 15:00:00+00	2016-11-04 21:00:00+00
1257	I	2016-10-19 10:58:44.385293+00	567	2477	15	2016-11-03 21:00:00+00	2016-11-04 06:00:00+00
1258	I	2016-10-19 10:58:44.385293+00	567	2478	15	2016-11-04 10:00:00+00	2016-11-04 11:00:00+00
1259	I	2016-10-19 10:59:06.16945+00	567	2479	15	2016-11-04 21:00:00+00	2016-11-05 21:00:00+00
1260	I	2016-10-19 10:59:25.742067+00	567	2480	15	2016-11-05 21:00:00+00	2016-11-06 21:00:00+00
1261	I	2016-10-19 10:59:48.746434+00	567	2481	15	2016-10-17 21:00:00+00	2016-10-18 06:00:00+00
1262	I	2016-10-19 10:59:48.746434+00	567	2482	15	2016-10-18 10:00:00+00	2016-10-18 11:00:00+00
1263	I	2016-10-19 10:59:48.746434+00	567	2483	15	2016-10-18 15:00:00+00	2016-10-18 21:00:00+00
1266	I	2016-10-19 11:41:24.237964+00	555	2486	7	2016-10-20 06:00:00+00	2016-10-20 06:30:00+00
1267	I	2016-10-20 02:13:32.52728+00	503	2487	11	2016-10-26 08:00:00+00	2016-10-26 08:30:00+00
1268	I	2016-10-20 05:53:15.112135+00	562	2488	14	2016-10-21 11:00:00+00	2016-10-21 11:30:00+00
1269	I	2016-10-20 05:53:25.382124+00	562	2489	14	2016-10-21 11:30:00+00	2016-10-21 12:00:00+00
1270	I	2016-10-20 09:36:04.784042+00	53	2490	14	2016-10-20 12:00:00+00	2016-10-20 12:30:00+00
1271	I	2016-10-20 12:22:12.963589+00	69	2491	7	2016-10-21 06:30:00+00	2016-10-21 07:30:00+00
1272	I	2016-10-21 08:36:45.556675+00	81	2492	7	2016-10-24 12:00:00+00	2016-10-24 13:00:00+00
1273	I	2016-10-21 10:28:03.800818+00	68	2493	7	2016-10-24 07:30:00+00	2016-10-24 09:00:00+00
1274	I	2016-10-21 11:28:45.356811+00	88	2494	7	2016-10-24 09:30:00+00	2016-10-24 10:00:00+00
1275	I	2016-10-21 11:29:07.186898+00	88	2495	7	2016-10-24 11:00:00+00	2016-10-24 11:30:00+00
1278	I	2016-10-24 05:31:51.762622+00	465	2498	14	2016-10-24 13:00:00+00	2016-10-24 14:00:00+00
1280	D	2016-10-24 07:09:52.526589+00	465	2498	14	2016-10-24 13:00:00+00	2016-10-24 14:00:00+00
1281	I	2016-10-24 07:11:05.387404+00	465	2500	14	2016-10-24 11:00:00+00	2016-10-24 12:00:00+00
1285	I	2016-10-24 07:54:38.091533+00	16	2672	11	2016-10-31 19:00:00+00	2016-11-01 04:00:00+00
1286	I	2016-10-24 07:54:40.570204+00	16	2673	11	2016-11-01 08:00:00+00	2016-11-01 09:00:00+00
1288	I	2016-10-24 08:27:25.567511+00	94	3311	7	2016-10-24 13:00:00+00	2016-10-24 13:30:00+00
1289	I	2016-10-24 08:43:16.509732+00	17	3312	7	2016-11-01 06:00:00+00	2016-11-01 15:00:00+00
1292	I	2016-10-24 08:44:02.196818+00	17	3315	7	2016-11-22 06:00:00+00	2016-11-22 15:00:00+00
1295	I	2016-10-24 08:48:40.609075+00	17	3318	7	2016-11-26 06:00:00+00	2016-11-26 15:00:00+00
1297	I	2016-10-24 08:49:04.092575+00	17	3320	7	2016-11-19 06:00:00+00	2016-11-19 15:00:00+00
1299	I	2016-10-24 08:49:24.603626+00	17	3322	7	2016-11-12 06:00:00+00	2016-11-12 15:00:00+00
1301	I	2016-10-24 08:49:43.8948+00	17	3324	7	2016-11-05 06:00:00+00	2016-11-05 15:00:00+00
1303	I	2016-10-24 08:49:58.682139+00	17	3326	7	2016-11-03 10:00:00+00	2016-11-03 11:00:00+00
1305	I	2016-10-24 08:50:19.961386+00	17	3328	7	2016-11-07 10:00:00+00	2016-11-07 11:00:00+00
1307	I	2016-10-24 08:50:26.501979+00	17	3330	7	2016-11-10 10:00:00+00	2016-11-10 11:00:00+00
1308	I	2016-10-24 08:50:31.821753+00	17	3331	7	2016-11-11 10:00:00+00	2016-11-11 11:00:00+00
1310	I	2016-10-24 08:50:40.304484+00	17	3333	7	2016-11-16 10:00:00+00	2016-11-16 11:00:00+00
1312	I	2016-10-24 08:50:47.709415+00	17	3335	7	2016-11-18 10:00:00+00	2016-11-18 11:00:00+00
1314	I	2016-10-24 08:50:55.401184+00	17	3337	7	2016-11-23 10:00:00+00	2016-11-23 11:00:00+00
1316	I	2016-10-24 08:51:02.480467+00	17	3339	7	2016-11-25 10:00:00+00	2016-11-25 11:00:00+00
1319	D	2016-10-24 08:56:50.435026+00	68	2497	7	2016-10-24 09:00:00+00	2016-10-24 09:30:00+00
1322	I	2016-10-24 09:08:49.898408+00	64	3344	11	2016-10-25 07:00:00+00	2016-10-25 11:00:00+00
1323	I	2016-10-25 05:48:55.489901+00	538	3345	14	2016-10-26 06:00:00+00	2016-10-26 09:00:00+00
1329	I	2016-10-25 10:05:07.403533+00	128	3351	7	2016-10-26 11:00:00+00	2016-10-26 11:30:00+00
1331	I	2016-10-25 13:01:36.428842+00	59	3353	7	2016-10-26 08:00:00+00	2016-10-26 08:30:00+00
1332	I	2016-10-25 13:42:53.036794+00	555	3354	7	2016-10-26 09:00:00+00	2016-10-26 09:30:00+00
1334	I	2016-10-26 02:39:13.27041+00	503	3355	7	2016-10-27 06:00:00+00	2016-10-27 06:30:00+00
1336	I	2016-10-26 02:40:56.24764+00	503	3356	9	2016-10-27 06:00:00+00	2016-10-27 06:30:00+00
1337	I	2016-10-26 07:38:59.081359+00	503	3357	7	2016-11-02 06:00:00+00	2016-11-02 06:30:00+00
1339	I	2016-10-26 10:40:44.459238+00	68	3359	7	2016-10-31 06:00:00+00	2016-10-31 10:00:00+00
1344	I	2016-10-27 11:14:04.047689+00	549	3364	7	2016-10-28 12:30:00+00	2016-10-28 13:30:00+00
1345	D	2016-10-28 04:43:43.530368+00	503	3361	11	2016-11-02 08:00:00+00	2016-11-02 08:30:00+00
1349	I	2016-10-28 05:15:09.227135+00	562	3368	14	2016-10-28 09:00:00+00	2016-10-28 10:00:00+00
1354	I	2016-10-28 08:08:45.855423+00	70	3373	7	2016-10-31 11:00:00+00	2016-10-31 11:30:00+00
1355	I	2016-10-28 11:10:55.227256+00	81	3374	7	2016-10-31 11:30:00+00	2016-10-31 12:30:00+00
1356	I	2016-10-28 11:12:22.227251+00	69	3376	7	2016-10-31 12:30:00+00	2016-10-31 13:30:00+00
1358	I	2016-10-28 12:27:32.986125+00	549	3378	7	2016-10-31 13:30:00+00	2016-10-31 14:30:00+00
1360	I	2016-10-31 07:18:49.099468+00	555	3380	11	2016-10-31 08:30:00+00	2016-10-31 09:00:00+00
1361	I	2016-10-31 08:31:24.834384+00	17	3381	14	2016-11-02 10:00:00+00	2016-11-02 11:00:00+00
1362	I	2016-10-31 08:31:24.834384+00	17	3382	14	2016-11-01 21:00:00+00	2016-11-02 06:00:00+00
1363	I	2016-10-31 08:31:24.834384+00	17	3383	14	2016-11-02 15:00:00+00	2016-11-02 21:00:00+00
1365	I	2016-10-31 08:32:01.632895+00	17	3385	14	2016-11-03 10:00:00+00	2016-11-03 11:00:00+00
1366	I	2016-10-31 08:32:01.632895+00	17	3386	14	2016-11-02 21:00:00+00	2016-11-03 06:00:00+00
1367	I	2016-10-31 08:32:01.632895+00	17	3387	14	2016-11-03 15:00:00+00	2016-11-03 21:00:00+00
1371	I	2016-10-31 08:32:40.956203+00	17	3391	14	2016-11-04 21:00:00+00	2016-11-05 21:00:00+00
1373	I	2016-10-31 08:33:21.35786+00	17	3393	14	2016-11-07 15:00:00+00	2016-11-07 21:00:00+00
1374	I	2016-10-31 08:33:21.35786+00	17	3394	14	2016-11-06 21:00:00+00	2016-11-07 06:00:00+00
1375	I	2016-10-31 08:33:21.35786+00	17	3395	14	2016-11-07 10:00:00+00	2016-11-07 11:00:00+00
1378	I	2016-10-31 09:29:52.351569+00	64	3398	7	2016-11-03 07:00:00+00	2016-11-03 10:00:00+00
1276	I	2016-10-24 04:17:23.253143+00	503	2496	11	2016-10-31 08:00:00+00	2016-10-31 08:30:00+00
1277	I	2016-10-24 05:29:59.849968+00	68	2497	7	2016-10-24 09:00:00+00	2016-10-24 09:30:00+00
1279	I	2016-10-24 07:06:23.744785+00	14	2499	15	2016-10-24 09:00:00+00	2016-10-24 09:30:00+00
1282	I	2016-10-24 07:44:21.60064+00	12	2501	7	2016-10-24 11:30:00+00	2016-10-24 12:00:00+00
1284	I	2016-10-24 07:47:29.399951+00	555	2503	11	2016-10-25 06:00:00+00	2016-10-25 06:30:00+00
1290	I	2016-10-24 08:43:33.737901+00	17	3313	7	2016-11-08 06:00:00+00	2016-11-08 15:00:00+00
1291	I	2016-10-24 08:43:52.236257+00	17	3314	7	2016-11-15 06:00:00+00	2016-11-15 15:00:00+00
1293	I	2016-10-24 08:48:11.98986+00	17	3316	7	2016-11-29 06:00:00+00	2016-11-29 15:00:00+00
1294	I	2016-10-24 08:48:30.559671+00	17	3317	7	2016-11-27 06:00:00+00	2016-11-27 15:00:00+00
1296	I	2016-10-24 08:48:52.666047+00	17	3319	7	2016-11-20 06:00:00+00	2016-11-20 15:00:00+00
1298	I	2016-10-24 08:49:13.943692+00	17	3321	7	2016-11-13 06:00:00+00	2016-11-13 15:00:00+00
1300	I	2016-10-24 08:49:33.523105+00	17	3323	7	2016-11-06 06:00:00+00	2016-11-06 15:00:00+00
1302	I	2016-10-24 08:49:55.123159+00	17	3325	7	2016-11-02 10:00:00+00	2016-11-02 11:00:00+00
1304	I	2016-10-24 08:50:02.563187+00	17	3327	7	2016-11-04 10:00:00+00	2016-11-04 11:00:00+00
1306	I	2016-10-24 08:50:23.040936+00	17	3329	7	2016-11-09 10:00:00+00	2016-11-09 11:00:00+00
1309	I	2016-10-24 08:50:36.484297+00	17	3332	7	2016-11-14 10:00:00+00	2016-11-14 11:00:00+00
1311	I	2016-10-24 08:50:43.961242+00	17	3334	7	2016-11-17 10:00:00+00	2016-11-17 11:00:00+00
1313	I	2016-10-24 08:50:51.201107+00	17	3336	7	2016-11-21 10:00:00+00	2016-11-21 11:00:00+00
1315	I	2016-10-24 08:50:59.400855+00	17	3338	7	2016-11-24 10:00:00+00	2016-11-24 11:00:00+00
1317	I	2016-10-24 08:51:06.960577+00	17	3340	7	2016-11-28 10:00:00+00	2016-11-28 11:00:00+00
1318	I	2016-10-24 08:51:12.240371+00	17	3341	7	2016-11-30 10:00:00+00	2016-11-30 11:00:00+00
1324	I	2016-10-25 07:52:35.76967+00	64	3346	7	2016-10-28 07:00:00+00	2016-10-28 10:00:00+00
1325	I	2016-10-25 08:28:18.961129+00	67	3347	7	2016-10-26 12:00:00+00	2016-10-26 12:30:00+00
1326	I	2016-10-25 08:28:24.566183+00	70	3348	7	2016-10-26 07:30:00+00	2016-10-26 08:00:00+00
1327	I	2016-10-25 08:33:17.598152+00	67	3349	7	2016-10-26 12:30:00+00	2016-10-26 13:00:00+00
1328	I	2016-10-25 09:59:16.424112+00	126	3350	7	2016-10-27 13:30:00+00	2016-10-27 14:30:00+00
1330	I	2016-10-25 10:11:17.646112+00	496	3352	11	2016-10-26 07:30:00+00	2016-10-26 08:00:00+00
1333	D	2016-10-26 02:30:13.915301+00	503	2428	7	2016-10-26 06:00:00+00	2016-10-26 06:30:00+00
1335	D	2016-10-26 02:39:28.866376+00	503	3355	7	2016-10-27 06:00:00+00	2016-10-27 06:30:00+00
1338	I	2016-10-26 08:09:01.762612+00	57	3358	7	2016-10-28 11:00:00+00	2016-10-28 12:30:00+00
1340	I	2016-10-26 12:31:37.268266+00	89	3360	7	2016-10-28 06:00:00+00	2016-10-28 06:30:00+00
1341	I	2016-10-27 09:22:54.998524+00	503	3361	11	2016-11-02 08:00:00+00	2016-11-02 08:30:00+00
1343	I	2016-10-27 11:11:59.036326+00	555	3363	11	2016-10-28 04:00:00+00	2016-10-28 04:30:00+00
1346	I	2016-10-28 04:45:08.137335+00	503	3365	11	2016-11-03 08:00:00+00	2016-11-03 08:30:00+00
1347	I	2016-10-28 04:59:35.722309+00	538	3366	14	2016-10-31 06:00:00+00	2016-10-31 09:00:00+00
1348	I	2016-10-28 04:59:59.129644+00	538	3367	14	2016-10-31 09:00:00+00	2016-10-31 10:00:00+00
1352	I	2016-10-28 07:05:42.18975+00	559	3371	10	2016-11-01 08:00:00+00	2016-11-01 14:00:00+00
1353	I	2016-10-28 07:08:37.792239+00	559	3372	15	2016-11-03 11:00:00+00	2016-11-03 14:00:00+00
1357	I	2016-10-28 11:57:51.330582+00	53	3377	14	2016-10-28 13:00:00+00	2016-10-28 13:30:00+00
1359	I	2016-10-28 13:10:01.469604+00	465	3379	14	2016-10-31 11:00:00+00	2016-10-31 12:00:00+00
1364	I	2016-10-31 08:31:45.213574+00	17	3384	14	2016-10-31 21:00:00+00	2016-11-01 21:00:00+00
1368	I	2016-10-31 08:32:17.932464+00	17	3388	14	2016-11-03 21:00:00+00	2016-11-04 06:00:00+00
1369	I	2016-10-31 08:32:17.932464+00	17	3389	14	2016-11-04 10:00:00+00	2016-11-04 11:00:00+00
1370	I	2016-10-31 08:32:17.932464+00	17	3390	14	2016-11-04 15:00:00+00	2016-11-04 21:00:00+00
1372	I	2016-10-31 08:33:05.515535+00	17	3392	14	2016-11-05 21:00:00+00	2016-11-06 21:00:00+00
1376	I	2016-10-31 08:33:49.39531+00	17	3396	14	2016-11-07 21:00:00+00	2016-11-08 21:00:00+00
1379	I	2016-11-02 06:57:35.454634+00	57	3402	7	2016-11-02 08:00:00+00	2016-11-02 10:00:00+00
1385	I	2016-11-02 07:17:34.849933+00	64	3417	11	2016-11-08 05:00:00+00	2016-11-08 07:00:00+00
1386	D	2016-11-02 07:17:49.490449+00	64	3417	11	2016-11-08 05:00:00+00	2016-11-08 07:00:00+00
1388	I	2016-11-02 07:19:15.877703+00	64	3419	11	2016-11-08 05:00:00+00	2016-11-08 09:00:00+00
1389	D	2016-11-02 07:20:22.291053+00	64	3419	11	2016-11-08 05:00:00+00	2016-11-08 09:00:00+00
1391	I	2016-11-02 07:39:22.90724+00	64	3430	11	2016-11-08 07:00:00+00	2016-11-08 11:00:00+00
1392	I	2016-11-02 10:20:20.400202+00	12	3431	7	2016-11-03 11:00:00+00	2016-11-03 11:30:00+00
1393	I	2016-11-02 10:21:07.74128+00	12	3432	7	2016-11-03 11:30:00+00	2016-11-03 12:00:00+00
1394	I	2016-11-02 10:22:43.338099+00	12	3433	7	2016-11-03 12:00:00+00	2016-11-03 12:30:00+00
1397	I	2016-11-02 11:16:35.832335+00	75	3436	14	2016-11-03 11:00:00+00	2016-11-03 11:30:00+00
1398	I	2016-11-02 12:08:15.55979+00	126	3437	7	2016-11-03 13:00:00+00	2016-11-03 14:00:00+00
1400	I	2016-11-02 12:42:10.299065+00	549	3439	14	2016-11-03 13:00:00+00	2016-11-03 14:00:00+00
1401	I	2016-11-02 13:35:24.651507+00	12	3440	7	2016-11-03 06:30:00+00	2016-11-03 07:00:00+00
1402	I	2016-11-03 06:02:36.358797+00	562	3446	14	2016-11-03 11:30:00+00	2016-11-03 12:30:00+00
1403	I	2016-11-03 06:48:09.08645+00	81	3447	7	2016-11-07 11:00:00+00	2016-11-07 12:00:00+00
1404	I	2016-11-03 06:48:51.724284+00	69	3448	7	2016-11-07 06:00:00+00	2016-11-07 07:00:00+00
1405	I	2016-11-03 06:57:35.408192+00	68	3449	7	2016-11-07 07:00:00+00	2016-11-07 10:00:00+00
1406	I	2016-11-03 08:11:30.281207+00	53	3452	14	2016-11-03 09:30:00+00	2016-11-03 10:00:00+00
1407	I	2016-11-03 13:08:35.652962+00	572	3453	7	2016-11-07 12:00:00+00	2016-11-07 13:00:00+00
\.


--
-- TOC entry 2323 (class 0 OID 0)
-- Dependencies: 200
-- Name: doc_periods_audit_docperiodsauditid_seq; Type: SEQUENCE SET; Schema: audit; Owner: postgres
--

SELECT pg_catalog.setval('doc_periods_audit_docperiodsauditid_seq', 1407, true);


--
-- TOC entry 2278 (class 0 OID 21074)
-- Dependencies: 203
-- Data for Name: donut_doc_periods_audit; Type: TABLE DATA; Schema: audit; Owner: postgres
--

COPY donut_doc_periods_audit (donutdocperiodsauditid, operation, stamp, userid, donutdocperiodid, creationdate, commentfordonut, driver, driverphonenumber, licenseplate, palletsqty, supplieruserid, lastmodified) FROM stdin;
1	I	2016-07-07 09:14:28.146085+00	15	191	2016-07-07					0	15	2016-07-07 09:14:28.146085+00
2	D	2016-07-07 09:14:31.166229+00	15	191	2016-07-07					0	15	2016-07-07 09:14:28.146085+00
3	I	2016-07-07 09:15:30.619245+00	13	192	2016-07-07					0	13	2016-07-07 09:15:30.619245+00
4	D	2016-07-07 09:15:32.949903+00	13	192	2016-07-07					0	13	2016-07-07 09:15:30.619245+00
5	I	2016-07-09 11:01:00.791788+00	12	197	2016-07-09		khlhg		8ji8y	0	12	2016-07-09 11:01:00.791788+00
6	I	2016-07-13 10:33:48.059204+00	15	202	2016-07-13					5	15	2016-07-13 10:33:48.059204+00
7	I	2016-07-14 12:17:49.806692+00	12	203	2016-07-14					15	12	2016-07-14 12:17:49.806692+00
8	U	2016-07-14 12:18:11.504094+00	12	203	2016-07-14				1	15	12	2016-07-14 12:17:49.806692+00
9	I	2016-07-14 12:37:24.347308+00	12	204	2016-07-14					0	12	2016-07-14 12:37:24.347308+00
10	D	2016-07-14 12:37:27.135198+00	12	204	2016-07-14					0	12	2016-07-14 12:37:24.347308+00
11	I	2016-07-14 16:11:06.026614+00	12	230	2016-07-14					0	12	2016-07-14 16:11:06.026614+00
12	D	2016-07-14 16:11:08.755496+00	12	230	2016-07-14					0	12	2016-07-14 16:11:06.026614+00
13	I	2016-07-14 16:11:42.70342+00	12	231	2016-07-14					0	12	2016-07-14 16:11:42.70342+00
14	D	2016-07-14 16:11:44.652836+00	12	231	2016-07-14					0	12	2016-07-14 16:11:42.70342+00
15	I	2016-07-19 09:24:02.40633+00	12	548	2016-07-19		Курушев Андрей			0	12	2016-07-19 09:24:02.40633+00
16	D	2016-07-19 09:24:07.014659+00	12	548	2016-07-19		Курушев Андрей			0	12	2016-07-19 09:24:02.40633+00
17	I	2016-07-19 09:24:28.661874+00	12	549	2016-07-19		Курушев Андрей			0	12	2016-07-19 09:24:28.661874+00
18	D	2016-07-19 09:24:35.748124+00	12	549	2016-07-19		Курушев Андрей			0	12	2016-07-19 09:24:28.661874+00
19	I	2016-07-19 09:26:22.142659+00	12	551	2016-07-19		пыв	36736737	ывпы	10	12	2016-07-19 09:26:22.142659+00
20	D	2016-07-19 09:26:25.369783+00	12	551	2016-07-19		пыв	36736737	ывпы	10	12	2016-07-19 09:26:22.142659+00
21	I	2016-07-19 09:27:49.94574+00	12	552	2016-07-19		Курушев Андрей	89163140142	Т281ЕС777	10	12	2016-07-19 09:27:49.94574+00
22	D	2016-07-19 09:28:35.250494+00	12	552	2016-07-19		Курушев Андрей	89163140142	Т281ЕС777	10	12	2016-07-19 09:27:49.94574+00
23	I	2016-07-19 09:35:12.01786+00	12	553	2016-07-19					0	12	2016-07-19 09:35:12.01786+00
24	D	2016-07-19 09:35:19.483652+00	12	553	2016-07-19					0	12	2016-07-19 09:35:12.01786+00
25	I	2016-07-20 09:21:09.953448+00	12	555	2016-07-20		Курушев Андрей	89163140142	Т281ЕС777	15	12	2016-07-20 09:21:09.953448+00
26	I	2016-07-21 09:17:38.212968+00	12	556	2016-07-21		Алексеев Сергей	89150954742	У158ОР77	8	12	2016-07-21 09:17:38.212968+00
27	I	2016-07-21 09:53:52.008757+00	53	557	2016-07-21					0	53	2016-07-21 09:53:52.008757+00
28	D	2016-07-21 09:53:54.145711+00	53	557	2016-07-21					0	53	2016-07-21 09:53:52.008757+00
29	I	2016-07-27 09:00:53.971644+00	12	560	2016-07-27		Алексеев Сергей	89150954247	У158ОР77	12	12	2016-07-27 09:00:53.971644+00
30	I	2016-07-28 09:28:03.152018+00	57	561	2016-07-28		Львов Сергей Николаевич	89261581792	Е272АO77	14	57	2016-07-28 09:28:03.152018+00
31	I	2016-07-28 13:25:28.361821+00	53	562	2016-07-28		Пустовойт В.И.	89652411295	к580ро197	4	53	2016-07-28 13:25:28.361821+00
32	I	2016-08-01 10:13:17.396213+00	12	563	2016-08-01					0	12	2016-08-01 10:13:17.396213+00
33	D	2016-08-01 10:14:38.588446+00	12	563	2016-08-01					0	12	2016-08-01 10:13:17.396213+00
34	I	2016-08-02 09:25:22.860618+00	59	564	2016-08-02					0	59	2016-08-02 09:25:22.860618+00
35	D	2016-08-02 09:25:25.802383+00	59	564	2016-08-02					0	59	2016-08-02 09:25:22.860618+00
36	I	2016-08-02 09:32:09.368667+00	60	565	2016-08-02					0	60	2016-08-02 09:32:09.368667+00
37	D	2016-08-02 09:32:11.674057+00	60	565	2016-08-02					0	60	2016-08-02 09:32:09.368667+00
38	I	2016-08-02 09:40:50.392693+00	61	566	2016-08-02					0	61	2016-08-02 09:40:50.392693+00
39	D	2016-08-02 09:40:53.791391+00	61	566	2016-08-02					0	61	2016-08-02 09:40:50.392693+00
40	I	2016-08-02 10:27:01.989026+00	63	798	2016-08-02					0	63	2016-08-02 10:27:01.989026+00
41	D	2016-08-02 10:27:04.820526+00	63	798	2016-08-02					0	63	2016-08-02 10:27:01.989026+00
42	I	2016-08-02 10:43:24.434101+00	65	799	2016-08-02					0	65	2016-08-02 10:43:24.434101+00
43	D	2016-08-02 10:43:27.203733+00	65	799	2016-08-02					0	65	2016-08-02 10:43:24.434101+00
44	I	2016-08-02 11:05:54.366183+00	68	800	2016-08-02					0	68	2016-08-02 11:05:54.366183+00
45	D	2016-08-02 11:05:56.315205+00	68	800	2016-08-02					0	68	2016-08-02 11:05:54.366183+00
46	I	2016-08-03 08:01:57.413198+00	12	801	2016-08-03		Курушев Андрей	89163140142	Т281УС777	15	12	2016-08-03 08:01:57.413198+00
47	I	2016-08-03 08:03:51.275783+00	12	802	2016-08-03		Алексеев	89150954247	У158ОР77	5	12	2016-08-03 08:03:51.275783+00
48	I	2016-08-03 13:13:51.107528+00	12	803	2016-08-03		Алексеев Сергей	89150954247	У158ОР77	10	12	2016-08-03 13:13:51.107528+00
49	D	2016-08-03 13:13:55.596943+00	12	802	2016-08-03		Алексеев	89150954247	У158ОР77	5	12	2016-08-03 08:03:51.275783+00
50	I	2016-08-03 14:48:06.92731+00	53	805	2016-08-03		Купцов Н.А.	8909975058	О138ТЕ58	5	53	2016-08-03 14:48:06.92731+00
51	D	2016-08-04 07:12:33.274688+00	53	805	2016-08-03		Купцов Н.А.	8909975058	О138ТЕ58	5	53	2016-08-03 14:48:06.92731+00
52	I	2016-08-04 07:21:25.372286+00	53	806	2016-08-04		Купцов Н.А.	8909975058	О138ТЕ58	4	53	2016-08-04 07:21:25.372286+00
53	I	2016-08-05 11:00:10.573483+00	69	811	2016-08-05		Заниборщ Роман Григорьевич	9164400532	О237УО	5	69	2016-08-05 11:00:10.573483+00
54	I	2016-08-05 12:03:39.372464+00	66	812	2016-08-05					0	66	2016-08-05 12:03:39.372464+00
55	D	2016-08-05 12:04:06.207694+00	66	812	2016-08-05					0	66	2016-08-05 12:03:39.372464+00
56	I	2016-08-05 12:44:26.657195+00	76	813	2016-08-05					0	76	2016-08-05 12:44:26.657195+00
57	I	2016-08-05 16:20:18.236996+00	12	815	2016-08-05					0	12	2016-08-05 16:20:18.236996+00
58	D	2016-08-05 16:20:27.130869+00	12	815	2016-08-05					0	12	2016-08-05 16:20:18.236996+00
59	I	2016-08-08 07:36:27.230911+00	12	816	2016-08-08		Курушев Андрей	89163140142	Т281ЕС777	15	12	2016-08-08 07:36:27.230911+00
60	I	2016-08-08 07:37:49.552047+00	12	817	2016-08-08		Алексеев Сергей	89150954247	У158ОР77	12	12	2016-08-08 07:37:49.552047+00
61	I	2016-08-09 14:49:04.000426+00	53	831	2016-08-09		Пустовойтов В.И.		р580ко	0	53	2016-08-09 14:49:04.000426+00
62	D	2016-08-09 15:07:33.41809+00	53	831	2016-08-09		Пустовойтов В.И.		р580ко	0	53	2016-08-09 14:49:04.000426+00
63	I	2016-08-10 09:56:27.377546+00	12	832	2016-08-10		Курушев Андрей	89163140142	Т281ЕС777	15	12	2016-08-10 09:56:27.377546+00
64	I	2016-08-10 09:59:24.142836+00	12	833	2016-08-10		Алексеев Сергей	89150954247	У158ОР77	11	12	2016-08-10 09:59:24.142836+00
65	I	2016-08-10 13:35:32.908166+00	59	834	2016-08-10		Якуб Лилиан	89266000922	е110тт190	0	59	2016-08-10 13:35:32.908166+00
66	I	2016-08-12 08:50:32.806255+00	12	837	2016-08-12		Курушев Андрей	89163140142	Т281ЕС777	15	12	2016-08-12 08:50:32.806255+00
67	I	2016-08-12 08:52:11.401685+00	12	838	2016-08-12		Алексеев Сергей	89150954247	У158ОР77	11	12	2016-08-12 08:52:11.401685+00
68	I	2016-08-12 09:35:29.939619+00	67	839	2016-08-12	товар предоставляется в коробках	Сошин М.И.	89166809293	Е511РН190	15	67	2016-08-12 09:35:29.939619+00
69	I	2016-08-12 11:13:36.140292+00	69	840	2016-08-12		Жданович Вячеслав Викторович	89856208022	У774ТХ	5	69	2016-08-12 11:13:36.140292+00
70	I	2016-08-16 07:09:13.516311+00	59	850	2016-08-16					5	59	2016-08-16 07:09:13.516311+00
72	U	2016-08-16 07:11:28.7817+00	59	850	2016-08-16					5	59	2016-08-16 07:09:13.516311+00
71	U	2016-08-16 07:10:43.986204+00	59	850	2016-08-16	5151287\n5151263				5	59	2016-08-16 07:09:13.516311+00
74	U	2016-08-17 13:01:45.822402+00	59	850	2016-08-16		Лебедев Сергей Владимирович			5	59	2016-08-16 07:09:13.516311+00
76	U	2016-08-17 13:02:35.769949+00	59	850	2016-08-16		Лебедев Сергей Владимирович	89266000986	с874аа 77	5	59	2016-08-16 07:09:13.516311+00
79	I	2016-08-24 12:33:50.854364+00	64	856	2016-08-24		Плотников Григорий Владимирович	89193698621	У509НУ96	2	64	2016-08-24 12:33:50.854364+00
87	U	2016-08-25 14:10:04.726777+00	59	861	2016-08-25		Туханов Михаил Павлович	89266000621	т886та199	3	59	2016-08-25 12:25:03.373442+00
89	I	2016-08-29 08:14:11.169352+00	12	864	2016-08-29		Милов Николай	89096468700	У158ОР77	11	12	2016-08-29 08:14:11.169352+00
90	I	2016-08-29 08:15:19.431734+00	12	865	2016-08-29		Курушев Андрей	89163140142	Т281ЕС777	15	12	2016-08-29 08:15:19.431734+00
93	I	2016-08-30 08:42:07.78958+00	117	964	2016-08-30					0	117	2016-08-30 08:42:07.78958+00
102	D	2016-08-30 09:58:46.123597+00	121	968	2016-08-30					0	121	2016-08-30 09:58:16.000713+00
103	I	2016-08-30 10:27:01.422216+00	498	969	2016-08-30					0	498	2016-08-30 10:27:01.422216+00
104	D	2016-08-30 10:27:04.617182+00	498	969	2016-08-30					0	498	2016-08-30 10:27:01.422216+00
105	I	2016-08-30 12:03:26.532978+00	53	970	2016-08-30		Матяжов С.В.	89152546306	у742ам50	10	53	2016-08-30 12:03:26.532978+00
106	I	2016-08-30 12:51:06.373393+00	129	971	2016-08-30					0	129	2016-08-30 12:51:06.373393+00
107	D	2016-08-30 12:51:11.461902+00	129	971	2016-08-30					0	129	2016-08-30 12:51:06.373393+00
73	I	2016-08-17 12:36:58.867057+00	12	851	2016-08-17		Курушев Андрей	89163140142	Т281ЕС777	15	12	2016-08-17 12:36:58.867057+00
75	U	2016-08-17 13:02:03.951525+00	59	850	2016-08-16		Лебедев Сергей Владимирович		с874аа 77	5	59	2016-08-16 07:09:13.516311+00
77	I	2016-08-19 09:22:58.071092+00	69	852	2016-08-19		Гасанов Рауф Ханбала Оглы	89162458658	Х814МК150	4	69	2016-08-19 09:22:58.071092+00
78	I	2016-08-22 07:11:32.38165+00	53	854	2016-08-22		Виноградов А	89773174131	0581ко777	4	53	2016-08-22 07:11:32.38165+00
80	I	2016-08-24 13:20:28.666022+00	12	857	2016-08-24		Курушев Андрей	89163140142	Т281ЕС777	15	12	2016-08-24 13:20:28.666022+00
81	I	2016-08-24 13:21:28.631958+00	12	858	2016-08-24		Милов Николай	89096468700	У158ОР77	11	12	2016-08-24 13:21:28.631958+00
82	D	2016-08-24 13:22:45.677969+00	12	857	2016-08-24		Курушев Андрей	89163140142	Т281ЕС777	15	12	2016-08-24 13:20:28.666022+00
83	I	2016-08-24 13:24:18.793444+00	12	859	2016-08-24		Курушев Андрей	89163140142	Т281ЕС777	15	12	2016-08-24 13:24:18.793444+00
84	I	2016-08-25 07:22:53.614589+00	57	860	2016-08-25		Львов  С.Н.	89261581792	Е272АО77	14	57	2016-08-25 07:22:53.614589+00
85	I	2016-08-25 12:25:03.373442+00	59	861	2016-08-25					3	59	2016-08-25 12:25:03.373442+00
86	I	2016-08-25 12:36:36.533033+00	69	862	2016-08-25		Акулин Юрий Алексеевич	89299870993	в453ва	3	69	2016-08-25 12:36:36.533033+00
88	I	2016-08-26 12:10:08.39229+00	69	863	2016-08-26		Гасанов Рауф Ханбала Оглы	89162457658	х814мк	2	69	2016-08-26 12:10:08.39229+00
91	I	2016-08-30 07:32:38.746945+00	494	868	2016-08-30					0	494	2016-08-30 07:32:38.746945+00
92	D	2016-08-30 07:32:43.492896+00	494	868	2016-08-30					0	494	2016-08-30 07:32:38.746945+00
94	D	2016-08-30 08:42:14.653509+00	117	964	2016-08-30					0	117	2016-08-30 08:42:07.78958+00
95	I	2016-08-30 09:00:14.484567+00	118	965	2016-08-30					0	118	2016-08-30 09:00:14.484567+00
96	D	2016-08-30 09:00:49.520375+00	118	965	2016-08-30					0	118	2016-08-30 09:00:14.484567+00
97	I	2016-08-30 09:44:35.761593+00	119	966	2016-08-30					0	119	2016-08-30 09:44:35.761593+00
98	D	2016-08-30 09:44:45.998442+00	119	966	2016-08-30					0	119	2016-08-30 09:44:35.761593+00
99	I	2016-08-30 09:48:26.760097+00	120	967	2016-08-30					0	120	2016-08-30 09:48:26.760097+00
100	D	2016-08-30 09:48:30.121966+00	120	967	2016-08-30					0	120	2016-08-30 09:48:26.760097+00
101	I	2016-08-30 09:58:16.000713+00	121	968	2016-08-30					0	121	2016-08-30 09:58:16.000713+00
108	I	2016-08-30 13:05:27.909528+00	123	972	2016-08-30					0	123	2016-08-30 13:05:27.909528+00
109	D	2016-08-30 13:05:35.851402+00	123	972	2016-08-30					0	123	2016-08-30 13:05:27.909528+00
110	I	2016-08-30 13:12:05.980721+00	123	973	2016-08-30					0	123	2016-08-30 13:12:05.980721+00
111	D	2016-08-30 13:12:09.331952+00	123	973	2016-08-30					0	123	2016-08-30 13:12:05.980721+00
112	I	2016-08-30 13:20:46.047054+00	119	974	2016-08-30					0	119	2016-08-30 13:20:46.047054+00
113	D	2016-08-30 13:20:49.193153+00	119	974	2016-08-30					0	119	2016-08-30 13:20:46.047054+00
114	I	2016-08-30 13:36:06.31646+00	124	975	2016-08-30					0	124	2016-08-30 13:36:06.31646+00
115	D	2016-08-30 13:36:10.225657+00	124	975	2016-08-30					0	124	2016-08-30 13:36:06.31646+00
116	I	2016-08-30 13:46:19.76463+00	125	976	2016-08-30					0	125	2016-08-30 13:46:19.76463+00
117	D	2016-08-30 13:46:22.021753+00	125	976	2016-08-30					0	125	2016-08-30 13:46:19.76463+00
118	I	2016-08-30 14:34:02.9399+00	127	977	2016-08-30					0	127	2016-08-30 14:34:02.9399+00
119	D	2016-08-30 14:34:09.978785+00	127	977	2016-08-30					0	127	2016-08-30 14:34:02.9399+00
120	I	2016-08-31 10:17:48.776641+00	509	978	2016-08-31					0	509	2016-08-31 10:17:48.776641+00
121	D	2016-08-31 10:17:52.223711+00	509	978	2016-08-31					0	509	2016-08-31 10:17:48.776641+00
122	I	2016-08-31 10:18:20.448223+00	510	979	2016-08-31					0	510	2016-08-31 10:18:20.448223+00
123	D	2016-08-31 10:18:23.880467+00	510	979	2016-08-31					0	510	2016-08-31 10:18:20.448223+00
124	I	2016-08-31 10:18:47.260408+00	511	980	2016-08-31					0	511	2016-08-31 10:18:47.260408+00
125	D	2016-08-31 10:18:50.33834+00	511	980	2016-08-31					0	511	2016-08-31 10:18:47.260408+00
126	I	2016-08-31 10:19:17.438008+00	512	981	2016-08-31					0	512	2016-08-31 10:19:17.438008+00
127	D	2016-08-31 10:19:25.520668+00	512	981	2016-08-31					0	512	2016-08-31 10:19:17.438008+00
128	I	2016-08-31 10:19:46.486974+00	513	982	2016-08-31					0	513	2016-08-31 10:19:46.486974+00
129	D	2016-08-31 10:19:52.076036+00	513	982	2016-08-31					0	513	2016-08-31 10:19:46.486974+00
130	I	2016-08-31 10:23:28.649595+00	514	983	2016-08-31					0	514	2016-08-31 10:23:28.649595+00
131	D	2016-08-31 10:23:30.96089+00	514	983	2016-08-31					0	514	2016-08-31 10:23:28.649595+00
132	I	2016-08-31 11:07:35.162425+00	472	994	2016-08-31	а/м Газель 	Морозов Андрей Валентинович	89538203043	О975КО 96	0	472	2016-08-31 11:07:35.162425+00
133	I	2016-08-31 12:36:17.506165+00	114	995	2016-08-31					0	114	2016-08-31 12:36:17.506165+00
134	D	2016-08-31 12:36:20.229778+00	114	995	2016-08-31					0	114	2016-08-31 12:36:17.506165+00
135	I	2016-08-31 12:40:24.207338+00	124	996	2016-08-31					0	124	2016-08-31 12:40:24.207338+00
136	D	2016-08-31 12:40:29.046533+00	124	996	2016-08-31					0	124	2016-08-31 12:40:24.207338+00
137	I	2016-08-31 13:00:57.021291+00	59	997	2016-08-31					0	59	2016-08-31 13:00:57.021291+00
138	I	2016-08-31 13:08:06.005252+00	492	998	2016-08-31					0	492	2016-08-31 13:08:06.005252+00
139	D	2016-08-31 13:08:15.852904+00	492	998	2016-08-31					0	492	2016-08-31 13:08:06.005252+00
140	I	2016-08-31 13:25:03.283291+00	122	999	2016-08-31					0	122	2016-08-31 13:25:03.283291+00
141	D	2016-08-31 13:25:10.857652+00	122	999	2016-08-31					0	122	2016-08-31 13:25:03.283291+00
142	U	2016-08-31 13:59:04.242627+00	59	997	2016-08-31		Ботнарь Станислав Алексеевич		а626вм37	6	59	2016-08-31 13:00:57.021291+00
143	I	2016-08-31 13:59:47.87064+00	518	1000	2016-08-31					0	518	2016-08-31 13:59:47.87064+00
144	D	2016-08-31 13:59:54.129547+00	518	1000	2016-08-31					0	518	2016-08-31 13:59:47.87064+00
145	I	2016-08-31 14:35:37.925306+00	523	1001	2016-08-31					0	523	2016-08-31 14:35:37.925306+00
146	D	2016-08-31 14:35:40.704234+00	523	1001	2016-08-31					0	523	2016-08-31 14:35:37.925306+00
147	I	2016-08-31 14:44:09.009524+00	524	1002	2016-08-31					0	524	2016-08-31 14:44:09.009524+00
148	D	2016-08-31 14:44:12.088409+00	524	1002	2016-08-31					0	524	2016-08-31 14:44:09.009524+00
149	I	2016-09-01 08:08:33.794959+00	12	1003	2016-09-01		Курушев Андрей	89163140142	Т281ЕС777	9	12	2016-09-01 08:08:33.794959+00
150	I	2016-09-01 08:09:15.658327+00	12	1004	2016-09-01		Алексеев Сергей	89150954247	У158ОР77	8	12	2016-09-01 08:09:15.658327+00
151	I	2016-09-01 11:06:18.999498+00	526	1012	2016-09-01					0	526	2016-09-01 11:06:18.999498+00
152	I	2016-09-01 11:14:39.909301+00	523	1014	2016-09-01		Жилин А.В		н661кх197	3	523	2016-09-01 11:14:39.909301+00
153	D	2016-09-01 11:15:25.505016+00	526	1012	2016-09-01					0	526	2016-09-01 11:06:18.999498+00
154	I	2016-09-01 11:15:29.427052+00	526	1015	2016-09-01					0	526	2016-09-01 11:15:29.427052+00
155	I	2016-09-01 11:23:10.45625+00	526	1018	2016-09-01					0	526	2016-09-01 11:23:10.45625+00
156	D	2016-09-01 11:26:21.608383+00	526	1015	2016-09-01					0	526	2016-09-01 11:15:29.427052+00
157	D	2016-09-01 11:26:23.762696+00	526	1018	2016-09-01					0	526	2016-09-01 11:23:10.45625+00
158	I	2016-09-01 11:31:41.438913+00	526	1019	2016-09-01					0	526	2016-09-01 11:31:41.438913+00
159	U	2016-09-01 11:37:11.7155+00	526	1019	2016-09-01	Машина вышла во второй половине дня	Лобанов Александр		Т279МК	33	526	2016-09-01 11:31:41.438913+00
160	D	2016-09-01 11:39:19.497433+00	526	1019	2016-09-01	Машина вышла во второй половине дня	Лобанов Александр		Т279МК	33	526	2016-09-01 11:31:41.438913+00
161	I	2016-09-01 11:39:28.368127+00	526	1020	2016-09-01					0	526	2016-09-01 11:39:28.368127+00
162	I	2016-09-01 11:39:33.373229+00	526	1021	2016-09-01					0	526	2016-09-01 11:39:33.373229+00
163	I	2016-09-01 11:39:49.062229+00	526	1022	2016-09-01					0	526	2016-09-01 11:39:49.062229+00
164	D	2016-09-01 11:40:04.847078+00	526	1022	2016-09-01					0	526	2016-09-01 11:39:49.062229+00
165	D	2016-09-01 11:40:07.35004+00	526	1021	2016-09-01					0	526	2016-09-01 11:39:33.373229+00
166	D	2016-09-01 11:40:09.909142+00	526	1020	2016-09-01					0	526	2016-09-01 11:39:28.368127+00
167	I	2016-09-01 11:43:13.959203+00	526	1023	2016-09-01		Овчинников Б.В.	243	цпукп	15	526	2016-09-01 11:43:13.959203+00
168	I	2016-09-01 11:48:45.23345+00	526	1024	2016-09-01					0	526	2016-09-01 11:48:45.23345+00
169	D	2016-09-01 11:56:24.405609+00	17	1014	2016-09-01		Жилин А.В		н661кх197	3	523	2016-09-01 11:14:39.909301+00
170	D	2016-09-01 11:59:22.669383+00	17	838	2016-08-12		Алексеев Сергей	89150954247	У158ОР77	11	12	2016-08-12 08:52:11.401685+00
171	I	2016-09-02 08:48:12.452724+00	528	1028	2016-09-02		Соколов А,К.	89176395873	У147АВ/73	33	528	2016-09-02 08:48:12.452724+00
172	I	2016-09-02 12:01:22.456837+00	89	1031	2016-09-02		Руденко Владимир Александрович			0	89	2016-09-02 12:01:22.456837+00
173	U	2016-09-02 12:03:01.948488+00	89	1031	2016-09-02	груз не на паллетах	Руденко Владимир Александрович	89212156469	М767КА60	0	89	2016-09-02 12:01:22.456837+00
174	I	2016-09-02 12:47:22.121614+00	465	1032	2016-09-02					0	465	2016-09-02 12:47:22.121614+00
175	U	2016-09-02 12:51:35.762786+00	465	1032	2016-09-02		Чувакин Евгений Боривич	89851298188	м359рк197	5	465	2016-09-02 12:47:22.121614+00
176	I	2016-09-05 06:09:13.101676+00	69	1033	2016-09-05		Гасанов Рауф Ханбала Оглы	89162457658	Х814МК	3	69	2016-09-05 06:09:13.101676+00
177	I	2016-09-05 06:41:56.9658+00	68	1036	2016-09-05		кислицын николай викторович	89772703809	к664хв197	2	68	2016-09-05 06:41:56.9658+00
178	I	2016-09-05 09:21:43.784537+00	11	1037	2016-09-05					0	11	2016-09-05 09:21:43.784537+00
179	D	2016-09-05 09:21:46.995706+00	11	1037	2016-09-05					0	11	2016-09-05 09:21:43.784537+00
180	I	2016-09-05 09:34:04.433225+00	11	1038	2016-09-05					0	11	2016-09-05 09:34:04.433225+00
181	D	2016-09-05 09:34:11.266033+00	11	1038	2016-09-05					0	11	2016-09-05 09:34:04.433225+00
182	I	2016-09-05 09:39:22.954828+00	11	1039	2016-09-05					0	11	2016-09-05 09:39:22.954828+00
183	D	2016-09-05 09:39:25.213797+00	11	1039	2016-09-05					0	11	2016-09-05 09:39:22.954828+00
184	I	2016-09-05 09:41:46.489932+00	11	1040	2016-09-05					0	11	2016-09-05 09:41:46.489932+00
185	D	2016-09-05 09:41:53.908085+00	11	1040	2016-09-05					0	11	2016-09-05 09:41:46.489932+00
186	I	2016-09-06 08:56:50.067962+00	128	1042	2016-09-06		Буланов И.А.	9266818297	х951вн50	5	128	2016-09-06 08:56:50.067962+00
187	I	2016-09-07 10:04:28.207946+00	472	1048	2016-09-07					0	472	2016-09-07 10:04:28.207946+00
188	D	2016-09-07 10:04:46.29105+00	472	1048	2016-09-07					0	472	2016-09-07 10:04:28.207946+00
189	I	2016-09-07 10:06:37.335201+00	472	1049	2016-09-07		Кузнецов Валерий Иваныч	89655104117	н846кх	0	472	2016-09-07 10:06:37.335201+00
190	I	2016-09-07 12:28:40.414058+00	59	1050	2016-09-07					0	59	2016-09-07 12:28:40.414058+00
191	U	2016-09-07 14:32:45.422728+00	59	1050	2016-09-07	кол-во паллет ориентировочное	Якуб Лилиан		е110тт190	7	59	2016-09-07 12:28:40.414058+00
192	I	2016-09-09 08:58:33.175062+00	69	1051	2016-09-09		Гасанов Рауф Ханбала Оглы	89162457658	Х814МК	6	69	2016-09-09 08:58:33.175062+00
193	I	2016-09-09 12:24:07.571651+00	465	1052	2016-09-09		Чиканчи Иван Степанович		А378ТО197	5	465	2016-09-09 12:24:07.571651+00
194	I	2016-09-09 12:59:59.176807+00	68	1053	2016-09-09		кислицын николай викторович		к664хв197	0	68	2016-09-09 12:59:59.176807+00
195	I	2016-09-09 13:27:33.639019+00	528	1055	2016-09-09					0	528	2016-09-09 13:27:33.639019+00
196	U	2016-09-09 13:31:24.891289+00	528	1055	2016-09-09		Ахунов Н.А.		В566УУ/73	33	528	2016-09-09 13:27:33.639019+00
197	U	2016-09-09 13:31:36.665099+00	528	1055	2016-09-09		Ахунов Н.А.	89050356986	В566УУ/73	33	528	2016-09-09 13:27:33.639019+00
198	I	2016-09-09 13:36:52.185657+00	527	1056	2016-09-09		Круглов Н.М.		У361ОТ 96	27	527	2016-09-09 13:36:52.185657+00
199	I	2016-09-12 06:04:35.158567+00	94	1057	2016-09-12		Дмитриус Дмитрий 	89055364221	В592НР	2	94	2016-09-12 06:04:35.158567+00
200	U	2016-09-12 06:20:06.278465+00	68	1053	2016-09-09		кислицын николай викторович	89772703809	М496ВА197	6	68	2016-09-09 12:59:59.176807+00
201	D	2016-09-12 07:25:49.802263+00	94	1057	2016-09-12		Дмитриус Дмитрий 	89055364221	В592НР	2	94	2016-09-12 06:04:35.158567+00
202	I	2016-09-12 10:44:28.95551+00	70	1090	2016-09-12					0	70	2016-09-12 10:44:28.95551+00
203	I	2016-09-12 13:34:25.623785+00	528	1487	2016-09-12		Новиков А.А			33	528	2016-09-12 13:34:25.623785+00
204	U	2016-09-12 13:34:44.043146+00	528	1487	2016-09-12		Новиков А.А		О296КЕ 58	33	528	2016-09-12 13:34:25.623785+00
205	U	2016-09-12 13:35:04.936803+00	528	1487	2016-09-12		Новиков А.А	89063923156	О296КЕ 58	33	528	2016-09-12 13:34:25.623785+00
206	I	2016-09-12 14:15:05.914591+00	527	1490	2016-09-12		Усманов р.р.			0	527	2016-09-12 14:15:05.914591+00
207	U	2016-09-12 14:16:13.325086+00	527	1490	2016-09-12		Усманов р.р.			33	527	2016-09-12 14:15:05.914591+00
208	I	2016-09-13 03:42:13.453697+00	472	1491	2016-09-13		Кузнецов В.И.	89655104117		0	472	2016-09-13 03:42:13.453697+00
209	I	2016-09-13 07:20:26.757695+00	503	1492	2016-09-13					0	503	2016-09-13 07:20:26.757695+00
210	D	2016-09-13 07:20:30.699948+00	503	1492	2016-09-13					0	503	2016-09-13 07:20:26.757695+00
211	I	2016-09-13 10:13:50.662822+00	496	1493	2016-09-13					0	496	2016-09-13 10:13:50.662822+00
212	D	2016-09-13 10:13:55.511786+00	496	1493	2016-09-13					0	496	2016-09-13 10:13:50.662822+00
213	I	2016-09-13 10:16:40.117658+00	496	1494	2016-09-13					0	496	2016-09-13 10:16:40.117658+00
214	D	2016-09-13 10:16:44.034225+00	496	1494	2016-09-13					0	496	2016-09-13 10:16:40.117658+00
215	I	2016-09-13 11:09:53.937759+00	62	1495	2016-09-13		Маторин	89043827525	В317ВР	1	62	2016-09-13 11:09:53.937759+00
216	I	2016-09-13 11:32:37.080781+00	69	1496	2016-09-13		Акулин Юрий Алексеевич	89299870993	В453ВА	2	69	2016-09-13 11:32:37.080781+00
217	I	2016-09-13 11:33:49.188434+00	535	1497	2016-09-13					0	535	2016-09-13 11:33:49.188434+00
218	D	2016-09-13 11:33:52.654653+00	535	1497	2016-09-13					0	535	2016-09-13 11:33:49.188434+00
219	I	2016-09-13 11:38:04.688666+00	535	1498	2016-09-13					0	535	2016-09-13 11:38:04.688666+00
220	D	2016-09-13 11:38:07.885528+00	535	1498	2016-09-13					0	535	2016-09-13 11:38:04.688666+00
221	I	2016-09-13 12:57:02.035609+00	538	1501	2016-09-13		Кудряшов Александр Михайлович		А470КМ/69	0	538	2016-09-13 12:57:02.035609+00
222	D	2016-09-13 12:57:31.065436+00	538	1501	2016-09-13		Кудряшов Александр Михайлович		А470КМ/69	0	538	2016-09-13 12:57:02.035609+00
223	I	2016-09-13 12:58:37.440527+00	538	1502	2016-09-13		Кудряшов Александр Михайлович	89607031017	А470КМ/69	0	538	2016-09-13 12:58:37.440527+00
224	I	2016-09-13 13:04:40.986585+00	538	1503	2016-09-13		Кудряшов Александр Михайлович		А470КМ/69	0	538	2016-09-13 13:04:40.986585+00
225	I	2016-09-13 13:05:19.719043+00	538	1504	2016-09-13		Кудряшов Александр Михайлович		А470КМ/69	0	538	2016-09-13 13:05:19.719043+00
226	I	2016-09-13 13:09:56.523089+00	59	1505	2016-09-13					0	59	2016-09-13 13:09:56.523089+00
227	U	2016-09-13 14:04:10.839568+00	59	1505	2016-09-13		Ботнарь Алексей Алексеевич		с782сн197	6	59	2016-09-13 13:09:56.523089+00
228	I	2016-09-13 14:06:09.64868+00	542	1506	2016-09-13					0	542	2016-09-13 14:06:09.64868+00
229	D	2016-09-13 14:06:15.739551+00	542	1506	2016-09-13					0	542	2016-09-13 14:06:09.64868+00
230	I	2016-09-14 08:08:46.208197+00	15	1507	2016-09-14					0	15	2016-09-14 08:08:46.208197+00
231	U	2016-09-14 09:27:46.812156+00	15	1507	2016-09-14					4	15	2016-09-14 08:08:46.208197+00
232	U	2016-09-14 09:32:45.629482+00	70	1090	2016-09-12		Журавлев Андрей	89163638151	О904ОЕ777	1	70	2016-09-12 10:44:28.95551+00
233	U	2016-09-14 12:10:13.643178+00	15	1507	2016-09-14		Коршунов Анатолий Николаевич	89296235264	А544АК50	4	15	2016-09-14 08:08:46.208197+00
234	I	2016-09-14 12:45:31.972502+00	126	1508	2016-09-14		Ступич Д.В.	89852124682	969	3	126	2016-09-14 12:45:31.972502+00
235	I	2016-09-14 14:27:21.445981+00	92	2208	2016-09-14				Газель	6	92	2016-09-14 14:27:21.445981+00
236	I	2016-09-15 05:29:31.958445+00	94	2246	2016-09-15		Дмитриус Дмитрий 	89055364221	в592нр750	2	94	2016-09-15 05:29:31.958445+00
237	I	2016-09-15 07:17:48.828681+00	53	2247	2016-09-15		Пустовойтов В.И.	89652411295	к580ро197	4	53	2016-09-15 07:17:48.828681+00
238	I	2016-09-15 07:21:03.116368+00	53	2249	2016-09-15		Виноградов А	89773174131	о581ко777	4	53	2016-09-15 07:21:03.116368+00
239	I	2016-09-15 11:52:15.870827+00	528	2250	2016-09-15		Овчинников Б.В.	89163579245	А427ЕТ50	15	528	2016-09-15 11:52:15.870827+00
240	I	2016-09-15 11:53:55.377724+00	528	2251	2016-09-15		Ахунов Н.А,	89050356986	В566УУ73	33	528	2016-09-15 11:53:55.377724+00
241	I	2016-09-15 12:53:32.525217+00	534	2254	2016-09-15		Чирковский Игорь	89161586483	О183УН190	8	534	2016-09-15 12:53:32.525217+00
242	D	2016-09-15 14:00:01.421574+00	126	1508	2016-09-14		Ступич Д.В.	89852124682	969	3	126	2016-09-14 12:45:31.972502+00
243	I	2016-09-15 14:04:18.407818+00	126	2256	2016-09-15		Возный С.М.	89688031014	341оу190	2	126	2016-09-15 14:04:18.407818+00
244	I	2016-09-16 06:09:18.370296+00	57	2257	2016-09-16					0	57	2016-09-16 06:09:18.370296+00
245	I	2016-09-16 06:52:14.013704+00	69	2258	2016-09-16		Акулин Юрий Алексеевич 	89299870993	В453ВА	3	69	2016-09-16 06:52:14.013704+00
246	D	2016-09-16 07:22:56.981917+00	69	2258	2016-09-16		Акулин Юрий Алексеевич 	89299870993	В453ВА	3	69	2016-09-16 06:52:14.013704+00
247	I	2016-09-16 07:26:32.414548+00	69	2259	2016-09-16		Акулин Юрий Алексеевич	89299870993	В453ВА	3	69	2016-09-16 07:26:32.414548+00
248	I	2016-09-16 08:14:55.877575+00	15	2260	2016-09-16					0	15	2016-09-16 08:14:55.877575+00
249	U	2016-09-16 11:48:04.249162+00	15	2260	2016-09-16		Чебесков 	89251528574		2	15	2016-09-16 08:14:55.877575+00
250	I	2016-09-16 12:12:07.004932+00	70	2261	2016-09-16					0	70	2016-09-16 12:12:07.004932+00
251	I	2016-09-16 12:35:51.57417+00	465	2262	2016-09-16		Чувакин Евгений Борисович			5	465	2016-09-16 12:35:51.57417+00
252	U	2016-09-16 12:36:51.3448+00	465	2262	2016-09-16		Чувакин Евгений Борисович	89851298188	К359РК197	5	465	2016-09-16 12:35:51.57417+00
253	I	2016-09-16 13:03:20.166236+00	68	2263	2016-09-16					0	68	2016-09-16 13:03:20.166236+00
254	U	2016-09-19 06:46:01.931944+00	68	2263	2016-09-16		Анеликов С А	89154323092	м495ва197	3	68	2016-09-16 13:03:20.166236+00
255	U	2016-09-19 07:06:55.152742+00	70	2261	2016-09-16		Журавлев Андрей	89163638151	О904ОЕ777	0	70	2016-09-16 12:12:07.004932+00
256	I	2016-09-19 10:33:59.855074+00	89	2264	2016-09-19		Кузнецов Андрей Викторович	89113682060	А059КВ	0	89	2016-09-19 10:33:59.855074+00
257	I	2016-09-20 08:07:07.643231+00	57	2268	2016-09-20					14	57	2016-09-20 08:07:07.643231+00
258	I	2016-09-20 10:45:31.584481+00	68	2271	2016-09-20					0	68	2016-09-20 10:45:31.584481+00
259	I	2016-09-20 12:19:58.418607+00	496	2272	2016-09-20		Ляхов С.П. 	89090076425	С 049 ВН 	2	496	2016-09-20 12:19:58.418607+00
260	I	2016-09-20 13:14:32.295606+00	59	2278	2016-09-20					0	59	2016-09-20 13:14:32.295606+00
261	I	2016-09-20 13:37:40.202317+00	549	2280	2016-09-20					0	549	2016-09-20 13:37:40.202317+00
262	D	2016-09-20 13:37:44.157726+00	549	2280	2016-09-20					0	549	2016-09-20 13:37:40.202317+00
263	U	2016-09-21 05:34:08.410384+00	68	2271	2016-09-20		Хапилов О Б	89154323236	в374ку197	4	68	2016-09-20 10:45:31.584481+00
264	I	2016-09-21 06:53:18.112685+00	94	2282	2016-09-21		Белов Руслан Вячеславович	89268289555	в593нр750	2	94	2016-09-21 06:53:18.112685+00
265	I	2016-09-21 09:35:10.365741+00	53	2283	2016-09-21		Пустовойт В.И.	89652411295	к580ро197	3	53	2016-09-21 09:35:10.365741+00
266	I	2016-09-21 10:33:16.693284+00	131	2284	2016-09-21					1	131	2016-09-21 10:33:16.693284+00
267	D	2016-09-21 11:27:13.946023+00	94	2282	2016-09-21		Белов Руслан Вячеславович	89268289555	в593нр750	2	94	2016-09-21 06:53:18.112685+00
268	I	2016-09-21 11:28:28.845961+00	94	2285	2016-09-21		Белов Руслан Вячеславович	89268289555	в593нр750	2	94	2016-09-21 11:28:28.845961+00
269	I	2016-09-22 06:27:33.932928+00	92	2286	2016-09-22					3	92	2016-09-22 06:27:33.932928+00
270	I	2016-09-22 08:26:16.625102+00	528	2287	2016-09-22		Овчинников Б.В.	89163579245	А427ЕТ50 	15	528	2016-09-22 08:26:16.625102+00
271	I	2016-09-22 08:27:00.049678+00	528	2288	2016-09-22					33	528	2016-09-22 08:27:00.049678+00
272	I	2016-09-23 06:17:20.909715+00	538	2301	2016-09-23		Кудряшов Н.М.		О708ЕС/69	0	538	2016-09-23 06:17:20.909715+00
273	I	2016-09-23 07:13:33.499048+00	57	2302	2016-09-23					0	57	2016-09-23 07:13:33.499048+00
274	U	2016-09-23 07:37:59.433267+00	538	2301	2016-09-23		Кудряшов А.М.		А470КМ/69	0	538	2016-09-23 06:17:20.909715+00
275	I	2016-09-23 08:56:52.694308+00	69	2303	2016-09-23		Бобков Дмитрий сергеевич	89167048378	Р281РУ	5	69	2016-09-23 08:56:52.694308+00
276	I	2016-09-23 10:04:23.505423+00	538	2304	2016-09-23		Кудряшов А.М.		А470КМ/69	0	538	2016-09-23 10:04:23.505423+00
277	D	2016-09-23 10:04:54.511898+00	538	2301	2016-09-23		Кудряшов А.М.		А470КМ/69	0	538	2016-09-23 06:17:20.909715+00
278	I	2016-09-23 10:18:12.499893+00	68	2305	2016-09-23					0	68	2016-09-23 10:18:12.499893+00
279	I	2016-09-23 12:28:12.514192+00	53	2306	2016-09-23		Павлов М.А.	89264957040	н077ср90	4	53	2016-09-23 12:28:12.514192+00
281	I	2016-09-23 13:40:54.867869+00	53	2308	2016-09-23		Павлов М.А.	89264957040	н077ср90	4	53	2016-09-23 13:40:54.867869+00
282	I	2016-09-26 04:13:09.661224+00	465	2309	2016-09-26		Матвеев Юрий Николаевич		к581мн197	8	465	2016-09-26 04:13:09.661224+00
283	I	2016-09-26 04:13:47.568497+00	465	2310	2016-09-26		Матвеев Юрий Николаевич		к581мн197	8	465	2016-09-26 04:13:47.568497+00
284	I	2016-09-26 04:29:01.193509+00	465	2311	2016-09-26		Матвеев Ю.Н.		К581МН	8	465	2016-09-26 04:29:01.193509+00
285	D	2016-09-26 04:29:07.967462+00	465	2309	2016-09-26		Матвеев Юрий Николаевич		к581мн197	8	465	2016-09-26 04:13:09.661224+00
286	D	2016-09-26 04:29:10.324769+00	465	2310	2016-09-26		Матвеев Юрий Николаевич		к581мн197	8	465	2016-09-26 04:13:47.568497+00
287	U	2016-09-26 06:29:38.085527+00	68	2305	2016-09-23		хапилов о б 	89154323236	в374ку199	3	68	2016-09-23 10:18:12.499893+00
288	I	2016-09-26 06:55:14.473361+00	12	2312	2016-09-26		Морозов Максим	89057709373	О648РА777	8	12	2016-09-26 06:55:14.473361+00
289	I	2016-09-26 06:56:17.037864+00	12	2313	2016-09-26		Курушев Андрей	89163140142	Т281ЕС777	15	12	2016-09-26 06:56:17.037864+00
290	I	2016-09-26 06:57:08.603208+00	12	2314	2016-09-26		Милов Николай	89096468700	У158ОР77	12	12	2016-09-26 06:57:08.603208+00
291	D	2016-09-26 06:58:48.815546+00	69	2303	2016-09-23		Бобков Дмитрий сергеевич	89167048378	Р281РУ	5	69	2016-09-23 08:56:52.694308+00
292	I	2016-09-26 07:03:15.729337+00	69	2315	2016-09-26		Бобков Дмитрий Сергеевич	89167048378	Р281РУ	5	69	2016-09-26 07:03:15.729337+00
293	U	2016-09-26 07:13:44.951603+00	131	2284	2016-09-21		Котельников Алексей Павлович		Н095ХК190	1	131	2016-09-21 10:33:16.693284+00
294	I	2016-09-26 08:42:53.274911+00	89	2316	2016-09-26		Рубцов валерий Павлович	89209462250	В 627 НВ	0	89	2016-09-26 08:42:53.274911+00
295	I	2016-09-26 11:14:55.158876+00	96	2317	2016-09-26					0	96	2016-09-26 11:14:55.158876+00
296	U	2016-09-26 11:16:04.020095+00	96	2317	2016-09-26					16	96	2016-09-26 11:14:55.158876+00
297	U	2016-09-26 11:25:35.681223+00	96	2317	2016-09-26					18	96	2016-09-26 11:14:55.158876+00
298	I	2016-09-26 12:18:14.027133+00	67	2318	2016-09-26		Самойлов Сергей евгеньевич	89169866633	H202EX190	27	67	2016-09-26 12:18:14.027133+00
299	U	2016-09-26 12:18:50.730178+00	67	2318	2016-09-26	товар предоставляется в коробках	Самойлов Сергей евгеньевич	89169866633	H202EX190	27	67	2016-09-26 12:18:14.027133+00
300	U	2016-09-26 12:21:19.981019+00	67	2318	2016-09-26	товар предоставляется в коробках 	Самойлов Сергей евгеньевич	89169866633	H202EX190	0	67	2016-09-26 12:18:14.027133+00
301	I	2016-09-27 07:29:25.139097+00	126	2319	2016-09-27		Ступич	89852124682	а	2	126	2016-09-27 07:29:25.139097+00
302	I	2016-09-27 12:38:14.327517+00	65	2320	2016-09-27					0	65	2016-09-27 12:38:14.327517+00
303	I	2016-09-27 12:59:43.153819+00	552	2322	2016-09-27					0	552	2016-09-27 12:59:43.153819+00
304	D	2016-09-27 12:59:45.893125+00	552	2322	2016-09-27					0	552	2016-09-27 12:59:43.153819+00
305	I	2016-09-27 13:01:54.678721+00	560	2323	2016-09-27					0	560	2016-09-27 13:01:54.678721+00
306	D	2016-09-27 13:02:03.088097+00	560	2323	2016-09-27					0	560	2016-09-27 13:01:54.678721+00
307	I	2016-09-27 13:04:34.102909+00	560	2324	2016-09-27					0	560	2016-09-27 13:04:34.102909+00
308	D	2016-09-27 13:04:36.181528+00	560	2324	2016-09-27					0	560	2016-09-27 13:04:34.102909+00
309	I	2016-09-27 13:04:43.707091+00	552	2325	2016-09-27					0	552	2016-09-27 13:04:43.707091+00
310	U	2016-09-27 13:37:32.211334+00	552	2325	2016-09-27		Острягин Владимир Петрович	89066504672	о705ех777	4	552	2016-09-27 13:04:43.707091+00
311	U	2016-09-28 08:32:33.271092+00	126	2319	2016-09-27		Гришин	89197238912 	а856ав177	2	126	2016-09-27 07:29:25.139097+00
312	D	2016-09-28 09:03:30.059989+00	552	2325	2016-09-27		Острягин Владимир Петрович	89066504672	о705ех777	4	552	2016-09-27 13:04:43.707091+00
313	I	2016-09-28 09:07:18.79933+00	53	2327	2016-09-28		Павлов М.А.	89264957040	н077ср90	4	53	2016-09-28 09:07:18.79933+00
314	I	2016-09-28 09:08:57.921239+00	53	2328	2016-09-28		Павлов М.А.	89264957040	н077ср90	4	53	2016-09-28 09:08:57.921239+00
315	I	2016-09-28 09:13:11.409079+00	552	2329	2016-09-28		Острягин Владимир Петрович	89066504672	о705ех777	4	552	2016-09-28 09:13:11.409079+00
316	I	2016-09-28 11:17:47.80888+00	57	2330	2016-09-28		Львов С.Н.			0	57	2016-09-28 11:17:47.80888+00
317	I	2016-09-28 12:29:47.480599+00	549	2331	2016-09-28		Нефедов Н.Я		С746ОА197	4	549	2016-09-28 12:29:47.480599+00
318	I	2016-09-28 12:42:27.710008+00	59	2332	2016-09-28					0	59	2016-09-28 12:42:27.710008+00
319	I	2016-09-29 04:53:04.170134+00	503	2333	2016-09-29		Москалев К.Е.	89612208500	А050АР154	0	503	2016-09-29 04:53:04.170134+00
320	I	2016-09-29 05:18:40.935085+00	538	2334	2016-09-29		Лазарев В.А.		436СА/69	0	538	2016-09-29 05:18:40.935085+00
321	I	2016-09-29 05:19:48.457099+00	538	2335	2016-09-29		Лазарев В.А.		436СА/69	0	538	2016-09-29 05:19:48.457099+00
322	D	2016-09-29 06:28:52.988557+00	538	2335	2016-09-29		Лазарев В.А.		436СА/69	0	538	2016-09-29 05:19:48.457099+00
323	I	2016-09-29 07:55:16.475475+00	128	2336	2016-09-29		Конопля		в037ск150	6	128	2016-09-29 07:55:16.475475+00
324	I	2016-09-29 08:48:11.136142+00	122	2341	2016-09-29					0	122	2016-09-29 08:48:11.136142+00
325	I	2016-09-29 11:40:41.86842+00	538	2342	2016-09-29		Бикбулатов		а470км/69	0	538	2016-09-29 11:40:41.86842+00
326	D	2016-09-29 11:49:07.777293+00	538	2342	2016-09-29		Бикбулатов		а470км/69	0	538	2016-09-29 11:40:41.86842+00
327	I	2016-09-29 12:06:34.611691+00	69	2343	2016-09-29		Бобков Дмитрий Сергеевич	89167048378	О725ВР	5	69	2016-09-29 12:06:34.611691+00
328	I	2016-09-30 07:41:13.367519+00	53	2344	2016-09-30		Пустовойтов В.И.	89652411295	к580ро197	3	53	2016-09-30 07:41:13.367519+00
329	I	2016-09-30 08:34:09.235536+00	68	2345	2016-09-30					0	68	2016-09-30 08:34:09.235536+00
330	I	2016-09-30 09:47:56.411152+00	496	2347	2016-09-30		Ляхов С.П. 	89090046425	С 049 ВН	0	496	2016-09-30 09:47:56.411152+00
331	I	2016-09-30 10:23:27.405118+00	12	2348	2016-09-30		Курушев Андрей	89163140142	Т281ЕС777	15	12	2016-09-30 10:23:27.405118+00
332	I	2016-09-30 10:26:07.977302+00	12	2349	2016-09-30		Алексеев Сергей	89150954247	У158ОР77	12	12	2016-09-30 10:26:07.977302+00
333	I	2016-10-03 04:43:45.956831+00	465	2352	2016-10-03		ЧувакинЕвгений Борисович		м359рк197	5	465	2016-10-03 04:43:45.956831+00
334	U	2016-10-03 06:26:39.367385+00	68	2345	2016-09-30		Хапилов Олег Борисович	89154323236	в374ку199	4	68	2016-09-30 08:34:09.235536+00
335	U	2016-10-03 06:26:39.379223+00	68	2345	2016-09-30		Хапилов Олег Борисович	89154323236	в374ку199	4	68	2016-09-30 08:34:09.235536+00
336	I	2016-10-03 06:28:58.927012+00	81	2353	2016-10-03					0	81	2016-10-03 06:28:58.927012+00
337	U	2016-10-03 07:03:42.283024+00	68	2345	2016-09-30		Хапилов Олег Борисович	89154323236	в374ку199	3	68	2016-09-30 08:34:09.235536+00
338	U	2016-10-03 07:29:40.937474+00	81	2353	2016-10-03		Засядкин Александр Евгеньевич	89151875004	М004РН190	4	81	2016-10-03 06:28:58.927012+00
339	I	2016-10-03 07:33:14.382637+00	81	2354	2016-10-03					0	81	2016-10-03 07:33:14.382637+00
340	I	2016-10-03 09:02:49.042649+00	553	2355	2016-10-03	Экогарант	Петров Александр	89168384433		1	553	2016-10-03 09:02:49.042649+00
341	I	2016-10-04 07:53:34.244058+00	562	2356	2016-10-04		Иванов	222	555	8	562	2016-10-04 07:53:34.244058+00
342	D	2016-10-04 07:54:07.187008+00	562	2356	2016-10-04		Иванов	222	555	8	562	2016-10-04 07:53:34.244058+00
343	I	2016-10-04 08:05:06.651455+00	126	2357	2016-10-04		ст	89852124682	0	2	126	2016-10-04 08:05:06.651455+00
344	I	2016-10-04 08:05:43.824007+00	562	2358	2016-10-04		Иванов			8	562	2016-10-04 08:05:43.824007+00
345	D	2016-10-04 08:36:30.506921+00	496	2347	2016-09-30		Ляхов С.П. 	89090046425	С 049 ВН	0	496	2016-09-30 09:47:56.411152+00
346	I	2016-10-04 08:37:37.891055+00	496	2359	2016-10-04		Ляхов С.П. 	89090076425	С 049 ВН 	0	496	2016-10-04 08:37:37.891055+00
347	I	2016-10-04 10:54:04.886233+00	68	2360	2016-10-04		Кислицин			0	68	2016-10-04 10:54:04.886233+00
348	I	2016-10-04 11:25:06.110431+00	59	2361	2016-10-04					0	59	2016-10-04 11:25:06.110431+00
349	I	2016-10-04 13:12:55.918939+00	68	2362	2016-10-04					0	68	2016-10-04 13:12:55.918939+00
350	U	2016-10-05 02:04:38.198314+00	503	2333	2016-09-29		Черненок Сергей Александрович	89773301412	Е961НР177	0	503	2016-09-29 04:53:04.170134+00
351	I	2016-10-05 04:50:54.248083+00	70	2364	2016-10-05		Журавлев Андрей Владиславович	9163638151	О904ОЕ777	2	70	2016-10-05 04:50:54.248083+00
352	I	2016-10-05 05:02:54.507721+00	503	2365	2016-10-05		Москалев К.Е.	89612208500	А050АР154	0	503	2016-10-05 05:02:54.507721+00
353	I	2016-10-05 05:10:32.406182+00	538	2366	2016-10-05		Кудряшов А.М.		А470км/69	0	538	2016-10-05 05:10:32.406182+00
354	U	2016-10-05 05:29:47.037326+00	538	2366	2016-10-05		Кудряшов А.М.		А470КМ/69	0	538	2016-10-05 05:10:32.406182+00
355	U	2016-10-05 05:30:56.045944+00	68	2362	2016-10-04		Кислицын Н В	89772703809	в664ку199	6	68	2016-10-04 13:12:55.918939+00
356	I	2016-10-05 06:16:02.18637+00	503	2367	2016-10-05		Москалев К.Е.	89612208500	А050АР154	0	503	2016-10-05 06:16:02.18637+00
357	I	2016-10-05 07:47:29.561929+00	53	2368	2016-10-05		Пустовойтов В.И.	89652411295	к580ро197	4	53	2016-10-05 07:47:29.561929+00
358	U	2016-10-05 13:59:43.174061+00	126	2357	2016-10-04		Беляев Иван Васильевич	89670945377	к753ст197	2	126	2016-10-04 08:05:06.651455+00
359	I	2016-10-06 05:10:01.929415+00	503	2369	2016-10-06		Москалев К.Е.	89612208500	А050АР154	0	503	2016-10-06 05:10:01.929415+00
361	U	2016-10-06 10:04:04.010101+00	562	2358	2016-10-04		Иванов			9	562	2016-10-04 08:05:43.824007+00
362	I	2016-10-06 12:19:44.578494+00	11	2371	2016-10-06		Иванов Иван 	88018018888	УУ111УУ	1	11	2016-10-06 12:19:44.578494+00
363	D	2016-10-06 12:26:39.640548+00	11	2371	2016-10-06		Иванов Иван 	88018018888	УУ111УУ	1	11	2016-10-06 12:19:44.578494+00
365	D	2016-10-06 12:44:36.833267+00	63	2372	2016-10-06					0	63	2016-10-06 12:43:49.743016+00
370	I	2016-10-07 12:03:10.532441+00	68	2377	2016-10-07					0	68	2016-10-07 12:03:10.532441+00
371	I	2016-10-07 12:03:15.225367+00	68	2378	2016-10-07					0	68	2016-10-07 12:03:15.225367+00
376	U	2016-10-11 04:19:27.656406+00	503	2367	2016-10-05		Черненок С.А.	89773301412	Е961НР177	0	503	2016-10-05 06:16:02.18637+00
377	I	2016-10-11 06:23:54.674808+00	14	2382	2016-10-11					5	14	2016-10-11 06:23:54.674808+00
378	D	2016-10-11 06:24:14.617824+00	14	2382	2016-10-11					5	14	2016-10-11 06:23:54.674808+00
379	I	2016-10-11 07:43:51.485711+00	503	2383	2016-10-11		Москалев К.Е.	89612208500	А050АР154	0	503	2016-10-11 07:43:51.485711+00
381	I	2016-10-11 11:17:07.466074+00	59	2385	2016-10-11					0	59	2016-10-11 11:17:07.466074+00
385	U	2016-10-12 07:17:01.155055+00	503	2369	2016-10-06		Истомин В.С.	89221998147	в659сн196	0	503	2016-10-06 05:10:01.929415+00
388	I	2016-10-12 11:11:18.016314+00	496	2389	2016-10-12		Ляхов С.П. 	89090076425	С 049 ВН	0	496	2016-10-12 11:11:18.016314+00
390	I	2016-10-12 13:20:32.756345+00	12	2391	2016-10-12		Алексеев Сергей	89150954247	У158Ор77	12	12	2016-10-12 13:20:32.756345+00
391	I	2016-10-12 13:21:36.296107+00	12	2392	2016-10-12		Курушев Андрей	89163140142	Т281ЕС777	15	12	2016-10-12 13:21:36.296107+00
360	I	2016-10-06 09:26:14.501754+00	559	2370	2016-10-06	возможны изменения в данных на машину и водителя	Александров Генадий Валерьевич	89111112171	В321В	1	559	2016-10-06 09:26:14.501754+00
364	I	2016-10-06 12:43:49.743016+00	63	2372	2016-10-06					0	63	2016-10-06 12:43:49.743016+00
366	I	2016-10-06 12:52:00.739289+00	465	2373	2016-10-06		Бровченко Юрий Викторович		М134ХХ	4	465	2016-10-06 12:52:00.739289+00
367	I	2016-10-06 14:59:52.002893+00	549	2374	2016-10-06		Овчинников Петр		С746ОА197	2	549	2016-10-06 14:59:52.002893+00
368	I	2016-10-07 05:44:50.239552+00	12	2375	2016-10-07		Курушев Андрей	89163140142	Т281ЕС777	15	12	2016-10-07 05:44:50.239552+00
369	I	2016-10-07 07:23:14.031161+00	53	2376	2016-10-07		Павлов М.А.	89264957040	н077ср90	4	53	2016-10-07 07:23:14.031161+00
372	U	2016-10-10 05:58:29.226278+00	68	2377	2016-10-07		Кислицын НВ	+79772703809	к664хв197	1	68	2016-10-07 12:03:10.532441+00
373	I	2016-10-10 08:12:12.54367+00	53	2379	2016-10-10		Пустовойт В.И.	89652411295	к580ро197	4	53	2016-10-10 08:12:12.54367+00
374	I	2016-10-10 08:34:57.87472+00	12	2380	2016-10-10		Милов Николай	89096468700	М186ТР777	8	12	2016-10-10 08:34:57.87472+00
375	I	2016-10-10 08:35:51.528603+00	12	2381	2016-10-10		Курушев Андрей	89163140142	Т281ЕС777	15	12	2016-10-10 08:35:51.528603+00
382	D	2016-10-12 01:37:33.820017+00	503	2383	2016-10-11		Москалев К.Е.	89612208500	А050АР154	0	503	2016-10-11 07:43:51.485711+00
383	I	2016-10-12 03:38:13.572167+00	503	2386	2016-10-12		Москалев К.Е.	89612208500	А050АР154	0	503	2016-10-12 03:38:13.572167+00
384	I	2016-10-12 03:44:49.487765+00	503	2387	2016-10-12		Москалев К.Е.	89612208500	А050АР154	0	503	2016-10-12 03:44:49.487765+00
386	I	2016-10-12 09:07:25.924292+00	538	2388	2016-10-12		Кудряшов А.М.		А470КМ/69	0	538	2016-10-12 09:07:25.924292+00
387	U	2016-10-12 10:48:01.400666+00	538	2388	2016-10-12		Кудряшов Н.М.		О708ЕС/69	0	538	2016-10-12 09:07:25.924292+00
389	I	2016-10-12 12:20:43.722847+00	128	2390	2016-10-12		Буланов		х951вн50	3	128	2016-10-12 12:20:43.722847+00
392	D	2016-10-12 13:28:10.386341+00	12	2391	2016-10-12		Алексеев Сергей	89150954247	У158Ор77	12	12	2016-10-12 13:20:32.756345+00
393	I	2016-10-12 13:28:55.135927+00	12	2393	2016-10-12		Алексеев Сергей	89150954247	У158ОР77	12	12	2016-10-12 13:28:55.135927+00
394	I	2016-10-13 01:08:05.259237+00	503	2394	2016-10-13		Москалев К.Е.	89612208500	А050АР154	0	503	2016-10-13 01:08:05.259237+00
395	I	2016-10-13 05:19:55.423094+00	12	2395	2016-10-13		Алексеев Сергей	89150954247	У158ОР77	11	12	2016-10-13 05:19:55.423094+00
396	D	2016-10-13 05:20:01.183045+00	12	2393	2016-10-12		Алексеев Сергей	89150954247	У158ОР77	12	12	2016-10-12 13:28:55.135927+00
397	I	2016-10-13 06:48:10.67703+00	503	2396	2016-10-13		Москалев К.Е.	89612208500	А050АР154	0	503	2016-10-13 06:48:10.67703+00
398	I	2016-10-13 07:04:52.105851+00	562	2397	2016-10-13					0	562	2016-10-13 07:04:52.105851+00
399	I	2016-10-13 07:39:43.182041+00	53	2398	2016-10-13		Пустовойт В.И.	89652411295	к580ро197	4	53	2016-10-13 07:39:43.182041+00
400	I	2016-10-13 08:17:53.112906+00	12	2399	2016-10-13		Милов Николай	89096468700	М186ТР777	8	12	2016-10-13 08:17:53.112906+00
401	I	2016-10-13 11:43:21.560235+00	549	2400	2016-10-13		Бойко К	89153121446	М685СУ777	5	549	2016-10-13 11:43:21.560235+00
402	U	2016-10-13 14:21:11.472397+00	562	2397	2016-10-13					4	562	2016-10-13 07:04:52.105851+00
403	I	2016-10-14 06:12:24.849601+00	57	2403	2016-10-14					0	57	2016-10-14 06:12:24.849601+00
404	I	2016-10-14 06:26:42.904606+00	126	2404	2016-10-14		Гришин Н.В.	111	А856АВ177	1	126	2016-10-14 06:26:42.904606+00
405	I	2016-10-14 07:56:03.679406+00	53	2405	2016-10-14		Пустовойт В.И.	89652411295	к580ро197	2	53	2016-10-14 07:56:03.679406+00
406	I	2016-10-14 09:15:30.33591+00	70	2406	2016-10-14		Журавлев Андрей Владиславович	9163638151	О904ОЕ777	0	70	2016-10-14 09:15:30.33591+00
407	I	2016-10-14 09:57:03.816674+00	538	2407	2016-10-14		Лазарев			0	538	2016-10-14 09:57:03.816674+00
408	I	2016-10-14 09:57:15.952322+00	538	2408	2016-10-14		Лазарев			0	538	2016-10-14 09:57:15.952322+00
409	U	2016-10-14 10:29:23.453306+00	538	2408	2016-10-14		Лазарев В.		М436СА/69	0	538	2016-10-14 09:57:15.952322+00
410	D	2016-10-14 10:29:50.429977+00	538	2407	2016-10-14		Лазарев			0	538	2016-10-14 09:57:03.816674+00
411	I	2016-10-14 10:34:12.633069+00	81	2409	2016-10-14					0	81	2016-10-14 10:34:12.633069+00
412	I	2016-10-14 10:42:00.497403+00	69	2410	2016-10-14		Бобков Дмитрий Сергеевич	89167048378	О725ВР	5	69	2016-10-14 10:42:00.497403+00
413	I	2016-10-14 13:16:50.547413+00	68	2411	2016-10-14					0	68	2016-10-14 13:16:50.547413+00
414	D	2016-10-17 03:37:17.757322+00	503	2365	2016-10-05		Москалев К.Е.	89612208500	А050АР154	0	503	2016-10-05 05:02:54.507721+00
415	I	2016-10-17 04:10:33.516446+00	465	2412	2016-10-17		Чиканчи Иван Степанович		а378то197	6	465	2016-10-17 04:10:33.516446+00
416	U	2016-10-17 07:09:12.048324+00	68	2411	2016-10-14		Пешехонцев В А	89269728205	м496ва197	6	68	2016-10-14 13:16:50.547413+00
417	I	2016-10-17 07:39:30.211302+00	12	2413	2016-10-17		милов Николай	89096468700	М186ТР777	8	12	2016-10-17 07:39:30.211302+00
418	I	2016-10-17 08:54:46.489185+00	53	2414	2016-10-17		Пустовойт В.И.	89652411295	к580ро197	4	53	2016-10-17 08:54:46.489185+00
419	I	2016-10-17 09:15:58.416326+00	53	2415	2016-10-17		Пустовойт В.И.	89652411295	к580ро197	4	53	2016-10-17 09:15:58.416326+00
420	I	2016-10-17 13:17:32.302399+00	53	2416	2016-10-17		Павлов М.	89264957040	н077ср90	4	53	2016-10-17 13:17:32.302399+00
421	U	2016-10-18 05:44:33.489712+00	503	2394	2016-10-13		Черненок С.А.	89773301412	Е961НР177	0	503	2016-10-13 01:08:05.259237+00
422	I	2016-10-18 07:36:02.030184+00	553	2417	2016-10-18		Петров	89168384433		0	553	2016-10-18 07:36:02.030184+00
423	I	2016-10-18 08:31:26.686853+00	126	2418	2016-10-18		Ступич	6	1	2	126	2016-10-18 08:31:26.686853+00
424	I	2016-10-18 10:48:21.109163+00	128	2419	2016-10-18		Буланов		х951вн50	0	128	2016-10-18 10:48:21.109163+00
425	I	2016-10-18 11:46:39.898596+00	562	2420	2016-10-18		Иванов			0	562	2016-10-18 11:46:39.898596+00
426	I	2016-10-18 12:15:15.937455+00	59	2421	2016-10-18					0	59	2016-10-18 12:15:15.937455+00
427	U	2016-10-19 01:53:54.486106+00	503	2396	2016-10-13		Чарышкин О.В.	89222156836	х947ое96	0	503	2016-10-13 06:48:10.67703+00
428	I	2016-10-19 08:43:43.932915+00	64	2426	2016-10-19		1	1	1	3	64	2016-10-19 08:43:43.932915+00
429	D	2016-10-19 08:45:21.381794+00	64	2426	2016-10-19		1	1	1	3	64	2016-10-19 08:43:43.932915+00
430	I	2016-10-19 08:45:37.21022+00	64	2427	2016-10-19		1	1	1	3	64	2016-10-19 08:45:37.21022+00
431	I	2016-10-19 08:53:54.963754+00	503	2428	2016-10-19		Москалев К.Е.	89612208500	А050АР154	0	503	2016-10-19 08:53:54.963754+00
432	U	2016-10-19 08:53:59.256532+00	64	2427	2016-10-19		Смирнов	1	В948РУ178	3	64	2016-10-19 08:45:37.21022+00
433	U	2016-10-19 08:54:24.38729+00	64	2427	2016-10-19		Смирнов Юрий 	8904-6168644	В948РУ178	3	64	2016-10-19 08:45:37.21022+00
434	I	2016-10-19 09:01:02.276875+00	89	2430	2016-10-19		Рубцов В.П.	89209462250	В627НВ33	0	89	2016-10-19 09:01:02.276875+00
435	I	2016-10-19 11:41:24.237964+00	555	2486	2016-10-19					0	555	2016-10-19 11:41:24.237964+00
436	U	2016-10-19 11:46:50.124483+00	555	2486	2016-10-19		Вертелецкий Олег Анатольевич  		Р746МР67	7	555	2016-10-19 11:41:24.237964+00
437	I	2016-10-20 02:13:32.52728+00	503	2487	2016-10-20		Москалев К.Е.	89612208500	А050АР154	0	503	2016-10-20 02:13:32.52728+00
438	I	2016-10-20 05:53:15.112135+00	562	2488	2016-10-20		Иванов			0	562	2016-10-20 05:53:15.112135+00
439	I	2016-10-20 05:53:25.382124+00	562	2489	2016-10-20		Иванов			0	562	2016-10-20 05:53:25.382124+00
441	U	2016-10-20 08:32:01.084183+00	562	2489	2016-10-20		Иванов			7	562	2016-10-20 05:53:25.382124+00
442	U	2016-10-20 09:15:53.069348+00	126	2418	2016-10-18		Возный С.М.	6	а341оу190	2	126	2016-10-18 08:31:26.686853+00
445	I	2016-10-21 08:36:45.556675+00	81	2492	2016-10-21					0	81	2016-10-21 08:36:45.556675+00
447	I	2016-10-21 11:28:45.356811+00	88	2494	2016-10-21					5	88	2016-10-21 11:28:45.356811+00
448	I	2016-10-21 11:29:07.186898+00	88	2495	2016-10-21					5	88	2016-10-21 11:29:07.186898+00
449	U	2016-10-21 11:29:45.076088+00	88	2494	2016-10-21	Савин Трейдинг				5	88	2016-10-21 11:28:45.356811+00
450	U	2016-10-21 11:30:30.477628+00	88	2495	2016-10-21	Савин Трейдинг				5	88	2016-10-21 11:29:07.186898+00
453	I	2016-10-24 05:31:51.762622+00	465	2498	2016-10-24		Матвее Юрий Николаевич		К581МН197	6	465	2016-10-24 05:31:51.762622+00
455	D	2016-10-24 07:09:52.526589+00	465	2498	2016-10-24		Матвее Юрий Николаевич		К581МН197	6	465	2016-10-24 05:31:51.762622+00
456	I	2016-10-24 07:11:05.387404+00	465	2500	2016-10-24		Матвеев Юрий Николаевич		К581МН197	6	465	2016-10-24 07:11:05.387404+00
460	U	2016-10-24 07:47:51.647002+00	555	2503	2016-10-24		Булдаков Вадим Валерьевич 			8	555	2016-10-24 07:47:29.399951+00
462	U	2016-10-24 07:49:03.673367+00	555	2503	2016-10-24		Булдаков Вадим Валерьевич 	89190418142	Т032ЕХ67	8	555	2016-10-24 07:47:29.399951+00
463	I	2016-10-24 08:27:25.567511+00	94	3311	2016-10-24		Белов Руслан Вячеславович	89268289555	в593нр750	1	94	2016-10-24 08:27:25.567511+00
464	D	2016-10-24 08:56:50.435026+00	68	2497	2016-10-24		анеликов С А		м495ва197	6	68	2016-10-24 05:29:59.849968+00
465	I	2016-10-24 09:08:49.898408+00	64	3344	2016-10-24					6	64	2016-10-24 09:08:49.898408+00
466	U	2016-10-25 04:01:48.484366+00	503	2487	2016-10-20		Черненок С. А.	89773301412	Е961НР177	0	503	2016-10-20 02:13:32.52728+00
467	I	2016-10-25 05:48:55.489901+00	538	3345	2016-10-25		Лазарев В		а470км/69	0	538	2016-10-25 05:48:55.489901+00
472	U	2016-10-25 09:47:38.83853+00	538	3345	2016-10-25		Лазарев В.			0	538	2016-10-25 05:48:55.489901+00
474	I	2016-10-25 10:05:07.403533+00	128	3351	2016-10-25		Буланов		х951вн50	0	128	2016-10-25 10:05:07.403533+00
476	I	2016-10-25 13:01:36.428842+00	59	3353	2016-10-25					0	59	2016-10-25 13:01:36.428842+00
477	I	2016-10-25 13:42:53.036794+00	555	3354	2016-10-25		Юлдошев Жамшид Жураевич	89687556240	м783ка750	1	555	2016-10-25 13:42:53.036794+00
479	U	2016-10-26 01:54:15.761334+00	503	2428	2016-10-19		Черенок С.А	89773301412	Е961НР177	0	503	2016-10-19 08:53:54.963754+00
481	I	2016-10-26 02:39:13.27041+00	503	3355	2016-10-26		Москалев	89612208500	Р636РР04	0	503	2016-10-26 02:39:13.27041+00
483	I	2016-10-26 02:40:56.24764+00	503	3356	2016-10-26		Москалев К.Е.	89612208500	Р636РР04	0	503	2016-10-26 02:40:56.24764+00
440	U	2016-10-20 08:31:54.073148+00	562	2488	2016-10-20		Иванов			7	562	2016-10-20 05:53:15.112135+00
443	I	2016-10-20 09:36:04.784042+00	53	2490	2016-10-20		Петрушин Ю.А.	89165462407	р475мх97	4	53	2016-10-20 09:36:04.784042+00
444	I	2016-10-20 12:22:12.963589+00	69	2491	2016-10-20		Лосев Олег Николаевич	89684176769	Р281РУ	3	69	2016-10-20 12:22:12.963589+00
446	I	2016-10-21 10:28:03.800818+00	68	2493	2016-10-21					0	68	2016-10-21 10:28:03.800818+00
451	I	2016-10-24 04:17:23.253143+00	503	2496	2016-10-24		Москалев К.Е.	89612208500	Р636РР04	0	503	2016-10-24 04:17:23.253143+00
452	I	2016-10-24 05:29:59.849968+00	68	2497	2016-10-24					0	68	2016-10-24 05:29:59.849968+00
454	I	2016-10-24 07:06:23.744785+00	14	2499	2016-10-24		Уткин Юрий Александрович		х220оо69	16	14	2016-10-24 07:06:23.744785+00
457	U	2016-10-24 07:22:58.119018+00	68	2497	2016-10-24		анеликов С А		м495ва197	6	68	2016-10-24 05:29:59.849968+00
458	I	2016-10-24 07:44:21.60064+00	12	2501	2016-10-24		Алексеев Сергей	89150954847	У158ОР77	11	12	2016-10-24 07:44:21.60064+00
459	I	2016-10-24 07:47:29.399951+00	555	2503	2016-10-24					0	555	2016-10-24 07:47:29.399951+00
461	U	2016-10-24 07:48:30.317779+00	555	2503	2016-10-24		Булдаков Вадим Валерьевич 	89190418142		8	555	2016-10-24 07:47:29.399951+00
468	I	2016-10-25 07:52:35.76967+00	64	3346	2016-10-25		СмирновЮрийВалерьевич	89046168644	В948РУ178	1	64	2016-10-25 07:52:35.76967+00
469	I	2016-10-25 08:28:18.961129+00	67	3347	2016-10-25	Товар предоставляется не  на паллетах а в коробах	Сошин Михаил 	+79166809293	Е511РН190	0	67	2016-10-25 08:28:18.961129+00
470	I	2016-10-25 08:28:24.566183+00	70	3348	2016-10-25		Журавлев Андрей Владиславович	9163638151	О904ОЕ777	0	70	2016-10-25 08:28:24.566183+00
471	I	2016-10-25 08:33:17.598152+00	67	3349	2016-10-25		Сошин М.	+79166409293	Е511РН190	0	67	2016-10-25 08:33:17.598152+00
473	I	2016-10-25 09:59:16.424112+00	126	3350	2016-10-25		С	1	а	2	126	2016-10-25 09:59:16.424112+00
475	I	2016-10-25 10:11:17.646112+00	496	3352	2016-10-25		Ляхов С.П. 	89090076425	С 049 ВН 	0	496	2016-10-25 10:11:17.646112+00
478	U	2016-10-26 01:52:16.217564+00	503	2487	2016-10-20		Киров А.Н	89122867823	Р240КО96	0	503	2016-10-20 02:13:32.52728+00
480	D	2016-10-26 02:30:13.915301+00	503	2428	2016-10-19		Черенок С.А	89773301412	Е961НР177	0	503	2016-10-19 08:53:54.963754+00
482	D	2016-10-26 02:39:28.866376+00	503	3355	2016-10-26		Москалев	89612208500	Р636РР04	0	503	2016-10-26 02:39:13.27041+00
484	I	2016-10-26 07:38:59.081359+00	503	3357	2016-10-26		Москалев К.Е.	89612208500	Р636РР04	0	503	2016-10-26 07:38:59.081359+00
485	I	2016-10-26 08:09:01.762612+00	57	3358	2016-10-26					0	57	2016-10-26 08:09:01.762612+00
486	I	2016-10-26 10:40:44.459238+00	68	3359	2016-10-26					0	68	2016-10-26 10:40:44.459238+00
487	I	2016-10-26 12:31:37.268266+00	89	3360	2016-10-26		Котов Валерий Леонтьевич	89319034424	А475КВ	0	89	2016-10-26 12:31:37.268266+00
488	U	2016-10-27 02:10:03.838941+00	503	3356	2016-10-26		Козырев М.Р.	89612208500	Р963УН77	0	503	2016-10-26 02:40:56.24764+00
489	I	2016-10-27 09:22:54.998524+00	503	3361	2016-10-27		Москалев К.Е.	89612208500	Р636РР04	0	503	2016-10-27 09:22:54.998524+00
490	I	2016-10-27 11:11:59.036326+00	555	3363	2016-10-27		Риндзак Юрий Миронович	89156525892		1	555	2016-10-27 11:11:59.036326+00
491	U	2016-10-27 11:12:34.803861+00	555	3363	2016-10-27		Риндзак Юрий Миронович	89156525892	P 827 УН	1	555	2016-10-27 11:11:59.036326+00
492	I	2016-10-27 11:14:04.047689+00	549	3364	2016-10-27		Солдатов А		С746ОА197	4	549	2016-10-27 11:14:04.047689+00
493	D	2016-10-28 04:43:43.530368+00	503	3361	2016-10-27		Москалев К.Е.	89612208500	Р636РР04	0	503	2016-10-27 09:22:54.998524+00
494	I	2016-10-28 04:45:08.137335+00	503	3365	2016-10-28		Москалев К.Е	89612208500	Р636РР04	0	503	2016-10-28 04:45:08.137335+00
495	I	2016-10-28 04:59:35.722309+00	538	3366	2016-10-28		Кудряшов			0	538	2016-10-28 04:59:35.722309+00
496	I	2016-10-28 04:59:59.129644+00	538	3367	2016-10-28		Кудряшов			0	538	2016-10-28 04:59:59.129644+00
497	I	2016-10-28 05:15:09.227135+00	562	3368	2016-10-28		Иванов			7	562	2016-10-28 05:15:09.227135+00
498	I	2016-10-28 07:05:42.18975+00	559	3371	2016-10-28	возможна замена машины	Котов Борис Анатольевич	9111112171	В325УК98	1	559	2016-10-28 07:05:42.18975+00
499	I	2016-10-28 07:08:37.792239+00	559	3372	2016-10-28	возможна замена машины	Котов Борис Анатольевич	9111112171	В325УК98	2	559	2016-10-28 07:08:37.792239+00
500	U	2016-10-28 08:01:15.084336+00	538	3366	2016-10-28		Кудряшов Н.М.			0	538	2016-10-28 04:59:35.722309+00
501	U	2016-10-28 08:01:35.46542+00	538	3367	2016-10-28		Кудряшов Н.М.			0	538	2016-10-28 04:59:59.129644+00
502	U	2016-10-28 08:03:09.187814+00	538	3366	2016-10-28		Кудряшов Н.М.		О708ЕС/69	0	538	2016-10-28 04:59:35.722309+00
503	U	2016-10-28 08:03:28.252218+00	538	3367	2016-10-28		Кудряшов Н.М.		О708ЕС/69	0	538	2016-10-28 04:59:59.129644+00
504	I	2016-10-28 08:08:45.855423+00	70	3373	2016-10-28		Журавлев Андрей Владиславович	9163638151	О904ОЕ777	0	70	2016-10-28 08:08:45.855423+00
505	I	2016-10-28 11:10:55.227256+00	81	3374	2016-10-28					0	81	2016-10-28 11:10:55.227256+00
506	I	2016-10-28 11:12:22.227251+00	69	3376	2016-10-28					0	69	2016-10-28 11:12:22.227251+00
507	U	2016-10-28 11:13:14.944115+00	69	3376	2016-10-28		Гасанов Рауф Ханбала Оглы	89162457658	Х814МК	4	69	2016-10-28 11:12:22.227251+00
508	I	2016-10-28 11:57:51.330582+00	53	3377	2016-10-28		Бердикулов А.С.	89060650371	о056ав50	6	53	2016-10-28 11:57:51.330582+00
509	I	2016-10-28 12:27:32.986125+00	549	3378	2016-10-28		Сивцев А		Т081СЕ197	0	549	2016-10-28 12:27:32.986125+00
510	I	2016-10-28 13:10:01.469604+00	465	3379	2016-10-28		Чиканчи Иван Степанович		А378ТО199	5	465	2016-10-28 13:10:01.469604+00
511	U	2016-10-31 04:25:24.902922+00	503	2496	2016-10-24		Маслов Ю.А.	89612208500	В684УХ96	0	503	2016-10-24 04:17:23.253143+00
512	I	2016-10-31 07:18:49.099468+00	555	3380	2016-10-31		Старченков Сергей Геннадьевич	89056961066	У334ЕО6	0	555	2016-10-31 07:18:49.099468+00
513	I	2016-10-31 09:29:52.351569+00	64	3398	2016-10-31		Смирнов Юрий Валерьевич	89046168644	В948РУ178	2	64	2016-10-31 09:29:52.351569+00
514	U	2016-11-02 06:15:05.36384+00	503	3365	2016-10-28		Истомин В.С.	89612208500	Р636РР04	0	503	2016-10-28 04:45:08.137335+00
515	I	2016-11-02 06:57:35.454634+00	57	3402	2016-11-02		Львов Сергей Николаевич	89261581792	Е272АО77	0	57	2016-11-02 06:57:35.454634+00
516	I	2016-11-02 07:17:34.849933+00	64	3417	2016-11-02		1	1	1	1	64	2016-11-02 07:17:34.849933+00
517	D	2016-11-02 07:17:49.490449+00	64	3417	2016-11-02		1	1	1	1	64	2016-11-02 07:17:34.849933+00
518	I	2016-11-02 07:19:15.877703+00	64	3419	2016-11-02		1	1	1	1	64	2016-11-02 07:19:15.877703+00
519	D	2016-11-02 07:20:22.291053+00	64	3419	2016-11-02		1	1	1	1	64	2016-11-02 07:19:15.877703+00
520	I	2016-11-02 07:39:22.90724+00	64	3430	2016-11-02		1	1	1	1	64	2016-11-02 07:39:22.90724+00
521	U	2016-11-02 07:41:06.30028+00	64	3430	2016-11-02		ДеловыеЛинии	1	1	1	64	2016-11-02 07:39:22.90724+00
522	U	2016-11-02 07:41:48.669847+00	64	3430	2016-11-02		ДеловыеЛинии	89046168644	948	1	64	2016-11-02 07:39:22.90724+00
523	I	2016-11-02 10:20:20.400202+00	12	3431	2016-11-02		Алексеев Сергей	89150954247	У158Ор77	11	12	2016-11-02 10:20:20.400202+00
524	I	2016-11-02 10:21:07.74128+00	12	3432	2016-11-02		Курушев Андрей	89163140142	Т281ЕС777	15	12	2016-11-02 10:21:07.74128+00
525	I	2016-11-02 10:22:43.338099+00	12	3433	2016-11-02		Васькин Кирилл	89166104642	Р427ОС777	8	12	2016-11-02 10:22:43.338099+00
526	I	2016-11-02 11:16:35.832335+00	75	3436	2016-11-02		Стаславский Олег	89035182557	О083МЕ50	20	75	2016-11-02 11:16:35.832335+00
527	I	2016-11-02 12:08:15.55979+00	126	3437	2016-11-02		Битейкин Ю.В.	89099140307	е327уо13	2	126	2016-11-02 12:08:15.55979+00
528	I	2016-11-02 12:42:10.299065+00	549	3439	2016-11-02		Махонько М		В109ТА197	1	549	2016-11-02 12:42:10.299065+00
529	I	2016-11-02 13:35:24.651507+00	12	3440	2016-11-02		Алексеев Сергей	89150954247	У158ОР77	11	12	2016-11-02 13:35:24.651507+00
530	U	2016-11-02 14:24:35.30343+00	64	3398	2016-10-31		Беков Ратмир Хасанбиевич	89046168644	Т187ХМ190	2	64	2016-10-31 09:29:52.351569+00
531	I	2016-11-03 06:02:36.358797+00	562	3446	2016-11-03		Иванов			13	562	2016-11-03 06:02:36.358797+00
532	I	2016-11-03 06:48:09.08645+00	81	3447	2016-11-03					0	81	2016-11-03 06:48:09.08645+00
533	I	2016-11-03 06:48:51.724284+00	69	3448	2016-11-03					0	69	2016-11-03 06:48:51.724284+00
534	I	2016-11-03 06:57:35.408192+00	68	3449	2016-11-03					0	68	2016-11-03 06:57:35.408192+00
535	I	2016-11-03 08:11:30.281207+00	53	3452	2016-11-03		Павлов М.	89264957040	н077ср90	5	53	2016-11-03 08:11:30.281207+00
536	U	2016-11-03 10:07:54.741528+00	69	3448	2016-11-03		Гасанов Рауф Ханбала Оглы	89162457658	Х814МК	5	69	2016-11-03 06:48:51.724284+00
537	I	2016-11-03 13:08:35.652962+00	572	3453	2016-11-03		Распопов Владимир Александрович	89035373216	Х304МК190	0	572	2016-11-03 13:08:35.652962+00
\.


--
-- TOC entry 2324 (class 0 OID 0)
-- Dependencies: 202
-- Name: donut_doc_periods_audit_donutdocperiodsauditid_seq; Type: SEQUENCE SET; Schema: audit; Owner: postgres
--

SELECT pg_catalog.setval('donut_doc_periods_audit_donutdocperiodsauditid_seq', 537, true);


--
-- TOC entry 2280 (class 0 OID 21087)
-- Dependencies: 205
-- Data for Name: orders_audit; Type: TABLE DATA; Schema: audit; Owner: postgres
--

COPY orders_audit (ordersauditid, operation, stamp, userid, orderid, ordernumber, boxqty, finaldestinationwarehouseid, donutdocperiodid, orderstatus, commentforstatus, invoicenumber, goodscost, orderpalletsqty, orderdate, invoicedate, deliverydate, weight, volume, lastmodified) FROM stdin;
1	I	2016-07-09 11:01:00.791788+00	12	3	5	4	3	197	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-07-09 11:01:00.791788+00
2	U	2016-07-09 11:02:15.693231+00	32	3	5	4	3	197	ARRIVED			0.00	0	\N	\N	\N	\N	\N	2016-07-09 11:01:00.791788+00
3	U	2016-07-09 11:02:51.586299+00	25	3	5	4	3	197	ERROR	,hgcfkhk		0.00	0	\N	\N	\N	\N	\N	2016-07-09 11:01:00.791788+00
4	U	2016-07-09 11:03:01.379944+00	25	3	5	4	3	197	DELIVERED	,hgcfkhk		0.00	0	\N	\N	\N	\N	\N	2016-07-09 11:01:00.791788+00
5	I	2016-07-13 10:33:48.059204+00	15	7	4	6	3	202	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-07-13 10:33:48.059204+00
6	I	2016-07-13 10:33:48.059204+00	15	8	1	7	3	202	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-07-13 10:33:48.059204+00
7	I	2016-07-13 10:33:48.059204+00	15	9	3	4	3	202	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-07-13 10:33:48.059204+00
8	I	2016-07-13 10:33:48.059204+00	15	10	2	6	3	202	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-07-13 10:33:48.059204+00
9	U	2016-07-13 10:38:43.085271+00	32	7	4	6	3	202	ARRIVED			0.00	0	\N	\N	\N	\N	\N	2016-07-13 10:33:48.059204+00
10	U	2016-07-13 10:38:43.085271+00	32	8	1	7	3	202	ARRIVED			0.00	0	\N	\N	\N	\N	\N	2016-07-13 10:33:48.059204+00
11	U	2016-07-13 10:38:43.085271+00	32	9	3	4	3	202	ARRIVED			0.00	0	\N	\N	\N	\N	\N	2016-07-13 10:33:48.059204+00
12	U	2016-07-13 10:38:43.085271+00	32	10	2	6	3	202	ARRIVED			0.00	0	\N	\N	\N	\N	\N	2016-07-13 10:33:48.059204+00
13	I	2016-07-14 12:17:49.806692+00	12	11	3	0	3	203	CREATED			0.00	14	\N	\N	\N	\N	\N	2016-07-14 12:17:49.806692+00
14	I	2016-07-14 12:17:49.806692+00	12	12	4	0	7	203	CREATED			0.00	1	\N	\N	\N	\N	\N	2016-07-14 12:17:49.806692+00
15	I	2016-07-14 12:37:24.347308+00	12	13	1	0	3	204	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-07-14 12:37:24.347308+00
16	D	2016-07-14 12:37:27.135198+00	12	13	1	0	3	204	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-07-14 12:37:24.347308+00
17	I	2016-07-14 16:11:06.026614+00	12	14		0	3	230	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-07-14 16:11:06.026614+00
18	D	2016-07-14 16:11:08.755496+00	12	14		0	3	230	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-07-14 16:11:06.026614+00
19	I	2016-07-14 16:11:42.70342+00	12	15		0	3	231	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-07-14 16:11:42.70342+00
20	D	2016-07-14 16:11:44.652836+00	12	15		0	3	231	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-07-14 16:11:42.70342+00
21	I	2016-07-19 09:24:28.661874+00	12	17	1	0	3	549	CREATED			0.00	10	\N	\N	\N	\N	\N	2016-07-19 09:24:28.661874+00
22	D	2016-07-19 09:24:35.748124+00	12	17	1	0	3	549	CREATED			0.00	10	\N	\N	\N	\N	\N	2016-07-19 09:24:28.661874+00
23	I	2016-07-19 09:26:22.142659+00	12	19	1	0	3	551	CREATED			0.00	10	\N	\N	\N	\N	\N	2016-07-19 09:26:22.142659+00
24	D	2016-07-19 09:26:25.369783+00	12	19	1	0	3	551	CREATED			0.00	10	\N	\N	\N	\N	\N	2016-07-19 09:26:22.142659+00
25	I	2016-07-19 09:27:49.94574+00	12	20	1	0	3	552	CREATED			0.00	10	\N	\N	\N	\N	\N	2016-07-19 09:27:49.94574+00
26	D	2016-07-19 09:28:35.250494+00	12	20	1	0	3	552	CREATED			0.00	10	\N	\N	\N	\N	\N	2016-07-19 09:27:49.94574+00
27	U	2016-07-19 09:32:19.310182+00	25	11	3	0	3	203	DELIVERED			0.00	14	\N	\N	\N	\N	\N	2016-07-14 12:17:49.806692+00
28	U	2016-07-19 09:32:19.310182+00	25	12	4	0	7	203	DELIVERED			0.00	1	\N	\N	\N	\N	\N	2016-07-14 12:17:49.806692+00
29	U	2016-07-19 09:32:42.126678+00	25	12	4	0	7	203	ERROR	Паллета повреждена		0.00	1	\N	\N	\N	\N	\N	2016-07-14 12:17:49.806692+00
30	U	2016-07-19 09:33:01.002899+00	25	11	3	0	3	203	ARRIVED			0.00	14	\N	\N	\N	\N	\N	2016-07-14 12:17:49.806692+00
31	U	2016-07-19 09:33:01.002899+00	25	12	4	0	7	203	ARRIVED	Паллета повреждена		0.00	1	\N	\N	\N	\N	\N	2016-07-14 12:17:49.806692+00
32	I	2016-07-20 09:21:09.953448+00	12	22	ЕК-5148007	0	7	555	CREATED		6545,6548,6544,6546,6547,6550,6554,7526,7487	0.00	15	\N	\N	\N	\N	\N	2016-07-20 09:21:09.953448+00
33	I	2016-07-21 09:17:38.212968+00	12	23	ЕК-5148009	0	3	556	CREATED			0.00	8	\N	\N	\N	\N	\N	2016-07-21 09:17:38.212968+00
34	I	2016-07-27 09:00:53.971644+00	12	25	ЕК-5148795	0	3	560	CREATED		6794,6779,6781,6783,6778,6791,6792,6797,6802,7563,6784,7574	0.00	12	\N	\N	\N	\N	\N	2016-07-27 09:00:53.971644+00
35	I	2016-07-28 09:28:03.152018+00	57	26		0	4	561	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-07-28 09:28:03.152018+00
36	I	2016-07-28 09:28:03.152018+00	57	27		0	7	561	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-07-28 09:28:03.152018+00
37	I	2016-07-28 09:28:03.152018+00	57	28		0	3	561	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-07-28 09:28:03.152018+00
38	I	2016-07-28 12:56:50.126638+00	57	29		99	3	561	CREATED		897	1053048.04	9	\N	\N	\N	\N	\N	2016-07-28 12:56:50.126638+00
39	U	2016-07-28 12:56:50.126638+00	57	27		1	7	561	CREATED		899	6563.53	0	\N	\N	\N	\N	\N	2016-07-28 09:28:03.152018+00
40	U	2016-07-28 12:56:50.126638+00	57	28		99	7	561	CREATED		898	829723.18	8	\N	\N	\N	\N	\N	2016-07-28 09:28:03.152018+00
41	U	2016-07-28 12:56:50.126638+00	57	26		51	4	561	CREATED		895	344338.55	3	\N	\N	\N	\N	\N	2016-07-28 09:28:03.152018+00
42	I	2016-07-28 13:25:28.361821+00	53	30	1	64	3	562	CREATED			408439.00	4	\N	\N	\N	\N	\N	2016-07-28 13:25:28.361821+00
43	I	2016-08-01 10:13:17.396213+00	12	31	1265	25	8	563	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-08-01 10:13:17.396213+00
44	I	2016-08-01 10:14:32.579885+00	12	32		0	7	563	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-08-01 10:14:32.579885+00
45	D	2016-08-01 10:14:38.588446+00	12	32		0	7	563	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-08-01 10:14:32.579885+00
46	D	2016-08-01 10:14:38.588446+00	12	31	1265	25	8	563	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-08-01 10:13:17.396213+00
47	I	2016-08-03 08:01:57.413198+00	12	33	Ек-5149704	0	3	801	CREATED		7005	0.00	15	\N	\N	\N	\N	\N	2016-08-03 08:01:57.413198+00
48	I	2016-08-03 08:03:51.275783+00	12	34	Ек-5149723	0	3	802	CREATED		7018	0.00	5	\N	\N	\N	\N	\N	2016-08-03 08:03:51.275783+00
49	I	2016-08-03 13:13:51.107528+00	12	35	ЕК-5149723	0	7	803	CREATED		7017	0.00	10	\N	\N	\N	\N	\N	2016-08-03 13:13:51.107528+00
50	D	2016-08-03 13:13:55.596943+00	12	34	Ек-5149723	0	3	802	CREATED		7018	0.00	5	\N	\N	\N	\N	\N	2016-08-03 08:03:51.275783+00
51	I	2016-08-03 14:48:06.92731+00	53	37	1	99	3	805	CREATED		21439	492093.00	5	\N	\N	\N	\N	\N	2016-08-03 14:48:06.92731+00
52	D	2016-08-04 07:12:33.274688+00	53	37	1	99	3	805	CREATED		21439	492093.00	5	\N	\N	\N	\N	\N	2016-08-03 14:48:06.92731+00
53	I	2016-08-04 07:21:25.372286+00	53	38	1	99	3	806	CREATED		17847, 17844, 17846	492094.00	4	\N	\N	\N	\N	\N	2016-08-04 07:21:25.372286+00
54	I	2016-08-05 11:00:10.573483+00	69	42		0	3	811	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-08-05 11:00:10.573483+00
55	I	2016-08-05 12:03:39.372464+00	66	43		1	6	812	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-08-05 12:03:39.372464+00
56	D	2016-08-05 12:04:06.207694+00	66	43		1	6	812	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-08-05 12:03:39.372464+00
57	I	2016-08-05 12:44:26.657195+00	76	44	ЕК-5150184	5	3	813	CREATED		11255	35845.48	0	\N	\N	\N	\N	\N	2016-08-05 12:44:26.657195+00
58	I	2016-08-05 13:00:41.9902+00	76	46	ЕК-5150182	17	7	813	CREATED		11257	162350.16	0	\N	\N	\N	\N	\N	2016-08-05 13:00:41.9902+00
59	I	2016-08-05 13:00:41.9902+00	76	47	ЕК-5150185	3	4	813	CREATED		11254	8288.56	0	\N	\N	\N	\N	\N	2016-08-05 13:00:41.9902+00
60	I	2016-08-05 13:00:41.9902+00	76	48	СПБ-013618	7	5	813	CREATED		11262	46512.04	0	\N	\N	\N	\N	\N	2016-08-05 13:00:41.9902+00
61	I	2016-08-05 14:00:31.501968+00	76	49	СПб-013619	22	6	813	CREATED		11269	192979.24	0	\N	\N	\N	\N	\N	2016-08-05 14:00:31.501968+00
62	I	2016-08-05 16:20:18.236996+00	12	50	111	12	3	815	CREATED			222.22	0	\N	\N	\N	\N	\N	2016-08-05 16:20:18.236996+00
63	D	2016-08-05 16:20:27.130869+00	12	50	111	12	3	815	CREATED			222.22	0	\N	\N	\N	\N	\N	2016-08-05 16:20:18.236996+00
64	I	2016-08-08 07:36:27.230911+00	12	51	Ек-5150052	0	3	816	CREATED		7127	0.00	15	\N	\N	\N	\N	\N	2016-08-08 07:36:27.230911+00
65	I	2016-08-08 07:37:49.552047+00	12	52	Ек-5150081	0	3	817	CREATED		7145	0.00	12	\N	\N	\N	\N	\N	2016-08-08 07:37:49.552047+00
66	I	2016-08-09 14:49:04.000426+00	53	53		99	3	831	CREATED			600000.00	4	\N	\N	\N	\N	\N	2016-08-09 14:49:04.000426+00
67	D	2016-08-09 15:07:33.41809+00	53	53		99	3	831	CREATED			600000.00	4	\N	\N	\N	\N	\N	2016-08-09 14:49:04.000426+00
68	I	2016-08-10 09:56:27.377546+00	12	54	Ек-5150515	0	7	832	CREATED		7293	0.00	15	\N	\N	\N	\N	\N	2016-08-10 09:56:27.377546+00
69	I	2016-08-10 09:59:24.142836+00	12	55	Ек-5150532	0	7	833	CREATED			0.00	11	\N	\N	\N	\N	\N	2016-08-10 09:59:24.142836+00
70	I	2016-08-10 13:35:32.908166+00	59	56	5150742	0	3	834	CREATED	Альфа Нева СПБ	ГФ0811-0007	39178.48	1	\N	\N	\N	\N	\N	2016-08-10 13:35:32.908166+00
71	I	2016-08-10 13:35:32.908166+00	59	57	5150741	0	3	834	CREATED	Смирнов бэттериз Екатеринбург	ГФ0811-0006	143109.99	6	\N	\N	\N	\N	\N	2016-08-10 13:35:32.908166+00
72	I	2016-08-10 13:35:32.908166+00	59	58	5150736	0	3	834	CREATED	СБ Логистик Ульяновск	ГФ0811-0005	14543.92	1	\N	\N	\N	\N	\N	2016-08-10 13:35:32.908166+00
73	I	2016-08-10 13:35:32.908166+00	59	59	5150747	0	3	834	CREATED	СБ Логистик Москва	ГФ0811-0008	126491.68	4	\N	\N	\N	\N	\N	2016-08-10 13:35:32.908166+00
74	I	2016-08-12 08:50:32.806255+00	12	66	ЕК-5150856	0	3	837	CREATED		7407	0.00	15	\N	\N	\N	\N	\N	2016-08-12 08:50:32.806255+00
75	I	2016-08-12 08:52:11.401685+00	12	67	Ек-5150883	0	4	838	CREATED		7431	0.00	11	\N	\N	\N	\N	\N	2016-08-12 08:52:11.401685+00
76	I	2016-08-12 09:35:29.939619+00	67	68		0	3	839	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-08-12 09:35:29.939619+00
77	I	2016-08-12 11:13:36.140292+00	69	69	5150649,5150655	0	3	840	CREATED			0.00	5	\N	\N	\N	\N	\N	2016-08-12 11:13:36.140292+00
78	I	2016-08-16 07:09:13.516311+00	59	70		0	3	850	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-08-16 07:09:13.516311+00
79	U	2016-08-16 07:11:28.7817+00	59	70		0	3	850	CREATED		5151287	0.00	0	\N	\N	\N	\N	\N	2016-08-16 07:09:13.516311+00
82	I	2016-08-17 12:36:58.867057+00	12	72	Ек-515338	0	7	851	CREATED		7588	0.00	15	\N	\N	\N	\N	\N	2016-08-17 12:36:58.867057+00
83	I	2016-08-22 07:11:32.38165+00	53	74	1	99	3	854	CREATED		19196,19198	214611.00	4	\N	\N	\N	\N	\N	2016-08-22 07:11:32.38165+00
85	I	2016-08-24 13:20:28.666022+00	12	76	Ек-5152095	0	7	857	CREATED		7860	0.00	15	\N	\N	\N	\N	\N	2016-08-24 13:20:28.666022+00
86	I	2016-08-24 13:21:28.631958+00	12	77		0	7	858	CREATED			0.00	11	\N	\N	\N	\N	\N	2016-08-24 13:21:28.631958+00
87	D	2016-08-24 13:22:45.677969+00	12	76	Ек-5152095	0	7	857	CREATED		7860	0.00	15	\N	\N	\N	\N	\N	2016-08-24 13:20:28.666022+00
88	I	2016-08-24 13:24:18.793444+00	12	78	Ек-5152095	0	7	859	CREATED		7860	0.00	15	\N	\N	\N	\N	\N	2016-08-24 13:24:18.793444+00
89	I	2016-08-25 12:25:03.373442+00	59	79	5152504	0	3	861	CREATED			0.00	2	\N	\N	\N	\N	\N	2016-08-25 12:25:03.373442+00
90	I	2016-08-25 12:25:03.373442+00	59	80	5152503	0	3	861	CREATED			0.00	1	\N	\N	\N	\N	\N	2016-08-25 12:25:03.373442+00
91	U	2016-08-25 12:32:16.045455+00	59	80	5152503	0	3	861	CREATED		ГФ0826-0021	0.00	1	\N	\N	\N	\N	\N	2016-08-25 12:25:03.373442+00
92	U	2016-08-25 12:32:16.045455+00	59	79	5152504	0	3	861	CREATED		ГФ0826-0020	0.00	2	\N	\N	\N	\N	\N	2016-08-25 12:25:03.373442+00
97	I	2016-08-31 11:07:35.162425+00	472	87		0	7	994	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-08-31 11:07:35.162425+00
98	I	2016-08-31 13:00:57.021291+00	59	88	Ек-5153113	0	3	997	CREATED	Сб Логистик Москва	ГФ0901-0010	56354.98	2	\N	\N	\N	\N	\N	2016-08-31 13:00:57.021291+00
99	I	2016-08-31 13:00:57.021291+00	59	89	Ек-5153116	0	3	997	CREATED	Смирнов Бэттериз Екатеринбург	ГФ0901-0011	94839.52	4	\N	\N	\N	\N	\N	2016-08-31 13:00:57.021291+00
104	I	2016-09-01 11:14:39.909301+00	523	93		0	3	1014	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-01 11:14:39.909301+00
105	I	2016-09-01 11:37:11.7155+00	526	94	МЛ-310	0	3	1019	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-01 11:37:11.7155+00
106	I	2016-09-01 11:37:11.7155+00	526	95	МЛ-309	0	3	1019	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-01 11:37:11.7155+00
80	I	2016-08-16 07:13:18.572541+00	59	71		0	3	850	CREATED		5151263	0.00	3	\N	\N	\N	\N	\N	2016-08-16 07:13:18.572541+00
81	U	2016-08-16 07:13:18.572541+00	59	70		0	3	850	CREATED		5151287	0.00	2	\N	\N	\N	\N	\N	2016-08-16 07:09:13.516311+00
84	I	2016-08-24 12:33:50.854364+00	64	75		0	7	856	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-08-24 12:33:50.854364+00
93	I	2016-08-29 08:14:11.169352+00	12	81		0	3	864	CREATED			0.00	11	\N	\N	\N	\N	\N	2016-08-29 08:14:11.169352+00
94	I	2016-08-29 08:15:19.431734+00	12	82		0	3	865	CREATED			0.00	15	\N	\N	\N	\N	\N	2016-08-29 08:15:19.431734+00
95	I	2016-08-30 12:03:26.532978+00	53	86	1	99	3	970	CREATED			735900.00	10	\N	\N	\N	\N	\N	2016-08-30 12:03:26.532978+00
96	U	2016-08-31 09:29:57.66346+00	97	86	1	99	3	970	DELIVERED	Прибытие на территорию в 11:30		735900.00	10	\N	\N	\N	\N	\N	2016-08-30 12:03:26.532978+00
100	I	2016-09-01 08:08:33.794959+00	12	90		0	7	1003	CREATED			0.00	9	\N	\N	\N	\N	\N	2016-09-01 08:08:33.794959+00
101	I	2016-09-01 08:09:15.658327+00	12	91		0	7	1004	CREATED			0.00	8	\N	\N	\N	\N	\N	2016-09-01 08:09:15.658327+00
102	U	2016-09-01 09:02:51.378364+00	97	88	Ек-5153113	0	3	997	DELIVERED	Сб Логистик Москва	ГФ0901-0010	56354.98	2	\N	\N	\N	\N	\N	2016-08-31 13:00:57.021291+00
103	U	2016-09-01 09:02:51.378364+00	97	89	Ек-5153116	0	3	997	DELIVERED	Смирнов Бэттериз Екатеринбург	ГФ0901-0011	94839.52	4	\N	\N	\N	\N	\N	2016-08-31 13:00:57.021291+00
107	D	2016-09-01 11:39:19.497433+00	526	94	МЛ-310	0	3	1019	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-01 11:37:11.7155+00
108	D	2016-09-01 11:39:19.497433+00	526	95	МЛ-309	0	3	1019	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-01 11:37:11.7155+00
109	I	2016-09-01 11:43:13.959203+00	526	96	93	0	3	1023	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-01 11:43:13.959203+00
110	D	2016-09-01 11:56:24.405609+00	17	93		0	3	1014	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-01 11:14:39.909301+00
111	U	2016-09-01 11:56:28.507608+00	97	90		0	7	1003	DELIVERED			0.00	9	\N	\N	\N	\N	\N	2016-09-01 08:08:33.794959+00
112	U	2016-09-01 11:56:37.330338+00	97	91		0	7	1004	DELIVERED			0.00	8	\N	\N	\N	\N	\N	2016-09-01 08:09:15.658327+00
113	D	2016-09-01 11:59:22.669383+00	17	67	Ек-5150883	0	4	838	CREATED		7431	0.00	11	\N	\N	\N	\N	\N	2016-08-12 08:52:11.401685+00
114	U	2016-09-01 12:52:06.974627+00	97	89	Ек-5153116	0	3	997	ERROR	Екатеринбург	ГФ0901-0011	94839.52	4	\N	\N	\N	\N	\N	2016-08-31 13:00:57.021291+00
115	U	2016-09-01 12:52:19.230328+00	97	89	Ек-5153116	0	3	997	DELIVERED	Екатеринбург	ГФ0901-0011	94839.52	4	\N	\N	\N	\N	\N	2016-08-31 13:00:57.021291+00
116	I	2016-09-02 08:48:12.452724+00	528	104	№94	0	3	1028	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-02 08:48:12.452724+00
117	I	2016-09-02 12:01:22.456837+00	89	107		0	3	1031	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-02 12:01:22.456837+00
118	I	2016-09-02 12:03:01.948488+00	89	108		0	3	1031	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-02 12:03:01.948488+00
119	I	2016-09-02 12:47:22.121614+00	465	109		0	3	1032	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-02 12:47:22.121614+00
120	U	2016-09-02 12:51:35.762786+00	465	109		64	3	1032	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-02 12:47:22.121614+00
121	I	2016-09-05 06:41:56.9658+00	68	114	014433	20	6	1036	CREATED		1118	136188.00	1	\N	\N	\N	\N	\N	2016-09-05 06:41:56.9658+00
122	I	2016-09-05 06:41:56.9658+00	68	115	014434	7	5	1036	CREATED		1119	51204.00	1	\N	\N	\N	\N	\N	2016-09-05 06:41:56.9658+00
123	U	2016-09-05 09:15:51.389119+00	23	87		0	7	994	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-08-31 11:07:35.162425+00
124	U	2016-09-05 10:35:46.141696+00	97	109		64	3	1032	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-09-02 12:47:22.121614+00
125	U	2016-09-05 10:36:02.697188+00	97	114	014433	20	6	1036	DELIVERED		1118	136188.00	1	\N	\N	\N	\N	\N	2016-09-05 06:41:56.9658+00
126	U	2016-09-05 10:36:02.697188+00	97	115	014434	7	5	1036	DELIVERED		1119	51204.00	1	\N	\N	\N	\N	\N	2016-09-05 06:41:56.9658+00
127	I	2016-09-06 08:56:50.067962+00	128	117		0	3	1042	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-06 08:56:50.067962+00
128	I	2016-09-07 10:06:37.335201+00	472	123		0	7	1049	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-07 10:06:37.335201+00
129	U	2016-09-07 12:51:44.119926+00	97	117		0	3	1042	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-09-06 08:56:50.067962+00
130	I	2016-09-07 13:23:05.703458+00	59	124	Ек-5153833	0	3	1050	CREATED	Екатеринбург	ГФ0908-0008	57392.20	2	\N	\N	\N	\N	\N	2016-09-07 13:23:05.703458+00
131	I	2016-09-07 13:23:05.703458+00	59	125	Ек-5153856	96	3	1050	CREATED	СПБ	ГФ0908-0006	28993.04	1	\N	\N	\N	\N	\N	2016-09-07 13:23:05.703458+00
132	I	2016-09-07 13:23:05.703458+00	59	126	Ек-5153829	94	3	1050	CREATED	Ульяновск	ГФ0908-0009	36796.83	2	\N	\N	\N	\N	\N	2016-09-07 13:23:05.703458+00
133	I	2016-09-07 13:23:05.703458+00	59	127	Ек-5153865	0	3	1050	CREATED	Москва	ГФ0908-0007	83863.43	2	\N	\N	\N	\N	\N	2016-09-07 13:23:05.703458+00
134	U	2016-09-08 10:48:01.932926+00	97	125	Ек-5153856	96	3	1050	DELIVERED	СПБ	ГФ0908-0006	28993.04	1	\N	\N	\N	\N	\N	2016-09-07 13:23:05.703458+00
135	U	2016-09-08 10:48:01.932926+00	97	127	Ек-5153865	0	3	1050	DELIVERED	Москва	ГФ0908-0007	83863.43	2	\N	\N	\N	\N	\N	2016-09-07 13:23:05.703458+00
136	U	2016-09-08 10:48:01.932926+00	97	126	Ек-5153829	94	3	1050	DELIVERED	Ульяновск	ГФ0908-0009	36796.83	2	\N	\N	\N	\N	\N	2016-09-07 13:23:05.703458+00
137	U	2016-09-08 10:48:01.932926+00	97	124	Ек-5153833	0	3	1050	DELIVERED	Екатеринбург	ГФ0908-0008	57392.20	2	\N	\N	\N	\N	\N	2016-09-07 13:23:05.703458+00
138	I	2016-09-09 12:24:07.571651+00	465	128		49	3	1052	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-09 12:24:07.571651+00
140	I	2016-09-09 13:32:43.752015+00	528	131	№97	0	4	1055	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-09 13:32:43.752015+00
141	I	2016-09-09 13:36:52.185657+00	527	132	№316	0	7	1056	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-09 13:36:52.185657+00
142	I	2016-09-12 06:04:35.158567+00	94	133		0	3	1057	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-12 06:04:35.158567+00
143	I	2016-09-12 06:20:06.278465+00	68	134	Ек-5154271	72	3	1053	CREATED		1151	545256.00	2	\N	\N	\N	\N	\N	2016-09-12 06:20:06.278465+00
144	I	2016-09-12 06:20:06.278465+00	68	135	 Ек-5154265	29	7	1053	CREATED		1149	227688.00	1	\N	\N	\N	\N	\N	2016-09-12 06:20:06.278465+00
145	I	2016-09-12 06:20:06.278465+00	68	136	СПб-014619	55	6	1053	CREATED		1155	375768.00	2	\N	\N	\N	\N	\N	2016-09-12 06:20:06.278465+00
146	I	2016-09-12 06:20:06.278465+00	68	137	 Ек-5154260	21	4	1053	CREATED		1150	164544.00	1	\N	\N	\N	\N	\N	2016-09-12 06:20:06.278465+00
147	I	2016-09-12 06:20:06.278465+00	68	138	СПб-014622	6	3	1053	CREATED		1156	28656.00	0	\N	\N	\N	\N	\N	2016-09-12 06:20:06.278465+00
148	U	2016-09-12 06:22:03.853684+00	68	138	СПб-014622	6	6	1053	CREATED		1156	28656.00	0	\N	\N	\N	\N	\N	2016-09-12 06:20:06.278465+00
149	D	2016-09-12 07:25:49.802263+00	94	133		0	3	1057	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-12 06:04:35.158567+00
150	U	2016-09-12 08:44:44.844774+00	97	128		49	3	1052	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-09-09 12:24:07.571651+00
151	U	2016-09-12 10:52:33.45492+00	97	136	СПб-014619	55	6	1053	DELIVERED		1155	375768.00	2	\N	\N	\N	\N	\N	2016-09-12 06:20:06.278465+00
152	U	2016-09-12 10:52:33.45492+00	97	137	 Ек-5154260	21	4	1053	DELIVERED		1150	164544.00	1	\N	\N	\N	\N	\N	2016-09-12 06:20:06.278465+00
153	U	2016-09-12 10:52:33.45492+00	97	134	Ек-5154271	72	3	1053	DELIVERED		1151	545256.00	2	\N	\N	\N	\N	\N	2016-09-12 06:20:06.278465+00
154	U	2016-09-12 10:52:33.45492+00	97	135	 Ек-5154265	29	7	1053	DELIVERED		1149	227688.00	1	\N	\N	\N	\N	\N	2016-09-12 06:20:06.278465+00
155	U	2016-09-12 10:52:33.45492+00	97	138	СПб-014622	6	6	1053	DELIVERED		1156	28656.00	0	\N	\N	\N	\N	\N	2016-09-12 06:20:06.278465+00
156	I	2016-09-12 13:35:36.379426+00	528	141	98	0	4	1487	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-12 13:35:36.379426+00
157	I	2016-09-13 03:42:13.453697+00	472	142		0	7	1491	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-13 03:42:13.453697+00
159	I	2016-09-13 12:57:02.035609+00	538	146		0	3	1501	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-13 12:57:02.035609+00
160	I	2016-09-13 12:57:02.035609+00	538	147		0	3	1501	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-13 12:57:02.035609+00
161	D	2016-09-13 12:57:31.065436+00	538	146		0	3	1501	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-13 12:57:02.035609+00
162	D	2016-09-13 12:57:31.065436+00	538	147		0	3	1501	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-13 12:57:02.035609+00
163	I	2016-09-13 12:58:37.440527+00	538	148		0	3	1502	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-13 12:58:37.440527+00
166	I	2016-09-13 14:04:10.839568+00	59	151	Ек-5154676	52	6	1505	CREATED		ГФ0914-0016	11391.52	1	\N	\N	\N	\N	\N	2016-09-13 14:04:10.839568+00
167	I	2016-09-13 14:04:10.839568+00	59	152	Ек-5154665	26	4	1505	CREATED		ГФ0914-0014	11804.74	1	\N	\N	\N	\N	\N	2016-09-13 14:04:10.839568+00
168	I	2016-09-13 14:04:10.839568+00	59	153	Ек-5154679	99	3	1505	CREATED		ГФ0914-0015	52889.24	2	\N	\N	\N	\N	\N	2016-09-13 14:04:10.839568+00
169	I	2016-09-13 14:04:10.839568+00	59	154	Ек-5154670	91	7	1505	CREATED		ГФ0914-0013	36102.09	2	\N	\N	\N	\N	\N	2016-09-13 14:04:10.839568+00
170	I	2016-09-14 08:08:46.208197+00	15	155	Ек-5154410	0	3	1507	CREATED		36149,36150	27398.00	1	\N	\N	\N	\N	\N	2016-09-14 08:08:46.208197+00
171	I	2016-09-14 08:08:46.208197+00	15	156	Ек-5154428	0	8	1507	CREATED		36154	6482.00	1	\N	\N	\N	\N	\N	2016-09-14 08:08:46.208197+00
172	I	2016-09-14 08:08:46.208197+00	15	157	Ек-5154408	0	7	1507	CREATED		36147,36148	46111.00	1	\N	\N	\N	\N	\N	2016-09-14 08:08:46.208197+00
173	I	2016-09-14 08:08:46.208197+00	15	158	Ек-5154419	0	4	1507	CREATED		36152,36153	24235.00	1	\N	\N	\N	\N	\N	2016-09-14 08:08:46.208197+00
174	I	2016-09-14 08:08:46.208197+00	15	159	Ек-5154387	0	7	1507	CREATED		36143,36144,36145	423838.00	5	\N	\N	\N	\N	\N	2016-09-14 08:08:46.208197+00
175	I	2016-09-14 08:08:46.208197+00	15	160	Ек-5154397	0	3	1507	CREATED		36140,36141,36142	187443.00	3	\N	\N	\N	\N	\N	2016-09-14 08:08:46.208197+00
158	I	2016-09-13 11:09:53.937759+00	62	143		0	3	1495	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-13 11:09:53.937759+00
164	I	2016-09-13 13:04:40.986585+00	538	149		0	3	1503	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-13 13:04:40.986585+00
165	I	2016-09-13 13:05:19.719043+00	538	150		0	3	1504	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-13 13:05:19.719043+00
176	U	2016-09-14 08:28:00.90192+00	97	152	Ек-5154665	26	4	1505	DELIVERED		ГФ0914-0014	11804.74	1	\N	\N	\N	\N	\N	2016-09-13 14:04:10.839568+00
177	U	2016-09-14 08:28:00.90192+00	97	153	Ек-5154679	99	3	1505	DELIVERED		ГФ0914-0015	52889.24	2	\N	\N	\N	\N	\N	2016-09-13 14:04:10.839568+00
178	U	2016-09-14 08:28:00.90192+00	97	151	Ек-5154676	52	6	1505	DELIVERED		ГФ0914-0016	11391.52	1	\N	\N	\N	\N	\N	2016-09-13 14:04:10.839568+00
179	U	2016-09-14 08:28:00.90192+00	97	154	Ек-5154670	91	7	1505	DELIVERED		ГФ0914-0013	36102.09	2	\N	\N	\N	\N	\N	2016-09-13 14:04:10.839568+00
180	I	2016-09-14 08:40:20.809365+00	69	161	Ек-5154152	42	3	1496	CREATED		2031	56826.00	1	\N	\N	\N	\N	\N	2016-09-14 08:40:20.809365+00
181	I	2016-09-14 08:40:20.809365+00	69	162	Ек-5154102	15	3	1496	CREATED		2029	20295.00	1	\N	\N	\N	\N	\N	2016-09-14 08:40:20.809365+00
182	U	2016-09-14 09:27:46.812156+00	15	159	Ек-5154387	0	7	1507	CREATED		36143,36144,36145	423838.00	1	\N	\N	\N	\N	\N	2016-09-14 08:08:46.208197+00
183	U	2016-09-14 09:27:46.812156+00	15	160	Ек-5154397	0	3	1507	CREATED		36140,36141,36142	187443.00	1	\N	\N	\N	\N	\N	2016-09-14 08:08:46.208197+00
184	I	2016-09-14 09:32:45.629482+00	70	163	Ек-5154326	9	3	1090	CREATED		4115	41867.22	1	\N	\N	\N	\N	\N	2016-09-14 09:32:45.629482+00
185	I	2016-09-14 09:32:45.629482+00	70	164	Ек-5154319	10	7	1090	CREATED		4117	45917.76	1	\N	\N	\N	\N	\N	2016-09-14 09:32:45.629482+00
186	U	2016-09-14 10:07:08.390087+00	97	164	Ек-5154319	10	7	1090	DELIVERED		4117	45917.76	1	\N	\N	\N	\N	\N	2016-09-14 09:32:45.629482+00
187	U	2016-09-14 10:07:08.390087+00	97	163	Ек-5154326	9	3	1090	DELIVERED		4115	41867.22	1	\N	\N	\N	\N	\N	2016-09-14 09:32:45.629482+00
188	U	2016-09-14 10:57:29.369472+00	97	161	Ек-5154152	42	3	1496	DELIVERED		2031	56826.00	1	\N	\N	\N	\N	\N	2016-09-14 08:40:20.809365+00
189	U	2016-09-14 10:57:29.369472+00	97	162	Ек-5154102	15	3	1496	DELIVERED		2029	20295.00	1	\N	\N	\N	\N	\N	2016-09-14 08:40:20.809365+00
190	I	2016-09-14 12:45:31.972502+00	126	165		0	3	1508	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-14 12:45:31.972502+00
191	I	2016-09-15 05:29:31.958445+00	94	166		0	3	2246	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-15 05:29:31.958445+00
192	I	2016-09-15 07:17:48.828681+00	53	167	1	99	3	2247	CREATED		21393	480644.00	4	\N	\N	\N	\N	\N	2016-09-15 07:17:48.828681+00
193	I	2016-09-15 07:21:03.116368+00	53	169	2	99	3	2249	CREATED		21392	326456.00	4	\N	\N	\N	\N	\N	2016-09-15 07:21:03.116368+00
194	U	2016-09-15 08:47:24.438223+00	97	166		0	3	2246	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-09-15 05:29:31.958445+00
195	U	2016-09-15 11:15:50.316125+00	97	167	1	99	3	2247	DELIVERED		21393	480644.00	4	\N	\N	\N	\N	\N	2016-09-15 07:17:48.828681+00
196	U	2016-09-15 11:15:57.686575+00	97	169	2	99	3	2249	DELIVERED		21392	326456.00	4	\N	\N	\N	\N	\N	2016-09-15 07:21:03.116368+00
197	U	2016-09-15 11:16:18.268709+00	97	148		0	3	1502	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-09-13 12:58:37.440527+00
198	U	2016-09-15 11:16:27.0007+00	97	149		0	3	1503	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-09-13 13:04:40.986585+00
199	U	2016-09-15 11:16:34.0874+00	97	150		0	3	1504	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-09-13 13:05:19.719043+00
200	I	2016-09-15 11:52:33.748409+00	528	170	99	0	4	2250	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-15 11:52:33.748409+00
201	I	2016-09-15 11:54:07.970056+00	528	171	100	0	4	2251	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-15 11:54:07.970056+00
202	I	2016-09-15 12:53:32.525217+00	534	175		0	3	2254	CREATED			0.00	8	\N	\N	\N	\N	\N	2016-09-15 12:53:32.525217+00
205	D	2016-09-15 14:00:01.421574+00	126	165		0	3	1508	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-14 12:45:31.972502+00
206	I	2016-09-15 14:06:58.502003+00	126	179	Ек-5154775	20	3	2256	CREATED		10097	143484.90	1	\N	\N	\N	\N	\N	2016-09-15 14:06:58.502003+00
207	U	2016-09-15 14:11:52.290458+00	97	160	Ек-5154397	0	3	1507	DELIVERED		36140,36141,36142	187443.00	1	\N	\N	\N	\N	\N	2016-09-14 08:08:46.208197+00
208	U	2016-09-15 14:11:52.290458+00	97	156	Ек-5154428	0	8	1507	DELIVERED		36154	6482.00	1	\N	\N	\N	\N	\N	2016-09-14 08:08:46.208197+00
209	U	2016-09-15 14:11:52.290458+00	97	155	Ек-5154410	0	3	1507	DELIVERED		36149,36150	27398.00	1	\N	\N	\N	\N	\N	2016-09-14 08:08:46.208197+00
210	U	2016-09-15 14:11:52.290458+00	97	159	Ек-5154387	0	7	1507	DELIVERED		36143,36144,36145	423838.00	1	\N	\N	\N	\N	\N	2016-09-14 08:08:46.208197+00
211	U	2016-09-15 14:11:52.290458+00	97	158	Ек-5154419	0	4	1507	DELIVERED		36152,36153	24235.00	1	\N	\N	\N	\N	\N	2016-09-14 08:08:46.208197+00
212	U	2016-09-15 14:11:52.290458+00	97	157	Ек-5154408	0	7	1507	DELIVERED		36147,36148	46111.00	1	\N	\N	\N	\N	\N	2016-09-14 08:08:46.208197+00
213	I	2016-09-16 06:52:14.013704+00	69	180	Ек-5154824	33	3	2258	CREATED		2079	35645.28	1	\N	\N	\N	\N	\N	2016-09-16 06:52:14.013704+00
214	I	2016-09-16 06:52:14.013704+00	69	181	Ек-5154826	94	3	2258	CREATED		2080	102357.28	2	\N	\N	\N	\N	\N	2016-09-16 06:52:14.013704+00
215	D	2016-09-16 07:22:56.981917+00	69	180	Ек-5154824	33	3	2258	CREATED		2079	35645.28	1	\N	\N	\N	\N	\N	2016-09-16 06:52:14.013704+00
216	D	2016-09-16 07:22:56.981917+00	69	181	Ек-5154826	94	3	2258	CREATED		2080	102357.28	2	\N	\N	\N	\N	\N	2016-09-16 06:52:14.013704+00
217	I	2016-09-16 07:26:32.414548+00	69	182	Ек-5154824	33	3	2259	CREATED		2079	35645.28	1	\N	\N	\N	\N	\N	2016-09-16 07:26:32.414548+00
218	I	2016-09-16 07:26:32.414548+00	69	183	Ек-5154826	94	3	2259	CREATED		2080	102357.28	2	\N	\N	\N	\N	\N	2016-09-16 07:26:32.414548+00
219	I	2016-09-16 08:14:55.877575+00	15	184	Ек-5154960	0	3	2260	CREATED			0.00	1	\N	\N	\N	\N	\N	2016-09-16 08:14:55.877575+00
220	I	2016-09-16 08:14:55.877575+00	15	185	Ек-5155009	0	7	2260	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-16 08:14:55.877575+00
221	I	2016-09-16 08:22:52.196925+00	126	186	ЕК-5154778	11	3	2256	CREATED		10096	93050.00	1	\N	\N	\N	\N	\N	2016-09-16 08:22:52.196925+00
222	U	2016-09-16 10:53:55.589414+00	97	175		0	3	2254	DELIVERED			0.00	8	\N	\N	\N	\N	\N	2016-09-15 12:53:32.525217+00
223	U	2016-09-16 11:51:09.744897+00	97	186	ЕК-5154778	11	3	2256	DELIVERED		10096	93050.00	1	\N	\N	\N	\N	\N	2016-09-16 08:22:52.196925+00
224	U	2016-09-16 11:51:09.744897+00	97	179	Ек-5154775	20	3	2256	DELIVERED		10097	143484.90	1	\N	\N	\N	\N	\N	2016-09-15 14:06:58.502003+00
225	I	2016-09-16 12:35:51.57417+00	465	187		48	3	2262	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-16 12:35:51.57417+00
226	U	2016-09-16 12:58:34.382369+00	97	183	Ек-5154826	94	3	2259	DELIVERED		2080	102357.28	2	\N	\N	\N	\N	\N	2016-09-16 07:26:32.414548+00
227	U	2016-09-16 12:58:34.382369+00	97	182	Ек-5154824	33	3	2259	DELIVERED	Заказ на Альфу 	2079	35645.28	1	\N	\N	\N	\N	\N	2016-09-16 07:26:32.414548+00
228	I	2016-09-16 13:03:20.166236+00	68	188		0	3	2263	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-16 13:03:20.166236+00
232	I	2016-09-19 06:46:01.931944+00	68	192	 СПб-014851	69	6	2263	CREATED		1193	443892.00	2	\N	\N	\N	\N	\N	2016-09-19 06:46:01.931944+00
233	I	2016-09-19 06:46:01.931944+00	68	193	СПб-014852	3	6	2263	CREATED		1192	11496.00	0	\N	\N	\N	\N	\N	2016-09-19 06:46:01.931944+00
234	U	2016-09-19 06:46:01.931944+00	68	188	СПб-014848	9	5	2263	CREATED		1191	64380.00	1	\N	\N	\N	\N	\N	2016-09-16 13:03:20.166236+00
238	I	2016-09-19 07:06:55.152742+00	70	197	Ек-5155153	4	7	2261	CREATED		4173	18397.32	0	\N	\N	\N	\N	\N	2016-09-19 07:06:55.152742+00
239	I	2016-09-19 07:06:55.152742+00	70	198	Ек-5155145	6	3	2261	CREATED		4174	24428.34	0	\N	\N	\N	\N	\N	2016-09-19 07:06:55.152742+00
240	I	2016-09-19 07:06:55.152742+00	70	199	Ек-5155149	4	4	2261	CREATED		4175	16103.64	0	\N	\N	\N	\N	\N	2016-09-19 07:06:55.152742+00
241	U	2016-09-19 07:42:56.361881+00	97	187		48	3	2262	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-09-16 12:35:51.57417+00
242	U	2016-09-19 08:05:09.814351+00	97	199	Ек-5155149	4	4	2261	DELIVERED		4175	16103.64	0	\N	\N	\N	\N	\N	2016-09-19 07:06:55.152742+00
243	U	2016-09-19 08:05:09.814351+00	97	197	Ек-5155153	4	7	2261	DELIVERED		4173	18397.32	0	\N	\N	\N	\N	\N	2016-09-19 07:06:55.152742+00
244	U	2016-09-19 08:05:09.814351+00	97	198	Ек-5155145	6	3	2261	DELIVERED		4174	24428.34	0	\N	\N	\N	\N	\N	2016-09-19 07:06:55.152742+00
245	U	2016-09-19 08:05:20.23496+00	97	185	Ек-5155009	0	7	2260	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-09-16 08:14:55.877575+00
246	U	2016-09-19 08:05:20.23496+00	97	184	Ек-5154960	0	3	2260	DELIVERED			0.00	1	\N	\N	\N	\N	\N	2016-09-16 08:14:55.877575+00
247	U	2016-09-19 10:51:36.52025+00	97	192	 СПб-014851	69	6	2263	DELIVERED		1193	443892.00	2	\N	\N	\N	\N	\N	2016-09-19 06:46:01.931944+00
248	U	2016-09-19 10:51:36.52025+00	97	188	СПб-014848	9	5	2263	DELIVERED		1191	64380.00	1	\N	\N	\N	\N	\N	2016-09-16 13:03:20.166236+00
249	U	2016-09-19 10:51:36.52025+00	97	193	СПб-014852	3	6	2263	DELIVERED		1192	11496.00	0	\N	\N	\N	\N	\N	2016-09-19 06:46:01.931944+00
250	I	2016-09-20 12:19:58.418607+00	496	202		0	3	2272	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-20 12:19:58.418607+00
251	U	2016-09-20 12:37:20.864583+00	30	142		0	7	1491	ARRIVED			0.00	0	\N	\N	\N	\N	\N	2016-09-13 03:42:13.453697+00
252	U	2016-09-20 12:38:30.472056+00	23	142		0	7	1491	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-09-13 03:42:13.453697+00
253	U	2016-09-20 12:38:40.730989+00	23	142		0	7	1491	ERROR	,mbvkjgfckh		0.00	0	\N	\N	\N	\N	\N	2016-09-13 03:42:13.453697+00
254	U	2016-09-20 12:38:46.821684+00	23	142		0	7	1491	DELIVERED	,mbvkjgfckh		0.00	0	\N	\N	\N	\N	\N	2016-09-13 03:42:13.453697+00
255	I	2016-09-20 13:37:40.202317+00	549	203	ош-987	0	8	2280	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-20 13:37:40.202317+00
256	D	2016-09-20 13:37:44.157726+00	549	203	ош-987	0	8	2280	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-20 13:37:40.202317+00
257	I	2016-09-20 14:38:05.711045+00	59	204	Ек-5155609	99	3	2278	CREATED		ГФ0920-0018	121537.49	5	\N	\N	\N	\N	\N	2016-09-20 14:38:05.711045+00
258	I	2016-09-20 14:38:05.711045+00	59	205	Ек-5155571	99	7	2278	CREATED		ГФ0920-0011	61413.74	2	\N	\N	\N	\N	\N	2016-09-20 14:38:05.711045+00
259	I	2016-09-21 05:34:08.410384+00	68	206	Ек-5155493	35	7	2271	CREATED		1212	258084.00	1	\N	\N	\N	\N	\N	2016-09-21 05:34:08.410384+00
260	I	2016-09-21 05:34:08.410384+00	68	207	Ек-5155496	54	3	2271	CREATED		1211	396468.00	2	\N	\N	\N	\N	\N	2016-09-21 05:34:08.410384+00
261	I	2016-09-21 05:34:08.410384+00	68	208	Ек-5155494	19	4	2271	CREATED		1210	137232.00	1	\N	\N	\N	\N	\N	2016-09-21 05:34:08.410384+00
262	I	2016-09-21 06:53:18.112685+00	94	209		0	3	2282	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-21 06:53:18.112685+00
263	U	2016-09-21 07:40:23.337958+00	97	206	Ек-5155493	35	7	2271	DELIVERED		1212	258084.00	1	\N	\N	\N	\N	\N	2016-09-21 05:34:08.410384+00
264	U	2016-09-21 07:40:23.337958+00	97	207	Ек-5155496	54	3	2271	DELIVERED		1211	396468.00	2	\N	\N	\N	\N	\N	2016-09-21 05:34:08.410384+00
265	U	2016-09-21 07:40:23.337958+00	97	208	Ек-5155494	19	4	2271	DELIVERED		1210	137232.00	1	\N	\N	\N	\N	\N	2016-09-21 05:34:08.410384+00
266	I	2016-09-21 09:35:10.365741+00	53	210	1	43	3	2283	CREATED		21966,21967,21968	246120.00	3	\N	\N	\N	\N	\N	2016-09-21 09:35:10.365741+00
267	U	2016-09-21 10:48:20.723393+00	97	205	Ек-5155571	99	7	2278	DELIVERED		ГФ0920-0011	61413.74	2	\N	\N	\N	\N	\N	2016-09-20 14:38:05.711045+00
268	U	2016-09-21 10:48:20.723393+00	97	204	Ек-5155609	99	3	2278	DELIVERED		ГФ0920-0018	121537.49	5	\N	\N	\N	\N	\N	2016-09-20 14:38:05.711045+00
269	U	2016-09-21 11:16:52.469621+00	97	210	1	43	3	2283	DELIVERED		21966,21967,21968	246120.00	3	\N	\N	\N	\N	\N	2016-09-21 09:35:10.365741+00
270	D	2016-09-21 11:27:13.946023+00	94	209		0	3	2282	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-21 06:53:18.112685+00
271	I	2016-09-21 11:28:28.845961+00	94	211		0	3	2285	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-21 11:28:28.845961+00
272	I	2016-09-22 06:27:33.932928+00	92	212		0	3	2286	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-22 06:27:33.932928+00
273	I	2016-09-22 06:27:33.932928+00	92	213		0	3	2286	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-22 06:27:33.932928+00
274	I	2016-09-22 08:26:36.269994+00	528	214	103	0	4	2287	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-22 08:26:36.269994+00
275	I	2016-09-22 08:28:10.986977+00	528	215	104	0	4	2288	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-22 08:28:10.986977+00
276	U	2016-09-22 12:42:12.920819+00	97	213		0	3	2286	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-09-22 06:27:33.932928+00
277	U	2016-09-22 12:42:12.920819+00	97	212		0	3	2286	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-09-22 06:27:33.932928+00
278	I	2016-09-23 06:17:20.909715+00	538	216		0	3	2301	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-23 06:17:20.909715+00
279	I	2016-09-23 07:37:59.433267+00	538	217		0	3	2301	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-23 07:37:59.433267+00
280	I	2016-09-23 08:56:52.694308+00	69	218	Ек-5155786	62	3	2303	CREATED		2134	64759.12	2	\N	\N	\N	\N	\N	2016-09-23 08:56:52.694308+00
281	I	2016-09-23 08:56:52.694308+00	69	219	Ек-5155793	87	3	2303	CREATED		2133	118273.64	2	\N	\N	\N	\N	\N	2016-09-23 08:56:52.694308+00
282	I	2016-09-23 08:56:52.694308+00	69	220	Ек-5155787	42	3	2303	CREATED		2132	42438.96	1	\N	\N	\N	\N	\N	2016-09-23 08:56:52.694308+00
283	I	2016-09-23 10:04:23.505423+00	538	221		0	3	2304	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-23 10:04:23.505423+00
284	D	2016-09-23 10:04:54.511898+00	538	216		0	3	2301	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-23 06:17:20.909715+00
285	D	2016-09-23 10:04:54.511898+00	538	217		0	3	2301	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-23 07:37:59.433267+00
286	I	2016-09-23 12:28:12.514192+00	53	222	1	99	3	2306	CREATED		22364,22362,22341	741389.00	4	\N	\N	\N	\N	\N	2016-09-23 12:28:12.514192+00
287	I	2016-09-23 13:40:54.867869+00	53	224	2	99	3	2308	CREATED		\t22364,22362,22341	741389.00	4	\N	\N	\N	\N	\N	2016-09-23 13:40:54.867869+00
288	U	2016-09-23 15:38:11.16893+00	97	222	1	99	3	2306	DELIVERED		22364,22362,22341	741389.00	4	\N	\N	\N	\N	\N	2016-09-23 12:28:12.514192+00
289	U	2016-09-23 15:38:23.800636+00	97	224	2	99	3	2308	DELIVERED		\t22364,22362,22341	741389.00	4	\N	\N	\N	\N	\N	2016-09-23 13:40:54.867869+00
290	I	2016-09-26 04:13:09.661224+00	465	225		99	3	2309	CREATED			0.00	8	\N	\N	\N	\N	\N	2016-09-26 04:13:09.661224+00
291	I	2016-09-26 04:13:47.568497+00	465	226		99	3	2310	CREATED			0.00	8	\N	\N	\N	\N	\N	2016-09-26 04:13:47.568497+00
292	I	2016-09-26 04:29:01.193509+00	465	227		99	3	2311	CREATED			0.00	8	\N	\N	\N	\N	\N	2016-09-26 04:29:01.193509+00
293	D	2016-09-26 04:29:07.967462+00	465	225		99	3	2309	CREATED			0.00	8	\N	\N	\N	\N	\N	2016-09-26 04:13:09.661224+00
294	D	2016-09-26 04:29:10.324769+00	465	226		99	3	2310	CREATED			0.00	8	\N	\N	\N	\N	\N	2016-09-26 04:13:47.568497+00
297	I	2016-09-26 06:29:38.085527+00	68	230	СПб-014990 	80	6	2305	CREATED		1263	621480.00	3	\N	\N	\N	\N	\N	2016-09-26 06:29:38.085527+00
298	I	2016-09-26 06:55:14.473361+00	12	231		0	6	2312	CREATED			0.00	8	\N	\N	\N	\N	\N	2016-09-26 06:55:14.473361+00
299	I	2016-09-26 06:56:17.037864+00	12	232		0	4	2313	CREATED			0.00	15	\N	\N	\N	\N	\N	2016-09-26 06:56:17.037864+00
300	I	2016-09-26 06:57:08.603208+00	12	233		0	3	2314	CREATED			0.00	12	\N	\N	\N	\N	\N	2016-09-26 06:57:08.603208+00
301	D	2016-09-26 06:58:48.815546+00	69	218	Ек-5155786	62	3	2303	CREATED		2134	64759.12	2	\N	\N	\N	\N	\N	2016-09-23 08:56:52.694308+00
302	D	2016-09-26 06:58:48.815546+00	69	219	Ек-5155793	87	3	2303	CREATED		2133	118273.64	2	\N	\N	\N	\N	\N	2016-09-23 08:56:52.694308+00
303	D	2016-09-26 06:58:48.815546+00	69	220	Ек-5155787	42	3	2303	CREATED		2132	42438.96	1	\N	\N	\N	\N	\N	2016-09-23 08:56:52.694308+00
304	I	2016-09-26 07:03:15.729337+00	69	234	Ек-5155786	62	3	2315	CREATED		2134	64759.12	2	\N	\N	\N	\N	\N	2016-09-26 07:03:15.729337+00
305	I	2016-09-26 07:03:15.729337+00	69	235	Ек-5155787	42	3	2315	CREATED		2132	42438.96	1	\N	\N	\N	\N	\N	2016-09-26 07:03:15.729337+00
306	I	2016-09-26 07:03:15.729337+00	69	236	Ек-5155793	87	3	2315	CREATED		2133	118273.64	2	\N	\N	\N	\N	\N	2016-09-26 07:03:15.729337+00
307	I	2016-09-26 07:13:44.951603+00	131	237		0	3	2284	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-26 07:13:44.951603+00
308	U	2016-09-26 08:01:14.214325+00	97	230	СПб-014990 	80	6	2305	DELIVERED		1263	621480.00	3	\N	\N	\N	\N	\N	2016-09-26 06:29:38.085527+00
309	U	2016-09-26 09:04:41.086333+00	97	237		0	3	2284	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-09-26 07:13:44.951603+00
310	U	2016-09-26 09:19:58.358506+00	32	231		0	6	2312	ARRIVED			0.00	8	\N	\N	\N	\N	\N	2016-09-26 06:55:14.473361+00
311	U	2016-09-26 09:48:08.837259+00	97	231		0	6	2312	DELIVERED			0.00	8	\N	\N	\N	\N	\N	2016-09-26 06:55:14.473361+00
312	I	2016-09-26 11:14:55.158876+00	96	238		0	3	2317	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-26 11:14:55.158876+00
313	I	2016-09-26 11:25:35.681223+00	96	239	Ек-5156274	0	3	2317	CREATED			0.00	2	\N	\N	\N	\N	\N	2016-09-26 11:25:35.681223+00
314	I	2016-09-26 11:25:35.681223+00	96	240	Ек-5156262	0	3	2317	CREATED			0.00	4	\N	\N	\N	\N	\N	2016-09-26 11:25:35.681223+00
315	I	2016-09-26 11:25:35.681223+00	96	241	Ек-5156264	0	3	2317	CREATED			0.00	2	\N	\N	\N	\N	\N	2016-09-26 11:25:35.681223+00
316	I	2016-09-26 11:25:35.681223+00	96	242	Ек-5156276	0	3	2317	CREATED			0.00	2	\N	\N	\N	\N	\N	2016-09-26 11:25:35.681223+00
317	I	2016-09-26 11:25:35.681223+00	96	243	Ек- 5156268	0	3	2317	CREATED			0.00	2	\N	\N	\N	\N	\N	2016-09-26 11:25:35.681223+00
318	U	2016-09-26 11:25:35.681223+00	96	238	Ек-5156259	0	3	2317	CREATED			0.00	6	\N	\N	\N	\N	\N	2016-09-26 11:14:55.158876+00
319	I	2016-09-26 12:18:14.027133+00	67	244		0	3	2318	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-26 12:18:14.027133+00
320	I	2016-09-26 12:18:50.730178+00	67	245		0	3	2318	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-26 12:18:50.730178+00
321	I	2016-09-26 12:18:50.730178+00	67	246		0	3	2318	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-26 12:18:50.730178+00
322	U	2016-09-26 12:21:19.981019+00	67	244		27	3	2318	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-26 12:18:14.027133+00
323	D	2016-09-26 12:21:19.981019+00	67	245		0	3	2318	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-26 12:18:50.730178+00
324	D	2016-09-26 12:21:19.981019+00	67	246		0	3	2318	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-26 12:18:50.730178+00
325	U	2016-09-27 05:42:57.969668+00	97	232		0	4	2313	DELIVERED			0.00	15	\N	\N	\N	\N	\N	2016-09-26 06:56:17.037864+00
326	U	2016-09-27 05:43:05.119653+00	97	233		0	3	2314	DELIVERED			0.00	12	\N	\N	\N	\N	\N	2016-09-26 06:57:08.603208+00
327	U	2016-09-27 05:43:17.022136+00	97	234	Ек-5155786	62	3	2315	DELIVERED		2134	64759.12	2	\N	\N	\N	\N	\N	2016-09-26 07:03:15.729337+00
328	U	2016-09-27 05:43:17.022136+00	97	235	Ек-5155787	42	3	2315	DELIVERED		2132	42438.96	1	\N	\N	\N	\N	\N	2016-09-26 07:03:15.729337+00
329	U	2016-09-27 05:43:17.022136+00	97	236	Ек-5155793	87	3	2315	DELIVERED		2133	118273.64	2	\N	\N	\N	\N	\N	2016-09-26 07:03:15.729337+00
330	U	2016-09-27 05:43:27.158262+00	97	227		99	3	2311	DELIVERED			0.00	8	\N	\N	\N	\N	\N	2016-09-26 04:29:01.193509+00
331	U	2016-09-27 05:43:36.179301+00	97	221		0	3	2304	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-09-23 10:04:23.505423+00
332	I	2016-09-27 07:29:25.139097+00	126	247		0	3	2319	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-27 07:29:25.139097+00
333	I	2016-09-27 12:38:14.327517+00	65	248		0	3	2320	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-27 12:38:14.327517+00
334	I	2016-09-27 13:01:54.678721+00	560	253		0	3	2323	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-27 13:01:54.678721+00
335	D	2016-09-27 13:02:03.088097+00	560	253		0	3	2323	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-27 13:01:54.678721+00
340	I	2016-09-27 13:33:18.193532+00	552	258		0	3	2325	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-27 13:33:18.193532+00
341	I	2016-09-27 13:33:18.193532+00	552	259		0	3	2325	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-27 13:33:18.193532+00
342	I	2016-09-27 13:33:18.193532+00	552	260		0	3	2325	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-27 13:33:18.193532+00
343	D	2016-09-27 13:33:31.831703+00	552	258		0	3	2325	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-27 13:33:18.193532+00
344	D	2016-09-27 13:33:31.831703+00	552	259		0	3	2325	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-27 13:33:18.193532+00
345	D	2016-09-27 13:33:31.831703+00	552	260		0	3	2325	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-27 13:33:18.193532+00
346	I	2016-09-27 13:37:32.211334+00	552	261	Ек-5156213	22	3	2325	CREATED		1124	25573.68	1	\N	\N	\N	\N	\N	2016-09-27 13:37:32.211334+00
347	I	2016-09-27 13:37:32.211334+00	552	262	Ек-5156332	29	4	2325	CREATED		1127	112274.88	1	\N	\N	\N	\N	\N	2016-09-27 13:37:32.211334+00
348	I	2016-09-27 13:37:32.211334+00	552	263	Ек-5156220	2	4	2325	CREATED		1125	16931.31	1	\N	\N	\N	\N	\N	2016-09-27 13:37:32.211334+00
349	I	2016-09-27 13:37:32.211334+00	552	264	 Ек-5156232	18	7	2325	CREATED		1126	17645.25	1	\N	\N	\N	\N	\N	2016-09-27 13:37:32.211334+00
350	I	2016-09-28 08:32:33.271092+00	126	266	ЕК-5156444	0	3	2319	CREATED		10687	30769.00	1	\N	\N	\N	\N	\N	2016-09-28 08:32:33.271092+00
351	I	2016-09-28 08:32:33.271092+00	126	267	ЕК-5156443	0	3	2319	CREATED		10686	64933.00	1	\N	\N	\N	\N	\N	2016-09-28 08:32:33.271092+00
352	D	2016-09-28 08:32:33.271092+00	126	247		0	3	2319	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-27 07:29:25.139097+00
353	D	2016-09-28 09:03:30.059989+00	552	261	Ек-5156213	22	3	2325	CREATED		1124	25573.68	1	\N	\N	\N	\N	\N	2016-09-27 13:37:32.211334+00
354	D	2016-09-28 09:03:30.059989+00	552	262	Ек-5156332	29	4	2325	CREATED		1127	112274.88	1	\N	\N	\N	\N	\N	2016-09-27 13:37:32.211334+00
355	D	2016-09-28 09:03:30.059989+00	552	263	Ек-5156220	2	4	2325	CREATED		1125	16931.31	1	\N	\N	\N	\N	\N	2016-09-27 13:37:32.211334+00
356	D	2016-09-28 09:03:30.059989+00	552	264	 Ек-5156232	18	7	2325	CREATED		1126	17645.25	1	\N	\N	\N	\N	\N	2016-09-27 13:37:32.211334+00
357	I	2016-09-28 09:07:18.79933+00	53	268	1	36	3	2327	CREATED		22730,22731,22732	238711.00	4	\N	\N	\N	\N	\N	2016-09-28 09:07:18.79933+00
358	I	2016-09-28 09:08:57.921239+00	53	269	1	36	3	2328	CREATED		22730,22731,22732	238711.00	4	\N	\N	\N	\N	\N	2016-09-28 09:08:57.921239+00
359	I	2016-09-28 09:13:11.409079+00	552	270	Ек-5156213	22	3	2329	CREATED		1124	25573.68	1	\N	\N	\N	\N	\N	2016-09-28 09:13:11.409079+00
360	I	2016-09-28 09:13:11.409079+00	552	271	 Ек-5156332	29	4	2329	CREATED		1127	112274.88	1	\N	\N	\N	\N	\N	2016-09-28 09:13:11.409079+00
361	I	2016-09-28 09:13:11.409079+00	552	272	Ек-5156220	2	4	2329	CREATED		1125	16931.31	1	\N	\N	\N	\N	\N	2016-09-28 09:13:11.409079+00
362	I	2016-09-28 09:13:11.409079+00	552	273	Ек-5156232	18	7	2329	CREATED		1126	17645.25	1	\N	\N	\N	\N	\N	2016-09-28 09:13:11.409079+00
363	U	2016-09-28 09:23:09.978262+00	30	132	№316	0	7	1056	ARRIVED			0.00	0	\N	\N	\N	\N	\N	2016-09-09 13:36:52.185657+00
364	U	2016-09-28 10:41:52.038216+00	97	243	Ек- 5156268	0	3	2317	DELIVERED			0.00	2	\N	\N	\N	\N	\N	2016-09-26 11:25:35.681223+00
365	U	2016-09-28 10:41:52.038216+00	97	238	Ек-5156259	0	3	2317	DELIVERED			0.00	6	\N	\N	\N	\N	\N	2016-09-26 11:14:55.158876+00
366	U	2016-09-28 10:41:52.038216+00	97	241	Ек-5156264	0	3	2317	DELIVERED			0.00	2	\N	\N	\N	\N	\N	2016-09-26 11:25:35.681223+00
367	U	2016-09-28 10:41:52.038216+00	97	239	Ек-5156274	0	3	2317	DELIVERED			0.00	2	\N	\N	\N	\N	\N	2016-09-26 11:25:35.681223+00
368	U	2016-09-28 10:41:52.038216+00	97	240	Ек-5156262	0	3	2317	DELIVERED			0.00	4	\N	\N	\N	\N	\N	2016-09-26 11:25:35.681223+00
369	U	2016-09-28 10:41:52.038216+00	97	242	Ек-5156276	0	3	2317	DELIVERED			0.00	2	\N	\N	\N	\N	\N	2016-09-26 11:25:35.681223+00
370	U	2016-09-28 10:42:10.247124+00	97	269	1	36	3	2328	DELIVERED		22730,22731,22732	238711.00	4	\N	\N	\N	\N	\N	2016-09-28 09:08:57.921239+00
371	I	2016-09-28 11:17:47.80888+00	57	274	Ек-5156554 	99	5	2330	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-28 11:17:47.80888+00
372	I	2016-09-28 11:17:47.80888+00	57	275	Ек-5156609	99	3	2330	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-28 11:17:47.80888+00
373	I	2016-09-28 11:17:47.80888+00	57	276	 Ек-5156557	99	6	2330	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-28 11:17:47.80888+00
374	I	2016-09-28 11:17:47.80888+00	57	277	Ек-5156602	99	7	2330	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-28 11:17:47.80888+00
375	I	2016-09-28 11:17:47.80888+00	57	278	Ек-5156566	99	4	2330	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-28 11:17:47.80888+00
376	U	2016-09-28 12:24:40.979206+00	97	244		27	3	2318	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-09-26 12:18:14.027133+00
377	I	2016-09-28 12:29:47.480599+00	549	279		6	5	2331	CREATED		870	0.00	1	\N	\N	\N	\N	\N	2016-09-28 12:29:47.480599+00
378	I	2016-09-28 12:29:47.480599+00	549	280		10	6	2331	CREATED		868	0.00	1	\N	\N	\N	\N	\N	2016-09-28 12:29:47.480599+00
379	I	2016-09-28 12:29:47.480599+00	549	281		6	3	2331	CREATED		871	0.00	1	\N	\N	\N	\N	\N	2016-09-28 12:29:47.480599+00
380	I	2016-09-28 12:29:47.480599+00	549	282		5	6	2331	CREATED		869	0.00	1	\N	\N	\N	\N	\N	2016-09-28 12:29:47.480599+00
381	I	2016-09-29 04:53:04.170134+00	503	283	Ек-5156479	10	3	2333	CREATED		816	136439.00	0	\N	\N	\N	\N	\N	2016-09-29 04:53:04.170134+00
382	I	2016-09-29 05:18:40.935085+00	538	284		0	3	2334	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-29 05:18:40.935085+00
383	I	2016-09-29 05:19:48.457099+00	538	285		0	3	2335	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-29 05:19:48.457099+00
384	D	2016-09-29 06:28:52.988557+00	538	285		0	3	2335	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-29 05:19:48.457099+00
385	I	2016-09-29 07:55:16.475475+00	128	286		0	3	2336	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-29 07:55:16.475475+00
387	I	2016-09-29 11:40:41.86842+00	538	292		0	3	2342	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-29 11:40:41.86842+00
388	D	2016-09-29 11:49:07.777293+00	538	292		0	3	2342	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-29 11:40:41.86842+00
389	I	2016-09-29 12:06:34.611691+00	69	293	Ек-5156857	21	3	2343	CREATED		2212	21986.64	1	\N	\N	\N	\N	\N	2016-09-29 12:06:34.611691+00
390	I	2016-09-29 12:06:34.611691+00	69	294	Ек-5156859	94	3	2343	CREATED		2214	116842.08	2	\N	\N	\N	\N	\N	2016-09-29 12:06:34.611691+00
391	I	2016-09-29 12:06:34.611691+00	69	295	Ек-5156855	56	3	2343	CREATED		2213	58786.68	2	\N	\N	\N	\N	\N	2016-09-29 12:06:34.611691+00
392	U	2016-09-29 12:12:59.372141+00	97	275	Ек-5156609	99	3	2330	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-09-28 11:17:47.80888+00
393	U	2016-09-29 12:12:59.372141+00	97	276	 Ек-5156557	99	6	2330	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-09-28 11:17:47.80888+00
394	U	2016-09-29 12:12:59.372141+00	97	278	Ек-5156566	99	4	2330	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-09-28 11:17:47.80888+00
395	U	2016-09-29 12:12:59.372141+00	97	274	Ек-5156554 	99	5	2330	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-09-28 11:17:47.80888+00
396	U	2016-09-29 12:12:59.372141+00	97	277	Ек-5156602	99	7	2330	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-09-28 11:17:47.80888+00
397	U	2016-09-29 12:35:05.427768+00	97	267	ЕК-5156443	0	3	2319	DELIVERED		10686	64933.00	1	\N	\N	\N	\N	\N	2016-09-28 08:32:33.271092+00
398	U	2016-09-29 12:35:05.427768+00	97	266	ЕК-5156444	0	3	2319	DELIVERED		10687	30769.00	1	\N	\N	\N	\N	\N	2016-09-28 08:32:33.271092+00
399	U	2016-09-29 12:35:36.791922+00	97	281		6	3	2331	DELIVERED		871	0.00	1	\N	\N	\N	\N	\N	2016-09-28 12:29:47.480599+00
400	U	2016-09-29 12:35:36.791922+00	97	282		5	6	2331	DELIVERED		869	0.00	1	\N	\N	\N	\N	\N	2016-09-28 12:29:47.480599+00
401	U	2016-09-29 12:35:36.791922+00	97	279		6	5	2331	DELIVERED		870	0.00	1	\N	\N	\N	\N	\N	2016-09-28 12:29:47.480599+00
402	U	2016-09-29 12:35:36.791922+00	97	280		10	6	2331	DELIVERED		868	0.00	1	\N	\N	\N	\N	\N	2016-09-28 12:29:47.480599+00
403	U	2016-09-30 07:29:31.763249+00	97	270	Ек-5156213	22	3	2329	DELIVERED		1124	25573.68	1	\N	\N	\N	\N	\N	2016-09-28 09:13:11.409079+00
404	U	2016-09-30 07:29:31.763249+00	97	271	 Ек-5156332	29	4	2329	DELIVERED		1127	112274.88	1	\N	\N	\N	\N	\N	2016-09-28 09:13:11.409079+00
405	U	2016-09-30 07:29:31.763249+00	97	273	Ек-5156232	18	7	2329	DELIVERED		1126	17645.25	1	\N	\N	\N	\N	\N	2016-09-28 09:13:11.409079+00
406	U	2016-09-30 07:29:31.763249+00	97	272	Ек-5156220	2	4	2329	DELIVERED		1125	16931.31	1	\N	\N	\N	\N	\N	2016-09-28 09:13:11.409079+00
407	I	2016-09-30 07:41:13.367519+00	53	296	1	50	3	2344	CREATED		23027	317364.00	3	\N	\N	\N	\N	\N	2016-09-30 07:41:13.367519+00
408	I	2016-09-30 09:47:56.411152+00	496	299		0	3	2347	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-30 09:47:56.411152+00
409	I	2016-09-30 09:47:56.411152+00	496	300		0	3	2347	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-30 09:47:56.411152+00
410	I	2016-09-30 10:23:27.405118+00	12	301		0	4	2348	CREATED			0.00	15	\N	\N	\N	\N	\N	2016-09-30 10:23:27.405118+00
411	I	2016-09-30 10:26:07.977302+00	12	302		0	3	2349	CREATED			0.00	12	\N	\N	\N	\N	\N	2016-09-30 10:26:07.977302+00
412	U	2016-09-30 10:33:10.939918+00	97	295	Ек-5156855	56	3	2343	ERROR	46 м. Ульяновск 	2213	58786.68	2	\N	\N	\N	\N	\N	2016-09-29 12:06:34.611691+00
413	U	2016-09-30 10:33:10.939918+00	97	293	Ек-5156857	21	3	2343	ERROR	Альфа 	2212	21986.64	1	\N	\N	\N	\N	\N	2016-09-29 12:06:34.611691+00
414	U	2016-09-30 10:33:10.939918+00	97	294	Ек-5156859	94	3	2343	DELIVERED		2214	116842.08	2	\N	\N	\N	\N	\N	2016-09-29 12:06:34.611691+00
415	U	2016-09-30 14:50:19.828185+00	97	284		0	3	2334	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-09-29 05:18:40.935085+00
416	I	2016-10-03 04:43:45.956831+00	465	308		39	3	2352	CREATED			0.00	5	\N	\N	\N	\N	\N	2016-10-03 04:43:45.956831+00
417	U	2016-10-03 05:27:13.844128+00	97	296	1	50	3	2344	DELIVERED		23027	317364.00	3	\N	\N	\N	\N	\N	2016-09-30 07:41:13.367519+00
418	I	2016-10-03 06:26:39.367385+00	68	309	СПб-015194 	69	6	2345	CREATED		1311	480958.00	2	\N	\N	\N	\N	\N	2016-10-03 06:26:39.367385+00
419	I	2016-10-03 06:26:39.367385+00	68	310	СПб-015191	0	5	2345	CREATED		1310	110244.00	2	\N	\N	\N	\N	\N	2016-10-03 06:26:39.367385+00
420	I	2016-10-03 06:26:39.379223+00	68	311	СПб-015191	0	5	2345	CREATED		1310	110244.00	2	\N	\N	\N	\N	\N	2016-10-03 06:26:39.379223+00
421	I	2016-10-03 06:26:39.379223+00	68	312	СПб-015194 	69	6	2345	CREATED		1311	480958.00	2	\N	\N	\N	\N	\N	2016-10-03 06:26:39.379223+00
422	U	2016-10-03 07:03:42.283024+00	68	310	СПб-015191	16	5	2345	CREATED		1310	110244.00	1	\N	\N	\N	\N	\N	2016-10-03 06:26:39.367385+00
423	D	2016-10-03 07:03:42.283024+00	68	311	СПб-015191	0	5	2345	CREATED		1310	110244.00	2	\N	\N	\N	\N	\N	2016-10-03 06:26:39.379223+00
424	D	2016-10-03 07:03:42.283024+00	68	312	СПб-015194 	69	6	2345	CREATED		1311	480958.00	2	\N	\N	\N	\N	\N	2016-10-03 06:26:39.379223+00
429	I	2016-10-03 07:29:40.937474+00	81	317	Ек-5157074	1	3	2353	CREATED	Центральный склад СПб	2688	3993.95	1	\N	\N	\N	\N	\N	2016-10-03 07:29:40.937474+00
430	I	2016-10-03 07:29:40.937474+00	81	318	Ек-5157073	4	3	2353	CREATED	Всеволожск 	2686	26786.20	1	\N	\N	\N	\N	\N	2016-10-03 07:29:40.937474+00
431	I	2016-10-03 07:29:40.937474+00	81	319	Ек-5157049	17	3	2353	CREATED	Москва	2687	91123.50	1	\N	\N	\N	\N	\N	2016-10-03 07:29:40.937474+00
432	I	2016-10-03 07:29:40.937474+00	81	320	Ек-5157072	10	3	2353	CREATED	Ульяновск 	2689	60607.70	1	\N	\N	\N	\N	\N	2016-10-03 07:29:40.937474+00
433	I	2016-10-03 09:02:49.042649+00	553	321		0	3	2355	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-03 09:02:49.042649+00
434	U	2016-10-03 10:57:10.762668+00	97	310	СПб-015191	16	5	2345	DELIVERED		1310	110244.00	1	\N	\N	\N	\N	\N	2016-10-03 06:26:39.367385+00
435	U	2016-10-03 10:57:10.762668+00	97	309	СПб-015194 	69	6	2345	DELIVERED		1311	480958.00	2	\N	\N	\N	\N	\N	2016-10-03 06:26:39.367385+00
436	U	2016-10-03 10:57:21.196308+00	97	308		39	3	2352	DELIVERED			0.00	5	\N	\N	\N	\N	\N	2016-10-03 04:43:45.956831+00
437	U	2016-10-03 10:57:28.639093+00	97	301		0	4	2348	DELIVERED			0.00	15	\N	\N	\N	\N	\N	2016-09-30 10:23:27.405118+00
438	I	2016-10-04 07:53:34.244058+00	562	322	1	0	3	2356	CREATED		1756	0.00	0	\N	\N	\N	\N	\N	2016-10-04 07:53:34.244058+00
439	D	2016-10-04 07:54:07.187008+00	562	322	1	0	3	2356	CREATED		1756	0.00	0	\N	\N	\N	\N	\N	2016-10-04 07:53:34.244058+00
440	I	2016-10-04 08:05:06.651455+00	126	323		0	3	2357	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-04 08:05:06.651455+00
441	I	2016-10-04 08:05:43.824007+00	562	324	2	0	4	2358	CREATED			0.00	1	\N	\N	\N	\N	\N	2016-10-04 08:05:43.824007+00
442	I	2016-10-04 08:05:43.824007+00	562	325	1	0	3	2358	CREATED			0.00	4	\N	\N	\N	\N	\N	2016-10-04 08:05:43.824007+00
443	I	2016-10-04 08:05:43.824007+00	562	326	3	0	7	2358	CREATED			0.00	3	\N	\N	\N	\N	\N	2016-10-04 08:05:43.824007+00
444	D	2016-10-04 08:36:30.506921+00	496	299		0	3	2347	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-30 09:47:56.411152+00
445	D	2016-10-04 08:36:30.506921+00	496	300		0	3	2347	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-30 09:47:56.411152+00
446	I	2016-10-04 08:37:37.891055+00	496	327		0	3	2359	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-04 08:37:37.891055+00
447	I	2016-10-04 08:37:37.891055+00	496	328		0	3	2359	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-04 08:37:37.891055+00
448	I	2016-10-04 10:54:04.886233+00	68	329		0	3	2360	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-04 10:54:04.886233+00
449	I	2016-10-04 12:05:14.614108+00	553	330		0	3	2355	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-04 12:05:14.614108+00
450	U	2016-10-04 12:05:14.614108+00	553	321		5	3	2355	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-03 09:02:49.042649+00
451	U	2016-10-04 12:06:08.925024+00	553	330		4	7	2355	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-04 12:05:14.614108+00
452	U	2016-10-04 12:06:08.925024+00	553	321		1	3	2355	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-03 09:02:49.042649+00
453	I	2016-10-05 04:50:54.248083+00	70	331		19	7	2364	CREATED		4526	83323.32	1	\N	\N	\N	\N	\N	2016-10-05 04:50:54.248083+00
454	I	2016-10-05 04:50:54.248083+00	70	332		27	3	2364	CREATED		4527	122433.66	1	\N	\N	\N	\N	\N	2016-10-05 04:50:54.248083+00
455	I	2016-10-05 04:50:54.248083+00	70	333		7	4	2364	CREATED		4528	29075.52	0	\N	\N	\N	\N	\N	2016-10-05 04:50:54.248083+00
456	I	2016-10-05 05:02:54.507721+00	503	334	Ек-5157546	10	4	2365	CREATED		855	31121.00	0	\N	\N	\N	\N	\N	2016-10-05 05:02:54.507721+00
457	I	2016-10-05 05:10:32.406182+00	538	335		0	3	2366	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-05 05:10:32.406182+00
458	I	2016-10-05 05:30:56.045944+00	68	336	Ек-5157500	31	4	2362	CREATED		1327	220188.00	1	\N	\N	\N	\N	\N	2016-10-05 05:30:56.045944+00
459	I	2016-10-05 05:30:56.045944+00	68	337	Ек-5157503	35	7	2362	CREATED		1331	236652.00	1	\N	\N	\N	\N	\N	2016-10-05 05:30:56.045944+00
460	I	2016-10-05 05:30:56.045944+00	68	338	Ек-5157506	11	3	2362	CREATED		1326	836418.00	4	\N	\N	\N	\N	\N	2016-10-05 05:30:56.045944+00
461	I	2016-10-05 06:16:02.18637+00	503	339	Ек-5157535	10	3	2367	CREATED		935	80881.00	0	\N	\N	\N	\N	\N	2016-10-05 06:16:02.18637+00
462	U	2016-10-05 06:38:22.254249+00	97	302		0	3	2349	DELIVERED			0.00	12	\N	\N	\N	\N	\N	2016-09-30 10:26:07.977302+00
463	U	2016-10-05 06:38:37.2965+00	97	320	Ек-5157072	10	3	2353	DELIVERED	Ульяновск 	2689	60607.70	1	\N	\N	\N	\N	\N	2016-10-03 07:29:40.937474+00
464	U	2016-10-05 06:38:37.2965+00	97	317	Ек-5157074	1	3	2353	DELIVERED	Центральный склад СПб	2688	3993.95	1	\N	\N	\N	\N	\N	2016-10-03 07:29:40.937474+00
465	U	2016-10-05 06:38:37.2965+00	97	319	Ек-5157049	17	3	2353	DELIVERED	Москва	2687	91123.50	1	\N	\N	\N	\N	\N	2016-10-03 07:29:40.937474+00
466	U	2016-10-05 06:38:37.2965+00	97	318	Ек-5157073	4	3	2353	DELIVERED	Всеволожск 	2686	26786.20	1	\N	\N	\N	\N	\N	2016-10-03 07:29:40.937474+00
467	U	2016-10-05 06:40:26.171013+00	97	283	Ек-5156479	10	3	2333	DELIVERED		816	136439.00	0	\N	\N	\N	\N	\N	2016-09-29 04:53:04.170134+00
468	I	2016-10-05 07:47:29.561929+00	53	340	1	0	3	2368	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-05 07:47:29.561929+00
469	U	2016-10-05 08:05:32.539297+00	53	340	1	64	3	2368	CREATED		23437,23438	312342.00	4	\N	\N	\N	\N	\N	2016-10-05 07:47:29.561929+00
470	U	2016-10-05 09:34:36.005953+00	97	336	Ек-5157500	31	4	2362	DELIVERED		1327	220188.00	1	\N	\N	\N	\N	\N	2016-10-05 05:30:56.045944+00
471	U	2016-10-05 09:34:36.005953+00	97	337	Ек-5157503	35	7	2362	DELIVERED		1331	236652.00	1	\N	\N	\N	\N	\N	2016-10-05 05:30:56.045944+00
472	U	2016-10-05 09:34:36.005953+00	97	338	Ек-5157506	11	3	2362	DELIVERED		1326	836418.00	4	\N	\N	\N	\N	\N	2016-10-05 05:30:56.045944+00
473	U	2016-10-05 09:34:58.594255+00	97	333		7	4	2364	DELIVERED		4528	29075.52	0	\N	\N	\N	\N	\N	2016-10-05 04:50:54.248083+00
474	U	2016-10-05 09:34:58.594255+00	97	331		19	7	2364	DELIVERED		4526	83323.32	1	\N	\N	\N	\N	\N	2016-10-05 04:50:54.248083+00
475	U	2016-10-05 09:34:58.594255+00	97	332		27	3	2364	DELIVERED		4527	122433.66	1	\N	\N	\N	\N	\N	2016-10-05 04:50:54.248083+00
476	U	2016-10-05 09:35:16.018687+00	97	340	1	64	3	2368	DELIVERED		23437,23438	312342.00	4	\N	\N	\N	\N	\N	2016-10-05 07:47:29.561929+00
477	U	2016-10-05 09:35:39.650984+00	97	321		1	3	2355	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-03 09:02:49.042649+00
478	U	2016-10-05 09:35:39.650984+00	97	330		4	7	2355	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-04 12:05:14.614108+00
479	I	2016-10-05 13:59:43.174061+00	126	341	Ек-5157474	10	3	2357	CREATED		11048	73976.00	1	\N	\N	\N	\N	\N	2016-10-05 13:59:43.174061+00
480	I	2016-10-05 13:59:43.174061+00	126	342	ЕК-5157471	8	3	2357	CREATED		11049	74543.00	1	\N	\N	\N	\N	\N	2016-10-05 13:59:43.174061+00
481	D	2016-10-05 13:59:43.174061+00	126	323		0	3	2357	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-04 08:05:06.651455+00
482	I	2016-10-06 05:10:01.929415+00	503	343	Ек-5156705	10	7	2369	CREATED		953	10022.00	0	\N	\N	\N	\N	\N	2016-10-06 05:10:01.929415+00
483	I	2016-10-06 05:49:02.256675+00	503	344	Ек-5157897	10	7	2369	CREATED			91673.00	0	\N	\N	\N	\N	\N	2016-10-06 05:49:02.256675+00
484	U	2016-10-06 05:49:02.256675+00	503	343	Ек-5156705	10	7	2369	CREATED		954	19596.00	0	\N	\N	\N	\N	\N	2016-10-06 05:10:01.929415+00
485	U	2016-10-06 05:50:19.837628+00	503	334	Ек-5157546	10	4	2365	CREATED		936	31121.00	0	\N	\N	\N	\N	\N	2016-10-05 05:02:54.507721+00
486	I	2016-10-06 09:26:14.501754+00	559	345		0	6	2370	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-06 09:26:14.501754+00
494	U	2016-10-06 12:29:20.008786+00	97	335		0	3	2366	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-05 05:10:32.406182+00
497	I	2016-10-06 12:43:49.743016+00	63	349		0	3	2372	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-06 12:43:49.743016+00
499	I	2016-10-06 12:52:00.739289+00	465	350		64	3	2373	CREATED			0.00	4	\N	\N	\N	\N	\N	2016-10-06 12:52:00.739289+00
500	I	2016-10-06 14:59:52.002893+00	549	351		17	6	2374	CREATED		909	0.00	1	\N	\N	\N	\N	\N	2016-10-06 14:59:52.002893+00
501	I	2016-10-06 14:59:52.002893+00	549	352		5	6	2374	CREATED		911	0.00	1	\N	\N	\N	\N	\N	2016-10-06 14:59:52.002893+00
502	I	2016-10-07 05:44:50.239552+00	12	353		0	7	2375	CREATED			0.00	15	\N	\N	\N	\N	\N	2016-10-07 05:44:50.239552+00
503	I	2016-10-07 07:23:14.031161+00	53	354	1	59	3	2376	CREATED		23725	290525.00	4	\N	\N	\N	\N	\N	2016-10-07 07:23:14.031161+00
504	U	2016-10-10 05:47:40.652491+00	97	350		64	3	2373	DELIVERED			0.00	4	\N	\N	\N	\N	\N	2016-10-06 12:52:00.739289+00
505	U	2016-10-10 05:48:03.467586+00	97	325	1	0	3	2358	DELIVERED		МCУТ021002	404964.00	3	\N	\N	\N	\N	\N	2016-10-04 08:05:43.824007+00
506	U	2016-10-10 05:48:03.467586+00	97	326	3	0	7	2358	DELIVERED			312339.00	2	\N	\N	\N	\N	\N	2016-10-04 08:05:43.824007+00
507	U	2016-10-10 05:48:03.467586+00	97	324	2	0	4	2358	DELIVERED		МCУТ021001	34002.00	1	\N	\N	\N	\N	\N	2016-10-04 08:05:43.824007+00
508	U	2016-10-10 05:48:03.467586+00	97	346	4	0	10	2358	DELIVERED		МCУТ021008	4214.00	1	\N	\N	\N	\N	\N	2016-10-06 10:04:04.010101+00
509	U	2016-10-10 05:48:03.467586+00	97	347	5	0	6	2358	DELIVERED		МCУТ021012	71340.00	1	\N	\N	\N	\N	\N	2016-10-06 10:04:04.010101+00
512	U	2016-10-10 05:48:24.521874+00	97	354	1	59	3	2376	DELIVERED		23725	290525.00	4	\N	\N	\N	\N	\N	2016-10-07 07:23:14.031161+00
514	I	2016-10-10 05:58:29.226278+00	68	355	СПб-015340 	37	10	2377	CREATED		1361	264972.00	1	\N	\N	\N	\N	\N	2016-10-10 05:58:29.226278+00
515	I	2016-10-10 05:58:29.226278+00	68	356	СПб-015341	1	10	2377	CREATED		1362	4856.00	0	\N	\N	\N	\N	\N	2016-10-10 05:58:29.226278+00
516	I	2016-10-10 08:12:12.54367+00	53	357	1	65	3	2379	CREATED		23848,23849	287148.00	4	\N	\N	\N	\N	\N	2016-10-10 08:12:12.54367+00
487	I	2016-10-06 10:04:04.010101+00	562	346	4	0	10	2358	CREATED		МCУТ021008	4214.00	1	\N	\N	\N	\N	\N	2016-10-06 10:04:04.010101+00
488	I	2016-10-06 10:04:04.010101+00	562	347	5	0	6	2358	CREATED		МCУТ021012	71340.00	1	\N	\N	\N	\N	\N	2016-10-06 10:04:04.010101+00
489	U	2016-10-06 10:04:04.010101+00	562	324	2	0	4	2358	CREATED		МCУТ021001	34002.00	1	\N	\N	\N	\N	\N	2016-10-04 08:05:43.824007+00
490	U	2016-10-06 10:04:04.010101+00	562	325	1	0	3	2358	CREATED		МCУТ021002	404964.00	3	\N	\N	\N	\N	\N	2016-10-04 08:05:43.824007+00
491	U	2016-10-06 10:04:04.010101+00	562	326	3	0	7	2358	CREATED			312339.00	2	\N	\N	\N	\N	\N	2016-10-04 08:05:43.824007+00
492	I	2016-10-06 12:19:44.578494+00	11	348		0	3	2371	CREATED	0	0	0.00	0	\N	\N	\N	\N	\N	2016-10-06 12:19:44.578494+00
493	D	2016-10-06 12:26:39.640548+00	11	348		0	3	2371	CREATED	0	0	0.00	0	\N	\N	\N	\N	\N	2016-10-06 12:19:44.578494+00
495	U	2016-10-06 12:29:40.828797+00	97	342	ЕК-5157471	8	3	2357	DELIVERED		11049	74543.00	1	\N	\N	\N	\N	\N	2016-10-05 13:59:43.174061+00
496	U	2016-10-06 12:29:40.828797+00	97	341	Ек-5157474	10	3	2357	DELIVERED		11048	73976.00	1	\N	\N	\N	\N	\N	2016-10-05 13:59:43.174061+00
498	D	2016-10-06 12:44:36.833267+00	63	349		0	3	2372	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-06 12:43:49.743016+00
510	U	2016-10-10 05:48:16.882278+00	97	351		17	6	2374	DELIVERED		909	0.00	1	\N	\N	\N	\N	\N	2016-10-06 14:59:52.002893+00
511	U	2016-10-10 05:48:16.882278+00	97	352		5	6	2374	DELIVERED		911	0.00	1	\N	\N	\N	\N	\N	2016-10-06 14:59:52.002893+00
513	U	2016-10-10 05:48:30.382739+00	97	353		0	7	2375	DELIVERED			0.00	15	\N	\N	\N	\N	\N	2016-10-07 05:44:50.239552+00
517	I	2016-10-10 08:34:57.87472+00	12	358		0	4	2380	CREATED			0.00	8	\N	\N	\N	\N	\N	2016-10-10 08:34:57.87472+00
518	I	2016-10-10 08:35:51.528603+00	12	359		0	4	2381	CREATED			0.00	15	\N	\N	\N	\N	\N	2016-10-10 08:35:51.528603+00
519	U	2016-10-10 09:15:08.985952+00	97	356	СПб-015341	1	10	2377	DELIVERED		1362	4856.00	0	\N	\N	\N	\N	\N	2016-10-10 05:58:29.226278+00
520	U	2016-10-10 09:15:08.985952+00	97	355	СПб-015340 	37	10	2377	DELIVERED		1361	264972.00	1	\N	\N	\N	\N	\N	2016-10-10 05:58:29.226278+00
521	U	2016-10-10 09:29:14.515157+00	97	357	1	65	3	2379	DELIVERED		23848,23849	287148.00	4	\N	\N	\N	\N	\N	2016-10-10 08:12:12.54367+00
522	U	2016-10-10 14:35:26.292134+00	97	359		0	4	2381	DELIVERED			0.00	15	\N	\N	\N	\N	\N	2016-10-10 08:35:51.528603+00
523	U	2016-10-11 04:19:27.656406+00	503	339	Ек-5157535	7	3	2367	CREATED		935	80881.00	0	\N	\N	\N	\N	\N	2016-10-05 06:16:02.18637+00
524	I	2016-10-11 06:23:54.674808+00	14	360		0	8	2382	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-11 06:23:54.674808+00
525	D	2016-10-11 06:24:14.617824+00	14	360		0	8	2382	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-11 06:23:54.674808+00
526	I	2016-10-11 07:43:51.485711+00	503	361	ЕКТ156	2	4	2383	CREATED		937	13000.00	0	\N	\N	\N	\N	\N	2016-10-11 07:43:51.485711+00
527	D	2016-10-12 01:37:33.820017+00	503	361	ЕКТ156	2	4	2383	CREATED		937	13000.00	0	\N	\N	\N	\N	\N	2016-10-11 07:43:51.485711+00
528	I	2016-10-12 03:38:13.572167+00	503	363	Ек-5158389	2	4	2386	CREATED		969	13000.00	0	\N	\N	\N	\N	\N	2016-10-12 03:38:13.572167+00
529	I	2016-10-12 03:38:13.572167+00	503	364	Ек-5158389	7	4	2386	CREATED		1010	39906.00	0	\N	\N	\N	\N	\N	2016-10-12 03:38:13.572167+00
530	I	2016-10-12 03:44:49.487765+00	503	365	СПб-015394	15	5	2387	CREATED		1004	85871.00	0	\N	\N	\N	\N	\N	2016-10-12 03:44:49.487765+00
531	U	2016-10-12 07:17:01.155055+00	503	343	Ек-5156705	1	7	2369	CREATED		954	19596.00	0	\N	\N	\N	\N	\N	2016-10-06 05:10:01.929415+00
532	U	2016-10-12 07:17:01.155055+00	503	344	Ек-5157897	13	7	2369	CREATED		953	91673.00	0	\N	\N	\N	\N	\N	2016-10-06 05:49:02.256675+00
533	U	2016-10-12 07:37:33.762704+00	97	339	Ек-5157535	7	3	2367	DELIVERED		935	80881.00	0	\N	\N	\N	\N	\N	2016-10-05 06:16:02.18637+00
534	I	2016-10-12 09:07:25.924292+00	538	366		0	3	2388	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-12 09:07:25.924292+00
535	I	2016-10-12 11:11:18.016314+00	496	367		0	3	2389	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-12 11:11:18.016314+00
536	I	2016-10-12 12:20:43.722847+00	128	368		0	3	2390	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-12 12:20:43.722847+00
537	I	2016-10-12 13:20:32.756345+00	12	369		0	7	2391	CREATED			0.00	12	\N	\N	\N	\N	\N	2016-10-12 13:20:32.756345+00
538	I	2016-10-12 13:21:36.296107+00	12	370		0	7	2392	CREATED			0.00	15	\N	\N	\N	\N	\N	2016-10-12 13:21:36.296107+00
539	D	2016-10-12 13:28:10.386341+00	12	369		0	7	2391	CREATED			0.00	12	\N	\N	\N	\N	\N	2016-10-12 13:20:32.756345+00
540	I	2016-10-12 13:28:55.135927+00	12	371		0	7	2393	CREATED			0.00	12	\N	\N	\N	\N	\N	2016-10-12 13:28:55.135927+00
541	I	2016-10-13 01:08:05.259237+00	503	372	Ек-5158386	28	3	2394	CREATED		1012	177972.00	0	\N	\N	\N	\N	\N	2016-10-13 01:08:05.259237+00
542	I	2016-10-13 01:08:05.259237+00	503	373	Ек-5158386	5	3	2394	CREATED		968	32500.00	0	\N	\N	\N	\N	\N	2016-10-13 01:08:05.259237+00
543	I	2016-10-13 05:19:55.423094+00	12	374		0	7	2395	CREATED			0.00	11	\N	\N	\N	\N	\N	2016-10-13 05:19:55.423094+00
544	D	2016-10-13 05:20:01.183045+00	12	371		0	7	2393	CREATED			0.00	12	\N	\N	\N	\N	\N	2016-10-12 13:28:55.135927+00
545	I	2016-10-13 06:48:10.67703+00	503	375	Ек-5158674	10	7	2396	CREATED		1037	178356.00	0	\N	\N	\N	\N	\N	2016-10-13 06:48:10.67703+00
546	I	2016-10-13 07:04:52.105851+00	562	376		0	3	2397	CREATED			0.00	2	\N	\N	\N	\N	\N	2016-10-13 07:04:52.105851+00
547	I	2016-10-13 07:04:52.105851+00	562	377		0	4	2397	CREATED			0.00	1	\N	\N	\N	\N	\N	2016-10-13 07:04:52.105851+00
548	I	2016-10-13 07:04:52.105851+00	562	378		0	7	2397	CREATED			0.00	2	\N	\N	\N	\N	\N	2016-10-13 07:04:52.105851+00
549	I	2016-10-13 07:39:43.182041+00	53	379	1	70	3	2398	CREATED		24248,24249,24250	489393.00	4	\N	\N	\N	\N	\N	2016-10-13 07:39:43.182041+00
550	I	2016-10-13 08:17:53.112906+00	12	380		0	7	2399	CREATED			0.00	8	\N	\N	\N	\N	\N	2016-10-13 08:17:53.112906+00
551	U	2016-10-13 10:41:37.223118+00	97	379	1	70	3	2398	DELIVERED		24248,24249,24250	489393.00	4	\N	\N	\N	\N	\N	2016-10-13 07:39:43.182041+00
552	U	2016-10-13 10:41:48.576727+00	97	374		0	7	2395	DELIVERED			0.00	11	\N	\N	\N	\N	\N	2016-10-13 05:19:55.423094+00
553	I	2016-10-13 11:43:21.560235+00	549	381		30	5	2400	CREATED		936	0.00	1	\N	\N	\N	\N	\N	2016-10-13 11:43:21.560235+00
554	I	2016-10-13 11:43:21.560235+00	549	382		12	6	2400	CREATED		939	0.00	1	\N	\N	\N	\N	\N	2016-10-13 11:43:21.560235+00
555	I	2016-10-13 11:43:21.560235+00	549	383		57	6	2400	CREATED		938	0.00	2	\N	\N	\N	\N	\N	2016-10-13 11:43:21.560235+00
556	I	2016-10-13 11:43:21.560235+00	549	384		48	5	2400	CREATED		937	0.00	1	\N	\N	\N	\N	\N	2016-10-13 11:43:21.560235+00
557	U	2016-10-13 12:26:43.224611+00	97	370		0	7	2392	DELIVERED			0.00	15	\N	\N	\N	\N	\N	2016-10-12 13:21:36.296107+00
558	U	2016-10-13 12:26:48.866479+00	97	380		0	7	2399	DELIVERED			0.00	8	\N	\N	\N	\N	\N	2016-10-13 08:17:53.112906+00
559	U	2016-10-13 12:27:55.222317+00	97	368		0	3	2390	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-12 12:20:43.722847+00
560	U	2016-10-13 12:28:03.347311+00	97	366		0	3	2388	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-12 09:07:25.924292+00
561	I	2016-10-13 14:21:11.472397+00	562	385		0	10	2397	CREATED			0.00	1	\N	\N	\N	\N	\N	2016-10-13 14:21:11.472397+00
562	U	2016-10-13 14:21:11.472397+00	562	376		0	3	2397	CREATED			0.00	1	\N	\N	\N	\N	\N	2016-10-13 07:04:52.105851+00
563	U	2016-10-13 14:21:11.472397+00	562	378		0	7	2397	CREATED			0.00	1	\N	\N	\N	\N	\N	2016-10-13 07:04:52.105851+00
564	I	2016-10-14 06:12:24.849601+00	57	399	Ек-5158547	0	7	2403	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-14 06:12:24.849601+00
565	I	2016-10-14 06:12:24.849601+00	57	400	 Ек-5158562 	0	10	2403	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-14 06:12:24.849601+00
566	I	2016-10-14 06:12:24.849601+00	57	401	Ек-5158522 	0	4	2403	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-14 06:12:24.849601+00
567	I	2016-10-14 06:12:24.849601+00	57	402	Ек-5158568	0	5	2403	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-14 06:12:24.849601+00
568	I	2016-10-14 06:12:24.849601+00	57	403	Ек-5158578	0	3	2403	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-14 06:12:24.849601+00
569	I	2016-10-14 06:26:42.904606+00	126	404	ЕК-5158526	0	3	2404	CREATED		11444	27697.00	1	\N	\N	\N	\N	\N	2016-10-14 06:26:42.904606+00
570	I	2016-10-14 06:26:42.904606+00	126	405	ЕК-5158525	0	3	2404	CREATED		11445	70541.00	1	\N	\N	\N	\N	\N	2016-10-14 06:26:42.904606+00
571	I	2016-10-14 06:26:42.904606+00	126	406		0	3	2404	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-14 06:26:42.904606+00
573	I	2016-10-14 09:15:30.33591+00	70	408		0	7	2406	CREATED		4729	51712.34	0	\N	\N	\N	\N	\N	2016-10-14 09:15:30.33591+00
574	I	2016-10-14 09:15:30.33591+00	70	409		0	4	2406	CREATED		4731	23406.96	0	\N	\N	\N	\N	\N	2016-10-14 09:15:30.33591+00
575	I	2016-10-14 09:15:30.33591+00	70	410		0	3	2406	CREATED		4730	67878.12	0	\N	\N	\N	\N	\N	2016-10-14 09:15:30.33591+00
578	D	2016-10-14 10:28:43.021561+00	538	411		0	3	2407	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-14 09:57:03.816674+00
579	I	2016-10-14 10:42:00.497403+00	69	413	Ек-5158814	0	3	2410	CREATED	количество коробок 129	2343	149318.52	3	\N	\N	\N	\N	\N	2016-10-14 10:42:00.497403+00
580	I	2016-10-14 10:42:00.497403+00	69	414	Ек-5158798	67	3	2410	CREATED		2342	69609.76	2	\N	\N	\N	\N	\N	2016-10-14 10:42:00.497403+00
586	U	2016-10-14 10:45:55.875375+00	97	406		0	3	2404	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-14 06:26:42.904606+00
587	U	2016-10-14 10:45:55.875375+00	97	405	ЕК-5158525	0	3	2404	DELIVERED		11445	70541.00	1	\N	\N	\N	\N	\N	2016-10-14 06:26:42.904606+00
588	U	2016-10-14 10:45:55.875375+00	97	404	ЕК-5158526	0	3	2404	DELIVERED		11444	27697.00	1	\N	\N	\N	\N	\N	2016-10-14 06:26:42.904606+00
589	U	2016-10-14 10:46:06.441869+00	97	407	1	31	3	2405	DELIVERED		24408	126807.00	2	\N	\N	\N	\N	\N	2016-10-14 07:56:03.679406+00
598	D	2016-10-17 03:37:17.757322+00	503	334	Ек-5157546	10	4	2365	CREATED		936	31121.00	0	\N	\N	\N	\N	\N	2016-10-05 05:02:54.507721+00
600	I	2016-10-17 07:09:12.048324+00	68	416	 Ек-5159038	32	4	2411	CREATED		1413	234936.00	1	\N	\N	\N	\N	\N	2016-10-17 07:09:12.048324+00
601	I	2016-10-17 07:09:12.048324+00	68	417	СПб-015564	25	5	2411	CREATED		1416	207216.00	1	\N	\N	\N	\N	\N	2016-10-17 07:09:12.048324+00
602	I	2016-10-17 07:09:12.048324+00	68	418	Ек-5159061	34	3	2411	CREATED		1414	340695.00	2	\N	\N	\N	\N	\N	2016-10-17 07:09:12.048324+00
603	I	2016-10-17 07:09:12.048324+00	68	419	СПб-015567	18	10	2411	CREATED		1415	111000.00	1	\N	\N	\N	\N	\N	2016-10-17 07:09:12.048324+00
604	I	2016-10-17 07:09:12.048324+00	68	420	СПб-015575	2	6	2411	CREATED		1417	9328.00	0	\N	\N	\N	\N	\N	2016-10-17 07:09:12.048324+00
605	I	2016-10-17 07:09:12.048324+00	68	421	Ек-5159043	28	7	2411	CREATED		1412;491	190440.00	1	\N	\N	\N	\N	\N	2016-10-17 07:09:12.048324+00
606	I	2016-10-17 07:39:30.211302+00	12	422		0	3	2413	CREATED			0.00	8	\N	\N	\N	\N	\N	2016-10-17 07:39:30.211302+00
607	I	2016-10-17 08:54:46.489185+00	53	423	1	95	3	2414	CREATED		39915	534690.00	4	\N	\N	\N	\N	\N	2016-10-17 08:54:46.489185+00
612	U	2016-10-17 15:07:40.481553+00	97	409		0	4	2406	DELIVERED		4731	23406.96	0	\N	\N	\N	\N	\N	2016-10-14 09:15:30.33591+00
613	U	2016-10-17 15:07:40.481553+00	97	410		0	3	2406	DELIVERED		4730	67878.12	0	\N	\N	\N	\N	\N	2016-10-14 09:15:30.33591+00
614	U	2016-10-17 15:07:40.481553+00	97	408		0	7	2406	DELIVERED		4729	51712.34	0	\N	\N	\N	\N	\N	2016-10-14 09:15:30.33591+00
572	I	2016-10-14 07:56:03.679406+00	53	407	1	31	3	2405	CREATED		24408	126807.00	2	\N	\N	\N	\N	\N	2016-10-14 07:56:03.679406+00
576	I	2016-10-14 09:57:03.816674+00	538	411		0	3	2407	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-14 09:57:03.816674+00
577	I	2016-10-14 09:57:15.952322+00	538	412		0	3	2408	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-14 09:57:15.952322+00
581	U	2016-10-14 10:45:44.864776+00	97	402	Ек-5158568	0	5	2403	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-14 06:12:24.849601+00
582	U	2016-10-14 10:45:44.864776+00	97	399	Ек-5158547	0	7	2403	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-14 06:12:24.849601+00
583	U	2016-10-14 10:45:44.864776+00	97	400	 Ек-5158562 	0	10	2403	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-14 06:12:24.849601+00
584	U	2016-10-14 10:45:44.864776+00	97	401	Ек-5158522 	0	4	2403	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-14 06:12:24.849601+00
585	U	2016-10-14 10:45:44.864776+00	97	403	Ек-5158578	0	3	2403	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-14 06:12:24.849601+00
590	U	2016-10-14 10:46:18.416766+00	97	376		0	3	2397	DELIVERED			0.00	1	\N	\N	\N	\N	\N	2016-10-13 07:04:52.105851+00
591	U	2016-10-14 10:46:18.416766+00	97	377		0	4	2397	DELIVERED			0.00	1	\N	\N	\N	\N	\N	2016-10-13 07:04:52.105851+00
592	U	2016-10-14 10:46:18.416766+00	97	385		0	10	2397	DELIVERED			0.00	1	\N	\N	\N	\N	\N	2016-10-13 14:21:11.472397+00
593	U	2016-10-14 10:46:18.416766+00	97	378		0	7	2397	DELIVERED			0.00	1	\N	\N	\N	\N	\N	2016-10-13 07:04:52.105851+00
594	U	2016-10-14 14:44:40.420399+00	97	382		12	6	2400	DELIVERED		939	0.00	1	\N	\N	\N	\N	\N	2016-10-13 11:43:21.560235+00
595	U	2016-10-14 14:44:40.420399+00	97	384		48	5	2400	DELIVERED		937	0.00	1	\N	\N	\N	\N	\N	2016-10-13 11:43:21.560235+00
596	U	2016-10-14 14:44:40.420399+00	97	383		57	6	2400	DELIVERED		938	0.00	2	\N	\N	\N	\N	\N	2016-10-13 11:43:21.560235+00
597	U	2016-10-14 14:44:40.420399+00	97	381		30	5	2400	DELIVERED		936	0.00	1	\N	\N	\N	\N	\N	2016-10-13 11:43:21.560235+00
599	I	2016-10-17 04:10:33.516446+00	465	415		74	3	2412	CREATED			0.00	6	\N	\N	\N	\N	\N	2016-10-17 04:10:33.516446+00
608	I	2016-10-17 09:15:58.416326+00	53	424	1	95	3	2415	CREATED		39915	534690.00	4	\N	\N	\N	\N	\N	2016-10-17 09:15:58.416326+00
609	I	2016-10-17 13:17:32.302399+00	53	425	1	66	3	2416	CREATED		24554,24556	328788.00	4	\N	\N	\N	\N	\N	2016-10-17 13:17:32.302399+00
610	U	2016-10-17 15:07:30.43444+00	97	413	Ек-5158814	0	3	2410	DELIVERED	количество коробок 129	2343	149318.52	3	\N	\N	\N	\N	\N	2016-10-14 10:42:00.497403+00
611	U	2016-10-17 15:07:30.43444+00	97	414	Ек-5158798	67	3	2410	DELIVERED		2342	69609.76	2	\N	\N	\N	\N	\N	2016-10-14 10:42:00.497403+00
615	U	2016-10-17 15:07:56.072017+00	97	420	СПб-015575	2	6	2411	DELIVERED		1417	9328.00	0	\N	\N	\N	\N	\N	2016-10-17 07:09:12.048324+00
616	U	2016-10-17 15:07:56.072017+00	97	419	СПб-015567	18	10	2411	DELIVERED		1415	111000.00	1	\N	\N	\N	\N	\N	2016-10-17 07:09:12.048324+00
617	U	2016-10-17 15:07:56.072017+00	97	417	СПб-015564	25	5	2411	DELIVERED		1416	207216.00	1	\N	\N	\N	\N	\N	2016-10-17 07:09:12.048324+00
618	U	2016-10-17 15:07:56.072017+00	97	421	Ек-5159043	28	7	2411	DELIVERED		1412;491	190440.00	1	\N	\N	\N	\N	\N	2016-10-17 07:09:12.048324+00
619	U	2016-10-17 15:07:56.072017+00	97	416	 Ек-5159038	32	4	2411	DELIVERED		1413	234936.00	1	\N	\N	\N	\N	\N	2016-10-17 07:09:12.048324+00
620	U	2016-10-17 15:07:56.072017+00	97	418	Ек-5159061	34	3	2411	DELIVERED		1414	340695.00	2	\N	\N	\N	\N	\N	2016-10-17 07:09:12.048324+00
621	U	2016-10-17 15:08:06.352807+00	97	412		0	3	2408	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-14 09:57:15.952322+00
622	U	2016-10-17 15:08:15.445866+00	97	415		74	3	2412	DELIVERED			0.00	6	\N	\N	\N	\N	\N	2016-10-17 04:10:33.516446+00
623	U	2016-10-17 15:08:22.949958+00	97	422		0	3	2413	DELIVERED			0.00	8	\N	\N	\N	\N	\N	2016-10-17 07:39:30.211302+00
624	U	2016-10-17 15:08:28.597894+00	97	424	1	95	3	2415	DELIVERED		39915	534690.00	4	\N	\N	\N	\N	\N	2016-10-17 09:15:58.416326+00
625	U	2016-10-17 15:08:34.556526+00	97	425	1	66	3	2416	DELIVERED		24554,24556	328788.00	4	\N	\N	\N	\N	\N	2016-10-17 13:17:32.302399+00
626	I	2016-10-18 07:36:02.030184+00	553	426		0	3	2417	CREATED		360	0.00	0	\N	\N	\N	\N	\N	2016-10-18 07:36:02.030184+00
627	I	2016-10-18 07:36:02.030184+00	553	427		0	7	2417	CREATED		359	0.00	0	\N	\N	\N	\N	\N	2016-10-18 07:36:02.030184+00
628	I	2016-10-18 08:31:26.686853+00	126	428		0	3	2418	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-18 08:31:26.686853+00
629	I	2016-10-18 10:48:21.109163+00	128	429		0	3	2419	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-18 10:48:21.109163+00
630	I	2016-10-18 11:46:39.898596+00	562	430		0	7	2420	CREATED			0.00	2	\N	\N	\N	\N	\N	2016-10-18 11:46:39.898596+00
631	I	2016-10-18 11:46:39.898596+00	562	431		0	3	2420	CREATED			0.00	2	\N	\N	\N	\N	\N	2016-10-18 11:46:39.898596+00
632	I	2016-10-18 11:46:39.898596+00	562	432		0	4	2420	CREATED			0.00	1	\N	\N	\N	\N	\N	2016-10-18 11:46:39.898596+00
633	U	2016-10-19 01:53:54.486106+00	503	375	Ек-5158674	23	7	2396	CREATED		1037	178356.00	0	\N	\N	\N	\N	\N	2016-10-13 06:48:10.67703+00
634	U	2016-10-19 07:22:58.625995+00	97	373	Ек-5158386	5	3	2394	DELIVERED		968	32500.00	0	\N	\N	\N	\N	\N	2016-10-13 01:08:05.259237+00
635	U	2016-10-19 07:22:58.625995+00	97	372	Ек-5158386	28	3	2394	DELIVERED		1012	177972.00	0	\N	\N	\N	\N	\N	2016-10-13 01:08:05.259237+00
636	I	2016-10-19 08:43:43.932915+00	64	437		0	3	2426	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-19 08:43:43.932915+00
637	D	2016-10-19 08:45:21.381794+00	64	437		0	3	2426	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-19 08:43:43.932915+00
638	I	2016-10-19 08:45:37.21022+00	64	438		0	3	2427	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-19 08:45:37.21022+00
640	I	2016-10-19 08:53:54.963754+00	503	440	Ек-5159277	20	3	2428	CREATED		1110	133421.00	0	\N	\N	\N	\N	\N	2016-10-19 08:53:54.963754+00
641	I	2016-10-19 08:53:59.256532+00	64	441		0	3	2427	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-19 08:53:59.256532+00
642	I	2016-10-19 08:54:24.38729+00	64	442		0	3	2427	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-19 08:54:24.38729+00
643	D	2016-10-19 08:54:43.333615+00	64	438		0	3	2427	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-19 08:45:37.21022+00
644	D	2016-10-19 08:54:43.333615+00	64	441		0	3	2427	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-19 08:53:59.256532+00
646	I	2016-10-19 11:46:50.124483+00	555	445		0	3	2486	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-19 11:46:50.124483+00
658	I	2016-10-20 02:13:32.52728+00	503	457	Ек-5170335	25	3	2487	CREATED		1150	235567.00	0	\N	\N	\N	\N	\N	2016-10-20 02:13:32.52728+00
659	I	2016-10-20 05:53:15.112135+00	562	458		0	3	2488	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-20 05:53:15.112135+00
660	I	2016-10-20 05:53:25.382124+00	562	459		0	3	2489	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-20 05:53:25.382124+00
662	I	2016-10-20 08:32:01.084183+00	562	461		0	3	2489	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-20 08:32:01.084183+00
663	I	2016-10-20 09:15:53.069348+00	126	462	ЕК-5159387	11	3	2418	CREATED		РН-11720	80205.00	1	\N	\N	\N	\N	\N	2016-10-20 09:15:53.069348+00
664	I	2016-10-20 09:15:53.069348+00	126	463	ЕК-5159388	7	3	2418	CREATED		РН-11721	30951.00	1	\N	\N	\N	\N	\N	2016-10-20 09:15:53.069348+00
665	D	2016-10-20 09:15:53.069348+00	126	428		0	3	2418	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-18 08:31:26.686853+00
670	U	2016-10-20 13:18:02.31465+00	97	445		0	3	2486	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-19 11:46:50.124483+00
672	U	2016-10-20 13:18:29.164894+00	97	427		0	7	2417	DELIVERED		359	0.00	0	\N	\N	\N	\N	\N	2016-10-18 07:36:02.030184+00
673	U	2016-10-20 13:18:29.164894+00	97	426		0	3	2417	DELIVERED		360	0.00	0	\N	\N	\N	\N	\N	2016-10-18 07:36:02.030184+00
679	I	2016-10-21 11:28:45.356811+00	88	467		0	3	2494	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-21 11:28:45.356811+00
680	I	2016-10-21 11:29:07.186898+00	88	468		0	3	2495	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-21 11:29:07.186898+00
683	U	2016-10-21 13:44:15.292664+00	97	459		0	3	2489	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-20 05:53:25.382124+00
684	U	2016-10-21 13:44:15.292664+00	97	461		0	3	2489	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-20 08:32:01.084183+00
686	I	2016-10-24 05:31:51.762622+00	465	470		10	3	2498	CREATED			0.00	6	\N	\N	\N	\N	\N	2016-10-24 05:31:51.762622+00
698	D	2016-10-24 07:09:52.526589+00	465	470		10	3	2498	CREATED			0.00	6	\N	\N	\N	\N	\N	2016-10-24 05:31:51.762622+00
699	I	2016-10-24 07:11:05.387404+00	465	482		99	3	2500	CREATED			0.00	6	\N	\N	\N	\N	\N	2016-10-24 07:11:05.387404+00
708	I	2016-10-24 07:36:53.817408+00	68	491	Ек-5170715	28	7	2497	CREATED		1467	210660.00	1	\N	\N	\N	\N	\N	2016-10-24 07:36:53.817408+00
714	I	2016-10-24 08:27:25.567511+00	94	499		0	3	3311	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-24 08:27:25.567511+00
717	D	2016-10-24 08:56:50.435026+00	68	491	Ек-5170715	28	7	2497	CREATED		1467	210660.00	1	\N	\N	\N	\N	\N	2016-10-24 07:36:53.817408+00
718	I	2016-10-24 09:08:49.898408+00	64	504		0	3	3344	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-24 09:08:49.898408+00
720	U	2016-10-24 12:53:38.426017+00	97	496		0	4	2501	DELIVERED			0.00	11	\N	\N	\N	\N	\N	2016-10-24 07:44:21.60064+00
721	U	2016-10-24 12:53:49.131961+00	97	499		0	3	3311	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-24 08:27:25.567511+00
722	U	2016-10-24 12:53:56.699691+00	97	482		99	3	2500	DELIVERED			0.00	6	\N	\N	\N	\N	\N	2016-10-24 07:11:05.387404+00
723	I	2016-10-25 05:48:55.489901+00	538	505		0	3	3345	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-25 05:48:55.489901+00
734	I	2016-10-26 02:39:13.27041+00	503	514	СПб-015394	2	5	3355	CREATED		1004	39383.00	0	\N	\N	\N	\N	\N	2016-10-26 02:39:13.27041+00
735	I	2016-10-26 02:39:13.27041+00	503	515	СПб-015394	2	5	3355	CREATED		1004	46487.00	0	\N	\N	\N	\N	\N	2016-10-26 02:39:13.27041+00
738	I	2016-10-26 02:40:56.24764+00	503	516	СПб-015394	2	5	3356	CREATED		1004	46487.00	0	\N	\N	\N	\N	\N	2016-10-26 02:40:56.24764+00
739	I	2016-10-26 02:40:56.24764+00	503	517	СПб-015394	2	5	3356	CREATED		1004	39383.00	0	\N	\N	\N	\N	\N	2016-10-26 02:40:56.24764+00
741	U	2016-10-26 04:46:20.526431+00	70	509		4	7	3348	CREATED		4916 от 25.10.2016	23066.16	0	\N	\N	\N	\N	\N	2016-10-25 08:28:24.566183+00
742	U	2016-10-26 04:46:20.526431+00	70	508		0	3	3348	CREATED		4915 от 25.10.2016	44360.73	0	\N	\N	\N	\N	\N	2016-10-25 08:28:24.566183+00
745	I	2016-10-26 07:38:59.081359+00	503	518	Ек-5170996	0	3	3357	CREATED		1216	19291.00	0	\N	\N	\N	\N	\N	2016-10-26 07:38:59.081359+00
746	I	2016-10-26 07:38:59.081359+00	503	519	Ек-5170991	0	3	3357	CREATED		1217	211328.00	0	\N	\N	\N	\N	\N	2016-10-26 07:38:59.081359+00
747	I	2016-10-26 07:38:59.081359+00	503	520	Ек-5170994	0	3	3357	CREATED		1218	11760.00	0	\N	\N	\N	\N	\N	2016-10-26 07:38:59.081359+00
755	U	2016-10-26 08:37:25.516984+00	97	505		0	3	3345	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-25 05:48:55.489901+00
757	U	2016-10-26 14:45:26.749043+00	97	510		0	3	3349	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-25 08:33:17.598152+00
661	I	2016-10-20 08:31:54.073148+00	562	460		0	3	2488	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-20 08:31:54.073148+00
666	I	2016-10-20 09:36:04.784042+00	53	464	1	99	3	2490	CREATED		24953,24954,24951	755000.00	4	\N	\N	\N	\N	\N	2016-10-20 09:36:04.784042+00
667	U	2016-10-20 10:24:05.85998+00	97	429		0	3	2419	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-18 10:48:21.109163+00
668	I	2016-10-20 12:22:12.963589+00	69	465	Ек-5170360	54	3	2491	CREATED		2397	64610.64	1	\N	\N	\N	\N	\N	2016-10-20 12:22:12.963589+00
669	I	2016-10-20 12:22:12.963589+00	69	466	Ек-5170350	51	3	2491	CREATED		2396	50676.08	2	\N	\N	\N	\N	\N	2016-10-20 12:22:12.963589+00
671	U	2016-10-20 13:18:17.348495+00	97	442		0	3	2427	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-19 08:54:24.38729+00
674	U	2016-10-20 13:18:40.302414+00	97	463	ЕК-5159388	7	3	2418	DELIVERED		РН-11721	30951.00	1	\N	\N	\N	\N	\N	2016-10-20 09:15:53.069348+00
675	U	2016-10-20 13:18:40.302414+00	97	462	ЕК-5159387	11	3	2418	DELIVERED		РН-11720	80205.00	1	\N	\N	\N	\N	\N	2016-10-20 09:15:53.069348+00
676	U	2016-10-20 13:19:20.428085+00	97	464	1	99	3	2490	DELIVERED		24953,24954,24951	755000.00	4	\N	\N	\N	\N	\N	2016-10-20 09:36:04.784042+00
677	U	2016-10-21 06:55:44.613036+00	97	466	Ек-5170350	51	3	2491	DELIVERED		2396	50676.08	2	\N	\N	\N	\N	\N	2016-10-20 12:22:12.963589+00
678	U	2016-10-21 06:55:44.613036+00	97	465	Ек-5170360	54	3	2491	DELIVERED		2397	64610.64	1	\N	\N	\N	\N	\N	2016-10-20 12:22:12.963589+00
681	U	2016-10-21 13:44:05.528669+00	97	458		0	3	2488	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-20 05:53:15.112135+00
682	U	2016-10-21 13:44:05.528669+00	97	460		0	3	2488	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-20 08:31:54.073148+00
685	I	2016-10-24 04:17:23.253143+00	503	469	Ек-5170743	0	7	2496	CREATED		1178	180248.00	0	\N	\N	\N	\N	\N	2016-10-24 04:17:23.253143+00
687	I	2016-10-24 07:06:23.744785+00	14	471	СПб-015749	0	10	2499	CREATED		ЭКМ00031213	171818.20	0	\N	\N	\N	\N	\N	2016-10-24 07:06:23.744785+00
688	I	2016-10-24 07:06:23.744785+00	14	472	СПб-015755	0	10	2499	CREATED		ЭКМ00031222	20179.00	0	\N	\N	\N	\N	\N	2016-10-24 07:06:23.744785+00
689	I	2016-10-24 07:06:23.744785+00	14	473	СПб-015735	0	10	2499	CREATED		ЭКМ00031216	238637.21	0	\N	\N	\N	\N	\N	2016-10-24 07:06:23.744785+00
690	I	2016-10-24 07:06:23.744785+00	14	474	СПб-015736	0	10	2499	CREATED		ЭКМ00031218	265381.24	0	\N	\N	\N	\N	\N	2016-10-24 07:06:23.744785+00
691	I	2016-10-24 07:06:23.744785+00	14	475	СПб-015746	0	10	2499	CREATED		ЭКМ00031219	160055.58	0	\N	\N	\N	\N	\N	2016-10-24 07:06:23.744785+00
692	I	2016-10-24 07:06:23.744785+00	14	476	СПб-015751	0	10	2499	CREATED		ЭКМ00031212	177932.54	0	\N	\N	\N	\N	\N	2016-10-24 07:06:23.744785+00
693	I	2016-10-24 07:06:23.744785+00	14	477	СПб-015757	0	10	2499	CREATED		ЭКМ00031221	258566.96	0	\N	\N	\N	\N	\N	2016-10-24 07:06:23.744785+00
694	I	2016-10-24 07:06:23.744785+00	14	478	СПб-015753	0	10	2499	CREATED		ЭКМ00031215	144386.39	0	\N	\N	\N	\N	\N	2016-10-24 07:06:23.744785+00
695	I	2016-10-24 07:06:23.744785+00	14	479	СПб-015748	0	10	2499	CREATED		ЭКМ00031223	154613.08	0	\N	\N	\N	\N	\N	2016-10-24 07:06:23.744785+00
696	I	2016-10-24 07:06:23.744785+00	14	480	СПб-015752	0	10	2499	CREATED		ЭКМ00031211	173922.07	0	\N	\N	\N	\N	\N	2016-10-24 07:06:23.744785+00
697	I	2016-10-24 07:06:23.744785+00	14	481	СПб-015759	0	10	2499	CREATED			11259.50	0	\N	\N	\N	\N	\N	2016-10-24 07:06:23.744785+00
713	I	2016-10-24 07:44:21.60064+00	12	496		0	4	2501	CREATED			0.00	11	\N	\N	\N	\N	\N	2016-10-24 07:44:21.60064+00
715	I	2016-10-24 08:39:17.489469+00	503	500	 Ек-5170833	11	7	2496	CREATED		1188	148542.00	0	\N	\N	\N	\N	\N	2016-10-24 08:39:17.489469+00
716	U	2016-10-24 08:39:17.489469+00	503	469	Ек-5170743	13	7	2496	CREATED		1178	180248.00	0	\N	\N	\N	\N	\N	2016-10-24 04:17:23.253143+00
719	U	2016-10-24 12:53:32.359881+00	97	468		0	3	2495	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-21 11:29:07.186898+00
724	I	2016-10-25 07:52:35.76967+00	64	506		0	3	3346	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-25 07:52:35.76967+00
725	I	2016-10-25 08:28:18.961129+00	67	507		0	3	3347	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-25 08:28:18.961129+00
726	I	2016-10-25 08:28:24.566183+00	70	508		0	3	3348	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-25 08:28:24.566183+00
727	I	2016-10-25 08:28:24.566183+00	70	509		0	7	3348	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-25 08:28:24.566183+00
728	I	2016-10-25 08:33:17.598152+00	67	510		0	3	3349	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-25 08:33:17.598152+00
729	I	2016-10-25 09:59:16.424112+00	126	511		0	3	3350	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-25 09:59:16.424112+00
730	I	2016-10-25 09:59:16.424112+00	126	512		0	3	3350	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-25 09:59:16.424112+00
731	I	2016-10-25 10:11:17.646112+00	496	513		0	3	3352	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-25 10:11:17.646112+00
732	U	2016-10-26 01:52:16.217564+00	503	457	Ек-5170335	25	7	2487	CREATED		1150	235567.00	0	\N	\N	\N	\N	\N	2016-10-20 02:13:32.52728+00
733	D	2016-10-26 02:30:13.915301+00	503	440	Ек-5159277	20	3	2428	CREATED		1110	133421.00	0	\N	\N	\N	\N	\N	2016-10-19 08:53:54.963754+00
736	D	2016-10-26 02:39:28.866376+00	503	514	СПб-015394	2	5	3355	CREATED		1004	39383.00	0	\N	\N	\N	\N	\N	2016-10-26 02:39:13.27041+00
737	D	2016-10-26 02:39:28.866376+00	503	515	СПб-015394	2	5	3355	CREATED		1004	46487.00	0	\N	\N	\N	\N	\N	2016-10-26 02:39:13.27041+00
740	U	2016-10-26 04:44:47.839472+00	70	509		4	7	3348	CREATED		4916 от 25.10.2016	0.00	0	\N	\N	\N	\N	\N	2016-10-25 08:28:24.566183+00
743	U	2016-10-26 07:13:22.138833+00	97	508		0	3	3348	DELIVERED		4915 от 25.10.2016	44360.73	0	\N	\N	\N	\N	\N	2016-10-25 08:28:24.566183+00
744	U	2016-10-26 07:13:22.138833+00	97	509		4	7	3348	DELIVERED		4916 от 25.10.2016	23066.16	0	\N	\N	\N	\N	\N	2016-10-25 08:28:24.566183+00
748	I	2016-10-26 08:09:01.762612+00	57	521	Ек-5171144 	99	4	3358	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-26 08:09:01.762612+00
749	I	2016-10-26 08:09:01.762612+00	57	522	Ек-5171181	99	3	3358	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-26 08:09:01.762612+00
750	I	2016-10-26 08:09:01.762612+00	57	523	Ек-5171183	99	5	3358	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-26 08:09:01.762612+00
751	I	2016-10-26 08:09:01.762612+00	57	524	Ек-5171152 	99	8	3358	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-26 08:09:01.762612+00
752	I	2016-10-26 08:09:01.762612+00	57	525	Ек-5171185	99	10	3358	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-26 08:09:01.762612+00
753	I	2016-10-26 08:09:01.762612+00	57	526	Ек-5171170	99	7	3358	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-26 08:09:01.762612+00
754	I	2016-10-26 08:09:01.762612+00	57	527	Ек-5171186	99	8	3358	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-26 08:09:01.762612+00
756	U	2016-10-26 14:45:21.826025+00	97	507		0	3	3347	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-25 08:28:18.961129+00
758	U	2016-10-27 08:57:28.227246+00	97	511		0	3	3350	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-25 09:59:16.424112+00
759	U	2016-10-27 08:57:28.227246+00	97	512		0	3	3350	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-25 09:59:16.424112+00
760	I	2016-10-27 09:22:54.998524+00	503	528	Ек-5171497	8	7	3361	CREATED		1272	18087.00	0	\N	\N	\N	\N	\N	2016-10-27 09:22:54.998524+00
761	I	2016-10-27 11:14:04.047689+00	549	529		54	5	3364	CREATED		993	0.00	1	\N	\N	\N	\N	\N	2016-10-27 11:14:04.047689+00
762	I	2016-10-27 11:14:04.047689+00	549	530		18	5	3364	CREATED		994	0.00	1	\N	\N	\N	\N	\N	2016-10-27 11:14:04.047689+00
763	I	2016-10-27 11:14:04.047689+00	549	531		45	10	3364	CREATED		992	0.00	1	\N	\N	\N	\N	\N	2016-10-27 11:14:04.047689+00
764	I	2016-10-27 11:14:04.047689+00	549	532		14	10	3364	CREATED		991	0.00	1	\N	\N	\N	\N	\N	2016-10-27 11:14:04.047689+00
768	D	2016-10-28 04:43:43.530368+00	503	528	Ек-5171497	8	7	3361	CREATED		1272	18087.00	0	\N	\N	\N	\N	\N	2016-10-27 09:22:54.998524+00
772	I	2016-10-28 05:15:09.227135+00	562	539		0	3	3368	CREATED			0.00	3	\N	\N	\N	\N	\N	2016-10-28 05:15:09.227135+00
773	I	2016-10-28 05:15:09.227135+00	562	540		0	7	3368	CREATED			0.00	3	\N	\N	\N	\N	\N	2016-10-28 05:15:09.227135+00
774	I	2016-10-28 05:15:09.227135+00	562	541		0	4	3368	CREATED			0.00	1	\N	\N	\N	\N	\N	2016-10-28 05:15:09.227135+00
777	I	2016-10-28 08:08:45.855423+00	70	546		6	4	3373	CREATED			26178.36	0	\N	\N	\N	\N	\N	2016-10-28 08:08:45.855423+00
778	I	2016-10-28 08:08:45.855423+00	70	547		8	7	3373	CREATED			33312.36	0	\N	\N	\N	\N	\N	2016-10-28 08:08:45.855423+00
779	I	2016-10-28 08:08:45.855423+00	70	548		12	3	3373	CREATED			50600.10	0	\N	\N	\N	\N	\N	2016-10-28 08:08:45.855423+00
780	I	2016-10-28 11:16:11.876234+00	69	552	Ек-5171366	0	3	3376	CREATED	количество коробок 135 шт	2464	151203.24	2	\N	\N	\N	\N	\N	2016-10-28 11:16:11.876234+00
781	I	2016-10-28 11:16:11.876234+00	69	553	СПб-015875	11	3	3376	CREATED		2462	14789.88	1	\N	\N	\N	\N	\N	2016-10-28 11:16:11.876234+00
782	I	2016-10-28 11:16:11.876234+00	69	554	Ек-5171361	42	3	3376	CREATED		2463	39432.96	1	\N	\N	\N	\N	\N	2016-10-28 11:16:11.876234+00
784	I	2016-10-28 12:27:32.986125+00	549	556		60	5	3378	CREATED		999	0.00	2	\N	\N	\N	\N	\N	2016-10-28 12:27:32.986125+00
796	U	2016-10-28 14:18:26.367389+00	97	532		14	10	3364	DELIVERED		991	0.00	1	\N	\N	\N	\N	\N	2016-10-27 11:14:04.047689+00
797	U	2016-10-28 14:18:26.367389+00	97	529		54	5	3364	DELIVERED		993	0.00	1	\N	\N	\N	\N	\N	2016-10-27 11:14:04.047689+00
798	U	2016-10-28 14:18:26.367389+00	97	530		18	5	3364	DELIVERED		994	0.00	1	\N	\N	\N	\N	\N	2016-10-27 11:14:04.047689+00
799	U	2016-10-28 14:18:26.367389+00	97	531		45	10	3364	DELIVERED		992	0.00	1	\N	\N	\N	\N	\N	2016-10-27 11:14:04.047689+00
800	U	2016-10-28 14:18:38.436889+00	97	541		0	4	3368	DELIVERED			0.00	1	\N	\N	\N	\N	\N	2016-10-28 05:15:09.227135+00
801	U	2016-10-28 14:18:38.436889+00	97	539		0	3	3368	DELIVERED			0.00	3	\N	\N	\N	\N	\N	2016-10-28 05:15:09.227135+00
802	U	2016-10-28 14:18:38.436889+00	97	540		0	7	3368	DELIVERED			0.00	3	\N	\N	\N	\N	\N	2016-10-28 05:15:09.227135+00
804	U	2016-10-31 08:21:49.500043+00	97	546		6	4	3373	DELIVERED			26178.36	0	\N	\N	\N	\N	\N	2016-10-28 08:08:45.855423+00
805	U	2016-10-31 08:21:49.500043+00	97	547		8	7	3373	DELIVERED			33312.36	0	\N	\N	\N	\N	\N	2016-10-28 08:08:45.855423+00
806	U	2016-10-31 08:21:49.500043+00	97	548		12	3	3373	DELIVERED			50600.10	0	\N	\N	\N	\N	\N	2016-10-28 08:08:45.855423+00
807	I	2016-10-31 09:29:52.351569+00	64	559		0	3	3398	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-31 09:29:52.351569+00
765	I	2016-10-27 13:56:47.412739+00	57	533	Ек-5171375	99	8	3358	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-27 13:56:47.412739+00
766	I	2016-10-27 13:56:47.412739+00	57	534	Ек-5171383 	99	7	3358	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-27 13:56:47.412739+00
767	I	2016-10-27 13:56:47.412739+00	57	535	 Ек-5171396	99	3	3358	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-27 13:56:47.412739+00
769	I	2016-10-28 04:45:08.137335+00	503	536	Ек-5171497	8	7	3365	CREATED		1272	118575.00	0	\N	\N	\N	\N	\N	2016-10-28 04:45:08.137335+00
770	I	2016-10-28 04:59:35.722309+00	538	537		0	3	3366	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-28 04:59:35.722309+00
771	I	2016-10-28 04:59:59.129644+00	538	538		0	3	3367	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-28 04:59:59.129644+00
775	I	2016-10-28 07:05:42.18975+00	559	544		0	6	3371	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-28 07:05:42.18975+00
776	I	2016-10-28 07:08:37.792239+00	559	545		0	10	3372	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-28 07:08:37.792239+00
783	I	2016-10-28 11:57:51.330582+00	53	555	1	99	3	3377	CREATED		25910,25909,25906,25905,25904	1091657.00	6	\N	\N	\N	\N	\N	2016-10-28 11:57:51.330582+00
785	I	2016-10-28 13:10:01.469604+00	465	557		63	3	3379	CREATED			0.00	5	\N	\N	\N	\N	\N	2016-10-28 13:10:01.469604+00
786	U	2016-10-28 14:18:13.438207+00	97	526	Ек-5171170	99	7	3358	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-26 08:09:01.762612+00
787	U	2016-10-28 14:18:13.438207+00	97	522	Ек-5171181	99	3	3358	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-26 08:09:01.762612+00
788	U	2016-10-28 14:18:13.438207+00	97	533	Ек-5171375	99	8	3358	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-27 13:56:47.412739+00
789	U	2016-10-28 14:18:13.438207+00	97	521	Ек-5171144 	99	4	3358	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-26 08:09:01.762612+00
790	U	2016-10-28 14:18:13.438207+00	97	525	Ек-5171185	99	10	3358	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-26 08:09:01.762612+00
791	U	2016-10-28 14:18:13.438207+00	97	534	Ек-5171383 	99	7	3358	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-27 13:56:47.412739+00
792	U	2016-10-28 14:18:13.438207+00	97	523	Ек-5171183	99	5	3358	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-26 08:09:01.762612+00
793	U	2016-10-28 14:18:13.438207+00	97	535	 Ек-5171396	99	3	3358	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-27 13:56:47.412739+00
794	U	2016-10-28 14:18:13.438207+00	97	524	Ек-5171152 	99	8	3358	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-26 08:09:01.762612+00
795	U	2016-10-28 14:18:13.438207+00	97	527	Ек-5171186	99	8	3358	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-26 08:09:01.762612+00
803	U	2016-10-28 14:18:43.798006+00	97	555	1	99	3	3377	DELIVERED		25910,25909,25906,25905,25904	1091657.00	6	\N	\N	\N	\N	\N	2016-10-28 11:57:51.330582+00
808	I	2016-11-02 06:57:35.454634+00	57	563	Ек-5171974	0	7	3402	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-11-02 06:57:35.454634+00
809	I	2016-11-02 06:57:35.454634+00	57	564	 Ек-5171940	0	4	3402	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-11-02 06:57:35.454634+00
810	I	2016-11-02 06:57:35.454634+00	57	565	 Ек-5171986 	0	3	3402	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-11-02 06:57:35.454634+00
811	I	2016-11-02 06:57:35.454634+00	57	566	 Ек-5171991 	0	10	3402	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-11-02 06:57:35.454634+00
812	I	2016-11-02 07:17:34.849933+00	64	581		0	3	3417	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-11-02 07:17:34.849933+00
813	D	2016-11-02 07:17:49.490449+00	64	581		0	3	3417	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-11-02 07:17:34.849933+00
814	I	2016-11-02 07:19:15.877703+00	64	583		0	7	3419	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-11-02 07:19:15.877703+00
815	D	2016-11-02 07:20:22.291053+00	64	583		0	7	3419	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-11-02 07:19:15.877703+00
816	I	2016-11-02 07:39:22.90724+00	64	594		0	7	3430	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-11-02 07:39:22.90724+00
819	I	2016-11-02 07:41:06.30028+00	64	597		0	7	3430	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-11-02 07:41:06.30028+00
821	I	2016-11-02 07:41:48.669847+00	64	599		0	7	3430	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-11-02 07:41:48.669847+00
822	U	2016-11-02 09:33:20.44471+00	23	469	Ек-5170743	13	7	2496	DELIVERED		1178	180248.00	0	\N	\N	\N	\N	\N	2016-10-24 04:17:23.253143+00
823	U	2016-11-02 09:33:20.44471+00	23	500	 Ек-5170833	11	7	2496	DELIVERED		1188	148542.00	0	\N	\N	\N	\N	\N	2016-10-24 08:39:17.489469+00
824	I	2016-11-02 10:20:20.400202+00	12	600		0	7	3431	CREATED			0.00	11	\N	\N	\N	\N	\N	2016-11-02 10:20:20.400202+00
825	I	2016-11-02 10:21:07.74128+00	12	601		0	7	3432	CREATED			0.00	15	\N	\N	\N	\N	\N	2016-11-02 10:21:07.74128+00
826	I	2016-11-02 10:22:43.338099+00	12	602		0	7	3433	CREATED			0.00	8	\N	\N	\N	\N	\N	2016-11-02 10:22:43.338099+00
827	I	2016-11-02 12:08:15.55979+00	126	604	ЕК-5172102	1	3	3437	CREATED		Рн-12402	58378.00	1	\N	\N	\N	\N	\N	2016-11-02 12:08:15.55979+00
828	I	2016-11-02 12:08:15.55979+00	126	605	ЕК-5172103	1	3	3437	CREATED		Рн-12401	56073.00	1	\N	\N	\N	\N	\N	2016-11-02 12:08:15.55979+00
829	I	2016-11-02 12:42:10.299065+00	549	608		23	10	3439	CREATED		1025	0.00	1	\N	\N	\N	\N	\N	2016-11-02 12:42:10.299065+00
830	I	2016-11-02 13:35:24.651507+00	12	609		0	7	3440	CREATED			0.00	11	\N	\N	\N	\N	\N	2016-11-02 13:35:24.651507+00
832	I	2016-11-02 14:24:35.30343+00	64	611		0	3	3398	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-11-02 14:24:35.30343+00
833	U	2016-11-02 14:24:48.930492+00	64	559		5	3	3398	CREATED			0.00	2	\N	\N	\N	\N	\N	2016-10-31 09:29:52.351569+00
834	U	2016-11-03 05:56:37.043272+00	97	519	Ек-5170991	0	3	3357	DELIVERED		1217	211328.00	0	\N	\N	\N	\N	\N	2016-10-26 07:38:59.081359+00
835	U	2016-11-03 05:56:37.043272+00	97	518	Ек-5170996	0	3	3357	DELIVERED		1216	19291.00	0	\N	\N	\N	\N	\N	2016-10-26 07:38:59.081359+00
836	U	2016-11-03 05:56:37.043272+00	97	520	Ек-5170994	0	3	3357	DELIVERED		1218	11760.00	0	\N	\N	\N	\N	\N	2016-10-26 07:38:59.081359+00
837	U	2016-11-03 05:56:51.980913+00	97	565	 Ек-5171986 	0	3	3402	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-11-02 06:57:35.454634+00
838	U	2016-11-03 05:56:51.980913+00	97	563	Ек-5171974	0	7	3402	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-11-02 06:57:35.454634+00
839	U	2016-11-03 05:56:51.980913+00	97	566	 Ек-5171991 	0	10	3402	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-11-02 06:57:35.454634+00
840	U	2016-11-03 05:56:51.980913+00	97	564	 Ек-5171940	0	4	3402	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-11-02 06:57:35.454634+00
841	U	2016-11-03 05:57:09.383258+00	97	537		0	3	3366	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-28 04:59:35.722309+00
842	U	2016-11-03 05:57:17.538888+00	97	538		0	3	3367	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-28 04:59:59.129644+00
843	U	2016-11-03 05:57:24.517037+00	97	557		63	3	3379	DELIVERED			0.00	5	\N	\N	\N	\N	\N	2016-10-28 13:10:01.469604+00
844	U	2016-11-03 05:57:43.988564+00	97	553	СПб-015875	11	3	3376	DELIVERED		2462	14789.88	1	\N	\N	\N	\N	\N	2016-10-28 11:16:11.876234+00
845	U	2016-11-03 05:57:43.988564+00	97	554	Ек-5171361	42	3	3376	DELIVERED		2463	39432.96	1	\N	\N	\N	\N	\N	2016-10-28 11:16:11.876234+00
846	U	2016-11-03 05:57:43.988564+00	97	552	Ек-5171366	0	3	3376	DELIVERED	количество коробок 135 шт	2464	151203.24	2	\N	\N	\N	\N	\N	2016-10-28 11:16:11.876234+00
847	U	2016-11-03 05:57:49.466097+00	97	556		60	5	3378	DELIVERED		999	0.00	2	\N	\N	\N	\N	\N	2016-10-28 12:27:32.986125+00
848	I	2016-11-03 06:02:36.358797+00	562	617		0	3	3446	CREATED			0.00	4	\N	\N	\N	\N	\N	2016-11-03 06:02:36.358797+00
849	I	2016-11-03 06:02:36.358797+00	562	618		0	7	3446	CREATED			0.00	6	\N	\N	\N	\N	\N	2016-11-03 06:02:36.358797+00
850	I	2016-11-03 06:02:36.358797+00	562	619		0	10	3446	CREATED			0.00	2	\N	\N	\N	\N	\N	2016-11-03 06:02:36.358797+00
851	I	2016-11-03 06:02:36.358797+00	562	620		0	4	3446	CREATED			0.00	1	\N	\N	\N	\N	\N	2016-11-03 06:02:36.358797+00
852	I	2016-11-03 08:11:30.281207+00	53	623	1	99	3	3452	CREATED		26494,26495,26496,26497,26498,26499,26500	663000.00	5	\N	\N	\N	\N	\N	2016-11-03 08:11:30.281207+00
853	U	2016-11-03 09:36:51.135683+00	97	611		0	3	3398	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-11-02 14:24:35.30343+00
854	U	2016-11-03 09:36:51.135683+00	97	559		5	3	3398	DELIVERED			0.00	2	\N	\N	\N	\N	\N	2016-10-31 09:29:52.351569+00
855	U	2016-11-03 09:36:56.983332+00	97	609		0	7	3440	DELIVERED			0.00	11	\N	\N	\N	\N	\N	2016-11-02 13:35:24.651507+00
856	I	2016-11-03 10:07:54.741528+00	69	624	Ек-5172451	50	3	3448	CREATED		2506	47896.56	2	\N	\N	\N	\N	\N	2016-11-03 10:07:54.741528+00
857	I	2016-11-03 10:07:54.741528+00	69	625	Ек-5172453	0	3	3448	CREATED	кол-во коробок 103	2507	114121.80	2	\N	\N	\N	\N	\N	2016-11-03 10:07:54.741528+00
858	I	2016-11-03 10:07:54.741528+00	69	626	СПб-016137	43	3	3448	CREATED		2505	63762.84	1	\N	\N	\N	\N	\N	2016-11-03 10:07:54.741528+00
859	U	2016-11-03 12:47:25.831551+00	97	623	1	99	3	3452	DELIVERED		26494,26495,26496,26497,26498,26499,26500	663000.00	5	\N	\N	\N	\N	\N	2016-11-03 08:11:30.281207+00
860	U	2016-11-03 12:47:43.010236+00	97	600		0	7	3431	DELIVERED			0.00	11	\N	\N	\N	\N	\N	2016-11-02 10:20:20.400202+00
861	U	2016-11-03 12:47:50.020618+00	97	601		0	7	3432	DELIVERED			0.00	15	\N	\N	\N	\N	\N	2016-11-02 10:21:07.74128+00
862	U	2016-11-03 12:47:57.006823+00	97	602		0	7	3433	DELIVERED			0.00	8	\N	\N	\N	\N	\N	2016-11-02 10:22:43.338099+00
863	U	2016-11-03 12:48:04.091223+00	97	604	ЕК-5172102	1	3	3437	DELIVERED		Рн-12402	58378.00	1	\N	\N	\N	\N	\N	2016-11-02 12:08:15.55979+00
864	U	2016-11-03 12:48:04.091223+00	97	605	ЕК-5172103	1	3	3437	DELIVERED		Рн-12401	56073.00	1	\N	\N	\N	\N	\N	2016-11-02 12:08:15.55979+00
865	I	2016-11-03 13:08:35.652962+00	572	627	1	19	3	3453	CREATED		ПК00-000281 от 03.11.2016	0.00	0	\N	\N	\N	\N	\N	2016-11-03 13:08:35.652962+00
866	I	2016-11-03 13:17:31.217739+00	572	628	2	9	4	3453	CREATED		ПК00-000282 от 03.11.2016	43624.00	0	\N	\N	\N	\N	\N	2016-11-03 13:17:31.217739+00
867	I	2016-11-03 13:17:31.217739+00	572	629	3	20	7	3453	CREATED		ПК00-000280 от 03.11.2016	92680.00	0	\N	\N	\N	\N	\N	2016-11-03 13:17:31.217739+00
868	U	2016-11-03 13:17:31.217739+00	572	627	1	19	3	3453	CREATED		ПК00-000281 от 03.11.2016	77800.00	0	\N	\N	\N	\N	\N	2016-11-03 13:08:35.652962+00
869	U	2016-11-03 14:44:18.656369+00	97	608		23	10	3439	DELIVERED		1025	0.00	1	\N	\N	\N	\N	\N	2016-11-02 12:42:10.299065+00
870	U	2016-11-03 14:44:30.505229+00	97	617		0	3	3446	DELIVERED			0.00	4	\N	\N	\N	\N	\N	2016-11-03 06:02:36.358797+00
871	U	2016-11-03 14:44:30.505229+00	97	620		0	4	3446	DELIVERED			0.00	1	\N	\N	\N	\N	\N	2016-11-03 06:02:36.358797+00
872	U	2016-11-03 14:44:30.505229+00	97	618		0	7	3446	DELIVERED			0.00	6	\N	\N	\N	\N	\N	2016-11-03 06:02:36.358797+00
873	U	2016-11-03 14:44:30.505229+00	97	619		0	10	3446	DELIVERED			0.00	2	\N	\N	\N	\N	\N	2016-11-03 06:02:36.358797+00
\.


--
-- TOC entry 2325 (class 0 OID 0)
-- Dependencies: 204
-- Name: orders_audit_ordersauditid_seq; Type: SEQUENCE SET; Schema: audit; Owner: postgres
--

SELECT pg_catalog.setval('orders_audit_ordersauditid_seq', 873, true);


SET search_path = public, pg_catalog;

--
-- TOC entry 2267 (class 0 OID 20956)
-- Dependencies: 192
-- Data for Name: doc_periods; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY doc_periods (docperiodid, docid, periodbegin, periodend) FROM stdin;
1968	9	2016-09-01 15:00:00+00	2016-09-01 21:00:00+00
1969	9	2016-09-02 15:00:00+00	2016-09-02 21:00:00+00
1970	9	2016-09-03 15:00:00+00	2016-09-03 21:00:00+00
5	11	2016-07-03 19:00:00+00	2016-07-04 04:00:00+00
6	11	2016-07-04 19:00:00+00	2016-07-05 04:00:00+00
7	11	2016-07-05 19:00:00+00	2016-07-06 04:00:00+00
8	11	2016-07-06 19:00:00+00	2016-07-07 04:00:00+00
9	11	2016-07-07 19:00:00+00	2016-07-08 04:00:00+00
10	11	2016-07-08 19:00:00+00	2016-07-09 04:00:00+00
11	11	2016-07-09 19:00:00+00	2016-07-10 04:00:00+00
12	11	2016-07-10 19:00:00+00	2016-07-11 04:00:00+00
13	11	2016-07-11 19:00:00+00	2016-07-12 04:00:00+00
14	11	2016-07-12 19:00:00+00	2016-07-13 04:00:00+00
15	11	2016-07-13 19:00:00+00	2016-07-14 04:00:00+00
16	11	2016-07-14 19:00:00+00	2016-07-15 04:00:00+00
17	11	2016-07-15 19:00:00+00	2016-07-16 04:00:00+00
18	11	2016-07-16 19:00:00+00	2016-07-17 04:00:00+00
19	11	2016-07-17 19:00:00+00	2016-07-18 04:00:00+00
20	7	2016-07-03 21:00:00+00	2016-07-04 06:00:00+00
21	7	2016-07-04 21:00:00+00	2016-07-05 06:00:00+00
24	7	2016-07-07 21:00:00+00	2016-07-08 06:00:00+00
26	7	2016-07-09 21:00:00+00	2016-07-10 06:00:00+00
27	7	2016-07-10 21:00:00+00	2016-07-11 06:00:00+00
28	7	2016-07-11 21:00:00+00	2016-07-12 06:00:00+00
30	7	2016-07-13 21:00:00+00	2016-07-14 06:00:00+00
31	7	2016-07-14 21:00:00+00	2016-07-15 06:00:00+00
35	8	2016-07-03 21:00:00+00	2016-07-04 06:00:00+00
36	8	2016-07-04 21:00:00+00	2016-07-05 06:00:00+00
37	8	2016-07-05 21:00:00+00	2016-07-06 06:00:00+00
38	8	2016-07-06 21:00:00+00	2016-07-07 06:00:00+00
39	8	2016-07-07 21:00:00+00	2016-07-08 06:00:00+00
40	8	2016-07-08 21:00:00+00	2016-07-09 06:00:00+00
41	8	2016-07-09 21:00:00+00	2016-07-10 06:00:00+00
42	8	2016-07-10 21:00:00+00	2016-07-11 06:00:00+00
43	8	2016-07-11 21:00:00+00	2016-07-12 06:00:00+00
44	8	2016-07-12 21:00:00+00	2016-07-13 06:00:00+00
45	8	2016-07-13 21:00:00+00	2016-07-14 06:00:00+00
46	8	2016-07-14 21:00:00+00	2016-07-15 06:00:00+00
47	8	2016-07-15 21:00:00+00	2016-07-16 06:00:00+00
48	8	2016-07-16 21:00:00+00	2016-07-17 06:00:00+00
49	8	2016-07-17 21:00:00+00	2016-07-18 06:00:00+00
50	10	2016-07-03 21:00:00+00	2016-07-04 06:00:00+00
51	10	2016-07-04 21:00:00+00	2016-07-05 06:00:00+00
52	10	2016-07-05 21:00:00+00	2016-07-06 06:00:00+00
53	10	2016-07-06 21:00:00+00	2016-07-07 06:00:00+00
54	10	2016-07-07 21:00:00+00	2016-07-08 06:00:00+00
55	10	2016-07-08 21:00:00+00	2016-07-09 06:00:00+00
56	10	2016-07-09 21:00:00+00	2016-07-10 06:00:00+00
57	10	2016-07-10 21:00:00+00	2016-07-11 06:00:00+00
58	10	2016-07-11 21:00:00+00	2016-07-12 06:00:00+00
59	10	2016-07-12 21:00:00+00	2016-07-13 06:00:00+00
60	10	2016-07-13 21:00:00+00	2016-07-14 06:00:00+00
61	10	2016-07-14 21:00:00+00	2016-07-15 06:00:00+00
62	10	2016-07-15 21:00:00+00	2016-07-16 06:00:00+00
63	10	2016-07-16 21:00:00+00	2016-07-17 06:00:00+00
64	10	2016-07-17 21:00:00+00	2016-07-18 06:00:00+00
65	9	2016-07-03 21:00:00+00	2016-07-04 06:00:00+00
66	9	2016-07-04 21:00:00+00	2016-07-05 06:00:00+00
67	9	2016-07-05 21:00:00+00	2016-07-06 06:00:00+00
68	9	2016-07-06 21:00:00+00	2016-07-07 06:00:00+00
69	9	2016-07-07 21:00:00+00	2016-07-08 06:00:00+00
70	9	2016-07-08 21:00:00+00	2016-07-09 06:00:00+00
71	9	2016-07-09 21:00:00+00	2016-07-10 06:00:00+00
72	9	2016-07-10 21:00:00+00	2016-07-11 06:00:00+00
73	9	2016-07-11 21:00:00+00	2016-07-12 06:00:00+00
74	9	2016-07-12 21:00:00+00	2016-07-13 06:00:00+00
75	9	2016-07-13 21:00:00+00	2016-07-14 06:00:00+00
76	9	2016-07-14 21:00:00+00	2016-07-15 06:00:00+00
77	9	2016-07-15 21:00:00+00	2016-07-16 06:00:00+00
78	9	2016-07-16 21:00:00+00	2016-07-17 06:00:00+00
79	9	2016-07-17 21:00:00+00	2016-07-18 06:00:00+00
80	13	2016-07-03 17:00:00+00	2016-07-04 02:00:00+00
81	13	2016-07-04 17:00:00+00	2016-07-05 02:00:00+00
82	13	2016-07-05 17:00:00+00	2016-07-06 02:00:00+00
83	13	2016-07-06 17:00:00+00	2016-07-07 02:00:00+00
84	13	2016-07-07 17:00:00+00	2016-07-08 02:00:00+00
85	13	2016-07-08 17:00:00+00	2016-07-09 02:00:00+00
86	13	2016-07-09 17:00:00+00	2016-07-10 02:00:00+00
87	13	2016-07-10 17:00:00+00	2016-07-11 02:00:00+00
88	13	2016-07-11 17:00:00+00	2016-07-12 02:00:00+00
89	13	2016-07-12 17:00:00+00	2016-07-13 02:00:00+00
90	13	2016-07-13 17:00:00+00	2016-07-14 02:00:00+00
91	13	2016-07-14 17:00:00+00	2016-07-15 02:00:00+00
92	13	2016-07-15 17:00:00+00	2016-07-16 02:00:00+00
93	13	2016-07-16 17:00:00+00	2016-07-17 02:00:00+00
94	13	2016-07-17 17:00:00+00	2016-07-18 02:00:00+00
95	12	2016-07-03 16:00:00+00	2016-07-04 01:00:00+00
96	12	2016-07-04 16:00:00+00	2016-07-05 01:00:00+00
97	12	2016-07-05 16:00:00+00	2016-07-06 01:00:00+00
98	12	2016-07-06 16:00:00+00	2016-07-07 01:00:00+00
99	12	2016-07-07 16:00:00+00	2016-07-08 01:00:00+00
100	12	2016-07-08 16:00:00+00	2016-07-09 01:00:00+00
101	12	2016-07-09 16:00:00+00	2016-07-10 01:00:00+00
102	12	2016-07-10 16:00:00+00	2016-07-11 01:00:00+00
103	12	2016-07-11 16:00:00+00	2016-07-12 01:00:00+00
104	12	2016-07-12 16:00:00+00	2016-07-13 01:00:00+00
105	12	2016-07-13 16:00:00+00	2016-07-14 01:00:00+00
106	12	2016-07-14 16:00:00+00	2016-07-15 01:00:00+00
107	12	2016-07-15 16:00:00+00	2016-07-16 01:00:00+00
108	12	2016-07-16 16:00:00+00	2016-07-17 01:00:00+00
109	12	2016-07-17 16:00:00+00	2016-07-18 01:00:00+00
110	7	2016-07-03 15:00:00+00	2016-07-03 21:00:00+00
111	7	2016-07-04 15:00:00+00	2016-07-04 21:00:00+00
112	7	2016-07-05 15:00:00+00	2016-07-05 21:00:00+00
113	7	2016-07-06 15:00:00+00	2016-07-06 21:00:00+00
114	7	2016-07-07 15:00:00+00	2016-07-07 21:00:00+00
115	7	2016-07-08 15:00:00+00	2016-07-08 21:00:00+00
117	7	2016-07-10 15:00:00+00	2016-07-10 21:00:00+00
118	7	2016-07-11 15:00:00+00	2016-07-11 21:00:00+00
119	7	2016-07-12 15:00:00+00	2016-07-12 21:00:00+00
120	7	2016-07-13 15:00:00+00	2016-07-13 21:00:00+00
121	7	2016-07-14 15:00:00+00	2016-07-14 21:00:00+00
122	7	2016-07-15 15:00:00+00	2016-07-15 21:00:00+00
123	7	2016-07-16 15:00:00+00	2016-07-16 21:00:00+00
124	8	2016-07-03 15:00:00+00	2016-07-03 21:00:00+00
125	8	2016-07-04 15:00:00+00	2016-07-04 21:00:00+00
126	8	2016-07-05 15:00:00+00	2016-07-05 21:00:00+00
127	8	2016-07-06 15:00:00+00	2016-07-06 21:00:00+00
128	8	2016-07-07 15:00:00+00	2016-07-07 21:00:00+00
129	8	2016-07-08 15:00:00+00	2016-07-08 21:00:00+00
130	8	2016-07-09 15:00:00+00	2016-07-09 21:00:00+00
131	8	2016-07-10 15:00:00+00	2016-07-10 21:00:00+00
132	8	2016-07-11 15:00:00+00	2016-07-11 21:00:00+00
133	8	2016-07-12 15:00:00+00	2016-07-12 21:00:00+00
134	8	2016-07-13 15:00:00+00	2016-07-13 21:00:00+00
135	8	2016-07-14 15:00:00+00	2016-07-14 21:00:00+00
136	8	2016-07-15 15:00:00+00	2016-07-15 21:00:00+00
137	8	2016-07-16 15:00:00+00	2016-07-16 21:00:00+00
138	10	2016-07-03 15:00:00+00	2016-07-03 21:00:00+00
139	10	2016-07-04 15:00:00+00	2016-07-04 21:00:00+00
22	7	2016-07-05 21:00:00+00	2016-07-06 05:30:00+00
34	7	2016-07-17 21:00:00+00	2016-07-18 00:00:00+00
32	7	2016-07-15 21:00:00+00	2016-07-16 05:30:00+00
29	7	2016-07-12 21:00:00+00	2016-07-13 05:30:00+00
33	7	2016-07-16 21:00:00+00	2016-07-17 05:30:00+00
140	10	2016-07-05 15:00:00+00	2016-07-05 21:00:00+00
141	10	2016-07-06 15:00:00+00	2016-07-06 21:00:00+00
142	10	2016-07-07 15:00:00+00	2016-07-07 21:00:00+00
143	10	2016-07-08 15:00:00+00	2016-07-08 21:00:00+00
144	10	2016-07-09 15:00:00+00	2016-07-09 21:00:00+00
145	10	2016-07-10 15:00:00+00	2016-07-10 21:00:00+00
146	10	2016-07-11 15:00:00+00	2016-07-11 21:00:00+00
147	10	2016-07-12 15:00:00+00	2016-07-12 21:00:00+00
148	10	2016-07-13 15:00:00+00	2016-07-13 21:00:00+00
149	10	2016-07-14 15:00:00+00	2016-07-14 21:00:00+00
150	10	2016-07-15 15:00:00+00	2016-07-15 21:00:00+00
151	10	2016-07-16 15:00:00+00	2016-07-16 21:00:00+00
1971	9	2016-09-04 15:00:00+00	2016-09-04 21:00:00+00
152	11	2016-07-10 13:30:00+00	2016-07-10 19:00:00+00
153	11	2016-07-10 13:00:00+00	2016-07-10 13:30:00+00
154	11	2016-07-06 13:00:00+00	2016-07-06 19:00:00+00
155	11	2016-07-07 13:00:00+00	2016-07-07 19:00:00+00
156	11	2016-07-08 13:00:00+00	2016-07-08 19:00:00+00
157	11	2016-07-09 13:00:00+00	2016-07-09 19:00:00+00
158	11	2016-07-10 04:00:00+00	2016-07-10 13:00:00+00
159	11	2016-07-09 04:00:00+00	2016-07-09 13:00:00+00
160	11	2016-07-11 13:00:00+00	2016-07-11 19:00:00+00
161	11	2016-07-12 13:00:00+00	2016-07-12 19:00:00+00
162	11	2016-07-13 13:00:00+00	2016-07-13 19:00:00+00
163	11	2016-07-14 13:00:00+00	2016-07-14 19:00:00+00
164	11	2016-07-15 13:00:00+00	2016-07-15 19:00:00+00
165	11	2016-07-16 10:00:00+00	2016-07-16 11:00:00+00
166	11	2016-07-16 16:00:00+00	2016-07-16 17:00:00+00
167	11	2016-07-16 04:00:00+00	2016-07-16 05:00:00+00
168	11	2016-07-16 07:00:00+00	2016-07-16 08:00:00+00
169	11	2016-07-16 13:00:00+00	2016-07-16 14:00:00+00
170	11	2016-07-16 11:00:00+00	2016-07-16 13:00:00+00
171	11	2016-07-16 14:00:00+00	2016-07-16 16:00:00+00
172	11	2016-07-16 08:00:00+00	2016-07-16 10:00:00+00
173	11	2016-07-16 17:00:00+00	2016-07-16 19:00:00+00
174	11	2016-07-16 05:00:00+00	2016-07-16 07:00:00+00
175	11	2016-07-17 04:00:00+00	2016-07-17 19:00:00+00
176	11	2016-07-18 13:00:00+00	2016-07-18 19:00:00+00
177	12	2016-07-06 10:00:00+00	2016-07-06 16:00:00+00
178	12	2016-07-07 10:00:00+00	2016-07-07 16:00:00+00
179	12	2016-07-08 10:00:00+00	2016-07-08 16:00:00+00
180	12	2016-07-09 10:00:00+00	2016-07-09 16:00:00+00
181	12	2016-07-10 10:00:00+00	2016-07-10 16:00:00+00
182	12	2016-07-11 10:00:00+00	2016-07-11 16:00:00+00
183	12	2016-07-12 10:00:00+00	2016-07-12 16:00:00+00
184	12	2016-07-13 10:00:00+00	2016-07-13 16:00:00+00
185	12	2016-07-14 10:00:00+00	2016-07-14 16:00:00+00
186	12	2016-07-15 10:00:00+00	2016-07-15 16:00:00+00
187	12	2016-07-16 01:00:00+00	2016-07-16 16:00:00+00
188	12	2016-07-17 01:00:00+00	2016-07-17 16:00:00+00
189	12	2016-07-18 10:00:00+00	2016-07-18 16:00:00+00
23	7	2016-07-06 21:00:00+00	2016-07-07 05:00:00+00
194	7	2016-07-07 05:30:00+00	2016-07-07 06:00:00+00
203	7	2016-07-14 10:00:00+00	2016-07-14 11:00:00+00
205	7	2016-07-18 02:00:00+00	2016-07-18 06:00:00+00
212	7	2016-07-15 10:00:00+00	2016-07-15 10:30:00+00
213	7	2016-07-16 10:00:00+00	2016-07-16 10:30:00+00
214	7	2016-07-16 05:30:00+00	2016-07-16 06:00:00+00
215	7	2016-07-17 15:00:00+00	2016-07-17 21:00:00+00
216	7	2016-07-18 00:00:00+00	2016-07-18 02:00:00+00
217	7	2016-07-18 15:00:00+00	2016-07-18 21:00:00+00
218	7	2016-07-18 10:00:00+00	2016-07-18 10:30:00+00
219	7	2016-07-18 21:00:00+00	2016-07-19 03:00:00+00
220	7	2016-07-19 15:00:00+00	2016-07-19 21:00:00+00
221	7	2016-07-19 10:00:00+00	2016-07-19 10:30:00+00
222	7	2016-07-19 21:00:00+00	2016-07-20 03:00:00+00
223	7	2016-07-20 15:00:00+00	2016-07-20 21:00:00+00
224	7	2016-07-20 10:00:00+00	2016-07-20 10:30:00+00
225	7	2016-07-21 15:00:00+00	2016-07-21 21:00:00+00
226	7	2016-07-20 21:00:00+00	2016-07-21 03:00:00+00
227	7	2016-07-21 10:00:00+00	2016-07-21 10:30:00+00
228	7	2016-07-21 21:00:00+00	2016-07-21 21:30:00+00
229	7	2016-07-13 05:30:00+00	2016-07-13 06:00:00+00
232	7	2016-07-17 05:30:00+00	2016-07-17 06:00:00+00
233	7	2016-07-17 10:00:00+00	2016-07-17 11:00:00+00
234	7	2016-07-18 10:30:00+00	2016-07-18 11:00:00+00
235	7	2016-07-19 10:30:00+00	2016-07-19 11:00:00+00
236	7	2016-07-19 03:00:00+00	2016-07-19 06:00:00+00
237	7	2016-07-20 03:00:00+00	2016-07-20 06:00:00+00
238	7	2016-07-20 10:30:00+00	2016-07-20 11:00:00+00
239	7	2016-07-21 03:00:00+00	2016-07-21 06:00:00+00
240	7	2016-07-21 10:30:00+00	2016-07-21 11:00:00+00
241	7	2016-07-21 21:30:00+00	2016-07-22 06:00:00+00
242	7	2016-07-22 10:30:00+00	2016-07-22 11:30:00+00
243	7	2016-07-22 15:00:00+00	2016-07-22 21:00:00+00
244	7	2016-07-22 21:00:00+00	2016-07-23 06:00:00+00
245	7	2016-07-23 15:00:00+00	2016-07-23 21:00:00+00
246	7	2016-07-24 10:00:00+00	2016-07-24 11:00:00+00
247	7	2016-07-23 21:00:00+00	2016-07-24 06:00:00+00
248	7	2016-07-24 15:00:00+00	2016-07-24 21:00:00+00
249	7	2016-07-24 21:00:00+00	2016-07-25 06:00:00+00
250	7	2016-07-25 10:00:00+00	2016-07-25 11:00:00+00
251	7	2016-07-25 15:00:00+00	2016-07-25 21:00:00+00
252	7	2016-07-26 10:00:00+00	2016-07-26 11:00:00+00
253	7	2016-07-26 15:00:00+00	2016-07-26 21:00:00+00
254	7	2016-07-25 21:00:00+00	2016-07-26 06:00:00+00
255	7	2016-07-26 21:00:00+00	2016-07-27 06:00:00+00
256	7	2016-07-27 10:00:00+00	2016-07-27 11:00:00+00
257	7	2016-07-27 15:00:00+00	2016-07-27 21:00:00+00
258	7	2016-07-27 21:00:00+00	2016-07-28 06:00:00+00
259	7	2016-07-28 10:00:00+00	2016-07-28 11:00:00+00
260	7	2016-07-28 15:00:00+00	2016-07-28 21:00:00+00
261	7	2016-07-28 21:00:00+00	2016-07-29 06:00:00+00
262	7	2016-07-29 10:00:00+00	2016-07-29 11:00:00+00
263	7	2016-07-29 15:00:00+00	2016-07-29 21:00:00+00
264	7	2016-07-30 10:00:00+00	2016-07-30 11:00:00+00
265	7	2016-07-29 21:00:00+00	2016-07-30 06:00:00+00
266	7	2016-07-30 15:00:00+00	2016-07-30 21:00:00+00
267	7	2016-07-30 21:00:00+00	2016-07-31 06:00:00+00
268	7	2016-07-31 15:00:00+00	2016-07-31 21:00:00+00
269	7	2016-07-31 10:00:00+00	2016-07-31 11:00:00+00
271	7	2016-07-31 21:00:00+00	2016-08-01 06:00:00+00
272	7	2016-08-01 15:00:00+00	2016-08-01 21:00:00+00
273	12	2016-07-18 05:00:00+00	2016-07-18 06:00:00+00
274	12	2016-07-19 05:00:00+00	2016-07-19 06:00:00+00
275	12	2016-07-18 16:00:00+00	2016-07-19 01:00:00+00
276	12	2016-07-19 10:00:00+00	2016-07-19 16:00:00+00
277	12	2016-07-20 10:00:00+00	2016-07-20 16:00:00+00
278	12	2016-07-19 16:00:00+00	2016-07-20 00:00:00+00
279	12	2016-07-20 05:00:00+00	2016-07-20 06:00:00+00
280	12	2016-07-20 00:30:00+00	2016-07-20 01:00:00+00
281	12	2016-07-20 00:00:00+00	2016-07-20 00:30:00+00
282	12	2016-07-21 05:00:00+00	2016-07-21 06:00:00+00
283	12	2016-07-20 16:00:00+00	2016-07-21 01:00:00+00
284	12	2016-07-21 10:00:00+00	2016-07-21 16:00:00+00
285	12	2016-07-21 16:00:00+00	2016-07-22 01:00:00+00
286	12	2016-07-22 05:00:00+00	2016-07-22 06:00:00+00
287	12	2016-07-22 10:00:00+00	2016-07-22 16:00:00+00
288	12	2016-07-23 10:00:00+00	2016-07-23 16:00:00+00
289	12	2016-07-23 05:00:00+00	2016-07-23 06:00:00+00
290	12	2016-07-22 16:00:00+00	2016-07-23 01:00:00+00
291	12	2016-07-23 16:00:00+00	2016-07-24 01:00:00+00
292	12	2016-07-24 05:00:00+00	2016-07-24 06:00:00+00
293	12	2016-07-24 10:00:00+00	2016-07-24 16:00:00+00
294	12	2016-07-24 16:00:00+00	2016-07-25 01:00:00+00
295	12	2016-07-25 05:00:00+00	2016-07-25 06:00:00+00
296	12	2016-07-25 10:00:00+00	2016-07-25 16:00:00+00
300	12	2016-07-26 16:00:00+00	2016-07-27 01:00:00+00
301	12	2016-07-27 05:00:00+00	2016-07-27 06:00:00+00
302	12	2016-07-27 10:00:00+00	2016-07-27 16:00:00+00
306	12	2016-07-29 10:00:00+00	2016-07-29 16:00:00+00
307	12	2016-07-29 05:00:00+00	2016-07-29 06:00:00+00
308	12	2016-07-28 16:00:00+00	2016-07-29 01:00:00+00
312	12	2016-07-30 16:00:00+00	2016-07-31 01:00:00+00
313	12	2016-07-31 05:00:00+00	2016-07-31 06:00:00+00
314	12	2016-07-31 10:00:00+00	2016-07-31 16:00:00+00
315	12	2016-08-01 05:00:00+00	2016-08-01 06:00:00+00
316	12	2016-07-31 16:00:00+00	2016-08-01 01:00:00+00
317	12	2016-08-01 10:00:00+00	2016-08-01 16:00:00+00
318	10	2016-07-18 10:00:00+00	2016-07-18 11:00:00+00
319	10	2016-07-18 15:00:00+00	2016-07-18 21:00:00+00
325	10	2016-07-20 15:00:00+00	2016-07-20 21:00:00+00
326	10	2016-07-19 21:00:00+00	2016-07-20 06:00:00+00
327	10	2016-07-20 10:00:00+00	2016-07-20 11:00:00+00
331	10	2016-07-21 21:00:00+00	2016-07-22 06:00:00+00
332	10	2016-07-22 10:00:00+00	2016-07-22 11:00:00+00
333	10	2016-07-22 15:00:00+00	2016-07-22 21:00:00+00
337	10	2016-07-24 15:00:00+00	2016-07-24 21:00:00+00
338	10	2016-07-23 21:00:00+00	2016-07-24 06:00:00+00
339	10	2016-07-24 10:00:00+00	2016-07-24 11:00:00+00
343	10	2016-07-26 10:00:00+00	2016-07-26 11:00:00+00
344	10	2016-07-26 15:00:00+00	2016-07-26 21:00:00+00
345	10	2016-07-25 21:00:00+00	2016-07-26 06:00:00+00
349	10	2016-07-28 10:00:00+00	2016-07-28 11:00:00+00
350	10	2016-07-27 21:00:00+00	2016-07-28 06:00:00+00
351	10	2016-07-28 15:00:00+00	2016-07-28 21:00:00+00
1972	9	2016-09-05 15:00:00+00	2016-09-05 21:00:00+00
1973	9	2016-09-06 15:00:00+00	2016-09-06 21:00:00+00
1974	9	2016-09-07 15:00:00+00	2016-09-07 21:00:00+00
1975	9	2016-09-08 15:00:00+00	2016-09-08 21:00:00+00
1976	9	2016-09-09 15:00:00+00	2016-09-09 21:00:00+00
1977	9	2016-09-10 15:00:00+00	2016-09-10 21:00:00+00
1978	9	2016-09-11 15:00:00+00	2016-09-11 21:00:00+00
1979	9	2016-09-12 15:00:00+00	2016-09-12 21:00:00+00
1980	9	2016-09-13 15:00:00+00	2016-09-13 21:00:00+00
1981	9	2016-09-14 15:00:00+00	2016-09-14 21:00:00+00
1982	9	2016-09-15 15:00:00+00	2016-09-15 21:00:00+00
1983	9	2016-09-16 15:00:00+00	2016-09-16 21:00:00+00
1984	9	2016-09-17 15:00:00+00	2016-09-17 21:00:00+00
1985	9	2016-09-18 15:00:00+00	2016-09-18 21:00:00+00
1986	9	2016-09-19 15:00:00+00	2016-09-19 21:00:00+00
1987	9	2016-09-20 15:00:00+00	2016-09-20 21:00:00+00
1988	9	2016-09-21 15:00:00+00	2016-09-21 21:00:00+00
1989	9	2016-09-22 15:00:00+00	2016-09-22 21:00:00+00
1990	9	2016-09-23 15:00:00+00	2016-09-23 21:00:00+00
1991	9	2016-09-24 15:00:00+00	2016-09-24 21:00:00+00
1992	9	2016-09-25 15:00:00+00	2016-09-25 21:00:00+00
1993	9	2016-09-26 15:00:00+00	2016-09-26 21:00:00+00
1994	9	2016-09-27 15:00:00+00	2016-09-27 21:00:00+00
1995	9	2016-09-28 15:00:00+00	2016-09-28 21:00:00+00
1996	9	2016-09-29 15:00:00+00	2016-09-29 21:00:00+00
1997	9	2016-09-30 15:00:00+00	2016-09-30 21:00:00+00
2333	14	2016-10-05 06:00:00+00	2016-10-05 06:30:00+00
2334	7	2016-09-30 12:00:00+00	2016-09-30 15:00:00+00
2336	7	2016-09-29 11:30:00+00	2016-09-29 12:00:00+00
2341	7	2016-09-30 06:30:00+00	2016-09-30 07:00:00+00
2343	7	2016-09-30 08:00:00+00	2016-09-30 09:00:00+00
2344	14	2016-09-30 09:00:00+00	2016-09-30 09:30:00+00
2345	7	2016-10-03 07:30:00+00	2016-10-03 08:00:00+00
2348	14	2016-10-03 09:00:00+00	2016-10-03 10:00:00+00
2349	14	2016-10-03 11:00:00+00	2016-10-03 12:00:00+00
2352	14	2016-10-03 07:00:00+00	2016-10-03 08:00:00+00
2353	7	2016-10-03 12:00:00+00	2016-10-03 13:00:00+00
2355	7	2016-10-05 11:00:00+00	2016-10-05 14:00:00+00
2359	11	2016-10-05 09:00:00+00	2016-10-05 09:30:00+00
2364	7	2016-10-05 08:30:00+00	2016-10-05 09:00:00+00
2367	7	2016-10-12 06:00:00+00	2016-10-12 06:30:00+00
2368	7	2016-10-05 09:30:00+00	2016-10-05 10:00:00+00
2370	10	2016-10-11 11:00:00+00	2016-10-11 13:00:00+00
2373	14	2016-10-07 09:00:00+00	2016-10-07 10:00:00+00
2374	7	2016-10-07 09:00:00+00	2016-10-07 10:00:00+00
2375	7	2016-10-07 11:00:00+00	2016-10-07 11:30:00+00
2376	7	2016-10-07 08:30:00+00	2016-10-07 09:00:00+00
2379	7	2016-10-10 09:30:00+00	2016-10-10 10:00:00+00
2380	7	2016-10-10 09:00:00+00	2016-10-10 09:30:00+00
2381	7	2016-10-10 11:00:00+00	2016-10-10 11:30:00+00
2386	8	2016-10-26 06:00:00+00	2016-10-26 06:30:00+00
2387	9	2016-10-25 06:00:00+00	2016-10-25 06:30:00+00
2388	7	2016-10-13 06:00:00+00	2016-10-13 09:00:00+00
2390	7	2016-10-13 11:00:00+00	2016-10-13 11:30:00+00
2396	11	2016-10-19 08:00:00+00	2016-10-19 08:30:00+00
2397	14	2016-10-14 11:00:00+00	2016-10-14 12:00:00+00
2400	7	2016-10-14 12:00:00+00	2016-10-14 13:00:00+00
2403	7	2016-10-14 09:00:00+00	2016-10-14 10:00:00+00
2405	14	2016-10-14 09:00:00+00	2016-10-14 09:30:00+00
2408	7	2016-10-17 12:00:00+00	2016-10-17 15:00:00+00
2409	7	2016-10-17 11:00:00+00	2016-10-17 12:00:00+00
2412	14	2016-10-17 08:30:00+00	2016-10-17 10:00:00+00
2415	14	2016-10-17 12:00:00+00	2016-10-17 12:30:00+00
2416	14	2016-10-17 14:00:00+00	2016-10-17 14:30:00+00
2418	7	2016-10-20 14:00:00+00	2016-10-20 15:00:00+00
2419	7	2016-10-19 11:00:00+00	2016-10-19 11:30:00+00
2421	7	2016-10-19 08:00:00+00	2016-10-19 08:30:00+00
2427	7	2016-10-20 07:00:00+00	2016-10-20 10:00:00+00
2433	15	2016-10-16 21:00:00+00	2016-10-17 06:00:00+00
2434	15	2016-10-17 15:00:00+00	2016-10-17 21:00:00+00
2435	15	2016-10-17 10:00:00+00	2016-10-17 11:00:00+00
2436	15	2016-10-19 10:00:00+00	2016-10-19 11:00:00+00
2437	15	2016-10-18 21:00:00+00	2016-10-19 06:00:00+00
2438	15	2016-10-19 15:00:00+00	2016-10-19 21:00:00+00
2445	15	2016-10-21 21:00:00+00	2016-10-22 21:00:00+00
2447	15	2016-10-24 10:00:00+00	2016-10-24 11:00:00+00
2448	15	2016-10-23 21:00:00+00	2016-10-24 06:00:00+00
2449	15	2016-10-24 15:00:00+00	2016-10-24 21:00:00+00
2453	15	2016-10-26 15:00:00+00	2016-10-26 21:00:00+00
2454	15	2016-10-26 10:00:00+00	2016-10-26 11:00:00+00
2455	15	2016-10-25 21:00:00+00	2016-10-26 06:00:00+00
2459	15	2016-10-28 10:00:00+00	2016-10-28 11:00:00+00
2460	15	2016-10-27 21:00:00+00	2016-10-28 06:00:00+00
2461	15	2016-10-28 15:00:00+00	2016-10-28 21:00:00+00
2463	15	2016-10-29 21:00:00+00	2016-10-30 21:00:00+00
2464	15	2016-10-31 15:00:00+00	2016-10-31 21:00:00+00
2465	15	2016-10-31 10:00:00+00	2016-10-31 11:00:00+00
2466	15	2016-10-30 21:00:00+00	2016-10-31 06:00:00+00
2470	15	2016-11-02 10:00:00+00	2016-11-02 11:00:00+00
2471	15	2016-11-01 21:00:00+00	2016-11-02 06:00:00+00
2472	15	2016-11-02 15:00:00+00	2016-11-02 21:00:00+00
2476	15	2016-11-04 15:00:00+00	2016-11-04 21:00:00+00
2477	15	2016-11-03 21:00:00+00	2016-11-04 06:00:00+00
2478	15	2016-11-04 10:00:00+00	2016-11-04 11:00:00+00
2480	15	2016-11-05 21:00:00+00	2016-11-06 21:00:00+00
2481	15	2016-10-17 21:00:00+00	2016-10-18 06:00:00+00
2482	15	2016-10-18 10:00:00+00	2016-10-18 11:00:00+00
2483	15	2016-10-18 15:00:00+00	2016-10-18 21:00:00+00
2486	7	2016-10-20 06:00:00+00	2016-10-20 06:30:00+00
2487	11	2016-10-26 08:00:00+00	2016-10-26 08:30:00+00
297	12	2016-07-25 16:00:00+00	2016-07-26 01:00:00+00
298	12	2016-07-26 10:00:00+00	2016-07-26 16:00:00+00
299	12	2016-07-26 05:00:00+00	2016-07-26 06:00:00+00
303	12	2016-07-28 05:00:00+00	2016-07-28 06:00:00+00
304	12	2016-07-28 10:00:00+00	2016-07-28 16:00:00+00
305	12	2016-07-27 16:00:00+00	2016-07-28 01:00:00+00
309	12	2016-07-29 16:00:00+00	2016-07-30 01:00:00+00
310	12	2016-07-30 05:00:00+00	2016-07-30 06:00:00+00
311	12	2016-07-30 10:00:00+00	2016-07-30 16:00:00+00
320	10	2016-07-18 21:00:00+00	2016-07-19 06:00:00+00
321	10	2016-07-19 15:00:00+00	2016-07-19 18:00:00+00
322	10	2016-07-19 10:00:00+00	2016-07-19 11:00:00+00
323	10	2016-07-19 18:30:00+00	2016-07-19 21:00:00+00
324	10	2016-07-19 18:00:00+00	2016-07-19 18:30:00+00
328	10	2016-07-21 10:00:00+00	2016-07-21 11:00:00+00
329	10	2016-07-21 15:00:00+00	2016-07-21 21:00:00+00
330	10	2016-07-20 21:00:00+00	2016-07-21 06:00:00+00
334	10	2016-07-23 10:00:00+00	2016-07-23 11:00:00+00
335	10	2016-07-23 15:00:00+00	2016-07-23 21:00:00+00
336	10	2016-07-22 21:00:00+00	2016-07-23 06:00:00+00
340	10	2016-07-25 10:00:00+00	2016-07-25 11:00:00+00
341	10	2016-07-24 21:00:00+00	2016-07-25 06:00:00+00
342	10	2016-07-25 15:00:00+00	2016-07-25 21:00:00+00
346	10	2016-07-26 21:00:00+00	2016-07-27 06:00:00+00
347	10	2016-07-27 10:00:00+00	2016-07-27 11:00:00+00
348	10	2016-07-27 15:00:00+00	2016-07-27 21:00:00+00
352	10	2016-07-29 15:00:00+00	2016-07-29 21:00:00+00
353	10	2016-07-29 10:00:00+00	2016-07-29 11:00:00+00
354	10	2016-07-28 21:00:00+00	2016-07-29 06:00:00+00
355	10	2016-07-29 21:00:00+00	2016-07-30 06:00:00+00
356	10	2016-07-30 10:00:00+00	2016-07-30 11:00:00+00
357	10	2016-07-30 15:00:00+00	2016-07-30 21:00:00+00
358	10	2016-07-30 21:00:00+00	2016-07-31 06:00:00+00
359	10	2016-07-31 10:00:00+00	2016-07-31 11:00:00+00
360	10	2016-07-31 15:00:00+00	2016-07-31 21:00:00+00
361	10	2016-07-31 21:00:00+00	2016-08-01 06:00:00+00
362	10	2016-08-01 15:00:00+00	2016-08-01 20:30:00+00
363	10	2016-08-01 10:00:00+00	2016-08-01 11:00:00+00
364	10	2016-08-01 20:30:00+00	2016-08-01 21:00:00+00
365	9	2016-07-18 10:00:00+00	2016-07-18 11:00:00+00
366	9	2016-07-18 15:00:00+00	2016-07-18 21:00:00+00
367	9	2016-07-19 10:00:00+00	2016-07-19 11:00:00+00
368	9	2016-07-18 21:00:00+00	2016-07-19 06:00:00+00
369	9	2016-07-19 15:00:00+00	2016-07-19 21:00:00+00
370	9	2016-07-20 15:00:00+00	2016-07-20 21:00:00+00
371	9	2016-07-20 10:00:00+00	2016-07-20 11:00:00+00
372	9	2016-07-19 21:00:00+00	2016-07-20 06:00:00+00
373	9	2016-07-20 21:00:00+00	2016-07-21 06:00:00+00
374	9	2016-07-21 15:00:00+00	2016-07-21 21:00:00+00
375	9	2016-07-21 10:00:00+00	2016-07-21 11:00:00+00
376	9	2016-07-22 10:00:00+00	2016-07-22 11:00:00+00
377	9	2016-07-22 15:00:00+00	2016-07-22 21:00:00+00
378	9	2016-07-21 21:00:00+00	2016-07-22 06:00:00+00
379	9	2016-07-23 15:00:00+00	2016-07-23 21:00:00+00
380	9	2016-07-22 21:00:00+00	2016-07-23 06:00:00+00
381	9	2016-07-23 10:00:00+00	2016-07-23 11:00:00+00
382	9	2016-07-23 21:00:00+00	2016-07-24 06:00:00+00
383	9	2016-07-24 10:00:00+00	2016-07-24 11:00:00+00
384	9	2016-07-24 15:00:00+00	2016-07-24 21:00:00+00
385	9	2016-07-24 21:00:00+00	2016-07-25 06:00:00+00
386	9	2016-07-25 10:00:00+00	2016-07-25 11:00:00+00
387	9	2016-07-25 15:00:00+00	2016-07-25 21:00:00+00
388	9	2016-07-26 15:00:00+00	2016-07-26 21:00:00+00
389	9	2016-07-25 21:00:00+00	2016-07-26 06:00:00+00
390	9	2016-07-26 10:00:00+00	2016-07-26 11:00:00+00
391	9	2016-07-27 10:00:00+00	2016-07-27 11:00:00+00
392	9	2016-07-27 00:00:00+00	2016-07-27 06:00:00+00
393	9	2016-07-26 21:00:00+00	2016-07-26 23:30:00+00
394	9	2016-07-27 15:00:00+00	2016-07-27 21:00:00+00
395	9	2016-07-28 15:00:00+00	2016-07-28 21:00:00+00
396	9	2016-07-27 21:00:00+00	2016-07-28 06:00:00+00
397	9	2016-07-28 10:00:00+00	2016-07-28 11:00:00+00
398	9	2016-07-28 21:00:00+00	2016-07-29 06:00:00+00
399	9	2016-07-29 10:00:00+00	2016-07-29 11:00:00+00
400	9	2016-07-29 15:00:00+00	2016-07-29 21:00:00+00
401	9	2016-07-29 21:00:00+00	2016-07-30 06:00:00+00
402	9	2016-07-30 15:00:00+00	2016-07-30 21:00:00+00
403	9	2016-07-30 10:00:00+00	2016-07-30 11:00:00+00
404	9	2016-07-30 21:00:00+00	2016-07-31 02:00:00+00
405	9	2016-07-31 15:00:00+00	2016-07-31 21:00:00+00
406	9	2016-07-31 02:30:00+00	2016-07-31 06:00:00+00
407	9	2016-07-31 10:00:00+00	2016-07-31 11:00:00+00
408	9	2016-07-31 02:00:00+00	2016-07-31 02:30:00+00
409	9	2016-08-01 10:00:00+00	2016-08-01 11:00:00+00
410	9	2016-07-31 21:00:00+00	2016-08-01 06:00:00+00
411	9	2016-08-01 15:00:00+00	2016-08-01 21:00:00+00
415	8	2016-07-18 15:00:00+00	2016-07-18 21:00:00+00
416	8	2016-07-18 10:00:00+00	2016-07-18 11:00:00+00
418	8	2016-07-18 21:00:00+00	2016-07-19 06:00:00+00
419	8	2016-07-19 15:00:00+00	2016-07-19 21:00:00+00
420	8	2016-07-19 10:00:00+00	2016-07-19 11:00:00+00
421	8	2016-07-19 21:00:00+00	2016-07-20 06:00:00+00
422	8	2016-07-20 10:00:00+00	2016-07-20 11:00:00+00
423	8	2016-07-20 15:00:00+00	2016-07-20 21:00:00+00
424	8	2016-07-21 15:00:00+00	2016-07-21 21:00:00+00
425	8	2016-07-20 21:00:00+00	2016-07-21 06:00:00+00
426	8	2016-07-21 10:00:00+00	2016-07-21 11:00:00+00
427	8	2016-07-21 21:00:00+00	2016-07-22 06:00:00+00
428	8	2016-07-22 10:00:00+00	2016-07-22 11:00:00+00
429	8	2016-07-22 15:00:00+00	2016-07-22 21:00:00+00
430	8	2016-07-22 21:00:00+00	2016-07-23 06:00:00+00
431	8	2016-07-23 10:00:00+00	2016-07-23 11:00:00+00
432	8	2016-07-23 15:00:00+00	2016-07-23 21:00:00+00
433	8	2016-07-23 21:00:00+00	2016-07-24 06:00:00+00
434	8	2016-07-24 15:00:00+00	2016-07-24 21:00:00+00
435	8	2016-07-24 10:00:00+00	2016-07-24 11:00:00+00
436	8	2016-07-25 10:00:00+00	2016-07-25 11:00:00+00
437	8	2016-07-24 21:00:00+00	2016-07-25 06:00:00+00
438	8	2016-07-25 15:00:00+00	2016-07-25 21:00:00+00
439	8	2016-07-25 21:00:00+00	2016-07-26 06:00:00+00
440	8	2016-07-26 10:00:00+00	2016-07-26 11:00:00+00
441	8	2016-07-26 15:00:00+00	2016-07-26 21:00:00+00
442	8	2016-07-27 10:00:00+00	2016-07-27 11:00:00+00
443	8	2016-07-26 21:00:00+00	2016-07-27 06:00:00+00
444	8	2016-07-27 15:00:00+00	2016-07-27 21:00:00+00
445	8	2016-07-28 10:00:00+00	2016-07-28 11:00:00+00
446	8	2016-07-28 15:00:00+00	2016-07-28 21:00:00+00
447	8	2016-07-27 21:00:00+00	2016-07-28 06:00:00+00
448	8	2016-07-29 15:00:00+00	2016-07-29 21:00:00+00
449	8	2016-07-28 21:00:00+00	2016-07-29 06:00:00+00
450	8	2016-07-29 10:00:00+00	2016-07-29 11:00:00+00
451	8	2016-07-30 15:00:00+00	2016-07-30 21:00:00+00
452	8	2016-07-29 21:00:00+00	2016-07-30 06:00:00+00
453	8	2016-07-30 10:00:00+00	2016-07-30 11:00:00+00
454	8	2016-07-30 21:00:00+00	2016-07-31 06:00:00+00
455	8	2016-07-31 15:00:00+00	2016-07-31 21:00:00+00
456	8	2016-07-31 10:00:00+00	2016-07-31 11:00:00+00
457	8	2016-08-01 15:00:00+00	2016-08-01 21:00:00+00
458	8	2016-07-31 21:00:00+00	2016-08-01 06:00:00+00
459	8	2016-08-01 10:00:00+00	2016-08-01 11:00:00+00
460	11	2016-07-18 08:00:00+00	2016-07-18 09:00:00+00
461	11	2016-07-19 08:00:00+00	2016-07-19 09:00:00+00
462	11	2016-07-19 13:00:00+00	2016-07-19 19:00:00+00
463	11	2016-07-18 19:00:00+00	2016-07-19 04:00:00+00
464	11	2016-07-20 08:00:00+00	2016-07-20 09:00:00+00
465	11	2016-07-19 19:00:00+00	2016-07-20 04:00:00+00
466	11	2016-07-20 13:00:00+00	2016-07-20 19:00:00+00
470	11	2016-07-22 08:00:00+00	2016-07-22 09:00:00+00
471	11	2016-07-21 19:00:00+00	2016-07-22 04:00:00+00
472	11	2016-07-22 13:00:00+00	2016-07-22 19:00:00+00
476	11	2016-07-23 19:00:00+00	2016-07-24 04:00:00+00
477	11	2016-07-24 08:00:00+00	2016-07-24 09:00:00+00
478	11	2016-07-24 13:00:00+00	2016-07-24 19:00:00+00
482	11	2016-07-25 19:00:00+00	2016-07-26 04:00:00+00
483	11	2016-07-26 13:00:00+00	2016-07-26 19:00:00+00
484	11	2016-07-26 08:00:00+00	2016-07-26 09:00:00+00
488	11	2016-07-28 13:00:00+00	2016-07-28 19:00:00+00
489	11	2016-07-27 19:00:00+00	2016-07-28 04:00:00+00
490	11	2016-07-28 08:00:00+00	2016-07-28 09:00:00+00
494	11	2016-07-30 08:00:00+00	2016-07-30 09:00:00+00
495	11	2016-07-29 19:00:00+00	2016-07-30 04:00:00+00
496	11	2016-07-30 13:00:00+00	2016-07-30 19:00:00+00
500	11	2016-07-31 19:00:00+00	2016-08-01 04:00:00+00
501	11	2016-08-01 08:00:00+00	2016-08-01 09:00:00+00
502	11	2016-08-01 13:00:00+00	2016-08-01 19:00:00+00
503	13	2016-07-18 06:00:00+00	2016-07-18 07:00:00+00
504	13	2016-07-18 11:00:00+00	2016-07-18 17:00:00+00
508	13	2016-07-19 17:00:00+00	2016-07-20 02:00:00+00
509	13	2016-07-20 11:00:00+00	2016-07-20 17:00:00+00
510	13	2016-07-20 06:00:00+00	2016-07-20 07:00:00+00
514	13	2016-07-22 11:00:00+00	2016-07-22 17:00:00+00
515	13	2016-07-21 17:00:00+00	2016-07-22 02:00:00+00
516	13	2016-07-22 06:00:00+00	2016-07-22 07:00:00+00
520	13	2016-07-24 11:00:00+00	2016-07-24 17:00:00+00
521	13	2016-07-23 17:00:00+00	2016-07-24 02:00:00+00
522	13	2016-07-24 06:00:00+00	2016-07-24 07:00:00+00
526	13	2016-07-25 17:00:00+00	2016-07-26 02:00:00+00
527	13	2016-07-26 06:00:00+00	2016-07-26 07:00:00+00
528	13	2016-07-26 11:00:00+00	2016-07-26 17:00:00+00
532	13	2016-07-27 17:00:00+00	2016-07-28 02:00:00+00
533	13	2016-07-28 11:00:00+00	2016-07-28 17:00:00+00
534	13	2016-07-28 06:00:00+00	2016-07-28 07:00:00+00
538	13	2016-07-30 11:00:00+00	2016-07-30 17:00:00+00
539	13	2016-07-29 17:00:00+00	2016-07-30 02:00:00+00
540	13	2016-07-30 06:00:00+00	2016-07-30 07:00:00+00
1998	10	2016-09-01 15:00:00+00	2016-09-01 21:00:00+00
1999	10	2016-09-02 15:00:00+00	2016-09-02 21:00:00+00
2000	10	2016-09-03 15:00:00+00	2016-09-03 21:00:00+00
2001	10	2016-09-04 15:00:00+00	2016-09-04 21:00:00+00
2002	10	2016-09-05 15:00:00+00	2016-09-05 21:00:00+00
2003	10	2016-09-06 15:00:00+00	2016-09-06 21:00:00+00
2004	10	2016-09-07 15:00:00+00	2016-09-07 21:00:00+00
2005	10	2016-09-08 15:00:00+00	2016-09-08 21:00:00+00
2006	10	2016-09-09 15:00:00+00	2016-09-09 21:00:00+00
2007	10	2016-09-10 15:00:00+00	2016-09-10 21:00:00+00
2008	10	2016-09-11 15:00:00+00	2016-09-11 21:00:00+00
2009	10	2016-09-12 15:00:00+00	2016-09-12 21:00:00+00
2010	10	2016-09-13 15:00:00+00	2016-09-13 21:00:00+00
2011	10	2016-09-14 15:00:00+00	2016-09-14 21:00:00+00
2012	10	2016-09-15 15:00:00+00	2016-09-15 21:00:00+00
2013	10	2016-09-16 15:00:00+00	2016-09-16 21:00:00+00
2014	10	2016-09-17 15:00:00+00	2016-09-17 21:00:00+00
2015	10	2016-09-18 15:00:00+00	2016-09-18 21:00:00+00
2016	10	2016-09-19 15:00:00+00	2016-09-19 21:00:00+00
2017	10	2016-09-20 15:00:00+00	2016-09-20 21:00:00+00
2018	10	2016-09-21 15:00:00+00	2016-09-21 21:00:00+00
2019	10	2016-09-22 15:00:00+00	2016-09-22 21:00:00+00
2020	10	2016-09-23 15:00:00+00	2016-09-23 21:00:00+00
2021	10	2016-09-24 15:00:00+00	2016-09-24 21:00:00+00
2022	10	2016-09-25 15:00:00+00	2016-09-25 21:00:00+00
2023	10	2016-09-26 15:00:00+00	2016-09-26 21:00:00+00
2024	10	2016-09-27 15:00:00+00	2016-09-27 21:00:00+00
2025	10	2016-09-28 15:00:00+00	2016-09-28 21:00:00+00
2026	10	2016-09-29 15:00:00+00	2016-09-29 21:00:00+00
2027	10	2016-09-30 15:00:00+00	2016-09-30 21:00:00+00
2028	8	2016-09-01 15:00:00+00	2016-09-01 21:00:00+00
2029	8	2016-09-02 15:00:00+00	2016-09-02 21:00:00+00
2030	8	2016-09-03 15:00:00+00	2016-09-03 21:00:00+00
2031	8	2016-09-04 15:00:00+00	2016-09-04 21:00:00+00
2032	8	2016-09-05 15:00:00+00	2016-09-05 21:00:00+00
2033	8	2016-09-06 15:00:00+00	2016-09-06 21:00:00+00
2034	8	2016-09-07 15:00:00+00	2016-09-07 21:00:00+00
2035	8	2016-09-08 15:00:00+00	2016-09-08 21:00:00+00
2036	8	2016-09-09 15:00:00+00	2016-09-09 21:00:00+00
2037	8	2016-09-10 15:00:00+00	2016-09-10 21:00:00+00
2038	8	2016-09-11 15:00:00+00	2016-09-11 21:00:00+00
2039	8	2016-09-12 15:00:00+00	2016-09-12 21:00:00+00
2040	8	2016-09-13 15:00:00+00	2016-09-13 21:00:00+00
2041	8	2016-09-14 15:00:00+00	2016-09-14 21:00:00+00
2042	8	2016-09-15 15:00:00+00	2016-09-15 21:00:00+00
2043	8	2016-09-16 15:00:00+00	2016-09-16 21:00:00+00
2044	8	2016-09-17 15:00:00+00	2016-09-17 21:00:00+00
2045	8	2016-09-18 15:00:00+00	2016-09-18 21:00:00+00
2046	8	2016-09-19 15:00:00+00	2016-09-19 21:00:00+00
2047	8	2016-09-20 15:00:00+00	2016-09-20 21:00:00+00
2048	8	2016-09-21 15:00:00+00	2016-09-21 21:00:00+00
2049	8	2016-09-22 15:00:00+00	2016-09-22 21:00:00+00
2050	8	2016-09-23 15:00:00+00	2016-09-23 21:00:00+00
2051	8	2016-09-24 15:00:00+00	2016-09-24 21:00:00+00
2052	8	2016-09-25 15:00:00+00	2016-09-25 21:00:00+00
2053	8	2016-09-26 15:00:00+00	2016-09-26 21:00:00+00
2054	8	2016-09-27 15:00:00+00	2016-09-27 21:00:00+00
2055	8	2016-09-28 15:00:00+00	2016-09-28 21:00:00+00
2056	8	2016-09-29 15:00:00+00	2016-09-29 21:00:00+00
2057	8	2016-09-30 15:00:00+00	2016-09-30 21:00:00+00
2058	12	2016-09-01 16:00:00+00	2016-09-02 01:00:00+00
2059	12	2016-09-02 16:00:00+00	2016-09-03 01:00:00+00
2060	12	2016-09-03 16:00:00+00	2016-09-04 01:00:00+00
2061	12	2016-09-04 16:00:00+00	2016-09-05 01:00:00+00
2062	12	2016-09-05 16:00:00+00	2016-09-06 01:00:00+00
2063	12	2016-09-06 16:00:00+00	2016-09-07 01:00:00+00
2064	12	2016-09-07 16:00:00+00	2016-09-08 01:00:00+00
2065	12	2016-09-08 16:00:00+00	2016-09-09 01:00:00+00
2066	12	2016-09-09 16:00:00+00	2016-09-10 01:00:00+00
2067	12	2016-09-10 16:00:00+00	2016-09-11 01:00:00+00
2068	12	2016-09-11 16:00:00+00	2016-09-12 01:00:00+00
2069	12	2016-09-12 16:00:00+00	2016-09-13 01:00:00+00
2070	12	2016-09-13 16:00:00+00	2016-09-14 01:00:00+00
2071	12	2016-09-14 16:00:00+00	2016-09-15 01:00:00+00
2072	12	2016-09-15 16:00:00+00	2016-09-16 01:00:00+00
2073	12	2016-09-16 16:00:00+00	2016-09-17 01:00:00+00
2074	12	2016-09-17 16:00:00+00	2016-09-18 01:00:00+00
2075	12	2016-09-18 16:00:00+00	2016-09-19 01:00:00+00
2076	12	2016-09-19 16:00:00+00	2016-09-20 01:00:00+00
2077	12	2016-09-20 16:00:00+00	2016-09-21 01:00:00+00
2078	12	2016-09-21 16:00:00+00	2016-09-22 01:00:00+00
2079	12	2016-09-22 16:00:00+00	2016-09-23 01:00:00+00
2080	12	2016-09-23 16:00:00+00	2016-09-24 01:00:00+00
2081	12	2016-09-24 16:00:00+00	2016-09-25 01:00:00+00
2082	12	2016-09-25 16:00:00+00	2016-09-26 01:00:00+00
2083	12	2016-09-26 16:00:00+00	2016-09-27 01:00:00+00
2084	12	2016-09-27 16:00:00+00	2016-09-28 01:00:00+00
2085	12	2016-09-28 16:00:00+00	2016-09-29 01:00:00+00
2086	12	2016-09-29 16:00:00+00	2016-09-30 01:00:00+00
2087	12	2016-09-30 16:00:00+00	2016-10-01 01:00:00+00
2088	13	2016-09-01 17:00:00+00	2016-09-02 02:00:00+00
2089	13	2016-09-02 17:00:00+00	2016-09-03 02:00:00+00
2090	13	2016-09-03 17:00:00+00	2016-09-04 02:00:00+00
2091	13	2016-09-04 17:00:00+00	2016-09-05 02:00:00+00
2092	13	2016-09-05 17:00:00+00	2016-09-06 02:00:00+00
2093	13	2016-09-06 17:00:00+00	2016-09-07 02:00:00+00
2094	13	2016-09-07 17:00:00+00	2016-09-08 02:00:00+00
467	11	2016-07-21 08:00:00+00	2016-07-21 09:00:00+00
468	11	2016-07-21 13:00:00+00	2016-07-21 19:00:00+00
469	11	2016-07-20 19:00:00+00	2016-07-21 04:00:00+00
473	11	2016-07-23 13:00:00+00	2016-07-23 19:00:00+00
474	11	2016-07-22 19:00:00+00	2016-07-23 04:00:00+00
475	11	2016-07-23 08:00:00+00	2016-07-23 09:00:00+00
479	11	2016-07-25 08:00:00+00	2016-07-25 09:00:00+00
480	11	2016-07-24 19:00:00+00	2016-07-25 04:00:00+00
481	11	2016-07-25 13:00:00+00	2016-07-25 19:00:00+00
485	11	2016-07-26 19:00:00+00	2016-07-27 04:00:00+00
486	11	2016-07-27 08:00:00+00	2016-07-27 09:00:00+00
487	11	2016-07-27 13:00:00+00	2016-07-27 19:00:00+00
491	11	2016-07-29 13:00:00+00	2016-07-29 19:00:00+00
492	11	2016-07-28 19:00:00+00	2016-07-29 04:00:00+00
493	11	2016-07-29 08:00:00+00	2016-07-29 09:00:00+00
497	11	2016-07-31 08:00:00+00	2016-07-31 09:00:00+00
498	11	2016-07-31 13:00:00+00	2016-07-31 19:00:00+00
499	11	2016-07-30 19:00:00+00	2016-07-31 04:00:00+00
505	13	2016-07-18 17:00:00+00	2016-07-19 02:00:00+00
506	13	2016-07-19 06:00:00+00	2016-07-19 07:00:00+00
507	13	2016-07-19 11:00:00+00	2016-07-19 17:00:00+00
511	13	2016-07-21 11:00:00+00	2016-07-21 17:00:00+00
512	13	2016-07-21 06:00:00+00	2016-07-21 07:00:00+00
513	13	2016-07-20 17:00:00+00	2016-07-21 02:00:00+00
517	13	2016-07-23 11:00:00+00	2016-07-23 17:00:00+00
518	13	2016-07-22 17:00:00+00	2016-07-23 02:00:00+00
519	13	2016-07-23 06:00:00+00	2016-07-23 07:00:00+00
523	13	2016-07-25 11:00:00+00	2016-07-25 17:00:00+00
524	13	2016-07-25 06:00:00+00	2016-07-25 07:00:00+00
525	13	2016-07-24 17:00:00+00	2016-07-25 02:00:00+00
529	13	2016-07-27 06:00:00+00	2016-07-27 07:00:00+00
530	13	2016-07-26 17:00:00+00	2016-07-27 02:00:00+00
531	13	2016-07-27 11:00:00+00	2016-07-27 17:00:00+00
535	13	2016-07-28 17:00:00+00	2016-07-29 02:00:00+00
536	13	2016-07-29 06:00:00+00	2016-07-29 07:00:00+00
537	13	2016-07-29 11:00:00+00	2016-07-29 17:00:00+00
541	13	2016-07-31 11:00:00+00	2016-07-31 17:00:00+00
542	13	2016-07-30 17:00:00+00	2016-07-31 02:00:00+00
543	13	2016-07-31 06:00:00+00	2016-07-31 07:00:00+00
544	13	2016-07-31 17:00:00+00	2016-08-01 02:00:00+00
545	13	2016-08-01 06:00:00+00	2016-08-01 07:00:00+00
546	13	2016-08-01 11:00:00+00	2016-08-01 17:00:00+00
555	7	2016-07-21 09:00:00+00	2016-07-21 10:00:00+00
556	7	2016-07-21 11:00:00+00	2016-07-21 12:00:00+00
270	7	2016-08-01 10:30:00+00	2016-08-01 11:00:00+00
558	7	2016-08-01 10:00:00+00	2016-08-01 10:30:00+00
560	7	2016-07-28 09:00:00+00	2016-07-28 10:00:00+00
561	7	2016-08-01 09:00:00+00	2016-08-01 10:00:00+00
562	7	2016-07-29 07:30:00+00	2016-07-29 08:00:00+00
567	7	2016-08-02 15:00:00+00	2016-08-02 21:00:00+00
568	7	2016-08-02 10:00:00+00	2016-08-02 11:00:00+00
569	7	2016-08-01 21:00:00+00	2016-08-02 06:00:00+00
570	7	2016-08-02 21:00:00+00	2016-08-03 06:00:00+00
571	7	2016-08-03 15:00:00+00	2016-08-03 21:00:00+00
572	7	2016-08-03 10:00:00+00	2016-08-03 11:00:00+00
573	11	2016-08-03 19:00:00+00	2016-08-04 04:00:00+00
574	11	2016-08-04 19:00:00+00	2016-08-05 04:00:00+00
575	11	2016-08-05 19:00:00+00	2016-08-06 04:00:00+00
576	11	2016-08-06 19:00:00+00	2016-08-07 04:00:00+00
577	11	2016-08-07 19:00:00+00	2016-08-08 04:00:00+00
578	11	2016-08-08 19:00:00+00	2016-08-09 04:00:00+00
579	11	2016-08-09 19:00:00+00	2016-08-10 04:00:00+00
580	11	2016-08-10 19:00:00+00	2016-08-11 04:00:00+00
581	11	2016-08-11 19:00:00+00	2016-08-12 04:00:00+00
582	11	2016-08-12 19:00:00+00	2016-08-13 04:00:00+00
583	11	2016-08-13 19:00:00+00	2016-08-14 04:00:00+00
584	11	2016-08-14 19:00:00+00	2016-08-15 04:00:00+00
585	11	2016-08-15 19:00:00+00	2016-08-16 04:00:00+00
586	11	2016-08-16 19:00:00+00	2016-08-17 04:00:00+00
587	11	2016-08-17 19:00:00+00	2016-08-18 04:00:00+00
588	7	2016-08-03 21:00:00+00	2016-08-04 06:00:00+00
589	7	2016-08-04 21:00:00+00	2016-08-05 06:00:00+00
590	7	2016-08-05 21:00:00+00	2016-08-06 06:00:00+00
591	7	2016-08-06 21:00:00+00	2016-08-07 06:00:00+00
592	7	2016-08-07 21:00:00+00	2016-08-08 06:00:00+00
593	7	2016-08-08 21:00:00+00	2016-08-09 06:00:00+00
594	7	2016-08-09 21:00:00+00	2016-08-10 06:00:00+00
595	7	2016-08-10 21:00:00+00	2016-08-11 06:00:00+00
596	7	2016-08-11 21:00:00+00	2016-08-12 06:00:00+00
597	7	2016-08-12 21:00:00+00	2016-08-13 06:00:00+00
598	7	2016-08-13 21:00:00+00	2016-08-14 06:00:00+00
599	7	2016-08-14 21:00:00+00	2016-08-15 06:00:00+00
600	7	2016-08-15 21:00:00+00	2016-08-16 06:00:00+00
601	7	2016-08-16 21:00:00+00	2016-08-17 06:00:00+00
602	7	2016-08-17 21:00:00+00	2016-08-18 06:00:00+00
603	8	2016-08-03 21:00:00+00	2016-08-04 06:00:00+00
604	8	2016-08-04 21:00:00+00	2016-08-05 06:00:00+00
605	8	2016-08-05 21:00:00+00	2016-08-06 06:00:00+00
606	8	2016-08-06 21:00:00+00	2016-08-07 06:00:00+00
607	8	2016-08-07 21:00:00+00	2016-08-08 06:00:00+00
608	8	2016-08-08 21:00:00+00	2016-08-09 06:00:00+00
609	8	2016-08-09 21:00:00+00	2016-08-10 06:00:00+00
610	8	2016-08-10 21:00:00+00	2016-08-11 06:00:00+00
611	8	2016-08-11 21:00:00+00	2016-08-12 06:00:00+00
612	8	2016-08-12 21:00:00+00	2016-08-13 06:00:00+00
613	8	2016-08-13 21:00:00+00	2016-08-14 06:00:00+00
614	8	2016-08-14 21:00:00+00	2016-08-15 06:00:00+00
615	8	2016-08-15 21:00:00+00	2016-08-16 06:00:00+00
616	8	2016-08-16 21:00:00+00	2016-08-17 06:00:00+00
617	8	2016-08-17 21:00:00+00	2016-08-18 06:00:00+00
618	10	2016-08-03 21:00:00+00	2016-08-04 06:00:00+00
619	10	2016-08-04 21:00:00+00	2016-08-05 06:00:00+00
620	10	2016-08-05 21:00:00+00	2016-08-06 06:00:00+00
621	10	2016-08-06 21:00:00+00	2016-08-07 06:00:00+00
622	10	2016-08-07 21:00:00+00	2016-08-08 06:00:00+00
623	10	2016-08-08 21:00:00+00	2016-08-09 06:00:00+00
624	10	2016-08-09 21:00:00+00	2016-08-10 06:00:00+00
625	10	2016-08-10 21:00:00+00	2016-08-11 06:00:00+00
626	10	2016-08-11 21:00:00+00	2016-08-12 06:00:00+00
627	10	2016-08-12 21:00:00+00	2016-08-13 06:00:00+00
628	10	2016-08-13 21:00:00+00	2016-08-14 06:00:00+00
629	10	2016-08-14 21:00:00+00	2016-08-15 06:00:00+00
630	10	2016-08-15 21:00:00+00	2016-08-16 06:00:00+00
631	10	2016-08-16 21:00:00+00	2016-08-17 06:00:00+00
632	10	2016-08-17 21:00:00+00	2016-08-18 06:00:00+00
633	9	2016-08-03 21:00:00+00	2016-08-04 06:00:00+00
634	9	2016-08-04 21:00:00+00	2016-08-05 06:00:00+00
635	9	2016-08-05 21:00:00+00	2016-08-06 06:00:00+00
636	9	2016-08-06 21:00:00+00	2016-08-07 06:00:00+00
637	9	2016-08-07 21:00:00+00	2016-08-08 06:00:00+00
638	9	2016-08-08 21:00:00+00	2016-08-09 06:00:00+00
639	9	2016-08-09 21:00:00+00	2016-08-10 06:00:00+00
640	9	2016-08-10 21:00:00+00	2016-08-11 06:00:00+00
641	9	2016-08-11 21:00:00+00	2016-08-12 06:00:00+00
642	9	2016-08-12 21:00:00+00	2016-08-13 06:00:00+00
643	9	2016-08-13 21:00:00+00	2016-08-14 06:00:00+00
644	9	2016-08-14 21:00:00+00	2016-08-15 06:00:00+00
645	9	2016-08-15 21:00:00+00	2016-08-16 06:00:00+00
646	9	2016-08-16 21:00:00+00	2016-08-17 06:00:00+00
647	9	2016-08-17 21:00:00+00	2016-08-18 06:00:00+00
648	13	2016-08-03 17:00:00+00	2016-08-04 02:00:00+00
649	13	2016-08-04 17:00:00+00	2016-08-05 02:00:00+00
650	13	2016-08-05 17:00:00+00	2016-08-06 02:00:00+00
651	13	2016-08-06 17:00:00+00	2016-08-07 02:00:00+00
652	13	2016-08-07 17:00:00+00	2016-08-08 02:00:00+00
653	13	2016-08-08 17:00:00+00	2016-08-09 02:00:00+00
654	13	2016-08-09 17:00:00+00	2016-08-10 02:00:00+00
655	13	2016-08-10 17:00:00+00	2016-08-11 02:00:00+00
656	13	2016-08-11 17:00:00+00	2016-08-12 02:00:00+00
657	13	2016-08-12 17:00:00+00	2016-08-13 02:00:00+00
658	13	2016-08-13 17:00:00+00	2016-08-14 02:00:00+00
659	13	2016-08-14 17:00:00+00	2016-08-15 02:00:00+00
660	13	2016-08-15 17:00:00+00	2016-08-16 02:00:00+00
661	13	2016-08-16 17:00:00+00	2016-08-17 02:00:00+00
662	13	2016-08-17 17:00:00+00	2016-08-18 02:00:00+00
2095	13	2016-09-08 17:00:00+00	2016-09-09 02:00:00+00
2096	13	2016-09-09 17:00:00+00	2016-09-10 02:00:00+00
2097	13	2016-09-10 17:00:00+00	2016-09-11 02:00:00+00
2098	13	2016-09-11 17:00:00+00	2016-09-12 02:00:00+00
2099	13	2016-09-12 17:00:00+00	2016-09-13 02:00:00+00
2100	13	2016-09-13 17:00:00+00	2016-09-14 02:00:00+00
2101	13	2016-09-14 17:00:00+00	2016-09-15 02:00:00+00
2102	13	2016-09-15 17:00:00+00	2016-09-16 02:00:00+00
2103	13	2016-09-16 17:00:00+00	2016-09-17 02:00:00+00
2104	13	2016-09-17 17:00:00+00	2016-09-18 02:00:00+00
2105	13	2016-09-18 17:00:00+00	2016-09-19 02:00:00+00
2106	13	2016-09-19 17:00:00+00	2016-09-20 02:00:00+00
2107	13	2016-09-20 17:00:00+00	2016-09-21 02:00:00+00
2108	13	2016-09-21 17:00:00+00	2016-09-22 02:00:00+00
2109	13	2016-09-22 17:00:00+00	2016-09-23 02:00:00+00
680	7	2016-08-04 15:00:00+00	2016-08-04 21:00:00+00
681	7	2016-08-05 15:00:00+00	2016-08-05 21:00:00+00
682	7	2016-08-06 15:00:00+00	2016-08-06 21:00:00+00
683	7	2016-08-07 15:00:00+00	2016-08-07 21:00:00+00
684	7	2016-08-08 15:00:00+00	2016-08-08 21:00:00+00
685	7	2016-08-09 15:00:00+00	2016-08-09 21:00:00+00
686	7	2016-08-10 15:00:00+00	2016-08-10 21:00:00+00
687	7	2016-08-11 15:00:00+00	2016-08-11 21:00:00+00
688	7	2016-08-12 15:00:00+00	2016-08-12 21:00:00+00
689	7	2016-08-13 15:00:00+00	2016-08-13 21:00:00+00
690	7	2016-08-14 15:00:00+00	2016-08-14 21:00:00+00
691	7	2016-08-15 15:00:00+00	2016-08-15 21:00:00+00
692	7	2016-08-16 15:00:00+00	2016-08-16 21:00:00+00
693	8	2016-08-03 15:00:00+00	2016-08-03 21:00:00+00
694	8	2016-08-04 15:00:00+00	2016-08-04 21:00:00+00
695	8	2016-08-05 15:00:00+00	2016-08-05 21:00:00+00
696	8	2016-08-06 15:00:00+00	2016-08-06 21:00:00+00
697	8	2016-08-07 15:00:00+00	2016-08-07 21:00:00+00
698	8	2016-08-08 15:00:00+00	2016-08-08 21:00:00+00
699	8	2016-08-09 15:00:00+00	2016-08-09 21:00:00+00
700	8	2016-08-10 15:00:00+00	2016-08-10 21:00:00+00
701	8	2016-08-11 15:00:00+00	2016-08-11 21:00:00+00
702	8	2016-08-12 15:00:00+00	2016-08-12 21:00:00+00
703	8	2016-08-13 15:00:00+00	2016-08-13 21:00:00+00
704	8	2016-08-14 15:00:00+00	2016-08-14 21:00:00+00
705	8	2016-08-15 15:00:00+00	2016-08-15 21:00:00+00
706	8	2016-08-16 15:00:00+00	2016-08-16 21:00:00+00
707	10	2016-08-03 15:00:00+00	2016-08-03 21:00:00+00
708	10	2016-08-04 15:00:00+00	2016-08-04 21:00:00+00
709	10	2016-08-05 15:00:00+00	2016-08-05 21:00:00+00
710	10	2016-08-06 15:00:00+00	2016-08-06 21:00:00+00
711	10	2016-08-07 15:00:00+00	2016-08-07 21:00:00+00
712	10	2016-08-08 15:00:00+00	2016-08-08 21:00:00+00
713	10	2016-08-09 15:00:00+00	2016-08-09 21:00:00+00
714	10	2016-08-10 15:00:00+00	2016-08-10 21:00:00+00
715	10	2016-08-11 15:00:00+00	2016-08-11 21:00:00+00
716	10	2016-08-12 15:00:00+00	2016-08-12 21:00:00+00
717	10	2016-08-13 15:00:00+00	2016-08-13 21:00:00+00
718	10	2016-08-14 15:00:00+00	2016-08-14 21:00:00+00
719	10	2016-08-15 15:00:00+00	2016-08-15 21:00:00+00
720	10	2016-08-16 15:00:00+00	2016-08-16 21:00:00+00
721	9	2016-08-03 15:00:00+00	2016-08-03 21:00:00+00
722	9	2016-08-04 15:00:00+00	2016-08-04 21:00:00+00
723	9	2016-08-05 15:00:00+00	2016-08-05 21:00:00+00
724	9	2016-08-06 15:00:00+00	2016-08-06 21:00:00+00
725	9	2016-08-07 15:00:00+00	2016-08-07 21:00:00+00
726	9	2016-08-08 15:00:00+00	2016-08-08 21:00:00+00
727	9	2016-08-09 15:00:00+00	2016-08-09 21:00:00+00
728	9	2016-08-10 15:00:00+00	2016-08-10 21:00:00+00
729	9	2016-08-11 15:00:00+00	2016-08-11 21:00:00+00
730	9	2016-08-12 15:00:00+00	2016-08-12 21:00:00+00
731	9	2016-08-13 15:00:00+00	2016-08-13 21:00:00+00
732	9	2016-08-14 15:00:00+00	2016-08-14 21:00:00+00
733	9	2016-08-15 15:00:00+00	2016-08-15 21:00:00+00
734	9	2016-08-16 15:00:00+00	2016-08-16 21:00:00+00
735	12	2016-08-03 16:00:00+00	2016-08-04 01:00:00+00
736	12	2016-08-04 16:00:00+00	2016-08-05 01:00:00+00
737	12	2016-08-05 16:00:00+00	2016-08-06 01:00:00+00
738	12	2016-08-06 16:00:00+00	2016-08-07 01:00:00+00
739	12	2016-08-07 16:00:00+00	2016-08-08 01:00:00+00
740	12	2016-08-08 16:00:00+00	2016-08-09 01:00:00+00
741	12	2016-08-09 16:00:00+00	2016-08-10 01:00:00+00
742	12	2016-08-10 16:00:00+00	2016-08-11 01:00:00+00
743	12	2016-08-11 16:00:00+00	2016-08-12 01:00:00+00
744	12	2016-08-12 16:00:00+00	2016-08-13 01:00:00+00
745	12	2016-08-13 16:00:00+00	2016-08-14 01:00:00+00
746	12	2016-08-14 16:00:00+00	2016-08-15 01:00:00+00
747	12	2016-08-15 16:00:00+00	2016-08-16 01:00:00+00
748	12	2016-08-16 16:00:00+00	2016-08-17 01:00:00+00
749	12	2016-08-17 16:00:00+00	2016-08-18 01:00:00+00
753	11	2016-08-05 13:00:00+00	2016-08-05 19:00:00+00
754	11	2016-08-03 15:00:00+00	2016-08-03 19:00:00+00
755	11	2016-08-04 15:00:00+00	2016-08-04 19:00:00+00
756	11	2016-08-06 15:00:00+00	2016-08-06 19:00:00+00
757	11	2016-08-07 15:00:00+00	2016-08-07 19:00:00+00
758	11	2016-08-08 15:00:00+00	2016-08-08 19:00:00+00
759	11	2016-08-09 15:00:00+00	2016-08-09 19:00:00+00
760	11	2016-08-10 15:00:00+00	2016-08-10 19:00:00+00
761	11	2016-08-11 15:00:00+00	2016-08-11 19:00:00+00
762	11	2016-08-12 15:00:00+00	2016-08-12 19:00:00+00
763	11	2016-08-13 15:00:00+00	2016-08-13 19:00:00+00
764	11	2016-08-14 15:00:00+00	2016-08-14 19:00:00+00
765	11	2016-08-15 15:00:00+00	2016-08-15 19:00:00+00
766	11	2016-08-16 15:00:00+00	2016-08-16 19:00:00+00
767	11	2016-08-17 15:00:00+00	2016-08-17 19:00:00+00
768	11	2016-08-03 13:00:00+00	2016-08-03 15:00:00+00
769	11	2016-08-04 13:00:00+00	2016-08-04 15:00:00+00
770	11	2016-08-06 13:00:00+00	2016-08-06 15:00:00+00
771	11	2016-08-07 13:00:00+00	2016-08-07 15:00:00+00
772	11	2016-08-08 13:00:00+00	2016-08-08 15:00:00+00
773	11	2016-08-09 13:00:00+00	2016-08-09 15:00:00+00
774	11	2016-08-10 13:00:00+00	2016-08-10 15:00:00+00
775	11	2016-08-11 13:00:00+00	2016-08-11 15:00:00+00
776	11	2016-08-12 13:00:00+00	2016-08-12 15:00:00+00
777	11	2016-08-13 13:00:00+00	2016-08-13 15:00:00+00
778	11	2016-08-14 13:00:00+00	2016-08-14 15:00:00+00
779	11	2016-08-15 13:00:00+00	2016-08-15 15:00:00+00
780	11	2016-08-16 13:00:00+00	2016-08-16 15:00:00+00
781	11	2016-08-17 13:00:00+00	2016-08-17 15:00:00+00
783	13	2016-08-03 11:00:00+00	2016-08-03 17:00:00+00
784	13	2016-08-04 11:00:00+00	2016-08-04 17:00:00+00
785	13	2016-08-05 11:00:00+00	2016-08-05 17:00:00+00
786	13	2016-08-06 11:00:00+00	2016-08-06 17:00:00+00
787	13	2016-08-07 11:00:00+00	2016-08-07 17:00:00+00
788	13	2016-08-08 11:00:00+00	2016-08-08 17:00:00+00
789	13	2016-08-09 11:00:00+00	2016-08-09 17:00:00+00
790	13	2016-08-10 11:00:00+00	2016-08-10 17:00:00+00
791	13	2016-08-11 11:00:00+00	2016-08-11 17:00:00+00
792	13	2016-08-12 11:00:00+00	2016-08-12 17:00:00+00
793	13	2016-08-13 11:00:00+00	2016-08-13 17:00:00+00
794	13	2016-08-14 11:00:00+00	2016-08-14 17:00:00+00
795	13	2016-08-15 11:00:00+00	2016-08-15 17:00:00+00
796	13	2016-08-16 11:00:00+00	2016-08-16 17:00:00+00
797	13	2016-08-17 11:00:00+00	2016-08-17 17:00:00+00
2110	13	2016-09-23 17:00:00+00	2016-09-24 02:00:00+00
801	7	2016-08-04 09:00:00+00	2016-08-04 10:00:00+00
803	7	2016-08-04 08:00:00+00	2016-08-04 09:00:00+00
806	7	2016-08-04 12:30:00+00	2016-08-04 13:00:00+00
811	7	2016-08-08 06:00:00+00	2016-08-08 07:00:00+00
813	7	2016-08-08 07:00:00+00	2016-08-08 09:00:00+00
816	7	2016-08-08 10:00:00+00	2016-08-08 11:00:00+00
817	7	2016-08-08 11:00:00+00	2016-08-08 12:00:00+00
818	7	2016-08-09 10:00:00+00	2016-08-09 10:30:00+00
819	7	2016-08-09 10:30:00+00	2016-08-09 11:00:00+00
820	7	2016-08-10 10:00:00+00	2016-08-10 10:30:00+00
821	7	2016-08-10 10:30:00+00	2016-08-10 11:00:00+00
822	7	2016-08-11 10:00:00+00	2016-08-11 10:30:00+00
823	7	2016-08-11 10:30:00+00	2016-08-11 11:00:00+00
824	7	2016-08-12 10:00:00+00	2016-08-12 10:30:00+00
825	7	2016-08-12 10:30:00+00	2016-08-12 11:00:00+00
826	7	2016-08-09 06:00:00+00	2016-08-09 10:00:00+00
827	7	2016-08-09 11:00:00+00	2016-08-09 15:00:00+00
828	7	2016-08-16 06:00:00+00	2016-08-16 15:00:00+00
829	7	2016-08-17 15:00:00+00	2016-08-17 21:00:00+00
830	7	2016-08-18 15:00:00+00	2016-08-18 21:00:00+00
832	7	2016-08-11 09:00:00+00	2016-08-11 10:00:00+00
833	7	2016-08-11 08:00:00+00	2016-08-11 09:00:00+00
834	7	2016-08-11 11:00:00+00	2016-08-11 11:30:00+00
837	7	2016-08-15 09:00:00+00	2016-08-15 10:00:00+00
839	7	2016-08-15 12:00:00+00	2016-08-15 13:00:00+00
840	7	2016-08-15 06:00:00+00	2016-08-15 07:00:00+00
841	7	2016-08-15 10:00:00+00	2016-08-15 10:30:00+00
842	7	2016-08-17 10:00:00+00	2016-08-17 10:30:00+00
843	7	2016-08-18 10:00:00+00	2016-08-18 10:30:00+00
844	7	2016-08-19 10:00:00+00	2016-08-19 10:30:00+00
845	7	2016-08-18 21:00:00+00	2016-08-19 03:00:00+00
846	7	2016-08-19 15:00:00+00	2016-08-19 21:00:00+00
847	7	2016-08-19 21:00:00+00	2016-08-20 21:00:00+00
848	7	2016-08-20 21:00:00+00	2016-08-21 21:00:00+00
849	7	2016-08-19 03:00:00+00	2016-08-19 06:00:00+00
850	7	2016-08-18 10:30:00+00	2016-08-18 11:00:00+00
851	7	2016-08-18 09:00:00+00	2016-08-18 10:00:00+00
852	7	2016-08-22 06:00:00+00	2016-08-22 07:00:00+00
854	7	2016-08-22 08:30:00+00	2016-08-22 09:00:00+00
856	11	2016-08-25 04:00:00+00	2016-08-25 09:00:00+00
858	7	2016-08-25 11:00:00+00	2016-08-25 12:00:00+00
859	7	2016-08-25 09:00:00+00	2016-08-25 10:30:00+00
860	7	2016-08-26 09:00:00+00	2016-08-26 10:00:00+00
861	7	2016-08-26 10:00:00+00	2016-08-26 10:30:00+00
862	7	2016-08-26 11:30:00+00	2016-08-26 13:00:00+00
863	7	2016-08-29 06:00:00+00	2016-08-29 07:00:00+00
864	7	2016-08-29 11:00:00+00	2016-08-29 12:00:00+00
865	7	2016-08-29 12:00:00+00	2016-08-29 13:00:00+00
869	7	2016-08-22 15:00:00+00	2016-08-22 21:00:00+00
870	7	2016-08-21 21:00:00+00	2016-08-22 06:00:00+00
871	7	2016-08-22 10:00:00+00	2016-08-22 11:00:00+00
872	7	2016-08-22 21:00:00+00	2016-08-23 06:00:00+00
873	7	2016-08-23 10:00:00+00	2016-08-23 11:00:00+00
874	7	2016-08-23 15:00:00+00	2016-08-23 21:00:00+00
875	7	2016-08-24 15:00:00+00	2016-08-24 21:00:00+00
876	7	2016-08-23 21:00:00+00	2016-08-24 06:00:00+00
877	7	2016-08-24 10:00:00+00	2016-08-24 11:00:00+00
878	7	2016-08-25 15:00:00+00	2016-08-25 21:00:00+00
879	7	2016-08-25 10:30:00+00	2016-08-25 11:00:00+00
880	7	2016-08-24 21:00:00+00	2016-08-25 06:00:00+00
881	7	2016-08-25 21:00:00+00	2016-08-26 06:00:00+00
882	7	2016-08-26 15:00:00+00	2016-08-26 21:00:00+00
883	7	2016-08-26 21:00:00+00	2016-08-27 06:00:00+00
884	7	2016-08-27 10:00:00+00	2016-08-27 11:00:00+00
885	7	2016-08-27 15:00:00+00	2016-08-27 21:00:00+00
886	7	2016-08-27 21:00:00+00	2016-08-28 06:00:00+00
887	7	2016-08-28 10:00:00+00	2016-08-28 11:00:00+00
888	7	2016-08-28 15:00:00+00	2016-08-28 21:00:00+00
889	7	2016-08-28 21:00:00+00	2016-08-29 06:00:00+00
890	7	2016-08-29 15:00:00+00	2016-08-29 21:00:00+00
891	7	2016-08-29 10:00:00+00	2016-08-29 11:00:00+00
892	7	2016-08-29 21:00:00+00	2016-08-30 06:00:00+00
893	7	2016-08-30 15:00:00+00	2016-08-30 21:00:00+00
894	7	2016-08-30 10:00:00+00	2016-08-30 11:00:00+00
895	7	2016-08-31 15:00:00+00	2016-08-31 21:00:00+00
896	7	2016-08-30 21:00:00+00	2016-08-31 06:00:00+00
897	7	2016-08-31 10:00:00+00	2016-08-31 11:00:00+00
898	7	2016-08-31 21:00:00+00	2016-09-01 06:00:00+00
899	7	2016-09-01 15:00:00+00	2016-09-01 21:00:00+00
900	7	2016-09-01 10:00:00+00	2016-09-01 11:00:00+00
901	7	2016-09-02 10:00:00+00	2016-09-02 11:00:00+00
902	7	2016-09-02 15:00:00+00	2016-09-02 21:00:00+00
903	7	2016-09-01 21:00:00+00	2016-09-02 06:00:00+00
904	7	2016-09-03 10:00:00+00	2016-09-03 11:00:00+00
905	7	2016-09-02 21:00:00+00	2016-09-03 06:00:00+00
906	7	2016-09-03 15:00:00+00	2016-09-03 21:00:00+00
907	7	2016-09-03 21:00:00+00	2016-09-04 06:00:00+00
908	7	2016-09-04 15:00:00+00	2016-09-04 21:00:00+00
909	7	2016-09-04 10:00:00+00	2016-09-04 11:00:00+00
910	7	2016-09-05 15:00:00+00	2016-09-05 21:00:00+00
911	7	2016-09-04 21:00:00+00	2016-09-05 06:00:00+00
912	7	2016-09-05 10:00:00+00	2016-09-05 11:00:00+00
913	7	2016-09-05 21:00:00+00	2016-09-06 06:00:00+00
914	7	2016-09-06 15:00:00+00	2016-09-06 21:00:00+00
915	7	2016-09-06 10:00:00+00	2016-09-06 11:00:00+00
916	7	2016-09-06 21:00:00+00	2016-09-07 06:00:00+00
917	7	2016-09-07 15:00:00+00	2016-09-07 21:00:00+00
918	7	2016-09-07 10:00:00+00	2016-09-07 11:00:00+00
919	7	2016-09-08 10:00:00+00	2016-09-08 11:00:00+00
920	7	2016-09-07 21:00:00+00	2016-09-08 06:00:00+00
921	7	2016-09-08 15:00:00+00	2016-09-08 21:00:00+00
922	7	2016-09-09 10:00:00+00	2016-09-09 11:00:00+00
923	7	2016-09-08 21:00:00+00	2016-09-09 06:00:00+00
924	7	2016-09-09 15:00:00+00	2016-09-09 21:00:00+00
925	7	2016-09-09 21:00:00+00	2016-09-10 06:00:00+00
926	7	2016-09-10 10:00:00+00	2016-09-10 11:00:00+00
927	7	2016-09-10 15:00:00+00	2016-09-10 21:00:00+00
928	7	2016-09-11 15:00:00+00	2016-09-11 21:00:00+00
929	7	2016-09-10 21:00:00+00	2016-09-11 06:00:00+00
930	7	2016-09-11 10:00:00+00	2016-09-11 11:00:00+00
931	7	2016-08-26 10:30:00+00	2016-08-26 11:00:00+00
932	11	2016-08-22 08:00:00+00	2016-08-22 09:00:00+00
933	11	2016-08-22 13:00:00+00	2016-08-22 19:00:00+00
934	11	2016-08-21 19:00:00+00	2016-08-22 04:00:00+00
935	11	2016-08-23 13:00:00+00	2016-08-23 19:00:00+00
936	11	2016-08-23 08:00:00+00	2016-08-23 09:00:00+00
937	11	2016-08-22 19:00:00+00	2016-08-23 04:00:00+00
938	11	2016-08-24 08:00:00+00	2016-08-24 09:00:00+00
939	11	2016-08-23 19:00:00+00	2016-08-24 04:00:00+00
940	11	2016-08-24 13:00:00+00	2016-08-24 19:00:00+00
941	11	2016-08-25 13:00:00+00	2016-08-25 19:00:00+00
942	11	2016-08-24 19:00:00+00	2016-08-25 04:00:00+00
943	11	2016-08-26 08:00:00+00	2016-08-26 09:00:00+00
944	11	2016-08-26 13:00:00+00	2016-08-26 19:00:00+00
945	11	2016-08-25 19:00:00+00	2016-08-26 04:00:00+00
946	11	2016-08-26 19:00:00+00	2016-08-27 19:00:00+00
947	11	2016-09-03 19:00:00+00	2016-09-04 19:00:00+00
948	11	2016-08-27 19:00:00+00	2016-08-28 19:00:00+00
949	11	2016-08-29 13:00:00+00	2016-08-29 19:00:00+00
950	11	2016-08-28 19:00:00+00	2016-08-29 04:00:00+00
951	11	2016-08-29 08:00:00+00	2016-08-29 09:00:00+00
955	11	2016-08-31 13:00:00+00	2016-08-31 19:00:00+00
956	11	2016-08-30 19:00:00+00	2016-08-31 04:00:00+00
957	11	2016-08-31 08:00:00+00	2016-08-31 09:00:00+00
961	11	2016-09-01 19:00:00+00	2016-09-02 04:00:00+00
962	11	2016-09-02 13:00:00+00	2016-09-02 19:00:00+00
963	11	2016-09-02 08:00:00+00	2016-09-02 09:00:00+00
2111	13	2016-09-24 17:00:00+00	2016-09-25 02:00:00+00
2112	13	2016-09-25 17:00:00+00	2016-09-26 02:00:00+00
970	7	2016-08-31 07:30:00+00	2016-08-31 08:00:00+00
2113	13	2016-09-26 17:00:00+00	2016-09-27 02:00:00+00
2114	13	2016-09-27 17:00:00+00	2016-09-28 02:00:00+00
2115	13	2016-09-28 17:00:00+00	2016-09-29 02:00:00+00
2116	13	2016-09-29 17:00:00+00	2016-09-30 02:00:00+00
2117	13	2016-09-30 17:00:00+00	2016-10-01 02:00:00+00
2118	9	2016-09-01 21:00:00+00	2016-09-02 06:00:00+00
2119	9	2016-09-02 21:00:00+00	2016-09-03 06:00:00+00
2120	9	2016-09-03 21:00:00+00	2016-09-04 06:00:00+00
986	7	2016-09-04 06:00:00+00	2016-09-04 10:00:00+00
987	7	2016-09-04 11:00:00+00	2016-09-04 15:00:00+00
2121	9	2016-09-04 21:00:00+00	2016-09-05 06:00:00+00
2122	9	2016-09-05 21:00:00+00	2016-09-06 06:00:00+00
2123	9	2016-09-06 21:00:00+00	2016-09-07 06:00:00+00
2124	9	2016-09-07 21:00:00+00	2016-09-08 06:00:00+00
1003	7	2016-09-01 11:00:00+00	2016-09-01 11:30:00+00
1004	7	2016-09-01 11:30:00+00	2016-09-01 12:00:00+00
2125	9	2016-09-08 21:00:00+00	2016-09-09 06:00:00+00
2126	9	2016-09-09 21:00:00+00	2016-09-10 06:00:00+00
2127	9	2016-09-10 21:00:00+00	2016-09-11 06:00:00+00
2128	9	2016-09-11 21:00:00+00	2016-09-12 06:00:00+00
2129	9	2016-09-12 21:00:00+00	2016-09-13 06:00:00+00
2130	9	2016-09-13 21:00:00+00	2016-09-14 06:00:00+00
2131	9	2016-09-14 21:00:00+00	2016-09-15 06:00:00+00
2132	9	2016-09-15 21:00:00+00	2016-09-16 06:00:00+00
2133	9	2016-09-16 21:00:00+00	2016-09-17 06:00:00+00
2134	9	2016-09-17 21:00:00+00	2016-09-18 06:00:00+00
2135	9	2016-09-18 21:00:00+00	2016-09-19 06:00:00+00
2136	9	2016-09-19 21:00:00+00	2016-09-20 06:00:00+00
2137	9	2016-09-20 21:00:00+00	2016-09-21 06:00:00+00
2138	9	2016-09-21 21:00:00+00	2016-09-22 06:00:00+00
2139	9	2016-09-22 21:00:00+00	2016-09-23 06:00:00+00
2140	9	2016-09-23 21:00:00+00	2016-09-24 06:00:00+00
2141	9	2016-09-24 21:00:00+00	2016-09-25 06:00:00+00
2142	9	2016-09-25 21:00:00+00	2016-09-26 06:00:00+00
2143	9	2016-09-26 21:00:00+00	2016-09-27 06:00:00+00
2144	9	2016-09-27 21:00:00+00	2016-09-28 06:00:00+00
2145	9	2016-09-28 21:00:00+00	2016-09-29 06:00:00+00
2146	9	2016-09-29 21:00:00+00	2016-09-30 06:00:00+00
2147	9	2016-09-30 21:00:00+00	2016-10-01 06:00:00+00
2148	10	2016-09-01 21:00:00+00	2016-09-02 06:00:00+00
2149	10	2016-09-02 21:00:00+00	2016-09-03 06:00:00+00
2150	10	2016-09-03 21:00:00+00	2016-09-04 06:00:00+00
2151	10	2016-09-04 21:00:00+00	2016-09-05 06:00:00+00
2152	10	2016-09-05 21:00:00+00	2016-09-06 06:00:00+00
2153	10	2016-09-06 21:00:00+00	2016-09-07 06:00:00+00
2154	10	2016-09-07 21:00:00+00	2016-09-08 06:00:00+00
2155	10	2016-09-08 21:00:00+00	2016-09-09 06:00:00+00
2156	10	2016-09-09 21:00:00+00	2016-09-10 06:00:00+00
2157	10	2016-09-10 21:00:00+00	2016-09-11 06:00:00+00
2158	10	2016-09-11 21:00:00+00	2016-09-12 06:00:00+00
2159	10	2016-09-12 21:00:00+00	2016-09-13 06:00:00+00
2160	10	2016-09-13 21:00:00+00	2016-09-14 06:00:00+00
2161	10	2016-09-14 21:00:00+00	2016-09-15 06:00:00+00
2162	10	2016-09-15 21:00:00+00	2016-09-16 06:00:00+00
2163	10	2016-09-16 21:00:00+00	2016-09-17 06:00:00+00
2164	10	2016-09-17 21:00:00+00	2016-09-18 06:00:00+00
2165	10	2016-09-18 21:00:00+00	2016-09-19 06:00:00+00
2166	10	2016-09-19 21:00:00+00	2016-09-20 06:00:00+00
2167	10	2016-09-20 21:00:00+00	2016-09-21 06:00:00+00
2168	10	2016-09-21 21:00:00+00	2016-09-22 06:00:00+00
2169	10	2016-09-22 21:00:00+00	2016-09-23 06:00:00+00
2170	10	2016-09-23 21:00:00+00	2016-09-24 06:00:00+00
2171	10	2016-09-24 21:00:00+00	2016-09-25 06:00:00+00
2172	10	2016-09-25 21:00:00+00	2016-09-26 06:00:00+00
2173	10	2016-09-26 21:00:00+00	2016-09-27 06:00:00+00
2174	10	2016-09-27 21:00:00+00	2016-09-28 06:00:00+00
2175	10	2016-09-28 21:00:00+00	2016-09-29 06:00:00+00
2176	10	2016-09-29 21:00:00+00	2016-09-30 06:00:00+00
2177	10	2016-09-30 21:00:00+00	2016-10-01 06:00:00+00
2178	8	2016-09-01 21:00:00+00	2016-09-02 06:00:00+00
2179	8	2016-09-02 21:00:00+00	2016-09-03 06:00:00+00
2180	8	2016-09-03 21:00:00+00	2016-09-04 06:00:00+00
2181	8	2016-09-04 21:00:00+00	2016-09-05 06:00:00+00
2182	8	2016-09-05 21:00:00+00	2016-09-06 06:00:00+00
2183	8	2016-09-06 21:00:00+00	2016-09-07 06:00:00+00
2184	8	2016-09-07 21:00:00+00	2016-09-08 06:00:00+00
2185	8	2016-09-08 21:00:00+00	2016-09-09 06:00:00+00
2186	8	2016-09-09 21:00:00+00	2016-09-10 06:00:00+00
2187	8	2016-09-10 21:00:00+00	2016-09-11 06:00:00+00
2188	8	2016-09-11 21:00:00+00	2016-09-12 06:00:00+00
2189	8	2016-09-12 21:00:00+00	2016-09-13 06:00:00+00
2190	8	2016-09-13 21:00:00+00	2016-09-14 06:00:00+00
2191	8	2016-09-14 21:00:00+00	2016-09-15 06:00:00+00
2192	8	2016-09-15 21:00:00+00	2016-09-16 06:00:00+00
2193	8	2016-09-16 21:00:00+00	2016-09-17 06:00:00+00
2194	8	2016-09-17 21:00:00+00	2016-09-18 06:00:00+00
2195	8	2016-09-18 21:00:00+00	2016-09-19 06:00:00+00
2196	8	2016-09-19 21:00:00+00	2016-09-20 06:00:00+00
2197	8	2016-09-20 21:00:00+00	2016-09-21 06:00:00+00
2198	8	2016-09-21 21:00:00+00	2016-09-22 06:00:00+00
2199	8	2016-09-22 21:00:00+00	2016-09-23 06:00:00+00
2200	8	2016-09-23 21:00:00+00	2016-09-24 06:00:00+00
2201	8	2016-09-24 21:00:00+00	2016-09-25 06:00:00+00
2202	8	2016-09-25 21:00:00+00	2016-09-26 06:00:00+00
2203	8	2016-09-26 21:00:00+00	2016-09-27 06:00:00+00
2204	8	2016-09-27 21:00:00+00	2016-09-28 06:00:00+00
2205	8	2016-09-28 21:00:00+00	2016-09-29 06:00:00+00
2206	8	2016-09-29 21:00:00+00	2016-09-30 06:00:00+00
2207	8	2016-09-30 21:00:00+00	2016-10-01 06:00:00+00
2354	7	2016-10-10 12:00:00+00	2016-10-10 13:00:00+00
2357	7	2016-10-06 13:00:00+00	2016-10-06 14:00:00+00
2358	14	2016-10-07 11:00:00+00	2016-10-07 12:00:00+00
2360	7	2016-10-05 08:00:00+00	2016-10-05 08:30:00+00
2361	7	2016-10-05 09:00:00+00	2016-10-05 09:30:00+00
2362	7	2016-10-05 07:30:00+00	2016-10-05 08:00:00+00
2366	7	2016-10-06 06:00:00+00	2016-10-06 09:00:00+00
2369	11	2016-10-13 04:00:00+00	2016-10-13 04:30:00+00
2377	7	2016-10-10 08:00:00+00	2016-10-10 08:30:00+00
2378	7	2016-10-10 08:30:00+00	2016-10-10 09:00:00+00
2385	7	2016-10-05 07:00:00+00	2016-10-05 07:30:00+00
2389	11	2016-10-13 06:30:00+00	2016-10-13 07:00:00+00
2392	14	2016-10-13 11:00:00+00	2016-10-13 11:30:00+00
2394	7	2016-10-19 06:00:00+00	2016-10-19 06:30:00+00
2395	14	2016-10-13 08:00:00+00	2016-10-13 09:00:00+00
2398	7	2016-10-13 09:00:00+00	2016-10-13 09:30:00+00
2399	14	2016-10-13 11:30:00+00	2016-10-13 12:00:00+00
2404	7	2016-10-14 11:00:00+00	2016-10-14 12:00:00+00
2406	7	2016-10-17 07:30:00+00	2016-10-17 08:00:00+00
2410	7	2016-10-17 06:00:00+00	2016-10-17 07:00:00+00
2411	7	2016-10-17 08:00:00+00	2016-10-17 10:00:00+00
2413	14	2016-10-17 11:00:00+00	2016-10-17 12:00:00+00
2414	14	2016-10-14 09:30:00+00	2016-10-14 10:00:00+00
2417	7	2016-10-20 11:00:00+00	2016-10-20 14:00:00+00
2420	14	2016-10-20 11:00:00+00	2016-10-20 12:00:00+00
952	11	2016-08-30 08:00:00+00	2016-08-30 09:00:00+00
953	11	2016-08-29 19:00:00+00	2016-08-30 04:00:00+00
954	11	2016-08-30 13:00:00+00	2016-08-30 19:00:00+00
958	11	2016-08-31 19:00:00+00	2016-09-01 04:00:00+00
959	11	2016-09-01 08:00:00+00	2016-09-01 09:00:00+00
960	11	2016-09-01 13:00:00+00	2016-09-01 19:00:00+00
984	7	2016-09-03 11:00:00+00	2016-09-03 15:00:00+00
985	7	2016-09-03 06:00:00+00	2016-09-03 10:00:00+00
988	7	2016-09-06 06:00:00+00	2016-09-06 10:00:00+00
990	7	2016-09-10 06:00:00+00	2016-09-10 10:00:00+00
991	7	2016-09-10 11:00:00+00	2016-09-10 15:00:00+00
992	7	2016-09-11 06:00:00+00	2016-09-11 10:00:00+00
993	7	2016-09-11 11:00:00+00	2016-09-11 15:00:00+00
994	11	2016-09-02 04:00:00+00	2016-09-02 04:30:00+00
997	7	2016-09-01 09:30:00+00	2016-09-01 10:00:00+00
989	7	2016-09-06 11:30:00+00	2016-09-06 15:00:00+00
1005	7	2016-09-06 11:00:00+00	2016-09-06 11:30:00+00
1006	11	2016-09-04 19:00:00+00	2016-09-05 01:00:00+00
1007	11	2016-09-05 13:00:00+00	2016-09-05 19:00:00+00
1008	11	2016-09-05 08:00:00+00	2016-09-05 09:00:00+00
1009	11	2016-09-05 19:00:00+00	2016-09-06 01:00:00+00
1010	11	2016-09-06 08:00:00+00	2016-09-06 09:00:00+00
1011	11	2016-09-06 13:00:00+00	2016-09-06 19:00:00+00
1016	11	2016-09-05 01:00:00+00	2016-09-05 04:00:00+00
1017	11	2016-09-06 01:00:00+00	2016-09-06 04:00:00+00
1023	8	2016-09-02 11:00:00+00	2016-09-02 11:30:00+00
1024	11	2016-09-05 04:00:00+00	2016-09-05 05:30:00+00
1028	8	2016-09-05 06:00:00+00	2016-09-05 07:00:00+00
1031	7	2016-09-07 06:00:00+00	2016-09-07 06:30:00+00
1032	7	2016-09-05 07:30:00+00	2016-09-05 08:30:00+00
1033	7	2016-09-05 13:00:00+00	2016-09-05 14:30:00+00
1036	7	2016-09-05 08:30:00+00	2016-09-05 09:00:00+00
1042	7	2016-09-07 12:00:00+00	2016-09-07 13:00:00+00
1049	11	2016-09-08 04:00:00+00	2016-09-08 04:30:00+00
1050	7	2016-09-08 09:00:00+00	2016-09-08 09:30:00+00
1051	7	2016-09-12 06:00:00+00	2016-09-12 07:30:00+00
1052	7	2016-09-12 07:30:00+00	2016-09-12 08:30:00+00
1053	7	2016-09-12 08:30:00+00	2016-09-12 09:00:00+00
1055	8	2016-09-12 06:00:00+00	2016-09-12 07:00:00+00
1056	11	2016-09-12 04:00:00+00	2016-09-12 05:00:00+00
1058	7	2016-09-11 21:00:00+00	2016-09-12 06:00:00+00
1059	7	2016-09-12 15:00:00+00	2016-09-12 21:00:00+00
1060	7	2016-09-13 15:00:00+00	2016-09-13 21:00:00+00
1061	7	2016-09-12 21:00:00+00	2016-09-13 06:00:00+00
1062	7	2016-09-13 10:00:00+00	2016-09-13 10:30:00+00
1063	7	2016-09-13 10:30:00+00	2016-09-13 15:00:00+00
1064	7	2016-09-13 06:00:00+00	2016-09-13 10:00:00+00
1065	7	2016-09-14 10:00:00+00	2016-09-14 11:00:00+00
1066	7	2016-09-14 15:00:00+00	2016-09-14 21:00:00+00
1067	7	2016-09-13 21:00:00+00	2016-09-14 06:00:00+00
1069	7	2016-09-14 21:00:00+00	2016-09-15 06:00:00+00
1070	7	2016-09-15 10:00:00+00	2016-09-15 11:00:00+00
1071	7	2016-09-15 21:00:00+00	2016-09-16 06:00:00+00
1072	7	2016-09-16 10:00:00+00	2016-09-16 11:00:00+00
1073	7	2016-09-16 15:00:00+00	2016-09-16 21:00:00+00
1074	7	2016-09-16 21:00:00+00	2016-09-17 21:00:00+00
1075	7	2016-09-17 21:00:00+00	2016-09-18 21:00:00+00
1076	7	2016-09-19 10:00:00+00	2016-09-19 11:00:00+00
1077	7	2016-09-18 21:00:00+00	2016-09-19 06:00:00+00
1078	7	2016-09-19 15:00:00+00	2016-09-19 21:00:00+00
1079	7	2016-09-20 15:00:00+00	2016-09-20 21:00:00+00
1080	7	2016-09-19 21:00:00+00	2016-09-20 06:00:00+00
1081	7	2016-09-20 10:00:00+00	2016-09-20 11:00:00+00
1082	7	2016-09-20 06:00:00+00	2016-09-20 07:00:00+00
1083	7	2016-09-20 09:00:00+00	2016-09-20 10:00:00+00
1084	7	2016-09-20 12:00:00+00	2016-09-20 13:00:00+00
1085	7	2016-09-20 13:00:00+00	2016-09-20 15:00:00+00
1086	7	2016-09-20 07:00:00+00	2016-09-20 09:00:00+00
1087	7	2016-09-20 11:00:00+00	2016-09-20 12:00:00+00
1090	7	2016-09-14 08:00:00+00	2016-09-14 08:30:00+00
1091	11	2016-10-01 19:00:00+00	2016-10-02 04:00:00+00
1092	11	2016-10-02 19:00:00+00	2016-10-03 04:00:00+00
1093	11	2016-10-03 19:00:00+00	2016-10-04 04:00:00+00
1094	11	2016-10-04 19:00:00+00	2016-10-05 04:00:00+00
1095	11	2016-10-05 19:00:00+00	2016-10-06 04:00:00+00
1096	11	2016-10-06 19:00:00+00	2016-10-07 04:00:00+00
1097	11	2016-10-07 19:00:00+00	2016-10-08 04:00:00+00
1098	11	2016-10-08 19:00:00+00	2016-10-09 04:00:00+00
1099	11	2016-10-09 19:00:00+00	2016-10-10 04:00:00+00
1100	11	2016-10-10 19:00:00+00	2016-10-11 04:00:00+00
1101	11	2016-10-11 19:00:00+00	2016-10-12 04:00:00+00
1102	11	2016-10-12 19:00:00+00	2016-10-13 04:00:00+00
1103	11	2016-10-13 19:00:00+00	2016-10-14 04:00:00+00
1104	11	2016-10-14 19:00:00+00	2016-10-15 04:00:00+00
1105	11	2016-10-15 19:00:00+00	2016-10-16 04:00:00+00
1106	11	2016-10-16 19:00:00+00	2016-10-17 04:00:00+00
1107	11	2016-10-17 19:00:00+00	2016-10-18 04:00:00+00
1108	11	2016-10-18 19:00:00+00	2016-10-19 04:00:00+00
1109	11	2016-10-19 19:00:00+00	2016-10-20 04:00:00+00
1110	11	2016-10-20 19:00:00+00	2016-10-21 04:00:00+00
1111	11	2016-10-21 19:00:00+00	2016-10-22 04:00:00+00
1112	11	2016-10-22 19:00:00+00	2016-10-23 04:00:00+00
1113	11	2016-10-23 19:00:00+00	2016-10-24 04:00:00+00
1114	11	2016-10-24 19:00:00+00	2016-10-25 04:00:00+00
1115	11	2016-10-25 19:00:00+00	2016-10-26 04:00:00+00
1116	11	2016-10-26 19:00:00+00	2016-10-27 04:00:00+00
1117	11	2016-10-27 19:00:00+00	2016-10-28 04:00:00+00
1118	11	2016-10-28 19:00:00+00	2016-10-29 04:00:00+00
1119	11	2016-10-29 19:00:00+00	2016-10-30 04:00:00+00
1120	11	2016-10-30 19:00:00+00	2016-10-31 04:00:00+00
1121	11	2016-10-01 13:00:00+00	2016-10-01 19:00:00+00
1122	11	2016-10-02 13:00:00+00	2016-10-02 19:00:00+00
1123	11	2016-10-03 13:00:00+00	2016-10-03 19:00:00+00
1124	11	2016-10-04 13:00:00+00	2016-10-04 19:00:00+00
1125	11	2016-10-05 13:00:00+00	2016-10-05 19:00:00+00
1126	11	2016-10-06 13:00:00+00	2016-10-06 19:00:00+00
1127	11	2016-10-07 13:00:00+00	2016-10-07 19:00:00+00
1128	11	2016-10-08 13:00:00+00	2016-10-08 19:00:00+00
1129	11	2016-10-09 13:00:00+00	2016-10-09 19:00:00+00
1130	11	2016-10-10 13:00:00+00	2016-10-10 19:00:00+00
1131	11	2016-10-11 13:00:00+00	2016-10-11 19:00:00+00
1132	11	2016-10-12 13:00:00+00	2016-10-12 19:00:00+00
1133	11	2016-10-13 13:00:00+00	2016-10-13 19:00:00+00
1134	11	2016-10-14 13:00:00+00	2016-10-14 19:00:00+00
1135	11	2016-10-15 13:00:00+00	2016-10-15 19:00:00+00
1136	11	2016-10-16 13:00:00+00	2016-10-16 19:00:00+00
1137	11	2016-10-17 13:00:00+00	2016-10-17 19:00:00+00
1138	11	2016-10-18 13:00:00+00	2016-10-18 19:00:00+00
1139	11	2016-10-19 13:00:00+00	2016-10-19 19:00:00+00
1140	11	2016-10-20 13:00:00+00	2016-10-20 19:00:00+00
1141	11	2016-10-21 13:00:00+00	2016-10-21 19:00:00+00
1142	11	2016-10-22 13:00:00+00	2016-10-22 19:00:00+00
1143	11	2016-10-23 13:00:00+00	2016-10-23 19:00:00+00
1144	11	2016-10-24 13:00:00+00	2016-10-24 19:00:00+00
1145	11	2016-10-25 13:00:00+00	2016-10-25 19:00:00+00
1146	11	2016-10-26 13:00:00+00	2016-10-26 19:00:00+00
1147	11	2016-10-27 13:00:00+00	2016-10-27 19:00:00+00
1148	11	2016-10-28 13:00:00+00	2016-10-28 19:00:00+00
1149	11	2016-10-29 13:00:00+00	2016-10-29 19:00:00+00
1150	11	2016-10-30 13:00:00+00	2016-10-30 19:00:00+00
1151	7	2016-10-01 21:00:00+00	2016-10-02 06:00:00+00
1152	7	2016-10-02 21:00:00+00	2016-10-03 06:00:00+00
1153	7	2016-10-03 21:00:00+00	2016-10-04 06:00:00+00
1154	7	2016-10-04 21:00:00+00	2016-10-05 06:00:00+00
1155	7	2016-10-05 21:00:00+00	2016-10-06 06:00:00+00
1156	7	2016-10-06 21:00:00+00	2016-10-07 06:00:00+00
1157	7	2016-10-07 21:00:00+00	2016-10-08 06:00:00+00
1158	7	2016-10-08 21:00:00+00	2016-10-09 06:00:00+00
1159	7	2016-10-09 21:00:00+00	2016-10-10 06:00:00+00
1160	7	2016-10-10 21:00:00+00	2016-10-11 06:00:00+00
1161	7	2016-10-11 21:00:00+00	2016-10-12 06:00:00+00
1162	7	2016-10-12 21:00:00+00	2016-10-13 06:00:00+00
1163	7	2016-10-13 21:00:00+00	2016-10-14 06:00:00+00
1164	7	2016-10-14 21:00:00+00	2016-10-15 06:00:00+00
1165	7	2016-10-15 21:00:00+00	2016-10-16 06:00:00+00
1166	7	2016-10-16 21:00:00+00	2016-10-17 06:00:00+00
1167	7	2016-10-17 21:00:00+00	2016-10-18 06:00:00+00
1168	7	2016-10-18 21:00:00+00	2016-10-19 06:00:00+00
1169	7	2016-10-19 21:00:00+00	2016-10-20 06:00:00+00
1170	7	2016-10-20 21:00:00+00	2016-10-21 06:00:00+00
1171	7	2016-10-21 21:00:00+00	2016-10-22 06:00:00+00
1172	7	2016-10-22 21:00:00+00	2016-10-23 06:00:00+00
1173	7	2016-10-23 21:00:00+00	2016-10-24 06:00:00+00
1174	7	2016-10-24 21:00:00+00	2016-10-25 06:00:00+00
1175	7	2016-10-25 21:00:00+00	2016-10-26 06:00:00+00
1176	7	2016-10-26 21:00:00+00	2016-10-27 06:00:00+00
1177	7	2016-10-27 21:00:00+00	2016-10-28 06:00:00+00
1178	7	2016-10-28 21:00:00+00	2016-10-29 06:00:00+00
1179	7	2016-10-29 21:00:00+00	2016-10-30 06:00:00+00
1180	7	2016-10-30 21:00:00+00	2016-10-31 06:00:00+00
1181	7	2016-10-01 15:00:00+00	2016-10-01 21:00:00+00
1182	7	2016-10-02 15:00:00+00	2016-10-02 21:00:00+00
1183	7	2016-10-03 15:00:00+00	2016-10-03 21:00:00+00
1184	7	2016-10-04 15:00:00+00	2016-10-04 21:00:00+00
1185	7	2016-10-05 15:00:00+00	2016-10-05 21:00:00+00
1186	7	2016-10-06 15:00:00+00	2016-10-06 21:00:00+00
1187	7	2016-10-07 15:00:00+00	2016-10-07 21:00:00+00
1188	7	2016-10-08 15:00:00+00	2016-10-08 21:00:00+00
1189	7	2016-10-09 15:00:00+00	2016-10-09 21:00:00+00
1190	7	2016-10-10 15:00:00+00	2016-10-10 21:00:00+00
1191	7	2016-10-11 15:00:00+00	2016-10-11 21:00:00+00
1192	7	2016-10-12 15:00:00+00	2016-10-12 21:00:00+00
1193	7	2016-10-13 15:00:00+00	2016-10-13 21:00:00+00
1194	7	2016-10-14 15:00:00+00	2016-10-14 21:00:00+00
1195	7	2016-10-15 15:00:00+00	2016-10-15 21:00:00+00
1196	7	2016-10-16 15:00:00+00	2016-10-16 21:00:00+00
1197	7	2016-10-17 15:00:00+00	2016-10-17 21:00:00+00
1198	7	2016-10-18 15:00:00+00	2016-10-18 21:00:00+00
1199	7	2016-10-19 15:00:00+00	2016-10-19 21:00:00+00
1200	7	2016-10-20 15:00:00+00	2016-10-20 21:00:00+00
1201	7	2016-10-21 15:00:00+00	2016-10-21 21:00:00+00
1202	7	2016-10-22 15:00:00+00	2016-10-22 21:00:00+00
1203	7	2016-10-23 15:00:00+00	2016-10-23 21:00:00+00
1204	7	2016-10-24 15:00:00+00	2016-10-24 21:00:00+00
1205	7	2016-10-25 15:00:00+00	2016-10-25 21:00:00+00
1206	7	2016-10-26 15:00:00+00	2016-10-26 21:00:00+00
1207	7	2016-10-27 15:00:00+00	2016-10-27 21:00:00+00
1208	7	2016-10-28 15:00:00+00	2016-10-28 21:00:00+00
1209	7	2016-10-29 15:00:00+00	2016-10-29 21:00:00+00
1210	7	2016-10-30 15:00:00+00	2016-10-30 21:00:00+00
1211	7	2016-09-12 10:00:00+00	2016-09-12 11:00:00+00
1212	7	2016-09-21 10:00:00+00	2016-09-21 11:00:00+00
1213	7	2016-09-21 15:00:00+00	2016-09-21 21:00:00+00
1214	7	2016-09-20 21:00:00+00	2016-09-21 06:00:00+00
1215	7	2016-09-21 21:00:00+00	2016-09-22 06:00:00+00
1216	7	2016-09-22 15:00:00+00	2016-09-22 21:00:00+00
1217	7	2016-09-22 10:00:00+00	2016-09-22 11:00:00+00
1218	7	2016-09-23 15:00:00+00	2016-09-23 21:00:00+00
1219	7	2016-09-22 21:00:00+00	2016-09-23 06:00:00+00
1220	7	2016-09-23 10:00:00+00	2016-09-23 11:00:00+00
1221	7	2016-09-23 21:00:00+00	2016-09-24 21:00:00+00
1222	7	2016-09-24 21:00:00+00	2016-09-25 21:00:00+00
1223	7	2016-09-26 15:00:00+00	2016-09-26 21:00:00+00
1224	7	2016-09-26 10:00:00+00	2016-09-26 11:00:00+00
1225	7	2016-09-25 21:00:00+00	2016-09-26 06:00:00+00
1226	7	2016-09-26 21:00:00+00	2016-09-27 21:00:00+00
1227	7	2016-09-28 15:00:00+00	2016-09-28 21:00:00+00
1228	7	2016-09-27 21:00:00+00	2016-09-28 06:00:00+00
1229	7	2016-09-28 10:00:00+00	2016-09-28 11:00:00+00
1230	7	2016-09-29 10:00:00+00	2016-09-29 11:00:00+00
1231	7	2016-09-29 15:00:00+00	2016-09-29 21:00:00+00
1232	7	2016-09-28 21:00:00+00	2016-09-29 06:00:00+00
1233	7	2016-09-29 21:00:00+00	2016-09-30 06:00:00+00
1234	7	2016-09-30 15:00:00+00	2016-09-30 21:00:00+00
1235	7	2016-09-30 10:00:00+00	2016-09-30 11:00:00+00
1236	7	2016-09-30 21:00:00+00	2016-10-01 15:00:00+00
1237	7	2016-10-02 06:00:00+00	2016-10-02 15:00:00+00
1238	7	2016-10-03 10:00:00+00	2016-10-03 11:00:00+00
1239	7	2016-10-04 06:00:00+00	2016-10-04 15:00:00+00
1240	7	2016-10-05 10:00:00+00	2016-10-05 11:00:00+00
1241	7	2016-10-06 10:00:00+00	2016-10-06 11:00:00+00
1242	7	2016-10-07 10:00:00+00	2016-10-07 11:00:00+00
1243	7	2016-10-08 06:00:00+00	2016-10-08 15:00:00+00
1244	7	2016-10-09 06:00:00+00	2016-10-09 15:00:00+00
1245	7	2016-10-10 10:00:00+00	2016-10-10 11:00:00+00
1246	7	2016-10-11 10:00:00+00	2016-10-11 11:00:00+00
1247	7	2016-10-11 09:00:00+00	2016-10-11 10:00:00+00
1248	7	2016-10-11 06:00:00+00	2016-10-11 07:00:00+00
1249	7	2016-10-11 12:00:00+00	2016-10-11 13:00:00+00
1250	7	2016-10-11 07:00:00+00	2016-10-11 07:30:00+00
1251	7	2016-10-11 13:00:00+00	2016-10-11 13:30:00+00
1252	7	2016-10-11 13:30:00+00	2016-10-11 14:00:00+00
1253	7	2016-10-11 07:30:00+00	2016-10-11 08:00:00+00
1254	7	2016-10-11 08:00:00+00	2016-10-11 09:00:00+00
1255	7	2016-10-11 11:00:00+00	2016-10-11 12:00:00+00
1256	7	2016-10-11 14:00:00+00	2016-10-11 15:00:00+00
1257	7	2016-10-12 10:00:00+00	2016-10-12 11:00:00+00
1258	7	2016-10-13 10:00:00+00	2016-10-13 11:00:00+00
1259	7	2016-10-14 10:00:00+00	2016-10-14 11:00:00+00
1260	7	2016-10-15 06:00:00+00	2016-10-15 15:00:00+00
1261	7	2016-10-16 06:00:00+00	2016-10-16 15:00:00+00
1262	7	2016-10-17 10:00:00+00	2016-10-17 11:00:00+00
1263	7	2016-10-18 06:00:00+00	2016-10-18 15:00:00+00
1264	7	2016-10-19 10:00:00+00	2016-10-19 11:00:00+00
1265	7	2016-10-20 10:00:00+00	2016-10-20 11:00:00+00
1266	7	2016-10-21 10:00:00+00	2016-10-21 11:00:00+00
1267	7	2016-10-22 06:00:00+00	2016-10-22 15:00:00+00
1268	7	2016-10-23 06:00:00+00	2016-10-23 15:00:00+00
1269	7	2016-10-24 10:00:00+00	2016-10-24 11:00:00+00
1270	7	2016-10-25 06:00:00+00	2016-10-25 15:00:00+00
1271	7	2016-10-26 10:00:00+00	2016-10-26 11:00:00+00
1272	7	2016-10-27 10:00:00+00	2016-10-27 11:00:00+00
1273	7	2016-10-28 10:00:00+00	2016-10-28 11:00:00+00
1274	7	2016-10-29 06:00:00+00	2016-10-29 15:00:00+00
1275	7	2016-10-30 06:00:00+00	2016-10-30 15:00:00+00
1276	7	2016-10-31 10:00:00+00	2016-10-31 11:00:00+00
1277	7	2016-10-31 15:00:00+00	2016-10-31 21:00:00+00
1278	14	2016-10-01 21:00:00+00	2016-10-02 06:00:00+00
1279	14	2016-10-02 21:00:00+00	2016-10-03 06:00:00+00
1280	14	2016-10-03 21:00:00+00	2016-10-04 06:00:00+00
1281	14	2016-10-04 21:00:00+00	2016-10-05 06:00:00+00
1282	14	2016-10-05 21:00:00+00	2016-10-06 06:00:00+00
1283	14	2016-10-06 21:00:00+00	2016-10-07 06:00:00+00
1284	14	2016-10-07 21:00:00+00	2016-10-08 06:00:00+00
1285	14	2016-10-08 21:00:00+00	2016-10-09 06:00:00+00
1286	14	2016-10-09 21:00:00+00	2016-10-10 06:00:00+00
1287	14	2016-10-10 21:00:00+00	2016-10-11 06:00:00+00
1288	14	2016-10-11 21:00:00+00	2016-10-12 06:00:00+00
1289	14	2016-10-12 21:00:00+00	2016-10-13 06:00:00+00
1290	14	2016-10-13 21:00:00+00	2016-10-14 06:00:00+00
1291	14	2016-10-14 21:00:00+00	2016-10-15 06:00:00+00
1292	14	2016-10-15 21:00:00+00	2016-10-16 06:00:00+00
1293	14	2016-10-16 21:00:00+00	2016-10-17 06:00:00+00
1294	14	2016-10-17 21:00:00+00	2016-10-18 06:00:00+00
1295	14	2016-10-18 21:00:00+00	2016-10-19 06:00:00+00
1296	14	2016-10-19 21:00:00+00	2016-10-20 06:00:00+00
1297	14	2016-10-20 21:00:00+00	2016-10-21 06:00:00+00
1298	14	2016-10-21 21:00:00+00	2016-10-22 06:00:00+00
1299	14	2016-10-22 21:00:00+00	2016-10-23 06:00:00+00
1300	14	2016-10-23 21:00:00+00	2016-10-24 06:00:00+00
1301	14	2016-10-24 21:00:00+00	2016-10-25 06:00:00+00
1302	14	2016-10-25 21:00:00+00	2016-10-26 06:00:00+00
1303	14	2016-10-26 21:00:00+00	2016-10-27 06:00:00+00
1304	14	2016-10-27 21:00:00+00	2016-10-28 06:00:00+00
1305	14	2016-10-28 21:00:00+00	2016-10-29 06:00:00+00
1306	14	2016-10-29 21:00:00+00	2016-10-30 06:00:00+00
1307	14	2016-10-30 21:00:00+00	2016-10-31 06:00:00+00
1308	14	2016-10-01 15:00:00+00	2016-10-01 21:00:00+00
1309	14	2016-10-02 15:00:00+00	2016-10-02 21:00:00+00
1310	14	2016-10-03 15:00:00+00	2016-10-03 21:00:00+00
1311	14	2016-10-04 15:00:00+00	2016-10-04 21:00:00+00
1312	14	2016-10-05 15:00:00+00	2016-10-05 21:00:00+00
1313	14	2016-10-06 15:00:00+00	2016-10-06 21:00:00+00
1314	14	2016-10-07 15:00:00+00	2016-10-07 21:00:00+00
1315	14	2016-10-08 15:00:00+00	2016-10-08 21:00:00+00
1316	14	2016-10-09 15:00:00+00	2016-10-09 21:00:00+00
1317	14	2016-10-10 15:00:00+00	2016-10-10 21:00:00+00
1318	14	2016-10-11 15:00:00+00	2016-10-11 21:00:00+00
1319	14	2016-10-12 15:00:00+00	2016-10-12 21:00:00+00
1320	14	2016-10-13 15:00:00+00	2016-10-13 21:00:00+00
1321	14	2016-10-14 15:00:00+00	2016-10-14 21:00:00+00
1322	14	2016-10-15 15:00:00+00	2016-10-15 21:00:00+00
1323	14	2016-10-16 15:00:00+00	2016-10-16 21:00:00+00
1324	14	2016-10-17 15:00:00+00	2016-10-17 21:00:00+00
1325	14	2016-10-18 15:00:00+00	2016-10-18 21:00:00+00
1326	14	2016-10-19 15:00:00+00	2016-10-19 21:00:00+00
1327	14	2016-10-20 15:00:00+00	2016-10-20 21:00:00+00
1328	14	2016-10-21 15:00:00+00	2016-10-21 21:00:00+00
1329	14	2016-10-22 15:00:00+00	2016-10-22 21:00:00+00
1330	14	2016-10-23 15:00:00+00	2016-10-23 21:00:00+00
1331	14	2016-10-24 15:00:00+00	2016-10-24 21:00:00+00
1332	14	2016-10-25 15:00:00+00	2016-10-25 21:00:00+00
1333	14	2016-10-26 15:00:00+00	2016-10-26 21:00:00+00
1334	14	2016-10-27 15:00:00+00	2016-10-27 21:00:00+00
1335	14	2016-10-28 15:00:00+00	2016-10-28 21:00:00+00
1336	14	2016-10-29 15:00:00+00	2016-10-29 21:00:00+00
1337	14	2016-10-30 15:00:00+00	2016-10-30 21:00:00+00
2208	14	2016-09-15 08:30:00+00	2016-09-15 09:00:00+00
2211	11	2016-09-08 13:00:00+00	2016-09-08 19:00:00+00
2212	11	2016-09-07 19:00:00+00	2016-09-08 04:00:00+00
2215	11	2016-09-12 13:00:00+00	2016-09-12 19:00:00+00
2216	11	2016-09-11 19:00:00+00	2016-09-12 04:00:00+00
2219	11	2016-09-13 19:00:00+00	2016-09-14 04:00:00+00
2220	11	2016-09-14 13:00:00+00	2016-09-14 19:00:00+00
2221	11	2016-09-14 19:00:00+00	2016-09-15 04:00:00+00
2222	11	2016-09-15 13:00:00+00	2016-09-15 19:00:00+00
2223	11	2016-09-16 13:00:00+00	2016-09-16 19:00:00+00
2224	11	2016-09-15 19:00:00+00	2016-09-16 04:00:00+00
2227	11	2016-09-20 13:00:00+00	2016-09-20 19:00:00+00
2228	11	2016-09-19 19:00:00+00	2016-09-20 04:00:00+00
2231	11	2016-09-22 13:00:00+00	2016-09-22 19:00:00+00
2232	11	2016-09-21 19:00:00+00	2016-09-22 04:00:00+00
2235	11	2016-09-25 19:00:00+00	2016-09-26 04:00:00+00
2236	11	2016-09-26 13:00:00+00	2016-09-26 19:00:00+00
2239	11	2016-09-27 19:00:00+00	2016-09-28 04:00:00+00
2240	11	2016-09-28 13:00:00+00	2016-09-28 19:00:00+00
2243	11	2016-09-30 13:00:00+00	2016-09-30 19:00:00+00
2244	11	2016-09-29 19:00:00+00	2016-09-30 04:00:00+00
2246	7	2016-09-15 11:00:00+00	2016-09-15 11:30:00+00
2249	14	2016-09-15 09:00:00+00	2016-09-15 09:30:00+00
2250	8	2016-09-16 11:00:00+00	2016-09-16 12:00:00+00
2251	8	2016-09-19 06:00:00+00	2016-09-19 07:00:00+00
2254	7	2016-09-16 07:00:00+00	2016-09-16 07:30:00+00
2259	7	2016-09-16 12:00:00+00	2016-09-16 13:00:00+00
1367	14	2016-09-01 21:00:00+00	2016-09-02 06:00:00+00
1368	14	2016-09-02 21:00:00+00	2016-09-03 06:00:00+00
1369	14	2016-09-03 21:00:00+00	2016-09-04 06:00:00+00
1370	14	2016-09-04 21:00:00+00	2016-09-05 06:00:00+00
1371	14	2016-09-05 21:00:00+00	2016-09-06 06:00:00+00
1372	14	2016-09-06 21:00:00+00	2016-09-07 06:00:00+00
1373	14	2016-09-07 21:00:00+00	2016-09-08 06:00:00+00
1374	14	2016-09-08 21:00:00+00	2016-09-09 06:00:00+00
1375	14	2016-09-09 21:00:00+00	2016-09-10 06:00:00+00
1376	14	2016-09-10 21:00:00+00	2016-09-11 06:00:00+00
1377	14	2016-09-11 21:00:00+00	2016-09-12 06:00:00+00
1378	14	2016-09-12 21:00:00+00	2016-09-13 06:00:00+00
1379	14	2016-09-13 21:00:00+00	2016-09-14 06:00:00+00
1380	14	2016-09-14 21:00:00+00	2016-09-15 06:00:00+00
1381	14	2016-09-15 21:00:00+00	2016-09-16 06:00:00+00
1382	14	2016-09-16 21:00:00+00	2016-09-17 06:00:00+00
1383	14	2016-09-17 21:00:00+00	2016-09-18 06:00:00+00
1384	14	2016-09-18 21:00:00+00	2016-09-19 06:00:00+00
1385	14	2016-09-19 21:00:00+00	2016-09-20 06:00:00+00
1386	14	2016-09-20 21:00:00+00	2016-09-21 06:00:00+00
1387	14	2016-09-21 21:00:00+00	2016-09-22 06:00:00+00
1388	14	2016-09-22 21:00:00+00	2016-09-23 06:00:00+00
1389	14	2016-09-23 21:00:00+00	2016-09-24 06:00:00+00
1390	14	2016-09-24 21:00:00+00	2016-09-25 06:00:00+00
1391	14	2016-09-25 21:00:00+00	2016-09-26 06:00:00+00
1392	14	2016-09-26 21:00:00+00	2016-09-27 06:00:00+00
1393	14	2016-09-27 21:00:00+00	2016-09-28 06:00:00+00
1394	14	2016-09-28 21:00:00+00	2016-09-29 06:00:00+00
1395	14	2016-09-29 21:00:00+00	2016-09-30 06:00:00+00
1396	14	2016-09-01 15:00:00+00	2016-09-01 21:00:00+00
1397	14	2016-09-02 15:00:00+00	2016-09-02 21:00:00+00
1398	14	2016-09-03 15:00:00+00	2016-09-03 21:00:00+00
1399	14	2016-09-04 15:00:00+00	2016-09-04 21:00:00+00
1400	14	2016-09-05 15:00:00+00	2016-09-05 21:00:00+00
1401	14	2016-09-06 15:00:00+00	2016-09-06 21:00:00+00
1402	14	2016-09-07 15:00:00+00	2016-09-07 21:00:00+00
1403	14	2016-09-08 15:00:00+00	2016-09-08 21:00:00+00
1404	14	2016-09-09 15:00:00+00	2016-09-09 21:00:00+00
1405	14	2016-09-10 15:00:00+00	2016-09-10 21:00:00+00
1406	14	2016-09-11 15:00:00+00	2016-09-11 21:00:00+00
1407	14	2016-09-12 15:00:00+00	2016-09-12 21:00:00+00
1408	14	2016-09-13 15:00:00+00	2016-09-13 21:00:00+00
1409	14	2016-09-14 15:00:00+00	2016-09-14 21:00:00+00
1410	14	2016-09-15 15:00:00+00	2016-09-15 21:00:00+00
1411	14	2016-09-16 15:00:00+00	2016-09-16 21:00:00+00
1412	14	2016-09-17 15:00:00+00	2016-09-17 21:00:00+00
1413	14	2016-09-18 15:00:00+00	2016-09-18 21:00:00+00
1414	14	2016-09-19 15:00:00+00	2016-09-19 21:00:00+00
1415	14	2016-09-20 15:00:00+00	2016-09-20 21:00:00+00
1416	14	2016-09-21 15:00:00+00	2016-09-21 21:00:00+00
1417	14	2016-09-22 15:00:00+00	2016-09-22 21:00:00+00
1418	14	2016-09-23 15:00:00+00	2016-09-23 21:00:00+00
1419	14	2016-09-24 15:00:00+00	2016-09-24 21:00:00+00
1420	14	2016-09-25 15:00:00+00	2016-09-25 21:00:00+00
1421	14	2016-09-26 15:00:00+00	2016-09-26 21:00:00+00
1422	14	2016-09-27 15:00:00+00	2016-09-27 21:00:00+00
1423	14	2016-09-28 15:00:00+00	2016-09-28 21:00:00+00
1424	14	2016-09-29 15:00:00+00	2016-09-29 21:00:00+00
2260	7	2016-09-19 08:30:00+00	2016-09-19 10:00:00+00
2261	7	2016-09-19 08:00:00+00	2016-09-19 08:30:00+00
1425	14	2016-09-30 15:00:00+00	2016-09-30 21:00:00+00
1426	14	2016-09-12 10:00:00+00	2016-09-12 11:00:00+00
1427	14	2016-09-13 10:00:00+00	2016-09-13 11:00:00+00
1428	14	2016-09-14 10:00:00+00	2016-09-14 11:00:00+00
1429	14	2016-09-13 06:00:00+00	2016-09-13 10:00:00+00
1430	14	2016-09-13 11:00:00+00	2016-09-13 15:00:00+00
1431	14	2016-09-15 10:00:00+00	2016-09-15 11:00:00+00
1432	14	2016-09-16 10:00:00+00	2016-09-16 11:00:00+00
1433	14	2016-09-17 06:00:00+00	2016-09-17 15:00:00+00
1434	14	2016-09-18 06:00:00+00	2016-09-18 15:00:00+00
1435	14	2016-09-19 10:00:00+00	2016-09-19 11:00:00+00
1436	14	2016-09-20 06:00:00+00	2016-09-20 15:00:00+00
1437	14	2016-09-21 10:00:00+00	2016-09-21 11:00:00+00
1438	14	2016-09-22 10:00:00+00	2016-09-22 11:00:00+00
1439	14	2016-09-23 10:00:00+00	2016-09-23 11:00:00+00
1440	14	2016-09-24 06:00:00+00	2016-09-24 15:00:00+00
1441	14	2016-09-25 06:00:00+00	2016-09-25 15:00:00+00
1442	14	2016-09-26 10:00:00+00	2016-09-26 11:00:00+00
1443	14	2016-09-27 06:00:00+00	2016-09-27 15:00:00+00
1444	14	2016-09-28 10:00:00+00	2016-09-28 11:00:00+00
1445	14	2016-09-29 10:00:00+00	2016-09-29 11:00:00+00
1446	14	2016-09-30 10:00:00+00	2016-09-30 11:00:00+00
1447	14	2016-09-30 21:00:00+00	2016-10-01 15:00:00+00
1448	14	2016-10-02 06:00:00+00	2016-10-02 15:00:00+00
1449	14	2016-10-03 10:00:00+00	2016-10-03 11:00:00+00
1450	14	2016-10-10 10:00:00+00	2016-10-10 11:00:00+00
1451	14	2016-10-17 10:00:00+00	2016-10-17 11:00:00+00
1452	14	2016-10-24 10:00:00+00	2016-10-24 11:00:00+00
1453	14	2016-10-04 13:00:00+00	2016-10-04 14:30:00+00
1454	14	2016-10-04 08:30:00+00	2016-10-04 09:00:00+00
1455	14	2016-10-04 11:30:00+00	2016-10-04 12:00:00+00
1456	14	2016-10-04 14:30:00+00	2016-10-04 15:00:00+00
1457	14	2016-10-04 06:00:00+00	2016-10-04 08:30:00+00
1458	14	2016-10-04 12:00:00+00	2016-10-04 13:00:00+00
1459	14	2016-10-04 09:00:00+00	2016-10-04 11:30:00+00
1460	14	2016-10-05 10:00:00+00	2016-10-05 11:00:00+00
1461	14	2016-10-06 10:00:00+00	2016-10-06 11:00:00+00
1462	14	2016-10-07 10:00:00+00	2016-10-07 11:00:00+00
1463	14	2016-10-08 06:00:00+00	2016-10-08 15:00:00+00
1464	14	2016-10-09 06:00:00+00	2016-10-09 15:00:00+00
1465	14	2016-10-12 10:00:00+00	2016-10-12 11:00:00+00
1466	14	2016-10-11 06:00:00+00	2016-10-11 15:00:00+00
1467	14	2016-10-13 10:00:00+00	2016-10-13 11:00:00+00
1468	14	2016-10-14 10:00:00+00	2016-10-14 11:00:00+00
1469	14	2016-10-15 06:00:00+00	2016-10-15 15:00:00+00
1470	14	2016-10-16 06:00:00+00	2016-10-16 15:00:00+00
1471	14	2016-10-19 10:00:00+00	2016-10-19 11:00:00+00
1472	14	2016-10-18 06:00:00+00	2016-10-18 15:00:00+00
1473	14	2016-10-20 10:00:00+00	2016-10-20 11:00:00+00
1474	14	2016-10-21 10:00:00+00	2016-10-21 11:00:00+00
1475	14	2016-10-22 06:00:00+00	2016-10-22 15:00:00+00
1476	14	2016-10-23 06:00:00+00	2016-10-23 15:00:00+00
1477	14	2016-10-25 06:00:00+00	2016-10-25 15:00:00+00
1478	14	2016-10-26 10:00:00+00	2016-10-26 11:00:00+00
1479	14	2016-10-27 10:00:00+00	2016-10-27 11:00:00+00
1480	14	2016-10-28 10:00:00+00	2016-10-28 11:00:00+00
1481	14	2016-10-29 06:00:00+00	2016-10-29 15:00:00+00
1482	14	2016-10-30 06:00:00+00	2016-10-30 15:00:00+00
1483	14	2016-10-31 15:00:00+00	2016-10-31 21:00:00+00
1484	14	2016-10-31 10:00:00+00	2016-10-31 11:00:00+00
1487	8	2016-09-14 11:00:00+00	2016-09-14 12:00:00+00
1490	11	2016-09-15 04:00:00+00	2016-09-15 05:00:00+00
1491	11	2016-09-13 04:30:00+00	2016-09-13 05:00:00+00
1495	11	2016-09-14 09:00:00+00	2016-09-14 09:30:00+00
1496	7	2016-09-14 11:30:00+00	2016-09-14 12:30:00+00
1502	7	2016-09-15 06:00:00+00	2016-09-15 06:30:00+00
1503	7	2016-09-15 06:30:00+00	2016-09-15 07:30:00+00
1504	7	2016-09-15 07:30:00+00	2016-09-15 09:00:00+00
1505	7	2016-09-14 08:30:00+00	2016-09-14 09:00:00+00
1507	7	2016-09-15 09:00:00+00	2016-09-15 10:00:00+00
1509	8	2016-10-01 21:00:00+00	2016-10-02 06:00:00+00
1510	8	2016-10-02 21:00:00+00	2016-10-03 06:00:00+00
1511	8	2016-10-03 21:00:00+00	2016-10-04 06:00:00+00
1512	8	2016-10-04 21:00:00+00	2016-10-05 06:00:00+00
1513	8	2016-10-05 21:00:00+00	2016-10-06 06:00:00+00
1514	8	2016-10-06 21:00:00+00	2016-10-07 06:00:00+00
1515	8	2016-10-07 21:00:00+00	2016-10-08 06:00:00+00
1516	8	2016-10-08 21:00:00+00	2016-10-09 06:00:00+00
1517	8	2016-10-09 21:00:00+00	2016-10-10 06:00:00+00
1518	8	2016-10-10 21:00:00+00	2016-10-11 06:00:00+00
1519	8	2016-10-11 21:00:00+00	2016-10-12 06:00:00+00
1520	8	2016-10-12 21:00:00+00	2016-10-13 06:00:00+00
1521	8	2016-10-13 21:00:00+00	2016-10-14 06:00:00+00
1522	8	2016-10-14 21:00:00+00	2016-10-15 06:00:00+00
1523	8	2016-10-15 21:00:00+00	2016-10-16 06:00:00+00
1524	8	2016-10-16 21:00:00+00	2016-10-17 06:00:00+00
1525	8	2016-10-17 21:00:00+00	2016-10-18 06:00:00+00
1526	8	2016-10-18 21:00:00+00	2016-10-19 06:00:00+00
1527	8	2016-10-19 21:00:00+00	2016-10-20 06:00:00+00
1528	8	2016-10-20 21:00:00+00	2016-10-21 06:00:00+00
1529	8	2016-10-21 21:00:00+00	2016-10-22 06:00:00+00
1530	8	2016-10-22 21:00:00+00	2016-10-23 06:00:00+00
1531	8	2016-10-23 21:00:00+00	2016-10-24 06:00:00+00
1532	8	2016-10-24 21:00:00+00	2016-10-25 06:00:00+00
1533	8	2016-10-25 21:00:00+00	2016-10-26 06:00:00+00
1534	8	2016-10-26 21:00:00+00	2016-10-27 06:00:00+00
1535	8	2016-10-27 21:00:00+00	2016-10-28 06:00:00+00
1536	8	2016-10-28 21:00:00+00	2016-10-29 06:00:00+00
1537	8	2016-10-29 21:00:00+00	2016-10-30 06:00:00+00
1538	8	2016-10-30 21:00:00+00	2016-10-31 06:00:00+00
1539	8	2016-10-31 21:00:00+00	2016-11-01 06:00:00+00
1540	10	2016-10-01 21:00:00+00	2016-10-02 06:00:00+00
1541	10	2016-10-02 21:00:00+00	2016-10-03 06:00:00+00
1542	10	2016-10-03 21:00:00+00	2016-10-04 06:00:00+00
1543	10	2016-10-04 21:00:00+00	2016-10-05 06:00:00+00
1544	10	2016-10-05 21:00:00+00	2016-10-06 06:00:00+00
1545	10	2016-10-06 21:00:00+00	2016-10-07 06:00:00+00
1546	10	2016-10-07 21:00:00+00	2016-10-08 06:00:00+00
1547	10	2016-10-08 21:00:00+00	2016-10-09 06:00:00+00
1548	10	2016-10-09 21:00:00+00	2016-10-10 06:00:00+00
1549	10	2016-10-10 21:00:00+00	2016-10-11 06:00:00+00
1550	10	2016-10-11 21:00:00+00	2016-10-12 06:00:00+00
1551	10	2016-10-12 21:00:00+00	2016-10-13 06:00:00+00
1552	10	2016-10-13 21:00:00+00	2016-10-14 06:00:00+00
1553	10	2016-10-14 21:00:00+00	2016-10-15 06:00:00+00
1554	10	2016-10-15 21:00:00+00	2016-10-16 06:00:00+00
1555	10	2016-10-16 21:00:00+00	2016-10-17 06:00:00+00
1556	10	2016-10-17 21:00:00+00	2016-10-18 06:00:00+00
1557	10	2016-10-18 21:00:00+00	2016-10-19 06:00:00+00
1558	10	2016-10-19 21:00:00+00	2016-10-20 06:00:00+00
1559	10	2016-10-20 21:00:00+00	2016-10-21 06:00:00+00
1560	10	2016-10-21 21:00:00+00	2016-10-22 06:00:00+00
1561	10	2016-10-22 21:00:00+00	2016-10-23 06:00:00+00
1562	10	2016-10-23 21:00:00+00	2016-10-24 06:00:00+00
1563	10	2016-10-24 21:00:00+00	2016-10-25 06:00:00+00
1564	10	2016-10-25 21:00:00+00	2016-10-26 06:00:00+00
1565	10	2016-10-26 21:00:00+00	2016-10-27 06:00:00+00
1566	10	2016-10-27 21:00:00+00	2016-10-28 06:00:00+00
1567	10	2016-10-28 21:00:00+00	2016-10-29 06:00:00+00
1568	10	2016-10-29 21:00:00+00	2016-10-30 06:00:00+00
1569	10	2016-10-30 21:00:00+00	2016-10-31 06:00:00+00
1570	10	2016-10-31 21:00:00+00	2016-11-01 06:00:00+00
1571	9	2016-10-01 21:00:00+00	2016-10-02 06:00:00+00
1572	9	2016-10-02 21:00:00+00	2016-10-03 06:00:00+00
1573	9	2016-10-03 21:00:00+00	2016-10-04 06:00:00+00
1574	9	2016-10-04 21:00:00+00	2016-10-05 06:00:00+00
1575	9	2016-10-05 21:00:00+00	2016-10-06 06:00:00+00
1576	9	2016-10-06 21:00:00+00	2016-10-07 06:00:00+00
1577	9	2016-10-07 21:00:00+00	2016-10-08 06:00:00+00
1578	9	2016-10-08 21:00:00+00	2016-10-09 06:00:00+00
1579	9	2016-10-09 21:00:00+00	2016-10-10 06:00:00+00
1580	9	2016-10-10 21:00:00+00	2016-10-11 06:00:00+00
1581	9	2016-10-11 21:00:00+00	2016-10-12 06:00:00+00
1582	9	2016-10-12 21:00:00+00	2016-10-13 06:00:00+00
1583	9	2016-10-13 21:00:00+00	2016-10-14 06:00:00+00
1584	9	2016-10-14 21:00:00+00	2016-10-15 06:00:00+00
1585	9	2016-10-15 21:00:00+00	2016-10-16 06:00:00+00
1586	9	2016-10-16 21:00:00+00	2016-10-17 06:00:00+00
1587	9	2016-10-17 21:00:00+00	2016-10-18 06:00:00+00
1588	9	2016-10-18 21:00:00+00	2016-10-19 06:00:00+00
1589	9	2016-10-19 21:00:00+00	2016-10-20 06:00:00+00
1590	9	2016-10-20 21:00:00+00	2016-10-21 06:00:00+00
1591	9	2016-10-21 21:00:00+00	2016-10-22 06:00:00+00
1592	9	2016-10-22 21:00:00+00	2016-10-23 06:00:00+00
1593	9	2016-10-23 21:00:00+00	2016-10-24 06:00:00+00
1594	9	2016-10-24 21:00:00+00	2016-10-25 06:00:00+00
1595	9	2016-10-25 21:00:00+00	2016-10-26 06:00:00+00
1596	9	2016-10-26 21:00:00+00	2016-10-27 06:00:00+00
1597	9	2016-10-27 21:00:00+00	2016-10-28 06:00:00+00
1598	9	2016-10-28 21:00:00+00	2016-10-29 06:00:00+00
1599	9	2016-10-29 21:00:00+00	2016-10-30 06:00:00+00
1600	9	2016-10-30 21:00:00+00	2016-10-31 06:00:00+00
1601	9	2016-10-31 21:00:00+00	2016-11-01 06:00:00+00
2209	11	2016-09-06 19:00:00+00	2016-09-07 04:00:00+00
2210	11	2016-09-07 13:00:00+00	2016-09-07 19:00:00+00
2213	11	2016-09-09 13:00:00+00	2016-09-09 19:00:00+00
2214	11	2016-09-08 19:00:00+00	2016-09-09 04:00:00+00
2217	11	2016-09-12 19:00:00+00	2016-09-13 04:00:00+00
2218	11	2016-09-13 13:00:00+00	2016-09-13 19:00:00+00
2225	11	2016-09-19 13:00:00+00	2016-09-19 19:00:00+00
2226	11	2016-09-18 19:00:00+00	2016-09-19 04:00:00+00
2229	11	2016-09-20 19:00:00+00	2016-09-21 04:00:00+00
2230	11	2016-09-21 13:00:00+00	2016-09-21 19:00:00+00
2234	11	2016-09-23 13:00:00+00	2016-09-23 19:00:00+00
2237	11	2016-09-27 13:00:00+00	2016-09-27 19:00:00+00
2238	11	2016-09-26 19:00:00+00	2016-09-27 04:00:00+00
2241	11	2016-09-29 13:00:00+00	2016-09-29 19:00:00+00
2242	11	2016-09-28 19:00:00+00	2016-09-29 04:00:00+00
2245	11	2016-09-30 19:00:00+00	2016-10-01 04:00:00+00
2247	14	2016-09-15 08:00:00+00	2016-09-15 08:30:00+00
2256	7	2016-09-16 13:00:00+00	2016-09-16 14:00:00+00
2257	7	2016-09-16 09:00:00+00	2016-09-16 10:00:00+00
2262	7	2016-09-19 07:00:00+00	2016-09-19 08:00:00+00
2233	11	2016-09-22 19:00:00+00	2016-09-23 01:00:00+00
2430	7	2016-10-21 06:00:00+00	2016-10-21 06:30:00+00
2439	15	2016-10-20 10:00:00+00	2016-10-20 11:00:00+00
2440	15	2016-10-19 21:00:00+00	2016-10-20 06:00:00+00
2441	15	2016-10-20 15:00:00+00	2016-10-20 21:00:00+00
2488	14	2016-10-21 11:00:00+00	2016-10-21 11:30:00+00
2489	14	2016-10-21 11:30:00+00	2016-10-21 12:00:00+00
2492	7	2016-10-24 12:00:00+00	2016-10-24 13:00:00+00
2494	7	2016-10-24 09:30:00+00	2016-10-24 10:00:00+00
2495	7	2016-10-24 11:00:00+00	2016-10-24 11:30:00+00
2500	14	2016-10-24 11:00:00+00	2016-10-24 12:00:00+00
1695	13	2016-10-01 17:00:00+00	2016-10-02 02:00:00+00
1696	13	2016-10-02 17:00:00+00	2016-10-03 02:00:00+00
1697	13	2016-10-03 17:00:00+00	2016-10-04 02:00:00+00
1698	13	2016-10-04 17:00:00+00	2016-10-05 02:00:00+00
1699	13	2016-10-05 17:00:00+00	2016-10-06 02:00:00+00
1700	13	2016-10-06 17:00:00+00	2016-10-07 02:00:00+00
1701	13	2016-10-07 17:00:00+00	2016-10-08 02:00:00+00
1702	13	2016-10-08 17:00:00+00	2016-10-09 02:00:00+00
1703	13	2016-10-09 17:00:00+00	2016-10-10 02:00:00+00
1704	13	2016-10-10 17:00:00+00	2016-10-11 02:00:00+00
1705	13	2016-10-11 17:00:00+00	2016-10-12 02:00:00+00
1706	13	2016-10-12 17:00:00+00	2016-10-13 02:00:00+00
1707	13	2016-10-13 17:00:00+00	2016-10-14 02:00:00+00
1708	13	2016-10-14 17:00:00+00	2016-10-15 02:00:00+00
1709	13	2016-10-15 17:00:00+00	2016-10-16 02:00:00+00
1710	13	2016-10-16 17:00:00+00	2016-10-17 02:00:00+00
1711	13	2016-10-17 17:00:00+00	2016-10-18 02:00:00+00
1712	13	2016-10-18 17:00:00+00	2016-10-19 02:00:00+00
1713	13	2016-10-19 17:00:00+00	2016-10-20 02:00:00+00
1714	13	2016-10-20 17:00:00+00	2016-10-21 02:00:00+00
1715	13	2016-10-21 17:00:00+00	2016-10-22 02:00:00+00
1716	13	2016-10-22 17:00:00+00	2016-10-23 02:00:00+00
1717	13	2016-10-23 17:00:00+00	2016-10-24 02:00:00+00
1718	13	2016-10-24 17:00:00+00	2016-10-25 02:00:00+00
1719	13	2016-10-25 17:00:00+00	2016-10-26 02:00:00+00
1720	13	2016-10-26 17:00:00+00	2016-10-27 02:00:00+00
1721	13	2016-10-27 17:00:00+00	2016-10-28 02:00:00+00
1722	13	2016-10-28 17:00:00+00	2016-10-29 02:00:00+00
1723	13	2016-10-29 17:00:00+00	2016-10-30 02:00:00+00
1724	13	2016-10-30 17:00:00+00	2016-10-31 02:00:00+00
2263	7	2016-09-19 11:00:00+00	2016-09-19 11:30:00+00
2442	15	2016-10-20 21:00:00+00	2016-10-21 06:00:00+00
2443	15	2016-10-21 15:00:00+00	2016-10-21 21:00:00+00
2444	15	2016-10-21 10:00:00+00	2016-10-21 11:00:00+00
2446	15	2016-10-22 21:00:00+00	2016-10-23 21:00:00+00
2450	15	2016-10-24 21:00:00+00	2016-10-25 06:00:00+00
2451	15	2016-10-25 10:00:00+00	2016-10-25 11:00:00+00
2452	15	2016-10-25 15:00:00+00	2016-10-25 21:00:00+00
2456	15	2016-10-27 15:00:00+00	2016-10-27 21:00:00+00
2457	15	2016-10-26 21:00:00+00	2016-10-27 06:00:00+00
2458	15	2016-10-27 10:00:00+00	2016-10-27 11:00:00+00
2462	15	2016-10-28 21:00:00+00	2016-10-29 21:00:00+00
2467	15	2016-11-01 10:00:00+00	2016-11-01 11:00:00+00
2468	15	2016-11-01 15:00:00+00	2016-11-01 21:00:00+00
2469	15	2016-10-31 21:00:00+00	2016-11-01 06:00:00+00
2473	15	2016-11-02 21:00:00+00	2016-11-03 06:00:00+00
2474	15	2016-11-03 10:00:00+00	2016-11-03 11:00:00+00
2475	15	2016-11-03 15:00:00+00	2016-11-03 21:00:00+00
2479	15	2016-11-04 21:00:00+00	2016-11-05 21:00:00+00
2490	14	2016-10-20 12:00:00+00	2016-10-20 12:30:00+00
2491	7	2016-10-21 06:30:00+00	2016-10-21 07:30:00+00
2493	7	2016-10-24 07:30:00+00	2016-10-24 09:00:00+00
2496	11	2016-10-31 08:00:00+00	2016-10-31 08:30:00+00
2499	15	2016-10-24 09:00:00+00	2016-10-24 09:30:00+00
2501	7	2016-10-24 11:30:00+00	2016-10-24 12:00:00+00
2503	11	2016-10-25 06:00:00+00	2016-10-25 06:30:00+00
1756	13	2016-10-01 11:00:00+00	2016-10-01 17:00:00+00
1757	13	2016-10-02 11:00:00+00	2016-10-02 17:00:00+00
1758	13	2016-10-03 11:00:00+00	2016-10-03 17:00:00+00
1759	13	2016-10-04 11:00:00+00	2016-10-04 17:00:00+00
1760	13	2016-10-05 11:00:00+00	2016-10-05 17:00:00+00
1761	13	2016-10-06 11:00:00+00	2016-10-06 17:00:00+00
1762	13	2016-10-07 11:00:00+00	2016-10-07 17:00:00+00
1763	13	2016-10-08 11:00:00+00	2016-10-08 17:00:00+00
1764	13	2016-10-09 11:00:00+00	2016-10-09 17:00:00+00
1765	13	2016-10-10 11:00:00+00	2016-10-10 17:00:00+00
1766	13	2016-10-11 11:00:00+00	2016-10-11 17:00:00+00
1767	13	2016-10-12 11:00:00+00	2016-10-12 17:00:00+00
1768	13	2016-10-13 11:00:00+00	2016-10-13 17:00:00+00
1769	13	2016-10-14 11:00:00+00	2016-10-14 17:00:00+00
1770	13	2016-10-15 11:00:00+00	2016-10-15 17:00:00+00
1771	13	2016-10-16 11:00:00+00	2016-10-16 17:00:00+00
1772	13	2016-10-17 11:00:00+00	2016-10-17 17:00:00+00
1773	13	2016-10-18 11:00:00+00	2016-10-18 17:00:00+00
1774	13	2016-10-19 11:00:00+00	2016-10-19 17:00:00+00
1775	13	2016-10-20 11:00:00+00	2016-10-20 17:00:00+00
1776	13	2016-10-21 11:00:00+00	2016-10-21 17:00:00+00
1777	13	2016-10-22 11:00:00+00	2016-10-22 17:00:00+00
1778	13	2016-10-23 11:00:00+00	2016-10-23 17:00:00+00
1779	13	2016-10-24 11:00:00+00	2016-10-24 17:00:00+00
1780	13	2016-10-25 11:00:00+00	2016-10-25 17:00:00+00
1781	13	2016-10-26 11:00:00+00	2016-10-26 17:00:00+00
1782	13	2016-10-27 11:00:00+00	2016-10-27 17:00:00+00
1783	13	2016-10-28 11:00:00+00	2016-10-28 17:00:00+00
1784	13	2016-10-29 11:00:00+00	2016-10-29 17:00:00+00
1785	13	2016-10-30 11:00:00+00	2016-10-30 17:00:00+00
1786	9	2016-10-01 15:00:00+00	2016-10-01 21:00:00+00
1787	9	2016-10-02 15:00:00+00	2016-10-02 21:00:00+00
1788	9	2016-10-03 15:00:00+00	2016-10-03 21:00:00+00
1789	9	2016-10-04 15:00:00+00	2016-10-04 21:00:00+00
1790	9	2016-10-05 15:00:00+00	2016-10-05 21:00:00+00
1791	9	2016-10-06 15:00:00+00	2016-10-06 21:00:00+00
1792	9	2016-10-07 15:00:00+00	2016-10-07 21:00:00+00
1793	9	2016-10-08 15:00:00+00	2016-10-08 21:00:00+00
1794	9	2016-10-09 15:00:00+00	2016-10-09 21:00:00+00
1795	9	2016-10-10 15:00:00+00	2016-10-10 21:00:00+00
1796	9	2016-10-11 15:00:00+00	2016-10-11 21:00:00+00
1797	9	2016-10-12 15:00:00+00	2016-10-12 21:00:00+00
1798	9	2016-10-13 15:00:00+00	2016-10-13 21:00:00+00
1799	9	2016-10-14 15:00:00+00	2016-10-14 21:00:00+00
1800	9	2016-10-15 15:00:00+00	2016-10-15 21:00:00+00
1801	9	2016-10-16 15:00:00+00	2016-10-16 21:00:00+00
1802	9	2016-10-17 15:00:00+00	2016-10-17 21:00:00+00
1803	9	2016-10-18 15:00:00+00	2016-10-18 21:00:00+00
1804	9	2016-10-19 15:00:00+00	2016-10-19 21:00:00+00
1805	9	2016-10-20 15:00:00+00	2016-10-20 21:00:00+00
1806	9	2016-10-21 15:00:00+00	2016-10-21 21:00:00+00
1807	9	2016-10-22 15:00:00+00	2016-10-22 21:00:00+00
1808	9	2016-10-23 15:00:00+00	2016-10-23 21:00:00+00
1809	9	2016-10-24 15:00:00+00	2016-10-24 21:00:00+00
1810	9	2016-10-25 15:00:00+00	2016-10-25 21:00:00+00
1811	9	2016-10-26 15:00:00+00	2016-10-26 21:00:00+00
1812	9	2016-10-27 15:00:00+00	2016-10-27 21:00:00+00
1813	9	2016-10-28 15:00:00+00	2016-10-28 21:00:00+00
1814	9	2016-10-29 15:00:00+00	2016-10-29 21:00:00+00
1815	9	2016-10-30 15:00:00+00	2016-10-30 21:00:00+00
1816	10	2016-10-01 15:00:00+00	2016-10-01 21:00:00+00
1817	10	2016-10-02 15:00:00+00	2016-10-02 21:00:00+00
1818	10	2016-10-03 15:00:00+00	2016-10-03 21:00:00+00
1819	10	2016-10-04 15:00:00+00	2016-10-04 21:00:00+00
1820	10	2016-10-05 15:00:00+00	2016-10-05 21:00:00+00
1821	10	2016-10-06 15:00:00+00	2016-10-06 21:00:00+00
1822	10	2016-10-07 15:00:00+00	2016-10-07 21:00:00+00
1823	10	2016-10-08 15:00:00+00	2016-10-08 21:00:00+00
1824	10	2016-10-09 15:00:00+00	2016-10-09 21:00:00+00
1825	10	2016-10-10 15:00:00+00	2016-10-10 21:00:00+00
1826	10	2016-10-11 15:00:00+00	2016-10-11 21:00:00+00
1827	10	2016-10-12 15:00:00+00	2016-10-12 21:00:00+00
1828	10	2016-10-13 15:00:00+00	2016-10-13 21:00:00+00
1829	10	2016-10-14 15:00:00+00	2016-10-14 21:00:00+00
1830	10	2016-10-15 15:00:00+00	2016-10-15 21:00:00+00
1831	10	2016-10-16 15:00:00+00	2016-10-16 21:00:00+00
1832	10	2016-10-17 15:00:00+00	2016-10-17 21:00:00+00
1833	10	2016-10-18 15:00:00+00	2016-10-18 21:00:00+00
1834	10	2016-10-19 15:00:00+00	2016-10-19 21:00:00+00
1835	10	2016-10-20 15:00:00+00	2016-10-20 21:00:00+00
1836	10	2016-10-21 15:00:00+00	2016-10-21 21:00:00+00
1837	10	2016-10-22 15:00:00+00	2016-10-22 21:00:00+00
1838	10	2016-10-23 15:00:00+00	2016-10-23 21:00:00+00
1839	10	2016-10-24 15:00:00+00	2016-10-24 21:00:00+00
1840	10	2016-10-25 15:00:00+00	2016-10-25 21:00:00+00
1841	10	2016-10-26 15:00:00+00	2016-10-26 21:00:00+00
1842	10	2016-10-27 15:00:00+00	2016-10-27 21:00:00+00
1843	10	2016-10-28 15:00:00+00	2016-10-28 21:00:00+00
1844	10	2016-10-29 15:00:00+00	2016-10-29 21:00:00+00
1845	10	2016-10-30 15:00:00+00	2016-10-30 21:00:00+00
1846	8	2016-10-01 15:00:00+00	2016-10-01 21:00:00+00
1847	8	2016-10-02 15:00:00+00	2016-10-02 21:00:00+00
1848	8	2016-10-03 15:00:00+00	2016-10-03 21:00:00+00
1849	8	2016-10-04 15:00:00+00	2016-10-04 21:00:00+00
1850	8	2016-10-05 15:00:00+00	2016-10-05 21:00:00+00
1851	8	2016-10-06 15:00:00+00	2016-10-06 21:00:00+00
1852	8	2016-10-07 15:00:00+00	2016-10-07 21:00:00+00
1853	8	2016-10-08 15:00:00+00	2016-10-08 21:00:00+00
1854	8	2016-10-09 15:00:00+00	2016-10-09 21:00:00+00
1855	8	2016-10-10 15:00:00+00	2016-10-10 21:00:00+00
1856	8	2016-10-11 15:00:00+00	2016-10-11 21:00:00+00
1857	8	2016-10-12 15:00:00+00	2016-10-12 21:00:00+00
1858	8	2016-10-13 15:00:00+00	2016-10-13 21:00:00+00
1859	8	2016-10-14 15:00:00+00	2016-10-14 21:00:00+00
1860	8	2016-10-15 15:00:00+00	2016-10-15 21:00:00+00
1861	8	2016-10-16 15:00:00+00	2016-10-16 21:00:00+00
1862	8	2016-10-17 15:00:00+00	2016-10-17 21:00:00+00
1863	8	2016-10-18 15:00:00+00	2016-10-18 21:00:00+00
1864	8	2016-10-19 15:00:00+00	2016-10-19 21:00:00+00
1865	8	2016-10-20 15:00:00+00	2016-10-20 21:00:00+00
1866	8	2016-10-21 15:00:00+00	2016-10-21 21:00:00+00
1867	8	2016-10-22 15:00:00+00	2016-10-22 21:00:00+00
1868	8	2016-10-23 15:00:00+00	2016-10-23 21:00:00+00
1869	8	2016-10-24 15:00:00+00	2016-10-24 21:00:00+00
1870	8	2016-10-25 15:00:00+00	2016-10-25 21:00:00+00
1871	8	2016-10-26 15:00:00+00	2016-10-26 21:00:00+00
1872	8	2016-10-27 15:00:00+00	2016-10-27 21:00:00+00
1873	8	2016-10-28 15:00:00+00	2016-10-28 21:00:00+00
1874	8	2016-10-29 15:00:00+00	2016-10-29 21:00:00+00
1875	8	2016-10-30 15:00:00+00	2016-10-30 21:00:00+00
1876	12	2016-10-01 16:00:00+00	2016-10-02 01:00:00+00
1877	12	2016-10-02 16:00:00+00	2016-10-03 01:00:00+00
1878	12	2016-10-03 16:00:00+00	2016-10-04 01:00:00+00
1879	12	2016-10-04 16:00:00+00	2016-10-05 01:00:00+00
1880	12	2016-10-05 16:00:00+00	2016-10-06 01:00:00+00
1881	12	2016-10-06 16:00:00+00	2016-10-07 01:00:00+00
1882	12	2016-10-07 16:00:00+00	2016-10-08 01:00:00+00
1883	12	2016-10-08 16:00:00+00	2016-10-09 01:00:00+00
1884	12	2016-10-09 16:00:00+00	2016-10-10 01:00:00+00
1885	12	2016-10-10 16:00:00+00	2016-10-11 01:00:00+00
1886	12	2016-10-11 16:00:00+00	2016-10-12 01:00:00+00
1887	12	2016-10-12 16:00:00+00	2016-10-13 01:00:00+00
1888	12	2016-10-13 16:00:00+00	2016-10-14 01:00:00+00
1889	12	2016-10-14 16:00:00+00	2016-10-15 01:00:00+00
1890	12	2016-10-15 16:00:00+00	2016-10-16 01:00:00+00
1891	12	2016-10-16 16:00:00+00	2016-10-17 01:00:00+00
1892	12	2016-10-17 16:00:00+00	2016-10-18 01:00:00+00
1893	12	2016-10-18 16:00:00+00	2016-10-19 01:00:00+00
1894	12	2016-10-19 16:00:00+00	2016-10-20 01:00:00+00
1895	12	2016-10-20 16:00:00+00	2016-10-21 01:00:00+00
1896	12	2016-10-21 16:00:00+00	2016-10-22 01:00:00+00
1897	12	2016-10-22 16:00:00+00	2016-10-23 01:00:00+00
1898	12	2016-10-23 16:00:00+00	2016-10-24 01:00:00+00
1899	12	2016-10-24 16:00:00+00	2016-10-25 01:00:00+00
1900	12	2016-10-25 16:00:00+00	2016-10-26 01:00:00+00
1901	12	2016-10-26 16:00:00+00	2016-10-27 01:00:00+00
1902	12	2016-10-27 16:00:00+00	2016-10-28 01:00:00+00
1903	12	2016-10-28 16:00:00+00	2016-10-29 01:00:00+00
1904	12	2016-10-29 16:00:00+00	2016-10-30 01:00:00+00
1905	12	2016-10-30 16:00:00+00	2016-10-31 01:00:00+00
1906	12	2016-10-31 16:00:00+00	2016-11-01 01:00:00+00
2264	7	2016-09-21 14:30:00+00	2016-09-21 15:00:00+00
2265	11	2016-09-23 02:30:00+00	2016-09-23 04:00:00+00
2266	11	2016-09-23 01:30:00+00	2016-09-23 02:00:00+00
2267	11	2016-09-23 02:00:00+00	2016-09-23 02:30:00+00
2268	7	2016-09-21 09:00:00+00	2016-09-21 10:00:00+00
2271	7	2016-09-21 07:30:00+00	2016-09-21 08:00:00+00
2272	11	2016-09-22 07:00:00+00	2016-09-22 07:30:00+00
1068	7	2016-09-15 15:00:00+00	2016-09-15 19:30:00+00
2274	7	2016-09-15 20:00:00+00	2016-09-15 21:00:00+00
2275	7	2016-09-15 19:30:00+00	2016-09-15 20:00:00+00
2278	7	2016-09-21 08:30:00+00	2016-09-21 09:00:00+00
2283	14	2016-09-21 11:00:00+00	2016-09-21 11:30:00+00
2284	7	2016-09-26 09:00:00+00	2016-09-26 09:30:00+00
2285	7	2016-09-21 13:00:00+00	2016-09-21 13:30:00+00
2286	7	2016-09-22 11:00:00+00	2016-09-22 11:30:00+00
2287	8	2016-09-23 11:00:00+00	2016-09-23 12:00:00+00
2288	8	2016-09-26 06:00:00+00	2016-09-26 07:00:00+00
2290	11	2016-09-23 01:00:00+00	2016-09-23 01:30:00+00
1938	13	2016-09-01 11:00:00+00	2016-09-01 17:00:00+00
1939	13	2016-09-02 11:00:00+00	2016-09-02 17:00:00+00
1940	13	2016-09-03 11:00:00+00	2016-09-03 17:00:00+00
1941	13	2016-09-04 11:00:00+00	2016-09-04 17:00:00+00
1942	13	2016-09-05 11:00:00+00	2016-09-05 17:00:00+00
1943	13	2016-09-06 11:00:00+00	2016-09-06 17:00:00+00
1944	13	2016-09-07 11:00:00+00	2016-09-07 17:00:00+00
1945	13	2016-09-08 11:00:00+00	2016-09-08 17:00:00+00
1946	13	2016-09-09 11:00:00+00	2016-09-09 17:00:00+00
1947	13	2016-09-10 11:00:00+00	2016-09-10 17:00:00+00
1948	13	2016-09-11 11:00:00+00	2016-09-11 17:00:00+00
1949	13	2016-09-12 11:00:00+00	2016-09-12 17:00:00+00
1950	13	2016-09-13 11:00:00+00	2016-09-13 17:00:00+00
1951	13	2016-09-14 11:00:00+00	2016-09-14 17:00:00+00
1952	13	2016-09-15 11:00:00+00	2016-09-15 17:00:00+00
1953	13	2016-09-16 11:00:00+00	2016-09-16 17:00:00+00
1954	13	2016-09-17 11:00:00+00	2016-09-17 17:00:00+00
1955	13	2016-09-18 11:00:00+00	2016-09-18 17:00:00+00
1956	13	2016-09-19 11:00:00+00	2016-09-19 17:00:00+00
1957	13	2016-09-20 11:00:00+00	2016-09-20 17:00:00+00
1958	13	2016-09-21 11:00:00+00	2016-09-21 17:00:00+00
1959	13	2016-09-22 11:00:00+00	2016-09-22 17:00:00+00
1960	13	2016-09-23 11:00:00+00	2016-09-23 17:00:00+00
1961	13	2016-09-24 11:00:00+00	2016-09-24 17:00:00+00
1962	13	2016-09-25 11:00:00+00	2016-09-25 17:00:00+00
1963	13	2016-09-26 11:00:00+00	2016-09-26 17:00:00+00
1964	13	2016-09-27 11:00:00+00	2016-09-27 17:00:00+00
1965	13	2016-09-28 11:00:00+00	2016-09-28 17:00:00+00
1966	13	2016-09-29 11:00:00+00	2016-09-29 17:00:00+00
1967	13	2016-09-30 11:00:00+00	2016-09-30 17:00:00+00
2293	11	2016-09-23 06:00:00+00	2016-09-23 06:30:00+00
2294	11	2016-09-23 06:30:00+00	2016-09-23 07:00:00+00
2295	11	2016-09-26 06:00:00+00	2016-09-26 07:00:00+00
2296	11	2016-09-27 06:00:00+00	2016-09-27 07:00:00+00
2298	11	2016-09-28 06:00:00+00	2016-09-28 07:00:00+00
2299	11	2016-09-29 06:00:00+00	2016-09-29 07:00:00+00
2300	11	2016-09-30 06:00:00+00	2016-09-30 07:00:00+00
2302	7	2016-09-26 11:00:00+00	2016-09-26 12:00:00+00
2304	14	2016-09-26 14:00:00+00	2016-09-26 15:00:00+00
2305	7	2016-09-26 08:00:00+00	2016-09-26 08:30:00+00
2306	7	2016-09-23 13:30:00+00	2016-09-23 14:00:00+00
2308	7	2016-09-23 14:00:00+00	2016-09-23 14:30:00+00
2311	14	2016-09-26 11:00:00+00	2016-09-26 12:00:00+00
2312	7	2016-09-26 09:30:00+00	2016-09-26 10:00:00+00
2313	7	2016-09-26 12:00:00+00	2016-09-26 12:30:00+00
2314	7	2016-09-26 12:30:00+00	2016-09-26 13:00:00+00
2315	7	2016-09-26 08:30:00+00	2016-09-26 09:00:00+00
2316	7	2016-09-28 06:00:00+00	2016-09-28 06:30:00+00
2317	7	2016-09-28 06:30:00+00	2016-09-28 07:30:00+00
2318	7	2016-09-28 12:00:00+00	2016-09-28 13:00:00+00
2319	7	2016-09-29 13:00:00+00	2016-09-29 14:00:00+00
2320	14	2016-09-28 11:30:00+00	2016-09-28 12:00:00+00
2327	7	2016-09-23 09:30:00+00	2016-09-23 10:00:00+00
2328	14	2016-09-28 09:30:00+00	2016-09-28 10:00:00+00
2329	7	2016-09-30 09:00:00+00	2016-09-30 09:30:00+00
2330	7	2016-09-29 09:00:00+00	2016-09-29 10:00:00+00
2331	7	2016-09-29 12:00:00+00	2016-09-29 13:00:00+00
2332	7	2016-09-29 11:00:00+00	2016-09-29 11:30:00+00
2619	11	2016-11-01 19:00:00+00	2016-11-02 04:00:00+00
2620	11	2016-11-02 19:00:00+00	2016-11-03 04:00:00+00
2621	11	2016-11-03 19:00:00+00	2016-11-04 04:00:00+00
2622	11	2016-11-04 19:00:00+00	2016-11-05 04:00:00+00
2623	11	2016-11-05 19:00:00+00	2016-11-06 04:00:00+00
2624	11	2016-11-06 19:00:00+00	2016-11-07 04:00:00+00
2625	11	2016-11-07 19:00:00+00	2016-11-08 04:00:00+00
2626	11	2016-11-08 19:00:00+00	2016-11-11 04:00:00+00
2627	11	2016-11-12 19:00:00+00	2016-11-13 04:00:00+00
2628	11	2016-11-13 19:00:00+00	2016-11-14 04:00:00+00
2629	11	2016-11-14 19:00:00+00	2016-11-15 04:00:00+00
2630	11	2016-11-15 19:00:00+00	2016-11-16 04:00:00+00
2631	11	2016-11-16 19:00:00+00	2016-11-17 04:00:00+00
2632	11	2016-11-17 19:00:00+00	2016-11-18 04:00:00+00
2633	11	2016-11-18 19:00:00+00	2016-11-19 04:00:00+00
2634	11	2016-11-19 19:00:00+00	2016-11-20 04:00:00+00
2635	11	2016-11-20 19:00:00+00	2016-11-21 04:00:00+00
2636	11	2016-11-21 19:00:00+00	2016-11-22 04:00:00+00
2637	11	2016-11-22 19:00:00+00	2016-11-23 04:00:00+00
2638	11	2016-11-23 19:00:00+00	2016-11-24 04:00:00+00
2639	11	2016-11-24 19:00:00+00	2016-11-25 04:00:00+00
2640	11	2016-11-25 19:00:00+00	2016-11-26 04:00:00+00
2641	11	2016-11-26 19:00:00+00	2016-11-27 04:00:00+00
2642	11	2016-11-27 19:00:00+00	2016-11-28 04:00:00+00
2643	11	2016-11-28 19:00:00+00	2016-11-29 04:00:00+00
2644	11	2016-11-29 19:00:00+00	2016-11-30 04:00:00+00
2645	11	2016-11-01 13:00:00+00	2016-11-01 19:00:00+00
2646	11	2016-11-02 13:00:00+00	2016-11-02 19:00:00+00
2647	11	2016-11-03 13:00:00+00	2016-11-03 19:00:00+00
2648	11	2016-11-04 13:00:00+00	2016-11-04 19:00:00+00
2649	11	2016-11-05 13:00:00+00	2016-11-05 19:00:00+00
2650	11	2016-11-06 13:00:00+00	2016-11-06 19:00:00+00
2651	11	2016-11-07 13:00:00+00	2016-11-07 19:00:00+00
2652	11	2016-11-08 13:00:00+00	2016-11-08 19:00:00+00
2653	11	2016-11-12 13:00:00+00	2016-11-12 19:00:00+00
2654	11	2016-11-13 13:00:00+00	2016-11-13 19:00:00+00
2655	11	2016-11-14 13:00:00+00	2016-11-14 19:00:00+00
2656	11	2016-11-15 13:00:00+00	2016-11-15 19:00:00+00
2657	11	2016-11-16 13:00:00+00	2016-11-16 19:00:00+00
2658	11	2016-11-17 13:00:00+00	2016-11-17 19:00:00+00
2659	11	2016-11-18 13:00:00+00	2016-11-18 19:00:00+00
2660	11	2016-11-19 13:00:00+00	2016-11-19 19:00:00+00
2661	11	2016-11-20 13:00:00+00	2016-11-20 19:00:00+00
2662	11	2016-11-21 13:00:00+00	2016-11-21 19:00:00+00
2663	11	2016-11-22 13:00:00+00	2016-11-22 19:00:00+00
2664	11	2016-11-23 13:00:00+00	2016-11-23 19:00:00+00
2665	11	2016-11-24 13:00:00+00	2016-11-24 19:00:00+00
2666	11	2016-11-25 13:00:00+00	2016-11-25 19:00:00+00
2667	11	2016-11-26 13:00:00+00	2016-11-26 19:00:00+00
2668	11	2016-11-27 13:00:00+00	2016-11-27 19:00:00+00
2669	11	2016-11-28 13:00:00+00	2016-11-28 19:00:00+00
2670	11	2016-11-29 13:00:00+00	2016-11-29 19:00:00+00
2671	11	2016-11-30 13:00:00+00	2016-11-30 19:00:00+00
2686	7	2016-11-01 21:00:00+00	2016-11-02 06:00:00+00
2687	7	2016-11-02 21:00:00+00	2016-11-03 06:00:00+00
2688	7	2016-11-03 21:00:00+00	2016-11-04 06:00:00+00
2689	7	2016-11-04 21:00:00+00	2016-11-05 06:00:00+00
2690	7	2016-11-05 21:00:00+00	2016-11-06 06:00:00+00
2691	7	2016-11-06 21:00:00+00	2016-11-07 06:00:00+00
2692	7	2016-11-07 21:00:00+00	2016-11-08 06:00:00+00
2693	7	2016-11-08 21:00:00+00	2016-11-09 06:00:00+00
2694	7	2016-11-09 21:00:00+00	2016-11-10 06:00:00+00
2695	7	2016-11-10 21:00:00+00	2016-11-11 06:00:00+00
2696	7	2016-11-11 21:00:00+00	2016-11-12 06:00:00+00
2697	7	2016-11-12 21:00:00+00	2016-11-13 06:00:00+00
2698	7	2016-11-13 21:00:00+00	2016-11-14 06:00:00+00
2699	7	2016-11-14 21:00:00+00	2016-11-15 06:00:00+00
2700	7	2016-11-15 21:00:00+00	2016-11-16 06:00:00+00
2701	7	2016-11-16 21:00:00+00	2016-11-17 06:00:00+00
2702	7	2016-11-17 21:00:00+00	2016-11-18 06:00:00+00
2703	7	2016-11-18 21:00:00+00	2016-11-19 06:00:00+00
2704	7	2016-11-19 21:00:00+00	2016-11-20 06:00:00+00
2705	7	2016-11-20 21:00:00+00	2016-11-21 06:00:00+00
2706	7	2016-11-21 21:00:00+00	2016-11-22 06:00:00+00
2707	7	2016-11-22 21:00:00+00	2016-11-23 06:00:00+00
2708	7	2016-11-23 21:00:00+00	2016-11-24 06:00:00+00
2709	7	2016-11-24 21:00:00+00	2016-11-25 06:00:00+00
2710	7	2016-11-25 21:00:00+00	2016-11-26 06:00:00+00
2711	7	2016-11-26 21:00:00+00	2016-11-27 06:00:00+00
2712	7	2016-11-27 21:00:00+00	2016-11-28 06:00:00+00
2713	7	2016-11-28 21:00:00+00	2016-11-29 06:00:00+00
2714	7	2016-11-29 21:00:00+00	2016-11-30 06:00:00+00
2715	7	2016-10-31 21:00:00+00	2016-11-01 06:00:00+00
2716	7	2016-11-01 15:00:00+00	2016-11-01 21:00:00+00
2717	7	2016-11-02 15:00:00+00	2016-11-02 21:00:00+00
2718	7	2016-11-03 15:00:00+00	2016-11-03 21:00:00+00
2719	7	2016-11-04 15:00:00+00	2016-11-04 21:00:00+00
2720	7	2016-11-05 15:00:00+00	2016-11-05 21:00:00+00
2721	7	2016-11-06 15:00:00+00	2016-11-06 21:00:00+00
2722	7	2016-11-07 15:00:00+00	2016-11-07 21:00:00+00
2723	7	2016-11-08 15:00:00+00	2016-11-08 21:00:00+00
2724	7	2016-11-09 15:00:00+00	2016-11-09 21:00:00+00
2725	7	2016-11-10 15:00:00+00	2016-11-10 21:00:00+00
2726	7	2016-11-11 15:00:00+00	2016-11-11 21:00:00+00
2727	7	2016-11-12 15:00:00+00	2016-11-12 21:00:00+00
2728	7	2016-11-13 15:00:00+00	2016-11-13 21:00:00+00
2729	7	2016-11-14 15:00:00+00	2016-11-14 21:00:00+00
2730	7	2016-11-15 15:00:00+00	2016-11-15 21:00:00+00
2731	7	2016-11-16 15:00:00+00	2016-11-16 21:00:00+00
2732	7	2016-11-17 15:00:00+00	2016-11-17 21:00:00+00
2733	7	2016-11-18 15:00:00+00	2016-11-18 21:00:00+00
2734	7	2016-11-19 15:00:00+00	2016-11-19 21:00:00+00
2735	7	2016-11-20 15:00:00+00	2016-11-20 21:00:00+00
2736	7	2016-11-21 15:00:00+00	2016-11-21 21:00:00+00
2737	7	2016-11-22 15:00:00+00	2016-11-22 21:00:00+00
2738	7	2016-11-23 15:00:00+00	2016-11-23 21:00:00+00
2739	7	2016-11-24 15:00:00+00	2016-11-24 21:00:00+00
2740	7	2016-11-25 15:00:00+00	2016-11-25 21:00:00+00
2741	7	2016-11-26 15:00:00+00	2016-11-26 21:00:00+00
2742	7	2016-11-27 15:00:00+00	2016-11-27 21:00:00+00
2743	7	2016-11-28 15:00:00+00	2016-11-28 21:00:00+00
2744	7	2016-11-29 15:00:00+00	2016-11-29 21:00:00+00
2745	7	2016-11-30 15:00:00+00	2016-11-30 21:00:00+00
2746	8	2016-11-01 21:00:00+00	2016-11-02 06:00:00+00
2747	8	2016-11-02 21:00:00+00	2016-11-03 06:00:00+00
2748	8	2016-11-03 21:00:00+00	2016-11-04 06:00:00+00
2672	11	2016-10-31 19:00:00+00	2016-11-01 04:00:00+00
2673	11	2016-11-01 08:00:00+00	2016-11-01 09:00:00+00
2749	8	2016-11-04 21:00:00+00	2016-11-05 06:00:00+00
2750	8	2016-11-05 21:00:00+00	2016-11-06 06:00:00+00
2751	8	2016-11-06 21:00:00+00	2016-11-07 06:00:00+00
2752	8	2016-11-07 21:00:00+00	2016-11-08 06:00:00+00
2753	8	2016-11-08 21:00:00+00	2016-11-09 06:00:00+00
2754	8	2016-11-09 21:00:00+00	2016-11-10 06:00:00+00
2755	8	2016-11-10 21:00:00+00	2016-11-11 06:00:00+00
2756	8	2016-11-11 21:00:00+00	2016-11-12 06:00:00+00
2757	8	2016-11-12 21:00:00+00	2016-11-13 06:00:00+00
2758	8	2016-11-13 21:00:00+00	2016-11-14 06:00:00+00
2759	8	2016-11-14 21:00:00+00	2016-11-15 06:00:00+00
2760	8	2016-11-15 21:00:00+00	2016-11-16 06:00:00+00
2761	8	2016-11-16 21:00:00+00	2016-11-17 06:00:00+00
2762	8	2016-11-17 21:00:00+00	2016-11-18 06:00:00+00
2763	8	2016-11-18 21:00:00+00	2016-11-19 06:00:00+00
2764	8	2016-11-19 21:00:00+00	2016-11-20 06:00:00+00
2765	8	2016-11-20 21:00:00+00	2016-11-21 06:00:00+00
2766	8	2016-11-21 21:00:00+00	2016-11-22 06:00:00+00
2767	8	2016-11-22 21:00:00+00	2016-11-23 06:00:00+00
2768	8	2016-11-23 21:00:00+00	2016-11-24 06:00:00+00
2769	8	2016-11-24 21:00:00+00	2016-11-25 06:00:00+00
2770	8	2016-11-25 21:00:00+00	2016-11-26 06:00:00+00
2771	8	2016-11-26 21:00:00+00	2016-11-27 06:00:00+00
2772	8	2016-11-27 21:00:00+00	2016-11-28 06:00:00+00
2773	8	2016-11-28 21:00:00+00	2016-11-29 06:00:00+00
2774	8	2016-11-29 21:00:00+00	2016-11-30 06:00:00+00
2775	8	2016-11-30 21:00:00+00	2016-12-01 06:00:00+00
2776	10	2016-11-01 21:00:00+00	2016-11-02 06:00:00+00
2777	10	2016-11-02 21:00:00+00	2016-11-03 06:00:00+00
2778	10	2016-11-03 21:00:00+00	2016-11-04 06:00:00+00
2779	10	2016-11-04 21:00:00+00	2016-11-05 06:00:00+00
2780	10	2016-11-05 21:00:00+00	2016-11-06 06:00:00+00
2781	10	2016-11-06 21:00:00+00	2016-11-07 06:00:00+00
2782	10	2016-11-07 21:00:00+00	2016-11-08 06:00:00+00
2783	10	2016-11-08 21:00:00+00	2016-11-09 06:00:00+00
2784	10	2016-11-09 21:00:00+00	2016-11-10 06:00:00+00
2785	10	2016-11-10 21:00:00+00	2016-11-11 06:00:00+00
2786	10	2016-11-11 21:00:00+00	2016-11-12 06:00:00+00
2787	10	2016-11-12 21:00:00+00	2016-11-13 06:00:00+00
2788	10	2016-11-13 21:00:00+00	2016-11-14 06:00:00+00
2789	10	2016-11-14 21:00:00+00	2016-11-15 06:00:00+00
2790	10	2016-11-15 21:00:00+00	2016-11-16 06:00:00+00
2791	10	2016-11-16 21:00:00+00	2016-11-17 06:00:00+00
2792	10	2016-11-17 21:00:00+00	2016-11-18 06:00:00+00
2793	10	2016-11-18 21:00:00+00	2016-11-19 06:00:00+00
2794	10	2016-11-19 21:00:00+00	2016-11-20 06:00:00+00
2795	10	2016-11-20 21:00:00+00	2016-11-21 06:00:00+00
2796	10	2016-11-21 21:00:00+00	2016-11-22 06:00:00+00
2797	10	2016-11-22 21:00:00+00	2016-11-23 06:00:00+00
2798	10	2016-11-23 21:00:00+00	2016-11-24 06:00:00+00
2799	10	2016-11-24 21:00:00+00	2016-11-25 06:00:00+00
2800	10	2016-11-25 21:00:00+00	2016-11-26 06:00:00+00
2801	10	2016-11-26 21:00:00+00	2016-11-27 06:00:00+00
2802	10	2016-11-27 21:00:00+00	2016-11-28 06:00:00+00
2803	10	2016-11-28 21:00:00+00	2016-11-29 06:00:00+00
2804	10	2016-11-29 21:00:00+00	2016-11-30 06:00:00+00
2805	10	2016-11-30 21:00:00+00	2016-12-01 06:00:00+00
2824	9	2016-11-01 21:00:00+00	2016-11-02 06:00:00+00
2825	9	2016-11-02 21:00:00+00	2016-11-03 06:00:00+00
2826	9	2016-11-03 21:00:00+00	2016-11-04 06:00:00+00
2827	9	2016-11-04 21:00:00+00	2016-11-05 06:00:00+00
2828	9	2016-11-05 21:00:00+00	2016-11-06 06:00:00+00
2829	9	2016-11-06 21:00:00+00	2016-11-07 06:00:00+00
2830	9	2016-11-07 21:00:00+00	2016-11-08 06:00:00+00
2831	9	2016-11-08 21:00:00+00	2016-11-11 06:00:00+00
2832	9	2016-11-12 21:00:00+00	2016-11-13 06:00:00+00
2833	9	2016-11-13 21:00:00+00	2016-11-14 06:00:00+00
2834	9	2016-11-14 21:00:00+00	2016-11-15 06:00:00+00
2835	9	2016-11-15 21:00:00+00	2016-11-16 06:00:00+00
2836	9	2016-11-16 21:00:00+00	2016-11-17 06:00:00+00
2837	9	2016-11-17 21:00:00+00	2016-11-18 06:00:00+00
2838	9	2016-11-18 21:00:00+00	2016-11-19 06:00:00+00
2839	9	2016-11-19 21:00:00+00	2016-11-20 06:00:00+00
2840	9	2016-11-20 21:00:00+00	2016-11-21 06:00:00+00
2841	9	2016-11-21 21:00:00+00	2016-11-22 06:00:00+00
2842	9	2016-11-22 21:00:00+00	2016-11-23 06:00:00+00
2843	9	2016-11-23 21:00:00+00	2016-11-24 06:00:00+00
2844	9	2016-11-24 21:00:00+00	2016-11-25 06:00:00+00
2845	9	2016-11-25 21:00:00+00	2016-11-26 06:00:00+00
2846	9	2016-11-26 21:00:00+00	2016-11-27 06:00:00+00
2847	9	2016-11-27 21:00:00+00	2016-11-28 06:00:00+00
2848	9	2016-11-28 21:00:00+00	2016-11-29 06:00:00+00
2849	9	2016-11-29 21:00:00+00	2016-11-30 06:00:00+00
2850	9	2016-11-30 21:00:00+00	2016-12-01 06:00:00+00
3135	13	2016-11-01 17:00:00+00	2016-11-02 02:00:00+00
3136	13	2016-11-02 17:00:00+00	2016-11-03 02:00:00+00
3137	13	2016-11-03 17:00:00+00	2016-11-04 02:00:00+00
3138	13	2016-11-04 17:00:00+00	2016-11-05 02:00:00+00
3139	13	2016-11-05 17:00:00+00	2016-11-06 02:00:00+00
3140	13	2016-11-06 17:00:00+00	2016-11-07 02:00:00+00
3141	13	2016-11-07 17:00:00+00	2016-11-08 02:00:00+00
3142	13	2016-11-08 17:00:00+00	2016-11-09 02:00:00+00
3143	13	2016-11-09 17:00:00+00	2016-11-10 02:00:00+00
3144	13	2016-11-10 17:00:00+00	2016-11-11 02:00:00+00
3145	13	2016-11-11 17:00:00+00	2016-11-12 02:00:00+00
3146	13	2016-11-12 17:00:00+00	2016-11-13 02:00:00+00
3147	13	2016-11-13 17:00:00+00	2016-11-14 02:00:00+00
3148	13	2016-11-14 17:00:00+00	2016-11-15 02:00:00+00
3149	13	2016-11-15 17:00:00+00	2016-11-16 02:00:00+00
3150	13	2016-11-16 17:00:00+00	2016-11-17 02:00:00+00
3151	13	2016-11-17 17:00:00+00	2016-11-18 02:00:00+00
3152	13	2016-11-18 17:00:00+00	2016-11-19 02:00:00+00
3153	13	2016-11-19 17:00:00+00	2016-11-20 02:00:00+00
3154	13	2016-11-20 17:00:00+00	2016-11-21 02:00:00+00
3155	13	2016-11-21 17:00:00+00	2016-11-22 02:00:00+00
3156	13	2016-11-22 17:00:00+00	2016-11-23 02:00:00+00
3157	13	2016-11-23 17:00:00+00	2016-11-24 02:00:00+00
3158	13	2016-11-24 17:00:00+00	2016-11-25 02:00:00+00
3159	13	2016-11-25 17:00:00+00	2016-11-26 02:00:00+00
3160	13	2016-11-26 17:00:00+00	2016-11-27 02:00:00+00
3161	13	2016-11-27 17:00:00+00	2016-11-28 02:00:00+00
3162	13	2016-11-28 17:00:00+00	2016-11-29 02:00:00+00
3163	13	2016-11-29 17:00:00+00	2016-11-30 02:00:00+00
3164	13	2016-11-30 17:00:00+00	2016-12-01 02:00:00+00
3165	12	2016-11-01 16:00:00+00	2016-11-02 01:00:00+00
3166	12	2016-11-02 16:00:00+00	2016-11-03 01:00:00+00
3167	12	2016-11-03 16:00:00+00	2016-11-04 01:00:00+00
3168	12	2016-11-04 16:00:00+00	2016-11-05 01:00:00+00
3169	12	2016-11-05 16:00:00+00	2016-11-06 01:00:00+00
3170	12	2016-11-06 16:00:00+00	2016-11-07 01:00:00+00
3171	12	2016-11-07 16:00:00+00	2016-11-08 01:00:00+00
3172	12	2016-11-08 16:00:00+00	2016-11-09 01:00:00+00
3173	12	2016-11-09 16:00:00+00	2016-11-10 01:00:00+00
3174	12	2016-11-10 16:00:00+00	2016-11-11 01:00:00+00
3175	12	2016-11-11 16:00:00+00	2016-11-12 01:00:00+00
3176	12	2016-11-12 16:00:00+00	2016-11-13 01:00:00+00
3177	12	2016-11-13 16:00:00+00	2016-11-14 01:00:00+00
3178	12	2016-11-14 16:00:00+00	2016-11-15 01:00:00+00
3179	12	2016-11-15 16:00:00+00	2016-11-16 01:00:00+00
3180	12	2016-11-16 16:00:00+00	2016-11-17 01:00:00+00
3181	12	2016-11-17 16:00:00+00	2016-11-18 01:00:00+00
3182	12	2016-11-18 16:00:00+00	2016-11-19 01:00:00+00
3183	12	2016-11-19 16:00:00+00	2016-11-20 01:00:00+00
3184	12	2016-11-20 16:00:00+00	2016-11-21 01:00:00+00
3185	12	2016-11-21 16:00:00+00	2016-11-22 01:00:00+00
3186	12	2016-11-22 16:00:00+00	2016-11-23 01:00:00+00
3187	12	2016-11-23 16:00:00+00	2016-11-24 01:00:00+00
3188	12	2016-11-24 16:00:00+00	2016-11-25 01:00:00+00
3189	12	2016-11-25 16:00:00+00	2016-11-26 01:00:00+00
3190	12	2016-11-26 16:00:00+00	2016-11-27 01:00:00+00
3191	12	2016-11-27 16:00:00+00	2016-11-28 01:00:00+00
3192	12	2016-11-28 16:00:00+00	2016-11-29 01:00:00+00
3193	12	2016-11-29 16:00:00+00	2016-11-30 01:00:00+00
3194	12	2016-11-30 16:00:00+00	2016-12-01 01:00:00+00
3195	8	2016-11-01 15:00:00+00	2016-11-01 21:00:00+00
3196	8	2016-11-02 15:00:00+00	2016-11-02 21:00:00+00
3197	8	2016-11-03 15:00:00+00	2016-11-03 21:00:00+00
3198	8	2016-11-04 15:00:00+00	2016-11-04 21:00:00+00
3199	8	2016-11-05 15:00:00+00	2016-11-05 21:00:00+00
3200	8	2016-11-06 15:00:00+00	2016-11-06 21:00:00+00
3201	8	2016-11-07 15:00:00+00	2016-11-07 21:00:00+00
3202	8	2016-11-08 15:00:00+00	2016-11-08 21:00:00+00
3203	8	2016-11-10 15:00:00+00	2016-11-10 21:00:00+00
3204	8	2016-11-12 15:00:00+00	2016-11-12 21:00:00+00
3205	8	2016-11-13 15:00:00+00	2016-11-13 21:00:00+00
3206	8	2016-11-14 15:00:00+00	2016-11-14 21:00:00+00
3207	8	2016-11-15 15:00:00+00	2016-11-15 21:00:00+00
3208	8	2016-11-16 15:00:00+00	2016-11-16 21:00:00+00
3209	8	2016-11-17 15:00:00+00	2016-11-17 21:00:00+00
3210	8	2016-11-18 15:00:00+00	2016-11-18 21:00:00+00
3211	8	2016-11-19 15:00:00+00	2016-11-19 21:00:00+00
3212	8	2016-11-20 15:00:00+00	2016-11-20 21:00:00+00
3213	8	2016-11-21 15:00:00+00	2016-11-21 21:00:00+00
3214	8	2016-11-22 15:00:00+00	2016-11-22 21:00:00+00
3215	8	2016-11-23 15:00:00+00	2016-11-23 21:00:00+00
3216	8	2016-11-24 15:00:00+00	2016-11-24 21:00:00+00
3217	8	2016-11-25 15:00:00+00	2016-11-25 21:00:00+00
3218	8	2016-11-26 15:00:00+00	2016-11-26 21:00:00+00
3219	8	2016-11-27 15:00:00+00	2016-11-27 21:00:00+00
3220	8	2016-11-28 15:00:00+00	2016-11-28 21:00:00+00
3221	8	2016-11-29 15:00:00+00	2016-11-29 21:00:00+00
3222	8	2016-11-30 15:00:00+00	2016-11-30 21:00:00+00
3223	10	2016-11-01 15:00:00+00	2016-11-01 21:00:00+00
3224	10	2016-11-02 15:00:00+00	2016-11-02 21:00:00+00
3225	10	2016-11-03 15:00:00+00	2016-11-03 21:00:00+00
3226	10	2016-11-04 15:00:00+00	2016-11-04 21:00:00+00
3227	10	2016-11-05 15:00:00+00	2016-11-05 21:00:00+00
3228	10	2016-11-06 15:00:00+00	2016-11-06 21:00:00+00
3229	10	2016-11-07 15:00:00+00	2016-11-07 21:00:00+00
3230	10	2016-11-08 15:00:00+00	2016-11-08 21:00:00+00
3231	10	2016-11-10 15:00:00+00	2016-11-10 21:00:00+00
3232	10	2016-11-12 15:00:00+00	2016-11-12 21:00:00+00
3233	10	2016-11-13 15:00:00+00	2016-11-13 21:00:00+00
3234	10	2016-11-14 15:00:00+00	2016-11-14 21:00:00+00
3235	10	2016-11-15 15:00:00+00	2016-11-15 21:00:00+00
3236	10	2016-11-16 15:00:00+00	2016-11-16 21:00:00+00
3237	10	2016-11-17 15:00:00+00	2016-11-17 21:00:00+00
3238	10	2016-11-18 15:00:00+00	2016-11-18 21:00:00+00
3239	10	2016-11-19 15:00:00+00	2016-11-19 21:00:00+00
3240	10	2016-11-20 15:00:00+00	2016-11-20 21:00:00+00
3241	10	2016-11-21 15:00:00+00	2016-11-21 21:00:00+00
3242	10	2016-11-22 15:00:00+00	2016-11-22 21:00:00+00
3243	10	2016-11-23 15:00:00+00	2016-11-23 21:00:00+00
3244	10	2016-11-24 15:00:00+00	2016-11-24 21:00:00+00
3245	10	2016-11-25 15:00:00+00	2016-11-25 21:00:00+00
3246	10	2016-11-26 15:00:00+00	2016-11-26 21:00:00+00
3247	10	2016-11-27 15:00:00+00	2016-11-27 21:00:00+00
3248	10	2016-11-28 15:00:00+00	2016-11-28 21:00:00+00
3249	10	2016-11-29 15:00:00+00	2016-11-29 21:00:00+00
3250	10	2016-11-30 15:00:00+00	2016-11-30 21:00:00+00
3251	9	2016-11-01 15:00:00+00	2016-11-01 21:00:00+00
3252	9	2016-11-02 15:00:00+00	2016-11-02 21:00:00+00
3253	9	2016-11-03 15:00:00+00	2016-11-03 21:00:00+00
3254	9	2016-11-04 15:00:00+00	2016-11-04 21:00:00+00
3255	9	2016-11-05 15:00:00+00	2016-11-05 21:00:00+00
3256	9	2016-11-06 15:00:00+00	2016-11-06 21:00:00+00
3257	9	2016-11-07 15:00:00+00	2016-11-07 21:00:00+00
3258	9	2016-11-08 15:00:00+00	2016-11-08 21:00:00+00
3259	9	2016-11-12 15:00:00+00	2016-11-12 21:00:00+00
3260	9	2016-11-13 15:00:00+00	2016-11-13 21:00:00+00
3261	9	2016-11-14 15:00:00+00	2016-11-14 21:00:00+00
3262	9	2016-11-15 15:00:00+00	2016-11-15 21:00:00+00
3263	9	2016-11-16 15:00:00+00	2016-11-16 21:00:00+00
3264	9	2016-11-17 15:00:00+00	2016-11-17 21:00:00+00
3265	9	2016-11-18 15:00:00+00	2016-11-18 21:00:00+00
3266	9	2016-11-19 15:00:00+00	2016-11-19 21:00:00+00
3267	9	2016-11-20 15:00:00+00	2016-11-20 21:00:00+00
3268	9	2016-11-21 15:00:00+00	2016-11-21 21:00:00+00
3269	9	2016-11-22 15:00:00+00	2016-11-22 21:00:00+00
3270	9	2016-11-23 15:00:00+00	2016-11-23 21:00:00+00
3271	9	2016-11-24 15:00:00+00	2016-11-24 21:00:00+00
3272	9	2016-11-25 15:00:00+00	2016-11-25 21:00:00+00
3273	9	2016-11-26 15:00:00+00	2016-11-26 21:00:00+00
3274	9	2016-11-27 15:00:00+00	2016-11-27 21:00:00+00
3275	9	2016-11-28 15:00:00+00	2016-11-28 21:00:00+00
3276	9	2016-11-29 15:00:00+00	2016-11-29 21:00:00+00
3277	9	2016-11-30 15:00:00+00	2016-11-30 21:00:00+00
3278	13	2016-11-01 11:00:00+00	2016-11-01 17:00:00+00
3279	13	2016-11-02 11:00:00+00	2016-11-02 17:00:00+00
3280	13	2016-11-03 11:00:00+00	2016-11-03 17:00:00+00
3281	13	2016-11-04 11:00:00+00	2016-11-04 17:00:00+00
3282	13	2016-11-05 11:00:00+00	2016-11-05 17:00:00+00
3283	13	2016-11-06 11:00:00+00	2016-11-06 17:00:00+00
3284	13	2016-11-07 11:00:00+00	2016-11-07 17:00:00+00
3285	13	2016-11-08 11:00:00+00	2016-11-08 17:00:00+00
3286	13	2016-11-09 11:00:00+00	2016-11-09 17:00:00+00
3287	13	2016-11-10 11:00:00+00	2016-11-10 17:00:00+00
3288	13	2016-11-11 11:00:00+00	2016-11-11 17:00:00+00
3289	13	2016-11-12 11:00:00+00	2016-11-12 17:00:00+00
3290	13	2016-11-13 11:00:00+00	2016-11-13 17:00:00+00
3291	13	2016-11-14 11:00:00+00	2016-11-14 17:00:00+00
3292	13	2016-11-15 11:00:00+00	2016-11-15 17:00:00+00
3293	13	2016-11-16 11:00:00+00	2016-11-16 17:00:00+00
3294	13	2016-11-17 11:00:00+00	2016-11-17 17:00:00+00
3295	13	2016-11-18 11:00:00+00	2016-11-18 17:00:00+00
3296	13	2016-11-19 11:00:00+00	2016-11-19 17:00:00+00
3297	13	2016-11-20 11:00:00+00	2016-11-20 17:00:00+00
3298	13	2016-11-21 11:00:00+00	2016-11-21 17:00:00+00
3299	13	2016-11-22 11:00:00+00	2016-11-22 17:00:00+00
3300	13	2016-11-23 11:00:00+00	2016-11-23 17:00:00+00
3301	13	2016-11-24 11:00:00+00	2016-11-24 17:00:00+00
3302	13	2016-11-25 11:00:00+00	2016-11-25 17:00:00+00
3303	13	2016-11-26 11:00:00+00	2016-11-26 17:00:00+00
3304	13	2016-11-27 11:00:00+00	2016-11-27 17:00:00+00
3305	13	2016-11-28 11:00:00+00	2016-11-28 17:00:00+00
3306	13	2016-11-29 11:00:00+00	2016-11-29 17:00:00+00
3307	13	2016-11-30 11:00:00+00	2016-11-30 17:00:00+00
3311	7	2016-10-24 13:00:00+00	2016-10-24 13:30:00+00
3312	7	2016-11-01 06:00:00+00	2016-11-01 15:00:00+00
3313	7	2016-11-08 06:00:00+00	2016-11-08 15:00:00+00
3314	7	2016-11-15 06:00:00+00	2016-11-15 15:00:00+00
3315	7	2016-11-22 06:00:00+00	2016-11-22 15:00:00+00
3316	7	2016-11-29 06:00:00+00	2016-11-29 15:00:00+00
3317	7	2016-11-27 06:00:00+00	2016-11-27 15:00:00+00
3318	7	2016-11-26 06:00:00+00	2016-11-26 15:00:00+00
3319	7	2016-11-20 06:00:00+00	2016-11-20 15:00:00+00
3320	7	2016-11-19 06:00:00+00	2016-11-19 15:00:00+00
3321	7	2016-11-13 06:00:00+00	2016-11-13 15:00:00+00
3322	7	2016-11-12 06:00:00+00	2016-11-12 15:00:00+00
3323	7	2016-11-06 06:00:00+00	2016-11-06 15:00:00+00
3324	7	2016-11-05 06:00:00+00	2016-11-05 15:00:00+00
3325	7	2016-11-02 10:00:00+00	2016-11-02 11:00:00+00
3326	7	2016-11-03 10:00:00+00	2016-11-03 11:00:00+00
3327	7	2016-11-04 10:00:00+00	2016-11-04 11:00:00+00
3328	7	2016-11-07 10:00:00+00	2016-11-07 11:00:00+00
3329	7	2016-11-09 10:00:00+00	2016-11-09 11:00:00+00
3330	7	2016-11-10 10:00:00+00	2016-11-10 11:00:00+00
3331	7	2016-11-11 10:00:00+00	2016-11-11 11:00:00+00
3332	7	2016-11-14 10:00:00+00	2016-11-14 11:00:00+00
3333	7	2016-11-16 10:00:00+00	2016-11-16 11:00:00+00
3334	7	2016-11-17 10:00:00+00	2016-11-17 11:00:00+00
3335	7	2016-11-18 10:00:00+00	2016-11-18 11:00:00+00
3336	7	2016-11-21 10:00:00+00	2016-11-21 11:00:00+00
3337	7	2016-11-23 10:00:00+00	2016-11-23 11:00:00+00
3338	7	2016-11-24 10:00:00+00	2016-11-24 11:00:00+00
3339	7	2016-11-25 10:00:00+00	2016-11-25 11:00:00+00
3340	7	2016-11-28 10:00:00+00	2016-11-28 11:00:00+00
3341	7	2016-11-30 10:00:00+00	2016-11-30 11:00:00+00
3344	11	2016-10-25 07:00:00+00	2016-10-25 11:00:00+00
3345	14	2016-10-26 06:00:00+00	2016-10-26 09:00:00+00
3346	7	2016-10-28 07:00:00+00	2016-10-28 10:00:00+00
3347	7	2016-10-26 12:00:00+00	2016-10-26 12:30:00+00
3348	7	2016-10-26 07:30:00+00	2016-10-26 08:00:00+00
3349	7	2016-10-26 12:30:00+00	2016-10-26 13:00:00+00
3350	7	2016-10-27 13:30:00+00	2016-10-27 14:30:00+00
3351	7	2016-10-26 11:00:00+00	2016-10-26 11:30:00+00
3352	11	2016-10-26 07:30:00+00	2016-10-26 08:00:00+00
3353	7	2016-10-26 08:00:00+00	2016-10-26 08:30:00+00
3354	7	2016-10-26 09:00:00+00	2016-10-26 09:30:00+00
3356	9	2016-10-27 06:00:00+00	2016-10-27 06:30:00+00
3357	7	2016-11-02 06:00:00+00	2016-11-02 06:30:00+00
3358	7	2016-10-28 11:00:00+00	2016-10-28 12:30:00+00
3359	7	2016-10-31 06:00:00+00	2016-10-31 10:00:00+00
3360	7	2016-10-28 06:00:00+00	2016-10-28 06:30:00+00
3363	11	2016-10-28 04:00:00+00	2016-10-28 04:30:00+00
3364	7	2016-10-28 12:30:00+00	2016-10-28 13:30:00+00
3365	11	2016-11-03 08:00:00+00	2016-11-03 08:30:00+00
3366	14	2016-10-31 06:00:00+00	2016-10-31 09:00:00+00
3367	14	2016-10-31 09:00:00+00	2016-10-31 10:00:00+00
3368	14	2016-10-28 09:00:00+00	2016-10-28 10:00:00+00
3371	10	2016-11-01 08:00:00+00	2016-11-01 14:00:00+00
3372	15	2016-11-03 11:00:00+00	2016-11-03 14:00:00+00
3373	7	2016-10-31 11:00:00+00	2016-10-31 11:30:00+00
3374	7	2016-10-31 11:30:00+00	2016-10-31 12:30:00+00
3376	7	2016-10-31 12:30:00+00	2016-10-31 13:30:00+00
3377	14	2016-10-28 13:00:00+00	2016-10-28 13:30:00+00
3378	7	2016-10-31 13:30:00+00	2016-10-31 14:30:00+00
3379	14	2016-10-31 11:00:00+00	2016-10-31 12:00:00+00
3380	11	2016-10-31 08:30:00+00	2016-10-31 09:00:00+00
3381	14	2016-11-02 10:00:00+00	2016-11-02 11:00:00+00
3382	14	2016-11-01 21:00:00+00	2016-11-02 06:00:00+00
3383	14	2016-11-02 15:00:00+00	2016-11-02 21:00:00+00
3384	14	2016-10-31 21:00:00+00	2016-11-01 21:00:00+00
3385	14	2016-11-03 10:00:00+00	2016-11-03 11:00:00+00
3386	14	2016-11-02 21:00:00+00	2016-11-03 06:00:00+00
3387	14	2016-11-03 15:00:00+00	2016-11-03 21:00:00+00
3388	14	2016-11-03 21:00:00+00	2016-11-04 06:00:00+00
3389	14	2016-11-04 10:00:00+00	2016-11-04 11:00:00+00
3390	14	2016-11-04 15:00:00+00	2016-11-04 21:00:00+00
3391	14	2016-11-04 21:00:00+00	2016-11-05 21:00:00+00
3392	14	2016-11-05 21:00:00+00	2016-11-06 21:00:00+00
3393	14	2016-11-07 15:00:00+00	2016-11-07 21:00:00+00
3394	14	2016-11-06 21:00:00+00	2016-11-07 06:00:00+00
3395	14	2016-11-07 10:00:00+00	2016-11-07 11:00:00+00
3396	14	2016-11-07 21:00:00+00	2016-11-08 21:00:00+00
3398	7	2016-11-03 07:00:00+00	2016-11-03 10:00:00+00
3402	7	2016-11-02 08:00:00+00	2016-11-02 10:00:00+00
3430	11	2016-11-08 07:00:00+00	2016-11-08 11:00:00+00
3431	7	2016-11-03 11:00:00+00	2016-11-03 11:30:00+00
3432	7	2016-11-03 11:30:00+00	2016-11-03 12:00:00+00
3433	7	2016-11-03 12:00:00+00	2016-11-03 12:30:00+00
3436	14	2016-11-03 11:00:00+00	2016-11-03 11:30:00+00
3437	7	2016-11-03 13:00:00+00	2016-11-03 14:00:00+00
3439	14	2016-11-03 13:00:00+00	2016-11-03 14:00:00+00
3440	7	2016-11-03 06:30:00+00	2016-11-03 07:00:00+00
3446	14	2016-11-03 11:30:00+00	2016-11-03 12:30:00+00
3447	7	2016-11-07 11:00:00+00	2016-11-07 12:00:00+00
3448	7	2016-11-07 06:00:00+00	2016-11-07 07:00:00+00
3449	7	2016-11-07 07:00:00+00	2016-11-07 10:00:00+00
3452	14	2016-11-03 09:30:00+00	2016-11-03 10:00:00+00
3453	7	2016-11-07 12:00:00+00	2016-11-07 13:00:00+00
\.


--
-- TOC entry 2326 (class 0 OID 0)
-- Dependencies: 191
-- Name: doc_periods_docperiodid_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('doc_periods_docperiodid_seq', 3453, true);


--
-- TOC entry 2265 (class 0 OID 20943)
-- Dependencies: 190
-- Data for Name: docs; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY docs (docid, docname, warehouseid) FROM stdin;
7	Разгрузочный док №1	3
8	Разгрузочный док №1	4
9	Разгрузочный док №1	5
10	Разгрузочный док №1	6
11	Разгрузочный док №1	7
12	Разгрузочный док №1	8
13	Разгрузочный док №1	9
14	Разгрузочный док №2	3
15	spb_warehouse_doc1	10
\.


--
-- TOC entry 2327 (class 0 OID 0)
-- Dependencies: 189
-- Name: docs_docid_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('docs_docid_seq', 15, true);


--
-- TOC entry 2272 (class 0 OID 21014)
-- Dependencies: 197
-- Data for Name: donut_doc_periods; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY donut_doc_periods (donutdocperiodid, creationdate, commentfordonut, driver, driverphonenumber, licenseplate, palletsqty, supplieruserid, lastmodified) FROM stdin;
1023	2016-09-01		Овчинников Б.В.	243	цпукп	15	526	2016-09-01 11:43:13.959203+00
203	2016-07-14				1	15	12	2016-07-14 12:17:49.806692+00
1024	2016-09-01					0	526	2016-09-01 11:48:45.23345+00
1028	2016-09-02		Соколов А,К.	89176395873	У147АВ/73	33	528	2016-09-02 08:48:12.452724+00
1031	2016-09-02	груз не на паллетах	Руденко Владимир Александрович	89212156469	М767КА60	0	89	2016-09-02 12:01:22.456837+00
1032	2016-09-02		Чувакин Евгений Боривич	89851298188	м359рк197	5	465	2016-09-02 12:47:22.121614+00
1033	2016-09-05		Гасанов Рауф Ханбала Оглы	89162457658	Х814МК	3	69	2016-09-05 06:09:13.101676+00
1036	2016-09-05		кислицын николай викторович	89772703809	к664хв197	2	68	2016-09-05 06:41:56.9658+00
555	2016-07-20		Курушев Андрей	89163140142	Т281ЕС777	15	12	2016-07-20 09:21:09.953448+00
556	2016-07-21		Алексеев Сергей	89150954742	У158ОР77	8	12	2016-07-21 09:17:38.212968+00
560	2016-07-27		Алексеев Сергей	89150954247	У158ОР77	12	12	2016-07-27 09:00:53.971644+00
561	2016-07-28		Львов Сергей Николаевич	89261581792	Е272АO77	14	57	2016-07-28 09:28:03.152018+00
562	2016-07-28		Пустовойт В.И.	89652411295	к580ро197	4	53	2016-07-28 13:25:28.361821+00
1042	2016-09-06		Буланов И.А.	9266818297	х951вн50	5	128	2016-09-06 08:56:50.067962+00
1049	2016-09-07		Кузнецов Валерий Иваныч	89655104117	н846кх	0	472	2016-09-07 10:06:37.335201+00
801	2016-08-03		Курушев Андрей	89163140142	Т281УС777	15	12	2016-08-03 08:01:57.413198+00
1050	2016-09-07	кол-во паллет ориентировочное	Якуб Лилиан		е110тт190	7	59	2016-09-07 12:28:40.414058+00
803	2016-08-03		Алексеев Сергей	89150954247	У158ОР77	10	12	2016-08-03 13:13:51.107528+00
1051	2016-09-09		Гасанов Рауф Ханбала Оглы	89162457658	Х814МК	6	69	2016-09-09 08:58:33.175062+00
806	2016-08-04		Купцов Н.А.	8909975058	О138ТЕ58	4	53	2016-08-04 07:21:25.372286+00
811	2016-08-05		Заниборщ Роман Григорьевич	9164400532	О237УО	5	69	2016-08-05 11:00:10.573483+00
1052	2016-09-09		Чиканчи Иван Степанович		А378ТО197	5	465	2016-09-09 12:24:07.571651+00
813	2016-08-05					0	76	2016-08-05 12:44:26.657195+00
816	2016-08-08		Курушев Андрей	89163140142	Т281ЕС777	15	12	2016-08-08 07:36:27.230911+00
817	2016-08-08		Алексеев Сергей	89150954247	У158ОР77	12	12	2016-08-08 07:37:49.552047+00
832	2016-08-10		Курушев Андрей	89163140142	Т281ЕС777	15	12	2016-08-10 09:56:27.377546+00
833	2016-08-10		Алексеев Сергей	89150954247	У158ОР77	11	12	2016-08-10 09:59:24.142836+00
834	2016-08-10		Якуб Лилиан	89266000922	е110тт190	0	59	2016-08-10 13:35:32.908166+00
837	2016-08-12		Курушев Андрей	89163140142	Т281ЕС777	15	12	2016-08-12 08:50:32.806255+00
839	2016-08-12	товар предоставляется в коробках	Сошин М.И.	89166809293	Е511РН190	15	67	2016-08-12 09:35:29.939619+00
840	2016-08-12		Жданович Вячеслав Викторович	89856208022	У774ТХ	5	69	2016-08-12 11:13:36.140292+00
1055	2016-09-09		Ахунов Н.А.	89050356986	В566УУ/73	33	528	2016-09-09 13:27:33.639019+00
1056	2016-09-09		Круглов Н.М.		У361ОТ 96	27	527	2016-09-09 13:36:52.185657+00
851	2016-08-17		Курушев Андрей	89163140142	Т281ЕС777	15	12	2016-08-17 12:36:58.867057+00
1053	2016-09-09		кислицын николай викторович	89772703809	М496ВА197	6	68	2016-09-09 12:59:59.176807+00
850	2016-08-16		Лебедев Сергей Владимирович	89266000986	с874аа 77	5	59	2016-08-16 07:09:13.516311+00
852	2016-08-19		Гасанов Рауф Ханбала Оглы	89162458658	Х814МК150	4	69	2016-08-19 09:22:58.071092+00
854	2016-08-22		Виноградов А	89773174131	0581ко777	4	53	2016-08-22 07:11:32.38165+00
856	2016-08-24		Плотников Григорий Владимирович	89193698621	У509НУ96	2	64	2016-08-24 12:33:50.854364+00
858	2016-08-24		Милов Николай	89096468700	У158ОР77	11	12	2016-08-24 13:21:28.631958+00
859	2016-08-24		Курушев Андрей	89163140142	Т281ЕС777	15	12	2016-08-24 13:24:18.793444+00
860	2016-08-25		Львов  С.Н.	89261581792	Е272АО77	14	57	2016-08-25 07:22:53.614589+00
862	2016-08-25		Акулин Юрий Алексеевич	89299870993	в453ва	3	69	2016-08-25 12:36:36.533033+00
861	2016-08-25		Туханов Михаил Павлович	89266000621	т886та199	3	59	2016-08-25 12:25:03.373442+00
863	2016-08-26		Гасанов Рауф Ханбала Оглы	89162457658	х814мк	2	69	2016-08-26 12:10:08.39229+00
864	2016-08-29		Милов Николай	89096468700	У158ОР77	11	12	2016-08-29 08:14:11.169352+00
865	2016-08-29		Курушев Андрей	89163140142	Т281ЕС777	15	12	2016-08-29 08:15:19.431734+00
1487	2016-09-12		Новиков А.А	89063923156	О296КЕ 58	33	528	2016-09-12 13:34:25.623785+00
1490	2016-09-12		Усманов р.р.			33	527	2016-09-12 14:15:05.914591+00
1491	2016-09-13		Кузнецов В.И.	89655104117		0	472	2016-09-13 03:42:13.453697+00
970	2016-08-30		Матяжов С.В.	89152546306	у742ам50	10	53	2016-08-30 12:03:26.532978+00
1495	2016-09-13		Маторин	89043827525	В317ВР	1	62	2016-09-13 11:09:53.937759+00
1496	2016-09-13		Акулин Юрий Алексеевич	89299870993	В453ВА	2	69	2016-09-13 11:32:37.080781+00
1502	2016-09-13		Кудряшов Александр Михайлович	89607031017	А470КМ/69	0	538	2016-09-13 12:58:37.440527+00
1503	2016-09-13		Кудряшов Александр Михайлович		А470КМ/69	0	538	2016-09-13 13:04:40.986585+00
1504	2016-09-13		Кудряшов Александр Михайлович		А470КМ/69	0	538	2016-09-13 13:05:19.719043+00
994	2016-08-31	а/м Газель 	Морозов Андрей Валентинович	89538203043	О975КО 96	0	472	2016-08-31 11:07:35.162425+00
1505	2016-09-13		Ботнарь Алексей Алексеевич		с782сн197	6	59	2016-09-13 13:09:56.523089+00
997	2016-08-31		Ботнарь Станислав Алексеевич		а626вм37	6	59	2016-08-31 13:00:57.021291+00
1090	2016-09-12		Журавлев Андрей	89163638151	О904ОЕ777	1	70	2016-09-12 10:44:28.95551+00
1507	2016-09-14		Коршунов Анатолий Николаевич	89296235264	А544АК50	4	15	2016-09-14 08:08:46.208197+00
2208	2016-09-14				Газель	6	92	2016-09-14 14:27:21.445981+00
1003	2016-09-01		Курушев Андрей	89163140142	Т281ЕС777	9	12	2016-09-01 08:08:33.794959+00
1004	2016-09-01		Алексеев Сергей	89150954247	У158ОР77	8	12	2016-09-01 08:09:15.658327+00
2246	2016-09-15		Дмитриус Дмитрий 	89055364221	в592нр750	2	94	2016-09-15 05:29:31.958445+00
2247	2016-09-15		Пустовойтов В.И.	89652411295	к580ро197	4	53	2016-09-15 07:17:48.828681+00
2249	2016-09-15		Виноградов А	89773174131	о581ко777	4	53	2016-09-15 07:21:03.116368+00
2256	2016-09-15		Возный С.М.	89688031014	341оу190	2	126	2016-09-15 14:04:18.407818+00
2250	2016-09-15		Овчинников Б.В.	89163579245	А427ЕТ50	15	528	2016-09-15 11:52:15.870827+00
2251	2016-09-15		Ахунов Н.А,	89050356986	В566УУ73	33	528	2016-09-15 11:53:55.377724+00
2254	2016-09-15		Чирковский Игорь	89161586483	О183УН190	8	534	2016-09-15 12:53:32.525217+00
2259	2016-09-16		Акулин Юрий Алексеевич	89299870993	В453ВА	3	69	2016-09-16 07:26:32.414548+00
2260	2016-09-16		Чебесков 	89251528574		2	15	2016-09-16 08:14:55.877575+00
2263	2016-09-16		Анеликов С А	89154323092	м495ва197	3	68	2016-09-16 13:03:20.166236+00
2261	2016-09-16		Журавлев Андрей	89163638151	О904ОЕ777	0	70	2016-09-16 12:12:07.004932+00
2257	2016-09-16					0	57	2016-09-16 06:09:18.370296+00
2262	2016-09-16		Чувакин Евгений Борисович	89851298188	К359РК197	5	465	2016-09-16 12:35:51.57417+00
2264	2016-09-19		Кузнецов Андрей Викторович	89113682060	А059КВ	0	89	2016-09-19 10:33:59.855074+00
2268	2016-09-20					14	57	2016-09-20 08:07:07.643231+00
2272	2016-09-20		Ляхов С.П. 	89090076425	С 049 ВН 	2	496	2016-09-20 12:19:58.418607+00
2278	2016-09-20					0	59	2016-09-20 13:14:32.295606+00
2271	2016-09-20		Хапилов О Б	89154323236	в374ку197	4	68	2016-09-20 10:45:31.584481+00
2283	2016-09-21		Пустовойт В.И.	89652411295	к580ро197	3	53	2016-09-21 09:35:10.365741+00
2285	2016-09-21		Белов Руслан Вячеславович	89268289555	в593нр750	2	94	2016-09-21 11:28:28.845961+00
2286	2016-09-22					3	92	2016-09-22 06:27:33.932928+00
2287	2016-09-22		Овчинников Б.В.	89163579245	А427ЕТ50 	15	528	2016-09-22 08:26:16.625102+00
2288	2016-09-22					33	528	2016-09-22 08:27:00.049678+00
2302	2016-09-23					0	57	2016-09-23 07:13:33.499048+00
2354	2016-10-03					0	81	2016-10-03 07:33:14.382637+00
2304	2016-09-23		Кудряшов А.М.		А470КМ/69	0	538	2016-09-23 10:04:23.505423+00
2306	2016-09-23		Павлов М.А.	89264957040	н077ср90	4	53	2016-09-23 12:28:12.514192+00
2308	2016-09-23		Павлов М.А.	89264957040	н077ср90	4	53	2016-09-23 13:40:54.867869+00
2311	2016-09-26		Матвеев Ю.Н.		К581МН	8	465	2016-09-26 04:29:01.193509+00
2305	2016-09-23		хапилов о б 	89154323236	в374ку199	3	68	2016-09-23 10:18:12.499893+00
2312	2016-09-26		Морозов Максим	89057709373	О648РА777	8	12	2016-09-26 06:55:14.473361+00
2313	2016-09-26		Курушев Андрей	89163140142	Т281ЕС777	15	12	2016-09-26 06:56:17.037864+00
2314	2016-09-26		Милов Николай	89096468700	У158ОР77	12	12	2016-09-26 06:57:08.603208+00
2315	2016-09-26		Бобков Дмитрий Сергеевич	89167048378	Р281РУ	5	69	2016-09-26 07:03:15.729337+00
2284	2016-09-21		Котельников Алексей Павлович		Н095ХК190	1	131	2016-09-21 10:33:16.693284+00
2316	2016-09-26		Рубцов валерий Павлович	89209462250	В 627 НВ	0	89	2016-09-26 08:42:53.274911+00
2355	2016-10-03	Экогарант	Петров Александр	89168384433		1	553	2016-10-03 09:02:49.042649+00
2317	2016-09-26					18	96	2016-09-26 11:14:55.158876+00
2318	2016-09-26	товар предоставляется в коробках 	Самойлов Сергей евгеньевич	89169866633	H202EX190	0	67	2016-09-26 12:18:14.027133+00
2320	2016-09-27					0	65	2016-09-27 12:38:14.327517+00
2319	2016-09-27		Гришин	89197238912 	а856ав177	2	126	2016-09-27 07:29:25.139097+00
2327	2016-09-28		Павлов М.А.	89264957040	н077ср90	4	53	2016-09-28 09:07:18.79933+00
2328	2016-09-28		Павлов М.А.	89264957040	н077ср90	4	53	2016-09-28 09:08:57.921239+00
2329	2016-09-28		Острягин Владимир Петрович	89066504672	о705ех777	4	552	2016-09-28 09:13:11.409079+00
2330	2016-09-28		Львов С.Н.			0	57	2016-09-28 11:17:47.80888+00
2331	2016-09-28		Нефедов Н.Я		С746ОА197	4	549	2016-09-28 12:29:47.480599+00
2332	2016-09-28					0	59	2016-09-28 12:42:27.710008+00
2334	2016-09-29		Лазарев В.А.		436СА/69	0	538	2016-09-29 05:18:40.935085+00
2336	2016-09-29		Конопля		в037ск150	6	128	2016-09-29 07:55:16.475475+00
2341	2016-09-29					0	122	2016-09-29 08:48:11.136142+00
2343	2016-09-29		Бобков Дмитрий Сергеевич	89167048378	О725ВР	5	69	2016-09-29 12:06:34.611691+00
2344	2016-09-30		Пустовойтов В.И.	89652411295	к580ро197	3	53	2016-09-30 07:41:13.367519+00
2348	2016-09-30		Курушев Андрей	89163140142	Т281ЕС777	15	12	2016-09-30 10:23:27.405118+00
2349	2016-09-30		Алексеев Сергей	89150954247	У158ОР77	12	12	2016-09-30 10:26:07.977302+00
2352	2016-10-03		ЧувакинЕвгений Борисович		м359рк197	5	465	2016-10-03 04:43:45.956831+00
2359	2016-10-04		Ляхов С.П. 	89090076425	С 049 ВН 	0	496	2016-10-04 08:37:37.891055+00
2345	2016-09-30		Хапилов Олег Борисович	89154323236	в374ку199	3	68	2016-09-30 08:34:09.235536+00
2353	2016-10-03		Засядкин Александр Евгеньевич	89151875004	М004РН190	4	81	2016-10-03 06:28:58.927012+00
2360	2016-10-04		Кислицин			0	68	2016-10-04 10:54:04.886233+00
2361	2016-10-04					0	59	2016-10-04 11:25:06.110431+00
2333	2016-09-29		Черненок Сергей Александрович	89773301412	Е961НР177	0	503	2016-09-29 04:53:04.170134+00
2364	2016-10-05		Журавлев Андрей Владиславович	9163638151	О904ОЕ777	2	70	2016-10-05 04:50:54.248083+00
2366	2016-10-05		Кудряшов А.М.		А470КМ/69	0	538	2016-10-05 05:10:32.406182+00
2362	2016-10-04		Кислицын Н В	89772703809	в664ку199	6	68	2016-10-04 13:12:55.918939+00
2368	2016-10-05		Пустовойтов В.И.	89652411295	к580ро197	4	53	2016-10-05 07:47:29.561929+00
2357	2016-10-04		Беляев Иван Васильевич	89670945377	к753ст197	2	126	2016-10-04 08:05:06.651455+00
2370	2016-10-06	возможны изменения в данных на машину и водителя	Александров Генадий Валерьевич	89111112171	В321В	1	559	2016-10-06 09:26:14.501754+00
2358	2016-10-04		Иванов			9	562	2016-10-04 08:05:43.824007+00
2373	2016-10-06		Бровченко Юрий Викторович		М134ХХ	4	465	2016-10-06 12:52:00.739289+00
2374	2016-10-06		Овчинников Петр		С746ОА197	2	549	2016-10-06 14:59:52.002893+00
2375	2016-10-07		Курушев Андрей	89163140142	Т281ЕС777	15	12	2016-10-07 05:44:50.239552+00
2376	2016-10-07		Павлов М.А.	89264957040	н077ср90	4	53	2016-10-07 07:23:14.031161+00
2378	2016-10-07					0	68	2016-10-07 12:03:15.225367+00
2377	2016-10-07		Кислицын НВ	+79772703809	к664хв197	1	68	2016-10-07 12:03:10.532441+00
2379	2016-10-10		Пустовойт В.И.	89652411295	к580ро197	4	53	2016-10-10 08:12:12.54367+00
2380	2016-10-10		Милов Николай	89096468700	М186ТР777	8	12	2016-10-10 08:34:57.87472+00
2381	2016-10-10		Курушев Андрей	89163140142	Т281ЕС777	15	12	2016-10-10 08:35:51.528603+00
2367	2016-10-05		Черненок С.А.	89773301412	Е961НР177	0	503	2016-10-05 06:16:02.18637+00
2385	2016-10-11					0	59	2016-10-11 11:17:07.466074+00
2386	2016-10-12		Москалев К.Е.	89612208500	А050АР154	0	503	2016-10-12 03:38:13.572167+00
2387	2016-10-12		Москалев К.Е.	89612208500	А050АР154	0	503	2016-10-12 03:44:49.487765+00
2369	2016-10-06		Истомин В.С.	89221998147	в659сн196	0	503	2016-10-06 05:10:01.929415+00
2388	2016-10-12		Кудряшов Н.М.		О708ЕС/69	0	538	2016-10-12 09:07:25.924292+00
2389	2016-10-12		Ляхов С.П. 	89090076425	С 049 ВН	0	496	2016-10-12 11:11:18.016314+00
2390	2016-10-12		Буланов		х951вн50	3	128	2016-10-12 12:20:43.722847+00
2396	2016-10-13		Чарышкин О.В.	89222156836	х947ое96	0	503	2016-10-13 06:48:10.67703+00
2392	2016-10-12		Курушев Андрей	89163140142	Т281ЕС777	15	12	2016-10-12 13:21:36.296107+00
2395	2016-10-13		Алексеев Сергей	89150954247	У158ОР77	11	12	2016-10-13 05:19:55.423094+00
2398	2016-10-13		Пустовойт В.И.	89652411295	к580ро197	4	53	2016-10-13 07:39:43.182041+00
2399	2016-10-13		Милов Николай	89096468700	М186ТР777	8	12	2016-10-13 08:17:53.112906+00
2404	2016-10-14		Гришин Н.В.	111	А856АВ177	1	126	2016-10-14 06:26:42.904606+00
2406	2016-10-14		Журавлев Андрей Владиславович	9163638151	О904ОЕ777	0	70	2016-10-14 09:15:30.33591+00
2410	2016-10-14		Бобков Дмитрий Сергеевич	89167048378	О725ВР	5	69	2016-10-14 10:42:00.497403+00
2411	2016-10-14		Пешехонцев В А	89269728205	м496ва197	6	68	2016-10-14 13:16:50.547413+00
2413	2016-10-17		милов Николай	89096468700	М186ТР777	8	12	2016-10-17 07:39:30.211302+00
2414	2016-10-17		Пустовойт В.И.	89652411295	к580ро197	4	53	2016-10-17 08:54:46.489185+00
2394	2016-10-13		Черненок С.А.	89773301412	Е961НР177	0	503	2016-10-13 01:08:05.259237+00
2417	2016-10-18		Петров	89168384433		0	553	2016-10-18 07:36:02.030184+00
2420	2016-10-18		Иванов			0	562	2016-10-18 11:46:39.898596+00
2430	2016-10-19		Рубцов В.П.	89209462250	В627НВ33	0	89	2016-10-19 09:01:02.276875+00
2486	2016-10-19		Вертелецкий Олег Анатольевич  		Р746МР67	7	555	2016-10-19 11:41:24.237964+00
2488	2016-10-20		Иванов			7	562	2016-10-20 05:53:15.112135+00
2489	2016-10-20		Иванов			7	562	2016-10-20 05:53:25.382124+00
2492	2016-10-21					0	81	2016-10-21 08:36:45.556675+00
2494	2016-10-21	Савин Трейдинг				5	88	2016-10-21 11:28:45.356811+00
2495	2016-10-21	Савин Трейдинг				5	88	2016-10-21 11:29:07.186898+00
2487	2016-10-20		Киров А.Н	89122867823	Р240КО96	0	503	2016-10-20 02:13:32.52728+00
2400	2016-10-13		Бойко К	89153121446	М685СУ777	5	549	2016-10-13 11:43:21.560235+00
2397	2016-10-13					4	562	2016-10-13 07:04:52.105851+00
2403	2016-10-14					0	57	2016-10-14 06:12:24.849601+00
2405	2016-10-14		Пустовойт В.И.	89652411295	к580ро197	2	53	2016-10-14 07:56:03.679406+00
2408	2016-10-14		Лазарев В.		М436СА/69	0	538	2016-10-14 09:57:15.952322+00
2409	2016-10-14					0	81	2016-10-14 10:34:12.633069+00
2412	2016-10-17		Чиканчи Иван Степанович		а378то197	6	465	2016-10-17 04:10:33.516446+00
2415	2016-10-17		Пустовойт В.И.	89652411295	к580ро197	4	53	2016-10-17 09:15:58.416326+00
2416	2016-10-17		Павлов М.	89264957040	н077ср90	4	53	2016-10-17 13:17:32.302399+00
2419	2016-10-18		Буланов		х951вн50	0	128	2016-10-18 10:48:21.109163+00
2421	2016-10-18					0	59	2016-10-18 12:15:15.937455+00
3378	2016-10-28		Сивцев А		Т081СЕ197	0	549	2016-10-28 12:27:32.986125+00
2427	2016-10-19		Смирнов Юрий 	8904-6168644	В948РУ178	3	64	2016-10-19 08:45:37.21022+00
2418	2016-10-18		Возный С.М.	6	а341оу190	2	126	2016-10-18 08:31:26.686853+00
2490	2016-10-20		Петрушин Ю.А.	89165462407	р475мх97	4	53	2016-10-20 09:36:04.784042+00
2491	2016-10-20		Лосев Олег Николаевич	89684176769	Р281РУ	3	69	2016-10-20 12:22:12.963589+00
2493	2016-10-21					0	68	2016-10-21 10:28:03.800818+00
2499	2016-10-24		Уткин Юрий Александрович		х220оо69	16	14	2016-10-24 07:06:23.744785+00
2500	2016-10-24		Матвеев Юрий Николаевич		К581МН197	6	465	2016-10-24 07:11:05.387404+00
3379	2016-10-28		Чиканчи Иван Степанович		А378ТО199	5	465	2016-10-28 13:10:01.469604+00
2501	2016-10-24		Алексеев Сергей	89150954847	У158ОР77	11	12	2016-10-24 07:44:21.60064+00
2496	2016-10-24		Маслов Ю.А.	89612208500	В684УХ96	0	503	2016-10-24 04:17:23.253143+00
3380	2016-10-31		Старченков Сергей Геннадьевич	89056961066	У334ЕО6	0	555	2016-10-31 07:18:49.099468+00
2503	2016-10-24		Булдаков Вадим Валерьевич 	89190418142	Т032ЕХ67	8	555	2016-10-24 07:47:29.399951+00
3311	2016-10-24		Белов Руслан Вячеславович	89268289555	в593нр750	1	94	2016-10-24 08:27:25.567511+00
3344	2016-10-24					6	64	2016-10-24 09:08:49.898408+00
3346	2016-10-25		СмирновЮрийВалерьевич	89046168644	В948РУ178	1	64	2016-10-25 07:52:35.76967+00
3347	2016-10-25	Товар предоставляется не  на паллетах а в коробах	Сошин Михаил 	+79166809293	Е511РН190	0	67	2016-10-25 08:28:18.961129+00
3348	2016-10-25		Журавлев Андрей Владиславович	9163638151	О904ОЕ777	0	70	2016-10-25 08:28:24.566183+00
3349	2016-10-25		Сошин М.	+79166409293	Е511РН190	0	67	2016-10-25 08:33:17.598152+00
3345	2016-10-25		Лазарев В.			0	538	2016-10-25 05:48:55.489901+00
3350	2016-10-25		С	1	а	2	126	2016-10-25 09:59:16.424112+00
3351	2016-10-25		Буланов		х951вн50	0	128	2016-10-25 10:05:07.403533+00
3352	2016-10-25		Ляхов С.П. 	89090076425	С 049 ВН 	0	496	2016-10-25 10:11:17.646112+00
3353	2016-10-25					0	59	2016-10-25 13:01:36.428842+00
3354	2016-10-25		Юлдошев Жамшид Жураевич	89687556240	м783ка750	1	555	2016-10-25 13:42:53.036794+00
3357	2016-10-26		Москалев К.Е.	89612208500	Р636РР04	0	503	2016-10-26 07:38:59.081359+00
3358	2016-10-26					0	57	2016-10-26 08:09:01.762612+00
3359	2016-10-26					0	68	2016-10-26 10:40:44.459238+00
3360	2016-10-26		Котов Валерий Леонтьевич	89319034424	А475КВ	0	89	2016-10-26 12:31:37.268266+00
3356	2016-10-26		Козырев М.Р.	89612208500	Р963УН77	0	503	2016-10-26 02:40:56.24764+00
3363	2016-10-27		Риндзак Юрий Миронович	89156525892	P 827 УН	1	555	2016-10-27 11:11:59.036326+00
3364	2016-10-27		Солдатов А		С746ОА197	4	549	2016-10-27 11:14:04.047689+00
3368	2016-10-28		Иванов			7	562	2016-10-28 05:15:09.227135+00
3371	2016-10-28	возможна замена машины	Котов Борис Анатольевич	9111112171	В325УК98	1	559	2016-10-28 07:05:42.18975+00
3372	2016-10-28	возможна замена машины	Котов Борис Анатольевич	9111112171	В325УК98	2	559	2016-10-28 07:08:37.792239+00
3365	2016-10-28		Истомин В.С.	89612208500	Р636РР04	0	503	2016-10-28 04:45:08.137335+00
3402	2016-11-02		Львов Сергей Николаевич	89261581792	Е272АО77	0	57	2016-11-02 06:57:35.454634+00
3366	2016-10-28		Кудряшов Н.М.		О708ЕС/69	0	538	2016-10-28 04:59:35.722309+00
3367	2016-10-28		Кудряшов Н.М.		О708ЕС/69	0	538	2016-10-28 04:59:59.129644+00
3373	2016-10-28		Журавлев Андрей Владиславович	9163638151	О904ОЕ777	0	70	2016-10-28 08:08:45.855423+00
3374	2016-10-28					0	81	2016-10-28 11:10:55.227256+00
3376	2016-10-28		Гасанов Рауф Ханбала Оглы	89162457658	Х814МК	4	69	2016-10-28 11:12:22.227251+00
3377	2016-10-28		Бердикулов А.С.	89060650371	о056ав50	6	53	2016-10-28 11:57:51.330582+00
3446	2016-11-03		Иванов			13	562	2016-11-03 06:02:36.358797+00
3430	2016-11-02		ДеловыеЛинии	89046168644	948	1	64	2016-11-02 07:39:22.90724+00
3431	2016-11-02		Алексеев Сергей	89150954247	У158Ор77	11	12	2016-11-02 10:20:20.400202+00
3432	2016-11-02		Курушев Андрей	89163140142	Т281ЕС777	15	12	2016-11-02 10:21:07.74128+00
3433	2016-11-02		Васькин Кирилл	89166104642	Р427ОС777	8	12	2016-11-02 10:22:43.338099+00
3436	2016-11-02		Стаславский Олег	89035182557	О083МЕ50	20	75	2016-11-02 11:16:35.832335+00
3437	2016-11-02		Битейкин Ю.В.	89099140307	е327уо13	2	126	2016-11-02 12:08:15.55979+00
3439	2016-11-02		Махонько М		В109ТА197	1	549	2016-11-02 12:42:10.299065+00
3440	2016-11-02		Алексеев Сергей	89150954247	У158ОР77	11	12	2016-11-02 13:35:24.651507+00
3398	2016-10-31		Беков Ратмир Хасанбиевич	89046168644	Т187ХМ190	2	64	2016-10-31 09:29:52.351569+00
3447	2016-11-03					0	81	2016-11-03 06:48:09.08645+00
3449	2016-11-03					0	68	2016-11-03 06:57:35.408192+00
3452	2016-11-03		Павлов М.	89264957040	н077ср90	5	53	2016-11-03 08:11:30.281207+00
3448	2016-11-03		Гасанов Рауф Ханбала Оглы	89162457658	Х814МК	5	69	2016-11-03 06:48:51.724284+00
3453	2016-11-03		Распопов Владимир Александрович	89035373216	Х304МК190	0	572	2016-11-03 13:08:35.652962+00
\.


--
-- TOC entry 2274 (class 0 OID 21038)
-- Dependencies: 199
-- Data for Name: orders; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY orders (orderid, ordernumber, boxqty, finaldestinationwarehouseid, donutdocperiodid, orderstatus, commentforstatus, invoicenumber, goodscost, orderpalletsqty, orderdate, invoicedate, deliverydate, weight, volume, lastmodified) FROM stdin;
202		0	3	2272	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-20 12:19:58.418607+00
86	1	99	3	970	DELIVERED	Прибытие на территорию в 11:30		735900.00	10	\N	\N	\N	\N	\N	2016-08-30 12:03:26.532978+00
142		0	7	1491	DELIVERED	,mbvkjgfckh		0.00	0	\N	\N	\N	\N	\N	2016-09-13 03:42:13.453697+00
132	№316	0	7	1056	ARRIVED			0.00	0	\N	\N	\N	\N	\N	2016-09-09 13:36:52.185657+00
88	Ек-5153113	0	3	997	DELIVERED	Сб Логистик Москва	ГФ0901-0010	56354.98	2	\N	\N	\N	\N	\N	2016-08-31 13:00:57.021291+00
125	Ек-5153856	96	3	1050	DELIVERED	СПБ	ГФ0908-0006	28993.04	1	\N	\N	\N	\N	\N	2016-09-07 13:23:05.703458+00
11	3	0	3	203	ARRIVED			0.00	14	\N	\N	\N	\N	\N	2016-07-14 12:17:49.806692+00
12	4	0	7	203	ARRIVED	Паллета повреждена		0.00	1	\N	\N	\N	\N	\N	2016-07-14 12:17:49.806692+00
22	ЕК-5148007	0	7	555	CREATED		6545,6548,6544,6546,6547,6550,6554,7526,7487	0.00	15	\N	\N	\N	\N	\N	2016-07-20 09:21:09.953448+00
23	ЕК-5148009	0	3	556	CREATED			0.00	8	\N	\N	\N	\N	\N	2016-07-21 09:17:38.212968+00
25	ЕК-5148795	0	3	560	CREATED		6794,6779,6781,6783,6778,6791,6792,6797,6802,7563,6784,7574	0.00	12	\N	\N	\N	\N	\N	2016-07-27 09:00:53.971644+00
29		99	3	561	CREATED		897	1053048.04	9	\N	\N	\N	\N	\N	2016-07-28 12:56:50.126638+00
27		1	7	561	CREATED		899	6563.53	0	\N	\N	\N	\N	\N	2016-07-28 09:28:03.152018+00
28		99	7	561	CREATED		898	829723.18	8	\N	\N	\N	\N	\N	2016-07-28 09:28:03.152018+00
26		51	4	561	CREATED		895	344338.55	3	\N	\N	\N	\N	\N	2016-07-28 09:28:03.152018+00
30	1	64	3	562	CREATED			408439.00	4	\N	\N	\N	\N	\N	2016-07-28 13:25:28.361821+00
33	Ек-5149704	0	3	801	CREATED		7005	0.00	15	\N	\N	\N	\N	\N	2016-08-03 08:01:57.413198+00
35	ЕК-5149723	0	7	803	CREATED		7017	0.00	10	\N	\N	\N	\N	\N	2016-08-03 13:13:51.107528+00
38	1	99	3	806	CREATED		17847, 17844, 17846	492094.00	4	\N	\N	\N	\N	\N	2016-08-04 07:21:25.372286+00
42		0	3	811	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-08-05 11:00:10.573483+00
44	ЕК-5150184	5	3	813	CREATED		11255	35845.48	0	\N	\N	\N	\N	\N	2016-08-05 12:44:26.657195+00
46	ЕК-5150182	17	7	813	CREATED		11257	162350.16	0	\N	\N	\N	\N	\N	2016-08-05 13:00:41.9902+00
47	ЕК-5150185	3	4	813	CREATED		11254	8288.56	0	\N	\N	\N	\N	\N	2016-08-05 13:00:41.9902+00
48	СПБ-013618	7	5	813	CREATED		11262	46512.04	0	\N	\N	\N	\N	\N	2016-08-05 13:00:41.9902+00
49	СПб-013619	22	6	813	CREATED		11269	192979.24	0	\N	\N	\N	\N	\N	2016-08-05 14:00:31.501968+00
51	Ек-5150052	0	3	816	CREATED		7127	0.00	15	\N	\N	\N	\N	\N	2016-08-08 07:36:27.230911+00
52	Ек-5150081	0	3	817	CREATED		7145	0.00	12	\N	\N	\N	\N	\N	2016-08-08 07:37:49.552047+00
54	Ек-5150515	0	7	832	CREATED		7293	0.00	15	\N	\N	\N	\N	\N	2016-08-10 09:56:27.377546+00
55	Ек-5150532	0	7	833	CREATED			0.00	11	\N	\N	\N	\N	\N	2016-08-10 09:59:24.142836+00
56	5150742	0	3	834	CREATED	Альфа Нева СПБ	ГФ0811-0007	39178.48	1	\N	\N	\N	\N	\N	2016-08-10 13:35:32.908166+00
57	5150741	0	3	834	CREATED	Смирнов бэттериз Екатеринбург	ГФ0811-0006	143109.99	6	\N	\N	\N	\N	\N	2016-08-10 13:35:32.908166+00
58	5150736	0	3	834	CREATED	СБ Логистик Ульяновск	ГФ0811-0005	14543.92	1	\N	\N	\N	\N	\N	2016-08-10 13:35:32.908166+00
59	5150747	0	3	834	CREATED	СБ Логистик Москва	ГФ0811-0008	126491.68	4	\N	\N	\N	\N	\N	2016-08-10 13:35:32.908166+00
66	ЕК-5150856	0	3	837	CREATED		7407	0.00	15	\N	\N	\N	\N	\N	2016-08-12 08:50:32.806255+00
68		0	3	839	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-08-12 09:35:29.939619+00
69	5150649,5150655	0	3	840	CREATED			0.00	5	\N	\N	\N	\N	\N	2016-08-12 11:13:36.140292+00
71		0	3	850	CREATED		5151263	0.00	3	\N	\N	\N	\N	\N	2016-08-16 07:13:18.572541+00
70		0	3	850	CREATED		5151287	0.00	2	\N	\N	\N	\N	\N	2016-08-16 07:09:13.516311+00
72	Ек-515338	0	7	851	CREATED		7588	0.00	15	\N	\N	\N	\N	\N	2016-08-17 12:36:58.867057+00
74	1	99	3	854	CREATED		19196,19198	214611.00	4	\N	\N	\N	\N	\N	2016-08-22 07:11:32.38165+00
75		0	7	856	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-08-24 12:33:50.854364+00
77		0	7	858	CREATED			0.00	11	\N	\N	\N	\N	\N	2016-08-24 13:21:28.631958+00
78	Ек-5152095	0	7	859	CREATED		7860	0.00	15	\N	\N	\N	\N	\N	2016-08-24 13:24:18.793444+00
80	5152503	0	3	861	CREATED		ГФ0826-0021	0.00	1	\N	\N	\N	\N	\N	2016-08-25 12:25:03.373442+00
79	5152504	0	3	861	CREATED		ГФ0826-0020	0.00	2	\N	\N	\N	\N	\N	2016-08-25 12:25:03.373442+00
81		0	3	864	CREATED			0.00	11	\N	\N	\N	\N	\N	2016-08-29 08:14:11.169352+00
82		0	3	865	CREATED			0.00	15	\N	\N	\N	\N	\N	2016-08-29 08:15:19.431734+00
96	93	0	3	1023	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-01 11:43:13.959203+00
90		0	7	1003	DELIVERED			0.00	9	\N	\N	\N	\N	\N	2016-09-01 08:08:33.794959+00
91		0	7	1004	DELIVERED			0.00	8	\N	\N	\N	\N	\N	2016-09-01 08:09:15.658327+00
127	Ек-5153865	0	3	1050	DELIVERED	Москва	ГФ0908-0007	83863.43	2	\N	\N	\N	\N	\N	2016-09-07 13:23:05.703458+00
89	Ек-5153116	0	3	997	DELIVERED	Екатеринбург	ГФ0901-0011	94839.52	4	\N	\N	\N	\N	\N	2016-08-31 13:00:57.021291+00
104	№94	0	3	1028	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-02 08:48:12.452724+00
107		0	3	1031	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-02 12:01:22.456837+00
108		0	3	1031	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-02 12:03:01.948488+00
126	Ек-5153829	94	3	1050	DELIVERED	Ульяновск	ГФ0908-0009	36796.83	2	\N	\N	\N	\N	\N	2016-09-07 13:23:05.703458+00
87		0	7	994	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-08-31 11:07:35.162425+00
109		64	3	1032	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-09-02 12:47:22.121614+00
114	014433	20	6	1036	DELIVERED		1118	136188.00	1	\N	\N	\N	\N	\N	2016-09-05 06:41:56.9658+00
115	014434	7	5	1036	DELIVERED		1119	51204.00	1	\N	\N	\N	\N	\N	2016-09-05 06:41:56.9658+00
123		0	7	1049	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-07 10:06:37.335201+00
117		0	3	1042	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-09-06 08:56:50.067962+00
124	Ек-5153833	0	3	1050	DELIVERED	Екатеринбург	ГФ0908-0008	57392.20	2	\N	\N	\N	\N	\N	2016-09-07 13:23:05.703458+00
131	№97	0	4	1055	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-09 13:32:43.752015+00
141	98	0	4	1487	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-12 13:35:36.379426+00
128		49	3	1052	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-09-09 12:24:07.571651+00
136	СПб-014619	55	6	1053	DELIVERED		1155	375768.00	2	\N	\N	\N	\N	\N	2016-09-12 06:20:06.278465+00
137	 Ек-5154260	21	4	1053	DELIVERED		1150	164544.00	1	\N	\N	\N	\N	\N	2016-09-12 06:20:06.278465+00
134	Ек-5154271	72	3	1053	DELIVERED		1151	545256.00	2	\N	\N	\N	\N	\N	2016-09-12 06:20:06.278465+00
135	 Ек-5154265	29	7	1053	DELIVERED		1149	227688.00	1	\N	\N	\N	\N	\N	2016-09-12 06:20:06.278465+00
138	СПб-014622	6	6	1053	DELIVERED		1156	28656.00	0	\N	\N	\N	\N	\N	2016-09-12 06:20:06.278465+00
143		0	3	1495	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-13 11:09:53.937759+00
161	Ек-5154152	42	3	1496	DELIVERED		2031	56826.00	1	\N	\N	\N	\N	\N	2016-09-14 08:40:20.809365+00
162	Ек-5154102	15	3	1496	DELIVERED		2029	20295.00	1	\N	\N	\N	\N	\N	2016-09-14 08:40:20.809365+00
167	1	99	3	2247	DELIVERED		21393	480644.00	4	\N	\N	\N	\N	\N	2016-09-15 07:17:48.828681+00
149		0	3	1503	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-09-13 13:04:40.986585+00
150		0	3	1504	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-09-13 13:05:19.719043+00
170	99	0	4	2250	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-15 11:52:33.748409+00
156	Ек-5154428	0	8	1507	DELIVERED		36154	6482.00	1	\N	\N	\N	\N	\N	2016-09-14 08:08:46.208197+00
152	Ек-5154665	26	4	1505	DELIVERED		ГФ0914-0014	11804.74	1	\N	\N	\N	\N	\N	2016-09-13 14:04:10.839568+00
153	Ек-5154679	99	3	1505	DELIVERED		ГФ0914-0015	52889.24	2	\N	\N	\N	\N	\N	2016-09-13 14:04:10.839568+00
151	Ек-5154676	52	6	1505	DELIVERED		ГФ0914-0016	11391.52	1	\N	\N	\N	\N	\N	2016-09-13 14:04:10.839568+00
154	Ек-5154670	91	7	1505	DELIVERED		ГФ0914-0013	36102.09	2	\N	\N	\N	\N	\N	2016-09-13 14:04:10.839568+00
164	Ек-5154319	10	7	1090	DELIVERED		4117	45917.76	1	\N	\N	\N	\N	\N	2016-09-14 09:32:45.629482+00
163	Ек-5154326	9	3	1090	DELIVERED		4115	41867.22	1	\N	\N	\N	\N	\N	2016-09-14 09:32:45.629482+00
166		0	3	2246	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-09-15 05:29:31.958445+00
169	2	99	3	2249	DELIVERED		21392	326456.00	4	\N	\N	\N	\N	\N	2016-09-15 07:21:03.116368+00
148		0	3	1502	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-09-13 12:58:37.440527+00
160	Ек-5154397	0	3	1507	DELIVERED		36140,36141,36142	187443.00	1	\N	\N	\N	\N	\N	2016-09-14 08:08:46.208197+00
155	Ек-5154410	0	3	1507	DELIVERED		36149,36150	27398.00	1	\N	\N	\N	\N	\N	2016-09-14 08:08:46.208197+00
159	Ек-5154387	0	7	1507	DELIVERED		36143,36144,36145	423838.00	1	\N	\N	\N	\N	\N	2016-09-14 08:08:46.208197+00
158	Ек-5154419	0	4	1507	DELIVERED		36152,36153	24235.00	1	\N	\N	\N	\N	\N	2016-09-14 08:08:46.208197+00
157	Ек-5154408	0	7	1507	DELIVERED		36147,36148	46111.00	1	\N	\N	\N	\N	\N	2016-09-14 08:08:46.208197+00
206	Ек-5155493	35	7	2271	DELIVERED		1212	258084.00	1	\N	\N	\N	\N	\N	2016-09-21 05:34:08.410384+00
207	Ек-5155496	54	3	2271	DELIVERED		1211	396468.00	2	\N	\N	\N	\N	\N	2016-09-21 05:34:08.410384+00
175		0	3	2254	DELIVERED			0.00	8	\N	\N	\N	\N	\N	2016-09-15 12:53:32.525217+00
208	Ек-5155494	19	4	2271	DELIVERED		1210	137232.00	1	\N	\N	\N	\N	\N	2016-09-21 05:34:08.410384+00
205	Ек-5155571	99	7	2278	DELIVERED		ГФ0920-0011	61413.74	2	\N	\N	\N	\N	\N	2016-09-20 14:38:05.711045+00
204	Ек-5155609	99	3	2278	DELIVERED		ГФ0920-0018	121537.49	5	\N	\N	\N	\N	\N	2016-09-20 14:38:05.711045+00
210	1	43	3	2283	DELIVERED		21966,21967,21968	246120.00	3	\N	\N	\N	\N	\N	2016-09-21 09:35:10.365741+00
211		0	3	2285	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-21 11:28:28.845961+00
214	103	0	4	2287	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-22 08:26:36.269994+00
215	104	0	4	2288	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-22 08:28:10.986977+00
213		0	3	2286	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-09-22 06:27:33.932928+00
212		0	3	2286	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-09-22 06:27:33.932928+00
268	1	36	3	2327	CREATED		22730,22731,22732	238711.00	4	\N	\N	\N	\N	\N	2016-09-28 09:07:18.79933+00
269	1	36	3	2328	DELIVERED		22730,22731,22732	238711.00	4	\N	\N	\N	\N	\N	2016-09-28 09:08:57.921239+00
286		0	3	2336	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-29 07:55:16.475475+00
275	Ек-5156609	99	3	2330	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-09-28 11:17:47.80888+00
276	 Ек-5156557	99	6	2330	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-09-28 11:17:47.80888+00
278	Ек-5156566	99	4	2330	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-09-28 11:17:47.80888+00
274	Ек-5156554 	99	5	2330	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-09-28 11:17:47.80888+00
277	Ек-5156602	99	7	2330	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-09-28 11:17:47.80888+00
281		6	3	2331	DELIVERED		871	0.00	1	\N	\N	\N	\N	\N	2016-09-28 12:29:47.480599+00
282		5	6	2331	DELIVERED		869	0.00	1	\N	\N	\N	\N	\N	2016-09-28 12:29:47.480599+00
279		6	5	2331	DELIVERED		870	0.00	1	\N	\N	\N	\N	\N	2016-09-28 12:29:47.480599+00
280		10	6	2331	DELIVERED		868	0.00	1	\N	\N	\N	\N	\N	2016-09-28 12:29:47.480599+00
270	Ек-5156213	22	3	2329	DELIVERED		1124	25573.68	1	\N	\N	\N	\N	\N	2016-09-28 09:13:11.409079+00
271	 Ек-5156332	29	4	2329	DELIVERED		1127	112274.88	1	\N	\N	\N	\N	\N	2016-09-28 09:13:11.409079+00
273	Ек-5156232	18	7	2329	DELIVERED		1126	17645.25	1	\N	\N	\N	\N	\N	2016-09-28 09:13:11.409079+00
272	Ек-5156220	2	4	2329	DELIVERED		1125	16931.31	1	\N	\N	\N	\N	\N	2016-09-28 09:13:11.409079+00
295	Ек-5156855	56	3	2343	ERROR	46 м. Ульяновск 	2213	58786.68	2	\N	\N	\N	\N	\N	2016-09-29 12:06:34.611691+00
293	Ек-5156857	21	3	2343	ERROR	Альфа 	2212	21986.64	1	\N	\N	\N	\N	\N	2016-09-29 12:06:34.611691+00
294	Ек-5156859	94	3	2343	DELIVERED		2214	116842.08	2	\N	\N	\N	\N	\N	2016-09-29 12:06:34.611691+00
284		0	3	2334	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-09-29 05:18:40.935085+00
296	1	50	3	2344	DELIVERED		23027	317364.00	3	\N	\N	\N	\N	\N	2016-09-30 07:41:13.367519+00
302		0	3	2349	DELIVERED			0.00	12	\N	\N	\N	\N	\N	2016-09-30 10:26:07.977302+00
310	СПб-015191	16	5	2345	DELIVERED		1310	110244.00	1	\N	\N	\N	\N	\N	2016-10-03 06:26:39.367385+00
309	СПб-015194 	69	6	2345	DELIVERED		1311	480958.00	2	\N	\N	\N	\N	\N	2016-10-03 06:26:39.367385+00
308		39	3	2352	DELIVERED			0.00	5	\N	\N	\N	\N	\N	2016-10-03 04:43:45.956831+00
301		0	4	2348	DELIVERED			0.00	15	\N	\N	\N	\N	\N	2016-09-30 10:23:27.405118+00
329		0	3	2360	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-04 10:54:04.886233+00
283	Ек-5156479	10	3	2333	DELIVERED		816	136439.00	0	\N	\N	\N	\N	\N	2016-09-29 04:53:04.170134+00
336	Ек-5157500	31	4	2362	DELIVERED		1327	220188.00	1	\N	\N	\N	\N	\N	2016-10-05 05:30:56.045944+00
337	Ек-5157503	35	7	2362	DELIVERED		1331	236652.00	1	\N	\N	\N	\N	\N	2016-10-05 05:30:56.045944+00
338	Ек-5157506	11	3	2362	DELIVERED		1326	836418.00	4	\N	\N	\N	\N	\N	2016-10-05 05:30:56.045944+00
351		17	6	2374	DELIVERED		909	0.00	1	\N	\N	\N	\N	\N	2016-10-06 14:59:52.002893+00
352		5	6	2374	DELIVERED		911	0.00	1	\N	\N	\N	\N	\N	2016-10-06 14:59:52.002893+00
354	1	59	3	2376	DELIVERED		23725	290525.00	4	\N	\N	\N	\N	\N	2016-10-07 07:23:14.031161+00
335		0	3	2366	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-05 05:10:32.406182+00
342	ЕК-5157471	8	3	2357	DELIVERED		11049	74543.00	1	\N	\N	\N	\N	\N	2016-10-05 13:59:43.174061+00
341	Ек-5157474	10	3	2357	DELIVERED		11048	73976.00	1	\N	\N	\N	\N	\N	2016-10-05 13:59:43.174061+00
350		64	3	2373	DELIVERED			0.00	4	\N	\N	\N	\N	\N	2016-10-06 12:52:00.739289+00
325	1	0	3	2358	DELIVERED		МCУТ021002	404964.00	3	\N	\N	\N	\N	\N	2016-10-04 08:05:43.824007+00
326	3	0	7	2358	DELIVERED			312339.00	2	\N	\N	\N	\N	\N	2016-10-04 08:05:43.824007+00
324	2	0	4	2358	DELIVERED		МCУТ021001	34002.00	1	\N	\N	\N	\N	\N	2016-10-04 08:05:43.824007+00
346	4	0	10	2358	DELIVERED		МCУТ021008	4214.00	1	\N	\N	\N	\N	\N	2016-10-06 10:04:04.010101+00
347	5	0	6	2358	DELIVERED		МCУТ021012	71340.00	1	\N	\N	\N	\N	\N	2016-10-06 10:04:04.010101+00
353		0	7	2375	DELIVERED			0.00	15	\N	\N	\N	\N	\N	2016-10-07 05:44:50.239552+00
358		0	4	2380	CREATED			0.00	8	\N	\N	\N	\N	\N	2016-10-10 08:34:57.87472+00
356	СПб-015341	1	10	2377	DELIVERED		1362	4856.00	0	\N	\N	\N	\N	\N	2016-10-10 05:58:29.226278+00
355	СПб-015340 	37	10	2377	DELIVERED		1361	264972.00	1	\N	\N	\N	\N	\N	2016-10-10 05:58:29.226278+00
357	1	65	3	2379	DELIVERED		23848,23849	287148.00	4	\N	\N	\N	\N	\N	2016-10-10 08:12:12.54367+00
359		0	4	2381	DELIVERED			0.00	15	\N	\N	\N	\N	\N	2016-10-10 08:35:51.528603+00
363	Ек-5158389	2	4	2386	CREATED		969	13000.00	0	\N	\N	\N	\N	\N	2016-10-12 03:38:13.572167+00
364	Ек-5158389	7	4	2386	CREATED		1010	39906.00	0	\N	\N	\N	\N	\N	2016-10-12 03:38:13.572167+00
365	СПб-015394	15	5	2387	CREATED		1004	85871.00	0	\N	\N	\N	\N	\N	2016-10-12 03:44:49.487765+00
343	Ек-5156705	1	7	2369	CREATED		954	19596.00	0	\N	\N	\N	\N	\N	2016-10-06 05:10:01.929415+00
375	Ек-5158674	23	7	2396	CREATED		1037	178356.00	0	\N	\N	\N	\N	\N	2016-10-13 06:48:10.67703+00
376		0	3	2397	DELIVERED			0.00	1	\N	\N	\N	\N	\N	2016-10-13 07:04:52.105851+00
171	100	0	4	2251	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-15 11:54:07.970056+00
186	ЕК-5154778	11	3	2256	DELIVERED		10096	93050.00	1	\N	\N	\N	\N	\N	2016-09-16 08:22:52.196925+00
179	Ек-5154775	20	3	2256	DELIVERED		10097	143484.90	1	\N	\N	\N	\N	\N	2016-09-15 14:06:58.502003+00
183	Ек-5154826	94	3	2259	DELIVERED		2080	102357.28	2	\N	\N	\N	\N	\N	2016-09-16 07:26:32.414548+00
182	Ек-5154824	33	3	2259	DELIVERED	Заказ на Альфу 	2079	35645.28	1	\N	\N	\N	\N	\N	2016-09-16 07:26:32.414548+00
222	1	99	3	2306	DELIVERED		22364,22362,22341	741389.00	4	\N	\N	\N	\N	\N	2016-09-23 12:28:12.514192+00
224	2	99	3	2308	DELIVERED		\t22364,22362,22341	741389.00	4	\N	\N	\N	\N	\N	2016-09-23 13:40:54.867869+00
187		48	3	2262	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-09-16 12:35:51.57417+00
199	Ек-5155149	4	4	2261	DELIVERED		4175	16103.64	0	\N	\N	\N	\N	\N	2016-09-19 07:06:55.152742+00
197	Ек-5155153	4	7	2261	DELIVERED		4173	18397.32	0	\N	\N	\N	\N	\N	2016-09-19 07:06:55.152742+00
198	Ек-5155145	6	3	2261	DELIVERED		4174	24428.34	0	\N	\N	\N	\N	\N	2016-09-19 07:06:55.152742+00
185	Ек-5155009	0	7	2260	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-09-16 08:14:55.877575+00
184	Ек-5154960	0	3	2260	DELIVERED			0.00	1	\N	\N	\N	\N	\N	2016-09-16 08:14:55.877575+00
192	 СПб-014851	69	6	2263	DELIVERED		1193	443892.00	2	\N	\N	\N	\N	\N	2016-09-19 06:46:01.931944+00
188	СПб-014848	9	5	2263	DELIVERED		1191	64380.00	1	\N	\N	\N	\N	\N	2016-09-16 13:03:20.166236+00
193	СПб-014852	3	6	2263	DELIVERED		1192	11496.00	0	\N	\N	\N	\N	\N	2016-09-19 06:46:01.931944+00
230	СПб-014990 	80	6	2305	DELIVERED		1263	621480.00	3	\N	\N	\N	\N	\N	2016-09-26 06:29:38.085527+00
237		0	3	2284	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-09-26 07:13:44.951603+00
231		0	6	2312	DELIVERED			0.00	8	\N	\N	\N	\N	\N	2016-09-26 06:55:14.473361+00
232		0	4	2313	DELIVERED			0.00	15	\N	\N	\N	\N	\N	2016-09-26 06:56:17.037864+00
233		0	3	2314	DELIVERED			0.00	12	\N	\N	\N	\N	\N	2016-09-26 06:57:08.603208+00
234	Ек-5155786	62	3	2315	DELIVERED		2134	64759.12	2	\N	\N	\N	\N	\N	2016-09-26 07:03:15.729337+00
235	Ек-5155787	42	3	2315	DELIVERED		2132	42438.96	1	\N	\N	\N	\N	\N	2016-09-26 07:03:15.729337+00
236	Ек-5155793	87	3	2315	DELIVERED		2133	118273.64	2	\N	\N	\N	\N	\N	2016-09-26 07:03:15.729337+00
227		99	3	2311	DELIVERED			0.00	8	\N	\N	\N	\N	\N	2016-09-26 04:29:01.193509+00
221		0	3	2304	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-09-23 10:04:23.505423+00
248		0	3	2320	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-09-27 12:38:14.327517+00
243	Ек- 5156268	0	3	2317	DELIVERED			0.00	2	\N	\N	\N	\N	\N	2016-09-26 11:25:35.681223+00
238	Ек-5156259	0	3	2317	DELIVERED			0.00	6	\N	\N	\N	\N	\N	2016-09-26 11:14:55.158876+00
241	Ек-5156264	0	3	2317	DELIVERED			0.00	2	\N	\N	\N	\N	\N	2016-09-26 11:25:35.681223+00
239	Ек-5156274	0	3	2317	DELIVERED			0.00	2	\N	\N	\N	\N	\N	2016-09-26 11:25:35.681223+00
240	Ек-5156262	0	3	2317	DELIVERED			0.00	4	\N	\N	\N	\N	\N	2016-09-26 11:25:35.681223+00
242	Ек-5156276	0	3	2317	DELIVERED			0.00	2	\N	\N	\N	\N	\N	2016-09-26 11:25:35.681223+00
244		27	3	2318	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-09-26 12:18:14.027133+00
267	ЕК-5156443	0	3	2319	DELIVERED		10686	64933.00	1	\N	\N	\N	\N	\N	2016-09-28 08:32:33.271092+00
266	ЕК-5156444	0	3	2319	DELIVERED		10687	30769.00	1	\N	\N	\N	\N	\N	2016-09-28 08:32:33.271092+00
327		0	3	2359	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-04 08:37:37.891055+00
328		0	3	2359	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-04 08:37:37.891055+00
367		0	3	2389	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-12 11:11:18.016314+00
320	Ек-5157072	10	3	2353	DELIVERED	Ульяновск 	2689	60607.70	1	\N	\N	\N	\N	\N	2016-10-03 07:29:40.937474+00
317	Ек-5157074	1	3	2353	DELIVERED	Центральный склад СПб	2688	3993.95	1	\N	\N	\N	\N	\N	2016-10-03 07:29:40.937474+00
319	Ек-5157049	17	3	2353	DELIVERED	Москва	2687	91123.50	1	\N	\N	\N	\N	\N	2016-10-03 07:29:40.937474+00
318	Ек-5157073	4	3	2353	DELIVERED	Всеволожск 	2686	26786.20	1	\N	\N	\N	\N	\N	2016-10-03 07:29:40.937474+00
333		7	4	2364	DELIVERED		4528	29075.52	0	\N	\N	\N	\N	\N	2016-10-05 04:50:54.248083+00
331		19	7	2364	DELIVERED		4526	83323.32	1	\N	\N	\N	\N	\N	2016-10-05 04:50:54.248083+00
332		27	3	2364	DELIVERED		4527	122433.66	1	\N	\N	\N	\N	\N	2016-10-05 04:50:54.248083+00
340	1	64	3	2368	DELIVERED		23437,23438	312342.00	4	\N	\N	\N	\N	\N	2016-10-05 07:47:29.561929+00
321		1	3	2355	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-03 09:02:49.042649+00
330		4	7	2355	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-04 12:05:14.614108+00
345		0	6	2370	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-06 09:26:14.501754+00
344	Ек-5157897	13	7	2369	CREATED		953	91673.00	0	\N	\N	\N	\N	\N	2016-10-06 05:49:02.256675+00
339	Ек-5157535	7	3	2367	DELIVERED		935	80881.00	0	\N	\N	\N	\N	\N	2016-10-05 06:16:02.18637+00
379	1	70	3	2398	DELIVERED		24248,24249,24250	489393.00	4	\N	\N	\N	\N	\N	2016-10-13 07:39:43.182041+00
374		0	7	2395	DELIVERED			0.00	11	\N	\N	\N	\N	\N	2016-10-13 05:19:55.423094+00
370		0	7	2392	DELIVERED			0.00	15	\N	\N	\N	\N	\N	2016-10-12 13:21:36.296107+00
380		0	7	2399	DELIVERED			0.00	8	\N	\N	\N	\N	\N	2016-10-13 08:17:53.112906+00
368		0	3	2390	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-12 12:20:43.722847+00
366		0	3	2388	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-12 09:07:25.924292+00
382		12	6	2400	DELIVERED		939	0.00	1	\N	\N	\N	\N	\N	2016-10-13 11:43:21.560235+00
402	Ек-5158568	0	5	2403	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-14 06:12:24.849601+00
406		0	3	2404	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-14 06:26:42.904606+00
405	ЕК-5158525	0	3	2404	DELIVERED		11445	70541.00	1	\N	\N	\N	\N	\N	2016-10-14 06:26:42.904606+00
404	ЕК-5158526	0	3	2404	DELIVERED		11444	27697.00	1	\N	\N	\N	\N	\N	2016-10-14 06:26:42.904606+00
407	1	31	3	2405	DELIVERED		24408	126807.00	2	\N	\N	\N	\N	\N	2016-10-14 07:56:03.679406+00
377		0	4	2397	DELIVERED			0.00	1	\N	\N	\N	\N	\N	2016-10-13 07:04:52.105851+00
385		0	10	2397	DELIVERED			0.00	1	\N	\N	\N	\N	\N	2016-10-13 14:21:11.472397+00
378		0	7	2397	DELIVERED			0.00	1	\N	\N	\N	\N	\N	2016-10-13 07:04:52.105851+00
384		48	5	2400	DELIVERED		937	0.00	1	\N	\N	\N	\N	\N	2016-10-13 11:43:21.560235+00
383		57	6	2400	DELIVERED		938	0.00	2	\N	\N	\N	\N	\N	2016-10-13 11:43:21.560235+00
381		30	5	2400	DELIVERED		936	0.00	1	\N	\N	\N	\N	\N	2016-10-13 11:43:21.560235+00
409		0	4	2406	DELIVERED		4731	23406.96	0	\N	\N	\N	\N	\N	2016-10-14 09:15:30.33591+00
410		0	3	2406	DELIVERED		4730	67878.12	0	\N	\N	\N	\N	\N	2016-10-14 09:15:30.33591+00
408		0	7	2406	DELIVERED		4729	51712.34	0	\N	\N	\N	\N	\N	2016-10-14 09:15:30.33591+00
419	СПб-015567	18	10	2411	DELIVERED		1415	111000.00	1	\N	\N	\N	\N	\N	2016-10-17 07:09:12.048324+00
417	СПб-015564	25	5	2411	DELIVERED		1416	207216.00	1	\N	\N	\N	\N	\N	2016-10-17 07:09:12.048324+00
412		0	3	2408	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-14 09:57:15.952322+00
373	Ек-5158386	5	3	2394	DELIVERED		968	32500.00	0	\N	\N	\N	\N	\N	2016-10-13 01:08:05.259237+00
372	Ек-5158386	28	3	2394	DELIVERED		1012	177972.00	0	\N	\N	\N	\N	\N	2016-10-13 01:08:05.259237+00
399	Ек-5158547	0	7	2403	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-14 06:12:24.849601+00
400	 Ек-5158562 	0	10	2403	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-14 06:12:24.849601+00
401	Ек-5158522 	0	4	2403	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-14 06:12:24.849601+00
403	Ек-5158578	0	3	2403	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-14 06:12:24.849601+00
413	Ек-5158814	0	3	2410	DELIVERED	количество коробок 129	2343	149318.52	3	\N	\N	\N	\N	\N	2016-10-14 10:42:00.497403+00
414	Ек-5158798	67	3	2410	DELIVERED		2342	69609.76	2	\N	\N	\N	\N	\N	2016-10-14 10:42:00.497403+00
416	 Ек-5159038	32	4	2411	DELIVERED		1413	234936.00	1	\N	\N	\N	\N	\N	2016-10-17 07:09:12.048324+00
418	Ек-5159061	34	3	2411	DELIVERED		1414	340695.00	2	\N	\N	\N	\N	\N	2016-10-17 07:09:12.048324+00
415		74	3	2412	DELIVERED			0.00	6	\N	\N	\N	\N	\N	2016-10-17 04:10:33.516446+00
424	1	95	3	2415	DELIVERED		39915	534690.00	4	\N	\N	\N	\N	\N	2016-10-17 09:15:58.416326+00
425	1	66	3	2416	DELIVERED		24554,24556	328788.00	4	\N	\N	\N	\N	\N	2016-10-17 13:17:32.302399+00
429		0	3	2419	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-18 10:48:21.109163+00
496		0	4	2501	DELIVERED			0.00	11	\N	\N	\N	\N	\N	2016-10-24 07:44:21.60064+00
506		0	3	3346	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-25 07:52:35.76967+00
513		0	3	3352	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-25 10:11:17.646112+00
518	Ек-5170996	0	3	3357	DELIVERED		1216	19291.00	0	\N	\N	\N	\N	\N	2016-10-26 07:38:59.081359+00
520	Ек-5170994	0	3	3357	DELIVERED		1218	11760.00	0	\N	\N	\N	\N	\N	2016-10-26 07:38:59.081359+00
565	 Ек-5171986 	0	3	3402	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-11-02 06:57:35.454634+00
508		0	3	3348	DELIVERED		4915 от 25.10.2016	44360.73	0	\N	\N	\N	\N	\N	2016-10-25 08:28:24.566183+00
509		4	7	3348	DELIVERED		4916 от 25.10.2016	23066.16	0	\N	\N	\N	\N	\N	2016-10-25 08:28:24.566183+00
507		0	3	3347	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-25 08:28:18.961129+00
510		0	3	3349	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-25 08:33:17.598152+00
511		0	3	3350	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-25 09:59:16.424112+00
512		0	3	3350	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-25 09:59:16.424112+00
532		14	10	3364	DELIVERED		991	0.00	1	\N	\N	\N	\N	\N	2016-10-27 11:14:04.047689+00
529		54	5	3364	DELIVERED		993	0.00	1	\N	\N	\N	\N	\N	2016-10-27 11:14:04.047689+00
530		18	5	3364	DELIVERED		994	0.00	1	\N	\N	\N	\N	\N	2016-10-27 11:14:04.047689+00
531		45	10	3364	DELIVERED		992	0.00	1	\N	\N	\N	\N	\N	2016-10-27 11:14:04.047689+00
541		0	4	3368	DELIVERED			0.00	1	\N	\N	\N	\N	\N	2016-10-28 05:15:09.227135+00
539		0	3	3368	DELIVERED			0.00	3	\N	\N	\N	\N	\N	2016-10-28 05:15:09.227135+00
540		0	7	3368	DELIVERED			0.00	3	\N	\N	\N	\N	\N	2016-10-28 05:15:09.227135+00
546		6	4	3373	DELIVERED			26178.36	0	\N	\N	\N	\N	\N	2016-10-28 08:08:45.855423+00
547		8	7	3373	DELIVERED			33312.36	0	\N	\N	\N	\N	\N	2016-10-28 08:08:45.855423+00
548		12	3	3373	DELIVERED			50600.10	0	\N	\N	\N	\N	\N	2016-10-28 08:08:45.855423+00
594		0	7	3430	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-11-02 07:39:22.90724+00
597		0	7	3430	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-11-02 07:41:06.30028+00
599		0	7	3430	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-11-02 07:41:48.669847+00
500	 Ек-5170833	11	7	2496	DELIVERED		1188	148542.00	0	\N	\N	\N	\N	\N	2016-10-24 08:39:17.489469+00
600		0	7	3431	DELIVERED			0.00	11	\N	\N	\N	\N	\N	2016-11-02 10:20:20.400202+00
519	Ек-5170991	0	3	3357	DELIVERED		1217	211328.00	0	\N	\N	\N	\N	\N	2016-10-26 07:38:59.081359+00
563	Ек-5171974	0	7	3402	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-11-02 06:57:35.454634+00
566	 Ек-5171991 	0	10	3402	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-11-02 06:57:35.454634+00
564	 Ек-5171940	0	4	3402	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-11-02 06:57:35.454634+00
553	СПб-015875	11	3	3376	DELIVERED		2462	14789.88	1	\N	\N	\N	\N	\N	2016-10-28 11:16:11.876234+00
554	Ек-5171361	42	3	3376	DELIVERED		2463	39432.96	1	\N	\N	\N	\N	\N	2016-10-28 11:16:11.876234+00
552	Ек-5171366	0	3	3376	DELIVERED	количество коробок 135 шт	2464	151203.24	2	\N	\N	\N	\N	\N	2016-10-28 11:16:11.876234+00
556		60	5	3378	DELIVERED		999	0.00	2	\N	\N	\N	\N	\N	2016-10-28 12:27:32.986125+00
611		0	3	3398	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-11-02 14:24:35.30343+00
559		5	3	3398	DELIVERED			0.00	2	\N	\N	\N	\N	\N	2016-10-31 09:29:52.351569+00
609		0	7	3440	DELIVERED			0.00	11	\N	\N	\N	\N	\N	2016-11-02 13:35:24.651507+00
624	Ек-5172451	50	3	3448	CREATED		2506	47896.56	2	\N	\N	\N	\N	\N	2016-11-03 10:07:54.741528+00
625	Ек-5172453	0	3	3448	CREATED	кол-во коробок 103	2507	114121.80	2	\N	\N	\N	\N	\N	2016-11-03 10:07:54.741528+00
626	СПб-016137	43	3	3448	CREATED		2505	63762.84	1	\N	\N	\N	\N	\N	2016-11-03 10:07:54.741528+00
623	1	99	3	3452	DELIVERED		26494,26495,26496,26497,26498,26499,26500	663000.00	5	\N	\N	\N	\N	\N	2016-11-03 08:11:30.281207+00
601		0	7	3432	DELIVERED			0.00	15	\N	\N	\N	\N	\N	2016-11-02 10:21:07.74128+00
602		0	7	3433	DELIVERED			0.00	8	\N	\N	\N	\N	\N	2016-11-02 10:22:43.338099+00
604	ЕК-5172102	1	3	3437	DELIVERED		Рн-12402	58378.00	1	\N	\N	\N	\N	\N	2016-11-02 12:08:15.55979+00
605	ЕК-5172103	1	3	3437	DELIVERED		Рн-12401	56073.00	1	\N	\N	\N	\N	\N	2016-11-02 12:08:15.55979+00
628	2	9	4	3453	CREATED		ПК00-000282 от 03.11.2016	43624.00	0	\N	\N	\N	\N	\N	2016-11-03 13:17:31.217739+00
629	3	20	7	3453	CREATED		ПК00-000280 от 03.11.2016	92680.00	0	\N	\N	\N	\N	\N	2016-11-03 13:17:31.217739+00
627	1	19	3	3453	CREATED		ПК00-000281 от 03.11.2016	77800.00	0	\N	\N	\N	\N	\N	2016-11-03 13:08:35.652962+00
608		23	10	3439	DELIVERED		1025	0.00	1	\N	\N	\N	\N	\N	2016-11-02 12:42:10.299065+00
617		0	3	3446	DELIVERED			0.00	4	\N	\N	\N	\N	\N	2016-11-03 06:02:36.358797+00
620		0	4	3446	DELIVERED			0.00	1	\N	\N	\N	\N	\N	2016-11-03 06:02:36.358797+00
618		0	7	3446	DELIVERED			0.00	6	\N	\N	\N	\N	\N	2016-11-03 06:02:36.358797+00
619		0	10	3446	DELIVERED			0.00	2	\N	\N	\N	\N	\N	2016-11-03 06:02:36.358797+00
423	1	95	3	2414	CREATED		39915	534690.00	4	\N	\N	\N	\N	\N	2016-10-17 08:54:46.489185+00
420	СПб-015575	2	6	2411	DELIVERED		1417	9328.00	0	\N	\N	\N	\N	\N	2016-10-17 07:09:12.048324+00
421	Ек-5159043	28	7	2411	DELIVERED		1412;491	190440.00	1	\N	\N	\N	\N	\N	2016-10-17 07:09:12.048324+00
422		0	3	2413	DELIVERED			0.00	8	\N	\N	\N	\N	\N	2016-10-17 07:39:30.211302+00
430		0	7	2420	CREATED			0.00	2	\N	\N	\N	\N	\N	2016-10-18 11:46:39.898596+00
431		0	3	2420	CREATED			0.00	2	\N	\N	\N	\N	\N	2016-10-18 11:46:39.898596+00
432		0	4	2420	CREATED			0.00	1	\N	\N	\N	\N	\N	2016-10-18 11:46:39.898596+00
504		0	3	3344	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-24 09:08:49.898408+00
468		0	3	2495	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-21 11:29:07.186898+00
499		0	3	3311	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-24 08:27:25.567511+00
482		99	3	2500	DELIVERED			0.00	6	\N	\N	\N	\N	\N	2016-10-24 07:11:05.387404+00
457	Ек-5170335	25	7	2487	CREATED		1150	235567.00	0	\N	\N	\N	\N	\N	2016-10-20 02:13:32.52728+00
516	СПб-015394	2	5	3356	CREATED		1004	46487.00	0	\N	\N	\N	\N	\N	2016-10-26 02:40:56.24764+00
517	СПб-015394	2	5	3356	CREATED		1004	39383.00	0	\N	\N	\N	\N	\N	2016-10-26 02:40:56.24764+00
445		0	3	2486	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-19 11:46:50.124483+00
442		0	3	2427	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-19 08:54:24.38729+00
427		0	7	2417	DELIVERED		359	0.00	0	\N	\N	\N	\N	\N	2016-10-18 07:36:02.030184+00
426		0	3	2417	DELIVERED		360	0.00	0	\N	\N	\N	\N	\N	2016-10-18 07:36:02.030184+00
463	ЕК-5159388	7	3	2418	DELIVERED		РН-11721	30951.00	1	\N	\N	\N	\N	\N	2016-10-20 09:15:53.069348+00
462	ЕК-5159387	11	3	2418	DELIVERED		РН-11720	80205.00	1	\N	\N	\N	\N	\N	2016-10-20 09:15:53.069348+00
464	1	99	3	2490	DELIVERED		24953,24954,24951	755000.00	4	\N	\N	\N	\N	\N	2016-10-20 09:36:04.784042+00
466	Ек-5170350	51	3	2491	DELIVERED		2396	50676.08	2	\N	\N	\N	\N	\N	2016-10-20 12:22:12.963589+00
465	Ек-5170360	54	3	2491	DELIVERED		2397	64610.64	1	\N	\N	\N	\N	\N	2016-10-20 12:22:12.963589+00
467		0	3	2494	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-21 11:28:45.356811+00
458		0	3	2488	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-20 05:53:15.112135+00
460		0	3	2488	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-20 08:31:54.073148+00
459		0	3	2489	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-20 05:53:25.382124+00
461		0	3	2489	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-20 08:32:01.084183+00
471	СПб-015749	0	10	2499	CREATED		ЭКМ00031213	171818.20	0	\N	\N	\N	\N	\N	2016-10-24 07:06:23.744785+00
472	СПб-015755	0	10	2499	CREATED		ЭКМ00031222	20179.00	0	\N	\N	\N	\N	\N	2016-10-24 07:06:23.744785+00
473	СПб-015735	0	10	2499	CREATED		ЭКМ00031216	238637.21	0	\N	\N	\N	\N	\N	2016-10-24 07:06:23.744785+00
474	СПб-015736	0	10	2499	CREATED		ЭКМ00031218	265381.24	0	\N	\N	\N	\N	\N	2016-10-24 07:06:23.744785+00
475	СПб-015746	0	10	2499	CREATED		ЭКМ00031219	160055.58	0	\N	\N	\N	\N	\N	2016-10-24 07:06:23.744785+00
476	СПб-015751	0	10	2499	CREATED		ЭКМ00031212	177932.54	0	\N	\N	\N	\N	\N	2016-10-24 07:06:23.744785+00
477	СПб-015757	0	10	2499	CREATED		ЭКМ00031221	258566.96	0	\N	\N	\N	\N	\N	2016-10-24 07:06:23.744785+00
478	СПб-015753	0	10	2499	CREATED		ЭКМ00031215	144386.39	0	\N	\N	\N	\N	\N	2016-10-24 07:06:23.744785+00
479	СПб-015748	0	10	2499	CREATED		ЭКМ00031223	154613.08	0	\N	\N	\N	\N	\N	2016-10-24 07:06:23.744785+00
480	СПб-015752	0	10	2499	CREATED		ЭКМ00031211	173922.07	0	\N	\N	\N	\N	\N	2016-10-24 07:06:23.744785+00
481	СПб-015759	0	10	2499	CREATED			11259.50	0	\N	\N	\N	\N	\N	2016-10-24 07:06:23.744785+00
505		0	3	3345	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-25 05:48:55.489901+00
536	Ек-5171497	8	7	3365	CREATED		1272	118575.00	0	\N	\N	\N	\N	\N	2016-10-28 04:45:08.137335+00
544		0	6	3371	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-28 07:05:42.18975+00
545		0	10	3372	CREATED			0.00	0	\N	\N	\N	\N	\N	2016-10-28 07:08:37.792239+00
526	Ек-5171170	99	7	3358	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-26 08:09:01.762612+00
522	Ек-5171181	99	3	3358	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-26 08:09:01.762612+00
533	Ек-5171375	99	8	3358	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-27 13:56:47.412739+00
521	Ек-5171144 	99	4	3358	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-26 08:09:01.762612+00
525	Ек-5171185	99	10	3358	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-26 08:09:01.762612+00
534	Ек-5171383 	99	7	3358	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-27 13:56:47.412739+00
523	Ек-5171183	99	5	3358	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-26 08:09:01.762612+00
535	 Ек-5171396	99	3	3358	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-27 13:56:47.412739+00
524	Ек-5171152 	99	8	3358	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-26 08:09:01.762612+00
527	Ек-5171186	99	8	3358	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-26 08:09:01.762612+00
555	1	99	3	3377	DELIVERED		25910,25909,25906,25905,25904	1091657.00	6	\N	\N	\N	\N	\N	2016-10-28 11:57:51.330582+00
469	Ек-5170743	13	7	2496	DELIVERED		1178	180248.00	0	\N	\N	\N	\N	\N	2016-10-24 04:17:23.253143+00
537		0	3	3366	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-28 04:59:35.722309+00
538		0	3	3367	DELIVERED			0.00	0	\N	\N	\N	\N	\N	2016-10-28 04:59:59.129644+00
557		63	3	3379	DELIVERED			0.00	5	\N	\N	\N	\N	\N	2016-10-28 13:10:01.469604+00
\.


--
-- TOC entry 2328 (class 0 OID 0)
-- Dependencies: 198
-- Name: orders_orderid_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('orders_orderid_seq', 629, true);


--
-- TOC entry 2260 (class 0 OID 20908)
-- Dependencies: 185
-- Data for Name: permissions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY permissions (permissionid) FROM stdin;
DELETE_ANY_DONUT
DELETE_CREATED_DONUT
\.


--
-- TOC entry 2261 (class 0 OID 20913)
-- Dependencies: 186
-- Data for Name: permissions_for_roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY permissions_for_roles (userroleid, permissionid) FROM stdin;
WH_BOSS	DELETE_ANY_DONUT
SUPPLIER_MANAGER	DELETE_CREATED_DONUT
\.


--
-- TOC entry 2271 (class 0 OID 20999)
-- Dependencies: 196
-- Data for Name: supplier_users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY supplier_users (userid, supplierid) FROM stdin;
11	8
12	9
13	10
14	11
15	12
53	13
54	14
55	17
56	15
57	16
58	18
59	19
60	20
61	21
62	22
63	23
64	24
65	25
66	26
67	27
68	28
69	29
70	30
71	31
73	33
74	34
75	35
76	36
81	41
83	43
84	44
85	45
88	48
89	49
90	50
92	52
93	53
94	54
95	55
96	56
98	57
494	133
495	134
499	138
501	140
117	147
118	148
119	149
120	150
121	151
498	137
129	155
123	157
124	158
125	159
127	160
496	135
462	101
509	162
510	163
511	164
512	165
513	166
472	111
514	167
473	112
470	109
468	107
464	103
126	168
114	170
128	172
492	131
131	173
122	174
517	176
518	177
522	178
523	178
524	176
526	179
527	179
528	179
465	104
529	169
533	169
503	180
534	182
535	126
536	183
537	184
538	185
539	186
542	187
543	188
545	189
546	190
548	193
549	191
551	118
552	194
553	195
554	196
555	200
556	201
558	75
559	53
560	202
562	143
563	203
477	116
571	204
572	205
\.


--
-- TOC entry 2270 (class 0 OID 20990)
-- Dependencies: 195
-- Data for Name: suppliers; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY suppliers (supplierid, inn, clientname, kpp, coraccount, curaccount, bik, bankname, contractnumber, dateofsigning, startcontractdate, endcontractdate) FROM stdin;
134	Голд Пак	ООО "Голд Пак Север" (г. Красноярск)	\N	\N	\N	\N	\N	\N	\N	\N	\N
133	Мирей	ООО "Мирей"	\N	\N	\N	\N	\N	\N	\N	\N	\N
167	Нева-Лайт	Нева-Лайт 	\N	\N	\N	\N	\N	\N	\N	\N	\N
155	Уралпак	Уралпак	\N	\N	\N	\N	\N	\N	\N	\N	\N
75	Триумф	ООО "Триумф" 	\N	\N	\N	\N	\N	\N	\N	\N	\N
148	АКОР	ПП "АКОР"	\N	\N	\N	\N	\N	\N	\N	\N	\N
24	РОСЭЛ	ООО "Росэл"	\N	\N	\N	\N	\N	\N	\N	\N	\N
126	1-й Терм. З-д Екб	ООО "Первый термометровый завод" 	\N	\N	\N	\N	\N	\N	\N	\N	\N
23	Вектор Групп	Вектор Групп (Москва)	\N	\N	\N	\N	\N	\N	\N	\N	\N
36	Источник	ООО "Источник бэттериз",Юнисел	\N	\N	\N	\N	\N	\N	\N	\N	\N
165	Невапластик	Невапластик 	\N	\N	\N	\N	\N	\N	\N	\N	\N
25	1-ый термометровый з-д	ООО "Первый термометровый завод" 	\N	\N	\N	\N	\N	\N	\N	\N	\N
55	Капитал ПРОК	АО "Капитал-ПРОК"	\N	\N	\N	\N	\N	\N	\N	\N	\N
28	Биосталь	ООО "Биосталь"	\N	\N	\N	\N	\N	\N	\N	\N	\N
131	Альбертотекс	ООО "Альбертотекс"	\N	\N	\N	\N	\N	\N	\N	\N	\N
138	Комильфо	ООО "Комильфо"	\N	\N	\N	\N	\N	\N	\N	\N	\N
149	АСТ-Трейд	ООО "АСТ-Трейд"	\N	\N	\N	\N	\N	\N	\N	\N	\N
164	Дори+	Дори+ 	\N	\N	\N	\N	\N	\N	\N	\N	\N
101	Экопром	ООО "ТД Экопром "	\N	\N	\N	\N	\N	\N	\N	\N	\N
18	Джетт	Джетт	\N	\N	\N	\N	\N	\N	\N	\N	\N
31	Мастикс	ООО "Мастикс"	\N	\N	\N	\N	\N	\N	\N	\N	\N
151	Элис	ООО "Элис"	\N	\N	\N	\N	\N	\N	\N	\N	\N
53	Химик Спб	ООО Торговая компания "Химик"	\N	\N	\N	\N	\N	\N	\N	\N	\N
33	ММЗ Мыловар	ООО "ММЗ" (Мыловар)	\N	\N	\N	\N	\N	\N	\N	\N	\N
17	РУНА	РУНА	\N	\N	\N	\N	\N	\N	\N	\N	\N
137	Чистые техн.	ООО "Чистые Технологии"	\N	\N	\N	\N	\N	\N	\N	\N	\N
44	Офисмаг	Офисмаг	\N	\N	\N	\N	\N	\N	\N	\N	\N
11	ТДМ	ТДМ	\N	\N	\N	\N	\N	\N	\N	\N	\N
20	А.Д.М.	ООО "А.Д.М."	\N	\N	\N	\N	\N	\N	\N	\N	\N
140	УПП Энергия	ООО "Чебоксарское УПП "Энергия"	\N	\N	\N	\N	\N	\N	\N	\N	\N
147	Интертул	Компания Интертул	\N	\N	\N	\N	\N	\N	\N	\N	\N
158	Теза-Трейд	ООО "Теза-Трейд"	\N	\N	\N	\N	\N	\N	\N	\N	\N
35	Совершенный свет	ООО "Совершенный свет"	\N	\N	\N	\N	\N	\N	\N	\N	\N
150	СКРАП	ООО СКРАП	\N	\N	\N	\N	\N	\N	\N	\N	\N
9	Ecola	Компания ECOLA	\N	\N	\N	\N	\N	\N	\N	\N	\N
13	LEEK	ЗАО «Энергокомплект»	\N	\N	\N	\N	\N	\N	\N	\N	\N
16	ОптЭлектроТорг	ООО «ОптЭлектроТогр»	\N	\N	\N	\N	\N	\N	\N	\N	\N
26	КЛАСС	ООО "КЛАСС"	\N	\N	\N	\N	\N	\N	\N	\N	\N
166	ELEKTRA	Электра	\N	\N	\N	\N	\N	\N	\N	\N	\N
118	Крупышева 	ИП Крупышева Екатерина Юрьевна	\N	\N	\N	\N	\N	\N	\N	\N	\N
107	МИРЕКС 	АО "Компания "МИРЕКС"	\N	\N	\N	\N	\N	\N	\N	\N	\N
160	Мастер Хаус 	Мастер Хаус	\N	\N	\N	\N	\N	\N	\N	\N	\N
135	Упаковка и Сервис-Урал	"Упаковка и Сервис"	\N	\N	\N	\N	\N	\N	\N	\N	\N
157	Вилина	ООО "Вилина"	\N	\N	\N	\N	\N	\N	\N	\N	\N
30	Арктика	ООО "Арктика"	\N	\N	\N	\N	\N	\N	\N	\N	\N
162	ТДФ	ООО «ТДФ»	\N	\N	\N	\N	\N	\N	\N	\N	\N
163	Промгруппа	ООО «Промгруппа»	\N	\N	\N	\N	\N	\N	\N	\N	\N
204	Конверс	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
205	Профклей	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
10	БТЛ	ООО "БТЛ"	\N	\N	\N	\N	\N	\N	\N	\N	\N
159	Хазар	ООО "Хазар" 	\N	\N	\N	\N	\N	\N	\N	\N	\N
48	Савин Трейдинг	ООО "Новис"	\N	\N	\N	\N	\N	\N	\N	\N	\N
15	ЕКФ	Компания "EKF"	\N	\N	\N	\N	\N	\N	\N	\N	\N
14	ИЭК	Компания "IEK"	\N	\N	\N	\N	\N	\N	\N	\N	\N
34	Барьер	ООО "Аквасистемы"	\N	\N	\N	\N	\N	\N	\N	\N	\N
104	Ситилайт	ООО "Ситилайт"	\N	\N	\N	\N	\N	\N	\N	\N	\N
43	Техноэксп	Техноэкспорт	\N	\N	\N	\N	\N	\N	\N	\N	\N
49	Зенча Псков	АО "Электротехнический Завод  "ЗЕНЧА-Псков"	\N	\N	\N	\N	\N	\N	\N	\N	\N
116	Химик Урал	ООО Торговая компания "Химик"	\N	\N	\N	\N	\N	\N	\N	\N	\N
29	Стека Плюс	ООО "Стека Плюс"	\N	\N	\N	\N	\N	\N	\N	\N	\N
22	Мир Красок	ИП Копытин (Урал)	\N	\N	\N	\N	\N	\N	\N	\N	\N
50	Цифромаркет	ООО «Цифромаркет»	\N	\N	\N	\N	\N	\N	\N	\N	\N
109	ЛЕД-Тех	ООО "ЛЕД-ТЕХНОЛОГИИ"	\N	\N	\N	\N	\N	\N	\N	\N	\N
19	Гринфилд	ООО "Гринфилд Рус"	\N	\N	\N	\N	\N	\N	\N	\N	\N
12	AZ	ООО "AZ"	\N	\N	\N	\N	\N	\N	\N	\N	\N
41	Электрическая Мануфактура	ООО "Электрическая мануфактура"	\N	\N	\N	\N	\N	\N	\N	\N	\N
57	ВИП-маркет	ООО "ВИП-Маркет"	\N	\N	\N	\N	\N	\N	\N	\N	\N
143	СДС-ГРУППА	ООО "СДС-поставщик"	\N	\N	\N	\N	\N	\N	\N	\N	\N
45	Аванти	ООО "Аванти"	\N	\N	\N	\N	\N	\N	\N	\N	\N
103	Дохлокс	ООО "Дохлокс"	\N	\N	\N	\N	\N	\N	\N	\N	\N
168	Мультидом	Мультидом Трейдин	\N	\N	\N	\N	\N	\N	\N	\N	\N
186	Гермес-Трейд	Гермес-Трейд Комплект	\N	\N	\N	\N	\N	\N	\N	\N	\N
194	Гуси Электрик ТД	Гуси Электрик ТД	\N	\N	\N	\N	\N	\N	\N	\N	\N
185	Калашников ТД	Калашников ТД	\N	\N	\N	\N	\N	\N	\N	\N	\N
172	Мультипласт	Мультипласт Групп	\N	\N	\N	\N	\N	\N	\N	\N	\N
54	КОНТЕЙ - ТРЕЙД	ООО "КОНТЕЙ - ТРЕЙД"	\N	\N	\N	\N	\N	\N	\N	\N	\N
178	Концепт Быт	Полимер КБ	\N	\N	\N	\N	\N	\N	\N	\N	\N
56	Пластик Репаблик	ООО "Пластик Репаблик"	\N	\N	\N	\N	\N	\N	\N	\N	\N
173	ТекстильСити	ООО "Текстиль-Сити"	\N	\N	\N	\N	\N	\N	\N	\N	\N
202	Симон Электрик	Симон Электрик	\N	\N	\N	\N	\N	\N	\N	\N	\N
8	Энергосистемы	ЗАО "Энергосистемы и технологии"	\N	\N	\N	\N	\N	\N	\N	\N	\N
180	СИГНАЛЭЛЕКТРОНИКС	СигналЭлектроникс	\N	\N	\N	\N	\N	\N	\N	\N	\N
52	Рубикон	ООО "Рубикон"	\N	\N	\N	\N	\N	\N	\N	\N	\N
191	Сфера Света	Сфера света	\N	\N	\N	\N	\N	\N	\N	\N	\N
111	Уралтекстиль	ООО "Фабрика Уралтекстиль"	\N	\N	\N	\N	\N	\N	\N	\N	\N
112	Теккрон	ООО "Теккрон"	\N	\N	\N	\N	\N	\N	\N	\N	\N
195	ЭКОГАРАНТ	ЭКОГАРАНТ	\N	\N	\N	\N	\N	\N	\N	\N	\N
182	ENERGIZER	"ENERGIZER"	\N	\N	\N	\N	\N	\N	\N	\N	\N
187	Меркурий М	ООО "МЕРКУРИЙ М"	\N	\N	\N	\N	\N	\N	\N	\N	\N
174	Евроимпорт	Евроимпорт 	\N	\N	\N	\N	\N	\N	\N	\N	\N
188	Техфорс	Техфорс 	\N	\N	\N	\N	\N	\N	\N	\N	\N
189	Энергопром	ООО «Энергопром»	\N	\N	\N	\N	\N	\N	\N	\N	\N
190	Шнейдер	Schneider Electric	\N	\N	\N	\N	\N	\N	\N	\N	\N
196	Энергокомплект	ООО "ЭНЕРГОКОМПЛЕКТ" 	\N	\N	\N	\N	\N	\N	\N	\N	\N
200	Осрам	Компания "ОСРАМ" 	\N	\N	\N	\N	\N	\N	\N	\N	\N
21	Градиент	ООО "Градиент Развитие"	\N	\N	\N	\N	\N	\N	\N	\N	\N
203	Philips	Компания «Philips» 	\N	\N	\N	\N	\N	\N	\N	\N	\N
183	Panasonic	"Panasonic"	\N	\N	\N	\N	\N	\N	\N	\N	\N
179	Красноармейск	Склад Красноармейск	\N	\N	\N	\N	\N	\N	\N	\N	\N
27	ТехЛаб	ООО "Техлабтерм" \n(ПАО "Стеклоприбор")	\N	\N	\N	\N	\N	\N	\N	\N	\N
170	Чайка Инвестопт	ООО "Чайка Инвестопт"	\N	\N	\N	\N	\N	\N	\N	\N	\N
184	T-plast	ЕвроПрофиль  (T-plast)	\N	\N	\N	\N	\N	\N	\N	\N	\N
177	Профессионал 	Профессионал 	\N	\N	\N	\N	\N	\N	\N	\N	\N
176	Аполло	Аполло ТД	\N	\N	\N	\N	\N	\N	\N	\N	\N
201	АСД-Москва	АСД-Москва	\N	\N	\N	\N	\N	\N	\N	\N	\N
193	Топсервис	Топсервис (Эко-Трейд)	\N	\N	\N	\N	\N	\N	\N	\N	\N
169	Оптима-М	Оптима-М	\N	\N	\N	\N	\N	\N	\N	\N	\N
\.


--
-- TOC entry 2329 (class 0 OID 0)
-- Dependencies: 194
-- Name: suppliers_supplierid_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('suppliers_supplierid_seq', 205, true);


--
-- TOC entry 2257 (class 0 OID 20885)
-- Dependencies: 182
-- Data for Name: user_roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY user_roles (userroleid) FROM stdin;
WH_BOSS
WH_DISPATCHER
WH_SECURITY_OFFICER
SUPPLIER_MANAGER
\.


--
-- TOC entry 2259 (class 0 OID 20892)
-- Dependencies: 184
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY users (userid, userlogin, salt, passandsalt, userroleid, username, phonenumber, email, "position") FROM stdin;
81	ELMAN	jrteOl270Hx8gS75	3f29aa72de0eae505348105042f4179f	SUPPLIER_MANAGER	ООО "Электрическая мануфактура"	8(495) 505-97-82, доб. 119; +7-905-755-33-79	bodrova@elman.su	логист
550	smirnov_a	jrteOl270Hx8gS75	07f47e9ea60f38ceba7bcdffcfc13e29	WH_BOSS	Смирнов Андрей Зотеевич	90163569874	smirnov_andrey@ekt.sbat.ru	Руководитель
562	sds-group	jrteOl270Hx8gS75	3b49f7aca441a6d4e9983f455e70de13	SUPPLIER_MANAGER	ООО "СДС-поставщик"	8(495)225-25-20; 8-903-618-10-10	lyablin@sds-group.ru	логист
15	AZ1	jrteOl270Hx8gS75	aca3c4db5b69109ab4132d0f6140013d	SUPPLIER_MANAGER	Поставка AZ	 8(495) 926-50-50	KFilimonov@azcompany.ru	логист
14	TDM	jrteOl270Hx8gS75	7d688f338e566b136a5194e3949add95	SUPPLIER_MANAGER	Поставка ТДМ	 8 499 558 29 93, 8 495 727 32 14	logish@tdme.ru	логист
549	sweko	jrteOl270Hx8gS75	fce58a3b78d079b1fe7aacde8c1042aa	SUPPLIER_MANAGER	 Сфера света 	+7 (495) 64-96-436	dima@sweko.ru 	manager
12	Ecola	jrteOl270Hx8gS75	6eb7159e27ea08cee99d84e650de237f	SUPPLIER_MANAGER	Поставка Ecola	000000000	rusakovavm@logicsmart.ru	логист
13	BTL	jrteOl270Hx8gS75	4f552f0b188e36fe0031cad26a34df26	SUPPLIER_MANAGER	Поставка БТЛ	000000000	rusakovavm@logicsmart.ru	логист
16	smirnov_andrey@ekt.sbat.ru	jrteOl270Hx8gS75	28b76da7eea30600f32ae43b56d2a54c	WH_BOSS	Смирнов Андрей Зотеевич	90163569874	smirnov_andrey@ekt.sbat.ru	Руководитель
17	mordvinov	jrteOl270Hx8gS75	8b18fa6d03b92343c38cb29eeced7285	WH_BOSS	Мордвинов Сергей Анатольевич	89035185890	mordvinov@moscow.sbat.ru	Руководитель
18	ryabov_i@ulianovsk.sbat.ru	jrteOl270Hx8gS75	f6448ce02190e1a4071f561f4a07f898	WH_BOSS	Рябов Игорь Анатольевич	79279829062	ryabov_i@ulianovsk.sbat.ru	Руководитель
19	volodenkov	jrteOl270Hx8gS75	8cb4caadf499f3f323ef999cee3f745a	WH_BOSS	Володенков Вячеслав	79384081472	volodenkov@aneva.ru	Руководитель
20	malyshev_a@novosibirsk.sbat.ru	jrteOl270Hx8gS75	c474d937572bb7c90387daca85e4997e	WH_BOSS	Малышев Александр Александрович	89134551335	malyshev_a@novosibirsk.sbat.ru	Руководитель
21	spb	jrteOl270Hx8gS75	bcf5f94655c879d0f44df34a647910ce	WH_BOSS	Начальник склада Санкт-Петербург	000000000	smirnovayi@logicsmart.ru	Руководитель
22	irk	jrteOl270Hx8gS75	d959f6f8e7879e3453dbdf3a88a4bcf7	WH_BOSS	Начальник склада Иркутск	000000000	smirnovayi@logicsmart.ru	Руководитель
23	ud1	jrteOl270Hx8gS75	c99c43749ca27407bac80057a807bcec	WH_DISPATCHER	Диспетчер склада	000000000	smirnovayi@logicsmart.ru	Диспетчер
24	ud2	jrteOl270Hx8gS75	c99c43749ca27407bac80057a807bcec	WH_DISPATCHER	Диспетчер склада	000000000	smirnovayi@logicsmart.ru	Диспетчер
25	ud3	jrteOl270Hx8gS75	c99c43749ca27407bac80057a807bcec	WH_DISPATCHER	Диспетчер склада	000000000	smirnovayi@logicsmart.ru	Диспетчер
26	ud4	jrteOl270Hx8gS75	c99c43749ca27407bac80057a807bcec	WH_DISPATCHER	Диспетчер склада	000000000	smirnovayi@logicsmart.ru	Диспетчер
27	ud5	jrteOl270Hx8gS75	c99c43749ca27407bac80057a807bcec	WH_DISPATCHER	Диспетчер склада	000000000	smirnovayi@logicsmart.ru	Диспетчер
28	ud6	jrteOl270Hx8gS75	c99c43749ca27407bac80057a807bcec	WH_DISPATCHER	Диспетчер склада	000000000	smirnovayi@logicsmart.ru	Диспетчер
29	ud7	jrteOl270Hx8gS75	c99c43749ca27407bac80057a807bcec	WH_DISPATCHER	Диспетчер склада	000000000	smirnovayi@logicsmart.ru	Диспетчер
30	uo1	jrteOl270Hx8gS75	c817bc747116f6912235c50254836bf0	WH_SECURITY_OFFICER	ОХРАНА	000000000	sidor@s.ru	officer
31	uo2	jrteOl270Hx8gS75	c817bc747116f6912235c50254836bf0	WH_SECURITY_OFFICER	ОХРАНА	000000000	sidor@s.ru	officer
32	uo3	jrteOl270Hx8gS75	c817bc747116f6912235c50254836bf0	WH_SECURITY_OFFICER	ОХРАНА	000000000	sidor@s.ru	officer
33	uo4	jrteOl270Hx8gS75	c817bc747116f6912235c50254836bf0	WH_SECURITY_OFFICER	ОХРАНА	000000000	sidor@s.ru	officer
34	uo5	jrteOl270Hx8gS75	c817bc747116f6912235c50254836bf0	WH_SECURITY_OFFICER	ОХРАНА	000000000	sidor@s.ru	officer
35	uo6	jrteOl270Hx8gS75	c817bc747116f6912235c50254836bf0	WH_SECURITY_OFFICER	ОХРАНА	000000000	sidor@s.ru	officer
36	uo7	jrteOl270Hx8gS75	c817bc747116f6912235c50254836bf0	WH_SECURITY_OFFICER	ОХРАНА	000000000	sidor@s.ru	officer
37	agamyan	jrteOl270Hx8gS75	d2e20913cd40e14a934a5439d8ce6496	WH_BOSS	Агамян С.М	000000000	agamyan@aneva.ru	зам. зав. складом
38	bahteyarova	jrteOl270Hx8gS75	da090ad02a0929099b82472b9db98cb9	WH_DISPATCHER	Бахтеярова С.А	000000000	bahteyarova@aneva.ru	Диспетчер
39	nikolaev	jrteOl270Hx8gS75	da090ad02a0929099b82472b9db98cb9	WH_DISPATCHER	Николаев С.А.	000000000	nikolaev@aneva.ru	Диспетчер
40	Shutko_s	jrteOl270Hx8gS75	73b1f3aa3133725c2223df355f7df183	WH_BOSS	Шутько Станислав Станиславович	84997077230	Shutko_s@moscow.sbat.ru	Зам. начальника склада
41	runova	jrteOl270Hx8gS75	da090ad02a0929099b82472b9db98cb9	WH_DISPATCHER	Рунова Юлия Леонидовна	84997077230	runova@moscow.sbat.ru	Старший товаровед
42	samoletova	jrteOl270Hx8gS75	fabe55294183ff535f2a1a27012105f0	WH_DISPATCHER	Самолетова Юлия Равилевна	84997077230	samoletova_u@moscow.sbat.ru	Товаровед
43	akobian	jrteOl270Hx8gS75	1f178d78e3de9142ab9cf33e1dfbd29d	WH_DISPATCHER	Акобян Марине Меликсетовна	84997077230	akobian_m@moscow.sbat.ru	Оператор приемки
44	blazhko	jrteOl270Hx8gS75	ded6f2d776a5343ac49b76124b22b1f3	WH_DISPATCHER	Блажко Инна Александровна	84997077230	blazhko_i@moscow.sbat.ru	Оператор приемки
45	aleshina	jrteOl270Hx8gS75	b7458c6d0e23ed15eea87f5aa4c6fb03	WH_DISPATCHER	Алешина Екатерина Валерьевна	84997077230	aleshina_e@moscow.sbat.ru	Диспетчер
46	kochenkov	jrteOl270Hx8gS75	8d753b2d499239dcbd8caa23fb3cbc9d	WH_DISPATCHER	Коченков Алексей Игоревич	84997077230	kochenkov_a@moscow.sbat.ru	Диспетчер
47	vasiliev_m	jrteOl270Hx8gS75	eb53ea6aa50a3b3c85c12776a96132e1	WH_DISPATCHER	Васильев Михаил Александрович	000000000	vasiliev_m@ulianovsk.sbat.ru	старший смены 
48	stepanov_ev	jrteOl270Hx8gS75	88108cf7ff1991117838e61e28c22432	WH_DISPATCHER	Степанов Евгений Вечеславович	000000000	stepanov_ev@ulianovsk.sbat.ru	старший смены 
49	lytkina_n	jrteOl270Hx8gS75	756c78bae548d39961a2b61de8424939	WH_DISPATCHER	Лыткина Наталья Владимировна	000000000	lytkina_n@ulianovsk.sbat.ru	товаровед 
50	ginkul_n	jrteOl270Hx8gS75	6cba2000e7771359e772c2fd80052e33	WH_DISPATCHER	Гинкул Наталья Анатольевна	000000000	ginkul_n@ulianovsk.sbat.ru	спец. склада брака 
51	petrova	jrteOl270Hx8gS75	40fad4457e6d54e7ab1a65d7346c0cff	WH_BOSS	Петрова Татьяна	000000000	petrova_t@irk.sbat.ru	начальник склада
52	vasilieva	jrteOl270Hx8gS75	8bf53f00a6e5cff33f67785085b0f3a4	WH_DISPATCHER	Васильева Надежда	00000000000	rusakovavm@logicsmart.ru	Диспетчер
53	LEEK1	jrteOl270Hx8gS75	c294a9954d288e0fc5c12c5180bfc23c	SUPPLIER_MANAGER	Руслан Головнев	8-495-781-06-75	golovnev@leek-lamp.ru	логист
54	ИЭК1	jrteOl270Hx8gS75	c2ec314c965cd69a593bf3756cbe7581	SUPPLIER_MANAGER	Голдыш Алла	000000000	goldishaa@iek.ru	логист
55	РУНА1	jrteOl270Hx8gS75	ea167891bedd237787a8530a8c9537e5	SUPPLIER_MANAGER	Наталия Почтова	8-916-972-76-67	pochtova@lezard.su	логист
57	ОЭТ1	jrteOl270Hx8gS75	a10c53e036920fc1cbbd20e4d12505a1	SUPPLIER_MANAGER	Антонова Евгения Олеговна	000000000	aeo@univ.su	логист
58	Джетт1	jrteOl270Hx8gS75	a443fdb377847783a82c0b5964b30e78	SUPPLIER_MANAGER	Шешина Татьяна	8 (495) 645-82-42	stv@jett.ru	логист
60	ADM	jrteOl270Hx8gS75	2ad5a3cc3a8686eef7af1703ef87e8d3	SUPPLIER_MANAGER	Поставка А.Д.М.	000000000	rusakovavm@logicsmart.ru	логист
542	MercuryM	jrteOl270Hx8gS75	fce58a3b78d079b1fe7aacde8c1042aa	SUPPLIER_MANAGER	Меркурий М	89099464215	apolynina@elecset.ru	логист
59	GFrus	jrteOl270Hx8gS75	e9d1364f7b71b0cb64435cf580db7016	SUPPLIER_MANAGER	Поставка Гринфилд	 8 (495)232-20-15, 744-56-40, 743-67-69	cyy@gf-r.ru	логист
74	BR8	jrteOl270Hx8gS75	9d76880f75733170d27386393ccaf5df	SUPPLIER_MANAGER	Поставка	000000000	rusakovavm@logicsmart.ru	логист
56	ЭКФ1	jrteOl270Hx8gS75	969ddc79fdbe6c6e1725a73137307ab5	SUPPLIER_MANAGER	Смирнова Мария	 8 495 788 88 15, доб.178, 8 926 832 42 77 	M.Smirnova@ekf.su, N.Galushko@ekf.ru	логист
551	Logist187400	jrteOl270Hx8gS75	0e314aa62dcf104b2271833cbbb7b8b7	SUPPLIER_MANAGER	ИП Крупышева 	 8(812) 449-90-20 	 logist@pgptrade.com	логист
563	Philips	jrteOl270Hx8gS75	1d497d18e118778de473e030f783cdaa	SUPPLIER_MANAGER	Philips	00000000	 Ekaterina.shvyryaeva@philips.com   	логист
61	GRAD	jrteOl270Hx8gS75	e4f91684147e2c8854f850774e81dd18	SUPPLIER_MANAGER	Поставка Градиент	отсутствует	n.lokucievskaya@gradural.ru	логист
63	VECTOR	jrteOl270Hx8gS75	da872f0458f30a273df12ae3687f3ccc	SUPPLIER_MANAGER	Вектор Групп (Москва)	8-920-972-37-35	henkel.vector@mail.ru	логист
62	AVKTD	jrteOl270Hx8gS75	0372e0deac5740ca6f1a1ff8163c2c23	SUPPLIER_MANAGER	Мир Красок	8(343) 351-03-66 доб.104	matorina@avktd.ru	логист
64	ROSEL	jrteOl270Hx8gS75	6eb7159e27ea08cee99d84e650de237f	SUPPLIER_MANAGER	РОСЭЛ	8 (812) 320-83-33, 8(922)6066361	dubrovinandrey@rosel.ru 	логист
65	Therm	jrteOl270Hx8gS75	6eb7159e27ea08cee99d84e650de237f	SUPPLIER_MANAGER	ООО "Первый термометровый завод"	8-915-079-91-96	seti@1thermometer.ru	логист
66	KLASS	jrteOl270Hx8gS75	92f60b20144763fc3a11af8428d93279	SUPPLIER_MANAGER	ООО "КЛАСС"	8 (812) 323-89-30 доб.3303	a.malinovskaya@jumboroll.ru	логист
67	Steklo	jrteOl270Hx8gS75	147bace277d2c1e16be016b8fddb04d7	SUPPLIER_MANAGER	ООО "Техлабтерм" 	8(495) 632-00-73, доб. 3166	home1@steklopribor.com	логист
69	STEKA	jrteOl270Hx8gS75	9d76880f75733170d27386393ccaf5df	SUPPLIER_MANAGER	ООО "Стека Плюс"	89617748568, 89501961176	stekaplus@mail.ru, steka05@mail.ru	логист
70	AR1	jrteOl270Hx8gS75	9d76880f75733170d27386393ccaf5df	SUPPLIER_MANAGER	ООО "Арктика"	8(495) 987-18-67 доб.103	opt3@rusarctica.ru	логист
71	MAST	jrteOl270Hx8gS75	9d76880f75733170d27386393ccaf5df	SUPPLIER_MANAGER	ООО "Мастикс"	 8-915-321-39-59	 info@mastiks.ru	логист
73	MMZ	jrteOl270Hx8gS75	9d76880f75733170d27386393ccaf5df	SUPPLIER_MANAGER	ООО "ММЗ" (Мыловар)	8(495) 225-81-16, доб. 100	tmiheykina@mosmilo.ru	логист
75	Helena	jrteOl270Hx8gS75	9d76880f75733170d27386393ccaf5df	SUPPLIER_MANAGER	ООО "Совершенный свет"	8(495) 965-05-60, 8(903)732-05-71	helena@uniel.ru	логист
76	IST1	jrteOl270Hx8gS75	3f29aa72de0eae505348105042f4179f	SUPPLIER_MANAGER	Ляпин Александр	(495) 223-25-29(30) доб. 116	sales1@istochnik.ru	логист
83	TEXP	jrteOl270Hx8gS75	3f29aa72de0eae505348105042f4179f	SUPPLIER_MANAGER	Техноэкспорт 	8 (495) 721-26-41 доб.332	trade@technoexport.ru	логист
84	SAMS	jrteOl270Hx8gS75	3f29aa72de0eae505348105042f4179f	SUPPLIER_MANAGER	Офисмаг	8 (495) 645-83-26,645-83-27	moscow@samsonopt.ru	логист
85	Avanti	jrteOl270Hx8gS75	3f29aa72de0eae505348105042f4179f	SUPPLIER_MANAGER	ООО "Аванти"	8(495)918-62-11	mish-a@yandex.ru	логист
88	Novis	jrteOl270Hx8gS75	3f29aa72de0eae505348105042f4179f	SUPPLIER_MANAGER	ООО "Новис"	8-495-504-27-70	migan2000@mail.ru	логист
567	ub10	jrteOl270Hx8gS75	4fa1583f9eb3ea339670581c4e955c43	WH_BOSS	Нач. склада	отсутствует	отсутствует	boss
543	Tehfors	jrteOl270Hx8gS75	12092b5ce2fa00c832f54d7b7444a8a3	SUPPLIER_MANAGER	Техфорс 	 84959806969 	y_petrova@mirvam.ru	логист
97	Blazhko	jrteOl270Hx8gS75	270e8ae9884c145efc6ae498a8b5f7e6	WH_DISPATCHER	Блажко Инна	000000000	smirnovayi@logicsmart.ru	Диспетчер
94	kontey	jrteOl270Hx8gS75	3f29aa72de0eae505348105042f4179f	SUPPLIER_MANAGER	ООО "КОНТЕЙ - ТРЕЙД"	 8-926-001-37-40	tsv_g@kontey.ru	логист
552	Gusi_El	jrteOl270Hx8gS75	736d23b320d9ac3d019aafdc8cc004f0	SUPPLIER_MANAGER	Гуси Электрик ТД 	 8(499) 642-55-03 доб.103 	 manager4@gusi.ru	логист
553	Ecogarant	jrteOl270Hx8gS75	bea4adaa2c28ceb36d0a09da75ada12e	SUPPLIER_MANAGER	ЭКОГАРАНТ	 +7(495) 580-37-91 	 irinaf@ecogarant.net	логист
554	EK	jrteOl270Hx8gS75	8cf37d5b02d202eaf65821c708726436	SUPPLIER_MANAGER	Энергокомплект	 8 (495) 781-06-75 	 golovnev@leek-lamp.ru	логист
89	ZENCHA	jrteOl270Hx8gS75	3f29aa72de0eae505348105042f4179f	SUPPLIER_MANAGER	АО "Электротехнический Завод  "ЗЕНЧА-Псков"	+7 (8112) 72 74 75	m.kiseleva@pzlt.ru	логист
95	PROK	jrteOl270Hx8gS75	3f29aa72de0eae505348105042f4179f	SUPPLIER_MANAGER	АО "Капитал-ПРОК"	 8(495) 745 67 87  доб. 543, 542, 541	1mfp@prok.ru	логист
90	CIFR	jrteOl270Hx8gS75	3f29aa72de0eae505348105042f4179f	SUPPLIER_MANAGER	ООО «Цифромаркет»	+7-495-662-32-52	mvershkova@tmtc.ru	логист
129	URALPAK	jrteOl270Hx8gS75	05767a5e335951177ebd76c9295c7a3e	SUPPLIER_MANAGER	 Уралпак 	 +7-912-035-08-89	albert-shajkhutdinov@ya.ru	логист
92	RUBIKON	jrteOl270Hx8gS75	3f29aa72de0eae505348105042f4179f	SUPPLIER_MANAGER	ООО "Рубикон"	89055374441	amir1968@mail.ru	логист
98	VIPM	jrteOl270Hx8gS75	3f29aa72de0eae505348105042f4179f	SUPPLIER_MANAGER	ООО "ВИП-Маркет"	+7 (343) 345-79 -69 (31-75)	altman_a@s3.ru	логист
96	PLAST	jrteOl270Hx8gS75	3f29aa72de0eae505348105042f4179f	SUPPLIER_MANAGER	ООО "Пластик Репаблик"	+7 495 933-00-77 доб. 144	a.vasileva@9330077.ru	логист
93	CHEM	jrteOl270Hx8gS75	3f29aa72de0eae505348105042f4179f	SUPPLIER_MANAGER	ООО Торговая Компания "Химик"	8(812) 325-72-18 доб. 226	manager2@himik.ru	логист
568	ud10	jrteOl270Hx8gS75	a6934705837218430b1a3f00eaaccaf5	WH_DISPATCHER	Диспетчер	отсутствует	отсутствует	dispatcher
569	uo10	jrteOl270Hx8gS75	a6934705837218430b1a3f00eaaccaf5	WH_SECURITY_OFFICER	Охрана	отсутствует	отсутствует	officer
117	INTERTUL	jrteOl270Hx8gS75	7cb4d3a9cf15f8328d57ea4418137d2a	SUPPLIER_MANAGER	 Интертул 	 +7 913 728 07 78	kam1@akor.ru	логист
118	AKOR	jrteOl270Hx8gS75	82f5f3515d9f41fabce675709dc5d429	SUPPLIER_MANAGER	 АКОР 	+7 (383) 227-68-60	grachev81@akor.ru	логист
119	AST-TR	jrteOl270Hx8gS75	aaaa68404e54cda2d097dbc84eee08bc	SUPPLIER_MANAGER	 АСТ-Трейд 	+7 812 337 25 08	irinaast1@mail.ru	логист
120	SKRAP	jrteOl270Hx8gS75	68a9928a56ed4249b5f6409dc7e4ad55	SUPPLIER_MANAGER	 СКРАП 	(812) 320-09-99 доб. 214	soboleva@skrap.spb.ru	логист
121	ELIS	jrteOl270Hx8gS75	9fc7c9c8301798f686d2a1d9c2f0c37b	SUPPLIER_MANAGER	 Элис 	+79052580953	vrudnitskiy@homequeen.ru	логист
122	EVROIMPORT	jrteOl270Hx8gS75	d56f463d35b8ba1679fb7e71f9fc8352	SUPPLIER_MANAGER	 Евроимпорт 	(812) 449-0-439, доб.222 	pin@rtp.sp.ru 	логист
124	TEZA-T	jrteOl270Hx8gS75	9550416a20dc9165c2b62c8112371e23	SUPPLIER_MANAGER	 Теза-Трейд 	8-925-157-49-67	olabzina@tezatrade.ru	логист
125	HAZAR	jrteOl270Hx8gS75	fb5c7ecea1f3bfbeebe993e726a7b72c	SUPPLIER_MANAGER	  Хазар 	 (812) 640-19-95, доп.206	s34@evatrade.ru	логист
126	MUL_TR	jrteOl270Hx8gS75	72c49a3a319ddd49104ff1f140d4421b	SUPPLIER_MANAGER	  Мультидом Трейдин 	+7(495)258-32-20	minjaeva@multidom.ru	логист
127	MAS_H	jrteOl270Hx8gS75	a391f53ff5ff7d07ad03005d63b6a8e5	SUPPLIER_MANAGER	 Мастер Хаус 	+7 904-633-84-93	ns@master.ru.com	логист
128	MUL_GR	jrteOl270Hx8gS75	7567f14d867a7fb995298bae52ca60e3	SUPPLIER_MANAGER	 Мультипласт Групп 	8 916-633-56-48	multiplast@multiplast.su	логист
131	T-CITY	jrteOl270Hx8gS75	b26afe2a8880e1ed0fe12523412fa7e0	SUPPLIER_MANAGER	 Текстиль-Сити	 8-921-888-13-55	scherbakov@ujut.ru	логист
123	VILINA	jrteOl270Hx8gS75	6c4aeba56efb79824e578aa85595edd1	SUPPLIER_MANAGER	 Вилина 	+7 (812) 406-70-01, 602-18-80  доб.238	vilina@vilina.ru 	логист
545	Energoprom	jrteOl270Hx8gS75	fce58a3b78d079b1fe7aacde8c1042aa	SUPPLIER_MANAGER	Энергопром 	83433111126	rp7@ekos2.russvet.ru	manager
571	Konvers	jrteOl270Hx8gS75	fce58a3b78d079b1fe7aacde8c1042aa	SUPPLIER_MANAGER	Поставка	89067901726	a.kiselev@konvers.net	manager
555	Osram	jrteOl270Hx8gS75	52bac0790e13d28be3e9d24e10d7e45f	SUPPLIER_MANAGER	Осрам	 8 909 004-6888 	 A.Mingalev1@osram.com	логист
556	ACD	jrteOl270Hx8gS75	4343c6823db2f44804e2d671bd971954	SUPPLIER_MANAGER	АСД-Москва	 8-495-974-71-94 внутр. 2049   	 MoiseevaTS@asd-electro.ru  	логист
572	ProfKlei	jrteOl270Hx8gS75	6eb7159e27ea08cee99d84e650de237f	SUPPLIER_MANAGER	Поставка Профклей	89255074544	kd-pk@mail.ru	manager
477	tkhimik	jrteOl270Hx8gS75	050506343fc3271469556edb8a4d36a6	SUPPLIER_MANAGER	ООО Торговая компания "Химик"	8-981-166-65-41	manager2@himik.ru	логист
546	schneider	jrteOl270Hx8gS75	fce58a3b78d079b1fe7aacde8c1042aa	SUPPLIER_MANAGER	Поставка Шнейдер	89877114757	grigorij.emelyanov@schneider-electric.com	manager
558	Triumf	jrteOl270Hx8gS75	d9a48063a8558530bedd9bb70c66e07f	SUPPLIER_MANAGER	Триумф	+7 (495) 380 02 49	 i.manoli@dogrular.ru   	логист
462	ECOPROM	jrteOl270Hx8gS75	42b8e1276ecd64c61290ffcd72bbf885	SUPPLIER_MANAGER	ООО "ТД Экопром "	8(495) 514-9331, 514-9342, доб.212	markova@ekoprom.org; kochergina@ekoprom.org	логист
464	DOHLOX	jrteOl270Hx8gS75	5a644ec9889b2b083f9cff24fb79dc2d	SUPPLIER_MANAGER	ООО "Дохлокс"	8 (903) 277-52-59	palilov73@mail.ru	логист
465	CITYLIGHT	jrteOl270Hx8gS75	d2dfb086dc417d3f7b1191a508805a41	SUPPLIER_MANAGER	ООО "Ситилайт"	8 (916)6028901	alex@optim.ws	логист
468	MIREX	jrteOl270Hx8gS75	71657657d774c97c30cd703f16bff973	SUPPLIER_MANAGER	АО "Компания "МИРЕКС"	+7(343) 374-37-04 доб.166	krivonosov@mirex.ru	логист
470	LED-TECHN	jrteOl270Hx8gS75	861f81e99e17be56c77d5e91facffc27	SUPPLIER_MANAGER	ООО "ЛЕД-ТЕХНОЛОГИИ"	8-920-955-33-53	fotocontrast@mail.ru	логист
472	F_U	jrteOl270Hx8gS75	4449ee9c70a0a6fc0ffbc404b72e27c9	SUPPLIER_MANAGER	ООО "Фабрика Уралтекстиль"	79222222860	utkin@fab-uraltex.ru	логист
559	himik	jrteOl270Hx8gS75	cf05eca61b434d03e34f763a0b53b348	SUPPLIER_MANAGER	ООО Торговая Компания "Химик"	8(812) 325-72-18 доб. 226	manager2@himik.ru	логист
492	ALBERTOTEX	jrteOl270Hx8gS75	8410dac91d66135a976a915282f82f68	SUPPLIER_MANAGER	ООО "Альбертотекс"	8-904-986-73-20	mogutina@list.ru	логист
494	MIREY	jrteOl270Hx8gS75	de1ec2c51ab6e86387b084392704e2ef	SUPPLIER_MANAGER	ООО "Мирей"	8-982-302-42-28	torg49@mirey-group.ru	логист
495	GOLDEN_PARK	jrteOl270Hx8gS75	dfb51f337ab8e97aabab633d301cdeec	SUPPLIER_MANAGER	ООО "Голд Пак Север" (г. Красноярск)	8-913-566-80-98	m.sannikova@goldpak-sever.ru	логист
496	UaS_U	jrteOl270Hx8gS75	1ce3db7bea5851c1a00bdeee16e5028d	SUPPLIER_MANAGER	ООО "Упаковка и Сервис - Урал"	8-912-676-48-20	kolobovaanna0@gmail.com	логист
498	CH_TECH	jrteOl270Hx8gS75	eb50f585b1179738ed6b8e4d9b3d1136	SUPPLIER_MANAGER	ООО "Чистые технологии" (Ревда)	8-932-121-80-98	info@krepak.ru	логист
499	KOMILFO	jrteOl270Hx8gS75	15f10c41a07054bcd1f42058212bd4df	SUPPLIER_MANAGER	ООО "Комильфо"	8-960-723-77-77	komilfo.textile@gmail.com	логист
501	ENERGY	jrteOl270Hx8gS75	96c6b9a1d885f7e82f538be800af8419	SUPPLIER_MANAGER	ООО "Чебоксарское УПП "Энергия"	(8352) 52-74-82	upp_vos@mail.ru	логист
503	PROMTORG	jrteOl270Hx8gS75	9c91eb487b90e74a674e65cb93e4b025	SUPPLIER_MANAGER	Промторг (Сигнал)	+7 (383) 206-28-71	ork3@signalelectronics.ru	логист
509	TDF	jrteOl270Hx8gS75	8559d6628437bf0d412590d5acc2b413	SUPPLIER_MANAGER	ТДФ	 8(812)4956880	Info@tdf.ru	логист
510	PROMGR	jrteOl270Hx8gS75	4eeadf70050841b6ea27a7af55176e4e	SUPPLIER_MANAGER	ООО СП Промгруппа	 8(812)449-90-20	btr@pgptrade.com	логист
511	DORI	jrteOl270Hx8gS75	28cc653efc66d1c3967c6c5b7b251c2b	SUPPLIER_MANAGER	Дори+	 8(495)5886688	zakaz@dori.ru	логист
512	NEVAPL	jrteOl270Hx8gS75	91a40ea137eaf9ac053caf2271f63d00	SUPPLIER_MANAGER	Невапластик	 8(495)7030335	rus.kom@mail.ru	логист
513	ELEKTRA	jrteOl270Hx8gS75	1571573c11caf968ab6dedd5c111e5d9	SUPPLIER_MANAGER	Электра	 8(495)698-98-75	oooelektra@rambler.ru	логист
514	NEVA-L	jrteOl270Hx8gS75	0c1b6d2db74bc7329101e007ada552ff	SUPPLIER_MANAGER	Нева-Лайт	 8(495)449-90-20	ira_s@pgptrade.com	логист
473	TEKKRON	jrteOl270Hx8gS75	6541878de296d667055bbd454b8e37a7	SUPPLIER_MANAGER	ООО "Теккрон"	8-922-122-89-10	geliopolis@bk.ru	логист
539	Germes	jrteOl270Hx8gS75	a7a807bfbdaa0cb869bb81815504cac4	SUPPLIER_MANAGER	Гермес-Трейд 	8(495)647-00-94 доб. 126	dav@germes-tk.ru	логист
114	CH_I	jrteOl270Hx8gS75	bded4c866a133a4e27da9dfebc9358a6	SUPPLIER_MANAGER	 Чайка Инвестопт 	7 (495) 933-12-45	a.vladimirova@plast-team.ru	логист
518	PROFESSIONAL	jrteOl270Hx8gS75	d225727b17fe0bc8b2891bda1bfee86f	SUPPLIER_MANAGER	 Профессионал 	8-9262298160	proffi78@yandex.ru	логист
517	Apollo	jrteOl270Hx8gS75	664ac9b12fbb55f0d2c3844007d7e213	SUPPLIER_MANAGER	Аполло ТД	8 812 378-53-20	m.saltanova@globalapollo.ru	логист
522	CONCEPT	jrteOl270Hx8gS75	ca5e989d336c23f377c9da3407b3aecd	SUPPLIER_MANAGER	Концепт Быт	84959749676	kam1@concept-b.ru	логист
536	Panasonic	jrteOl270Hx8gS75	a7a807bfbdaa0cb869bb81815504cac4	SUPPLIER_MANAGER	Панасоник (Московский склад)	8-495-665-42-39	Kseniya.Ulybysheva@ru.panasonic.com	логист
529	OptimaM1	jrteOl270Hx8gS75	664ac9b12fbb55f0d2c3844007d7e213	SUPPLIER_MANAGER	 Оптима-М 	+7 926 647 16 43	ka@akor.ru 	логист
68	BIOSTAL	jrteOl270Hx8gS75	9d76880f75733170d27386393ccaf5df	SUPPLIER_MANAGER	Осоткин Виталий Анатольевич	000000000	skladv@biostal.ru	логист
526	TEST1	jrteOl270Hx8gS75	d6d622e3f245e8e992a9518db8b31657	SUPPLIER_MANAGER	Красноармейск	000000000	rusakovavm@logicsmart.ru	логист
523	CONCEPT2	jrteOl270Hx8gS75	30748bb83baf0d28f50c9084c08970b0	SUPPLIER_MANAGER	Концепт Быт	84959749676	kam1@concept-b.ru	логист
533	OptimaM	jrteOl270Hx8gS75	ec7b70b5a106b628fe4e3c18b8d0ca80	SUPPLIER_MANAGER	 Оптима-М 	+7 926 647 16 43	ka@akor.ru	логист
535	1TZ	jrteOl270Hx8gS75	440144f72c55742075e20a2e9d3c930e	SUPPLIER_MANAGER	Первый Термометровый Завод	8 343 268 97 35	ekaterinburg@1thermometr.ru	логист
548	kosmos	jrteOl270Hx8gS75	fce58a3b78d079b1fe7aacde8c1042aa	SUPPLIER_MANAGER	Топсервис	89052232457	kosmos_spb@bk.ru	manager
560	simon	jrteOl270Hx8gS75	e3c608b9f3d447551f2dec66048911f7	SUPPLIER_MANAGER	Симон Электрик"	8495 646 84 00 (#128) 	stupenkova@simonelectric.ru	логист
534	Energizer	jrteOl270Hx8gS75	a7a807bfbdaa0cb869bb81815504cac4	SUPPLIER_MANAGER	Поставка Energizer	8-982-668-57-44	andrey.tebenev@energizer.com	логист
11	ET1	jrteOl270Hx8gS75	e9d1364f7b71b0cb64435cf580db7016	SUPPLIER_MANAGER	Энергосистемы	 8(495) 780-60-18	vkuznetsov@e-s-t.ru, asinsov@e-s-t.ru, ngoryachev@e-s-t.ru	логист
537	Tplast	jrteOl270Hx8gS75	a7a807bfbdaa0cb869bb81815504cac4	SUPPLIER_MANAGER	ЕвроПрофиль  (T-plast)	8(495)308-82-14,  доб.271	tatarchuk@tplast.org	логист
527	aleshina_e	jrteOl270Hx8gS75	713c05b1326d22594cf406a6ac77ff21	SUPPLIER_MANAGER	Красноармейск	000000000	rusakovavm@logicsmart.ru	логист
524	APOLLO	jrteOl270Hx8gS75	34ddf90f3d9c84a0ed51252b82b402b9	SUPPLIER_MANAGER	Аполло ТД	8 812 378-53-20	m.saltanova@globalapollo.ru	логист
528	kochenkov_a	jrteOl270Hx8gS75	dad2e021e09a8ca3aced435988fdea31	SUPPLIER_MANAGER	Красноармейск	000000000	rusakovavm@logicsmart.ru	логист
538	Kalashnikov	jrteOl270Hx8gS75	a7a807bfbdaa0cb869bb81815504cac4	SUPPLIER_MANAGER	Калашников ТД	+7 (48261) 3-41-00, 3-31-41 	alexrus007@bk.ru 	логист
\.


--
-- TOC entry 2330 (class 0 OID 0)
-- Dependencies: 183
-- Name: users_userid_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('users_userid_seq', 572, true);


--
-- TOC entry 2268 (class 0 OID 20973)
-- Dependencies: 193
-- Data for Name: warehouse_users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY warehouse_users (userid, warehouseid) FROM stdin;
25	3
32	3
17	3
26	4
33	4
18	4
27	5
34	5
19	5
24	6
31	6
21	6
30	7
16	7
28	8
35	8
29	9
36	9
20	9
22	8
23	7
37	5
38	5
39	5
40	3
41	3
42	3
43	3
44	3
45	3
46	3
47	4
48	4
49	4
50	4
51	8
52	8
97	3
550	7
567	10
568	10
569	10
\.


--
-- TOC entry 2263 (class 0 OID 20931)
-- Dependencies: 188
-- Data for Name: warehouses; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY warehouses (warehouseid, warehousename, rustimezoneabbr, region, district, locality, mailindex, address, email, phonenumber, responsiblepersonid) FROM stdin;
6	Санкт-Петербург(Киришская)	MSK	\N	\N	\N	\N	\N	\N	\N	\N
3	Красноармейск	MSK	\N	\N	\N	\N	\N	\N	\N	\N
4	Ульяновск	MSK	\N	\N	\N	\N	\N	\N	\N	\N
5	Краснодар	MSK	\N	\N	\N	\N	\N	\N	\N	\N
7	Ревда	YEKT	\N	\N	\N	\N	\N	\N	\N	\N
8	Иркутск	IRKT	\N	\N	\N	\N	\N	\N	\N	\N
9	Новосибирск	KRAT	\N	\N	\N	\N	\N	\N	\N	\N
10	Санкт-Петербург(Всеволожск)	MSK	\N	\N	\N	\N	\N	\N	\N	\N
\.


--
-- TOC entry 2331 (class 0 OID 0)
-- Dependencies: 187
-- Name: warehouses_warehouseid_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('warehouses_warehouseid_seq', 10, true);


SET search_path = audit, pg_catalog;

--
-- TOC entry 2121 (class 2606 OID 21069)
-- Name: doc_periods_audit_pkey; Type: CONSTRAINT; Schema: audit; Owner: postgres
--

ALTER TABLE ONLY doc_periods_audit
    ADD CONSTRAINT doc_periods_audit_pkey PRIMARY KEY (docperiodsauditid);


--
-- TOC entry 2123 (class 2606 OID 21082)
-- Name: donut_doc_periods_audit_pkey; Type: CONSTRAINT; Schema: audit; Owner: postgres
--

ALTER TABLE ONLY donut_doc_periods_audit
    ADD CONSTRAINT donut_doc_periods_audit_pkey PRIMARY KEY (donutdocperiodsauditid);


--
-- TOC entry 2125 (class 2606 OID 21098)
-- Name: orders_audit_pkey; Type: CONSTRAINT; Schema: audit; Owner: postgres
--

ALTER TABLE ONLY orders_audit
    ADD CONSTRAINT orders_audit_pkey PRIMARY KEY (ordersauditid);


SET search_path = public, pg_catalog;

--
-- TOC entry 2107 (class 2606 OID 20964)
-- Name: doc_periods_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY doc_periods
    ADD CONSTRAINT doc_periods_pkey PRIMARY KEY (docperiodid);


--
-- TOC entry 2105 (class 2606 OID 20948)
-- Name: docs_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY docs
    ADD CONSTRAINT docs_pkey PRIMARY KEY (docid);


--
-- TOC entry 2116 (class 2606 OID 21024)
-- Name: donut_doc_periods_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY donut_doc_periods
    ADD CONSTRAINT donut_doc_periods_pkey PRIMARY KEY (donutdocperiodid);


--
-- TOC entry 2119 (class 2606 OID 21050)
-- Name: orders_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY orders
    ADD CONSTRAINT orders_pkey PRIMARY KEY (orderid);


--
-- TOC entry 2101 (class 2606 OID 20917)
-- Name: permissions_for_roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY permissions_for_roles
    ADD CONSTRAINT permissions_for_roles_pkey PRIMARY KEY (userroleid, permissionid);


--
-- TOC entry 2099 (class 2606 OID 20912)
-- Name: permissions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY permissions
    ADD CONSTRAINT permissions_pkey PRIMARY KEY (permissionid);


--
-- TOC entry 2114 (class 2606 OID 21003)
-- Name: supplier_users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY supplier_users
    ADD CONSTRAINT supplier_users_pkey PRIMARY KEY (userid);


--
-- TOC entry 2112 (class 2606 OID 20998)
-- Name: suppliers_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY suppliers
    ADD CONSTRAINT suppliers_pkey PRIMARY KEY (supplierid);


--
-- TOC entry 2093 (class 2606 OID 20889)
-- Name: user_roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY user_roles
    ADD CONSTRAINT user_roles_pkey PRIMARY KEY (userroleid);


--
-- TOC entry 2095 (class 2606 OID 20900)
-- Name: users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (userid);


--
-- TOC entry 2097 (class 2606 OID 20902)
-- Name: users_userlogin_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_userlogin_key UNIQUE (userlogin);


--
-- TOC entry 2110 (class 2606 OID 20977)
-- Name: warehouse_users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY warehouse_users
    ADD CONSTRAINT warehouse_users_pkey PRIMARY KEY (userid);


--
-- TOC entry 2103 (class 2606 OID 20940)
-- Name: warehouses_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY warehouses
    ADD CONSTRAINT warehouses_pkey PRIMARY KEY (warehouseid);


--
-- TOC entry 2117 (class 1259 OID 21035)
-- Name: idx_last_mod; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_last_mod ON donut_doc_periods USING btree (lastmodified);


--
-- TOC entry 2108 (class 1259 OID 20972)
-- Name: idx_time_limits_inversed; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_time_limits_inversed ON doc_periods USING btree (docid, periodbegin, periodend DESC);


--
-- TOC entry 2140 (class 2620 OID 21071)
-- Name: doc_periods_audit; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER doc_periods_audit AFTER INSERT OR DELETE OR UPDATE ON doc_periods FOR EACH ROW EXECUTE PROCEDURE audit.process_doc_periods_audit();


--
-- TOC entry 2139 (class 2620 OID 20971)
-- Name: doc_periods_no_intersections; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER doc_periods_no_intersections BEFORE INSERT OR UPDATE ON doc_periods FOR EACH ROW EXECUTE PROCEDURE doc_periods_no_intersections();


--
-- TOC entry 2141 (class 2620 OID 21084)
-- Name: donut_doc_periods_audit; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER donut_doc_periods_audit AFTER INSERT OR DELETE OR UPDATE ON donut_doc_periods FOR EACH ROW EXECUTE PROCEDURE audit.process_donut_doc_periods_audit();


--
-- TOC entry 2142 (class 2620 OID 21100)
-- Name: orders_audit; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER orders_audit AFTER INSERT OR DELETE OR UPDATE ON orders FOR EACH ROW EXECUTE PROCEDURE audit.process_orders_audit();


--
-- TOC entry 2130 (class 2606 OID 20965)
-- Name: doc_periods_docid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY doc_periods
    ADD CONSTRAINT doc_periods_docid_fkey FOREIGN KEY (docid) REFERENCES docs(docid);


--
-- TOC entry 2129 (class 2606 OID 20949)
-- Name: docs_warehouseid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY docs
    ADD CONSTRAINT docs_warehouseid_fkey FOREIGN KEY (warehouseid) REFERENCES warehouses(warehouseid);


--
-- TOC entry 2135 (class 2606 OID 21025)
-- Name: donut_doc_periods_donutdocperiodid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY donut_doc_periods
    ADD CONSTRAINT donut_doc_periods_donutdocperiodid_fkey FOREIGN KEY (donutdocperiodid) REFERENCES doc_periods(docperiodid);


--
-- TOC entry 2136 (class 2606 OID 21030)
-- Name: donut_doc_periods_supplieruserid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY donut_doc_periods
    ADD CONSTRAINT donut_doc_periods_supplieruserid_fkey FOREIGN KEY (supplieruserid) REFERENCES supplier_users(userid);


--
-- TOC entry 2138 (class 2606 OID 21056)
-- Name: orders_donutdocperiodid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY orders
    ADD CONSTRAINT orders_donutdocperiodid_fkey FOREIGN KEY (donutdocperiodid) REFERENCES donut_doc_periods(donutdocperiodid);


--
-- TOC entry 2137 (class 2606 OID 21051)
-- Name: orders_finaldestinationwarehouseid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY orders
    ADD CONSTRAINT orders_finaldestinationwarehouseid_fkey FOREIGN KEY (finaldestinationwarehouseid) REFERENCES warehouses(warehouseid);


--
-- TOC entry 2127 (class 2606 OID 20918)
-- Name: permissions_for_roles_permissionid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY permissions_for_roles
    ADD CONSTRAINT permissions_for_roles_permissionid_fkey FOREIGN KEY (permissionid) REFERENCES permissions(permissionid) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 2128 (class 2606 OID 20923)
-- Name: permissions_for_roles_userroleid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY permissions_for_roles
    ADD CONSTRAINT permissions_for_roles_userroleid_fkey FOREIGN KEY (userroleid) REFERENCES user_roles(userroleid) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 2134 (class 2606 OID 21009)
-- Name: supplier_users_supplierid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY supplier_users
    ADD CONSTRAINT supplier_users_supplierid_fkey FOREIGN KEY (supplierid) REFERENCES suppliers(supplierid) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 2133 (class 2606 OID 21004)
-- Name: supplier_users_userid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY supplier_users
    ADD CONSTRAINT supplier_users_userid_fkey FOREIGN KEY (userid) REFERENCES users(userid) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2126 (class 2606 OID 20903)
-- Name: users_userroleid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_userroleid_fkey FOREIGN KEY (userroleid) REFERENCES user_roles(userroleid);


--
-- TOC entry 2131 (class 2606 OID 20978)
-- Name: warehouse_users_userid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY warehouse_users
    ADD CONSTRAINT warehouse_users_userid_fkey FOREIGN KEY (userid) REFERENCES users(userid) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2132 (class 2606 OID 20983)
-- Name: warehouse_users_warehouseid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY warehouse_users
    ADD CONSTRAINT warehouse_users_warehouseid_fkey FOREIGN KEY (warehouseid) REFERENCES warehouses(warehouseid) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 2287 (class 0 OID 0)
-- Dependencies: 6
-- Name: audit; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA audit FROM PUBLIC;
REVOKE ALL ON SCHEMA audit FROM postgres;
GRANT ALL ON SCHEMA audit TO postgres;
GRANT USAGE ON SCHEMA audit TO app_user;
GRANT USAGE ON SCHEMA audit TO admin_user;


--
-- TOC entry 2288 (class 0 OID 0)
-- Dependencies: 5
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT USAGE ON SCHEMA public TO app_user;
GRANT USAGE ON SCHEMA public TO admin_user;


SET search_path = audit, pg_catalog;

--
-- TOC entry 2290 (class 0 OID 0)
-- Dependencies: 201
-- Name: doc_periods_audit; Type: ACL; Schema: audit; Owner: postgres
--

REVOKE ALL ON TABLE doc_periods_audit FROM PUBLIC;
REVOKE ALL ON TABLE doc_periods_audit FROM postgres;
GRANT ALL ON TABLE doc_periods_audit TO postgres;
GRANT INSERT ON TABLE doc_periods_audit TO app_user;
GRANT SELECT ON TABLE doc_periods_audit TO admin_user;


--
-- TOC entry 2292 (class 0 OID 0)
-- Dependencies: 200
-- Name: doc_periods_audit_docperiodsauditid_seq; Type: ACL; Schema: audit; Owner: postgres
--

REVOKE ALL ON SEQUENCE doc_periods_audit_docperiodsauditid_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE doc_periods_audit_docperiodsauditid_seq FROM postgres;
GRANT ALL ON SEQUENCE doc_periods_audit_docperiodsauditid_seq TO postgres;
GRANT ALL ON SEQUENCE doc_periods_audit_docperiodsauditid_seq TO app_user;


--
-- TOC entry 2293 (class 0 OID 0)
-- Dependencies: 203
-- Name: donut_doc_periods_audit; Type: ACL; Schema: audit; Owner: postgres
--

REVOKE ALL ON TABLE donut_doc_periods_audit FROM PUBLIC;
REVOKE ALL ON TABLE donut_doc_periods_audit FROM postgres;
GRANT ALL ON TABLE donut_doc_periods_audit TO postgres;
GRANT INSERT ON TABLE donut_doc_periods_audit TO app_user;
GRANT SELECT ON TABLE donut_doc_periods_audit TO admin_user;


--
-- TOC entry 2295 (class 0 OID 0)
-- Dependencies: 202
-- Name: donut_doc_periods_audit_donutdocperiodsauditid_seq; Type: ACL; Schema: audit; Owner: postgres
--

REVOKE ALL ON SEQUENCE donut_doc_periods_audit_donutdocperiodsauditid_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE donut_doc_periods_audit_donutdocperiodsauditid_seq FROM postgres;
GRANT ALL ON SEQUENCE donut_doc_periods_audit_donutdocperiodsauditid_seq TO postgres;
GRANT ALL ON SEQUENCE donut_doc_periods_audit_donutdocperiodsauditid_seq TO app_user;


--
-- TOC entry 2296 (class 0 OID 0)
-- Dependencies: 205
-- Name: orders_audit; Type: ACL; Schema: audit; Owner: postgres
--

REVOKE ALL ON TABLE orders_audit FROM PUBLIC;
REVOKE ALL ON TABLE orders_audit FROM postgres;
GRANT ALL ON TABLE orders_audit TO postgres;
GRANT INSERT ON TABLE orders_audit TO app_user;
GRANT SELECT ON TABLE orders_audit TO admin_user;


--
-- TOC entry 2298 (class 0 OID 0)
-- Dependencies: 204
-- Name: orders_audit_ordersauditid_seq; Type: ACL; Schema: audit; Owner: postgres
--

REVOKE ALL ON SEQUENCE orders_audit_ordersauditid_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE orders_audit_ordersauditid_seq FROM postgres;
GRANT ALL ON SEQUENCE orders_audit_ordersauditid_seq TO postgres;
GRANT ALL ON SEQUENCE orders_audit_ordersauditid_seq TO app_user;


SET search_path = public, pg_catalog;

--
-- TOC entry 2299 (class 0 OID 0)
-- Dependencies: 192
-- Name: doc_periods; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE doc_periods FROM PUBLIC;
REVOKE ALL ON TABLE doc_periods FROM postgres;
GRANT ALL ON TABLE doc_periods TO postgres;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE doc_periods TO app_user;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE doc_periods TO admin_user;


--
-- TOC entry 2301 (class 0 OID 0)
-- Dependencies: 191
-- Name: doc_periods_docperiodid_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE doc_periods_docperiodid_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE doc_periods_docperiodid_seq FROM postgres;
GRANT ALL ON SEQUENCE doc_periods_docperiodid_seq TO postgres;
GRANT ALL ON SEQUENCE doc_periods_docperiodid_seq TO app_user;
GRANT ALL ON SEQUENCE doc_periods_docperiodid_seq TO admin_user;


--
-- TOC entry 2302 (class 0 OID 0)
-- Dependencies: 190
-- Name: docs; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE docs FROM PUBLIC;
REVOKE ALL ON TABLE docs FROM postgres;
GRANT ALL ON TABLE docs TO postgres;
GRANT SELECT ON TABLE docs TO app_user;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE docs TO admin_user;


--
-- TOC entry 2304 (class 0 OID 0)
-- Dependencies: 189
-- Name: docs_docid_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE docs_docid_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE docs_docid_seq FROM postgres;
GRANT ALL ON SEQUENCE docs_docid_seq TO postgres;
GRANT ALL ON SEQUENCE docs_docid_seq TO admin_user;


--
-- TOC entry 2305 (class 0 OID 0)
-- Dependencies: 197
-- Name: donut_doc_periods; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE donut_doc_periods FROM PUBLIC;
REVOKE ALL ON TABLE donut_doc_periods FROM postgres;
GRANT ALL ON TABLE donut_doc_periods TO postgres;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE donut_doc_periods TO app_user;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE donut_doc_periods TO admin_user;


--
-- TOC entry 2306 (class 0 OID 0)
-- Dependencies: 199
-- Name: orders; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE orders FROM PUBLIC;
REVOKE ALL ON TABLE orders FROM postgres;
GRANT ALL ON TABLE orders TO postgres;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE orders TO app_user;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE orders TO admin_user;


--
-- TOC entry 2308 (class 0 OID 0)
-- Dependencies: 198
-- Name: orders_orderid_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE orders_orderid_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE orders_orderid_seq FROM postgres;
GRANT ALL ON SEQUENCE orders_orderid_seq TO postgres;
GRANT ALL ON SEQUENCE orders_orderid_seq TO app_user;
GRANT ALL ON SEQUENCE orders_orderid_seq TO admin_user;


--
-- TOC entry 2309 (class 0 OID 0)
-- Dependencies: 185
-- Name: permissions; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE permissions FROM PUBLIC;
REVOKE ALL ON TABLE permissions FROM postgres;
GRANT ALL ON TABLE permissions TO postgres;
GRANT SELECT ON TABLE permissions TO app_user;
GRANT SELECT ON TABLE permissions TO admin_user;


--
-- TOC entry 2310 (class 0 OID 0)
-- Dependencies: 186
-- Name: permissions_for_roles; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE permissions_for_roles FROM PUBLIC;
REVOKE ALL ON TABLE permissions_for_roles FROM postgres;
GRANT ALL ON TABLE permissions_for_roles TO postgres;
GRANT SELECT ON TABLE permissions_for_roles TO app_user;
GRANT SELECT ON TABLE permissions_for_roles TO admin_user;


--
-- TOC entry 2311 (class 0 OID 0)
-- Dependencies: 196
-- Name: supplier_users; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE supplier_users FROM PUBLIC;
REVOKE ALL ON TABLE supplier_users FROM postgres;
GRANT ALL ON TABLE supplier_users TO postgres;
GRANT SELECT ON TABLE supplier_users TO app_user;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE supplier_users TO admin_user;


--
-- TOC entry 2312 (class 0 OID 0)
-- Dependencies: 195
-- Name: suppliers; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE suppliers FROM PUBLIC;
REVOKE ALL ON TABLE suppliers FROM postgres;
GRANT ALL ON TABLE suppliers TO postgres;
GRANT SELECT ON TABLE suppliers TO app_user;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE suppliers TO admin_user;


--
-- TOC entry 2314 (class 0 OID 0)
-- Dependencies: 194
-- Name: suppliers_supplierid_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE suppliers_supplierid_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE suppliers_supplierid_seq FROM postgres;
GRANT ALL ON SEQUENCE suppliers_supplierid_seq TO postgres;
GRANT ALL ON SEQUENCE suppliers_supplierid_seq TO admin_user;


--
-- TOC entry 2315 (class 0 OID 0)
-- Dependencies: 182
-- Name: user_roles; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE user_roles FROM PUBLIC;
REVOKE ALL ON TABLE user_roles FROM postgres;
GRANT ALL ON TABLE user_roles TO postgres;
GRANT SELECT ON TABLE user_roles TO app_user;
GRANT SELECT ON TABLE user_roles TO admin_user;


--
-- TOC entry 2316 (class 0 OID 0)
-- Dependencies: 184
-- Name: users; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE users FROM PUBLIC;
REVOKE ALL ON TABLE users FROM postgres;
GRANT ALL ON TABLE users TO postgres;
GRANT SELECT ON TABLE users TO app_user;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE users TO admin_user;


--
-- TOC entry 2318 (class 0 OID 0)
-- Dependencies: 183
-- Name: users_userid_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE users_userid_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE users_userid_seq FROM postgres;
GRANT ALL ON SEQUENCE users_userid_seq TO postgres;
GRANT ALL ON SEQUENCE users_userid_seq TO admin_user;


--
-- TOC entry 2319 (class 0 OID 0)
-- Dependencies: 193
-- Name: warehouse_users; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE warehouse_users FROM PUBLIC;
REVOKE ALL ON TABLE warehouse_users FROM postgres;
GRANT ALL ON TABLE warehouse_users TO postgres;
GRANT SELECT ON TABLE warehouse_users TO app_user;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE warehouse_users TO admin_user;


--
-- TOC entry 2320 (class 0 OID 0)
-- Dependencies: 188
-- Name: warehouses; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE warehouses FROM PUBLIC;
REVOKE ALL ON TABLE warehouses FROM postgres;
GRANT ALL ON TABLE warehouses TO postgres;
GRANT SELECT ON TABLE warehouses TO app_user;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE warehouses TO admin_user;


--
-- TOC entry 2322 (class 0 OID 0)
-- Dependencies: 187
-- Name: warehouses_warehouseid_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE warehouses_warehouseid_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE warehouses_warehouseid_seq FROM postgres;
GRANT ALL ON SEQUENCE warehouses_warehouseid_seq TO postgres;
GRANT ALL ON SEQUENCE warehouses_warehouseid_seq TO admin_user;


-- Completed on 2016-11-03 20:03:33

--
-- PostgreSQL database dump complete
--

\connect template1

SET default_transaction_read_only = off;

--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.3
-- Dumped by pg_dump version 9.5.4

-- Started on 2016-11-03 20:03:33

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 2098 (class 1262 OID 1)
-- Dependencies: 2097
-- Name: template1; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON DATABASE template1 IS 'default template for new databases';


--
-- TOC entry 1 (class 3079 OID 12361)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2101 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


--
-- TOC entry 2100 (class 0 OID 0)
-- Dependencies: 6
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2016-11-03 20:03:35

--
-- PostgreSQL database dump complete
--

-- Completed on 2016-11-03 20:03:35

--
-- PostgreSQL database cluster dump complete
--

