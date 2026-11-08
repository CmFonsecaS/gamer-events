-- =====================================================================
-- Gamer Events - Script de inserción de datos de prueba
-- Requiere ejecutar antes 01_schema.sql
-- =====================================================================

USE gamer_events;
SET NAMES utf8mb4;

-- Usuarios de prueba:
--   jugador1 / Gamer#2026      jugador2 / Duoc#Seguro26
INSERT INTO usuario (username, password_hash, nombre, rol, activo) VALUES
  ('jugador1', '$2a$12$Yq/T10c74EtHkU./UsWy9eXJ.Bfwnqvm2ZCIez.6zax.clpTH7XJ2', 'Jugador Uno', 'USER', TRUE),
  ('jugador2', '$2a$12$nfWiF65BaYnDRKIWrOr5x.97uYzELtSxn5hkoF/2sKdVxD0TqM4T6', 'Jugador Dos', 'USER', TRUE);

INSERT INTO categoria (nombre) VALUES ('Torneo'), ('LAN Party'), ('Convención'), ('Meetup'), ('Taller'), ('Maratón benéfica');

INSERT INTO videojuego (nombre, genero) VALUES
  ('Valorant', 'Shooter táctico'),
  ('Counter-Strike 2', 'Shooter táctico'),
  ('League of Legends', 'MOBA'),
  ('Super Smash Bros. Ultimate', 'Pelea'),
  ('Multijuego', 'Varios'),
  ('EA Sports FC 26', 'Deportes'),
  ('Street Fighter 6', 'Pelea'),
  ('Fortnite', 'Battle royale'),
  ('Pokémon Escarlata y Púrpura', 'RPG'),
  ('Rocket League', 'Deportes'),
  ('Tekken 8', 'Pelea'),
  ('Dota 2', 'MOBA'),
  ('Clash Royale', 'Estrategia móvil'),
  ('Minecraft', 'Sandbox'),
  ('Super Mario Odyssey', 'Plataformas');

INSERT INTO organizador (nombre, email, sitio_web) VALUES
  ('Andes Esports', 'contacto@andes-esports.example', 'https://andes-esports.example'),
  ('Club eSports Duoc', 'clubesports@duoc.example', NULL),
  ('Liga Gamer Chile', 'hola@ligagamer.example', 'https://ligagamer.example'),
  ('Comunidad Retro Valpo', 'retrovalpo@comunidad.example', NULL),
  ('Pixel Fest Producciones', 'info@pixelfest.example', 'https://pixelfest.example'),
  ('Fighters Latam', 'staff@fighterslatam.example', 'https://fighterslatam.example');

