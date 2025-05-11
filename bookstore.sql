--
-- PostgreSQL database dump
--

-- Dumped from database version 16.3
-- Dumped by pg_dump version 16.3

-- Started on 2025-05-11 22:57:24

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
-- TOC entry 215 (class 1259 OID 50637)
-- Name: addresses; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.addresses (
    id bigint NOT NULL,
    apartment character varying(255),
    city character varying(255),
    house character varying(255),
    street character varying(255),
    customer_id bigint
);


ALTER TABLE public.addresses OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 50642)
-- Name: addresses_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.addresses_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.addresses_id_seq OWNER TO postgres;

--
-- TOC entry 4949 (class 0 OID 0)
-- Dependencies: 216
-- Name: addresses_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.addresses_id_seq OWNED BY public.addresses.id;


--
-- TOC entry 217 (class 1259 OID 50644)
-- Name: bonus_cards; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.bonus_cards (
    id bigint NOT NULL,
    card_number character varying(10) NOT NULL,
    points integer NOT NULL,
    customer_id bigint NOT NULL
);


ALTER TABLE public.bonus_cards OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 50647)
-- Name: bonus_cards_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.bonus_cards_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.bonus_cards_id_seq OWNER TO postgres;

--
-- TOC entry 4950 (class 0 OID 0)
-- Dependencies: 218
-- Name: bonus_cards_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.bonus_cards_id_seq OWNED BY public.bonus_cards.id;


--
-- TOC entry 219 (class 1259 OID 50649)
-- Name: book_warehouse; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book_warehouse (
    id bigint NOT NULL,
    stock integer NOT NULL,
    book_id bigint NOT NULL,
    warehouse_id bigint NOT NULL
);


ALTER TABLE public.book_warehouse OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 50652)
-- Name: book_warehouse_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.book_warehouse_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.book_warehouse_id_seq OWNER TO postgres;

--
-- TOC entry 4951 (class 0 OID 0)
-- Dependencies: 220
-- Name: book_warehouse_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.book_warehouse_id_seq OWNED BY public.book_warehouse.id;


--
-- TOC entry 221 (class 1259 OID 50654)
-- Name: books; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.books (
    id bigint NOT NULL,
    author character varying(255),
    description character varying(255),
    isbn character varying(255),
    price numeric(38,2),
    publication_year integer NOT NULL,
    publisher character varying(255),
    stock integer NOT NULL,
    title character varying(255),
    category_id bigint,
    category character varying(255),
    cover_image character varying(255),
    discount_percentage numeric(38,2) DEFAULT NULL::numeric
);


ALTER TABLE public.books OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 50659)
-- Name: books_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.books_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.books_id_seq OWNER TO postgres;

--
-- TOC entry 4952 (class 0 OID 0)
-- Dependencies: 222
-- Name: books_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.books_id_seq OWNED BY public.books.id;


--
-- TOC entry 223 (class 1259 OID 50661)
-- Name: customer_roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.customer_roles (
    customer_id bigint NOT NULL,
    role_id bigint NOT NULL
);


ALTER TABLE public.customer_roles OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 50664)
-- Name: customers; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.customers (
    id bigint NOT NULL,
    email character varying(255),
    full_name character varying(255),
    login character varying(255),
    password character varying(255),
    balance numeric(38,2) DEFAULT 0.00 NOT NULL
);


ALTER TABLE public.customers OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 50670)
-- Name: customers_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.customers_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.customers_id_seq OWNER TO postgres;

--
-- TOC entry 4953 (class 0 OID 0)
-- Dependencies: 225
-- Name: customers_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.customers_id_seq OWNED BY public.customers.id;


--
-- TOC entry 234 (class 1259 OID 50767)
-- Name: discounts; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.discounts (
    id bigint NOT NULL,
    book_id bigint NOT NULL,
    discount_percentage numeric(38,2) NOT NULL,
    start_date timestamp without time zone NOT NULL,
    end_date timestamp without time zone NOT NULL,
    active boolean DEFAULT true NOT NULL
);


ALTER TABLE public.discounts OWNER TO postgres;

--
-- TOC entry 233 (class 1259 OID 50766)
-- Name: discounts_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.discounts_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.discounts_id_seq OWNER TO postgres;

