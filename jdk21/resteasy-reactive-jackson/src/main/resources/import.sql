-- Insert 2 heroes
INSERT INTO characters(id, affiliation, name, heroRank) VALUES (1, 'hero', 'Quarkus Girl', 'S');
INSERT INTO characters(id, affiliation, name, heroRank, quirk) VALUES (2, 'hero', 'Captain Quarkus', 'A', 'Super shield');
-- Insert 2 villains
INSERT INTO characters(id, affiliation, name, bounty, quirk) VALUES (3, 'villain', 'Professor Trick', 150000, 'Body scatter');
INSERT INTO characters(id, affiliation, name, bounty) VALUES (4, 'villain', 'Doctor Quarkus', 5000000);

-- Used for testing
insert INTO jep431(id) VALUES (1);