INSERT INTO evento (nombre, resumen, descripcion, videojuego_id, categoria_id, organizador_id, modalidad, fecha_inicio, fecha_fin, pais, ciudad, direccion, enlace_acceso, requisitos, info_inscripcion, precio, cupos, inscritos, imagen_portada, fecha_publicacion) VALUES
  ('Copa Andes Valorant 2026', 'El torneo presencial de Valorant más grande de la zona central, con 32 equipos y transmisión en vivo.', 'La Copa Andes reúne a 32 equipos de cinco jugadores en un formato de doble eliminación. Las finales se juegan en escenario principal con casters en vivo, zona de streaming y área de exhibición de periféricos. Los mejores equipos clasifican al circuito nacional de 2027.', 1, 1, 1, 'PRESENCIAL', '2026-11-21 10:00:00', '2026-11-22 20:00:00', 'Chile', 'Santiago', 'Centro de Convenciones Matta, Av. Matta 1200, Santiago', NULL, 'Equipos de 5 jugadores titulares y 1 suplente. Todos los integrantes deben tener 16 años o más (menores con autorización firmada). Cuenta Riot con rango mínimo Platino.', 'Inscripción por equipo hasta el 15 de noviembre. Valor $5.000 por jugador, pago al confirmar el cupo. Se envía el cuadro del torneo por correo 48 horas antes.', 5000, 160, 152, '/img/eventos/copa-andes-valorant.svg', '2026-10-20 09:00:00'),
  ('LAN Party Concepción: CS2 Night', 'Una noche completa de Counter-Strike 2 en red local, con mini torneos 2v2 y 5v5 hasta el amanecer.', 'Trae tu equipo y conecta tu PC a la red de la LAN. Habrá mini torneos 2v2 cada dos horas, un torneo principal 5v5, comida y zona de descanso. La organización proporciona switch, energía y servidor dedicado.', 2, 2, 2, 'PRESENCIAL', '2026-11-28 18:00:00', '2026-11-29 08:00:00', 'Chile', 'Concepción', 'Sede Duoc UC Concepción, Arturo Prat 696, Concepción', NULL, 'Llevar computador propio, audífonos y cable de red. Mayores de 18 años o menores acompañados de un adulto responsable.', 'Cupos limitados a 80 puestos. Valor $3.000 que incluye snack nocturno. Inscripción en el mesón del club o por correo.', 3000, 80, 61, '/img/eventos/lan-party-concepcion.svg', '2026-10-28 12:00:00'),
  ('Liga Online LoL Latam Sur — Temporada Verano', 'Liga online por temporadas para equipos amateur de la región LAS, con partidas semanales y premios en RP.', 'La temporada de verano de la Liga Gamer se juega los sábados a las 19:00 durante ocho semanas. Fase de grupos al mejor de uno y playoffs al mejor de tres. Las partidas destacadas se transmiten en el canal oficial de la liga.', 3, 1, 3, 'ONLINE', '2026-12-05 19:00:00', '2027-01-30 23:00:00', 'Latinoamérica', 'Online', NULL, 'https://ligagamer.example/lol-verano', 'Equipos de 5 jugadores con cuenta en el servidor LAS nivel 30 o superior. Se exige Discord para la coordinación y check-in 30 minutos antes de cada fecha.', 'Inscripción gratuita por formulario en el sitio de la liga. Cierre de inscripciones el 30 de noviembre o al completar 256 jugadores.', 0, 256, 230, '/img/eventos/liga-online-lol-latam-sur.svg', '2026-11-02 10:00:00'),
  ('Smash Fest Valparaíso', 'Torneo de Smash Ultimate 1v1 y dobles con setups, música en vivo y feria de coleccionables.', 'Smash Fest vuelve al puerto con un torneo 1v1 de doble eliminación y un torneo de dobles por la tarde. La comunidad lleva consolas y monitores para 24 setups. Habrá feria de figuras, retro gaming libre y premiación con trofeos impresos en 3D.', 4, 1, 4, 'PRESENCIAL', '2026-12-12 11:00:00', '2026-12-12 21:00:00', 'Chile', 'Valparaíso', 'Centro Cultural Cerro Alegre, Templeman 650, Valparaíso', NULL, 'Abierto a todas las edades. Traer control propio (Pro Controller o GameCube con adaptador). Reglas estándar de la comunidad: 3 stocks, 7 minutos.', 'Valor $2.000 por modalidad. Inscripción previa en línea o el mismo día hasta las 11:30 si quedan cupos.', 2000, 96, 88, '/img/eventos/smash-fest-valparaiso.svg', '2026-11-05 18:00:00'),
  ('Pixel Fest Santiago 2026', 'Tres días de convención gamer: zona indie, retro, cosplay, torneos abiertos y charlas de la industria.', 'Pixel Fest es la convención gamer de fin de año. Incluye pabellón de desarrolladores indie chilenos, zona retro con más de 50 consolas, concurso de cosplay, torneos abiertos de distintos juegos y charlas sobre desarrollo de videojuegos y esports.', 5, 3, 5, 'PRESENCIAL', '2026-12-18 10:00:00', '2026-12-20 20:00:00', 'Chile', 'Santiago', 'Espacio Riesco, Av. El Salto 5000, Huechuraba', NULL, 'Entrada general para mayores de 6 años. Menores de 14 años deben ingresar con un adulto. Prohibido el ingreso de réplicas de armas metálicas en cosplay.', 'Entradas a $12.000 por día o $30.000 el pase de tres días. Venta en línea; se presenta el código QR en el acceso.', 12000, 3000, 2740, '/img/eventos/pixel-fest-santiago.svg', '2026-10-01 09:00:00'),
  ('Copa Online EA Sports FC 26', 'Torneo 1v1 online multiplataforma de FC 26 con cruces eliminatorios en una sola tarde.', 'Torneo de un día con 128 jugadores en llaves de eliminación directa. Partidos de ida y vuelta en modo Ultimate Team con reglas de la liga. La final se transmite con relatores invitados.', 6, 1, 3, 'ONLINE', '2026-11-15 16:00:00', '2026-11-15 22:00:00', 'Chile', 'Online', NULL, 'https://ligagamer.example/fc26', 'Residir en Chile. Contar con PS5, Xbox Series o PC con juego cruzado activado y conexión estable. Check-in obligatorio a las 15:30.', 'Inscripción gratuita en el sitio de la liga con tu ID de plataforma. Los cruces se publican el día del evento.', 0, 128, 97, '/img/eventos/copa-online-ea-fc-26.svg', '2026-10-25 15:00:00'),
  ('Street Fighter 6 Buenos Aires Showdown', 'El major de juegos de pelea del Cono Sur, con jugadores invitados y bolsa de premios.', 'Showdown reúne a la escena de juegos de pelea de Argentina, Chile y Uruguay. Torneo principal de Street Fighter 6 con pools el sábado y top 8 el domingo, side events y zona casual con más de 40 setups.', 7, 1, 6, 'PRESENCIAL', '2027-01-16 12:00:00', '2027-01-17 21:00:00', 'Argentina', 'Buenos Aires', 'Centro Cultural Konex, Sarmiento 3131, CABA', NULL, 'Mayores de 13 años. Traer control o arcade stick propio. Se aplica el reglamento oficial de Capcom Pro Tour.', 'Inscripción online en pesos argentinos o dólares. Cierre de inscripción el 10 de enero.', 8000, 256, 201, '/img/eventos/sf6-buenos-aires-showdown.svg', '2026-11-04 11:00:00'),
  ('Fortnite Creative Jam: crea tu mapa', 'Taller online para aprender a diseñar mapas en Fortnite Creative y UEFN, con concurso al final.', 'En cuatro horas aprenderás lo básico de UEFN: terreno, dispositivos y lógica de juego. Al terminar, los participantes tienen una semana para publicar su mapa y competir por premios en V-Bucks y licencias de software.', 8, 5, 5, 'ONLINE', '2026-11-29 15:00:00', '2026-11-29 19:00:00', 'Latinoamérica', 'Online', NULL, 'https://pixelfest.example/creative-jam', 'PC con Windows capaz de ejecutar Unreal Editor for Fortnite. Cuenta de Epic Games. Edad mínima 13 años.', 'Inscripción gratuita con correo electrónico. El enlace a la sala se envía 24 horas antes.', 0, 300, 145, '/img/eventos/fortnite-creative-jam.svg', '2026-11-06 20:00:00'),
  ('Meetup Pokémon VGC Lima', 'Encuentro de jugadores competitivos de Pokémon con práctica de equipos, intercambios y mini torneo.', 'Tarde comunitaria para jugadores de VGC: revisión de equipos en grupo, charla sobre el metajuego y mini torneo suizo con premios. Ideal para quienes quieren empezar en el formato competitivo.', 9, 4, 6, 'PRESENCIAL', '2026-12-06 14:00:00', '2026-12-06 20:00:00', 'Perú', 'Lima', 'Larcomar Gaming Hub, Malecón de la Reserva 610, Miraflores', NULL, 'Nintendo Switch con Pokémon Escarlata o Púrpura y equipo legal según reglamento vigente. Todas las edades.', 'Entrada liberada con inscripción previa. Cupos limitados por espacio.', 0, 64, 39, '/img/eventos/meetup-pokemon-vgc-lima.svg', '2026-10-30 10:00:00'),
  ('Rocket League 3v3 Bogotá Open', 'Open presencial de Rocket League 3v3 con 32 equipos y final en escenario.', 'El Bogotá Open reúne a equipos colombianos y de la región en formato suizo seguido de playoffs. Habrá zona de práctica, tienda de periféricos y transmisión oficial.', 10, 1, 1, 'PRESENCIAL', '2027-02-06 10:00:00', '2027-02-07 19:00:00', 'Colombia', 'Bogotá', 'Ágora Bogotá, Calle 24 #38-47, Bogotá', NULL, 'Equipos de 3 jugadores más 1 suplente opcional. Edad mínima 15 años con autorización si es menor de edad.', 'Inscripción por equipo en el sitio de Andes Esports. Pago en pesos colombianos al confirmar.', 6000, 96, 54, '/img/eventos/rocket-league-bogota-3v3.svg', '2026-11-01 09:00:00'),
  ('Tekken 8 CDMX Clash', 'Torneo de Tekken 8 con pools abiertas y top 16 transmitido en vivo desde la Ciudad de México.', 'CDMX Clash es uno de los torneos de Tekken más concurridos de Latinoamérica. Pools el sábado, top 16 el domingo y exhibición con jugadores invitados. Incluye torneo de equipos 3v3 como evento paralelo.', 11, 1, 6, 'PRESENCIAL', '2027-03-13 11:00:00', '2027-03-14 22:00:00', 'México', 'Ciudad de México', 'Arena Gamer Roma, Av. Álvaro Obregón 120, CDMX', NULL, 'Mayores de 13 años. Traer control propio. Reglamento: 3 de 5 rondas, mejor de 3 partidas; top 8 al mejor de 5.', 'Pre-registro en línea hasta el 1 de marzo. Inscripción en sitio solo si quedan cupos.', 9000, 384, 120, '/img/eventos/tekken-8-cdmx-clash.svg', '2026-11-07 08:00:00'),
  ('Dota 2 Andes Cup Online', 'Copa online de Dota 2 para equipos de Sudamérica con fase de grupos y playoffs transmitidos.', 'La Andes Cup se juega durante tres fines de semana. Fase de grupos al mejor de dos y playoffs al mejor de tres. Las partidas se juegan en lobbies de servidor Chile o Perú según acuerdo de los equipos.', 12, 1, 1, 'ONLINE', '2027-01-09 17:00:00', '2027-01-24 23:00:00', 'Latinoamérica', 'Online', NULL, 'https://andes-esports.example/dota-cup', 'Equipos de 5 jugadores más hasta 2 suplentes. Cuentas Steam con al menos 500 horas en Dota 2 y sin sanciones vigentes.', 'Inscripción gratuita por equipo. Cierre el 31 de diciembre o al completar 32 equipos.', 0, 160, 158, '/img/eventos/dota-2-andes-cup.svg', '2026-10-15 16:00:00'),
  ('Clash Royale Viña Mobile Cup', 'Torneo de Clash Royale presencial para jugadores de dispositivos móviles, con premios en tienda gamer.', 'Torneo de un día en formato eliminación doble con partidas al mejor de tres. Se habilitan cargadores y Wi-Fi exclusivo para participantes. Premiación al cierre con gift cards de tienda gamer.', 13, 1, 2, 'PRESENCIAL', '2026-12-19 15:00:00', '2026-12-19 20:00:00', 'Chile', 'Viña del Mar', 'Sede Duoc UC Viña del Mar, Av. Padre Hurtado 1090', NULL, 'Teléfono propio con Clash Royale actualizado y nivel de rey 11 o superior. Abierto a estudiantes y público general.', 'Inscripción gratuita por correo al club. Confirmación de cupo por orden de llegada.', 0, 64, 44, '/img/eventos/clash-royale-vina-mobile.svg', '2026-11-03 13:00:00'),
  ('Taller presencial de Redstone en Minecraft', 'Taller práctico de circuitos de Redstone para crear puertas, granjas automáticas y minijuegos.', 'Taller guiado de tres horas sobre lógica con Redstone: compuertas, relojes, pistones y sensores. Al final cada grupo construye un minijuego funcional. Ideal para introducir pensamiento computacional jugando.', 14, 5, 2, 'PRESENCIAL', '2027-01-23 10:00:00', '2027-01-23 13:00:00', 'Chile', 'Santiago', 'Sede Duoc UC San Carlos de Apoquindo, Camino El Alba 12881', NULL, 'Edad mínima 10 años. Se proporcionan computadores con Minecraft Java Edition. Conocimientos básicos del juego.', 'Inscripción gratuita con cupos limitados a 30 personas. Se solicita confirmar asistencia 48 horas antes.', 0, 30, 28, '/img/eventos/taller-minecraft-redstone.svg', '2026-11-06 09:00:00'),
  ('Maratón Solidaria de Speedruns', 'Maratón online de speedruns en apoyo a una fundación de salud infantil, con corredores de toda la región.', 'Durante 36 horas, corredores de la comunidad completarán juegos lo más rápido posible mientras se reúnen donaciones. El evento estelar es Super Mario Odyssey Any%. Se aceptan postulaciones de otros juegos en el formulario.', 15, 6, 5, 'ONLINE', '2027-02-20 12:00:00', '2027-02-22 00:00:00', 'Chile', 'Online', NULL, 'https://pixelfest.example/maraton', 'Postulación con un video de tu mejor run. Conexión estable y software de transmisión configurado. Mayores de 15 años.', 'Postulación de corredores gratuita hasta el 31 de enero. El público puede seguir la transmisión sin inscribirse.', 0, 50, 22, '/img/eventos/speedrun-maraton-solidaria.svg', '2026-10-10 10:00:00');

