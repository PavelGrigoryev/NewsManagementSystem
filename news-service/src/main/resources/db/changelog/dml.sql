--liquibase formatted sql

--changeset Grigoryev_Pavel:1
INSERT INTO news (id, time, title, text, email)
VALUES (1, '2023-06-14 10:30:00', 'Breaking: Volcano erupts in Hawaii',
        'A massive volcanic eruption has occurred on the Big Island of Hawaii, spewing lava and ash into the air. The eruption was triggered by a series of earthquakes that rocked the island in the past few days. Authorities have issued evacuation orders for nearby residents and warned of possible tsunamis and landslides.',
        'reporter@news.com'),
       (2, '2023-06-14 10:35:00', 'SpaceX launches Starship SN20 to orbit',
        'SpaceX has successfully launched its Starship SN20 vehicle to orbit for the first time, marking a major milestone for the company and the future of space exploration. The Starship SN20 lifted off from Boca Chica, Texas, at 10:15 a.m. local time and reached an altitude of about 200 kilometers before performing a flip maneuver and reentering the atmosphere. The Starship SN20 then landed safely on a floating platform in the Gulf of Mexico.',
        'space@news.com'),
       (3, '2023-06-14 10:40:00', 'Apple unveils iPhone 15 with holographic display',
        'Apple has announced its latest flagship smartphone, the iPhone 15, which features a revolutionary holographic display that projects 3D images in mid-air. The iPhone 15 also boasts a faster processor, a longer battery life, and a new design that is thinner and lighter than ever. The iPhone 15 will be available in stores starting from July 1st.',
        'tech@news.com'),
       (4, '2023-06-14 10:45:00', 'World Cup kicks off in Qatar',
        'The 2023 FIFA World Cup has officially begun in Qatar, with the host nation facing Brazil in the opening match. The World Cup, which is held every four years, is the most prestigious and popular soccer tournament in the world, attracting millions of fans and viewers from around the globe. The World Cup will last for a month, with 32 teams competing for the coveted trophy.',
        'sports@news.com'),
       (5, '2023-06-14 10:50:00', 'New study reveals the secrets of longevity',
        'A new study published in the journal Nature has revealed the secrets of longevity, or how to live longer and healthier lives. The study, which involved analyzing the genomes of over 10,000 centenarians, or people who live past 100 years old, found that they share certain genetic variants that protect them from age-related diseases such as cancer, diabetes, and Alzheimer''s. The study also identified some lifestyle factors that contribute to longevity, such as eating a balanced diet, exercising regularly, and maintaining social connections.',
        'health@news.com'),
       (6, '2023-06-14 10:55:00', 'New album by Adele breaks records',
        'The British singer-songwriter Adele has released her new album, titled 33, which has broken records for the most streams and sales in the first week. The album, which is her first since 2015, features 12 songs that explore themes of love, loss, and healing. The album has received critical acclaim and praise from fans and celebrities alike.',
        'music@news.com'),
       (7, '2023-06-14 11:00:00', 'Bitcoin reaches new all-time high',
        'The cryptocurrency Bitcoin has reached a new all-time high of $100,000 USD per coin, surpassing its previous record of $64,000 USD in April 2021. The surge in price is attributed to increased adoption and demand from institutional investors, as well as the launch of the first Bitcoin exchange-traded fund (ETF) in the US. Bitcoin is the most popular and valuable cryptocurrency in the world, with a market capitalization of over $1.8 trillion USD.',
        'finance@news.com'),
       (8, '2023-06-14 11:05:00', 'New dinosaur species discovered in Argentina',
        'A team of paleontologists from Argentina and Brazil have discovered a new species of dinosaur that lived about 90 million years ago. The dinosaur, named Argentinosaurus giganteus, is estimated to be the largest land animal ever to exist, measuring about 40 meters long and weighing about 100 tons. The dinosaur belongs to the group of sauropods, which are long-necked herbivorous dinosaurs.',
        'science@news.com'),
       (9, '2023-06-14 11:10:00', 'New York City hit by massive blackout',
        'A massive blackout has hit New York City, leaving millions of people without power and disrupting transportation and communication systems. The blackout was caused by a failure in the transmission grid that supplies electricity to the city. The authorities are working to restore power as soon as possible and to ensure public safety and order.',
        'city@news.com'),
       (10, '2023-06-14 11:15:00', 'Oprah Winfrey announces retirement from TV',
        'The legendary talk show host and media mogul Oprah Winfrey has announced that she will retire from TV after 40 years of career. Winfrey said that she will focus on her philanthropic and educational projects, as well as her own network, OWN. Winfrey is widely regarded as one of the most influential and inspiring women in the world, having interviewed countless celebrities, politicians, and ordinary people on her show.',
        'entertainment@news.com'),
       (11, '2023-06-14 11:20:00', 'New vaccine for malaria shows promise',
        'A new vaccine for malaria, developed by a team of researchers from the University of Oxford, has shown promising results in a clinical trial. The vaccine, which targets the parasite that causes malaria, was able to prevent infection in 77% of the participants, who were exposed to the parasite in a controlled setting. The vaccine is the first to achieve such a high level of efficacy and could potentially save millions of lives.',
        'science@news.com'),
       (12, '2023-06-14 11:25:00', 'New movie by Christopher Nolan wins Oscar',
        'The new movie by the acclaimed director Christopher Nolan, titled Inversion, has won the Oscar for Best Picture at the 95th Academy Awards. The movie, which is a sci-fi thriller that explores the concept of time reversal, also won Oscars for Best Director, Best Original Screenplay, Best Cinematography, and Best Visual Effects. The movie has been praised for its originality, complexity, and stunning visuals.',
        'entertainment@news.com'),
       (13, '2023-06-14 11:30:00', 'New law bans plastic bags in California',
        'A new law that bans the use of plastic bags in California has gone into effect on June 1st. The law, which aims to reduce plastic pollution and protect the environment, prohibits retailers from providing single-use plastic bags to customers and requires them to charge 10 cents for reusable or paper bags. The law also encourages consumers to bring their own bags or use alternative materials such as cloth or bamboo.',
        'environment@news.com'),
       (14, '2023-06-14 11:35:00', 'New book by J.K. Rowling sparks controversy',
        'The new book by the famous author J.K. Rowling, titled The Cursed Child, has sparked controversy among fans and critics alike. The book, which is a sequel to the Harry Potter series, follows the adventures of Harry''s son Albus and his friend Scorpius, who travel back in time to change the course of history. The book has been criticized for its plot inconsistencies, character development, and representation of minorities.',
        'books@news.com'),
       (15, '2023-06-14 11:40:00', 'New game by Nintendo breaks records',
        'The new game by the Japanese video game company Nintendo, titled Super Mario Odyssey, has broken records for the most sales and downloads in the first week. The game, which is a platformer that features Mario traveling across different worlds and using his hat to possess various objects and creatures, has sold over 10 million copies and has been downloaded over 50 million times. The game has received rave reviews from critics and gamers alike.',
        'games@news.com'),
       (16, '2023-06-14 11:45:00', 'New robot by Boston Dynamics goes viral',
        'The new robot by the American robotics company Boston Dynamics, named Spot, has gone viral on social media for its impressive abilities and cute appearance. The robot, which is a four-legged dog-like machine that can walk, run, jump, and climb stairs, has been featured in various videos and memes that showcase its skills and personality. The robot is designed for various applications such as research, entertainment, and security.',
        'tech@news.com'),
       (17, '2023-06-14 11:50:00', 'New diet by Dr. Oz claims to boost metabolism',
        'The new diet by the famous TV doctor Dr. Oz, called the Metabolism Miracle, claims to boost metabolism and help people lose weight fast. The diet, which consists of three phases that involve eating different types of foods and supplements, promises to increase the body''s ability to burn fat and calories. The diet has been endorsed by several celebrities and influencers who claim to have seen positive results.',
        'health@news.com'),
       (18, '2023-06-14 11:55:00', 'New painting by Banksy sells for millions',
        'The new painting by the mysterious street artist Banksy, titled The Girl with the Balloon Heart, has sold for millions at an auction in London. The painting, which depicts a girl holding a balloon shaped like a heart that is partially deflated, is a commentary on the state of the world and the loss of innocence. The painting also features a hidden shredder that activates when the painting is sold, creating a unique and controversial piece of art.',
        'art@news.com'),
       (19, '2023-06-14 12:00:00', 'New song by Ed Sheeran tops charts',
        'The new song by the popular singer-songwriter Ed Sheeran, titled Perfect Harmony, has topped the charts in several countries around the world. The song, which is a duet with his wife Cherry Seaborn, is a romantic ballad that expresses their love and happiness. The song has been praised for its melody, lyrics, and vocals.',
        'music@news.com'),
       (20, '2023-06-14 12:05:00', 'New scandal by Donald Trump shocks nation',
        'The new scandal by the former president Donald Trump, involving his alleged involvement in a bribery scheme with a foreign leader, has shocked the nation and sparked outrage. The scandal, which was revealed by a whistleblower who leaked a phone call transcript between Trump and the leader of Ukraine, shows that Trump asked the leader to investigate his political rival Joe Biden in exchange for military aid. The scandal has led to calls for impeachment and criminal charges against Trump.',
        'politics@news.com');