--
-- TOC entry 4954 (class 0 OID 0)
-- Dependencies: 233
-- Name: discounts_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.discounts_id_seq OWNED BY public.discounts.id;


--
-- TOC entry 226 (class 1259 OID 50672)
-- Name: order_items; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.order_items (
    price_at_purchase numeric(38,2),
    quantity integer,
    order_id bigint NOT NULL,
    book_id bigint NOT NULL
);


ALTER TABLE public.order_items OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 50675)
-- Name: orders; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.orders (
    id bigint NOT NULL,
    order_date timestamp(6) without time zone,
    payment_status character varying(255),
    payment_type character varying(255),
    status character varying(255),
    total numeric(38,2),
    customer_id bigint,
    delivery_address_id bigint,
    "orderNumber" bigint,
    order_number character varying(255)
);


ALTER TABLE public.orders OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 50680)
-- Name: orders_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.orders_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.orders_id_seq OWNER TO postgres;

--
-- TOC entry 4955 (class 0 OID 0)
-- Dependencies: 228
-- Name: orders_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.orders_id_seq OWNED BY public.orders.id;


--
-- TOC entry 229 (class 1259 OID 50682)
-- Name: roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.roles (
    id bigint NOT NULL,
    name character varying(255)
);


ALTER TABLE public.roles OWNER TO postgres;

--
-- TOC entry 230 (class 1259 OID 50685)
-- Name: roles_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.roles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.roles_id_seq OWNER TO postgres;

--
-- TOC entry 4956 (class 0 OID 0)
-- Dependencies: 230
-- Name: roles_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.roles_id_seq OWNED BY public.roles.id;


--
-- TOC entry 231 (class 1259 OID 50687)
-- Name: warehouses; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.warehouses (
    id bigint NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    name character varying(255) NOT NULL,
    city character varying(255),
    street character varying(255),
    house_number character varying(255)
);


ALTER TABLE public.warehouses OWNER TO postgres;

--
-- TOC entry 232 (class 1259 OID 50692)
-- Name: warehouses_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.warehouses_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.warehouses_id_seq OWNER TO postgres;

--
-- TOC entry 4957 (class 0 OID 0)
-- Dependencies: 232
-- Name: warehouses_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.warehouses_id_seq OWNED BY public.warehouses.id;


--
-- TOC entry 4736 (class 2604 OID 50643)
-- Name: addresses id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.addresses ALTER COLUMN id SET DEFAULT nextval('public.addresses_id_seq'::regclass);


--
-- TOC entry 4737 (class 2604 OID 50648)
-- Name: bonus_cards id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bonus_cards ALTER COLUMN id SET DEFAULT nextval('public.bonus_cards_id_seq'::regclass);


--
-- TOC entry 4738 (class 2604 OID 50653)
-- Name: book_warehouse id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_warehouse ALTER COLUMN id SET DEFAULT nextval('public.book_warehouse_id_seq'::regclass);


--
-- TOC entry 4739 (class 2604 OID 50660)
-- Name: books id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.books ALTER COLUMN id SET DEFAULT nextval('public.books_id_seq'::regclass);


--
-- TOC entry 4741 (class 2604 OID 50671)
-- Name: customers id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customers ALTER COLUMN id SET DEFAULT nextval('public.customers_id_seq'::regclass);


--
-- TOC entry 4746 (class 2604 OID 50770)
-- Name: discounts id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.discounts ALTER COLUMN id SET DEFAULT nextval('public.discounts_id_seq'::regclass);


--
-- TOC entry 4743 (class 2604 OID 50681)
-- Name: orders id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders ALTER COLUMN id SET DEFAULT nextval('public.orders_id_seq'::regclass);


--
-- TOC entry 4744 (class 2604 OID 50686)
-- Name: roles id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles ALTER COLUMN id SET DEFAULT nextval('public.roles_id_seq'::regclass);


--
-- TOC entry 4745 (class 2604 OID 50693)
-- Name: warehouses id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.warehouses ALTER COLUMN id SET DEFAULT nextval('public.warehouses_id_seq'::regclass);