INSERT INTO evento_foto (evento_id, url, descripcion, orden) VALUES
  (1, '/img/eventos/copa-andes-valorant-1.svg', 'Escenario principal - Copa Andes Valorant 2026', 1),
  (1, '/img/eventos/copa-andes-valorant-2.svg', 'Zona de juego - Copa Andes Valorant 2026', 2),
  (1, '/img/eventos/copa-andes-valorant-3.svg', 'Comunidad y premiación - Copa Andes Valorant 2026', 3),
  (2, '/img/eventos/lan-party-concepcion-1.svg', 'Escenario principal - LAN Party Concepción: CS2 Night', 1),
  (2, '/img/eventos/lan-party-concepcion-2.svg', 'Zona de juego - LAN Party Concepción: CS2 Night', 2),
  (2, '/img/eventos/lan-party-concepcion-3.svg', 'Comunidad y premiación - LAN Party Concepción: CS2 Night', 3),
  (3, '/img/eventos/liga-online-lol-latam-sur-1.svg', 'Escenario principal - Liga Online LoL Latam Sur — Temporada Verano', 1),
  (3, '/img/eventos/liga-online-lol-latam-sur-2.svg', 'Zona de juego - Liga Online LoL Latam Sur — Temporada Verano', 2),
  (3, '/img/eventos/liga-online-lol-latam-sur-3.svg', 'Comunidad y premiación - Liga Online LoL Latam Sur — Temporada Verano', 3),
  (4, '/img/eventos/smash-fest-valparaiso-1.svg', 'Escenario principal - Smash Fest Valparaíso', 1),
  (4, '/img/eventos/smash-fest-valparaiso-2.svg', 'Zona de juego - Smash Fest Valparaíso', 2),
  (4, '/img/eventos/smash-fest-valparaiso-3.svg', 'Comunidad y premiación - Smash Fest Valparaíso', 3),
  (5, '/img/eventos/pixel-fest-santiago-1.svg', 'Escenario principal - Pixel Fest Santiago 2026', 1),
  (5, '/img/eventos/pixel-fest-santiago-2.svg', 'Zona de juego - Pixel Fest Santiago 2026', 2),
  (5, '/img/eventos/pixel-fest-santiago-3.svg', 'Comunidad y premiación - Pixel Fest Santiago 2026', 3),
  (6, '/img/eventos/copa-online-ea-fc-26-1.svg', 'Escenario principal - Copa Online EA Sports FC 26', 1),
  (6, '/img/eventos/copa-online-ea-fc-26-2.svg', 'Zona de juego - Copa Online EA Sports FC 26', 2),
  (6, '/img/eventos/copa-online-ea-fc-26-3.svg', 'Comunidad y premiación - Copa Online EA Sports FC 26', 3),
  (7, '/img/eventos/sf6-buenos-aires-showdown-1.svg', 'Escenario principal - Street Fighter 6 Buenos Aires Showdown', 1),
  (7, '/img/eventos/sf6-buenos-aires-showdown-2.svg', 'Zona de juego - Street Fighter 6 Buenos Aires Showdown', 2),
  (7, '/img/eventos/sf6-buenos-aires-showdown-3.svg', 'Comunidad y premiación - Street Fighter 6 Buenos Aires Showdown', 3),
  (8, '/img/eventos/fortnite-creative-jam-1.svg', 'Escenario principal - Fortnite Creative Jam: crea tu mapa', 1),
  (8, '/img/eventos/fortnite-creative-jam-2.svg', 'Zona de juego - Fortnite Creative Jam: crea tu mapa', 2),
  (8, '/img/eventos/fortnite-creative-jam-3.svg', 'Comunidad y premiación - Fortnite Creative Jam: crea tu mapa', 3),
  (9, '/img/eventos/meetup-pokemon-vgc-lima-1.svg', 'Escenario principal - Meetup Pokémon VGC Lima', 1),
  (9, '/img/eventos/meetup-pokemon-vgc-lima-2.svg', 'Zona de juego - Meetup Pokémon VGC Lima', 2),
  (9, '/img/eventos/meetup-pokemon-vgc-lima-3.svg', 'Comunidad y premiación - Meetup Pokémon VGC Lima', 3),
  (10, '/img/eventos/rocket-league-bogota-3v3-1.svg', 'Escenario principal - Rocket League 3v3 Bogotá Open', 1),
  (10, '/img/eventos/rocket-league-bogota-3v3-2.svg', 'Zona de juego - Rocket League 3v3 Bogotá Open', 2),
  (10, '/img/eventos/rocket-league-bogota-3v3-3.svg', 'Comunidad y premiación - Rocket League 3v3 Bogotá Open', 3),
  (11, '/img/eventos/tekken-8-cdmx-clash-1.svg', 'Escenario principal - Tekken 8 CDMX Clash', 1),
  (11, '/img/eventos/tekken-8-cdmx-clash-2.svg', 'Zona de juego - Tekken 8 CDMX Clash', 2),
  (11, '/img/eventos/tekken-8-cdmx-clash-3.svg', 'Comunidad y premiación - Tekken 8 CDMX Clash', 3),
  (12, '/img/eventos/dota-2-andes-cup-1.svg', 'Escenario principal - Dota 2 Andes Cup Online', 1),
  (12, '/img/eventos/dota-2-andes-cup-2.svg', 'Zona de juego - Dota 2 Andes Cup Online', 2),
  (12, '/img/eventos/dota-2-andes-cup-3.svg', 'Comunidad y premiación - Dota 2 Andes Cup Online', 3),
  (13, '/img/eventos/clash-royale-vina-mobile-1.svg', 'Escenario principal - Clash Royale Viña Mobile Cup', 1),
  (13, '/img/eventos/clash-royale-vina-mobile-2.svg', 'Zona de juego - Clash Royale Viña Mobile Cup', 2),
  (13, '/img/eventos/clash-royale-vina-mobile-3.svg', 'Comunidad y premiación - Clash Royale Viña Mobile Cup', 3),
  (14, '/img/eventos/taller-minecraft-redstone-1.svg', 'Escenario principal - Taller presencial de Redstone en Minecraft', 1),
  (14, '/img/eventos/taller-minecraft-redstone-2.svg', 'Zona de juego - Taller presencial de Redstone en Minecraft', 2),
  (14, '/img/eventos/taller-minecraft-redstone-3.svg', 'Comunidad y premiación - Taller presencial de Redstone en Minecraft', 3),
  (15, '/img/eventos/speedrun-maraton-solidaria-1.svg', 'Escenario principal - Maratón Solidaria de Speedruns', 1),
  (15, '/img/eventos/speedrun-maraton-solidaria-2.svg', 'Zona de juego - Maratón Solidaria de Speedruns', 2),
  (15, '/img/eventos/speedrun-maraton-solidaria-3.svg', 'Comunidad y premiación - Maratón Solidaria de Speedruns', 3);

INSERT INTO banner (titulo, texto, imagen, activo, orden) VALUES
  ('HyperByte Store', '20% de descuento en periféricos con el código GAMEREVENTS', '/img/banners/hyperbyte.svg', TRUE, 1),
  ('NovaNet Fibra Gamer', 'Ping bajo y 1 Gbps simétrico para tus torneos online', '/img/banners/novanet.svg', TRUE, 2),
  ('Pixel Fest 2026', 'Entradas a la venta: 18 al 20 de diciembre en Santiago', '/img/banners/pixelfest.svg', TRUE, 3);
