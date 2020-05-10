Project 2

-------------PART 1-------------

DROP TABLE Payment          CASCADE;
DROP TABLE Booking          CASCADE;
DROP TABLE User_Account     CASCADE;
DROP TABLE Movie            CASCADE;
DROP TABLE Show             CASCADE;
DROP TABLE Show_Seating     CASCADE;
DROP TABLE Cinema           CASCADE;
DROP TABLE Cinema_Theater   CASCADE;
DROP TABLE Cinema_Seating   CASCADE;
DROP TABLE City             CASCADE;
DROP TABLE booked_by        CASCADE;
DROP TABLE played           CASCADE;
DROP TABLE has              CASCADE;

CREATE TABLE Payment (pay_ID       CHAR(11) NOT NULL,
                      trans_ID     CHAR(11) NOT NULL,
                      Amount       INTEGER,
                      pay_Method   CHAR(32) NOT NULL,
                      pay_DateTime CHAR(32) NOT NULL,
                      PRIMARY KEY(ssn_payID));

CREATE TABLE Booking (booking_ID    CHAR(11) NOT NULL,
                      status        CHAR(32) NOT NULL,
                      book_NumSeats INTEGER  NOT NULL,
                      book_DateTime CHAR(32) NOT NULL,
                      PRIMARY KEY(book_ID));

CREATE TABLE User_Account (email     CHAR(32) NOT NULL,
                           firstName CHAR(32) NOT NULL,
                           lastName  CHAR(32) NOT NULL,
                           phoneNum  INTEGER  NOT NULL,
                           password  CHAR(32) NOT NULL,
                           PRIMARY KEY(email);

CREATE TABLE Movie (movie_ID CHAR(11) NOT NULL,
                    title        CHAR(32) NOT NULL,
                    description  CHAR(128) NOT NULL,
                    genre        CHAR(32) NOT NULL,
                    country      CHAR(32) NOT NULL,
                    language     CHAR(32) NOT NULL,
                    duration     INTEGER  NOT NULL,
                    release_Date CHAR(32) NOT NULL,
                    PRIMARY KEY(movie_ID);

CREATE TABLE Show (show_ID    CHAR(11) NOT NULL,
                   date       CHAR(32) NOT NULL,
                   start_Time CHAR(11) NOT NULL,
                   end_Time   CHAR(11) NOT NULL,
                   PRIMARY KEY(show_ID);

CREATE TABLE Show_Seating (show_Seat_ID CHAR(11) NOT NULL,
                           price        CHAR(11) NOT NULL,  -- Double probably wouldn't fit
                           PRIMARY KEY(show_Seat_ID);

CREATE TABLE Cinema (cinema_ID      CHAR(11) NOT NULL,
                     cinema_Name    CHAR(32) NOT NULL,
                     total_Theaters INTEGER  NOT NULL,
                     PRIMARY KEY(cinema_ID);

CREATE TABLE Cinema_Theater (cinemaTheater_ID CHAR(11) NOT NULL,
                             total_NumSeats    INTEGER  NOT NULL,
                             theater_Name      CHAR(32) NOT NULL,
                             PRIMARY KEY(cinemaTheater_ID);

CREATE TABLE Cinema_Seating (cinemaSeat_ID CHAR(11) NOT NULL,
                             seatNum       INTEGER  NOT NULL,
                             type          CHAR(32) NOT NULL,
                             PRIMARY KEY(cinemaSeat_ID);

CREATE TABLE City (city_ID   CHAR(11) NOT NULL,
                   city_Name CHAR(32) NOT NULL,
                   zip_Code  INTEGER  NOT NULL,
                   state     CHAR(32) NOT NULL,
                   PRIMARY KEY(city_ID);

CREATE TABLE booked_by (ssn_grad CHAR(11) NOT NULL,         -- Not sure about these, EDIT LATER
			pno CHAR(11) NOT NULL,
			ssn_prof CHAR(11) NOT NULL,
			since INTEGER NOT NULL,
			PRIMARY KEY(ssn_grad, pno),
			FOREIGN KEY(ssn_grad) REFERENCES Graduate(ssn_grad),
			FOREIGN KEY(pno) REFERENCES Project(pno),
			FOREIGN KEY(ssn_prof) REFERENCES Professor(ssn_prof));

CREATE TABLE played (ssn_prof CHAR(11) NOT NULL,
			pno CHAR(11) NOT NULL,
			PRIMARY KEY(ssn_prof, pno),
			FOREIGN KEY(ssn_prof) REFERENCES Professor(ssn_prof),
			FOREIGN KEY(pno) REFERENCES Project(pno));

CREATE TABLE has (ssn_prof CHAR(11) NOT NULL,
			dno CHAR(11) NOT NULL,
			time_pc CHAR(32) NOT NULL,
			PRIMARY KEY(ssn_prof, dno),
			FOREIGN KEY(ssn_prof) REFERENCES Professor(ssn_prof),
			FOREIGN KEY(dno) REFERENCES Dept(dno));