--
-- TOC entry 4924 (class 0 OID 50637)
-- Dependencies: 215
-- Data for Name: addresses; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.addresses VALUES (6, '32', 'Тольятти', '2Б', 'Офицерская', 2);
INSERT INTO public.addresses VALUES (7, '309', 'Самара', '5', 'Революционная', 2);
INSERT INTO public.addresses VALUES (9, '32', 'Тольятти', '2Б', 'Офицерская', 3);
INSERT INTO public.addresses VALUES (10, '309', 'Самара', '5', 'Революционная', 9);
INSERT INTO public.addresses VALUES (11, NULL, 'Москва', '14', 'Ленина', 10);
INSERT INTO public.addresses VALUES (12, '', 'Санкт-Питербург', '5', 'Ленина', 2);


--
-- TOC entry 4926 (class 0 OID 50644)
-- Dependencies: 217
-- Data for Name: bonus_cards; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.bonus_cards VALUES (3, '0395931356', 34, 3);
INSERT INTO public.bonus_cards VALUES (5, '0415249394', 10, 10);
INSERT INTO public.bonus_cards VALUES (2, '0395931375', 23, 2);
INSERT INTO public.bonus_cards VALUES (4, '0845649412', 44, 9);


--
-- TOC entry 4928 (class 0 OID 50649)
-- Dependencies: 219
-- Data for Name: book_warehouse; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.book_warehouse VALUES (1, 14, 13, 3);


