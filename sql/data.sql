insert into customers(name, role) values
('Akdana', 'ADMIN'),
('Abyaly', 'MANAGER'),
('Sultan-Ali', 'CUSTOMER');

insert into comics(title, price, stock, category, story) values
('Goodnight Punpun Vol. 1', 6000, 4, 'Seinen', 'A quiet, unsettling coming-of-age story about Punpun...'),
('Chainsaw Man Vol. 1', 5500, 6, 'Shonen', 'Denji wants a normal life, but debt and devils...'),
('Berserk Vol. 1', 6200, 3, 'Seinen', 'Guts, a lone swordsman with a cursed fate...'),

('Dorohedoro Vol. 1', 5800, 5, 'Seinen', 'In the chaotic city called Hole, Caiman wakes up...'),
('Jujutsu Kaisen Vol. 1', 5400, 8, 'Shonen', 'Yuji Itadori swallows a cursed object...'),
('Spy x Family Vol. 1', 5200, 7, 'Comedy', 'Agent Twilight must build a fake family...'),

('Uzumaki Vol. 1', 5900, 5, 'Horror', 'In a small town cursed by spirals...'),
('Another Vol. 1', 5600, 4, 'Horror', 'A transfer student enters a class haunted...'),

('Neon Genesis Evangelion Vol. 1', 6100, 6, 'Mecha', 'Teenagers are forced to pilot giant bio-mechanical machines...'),
('Mobile Suit Gundam: The Origin Vol. 1', 6300, 3, 'Mecha', 'War breaks out between space colonies and Earth...'),

('Re:Zero − Starting Life in Another World Vol. 1', 5700, 6, 'Isekai', 'Subaru Natsuki is suddenly transported to another world...');

insert into comic_chapters(comic_id, chapter_number, title, text) values
((select id from comics where title='Re:Zero − Starting Life in Another World Vol. 1'), 1, 'Arrival',
'Subaru Natsuki suddenly finds himself in another world...'),
((select id from comics where title='Re:Zero − Starting Life in Another World Vol. 1'), 2, 'Return by Death',
'After dying, Subaru wakes up at the same moment again...'),
((select id from comics where title='Re:Zero − Starting Life in Another World Vol. 1'), 3, 'Choice',
'Knowing what will happen, Subaru tries to change fate...'),

((select id from comics where title='Uzumaki Vol. 1'), 1, 'Spiral Obsession',
'People in Kurouzu Town begin to notice strange spiral patterns...'),
((select id from comics where title='Uzumaki Vol. 1'), 2, 'Twisted Bodies',
'The curse deepens as human bodies start to twist unnaturally...'),

((select id from comics where title='Another Vol. 1'), 1, 'The Transfer Student',
'A new student joins Class 3-3 and senses something is wrong...'),
((select id from comics where title='Another Vol. 1'), 2, 'Death Begins',
'A sudden accident shocks the class...'),

((select id from comics where title='Neon Genesis Evangelion Vol. 1'), 1, 'Summoned to Tokyo-3',
'Shinji Ikari arrives in a city under attack...'),
((select id from comics where title='Neon Genesis Evangelion Vol. 1'), 2, 'Inside the Eva',
'The cockpit closes. Fear overwhelms Shinji...'),

((select id from comics where title='Mobile Suit Gundam: The Origin Vol. 1'), 1, 'War Begins',
'War erupts between Earth and the space colonies...'),
((select id from comics where title='Mobile Suit Gundam: The Origin Vol. 1'), 2, 'Mobile Suit Pilot',
'A young boy enters the cockpit of a mobile suit...'),

((select id from comics where title='Goodnight Punpun Vol. 1'), 1, 'Punpun',
'Punpun is an ordinary boy living with his family...'),
((select id from comics where title='Goodnight Punpun Vol. 1'), 2, 'Growing Up',
'Punpun begins to realize that adults are flawed...'),
((select id from comics where title='Goodnight Punpun Vol. 1'), 3, 'Inner Voice',
'As Punpun grows, his inner voice becomes louder...'),

((select id from comics where title='Chainsaw Man Vol. 1'), 1, 'Chainsaw Dog',
'Denji lives in poverty, hunting devils to repay his debt...'),
((select id from comics where title='Chainsaw Man Vol. 1'), 2, 'A New Life',
'After betrayal and death, Denji becomes Chainsaw Man...'),
((select id from comics where title='Chainsaw Man Vol. 1'), 3, 'Devil Hunter',
'Denji joins the Public Safety Devil Hunters...'),

((select id from comics where title='Berserk Vol. 1'), 1, 'The Black Swordsman',
'Guts wanders a dark world...'),
((select id from comics where title='Berserk Vol. 1'), 2, 'Brand of Sacrifice',
'A mysterious mark attracts monsters...'),
((select id from comics where title='Berserk Vol. 1'), 3, 'Revenge',
'Driven by hatred and pain, Guts continues his journey...');


