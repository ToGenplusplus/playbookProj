INSERT INTO organizations (name,country,state,organization_type,organization_link,logo_image_location) VALUES
('University of Western Ontario','Canada','Ontario','College','www.uwo.ca','some.image.location.inS3');

INSERT INTO team (organization_id,name) VALUES (1,'Mens Football');

INSERT INTO coach (email,first_name,last_name,team_id) VALUES 
('Quisque.tincidunt.pede@sagittis.edu','Colby','Moses',1),
('urna.suscipit.nonummy@sitametorci.com','Caldwell','Valenzuela',1),
('mus@Donecsollicitudin.co.uk','Kenneth','Hudson',1),
('risus.Quisque@magnaUttincidunt.ca','Luke','Farmer',1),
('magna.Phasellus@risusDonecnibh.co.uk','Keane','Curtis',1);

INSERT INTO positions (position) VALUES 
('HC'),
('AHC'),
('OC'),
('AOC'),
('DC'),
('ADC'),
('OL'),
('QB'),
('RB'),
('FB'),
('REC'),
('TE'),
('DL'),
('LB'),
('DB'),
('K'),
('P'),
('ST');
('GEN');

INSERT INTO coach_positions (position_id,coach_id) VALUES 
('ST',1),
('QB',4),
('DB',5),
('RB',3),
('OL',2),
('OC',3),
('DL',1);

INSERT INTO players (student_number,email,first_name,jersey,last_name,team_id) VALUES 
('386246256','fringilla.Donec@pharetra.edu','Nissim','42','Hanson',1),
('562428070','Nulla.facilisi@Etiam.co.uk','Hayden','4','Bentley',1),
('703583295','in.magna.Phasellus@tristique.edu','Upton','9','Rivers',1),
('748696152','enim@lectus.co.uk','Colin','75','Talley',1),
('394801499','neque.et@elit.com','Emery','33','Humphrey',1),
('292023563','natoque@afeugiat.net','Chase','59','Woods',1),
('003722324','sed@nibhenimgravida.co.uk','Amal','67','Franks',1),
('402609636','vel.vulputate@cursus.net','Flynn','63','Warner',1),
('120718978','sit@disparturientmontes.ca','Valentine','86','Thomas',1),
('259445079','nec@quis.ca','Quinn','99','Kaufman',1),
('304939192','Quisque@faucibusMorbi.org','Grant','76','Douglas',1),
('019283778','turpis.non.enim@maurisipsumporta.ca','Dane','52','Baxter',1),
('794911849','Donec.consectetuer@Namac.co.uk','Ezra','1','Obrien',1),
('197123463','consequat.dolor.vitae@Maecenasmalesuada.co.uk','Kirk','16','Weaver',1),
('185479784','auctor@nonquam.org','Allen','11','Randolph',1),
('850873008','adipiscing.fringilla.porttitor@temporeratneque.co.uk','Cameron','8','Barker',1),
('741159390','Suspendisse@habitantmorbi.edu','Levi','7','Sharpe',1),
('062474263','arcu.ac.orci@lobortisnisinibh.org','Jackson','32','Craig',1),
('673582657','rutrum.eu.ultrices@semconsequat.ca','Oren','3','Macdonald',1);

INSERT INTO player_positions (position_id,player_id) VALUES 
('QB','673582657'),
('LB','386246256'),
('RB','562428070'),
('LB','703583295'),
('OL','748696152'),
('RB','394801499'),
('OL','292023563'),
('OL','003722324'),
('OL','402609636'),
('REC','120718978'),
('DL','259445079'),
('OL','304939192'),
('LB','019283778'),
('REC','794911849'),
('DB','197123463'),
('REC','185479784'),
('DB','850873008'),
('QB','741159390'),
('LB','062474263');

INSERT INTO quiz_questions (correct_answer,image_location,wrong_answer1,wrong_answer2,wrong_answer3,question,question_type,is_active,team_id) VALUES 
('elementum, dui quis accumsan convallis,','aliquam@AliquamnislNulla.org','laoreet ipsum. Curabitur','elit sed consequat auctor, nunc nulla vulputate dui, nec','lorem, vehicula','lectus sit amet luctus vulputate, nisi sem','GEN','1',1),
('semper auctor.','Morbi.quis.urna@maurisa.net','malesuada ut,','molestie tortor nibh sit amet','pede.','imperdiet dictum magna. Ut tincidunt orci quis','DL','1',1),
('Nulla aliquet. Proin velit. Sed','placerat.orci@eratin.net','aliquam eu,','Integer tincidunt aliquam arcu. Aliquam ultrices iaculis odio. Nam interdum enim non nisi. Aenean','consectetuer','eu, placerat eget, venenatis a, magna. Lorem','DB','1',1),
('elit erat','porttitor.scelerisque.neque@vitae.com','elementum, lorem','facilisis vitae, orci. Phasellus dapibus quam quis diam.','lobortis risus.','faucibus orci luctus et ultrices posuere cubilia','OL','1',1),
('nec, diam. Duis mi','senectus.et@Loremipsum.edu','dui,','Nam nulla magna, malesuada','metus.','sed leo. Cras vehicula aliquet libero. Integer','OL','1',1),
('dictum. Phasellus in felis. Nulla tempor','Nunc.quis.arcu@aliquetmagna.net','pellentesque','amet massa.','et','dolor vitae dolor. Donec fringilla. Donec feugiat','ST','1',1);

INSERT INTO player_answers (is_correct,answered_time,player_id,question_id) VALUES 
('1',7,'794911849',6),
('1',4,'304939192',4),
('1',20,'748696152',5),
('1',18,'259445079',2),
('0',17,'850873008',3),
('0',4,'386246256',1);