--
-- TOC entry 4930 (class 0 OID 50654)
-- Dependencies: 221
-- Data for Name: books; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.books VALUES (12, 'Илья Ильф и Евгений Петров', 'Юмористический роман о приключениях Остапа Бендера в поисках сокровищ, спрятанных в одном из двенадцати стульев.', '9785267003456', 450.00, 1928, 'Земля и фабрика', 25, 'Двенадцать стульев', NULL, 'Сатира', '/uploads/covers/78c06202-b7dc-47bd-803c-abff52df1882_12_chairs.jpg', NULL);
INSERT INTO public.books VALUES (4, 'F. Scott Fitzgerald', 'История о загадочном миллионере Джее Гэтсби и его любви к прекрасной Дейзи Бьюкенен на фоне роскоши 1920-х годов в Америке.', '9780743273565', 500.00, 1925, 'Scribner', 29, 'The Great Gatsby', NULL, 'Классика', '/uploads/covers/851dc3ac-83ff-4622-869c-baac23cbc0a9_The_Great_Gatsby.jpg', NULL);
INSERT INTO public.books VALUES (1, 'Лев Толстой', 'Эпический роман о судьбах людей на фоне Наполеоновских войн.', '9785353003083', 1200.00, 2009, 'Азбука', 10, 'Война и мир', 1, 'Роман', '/uploads/covers/6532e7bb-c124-4036-a48a-7c229a5ff512_WarAndPeace.jpg', NULL);
INSERT INTO public.books VALUES (5, 'Harper Lee', 'Роман о расовой несправедливости и взрослении в небольшом американском городке через глаза юной Скаут Финч.', '9780446310789', 600.00, 1960, 'J.B. Lippincott & Co.', 25, 'To Kill a Mockingbird', NULL, 'Классика', '/uploads/covers/7e08efa3-831e-445a-93f9-842a6d82c4bf_250px-To_Kill_a_Mockingbird_(first_edition_cover).jpg', NULL);
INSERT INTO public.books VALUES (7, 'Jane Austen', 'История Элизабет Беннет и мистера Дарси, полная гордости, предубеждений и любви.', '9780141439518', 400.00, 1813, 'T. Egerton', 35, 'Pride and Prejudice', NULL, 'Романтика', '/uploads/covers/49f5088b-345f-47e5-9c05-08c7c32fc2f4_PrideAndPrejudice.jpg', NULL);
INSERT INTO public.books VALUES (8, 'J.R.R. Tolkien', 'Приключения Бильбо Бэггинса, который отправляется в путешествие с гномами, чтобы вернуть их сокровища, охраняемые драконом Смаугом.', '9780547928227', 700.00, 1937, 'George Allen & Unwin', 20, 'The Hobbit', NULL, 'Фэнтези', '/uploads/covers/9ff17214-eca2-441e-8521-8c050ca988c2_TheHobbit_FirstEdition.jpg', NULL);
INSERT INTO public.books VALUES (9, 'Frank Herbert', 'Эпическая история о Поле Атрейдесе, который борется за контроль над пустынной планетой Арракис, единственным источником ценного ресурса — пряности.', '9780441172719', 800.00, 1965, 'Chilton Books', 15, 'Dune', NULL, 'Фантастика', '/uploads/covers/eee9f377-1810-4620-8012-f4eae30773fc_Dune-Frank_Herbert.jpg', NULL);
INSERT INTO public.books VALUES (14, 'Михаил Булгаков', 'Повесть о профессоре Преображенском, который превращает собаку в человека, но сталкивается с неожиданными последствиями.', '9785267005678', 350.00, 1987, 'Советский писатель', 15, 'Собачье сердце', NULL, 'Сатира', '/uploads/covers/9d5d2377-1869-445d-85eb-0d979cad4612_Собачье_сердце.jpg', NULL);
INSERT INTO public.books VALUES (10, 'Михаил Булгаков', 'Роман о любви, магии и борьбе добра со злом, где дьявол посещает Москву 1930-х годов, а Мастер и Маргарита проходят через испытания.', '9785267001235', 500.00, 1967, 'Москва', 19, 'Мастер и Маргарита', NULL, 'Классика', '/uploads/covers/f04a7a8c-97b0-4c42-95fd-ecf94f1ce390_The_Master_and_Margarita.jpg', NULL);
INSERT INTO public.books VALUES (11, 'Михаил Шолохов', 'Эпическая история казаков на Дону в период революции и Гражданской войны, за которую Шолохов получил Нобелевскую премию.', '9785267002345', 600.00, 1940, 'Гослитиздат', 13, 'Тихий Дон', NULL, 'Исторический роман', '/uploads/covers/b2d1f0d4-0471-4425-9bdc-90977e892ac8_Quiet_Flows_the_Don.jpg', NULL);
INSERT INTO public.books VALUES (13, 'Нечипоренко Олег Викторович', 'Эта книга честный рассказ об изнанке популярности, предательстве друзей и жизненных уроках. И пусть биография автора неоднозначна, его предприимчивость и талант привели музыканта на вершину российского хип-хопа.', '9785171392079', 966.00, 2024, 'АСТ', 15, 'Дежавю', NULL, 'Биография', '/uploads/covers/e528c7b1-6aaa-487f-b304-e28142768c31_Deja_vu.jpg', NULL);
INSERT INTO public.books VALUES (6, 'George Orwell', 'Классическая антиутопия о тоталитарном обществе, где Большой Брат следит за каждым шагом.', '9780451524935', 450.00, 1949, 'Secker & Warburg', 38, '1984', NULL, 'Антиутопия', '/uploads/covers/e9b8c14d-08ba-43dd-a950-6736b7462a44_1984_book.jpg', NULL);
INSERT INTO public.books VALUES (3, 'Стивен Кинг', 'Первая книга из цикла "Тёмная башня", рассказывающая о Роланде Дискейне, последнем стрелке, и его путешествии к загадочной Тёмной башне.', '9785170801442', 600.00, 1982, 'АСТ', 46, 'Тёмная башня: Стрелок', 2, 'Фантастика', '/uploads/covers/ed243126-e02a-47cd-8270-841067c15aee_DarkTower.jpg', NULL);


--
-- TOC entry 4932 (class 0 OID 50661)
-- Dependencies: 223
-- Data for Name: customer_roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.customer_roles VALUES (2, 2);
INSERT INTO public.customer_roles VALUES (3, 1);
INSERT INTO public.customer_roles VALUES (9, 1);
INSERT INTO public.customer_roles VALUES (10, 1);