SELECT setval(' news_id_seq ', (SELECT max(id) FROM news));

INSERT INTO comments (id, time, text, username, email, news_id)
VALUES (1, '2023-06-14 10:31:00', 'Wow! That''s scary! I hope everyone is safe!', 'LavaLover', 'lavalover@gmail.com',
        1),
       (2, '2023-06-14 10:32:00', 'I live in Hawaii and I can see the smoke from my window. It''s crazy!', 'Aloha123',
        'aloha123@yahoo.com', 1),
       (3, '2023-06-14 10:33:00',
        'This is why we need to stop global warming. Volcanoes are getting more active because of human activities.',
        'GreenPeace', 'greenpeace@outlook.com', 1),
       (4, '2023-06-14 10:34:00',
        'I wonder if this will affect the flights to Hawaii. I have a vacation planned next week.', 'TravelBug',
        'travelbug@hotmail.com', 1),
       (5, '2023-06-14 10:35:00', 'This is so cool! I wish I could see it in person!', 'VolcanoFan',
        'volcanofan@gmail.com', 1),
       (6, '2023-06-14 10:36:00', 'Wow! That''s amazing! Go SpaceX!', 'RocketMan', 'rocketman@gmail.com', 2),
       (7, '2023-06-14 10:37:00', 'This is a historic moment for humanity. Starship is the future of space travel.',
        'SpaceFan', 'spacefan@yahoo.com', 2),
       (8, '2023-06-14 10:38:00', 'I''m so proud of Elon Musk and his team. They are making history.', 'Musketeer',
        'musketeer@outlook.com', 2),
       (9, '2023-06-14 10:39:00', 'I wonder when they will send people to Mars. I would love to be one of them.',
        'MarsLover', 'marslover@hotmail.com', 2),
       (10, '2023-06-14 10:40:00', 'This is so exciting! I can''t wait to see more Starship launches!', 'SpaceXFan',
        'spacexfan@gmail.com', 2),
       (11, '2023-06-14 10:41:00', 'Wow! That''s awesome! I want an iPhone 15!', 'AppleFan', 'applefan@gmail.com', 3),
       (12, '2023-06-14 10:42:00', 'This is so cool! A holographic display? That''s like sci-fi!', 'TechLover',
        'techlover@yahoo.com', 3),
       (13, '2023-06-14 10:43:00', 'This is so lame. Apple is just copying Samsung. They have nothing new to offer.',
        'AndroidFan', 'androidfan@outlook.com', 3),
       (14, '2023-06-14 10:44:00', 'This is so expensive. Who can afford this?', 'BudgetUser', 'budgetuser@hotmail.com',
        3),
       (15, '2023-06-14 10:45:00', 'This is so unnecessary. Who needs a holographic display?', 'SimpleUser',
        'simpleuser@gmail.com', 3),
       (16, '2023-06-14 10:46:00', 'Wow! That''s awesome! I love soccer!', 'SoccerFan', 'soccerfan@gmail.com', 4),
       (17, '2023-06-14 10:47:00', 'This is so cool! I''m rooting for Brazil!', 'BrazilFan', 'brazilfan@yahoo.com', 4),
       (18, '2023-06-14 10:48:00', 'This is so lame. Qatar is a terrible host. They have no respect for human rights.',
        'HumanRights', 'humanrights@outlook.com', 4),
       (19, '2023-06-14 10:49:00', 'This is so boring. Soccer is a stupid sport.', 'SoccerHater',
        'soccerhater@hotmail.com',
        4),
       (20, '2023-06-14 10:50:00', 'This is so exciting! I can''t wait to watch the games!', 'WorldCupFan',
        'worldcupfan@gmail.com', 4),
       (21, '2023-06-14 10:51:00', 'Wow! That''s amazing! I want to live longer and healthier!', 'HealthFan',
        'healthfan@gmail.com', 5),
       (22, '2023-06-14 10:52:00', 'This is so cool! I wonder if I have the longevity genes.', 'GeneLover',
        'genelover@yahoo.com', 5),
       (23, '2023-06-14 10:53:00', 'This is so lame. Life is already too long and boring.', 'LifeHater',
        'lifehater@outlook.com', 5),
       (24, '2023-06-14 10:54:00', 'This is so scary. What if the world gets overpopulated?', 'EcoWarrior',
        'ecowarrior@hotmail.com', 5),
       (25, '2023-06-14 10:55:00', 'This is so interesting! I want to read the full study.', 'ScienceFan',
        'sciencefan@gmail.com', 5),
       (26, '2023-06-14 10:56:00', 'Wow! That''s awesome! I love Adele!', 'AdeleFan', 'adelefan@gmail.com', 6),
       (27, '2023-06-14 10:57:00', 'This is so beautiful! Her voice is amazing!', 'MusicLover', 'musiclover@yahoo.com',
        6),
       (28, '2023-06-14 10:58:00', 'This is so boring. Adele is overrated.', 'MusicHater', 'musichater@outlook.com', 6),
       (29, '2023-06-14 10:59:00', 'This is so sad. Her songs make me cry.', 'EmoUser', 'emouser@hotmail.com', 6),
       (30, '2023-06-14 11:00:00', 'This is so inspiring! She is a role model for me!', 'AdeleLover',
        'adelelover@gmail.com', 6),
       (31, '2023-06-14 11:01:00', 'Wow! That''s amazing! I wish I had some Bitcoin!', 'BitcoinFan',
        'bitcoinfan@gmail.com',
        7),
       (32, '2023-06-14 11:02:00', 'This is so cool! Bitcoin is the future of money!', 'CryptoLover',
        'cryptolover@yahoo.com', 7),
       (33, '2023-06-14 11:03:00', 'This is so lame. Bitcoin is a scam.', 'CryptoHater', 'cryptohater@outlook.com', 7),
       (34, '2023-06-14 11:04:00', 'This is so risky. Bitcoin is too volatile.', 'RiskAverse', 'riskaverse@hotmail.com',
        7),
       (35, '2023-06-14 11:05:00', 'This is so confusing. How does Bitcoin work?', 'CryptoNewbie',
        'cryptonewbie@gmail.com',
        7),
       (36, '2023-06-14 11:06:00', 'Wow! That''s awesome! I love dinosaurs!', 'DinoFan', 'dinofan@gmail.com', 8),
       (37, '2023-06-14 11:07:00', 'This is so cool! I wonder what it looked like.', 'DinoLover', 'dinolover@yahoo.com',
        8),
       (38, '2023-06-14 11:08:00', 'This is so lame. Dinosaurs are extinct.', 'DinoHater', 'dinohater@outlook.com', 8),
       (39, '2023-06-14 11:09:00', 'This is so scary. What if they come back?', 'DinoPhobe', 'dinophobe@hotmail.com',
        8),
       (40, '2023-06-14 11:10:00', 'This is so interesting! I want to learn more about dinosaurs.', 'DinoNerd',
        'dinonerd@gmail.com', 8),
       (41, '2023-06-14 11:11:00', 'Wow! That''s terrible! I hope everyone is okay!', 'NYCFan', 'nycfan@gmail.com', 9),
       (42, '2023-06-14 11:12:00', 'This is so crazy! I can''t believe this happened!', 'NYCLover',
        'nyclover@yahoo.com',
        9),
       (43, '2023-06-14 11:13:00', 'This is so funny. New York deserves this.', 'NYCHater', 'nychater@outlook.com', 9),
       (44, '2023-06-14 11:14:00', 'This is so annoying. I have a meeting in New York today.', 'NYCUser',
        'nycuser@hotmail.com', 9),
       (45, '2023-06-14 11:15:00', 'This is so suspicious. Who is behind this?', 'NYCConspiracy',
        'nycconspiracy@gmail.com',
        9),
       (46, '2023-06-14 11:16:00', 'Wow! That''s sad! I will miss her!', 'OprahFan', 'oprahfan@gmail.com', 10),
       (47, '2023-06-14 11:17:00', 'This is so nice! She deserves a break!', 'OprahLover', 'oprahlover@yahoo.com', 10),
       (48, '2023-06-14 11:18:00', 'This is so stupid. Oprah is irrelevant.', 'OprahHater', 'oprahater@outlook.com',
        10),
       (49, '2023-06-14 11:19:00', 'This is so surprising. I thought she would never retire.', 'OprahUser',
        'oprahuser@hotmail.com', 10),
       (50, '2023-06-14 11:20:00', 'This is so inspiring! She is a legend!', 'OprahLover2', 'oprahlover2@gmail.com',
        10),
       (51, '2023-06-14 11:21:00', 'Wow! That''s amazing! I hope this vaccine becomes available soon!', 'HealthFan2',
        'healthfan2@gmail.com', 11),
       (52, '2023-06-14 11:22:00', 'This is so cool! I wonder how they made the vaccine.', 'ScienceLover2',
        'sciencelover2@yahoo.com', 11),
       (53, '2023-06-14 11:23:00', 'This is so lame. Malaria is not a big deal.', 'HealthHater',
        'healthhater@outlook.com',
        11),
       (54, '2023-06-14 11:24:00', 'This is so risky. What if the vaccine has side effects?', 'RiskAverse2',
        'riskaverse2@hotmail.com', 11),
       (55, '2023-06-14 11:25:00', 'This is so interesting! I want to learn more about malaria.', 'ScienceFan2',
        'sciencefan2@gmail.com', 11),
       (56, '2023-06-14 11:26:00', 'Wow! That''s awesome! I love Christopher Nolan!', 'NolanFan', 'nolanfan@gmail.com',
        12),
       (57, '2023-06-14 11:27:00', 'This is so cool! I can''t wait to watch the movie.', 'MovieLover',
        'movielover@yahoo.com', 12),
       (58, '2023-06-14 11:28:00', 'This is so lame. Christopher Nolan is overrated.', 'MovieHater',
        'moviehater@outlook.com', 12),
       (59, '2023-06-14 11:29:00', 'This is so confusing. I don''t understand the movie.', 'MovieUser',
        'movieuser@hotmail.com', 12),
       (60, '2023-06-14 11:30:00', 'This is so inspiring! He is a genius!', 'NolanLover', 'nolanlover@gmail.com', 12),
       (61, '2023-06-14 11:31:00', 'Wow! That''s great! I hate plastic bags!', 'EcoFan', 'ecofan@gmail.com', 13),
       (62, '2023-06-14 11:32:00', 'This is so nice! California is leading the way!', 'EcoLover', 'ecolover@yahoo.com',
        13),
       (63, '2023-06-14 11:33:00', 'This is so stupid. Plastic bags are convenient.', 'EcoHater',
        'ecohater@outlook.com',
        13),
       (64, '2023-06-14 11:34:00', 'This is so annoying. I don''t want to pay for bags.', 'EcoUser',
        'ecouser@hotmail.com',
        13),
       (65, '2023-06-14 11:35:00', 'This is so interesting! I wonder how this will affect the economy.', 'EcoNerd',
        'econerd@gmail.com', 13),
       (66, '2023-06-14 11:36:00', 'Wow! That''s sad! I love J.K. Rowling!', 'RowlingFan', 'rowlingfan@gmail.com', 14),
       (67, '2023-06-14 11:37:00', 'This is so nice! She has the right to write whatever she wants!', 'RowlingLover',
        'rowlinglover@yahoo.com', 14),
       (68, '2023-06-14 11:38:00', 'This is so stupid. J.K. Rowling is a bigot.', 'RowlingHater',
        'rowlinghater@outlook.com', 14),
       (69, '2023-06-14 11:39:00', 'This is so surprising. I thought she was a good writer.', 'RowlingUser',
        'rowlinguser@hotmail.com', 14),
       (70, '2023-06-14 11:40:00', 'This is so inspiring! She is a legend!', 'RowlingLover2', 'rowlinglover2@gmail.com',
        14),
       (71, '2023-06-14 11:41:00', 'Wow! That''s awesome! I love Nintendo!', 'NintendoFan', 'nintendofan@gmail.com',
        15),
       (72, '2023-06-14 11:42:00', 'This is so cool! I want to play the game.', 'GameLover', 'gamelover@yahoo.com', 15),
       (73, '2023-06-14 11:43:00', 'This is so lame. Nintendo is childish.', 'GameHater', 'gamehater@outlook.com', 15),
       (74, '2023-06-14 11:44:00', 'This is so expensive. Who can afford this?', 'GameUser', 'gameuser@hotmail.com',
        15),
       (75, '2023-06-14 11:45:00', 'This is so interesting! I wonder how they made the game.',
        'GameNerd', 'gamenerd@gmail.com', 15),
       (76, '2023-06-14 11:46:00', 'Wow! That''s awesome! I want a robot like that!', 'RobotFan', 'robotfan@gmail.com',
        16),
       (77, '2023-06-14 11:47:00', 'This is so cool! Spot is so cute and smart!', 'RobotLover', 'robotlover@yahoo.com',
        16),
       (78, '2023-06-14 11:48:00', 'This is so lame. Robots are taking over the world.', 'RobotHater',
        'robothater@outlook.com', 16),
       (79, '2023-06-14 11:49:00', 'This is so scary. What if Spot turns evil?', 'RobotPhobe', 'robotphobe@hotmail.com',
        16),
       (80, '2023-06-14 11:50:00', 'This is so interesting! I wonder how they made Spot.', 'RobotNerd',
        'robotnerd@gmail.com', 16),
       (81, '2023-06-14 11:51:00', 'Wow! That''s amazing! I want to try this diet!', 'DietFan', 'dietfan@gmail.com',
        17),
       (82, '2023-06-14 11:52:00', 'This is so nice! Dr. Oz is so helpful!', 'DietLover', 'dietlover@yahoo.com', 17),
       (83, '2023-06-14 11:53:00', 'This is so stupid. Dr. Oz is a quack.', 'DietHater', 'diethater@outlook.com', 17),
       (84, '2023-06-14 11:54:00', 'This is so risky. This diet is unhealthy.', 'DietUser', 'dietuser@hotmail.com', 17),
       (85, '2023-06-14 11:55:00', 'This is so interesting! I wonder how this diet works.', 'DietNerd',
        'dietnerd@gmail.com', 17),
       (86, '2023-06-14 11:56:00', 'Wow! That''s awesome! I love Banksy!', 'BanksyFan', 'banksyfan@gmail.com', 18),
       (87, '2023-06-14 11:57:00', 'This is so cool! The painting is so meaningful!', 'BanksyLover',
        'banksylover@yahoo.com', 18),
       (88, '2023-06-14 11:58:00', 'This is so lame. Banksy is overrated.', 'BanksyHater', 'banksyhater@outlook.com',
        18),
       (89, '2023-06-14 11:59:00', 'This is so surprising. I thought the painting was destroyed.', 'BanksyUser',
        'banksyuser@hotmail.com', 18),
       (90, '2023-06-14 12:00:00', 'This is so inspiring! He is a genius!', 'BanksyLover2', 'banksylover2@gmail.com',
        18),
       (91, '2023-06-14 12:01:00', 'Wow! That''s awesome! I love Ed Sheeran!', 'SheeranFan', 'sheeranfan@gmail.com',
        19),
       (92, '2023-06-14 12:02:00', 'This is so cool! The song is so beautiful!', 'SheeranLover',
        'sheeranlover@yahoo.com',
        19),
       (93, '2023-06-14 12:03:00', 'This is so lame. Ed Sheeran is boring.', 'SheeranHater', 'sheeranhater@outlook.com',
        19),
       (94, '2023-06-14 12:04:00', 'This is so sad. The song makes me cry.', 'SheeranUser', 'sheeranuser@hotmail.com',
        19),
       (95, '2023-06-14 12:05:00', 'This is so inspiring! He is a legend!', 'SheeranLover2', 'sheeranlover2@gmail.com',
        19),
       (96, '2023-06-14 12:06:00', 'Wow! That''s terrible! I hate Donald Trump!', 'TrumpFan', 'trumpfan@gmail.com', 20),
       (97, '2023-06-14 12:07:00', 'This is so nice! He deserves to be impeached!', 'TrumpLover',
        'trumplover@yahoo.com',
        20),
       (98, '2023-06-14 12:08:00', 'This is so stupid. Donald Trump is innocent.', 'TrumpHater',
        'trumphater@outlook.com',
        20),
       (99, '2023-06-14 12:09:00', 'This is so annoying. I don''t care about politics.', 'TrumpUser',
        'trumpuser@hotmail.com', 20),
       (100, '2023-06-14 12:10:00', 'This is so interesting! I wonder what will happen next.',
        'TrumpNerd', 'trumpnerd@gmail.com', 20),
       (101, '2023-06-14 10:36:00', 'This is so awesome! I hope they find a cure for malaria soon!', 'MalariaFan',
        'malariafan@gmail.com', 11),
       (102, '2023-06-14 10:37:00', 'This is so cool! I wonder how they tested the vaccine.', 'MalariaLover',
        'malarialover@yahoo.com', 11),
       (103, '2023-06-14 10:38:00', 'This is so lame. Malaria is a hoax.', 'MalariaHater', 'malariahater@outlook.com',
        11),
       (104, '2023-06-14 10:39:00', 'This is so risky. What if the vaccine causes autism?', 'MalariaPhobe',
        'malariaphobe@hotmail.com', 11),
       (105, '2023-06-14 10:40:00', 'This is so interesting! I want to read the full paper.', 'MalariaNerd',
        'malarianerd@gmail.com', 11),
       (106, '2023-06-14 10:41:00', 'Wow! That''s awesome! I love Christopher Nolan!', 'NolanFan2',
        'nolanfan2@gmail.com',
        12),
       (107, '2023-06-14 10:42:00', 'This is so cool! I wonder how they filmed the movie.', 'NolanLover2',
        'nolanlover2@yahoo.com', 12),
       (108, '2023-06-14 10:43:00', 'This is so lame. Christopher Nolan is boring.', 'NolanHater2',
        'nolanhater2@outlook.com', 12),
       (109, '2023-06-14 10:44:00', 'This is so confusing. I don''t get the movie.', 'NolanUser2',
        'nolanuser2@hotmail.com',
        12),
       (110, '2023-06-14 10:45:00', 'This is so inspiring! He is a genius!', 'NolanLover3', 'nolanlover3@gmail.com',
        12),
       (111, '2023-06-14 10:46:00', 'Wow! That''s great! I love the environment!', 'EcoFan2', 'ecofan2@gmail.com', 13),
       (112, '2023-06-14 10:47:00', 'This is so nice! California is doing the right thing!', 'EcoLover2',
        'ecolover2@yahoo.com', 13),
       (113, '2023-06-14 10:48:00', 'This is so stupid. Plastic bags are useful.', 'EcoHater2', 'ecohater2@outlook.com',
        13),
       (114, '2023-06-14 10:49:00', 'This is so annoying. I don''t like paying for bags.', 'EcoUser2',
        'ecouser2@hotmail.com', 13),
       (115, '2023-06-14 10:50:00', 'This is so interesting! I wonder how this will affect the wildlife.',
        'EcoNerd2', 'econerd2@gmail.com', 13),
       (116, '2023-06-14 10:51:00', 'Wow! That''s sad! I love J.K. Rowling!', 'RowlingFan2', 'rowlingfan2@gmail.com',
        14),
       (117, '2023-06-14 10:52:00', 'This is so nice! She can write whatever she wants!',
        'RowlingLover3', 'rowlinglover3@yahoo.com', 14),
       (118, '2023-06-14 10:53:00', 'This is so stupid. J.K. Rowling is a fraud.',
        'RowlingHater2', 'rowlinghater2@outlook.com', 14),
       (119, '2023-06-14 10:54:00', 'This is so surprising. I thought she was a better writer.',
        'RowlingUser2', 'rowlinguser2@hotmail.com', 14),
       (120, '2023-06-14 10:55:00', 'This is so inspiring! She is a legend!', 'RowlingLover4',
        'rowlinglover4@gmail.com',
        14),
       (121, '2023-06-14 10:56:00', 'Wow! That''s awesome! I love Nintendo!', 'NintendoFan2', 'nintendofan2@gmail.com',
        15),
       (122, '2023-06-14 10:57:00', 'This is so cool! I want to play the game.', 'GameLover2', 'gamelover2@yahoo.com',
        15),
       (123, '2023-06-14 10:58:00', 'This is so lame. Nintendo is outdated.', 'GameHater2', 'gamehater2@outlook.com',
        15),
       (124, '2023-06-14 10:59:00', 'This is so expensive. Who can afford this?', 'GameUser2', 'gameuser2@hotmail.com',
        15),
       (125, '2023-06-14 11:00:00', 'This is so interesting! I wonder how they made the game.',
        'GameNerd2', 'gamenerd2@gmail.com', 15),
       (126, '2023-06-14 11:46:00', 'Wow! That''s awesome! I want a robot like that!', 'RobotFan2',
        'robotfan2@gmail.com',
        16),
       (127, '2023-06-14 11:47:00', 'This is so cool! Spot is so cute and smart!', 'RobotLover2',
        'robotlover2@yahoo.com',
        16),
       (128, '2023-06-14 11:48:00', 'This is so lame. Robots are taking over the world.', 'RobotHater2',
        'robothater2@outlook.com', 16),
       (129, '2023-06-14 11:49:00', 'This is so scary. What if Spot turns evil?', 'RobotPhobe2',
        'robotphobe2@hotmail.com',
        16),
       (130, '2023-06-14 11:50:00', 'This is so interesting! I wonder how they made Spot.', 'RobotNerd2',
        'robotnerd2@gmail.com', 16),
       (131, '2023-06-14 11:51:00', 'Wow! That''s amazing! I want to try this diet!', 'DietFan2', 'dietfan2@gmail.com',
        17),
       (132, '2023-06-14 11:52:00', 'This is so nice! Dr. Oz is so helpful!', 'DietLover2', 'dietlover2@yahoo.com', 17),
       (133, '2023-06-14 11:53:00', 'This is so stupid. Dr. Oz is a quack.', 'DietHater2', 'diethater2@outlook.com',
        17),
       (134, '2023-06-14 11:54:00', 'This is so risky. This diet is unhealthy.', 'DietUser2', 'dietuser2@hotmail.com',
        17),
       (135, '2023-06-14 11:55:00', 'This is so interesting! I wonder how this diet works.', 'DietNerd2',
        'dietnerd2@gmail.com', 17),
       (136, '2023-06-14 11:56:00', 'Wow! That''s awesome! I love Banksy!', 'BanksyFan2', 'banksyfan2@gmail.com', 18),
       (137, '2023-06-14 11:57:00', 'This is so cool! The painting is so meaningful!',
        'BanksyLover2', 'banksylover2@yahoo.com', 18),
       (138, '2023-06-14 11:58:00', 'This is so lame. Banksy is overrated.', 'BanksyHater2', 'banksyhater2@outlook.com',
        18),
       (139, '2023-06-14 11:59:00', 'This is so surprising. I thought the painting was destroyed.',
        'BanksyUser2', 'banksyuser2@hotmail.com', 18),
       (140, '2023-06-14 12:00:00', 'This is so inspiring! He is a genius!', 'BanksyLover3', 'banksylover3@gmail.com',
        18),
       (141, '2023-06-14 12:01:00', 'Wow! That''s awesome! I love Ed Sheeran!', 'SheeranFan2', 'sheeranfan2@gmail.com',
        19),
       (142, '2023-06-14 12:02:00', 'This is so cool! The song is so beautiful!',
        'SheeranLover2', 'sheeranlover2@yahoo.com',
        19),
       (143, '2023-06-14 12:03:00', 'This is so lame. Ed Sheeran is boring.',
        'SheeranHater2', 'sheeranhater2@outlook.com',
        19),
       (144, '2023-06-14 12:04:00', 'This is so sad. The song makes me cry.', 'SheeranUser2',
        'sheeranuser2@hotmail.com',
        19),
       (145, '2023-06-14 12:05:00', 'This is so inspiring! He is a legend!', 'SheeranLover3', 'sheeranlover3@gmail.com',
        19),
       (146, '2023-06-14 12:06:00', 'Wow! That''s terrible! I hate Donald Trump!', 'TrumpFan2', 'trumpfan2@gmail.com',
        20),
       (147, '2023-06-14 12:07:00', 'This is so nice! He deserves to be impeached!',
        'TrumpLover2', 'trumplover2@yahoo.com',
        20),
       (148, '2023-06-14 12:08:00', 'This is so stupid. Donald Trump is innocent.',
        'TrumpHater2', 'trumphater2@outlook.com',
        20),
       (149, '2023-06-14 12:09:00', 'This is so annoying. I don''t care about politics.',
        'TrumpUser2', 'trumpuser2@hotmail.com', 20),
       (150, '2023-06-14 12:10:00', 'This is so interesting! I wonder what will happen next.',
        'TrumpNerd2', 'trumpnerd2@gmail.com', 20),
       (151, '2023-06-14 10:56:00', 'Wow! That''s awesome! I love Adele!', 'AdeleFan2', 'adelefan2@gmail.com', 6),
       (152, '2023-06-14 10:57:00', 'This is so beautiful! Her voice is amazing!', 'MusicLover2',
        'musiclover2@yahoo.com',
        6),
       (153, '2023-06-14 10:58:00', 'This is so boring. Adele is overrated.', 'MusicHater2', 'musichater2@outlook.com',
        6),
       (154, '2023-06-14 10:59:00', 'This is so sad. Her songs make me cry.', 'EmoUser2', 'emouser2@hotmail.com', 6),
       (155, '2023-06-14 11:00:00', 'This is so inspiring! She is a role model for me!', 'AdeleLover2',
        'adelelover2@gmail.com', 6),
       (156, '2023-06-14 11:01:00', 'Wow! That''s amazing! I wish I had some Bitcoin!', 'BitcoinFan2',
        'bitcoinfan2@gmail.com', 7),
       (157, '2023-06-14 11:02:00', 'This is so cool! Bitcoin is the future of money!', 'CryptoLover2',
        'cryptolover2@yahoo.com', 7),
       (158, '2023-06-14 11:03:00', 'This is so lame. Bitcoin is a scam.', 'CryptoHater2', 'cryptohater2@outlook.com',
        7),
       (159, '2023-06-14 11:04:00', 'This is so risky. Bitcoin is too volatile.', 'RiskAverse3',
        'riskaverse3@hotmail.com',
        7),
       (160, '2023-06-14 11:05:00', 'This is so confusing. How does Bitcoin work?', 'CryptoNewbie2',
        'cryptonewbie2@gmail.com', 7),
       (161, '2023-06-14 11:06:00', 'Wow! That''s awesome! I love dinosaurs!', 'DinoFan2', 'dinofan2@gmail.com', 8),
       (162, '2023-06-14 11:07:00', 'This is so cool! I wonder what it looked like.', 'DinoLover2',
        'dinolover2@yahoo.com',
        8),
       (163, '2023-06-14 11:08:00', 'This is so lame. Dinosaurs are extinct.', 'DinoHater2', 'dinohater2@outlook.com',
        8),
       (164, '2023-06-14 11:09:00', 'This is so scary. What if they come back?', 'DinoPhobe2', 'dinophobe2@hotmail.com',
        8),
       (165, '2023-06-14 11:10:00', 'This is so interesting! I want to learn more about dinosaurs.', 'DinoNerd2',
        'dinonerd2@gmail.com', 8),
       (166, '2023-06-14 11:11:00', 'Wow! That''s terrible! I hope everyone is okay!', 'NYCFan2', 'nycfan2@gmail.com',
        9),
       (167, '2023-06-14 11:12:00', 'This is so crazy! I can''t believe this happened!', 'NYCLover2',
        'nyclover2@yahoo.com',
        9),
       (168, '2023-06-14 11:13:00', 'This is so funny. New York deserves this.', 'NYCHater2', 'nychater2@outlook.com',
        9),
       (169, '2023-06-14 11:14:00', 'This is so annoying. I have a meeting in New York today.', 'NYCUser2',
        'nycuser2@hotmail.com', 9),
       (170, '2023-06-14 11:15:00', 'This is so suspicious. Who is behind this?', 'NYCConspiracy2',
        'nycconspiracy2@gmail.com', 9),
       (171, '2023-06-14 11:16:00', 'Wow! That''s sad! I will miss her!', 'OprahFan2', 'oprahfan2@gmail.com', 10),
       (172, '2023-06-14 11:17:00', 'This is so nice! She deserves a break!', 'OprahLover3', 'oprahlover3@yahoo.com',
        10),
       (173, '2023-06-14 11:18:00', 'This is so stupid. Oprah is irrelevant.', 'OprahHater2', 'oprahater2@outlook.com',
        10),
       (174, '2023-06-14 11:19:00', 'This is so surprising. I thought she would never retire.', 'OprahUser2',
        'oprahuser2@hotmail.com', 10),
       (175, '2023-06-14 11:20:00', 'This is so inspiring! She is a legend!', 'OprahLover4', 'oprahlover4@gmail.com',
        10),
       (176, '2023-06-14 10:31:00', 'Wow! That''s scary! I hope everyone is safe!', 'HawaiiFan', 'hawaiifan@gmail.com',
        1),
       (177, '2023-06-14 10:32:00', 'This is so cool! I wish I could see the volcano!', 'VolcanoLover',
        'volcanolover@yahoo.com', 1),
       (178, '2023-06-14 10:33:00', 'This is so lame. Volcanoes are boring.', 'VolcanoHater',
        'volcanohater@outlook.com',
        1),
       (179, '2023-06-14 10:34:00', 'This is so sad. I have family in Hawaii.', 'HawaiiUser', 'hawaiiuser@hotmail.com',
        1),
       (180, '2023-06-14 10:35:00', 'This is so interesting! I want to learn more about volcanoes.', 'VolcanoNerd',
        'volcanonerd@gmail.com', 1),
       (181, '2023-06-14 10:36:00', 'Wow! That''s amazing! I love SpaceX!', 'SpaceFan', 'spacefan@gmail.com', 2),
       (182, '2023-06-14 10:37:00', 'This is so cool! I wonder how they built the Starship!', 'SpaceLover',
        'spacelover@yahoo.com', 2),
       (183, '2023-06-14 10:38:00', 'This is so lame. SpaceX is a waste of money.', 'SpaceHater',
        'spacehater@outlook.com',
        2),
       (184, '2023-06-14 10:39:00', 'This is so risky. What if the Starship explodes?', 'SpacePhobe',
        'spacephobe@hotmail.com', 2),
       (185, '2023-06-14 10:40:00', 'This is so interesting! I want to go to space someday.', 'SpaceNerd',
        'spacenerd@gmail.com', 2),
       (186, '2023-06-14 10:41:00', 'Wow! That''s awesome! I want an iPhone 15!', 'iPhoneFan', 'iphonefan@gmail.com',
        3),
       (187, '2023-06-14 10:42:00', 'This is so cool! The holographic display is amazing!', 'iPhoneLover',
        'iphonelover@yahoo.com', 3),
       (188, '2023-06-14 10:43:00', 'This is so lame. iPhone is overpriced.', 'iPhoneHater', 'iphonehater@outlook.com',
        3),
       (189, '2023-06-14 10:44:00', 'This is so annoying. I just bought an iPhone 14.', 'iPhoneUser',
        'iphoneuser@hotmail.com', 3),
       (190, '2023-06-14 10:45:00', 'This is so interesting! I wonder how they made the holographic display.',
        'iPhoneNerd',
        'iphonenerd@gmail.com', 3),
       (191, '2023-06-14 10:46:00', 'Wow! That''s awesome! I love soccer!', 'SoccerFan', 'soccerfan@gmail.com', 4),
       (192, '2023-06-14 10:47:00', 'This is so cool! I can''t wait to watch the World Cup!', 'SoccerLover',
        'soccerlover@yahoo.com', 4),
       (193, '2023-06-14 10:48:00', 'This is so lame. Soccer is boring.', 'SoccerHater', 'soccerhater@outlook.com', 4),
       (194, '2023-06-14 10:49:00', 'This is so sad. My team didn''t qualify for the World Cup.', 'SoccerUser',
        'socceruser@hotmail.com', 4),
       (195, '2023-06-14 10:50:00', 'This is so interesting! I wonder who will win the World Cup.', 'SoccerNerd',
        'soccernerd@gmail.com', 4),
       (196, '2023-06-14 10:51:00', 'Wow! That''s awesome! I want to live longer and healthier!', 'LongevityFan',
        'longevityfan@gmail.com', 5),
       (197, '2023-06-14 10:52:00', 'This is so nice! The secrets of longevity are fascinating!', 'LongevityLover',
        'longevitylover@yahoo.com', 5),
       (198, '2023-06-14 10:53:00', 'This is so stupid. Longevity is a myth.', 'LongevityHater',
        'longevityhater@outlook.com', 5),
       (199, '2023-06-14 10:54:00', 'This is so risky. What if there are side effects?', 'LongevityUser',
        'longevityuser@hotmail.com', 5),
       (200, '2023-06-14 10:55:00', 'This is so interesting! I want to read the full study.', 'LongevityNerd',
        'longevitynerd@gmail.com', 5);

SELECT setval('comments_id_seq', (SELECT max(id) FROM comments));
