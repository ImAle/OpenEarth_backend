-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 03-05-2025 a las 19:10:38
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `openearth`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `chat_conversation`
--

CREATE TABLE `chat_conversation` (
  `id` bigint(20) NOT NULL,
  `last_activity` datetime(6) NOT NULL,
  `user1_id` bigint(20) NOT NULL,
  `user2_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `chat_conversation`
--

INSERT INTO `chat_conversation` (`id`, `last_activity`, `user1_id`, `user2_id`) VALUES
(1, '2025-05-02 23:43:35.000000', 2, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `chat_message`
--

CREATE TABLE `chat_message` (
  `id` bigint(20) NOT NULL,
  `read` bit(1) DEFAULT NULL,
  `text_content` text DEFAULT NULL,
  `timestamp` datetime(6) NOT NULL,
  `receiver_id` bigint(20) NOT NULL,
  `sender_id` bigint(20) NOT NULL,
  `conversation_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `chat_message`
--

INSERT INTO `chat_message` (`id`, `read`, `text_content`, `timestamp`, `receiver_id`, `sender_id`, `conversation_id`) VALUES
(1, b'1', 'Hola, he visto tu anuncio de una casa en Syke y me interesa conocer algunos detalles más 😊', '2025-05-02 23:43:00.000000', 1, 2, 1),
(2, b'0', '¿Que quieres saber?', '2025-05-02 23:43:35.000000', 2, 1, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `house`
--

CREATE TABLE `house` (
  `id` bigint(20) NOT NULL,
  `bathrooms` int(11) NOT NULL,
  `bedrooms` int(11) NOT NULL,
  `beds` int(11) NOT NULL,
  `category` enum('AMAZING_VIEWS','ARCTIC','BARNS','BEACH','BOATS','CABINS','CAMPING','CAVES','CITY','CONTAINERS','COUNTRYSIDE','DESERT','FARM','ISLANDS','LAKE','LUXE','MANSIONS','MINSUS','NATIONAL_PARKS','NEW','POOLS','ROOMS','RYOKANS','TINY','TOWERS','TREEHOUSES','TROPICAL','WINDMILLS') DEFAULT NULL,
  `creation_date` date DEFAULT NULL,
  `description` text NOT NULL,
  `guests` int(11) NOT NULL,
  `last_update_date` date DEFAULT NULL,
  `latitude` double NOT NULL,
  `location` varchar(255) NOT NULL,
  `longitude` double NOT NULL,
  `price` double NOT NULL,
  `status` enum('AVAILABLE','CLOSED') DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `owner_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `house`
--

INSERT INTO `house` (`id`, `bathrooms`, `bedrooms`, `beds`, `category`, `creation_date`, `description`, `guests`, `last_update_date`, `latitude`, `location`, `longitude`, `price`, `status`, `title`, `owner_id`) VALUES
(3, 1, 2, 2, 'CITY', '2025-05-02', 'It\'s that time of year again for a surprise for two or four\nOur romantic apartment offers the perfect retreat for couples who want to experience unforgettable moments together. The stylishly furnished rooms create a warm, inviting atmosphere that invites you to relax and linger.\nEnjoy relaxing hours in our sauna, which provides cozy warmth and relaxation - while the hot tub in the living room offers you an oasis of relaxation.\nThe space\nThe stylishly decorated spaces create a warm, welcoming atmosphere that invites you to relax and linger.\n\nEnjoy relaxing hours in your private sauna on the terrace or in the large hot tub in the living room. The large windows provide light-flooded rooms and a beautiful view of the surrounding nature.\n\nThe bedroom of this romantic apartment is a paradise for couples. An elegant four-poster bed with delicate, flowing fabrics creates a particularly dreamy atmosphere, while the box spring bed offers the highest sleeping comfort. Here you can enjoy relaxing nights together.\n\nThe open plan kitchen combines functionality with stylish design, creating a welcoming atmosphere perfect for cooking and lingering together. Equipped with high-quality appliances, it leaves nothing to be desired: whether for a cozy breakfast or a romantic dinner – here you can prepare culinary delights together and enjoy the time together.\n\nFor an unforgettable stay, we offer you exclusive packages that make your time in our romantic apartment even more special. Choose from a variety of individual offers that are perfectly tailored to your wishes and needs:\n\nEarly check-in: from 13:00 + €20.00 (if possible!)\nLate check-out: until 12 o\'clock + €20.00 (if possible!)\n\n\nFeel free to call us.', 4, '2025-05-02', 52.91504295278435, 'Deutschland, Niedersachsen, Syke-Mitte, 28857, An der Volksbank', 8.821809093912293, 145, 'AVAILABLE', 'Entire home in Syke, Germany', 1),
(4, 1, 1, 2, 'COUNTRYSIDE', '2025-05-03', 'Enjoy this amazing chalet surrounded by nature in the heart of the São Roque Wine Route. A sophisticated, private and complete space to relax and enjoy great moments in the company of those you love most.\n\nRelax in our heated SPA, heated pool, steam room or fireplace while watching your favorite movies and series.\nThe space\nOur chalet is located in the city of São Roque, which stands out for its excellent wine production and its varied and high standard cuisine.\n\nThe chalet has sophisticated and minimalist style, in a 100m2 area with balcony and fully integrated environments. A complete structure for your accommodation to be full of unforgettable moments.\n\nEquipado with heated SPA and hydromassage, an air-conditioned infinity pool, a steam sauna integrated with showers and a cozy fireplace.\n\nThe chalet also has a full kitchen equipped with barbecue, pizza oven, cooktop, electric oven, microwave, duplex fridge and Brastemp water filter.\n\nFurther listen to your favorite songs on Alexa and watch movies and series on a 55\"4k TV on a comfortable sofa facing the fireplace.\n\nCity Council rest on a king size bed, super comfortable with sheets of 1000 Egyptian cotton yarns and pillows that will make you dream of coming back every time.\n\nWe also offer courtesy welcome handmade pizza pasta and natural fermentation, pre-baked and super-practice to prepare. Township your favorite fillings, which the dough is on us.\n\nSee the full list of what we offer guests:\n\n- SPA heated with whirlpool\n\n- Air-conditioned swimming pool with infinity\n\nWood-fired fireplace (we provide the wood)\n\n- Transparent steam sauna integrated to showers\n\n- Amenities (shampoo, conditioner, soap and bath salts)\n\norning Face Towels, Bathroom, Floor\n\n- King Size Bed\n\nBed linens\n\n- Bathrobe\n\n- Hair dryer\n\nAlexa Sound Box\n\nElectric BBQ Grill\n\n- Private parking\n\n- Lounger\n\n- Wifi-Fibra Internet (300 mb)\n\n- Hot and cold air conditioning\n\n- 55’’ Smartv com Netflix, Amazon, GloboPlay, Disney+, ClaroTV+ and YouTube\n\n- 400L freezer\n\n- Electric Oven\n\n- Microwave\n\n- Cooktop\n\nKitchen utensils\n\npose Salt and olive oil\n\nMachine and coffee capsules\n\netero Brastemp Water filter\n\nappropriate Meat and cold tablets\n\nFacas especias\n\nSelective garbage collection (recyclable/organic)\n\nOptional Services (Partly Costs):\n\n- Breakfast Baskets: We offer wonderful baskets made by a traditional São Roque coffee shop.\n\n- Personal Chef: We also partner with a Personal Chef from the region whose cuisine is marked by fresh spices, products\nhandmade craftsmanship and ingredients of the best quality and following the line of\ncontemporary Italian food.\n\n\nCheckin from 3pm\nCheck-out until 11ams\nGuest access\nThe chalet area is private and exclusive to its guests.\n\nAccess to the site begins on the Wine Route, covering about 3 km on a beautiful paved road for the first 2 km. Complete check-in instructions will be sent after booking.', 4, '2025-05-03', -23.579681535829252, 'Brasil, São Paulo, Canguera, 18145-105, Morada dos Pássaros I, Alameda dos Tucanos', -47.11345655162157, 305, 'AVAILABLE', 'Entire chalet in São Roque, Brazil', 1),
(5, 1, 1, 1, 'CITY', '2025-05-03', 'Una habitación en una casa adosada tranquila en Madrid . El alojamiento es compartido por la propietaria. Hay un gato muy dulce y amable. La cama es de 160 cm.\nEl espacio\nLa casa es agradable y soleada.\nServicios y zonas comunes\nSolo para fines de semana se utiliza cocina para desayunar. Las estancias semanales (mínimo 7 días)pueden usar la cocina (dejar bien la cocina en las mismas condiciones ) y la lavadora.\nEl acceso a la zona de la piscina está regulado. Cuando el propietario no esté allí.\nEs imprescindible cerrar la puerta a la calle.\nDurante tu estancia\nPor mensaje y teléfono', 1, '2025-05-03', 40.43081937208646, 'España, Comunidad de Madrid, Madrid, 28037, San Blas - Canillejas, Calle de Alconera', -3.617672731878571, 36, 'AVAILABLE', 'Room in San Blas, Madrid', 1),
(6, 1, 1, 1, 'CITY', '2025-05-03', 'Accommodation very close to the Metropolitano stadium and the airport.\nNearby you will find bars, supermarkets and a white zone to park\nThe space\nThe accommodation is a room in a family apartment, it consists of a single bathroom that we who live there share,\nIt is located on the fourth floor. The building does not have an elevator.\nOther things to note\nThere is a security camera, located in the hallway, just take the entrance door and the hallway. Not private areas', 1, '2025-05-03', 40.430915003063404, 'España, Comunidad de Madrid, Madrid, 28037, San Blas - Canillejas, Calle de Hinojosa del Duque', -3.613525324131217, 32, 'AVAILABLE', 'Room in San Blas, Madrid', 1),
(7, 2, 2, 2, 'COUNTRYSIDE', '2025-05-03', 'This pleasant and cozy accommodation is located in Tarifa, on a plot of one hectare 400 meters from the beach and 5 minutes from Tarifa by car.\nIt stands out for its tranquility and for its good location with respect to points of interest near Tarifa.\nFree private parking is available and a sports equipment is available.\nThis accommodation is located in La Peña (Tarifa), the area is nice and quiet.\nNear the accommodation you can do nautical sports, hiking...\nThe space\nThis pleasant and cozy accommodation is located in Tarifa, on a plot of onehectare 400 meters from the beach and 5 minutes from Tarifa by car.\nIt stands out for its tranquility and for its good location with respect to points of interest near Tarifa.\nFree private parking is available and a sports equipment is available.\nAs for the house, it has a porch with outdoor shower, sunbathing area and outdoor dining area.\nThe interior of the accommodation, the salt, has a sofabed, dining room, TV and fireplace.\nThere are two bedrooms, one with a double bed and the other bedroom with two twin beds.\nThe livingroom has a sofabed, dining room, television and fireplace. A bathroom and kitchen that has an oven, microwave, washing machine, andrefrigerator.\nThis accommodation is located in La Peư a (Tarifa), the area is beautiful and quiet, east very close to the beach which can be accessed through a t \'in that crosses the road and is located in the countryside next to the mountain.\nUseful sports, hiking, biking and horseback riding, tennis are all close to the accommodation.', 3, '2025-05-03', 36.02564916746198, 'España, Andalucía, 11380, Calle Gibraltar', -5.60873923498869, 67, 'AVAILABLE', 'Entire cottage in Tarifa, Spain', 3),
(8, 1, 1, 1, 'WINDMILLS', '2025-05-03', 'Built in the 19th century, with a 360 degrees view over the sea and surroundings on the top floor.\nIt features a Bedroom, a very well-decorated living room with kitchenette, and a WC.\nFree WiFi, air conditioning, Led TV and DVD player.\nPrivate parking inside the premises, providing extra security.\nPerfect for an unforgettable honeymoon experience.\nThe space\nIt has a 4000 m garden with sub-tropical fruit trees, garden trees, and flowers.\nIn addition to the Mill ideal for 2 people, it has two more accommodation units: the Mó de Cima\'s House ideal up to 3 people and the Moleiro\'s House that hold up tp 4 people.\nGuest access\nGuests have access to all property spaces.', 2, '2025-05-03', 37.80712588422763, 'Portugal, Ponta Delgada, 9500-557', -25.802040933798622, 136, 'AVAILABLE', 'Cute windmill in Azores', 3),
(9, 3, 3, 9, 'AMAZING_VIEWS', '2025-05-03', 'Apartment highlighted for its spaciousness, tranquility and good enjoyment on the large, very sunny terrace overlooking the sea, a pleasure, in La Peña, a 5-minute drive from Tarifa, consists of : internet, 3 bedrooms, equipped kitchen, a large living room with a fireplace, 2 bathrooms, 3 showers, easy parking and everything you need to enjoy a good vacation, you can take the route of the Buddha or visit the Peña viewpoint.\nThe space\nApartment in a very quiet area to enjoy nature and beaches, it is very spacious and comfortable due to its 3 bedrooms, spacious living room and a great private terrace to enjoy the sun and magnificent views\nGuest access\nLocated 5 minutes by car from Tarifa and 5 minutes walking from beaches , close to the Hurricane and restaurants where to eat\nOther things to note\nTickets will be after 4 pm, but if the apartment is clean before there is no problem with the client entering before 4:00 pm. From 16.00 hours until 21.30 hours the delivery of keys is included in the price, but the delivery of keys from 21.30 hours until 00.00 hours has a cost of 30.00 euros and the deliveries of keys from 00.00 hours until 08.00 hours, have a cost of 50 .00 eurosTarifa is the perfect place to spend a few days of vacation,the southernmost point of Europe where you can enjoy its gastronomy, climate and its wonderful people. It is known for water sports such as kite surfing, wind surfing but you can also do many more activities such as horseback riding, ATV tours, cetacean sightings and you can even travel to the African continent just 35 minutes by ferry from Tarifa. The house is located near all these types of activities. Don\'t hesitate! Fee is waiting for you!', 8, '2025-05-03', 36.058925807164954, 'España, Andalucía, 11380, Camino de la Pista', -5.65243327776156, 130, 'AVAILABLE', 'Entire condom in Tarifa, Spain', 5),
(10, 2, 2, 3, 'COUNTRYSIDE', '2025-05-03', 'Beautiful house in the very heart of sevilla. Walking distance from everything,perfect to wander around in the small streets of the centre.  Recently renovated its perfect for your stay in sevilla.\n Come and enjoy it!', 4, '2025-05-03', 37.46367964274383, 'España, Andalucía, 41980, Calle Cristobal Colón', -6.0166415345619555, 60, 'AVAILABLE', 'Countryside house in Seville', 5);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `message_attachment`
--

CREATE TABLE `message_attachment` (
  `id` bigint(20) NOT NULL,
  `content` varchar(255) NOT NULL,
  `metadata` varchar(255) DEFAULT NULL,
  `type` enum('AUDIO','FILE','IMAGE') NOT NULL,
  `message_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `payment`
--

CREATE TABLE `payment` (
  `id` bigint(20) NOT NULL,
  `amount` double DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `currency` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `method` varchar(255) DEFAULT NULL,
  `payer_id` varchar(255) DEFAULT NULL,
  `payment_id` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `payment`
--

INSERT INTO `payment` (`id`, `amount`, `created_at`, `currency`, `description`, `method`, `payer_id`, `payment_id`, `status`, `updated_at`, `user_id`) VALUES
(1, 145, '2025-05-02 18:55:57.000000', 'EUR', 'Purchase Order: 0JE2417682128991E at 2025-05-02T20:55:57.307805500 for 145.00EUR', 'Paypal', '3REABK64YWAQ2', '0JE2417682128991E', 'COMPLETED', '2025-05-02 18:55:57.000000', 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `picture`
--

CREATE TABLE `picture` (
  `id` bigint(20) NOT NULL,
  `url` varchar(255) NOT NULL,
  `house_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `picture`
--

INSERT INTO `picture` (`id`, `url`, `house_id`, `user_id`) VALUES
(17, '/api/picture/syke-1_3.avif', 3, NULL),
(18, '/api/picture/syke-7_3.jpeg', 3, NULL),
(19, '/api/picture/syke-6_3.avif', 3, NULL),
(21, '/api/picture/syke-4_3.avif', 3, NULL),
(22, '/api/picture/syke-3_3.jpeg', 3, NULL),
(23, '/api/picture/syke-2_3.avif', 3, NULL),
(24, '/api/picture/syke-8_3.avif', 3, NULL),
(25, '/api/picture/syke-5_3.avif', 3, NULL),
(26, '/api/picture/ale_1.jpeg', NULL, 1),
(27, '/api/picture/brazil-chalet-8_4.jpg', 4, NULL),
(28, '/api/picture/brazil-chalet-7_4.avif', 4, NULL),
(29, '/api/picture/brazil-chalet-6_4.avif', 4, NULL),
(30, '/api/picture/brazil-chalet-5_4.jpg', 4, NULL),
(31, '/api/picture/brazil-chalet-4_4.avif', 4, NULL),
(32, '/api/picture/brazil-chalet-3_4.avif', 4, NULL),
(33, '/api/picture/brazil-chalet-2_4.avif', 4, NULL),
(34, '/api/picture/brazil-chalet-1_4.avif', 4, NULL),
(35, '/api/picture/sanblasi-1_5.avif', 5, NULL),
(36, '/api/picture/sanblasi-6_5.avif', 5, NULL),
(37, '/api/picture/sanblasi-5_5.avif', 5, NULL),
(38, '/api/picture/sanblasi-3_5.avif', 5, NULL),
(39, '/api/picture/sanblasi-2_5.avif', 5, NULL),
(40, '/api/picture/sanblas-1_6.avif', 6, NULL),
(41, '/api/picture/sanblas-4_6.avif', 6, NULL),
(42, '/api/picture/sanblas-3_6.avif', 6, NULL),
(43, '/api/picture/sanblas-2_6.avif', 6, NULL),
(44, '/api/picture/tarifa-1_7.avif', 7, NULL),
(45, '/api/picture/tarifa-8_7.avif', 7, NULL),
(46, '/api/picture/tarifa-7_7.avif', 7, NULL),
(47, '/api/picture/tarifa-6_7.avif', 7, NULL),
(48, '/api/picture/tarifa-5_7.avif', 7, NULL),
(49, '/api/picture/tarifa-4_7.avif', 7, NULL),
(50, '/api/picture/tarifa-3_7.avif', 7, NULL),
(51, '/api/picture/tarifa-2_7.avif', 7, NULL),
(52, '/api/picture/windPortugal-1_8.avif', 8, NULL),
(53, '/api/picture/windPortugal-9_8.avif', 8, NULL),
(54, '/api/picture/windPortugal-8_8.avif', 8, NULL),
(55, '/api/picture/windPortugal-7_8.avif', 8, NULL),
(56, '/api/picture/windPortugal-6_8.avif', 8, NULL),
(57, '/api/picture/windPortugal-5_8.avif', 8, NULL),
(58, '/api/picture/windPortugal-4_8.avif', 8, NULL),
(59, '/api/picture/windPortugal-3_8.avif', 8, NULL),
(60, '/api/picture/windPortugal-2_8.avif', 8, NULL),
(61, '/api/picture/manolo_3.avif', NULL, 3),
(62, '/api/picture/aurora_5.avif', NULL, 5),
(63, '/api/picture/tarifa-condo-9_9.avif', 9, NULL),
(64, '/api/picture/tarifa-condo-8_9.avif', 9, NULL),
(65, '/api/picture/tarifa-condo-7_9.avif', 9, NULL),
(66, '/api/picture/tarifa-condo-6_9.avif', 9, NULL),
(67, '/api/picture/tarifa-condo-5_9.avif', 9, NULL),
(68, '/api/picture/tarifa-condo-4_9.avif', 9, NULL),
(69, '/api/picture/tarifa-condo-3_9.avif', 9, NULL),
(70, '/api/picture/tarifa-condo-2_9.avif', 9, NULL),
(71, '/api/picture/tarifa-condo-1_9.avif', 9, NULL),
(72, '/api/picture/seville-3_10.avif', 10, NULL),
(73, '/api/picture/seville-2_10.avif', 10, NULL),
(74, '/api/picture/seville-1_10.avif', 10, NULL),
(75, '/api/picture/seville-7_10.avif', 10, NULL),
(76, '/api/picture/seville-6_10.avif', 10, NULL),
(77, '/api/picture/seville-5_10.avif', 10, NULL),
(78, '/api/picture/seville-4_10.avif', 10, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `rent`
--

CREATE TABLE `rent` (
  `id` bigint(20) NOT NULL,
  `end_date` date DEFAULT NULL,
  `price` double NOT NULL,
  `start_date` date DEFAULT NULL,
  `house_id` bigint(20) NOT NULL,
  `payment_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `cancelled` bit(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `rent`
--

INSERT INTO `rent` (`id`, `end_date`, `price`, `start_date`, `house_id`, `payment_id`, `user_id`, `cancelled`) VALUES
(1, '2025-05-02', 145, '2025-05-01', 3, 1, 2, b'0');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `report`
--

CREATE TABLE `report` (
  `id` bigint(20) NOT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `reported_id` bigint(20) NOT NULL,
  `reporter_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `review`
--

CREATE TABLE `review` (
  `id` bigint(20) NOT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `house_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `review`
--

INSERT INTO `review` (`id`, `comment`, `house_id`, `user_id`) VALUES
(1, 'It was a very nice place. The hostess was very nice to us. Looking for coming back soon', 3, 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `user`
--

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL,
  `creation_date` date NOT NULL,
  `email` varchar(255) NOT NULL,
  `enabled` bit(1) NOT NULL,
  `firstname` varchar(255) NOT NULL,
  `lastname` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('ADMIN','GUEST','HOSTESS') NOT NULL,
  `token` varchar(255) DEFAULT NULL,
  `username` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `user`
--

INSERT INTO `user` (`id`, `creation_date`, `email`, `enabled`, `firstname`, `lastname`, `password`, `role`, `token`, `username`) VALUES
(1, '2025-05-02', 'alejandrogalldom@gmail.com', b'1', 'Alejandro', 'Gallego', '$2a$10$fqanI2OHrsyAlO3p0aZEQOo3inJEklqOcFDDVw9U3Hgh8hZ9Slq8u', 'HOSTESS', 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhbGVqYW5kcm9nYWxsZG9tQGdtYWlsLmNvbSIsImlhdCI6MTc0NjI5MTE0OCwiZXhwIjoxNzQ2NDYzOTQ4fQ.YOe0EEl6dM9sPQniQ2lJZF_5bB0Ca5SKLSSW0Nuv93QMwbBjcFSfxEefK31DuDjgft_PP8AgoueLk-20rPQdlg', 'alejandro'),
(2, '2025-05-02', 'juan@gmail.com', b'1', 'Juan', 'Gonzalez', '$2a$10$.JzeqZS.ifEOYXLHFk4SzeHMbbTX0nYTwVTSvFL1Kjgc5admiL2b2', 'GUEST', 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqdWFuQGdtYWlsLmNvbSIsImlhdCI6MTc0NjI4ODQ3OSwiZXhwIjoxNzQ2NDYxMjc5fQ.4bA2yFLI9GAXJgehhaiosUF9RxTC6XFX4lJH3vmRUt8yxdG_xXzkPIENnACQvULdW5TyK_KUlnfE5fYkxILgKQ', 'Juanito'),
(3, '2025-05-03', 'manolo@gmail.com', b'1', 'Manolo', 'Gallego', '$2a$10$bgPdkTjB2MpZMCInw15uR.cQsCqXKbttcg99S5dH2pisnSXgI0dMS', 'HOSTESS', 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYW5vbG9AZ21haWwuY29tIiwiaWF0IjoxNzQ2Mjg5MTUyLCJleHAiOjE3NDY0NjE5NTJ9.y2XoPK1vi9QxEW_6C6D-PbNthmeCaFOSb0apoNYvHSYJdpxA94s4Wf-dvZZsP7_cYp3jIvCTtMnTFoFJWn9_Rg', 'manolo'),
(4, '2025-05-03', 'admin@gmail.com', b'1', 'Leopoldo', 'Gutierrez', '$2a$10$vrygrejNn.KJySZWadGu2OY6QyOcLWR9uCrs3QQO6vw/Bi62x7xVa', 'ADMIN', 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJpYXQiOjE3NDYyODk2MzQsImV4cCI6MTc0NjQ2MjQzNH0.QDDzpJ9GiHIkER__1kCrbWjLrd4I00nt6vG_vceJyHALNbzcUbrypcf8_Ch2sHh5a6oMILyj1GD5qDNdw6bhyg', 'Admin'),
(5, '2025-05-03', 'aurora@gmail.com', b'1', 'Aurora', 'Boreal', '$2a$10$RbWiEv/irurqB4gWU3s5CueT6LeWJhidVwQLpqi5mjDoSteuaDuzm', 'HOSTESS', 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhdXJvcmFAZ21haWwuY29tIiwiaWF0IjoxNzQ2Mjg5NzIzLCJleHAiOjE3NDY0NjI1MjN9.5otaDFQyAoIQdWDikcxyVs_CinjczIgUt8BGo7iwG9SdrjcYsmMaXBtUZOrlv0KXp-ViCE5YRWQXMunT9lW19g', 'Aurora');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `chat_conversation`
--
ALTER TABLE `chat_conversation`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKpg7s54rtiqob8yhve15q4w2cw` (`user1_id`),
  ADD KEY `FKbpcntk09kc2qhc963kfarygiv` (`user2_id`);

--
-- Indices de la tabla `chat_message`
--
ALTER TABLE `chat_message`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK8535ns29owlsavcfbdx1fqejr` (`receiver_id`),
  ADD KEY `FKm92rh2bmfw19xcn7nj5vrixsi` (`sender_id`),
  ADD KEY `FK2ojdav5p4lsot6u8fllhp3h7k` (`conversation_id`);

--
-- Indices de la tabla `house`
--
ALTER TABLE `house`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK4r0nxoot9tsovfbhhopg2mhi` (`owner_id`);

--
-- Indices de la tabla `message_attachment`
--
ALTER TABLE `message_attachment`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKf3fvr1eaq70rdiwydv3k7l5xl` (`message_id`);

--
-- Indices de la tabla `payment`
--
ALTER TABLE `payment`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `picture`
--
ALTER TABLE `picture`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKjvfb39eclkk6ossbtnlmtjrgg` (`user_id`),
  ADD KEY `FKagyupvh4jygt61mnvu1gmu3ff` (`house_id`);

--
-- Indices de la tabla `rent`
--
ALTER TABLE `rent`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKla3v9ca9gj0da9ma88qlx2it` (`payment_id`),
  ADD KEY `FK8886y7dhxl6xsuhsbnclss1ie` (`house_id`),
  ADD KEY `FKg3q3j6lq13lh6qkjw2fpj2kdk` (`user_id`);

--
-- Indices de la tabla `report`
--
ALTER TABLE `report`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKkqwu7egf4fup0xog0ot2s065c` (`reported_id`),
  ADD KEY `FKndpjl61ubcm2tkf7ml1ynq13t` (`reporter_id`);

--
-- Indices de la tabla `review`
--
ALTER TABLE `review`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKg24b2dffe33e4197k5d2hwncd` (`user_id`,`house_id`),
  ADD KEY `FK6ecwggu7yjrhb0buspnijuns0` (`house_id`);

--
-- Indices de la tabla `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKob8kqyqqgmefl0aco34akdtpe` (`email`),
  ADD UNIQUE KEY `UKsb8bbouer5wak8vyiiy4pf2bx` (`username`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `chat_conversation`
--
ALTER TABLE `chat_conversation`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `chat_message`
--
ALTER TABLE `chat_message`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `house`
--
ALTER TABLE `house`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT de la tabla `message_attachment`
--
ALTER TABLE `message_attachment`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `payment`
--
ALTER TABLE `payment`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `picture`
--
ALTER TABLE `picture`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=79;

--
-- AUTO_INCREMENT de la tabla `rent`
--
ALTER TABLE `rent`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `report`
--
ALTER TABLE `report`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `review`
--
ALTER TABLE `review`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `user`
--
ALTER TABLE `user`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `chat_conversation`
--
ALTER TABLE `chat_conversation`
  ADD CONSTRAINT `FKbpcntk09kc2qhc963kfarygiv` FOREIGN KEY (`user2_id`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `FKpg7s54rtiqob8yhve15q4w2cw` FOREIGN KEY (`user1_id`) REFERENCES `user` (`id`);

--
-- Filtros para la tabla `chat_message`
--
ALTER TABLE `chat_message`
  ADD CONSTRAINT `FK2ojdav5p4lsot6u8fllhp3h7k` FOREIGN KEY (`conversation_id`) REFERENCES `chat_conversation` (`id`),
  ADD CONSTRAINT `FK8535ns29owlsavcfbdx1fqejr` FOREIGN KEY (`receiver_id`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `FKm92rh2bmfw19xcn7nj5vrixsi` FOREIGN KEY (`sender_id`) REFERENCES `user` (`id`);

--
-- Filtros para la tabla `house`
--
ALTER TABLE `house`
  ADD CONSTRAINT `FK4r0nxoot9tsovfbhhopg2mhi` FOREIGN KEY (`owner_id`) REFERENCES `user` (`id`);

--
-- Filtros para la tabla `message_attachment`
--
ALTER TABLE `message_attachment`
  ADD CONSTRAINT `FKf3fvr1eaq70rdiwydv3k7l5xl` FOREIGN KEY (`message_id`) REFERENCES `chat_message` (`id`);

--
-- Filtros para la tabla `picture`
--
ALTER TABLE `picture`
  ADD CONSTRAINT `FKagyupvh4jygt61mnvu1gmu3ff` FOREIGN KEY (`house_id`) REFERENCES `house` (`id`),
  ADD CONSTRAINT `FKfa3htlps9ddix2jx1spmpedko` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Filtros para la tabla `rent`
--
ALTER TABLE `rent`
  ADD CONSTRAINT `FK8886y7dhxl6xsuhsbnclss1ie` FOREIGN KEY (`house_id`) REFERENCES `house` (`id`),
  ADD CONSTRAINT `FKcrsfxwdbkwgr9ru40arao9rgd` FOREIGN KEY (`payment_id`) REFERENCES `payment` (`id`),
  ADD CONSTRAINT `FKg3q3j6lq13lh6qkjw2fpj2kdk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Filtros para la tabla `report`
--
ALTER TABLE `report`
  ADD CONSTRAINT `FKkqwu7egf4fup0xog0ot2s065c` FOREIGN KEY (`reported_id`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `FKndpjl61ubcm2tkf7ml1ynq13t` FOREIGN KEY (`reporter_id`) REFERENCES `user` (`id`);

--
-- Filtros para la tabla `review`
--
ALTER TABLE `review`
  ADD CONSTRAINT `FK6ecwggu7yjrhb0buspnijuns0` FOREIGN KEY (`house_id`) REFERENCES `house` (`id`),
  ADD CONSTRAINT `FKiyf57dy48lyiftdrf7y87rnxi` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