--
-- TOC entry 4933 (class 0 OID 50664)
-- Dependencies: 224
-- Data for Name: customers; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.customers VALUES (3, 'Reyshow163@gmail.com', 'Владислав Калинин', 'LILKSBK123', '$2a$10$J35VqAjrZ/yXXv7v4.WqdeuPPf88uA0qhjFxeLgGIOE1Qi0hECE9a', 10502.00);
INSERT INTO public.customers VALUES (10, 'Spokoynich@yandex.ru', 'MAKSIM SPOKOYNICH', 'Spokoynich', '$2a$10$swKNOH8oKiuzBYVBO/NbHu9I8KU/OTg8U7JiEmLqbfy77Bg5FvQjy', 200.00);
INSERT INTO public.customers VALUES (2, 'testexample@example.com', 'test user', 'testuser', '$2a$10$QS5g1pJEk6vOxaaWweJomunR9uv.LgzcsSVMqwa9mVjbXPsACRDfO', 500.00);
INSERT INTO public.customers VALUES (9, 'Optional@optional.ru', 'Optional optional', 'Optional', '$2a$10$FkhJtIS61sz4Qo1p8Qu3Z.bp5SUpbOHCPTf.Is18OwUoncNxMToXe', 954.00);


--
-- TOC entry 4943 (class 0 OID 50767)
-- Dependencies: 234
-- Data for Name: discounts; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.discounts VALUES (2, 11, 20.00, '2025-05-11 10:44:00', '2025-05-20 16:00:00', true);
INSERT INTO public.discounts VALUES (3, 1, 50.00, '2025-05-11 13:33:00', '2025-05-20 16:00:00', true);


--
-- TOC entry 4935 (class 0 OID 50672)
-- Dependencies: 226
-- Data for Name: order_items; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.order_items VALUES (1200.00, 2, 4, 1);
INSERT INTO public.order_items VALUES (600.00, 1, 5, 3);
INSERT INTO public.order_items VALUES (966.00, 3, 10, 13);
INSERT INTO public.order_items VALUES (600.00, 1, 11, 3);
INSERT INTO public.order_items VALUES (500.00, 1, 12, 4);
INSERT INTO public.order_items VALUES (600.00, 1, 13, 11);
INSERT INTO public.order_items VALUES (600.00, 1, 13, 3);
INSERT INTO public.order_items VALUES (350.00, 1, 14, 14);
INSERT INTO public.order_items VALUES (450.00, 1, 15, 6);
INSERT INTO public.order_items VALUES (350.00, 1, 16, 14);
INSERT INTO public.order_items VALUES (350.00, 1, 17, 14);
INSERT INTO public.order_items VALUES (500.00, 1, 18, 10);
INSERT INTO public.order_items VALUES (480.00, 1, 19, 11);
INSERT INTO public.order_items VALUES (966.00, 1, 19, 13);
INSERT INTO public.order_items VALUES (450.00, 1, 19, 6);
INSERT INTO public.order_items VALUES (600.00, 1, 19, 3);


--
-- TOC entry 4936 (class 0 OID 50675)
-- Dependencies: 227
-- Data for Name: orders; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.orders VALUES (4, '2025-05-06 01:05:38.266738', 'pending', 'card', 'pending', 2400.00, 2, 6, NULL, NULL);
INSERT INTO public.orders VALUES (5, '2025-05-06 20:46:56.14641', 'pending', 'card', 'pending', 600.00, 2, 7, NULL, NULL);
INSERT INTO public.orders VALUES (10, '2025-05-08 19:28:39.903034', 'completed', 'balance', 'pending', 2898.00, 3, 9, NULL, NULL);
INSERT INTO public.orders VALUES (11, '2025-05-08 19:57:51.222746', 'completed', 'balance', 'pending', 600.00, 3, 9, NULL, NULL);
INSERT INTO public.orders VALUES (12, '2025-05-08 20:30:54.116871', 'completed', 'balance', 'pending', 500.00, 9, 10, NULL, NULL);
INSERT INTO public.orders VALUES (13, '2025-05-08 20:43:49.891891', 'completed', 'balance', 'pending', 1200.00, 9, 10, NULL, NULL);
INSERT INTO public.orders VALUES (14, '2025-05-08 20:45:55.289157', 'completed', 'balance', 'pending', 350.00, 9, 10, NULL, NULL);
INSERT INTO public.orders VALUES (15, '2025-05-08 20:47:26.990668', 'completed', 'balance', 'pending', 450.00, 10, 11, NULL, NULL);
INSERT INTO public.orders VALUES (16, '2025-05-08 20:50:49.088951', 'completed', 'balance', 'pending', 350.00, 10, 11, NULL, 'ORD-10-1');
INSERT INTO public.orders VALUES (17, '2025-05-08 20:52:13.377947', 'completed', 'balance', 'pending', 350.00, 10, 11, NULL, 'ORD-10-11');
INSERT INTO public.orders VALUES (18, '2025-05-10 21:59:56.974292', 'completed', 'balance', 'pending', 500.00, 2, 12, NULL, 'ORD-2-1');
INSERT INTO public.orders VALUES (19, '2025-05-11 21:14:32.068686', 'completed', 'balance', 'pending', 2496.00, 9, 10, NULL, 'ORD-9-1');


--
-- TOC entry 4938 (class 0 OID 50682)
-- Dependencies: 229
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.roles VALUES (1, 'USER');
INSERT INTO public.roles VALUES (2, 'ADMIN');


--
-- TOC entry 4940 (class 0 OID 50687)
-- Dependencies: 231
-- Data for Name: warehouses; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.warehouses VALUES (3, '2025-05-08 16:50:36.580085', 'Тёмный принц', 'Москва', 'Ленина', '15');


--
-- TOC entry 4958 (class 0 OID 0)
-- Dependencies: 216
-- Name: addresses_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.addresses_id_seq', 12, true);


--
-- TOC entry 4959 (class 0 OID 0)
-- Dependencies: 218
-- Name: bonus_cards_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.bonus_cards_id_seq', 5, true);


--
-- TOC entry 4960 (class 0 OID 0)
-- Dependencies: 220
-- Name: book_warehouse_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.book_warehouse_id_seq', 1, true);


--
-- TOC entry 4961 (class 0 OID 0)
-- Dependencies: 222
-- Name: books_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.books_id_seq', 14, true);


--
-- TOC entry 4962 (class 0 OID 0)
-- Dependencies: 225
-- Name: customers_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.customers_id_seq', 10, true);


--
-- TOC entry 4963 (class 0 OID 0)
-- Dependencies: 233
-- Name: discounts_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.discounts_id_seq', 3, true);


--
-- TOC entry 4964 (class 0 OID 0)
-- Dependencies: 228
-- Name: orders_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.orders_id_seq', 19, true);


--
-- TOC entry 4965 (class 0 OID 0)
-- Dependencies: 230
-- Name: roles_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.roles_id_seq', 1, true);


--
-- TOC entry 4966 (class 0 OID 0)
-- Dependencies: 232
-- Name: warehouses_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.warehouses_id_seq', 3, true);


--
-- TOC entry 4749 (class 2606 OID 50695)
-- Name: addresses addresses_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.addresses
    ADD CONSTRAINT addresses_pkey PRIMARY KEY (id);


--
-- TOC entry 4751 (class 2606 OID 50697)
-- Name: bonus_cards bonus_cards_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bonus_cards
    ADD CONSTRAINT bonus_cards_pkey PRIMARY KEY (id);


--
-- TOC entry 4755 (class 2606 OID 50701)
-- Name: book_warehouse book_warehouse_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_warehouse
    ADD CONSTRAINT book_warehouse_pkey PRIMARY KEY (id);


--
-- TOC entry 4757 (class 2606 OID 50703)
-- Name: books books_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.books
    ADD CONSTRAINT books_pkey PRIMARY KEY (id);


--
-- TOC entry 4759 (class 2606 OID 50705)
-- Name: customers customers_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customers
    ADD CONSTRAINT customers_pkey PRIMARY KEY (id);


--
-- TOC entry 4769 (class 2606 OID 50773)
-- Name: discounts discounts_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.discounts
    ADD CONSTRAINT discounts_pkey PRIMARY KEY (id);


--
-- TOC entry 4761 (class 2606 OID 50707)
-- Name: order_items order_items_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.order_items
    ADD CONSTRAINT order_items_pkey PRIMARY KEY (book_id, order_id);


--
-- TOC entry 4763 (class 2606 OID 50709)
-- Name: orders orders_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_pkey PRIMARY KEY (id);


--
-- TOC entry 4765 (class 2606 OID 50711)
-- Name: roles roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);


--
-- TOC entry 4753 (class 2606 OID 50699)
-- Name: bonus_cards ukclei6twht9pdk0mshq3mu76qh; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bonus_cards
    ADD CONSTRAINT ukclei6twht9pdk0mshq3mu76qh UNIQUE (card_number);


--
-- TOC entry 4767 (class 2606 OID 50713)
-- Name: warehouses warehouses_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.warehouses
    ADD CONSTRAINT warehouses_pkey PRIMARY KEY (id);


--
-- TOC entry 4780 (class 2606 OID 50774)
-- Name: discounts discounts_book_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.discounts
    ADD CONSTRAINT discounts_book_id_fkey FOREIGN KEY (book_id) REFERENCES public.books(id) ON DELETE CASCADE;


--
-- TOC entry 4778 (class 2606 OID 50754)
-- Name: orders fk3s2t83m5ddty3rgomn94d4ht6; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT fk3s2t83m5ddty3rgomn94d4ht6 FOREIGN KEY (delivery_address_id) REFERENCES public.addresses(id);


--
-- TOC entry 4772 (class 2606 OID 50724)
-- Name: book_warehouse fk8nb2rl0kjffpnmgug5qwl5sle; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_warehouse
    ADD CONSTRAINT fk8nb2rl0kjffpnmgug5qwl5sle FOREIGN KEY (book_id) REFERENCES public.books(id);


--
-- TOC entry 4771 (class 2606 OID 50719)
-- Name: bonus_cards fka0mmsg5bonlw0at4pxcc5dm15; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bonus_cards
    ADD CONSTRAINT fka0mmsg5bonlw0at4pxcc5dm15 FOREIGN KEY (customer_id) REFERENCES public.customers(id);


--
-- TOC entry 4776 (class 2606 OID 50744)
-- Name: order_items fkbioxgbv59vetrxe0ejfubep1w; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.order_items
    ADD CONSTRAINT fkbioxgbv59vetrxe0ejfubep1w FOREIGN KEY (order_id) REFERENCES public.orders(id);


--
-- TOC entry 4774 (class 2606 OID 50734)
-- Name: customer_roles fkgee5mori29s8ae8hwcl23qxf0; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customer_roles
    ADD CONSTRAINT fkgee5mori29s8ae8hwcl23qxf0 FOREIGN KEY (customer_id) REFERENCES public.customers(id);


--
-- TOC entry 4770 (class 2606 OID 50714)
-- Name: addresses fkhrpf5e8dwasvdc5cticysrt2k; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.addresses
    ADD CONSTRAINT fkhrpf5e8dwasvdc5cticysrt2k FOREIGN KEY (customer_id) REFERENCES public.customers(id);


--
-- TOC entry 4773 (class 2606 OID 50729)
-- Name: book_warehouse fkhv9jxcs8h3yra6ii5byhnfqbk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_warehouse
    ADD CONSTRAINT fkhv9jxcs8h3yra6ii5byhnfqbk FOREIGN KEY (warehouse_id) REFERENCES public.warehouses(id);


--
-- TOC entry 4777 (class 2606 OID 50749)
-- Name: order_items fki4ptndslo2pyfp9r1x0eulh9g; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.order_items
    ADD CONSTRAINT fki4ptndslo2pyfp9r1x0eulh9g FOREIGN KEY (book_id) REFERENCES public.books(id);


--
-- TOC entry 4775 (class 2606 OID 50739)
-- Name: customer_roles fknjtvg9npn1etke1ldeffa0ym6; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customer_roles
    ADD CONSTRAINT fknjtvg9npn1etke1ldeffa0ym6 FOREIGN KEY (role_id) REFERENCES public.roles(id);


--
-- TOC entry 4779 (class 2606 OID 50759)
-- Name: orders fkpxtb8awmi0dk6smoh2vp1litg; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT fkpxtb8awmi0dk6smoh2vp1litg FOREIGN KEY (customer_id) REFERENCES public.customers(id);


-- Completed on 2025-05-11 22:57:25

--
-- PostgreSQL database dump complete
--

