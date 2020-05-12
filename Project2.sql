---CS 166 Phase 2---


DROP TABLE User_Account     CASCADE;
DROP TABLE Movie            CASCADE;
DROP TABLE Show             CASCADE;
DROP TABLE Booking          CASCADE;
DROP TABLE Payment          CASCADE;
DROP TABLE Cinema_Theater   CASCADE;
DROP TABLE Cinema_Seating   CASCADE;
DROP TABLE Show_Seating     CASCADE;
DROP TABLE City             CASCADE;
DROP TABLE Cinema           CASCADE;
DROP TABLE Seat_Booked      CASCADE;
DROP TABLE Played_In        CASCADE;


CREATE TABLE User_Account (email     CHAR(32) NOT NULL,
                           firstName CHAR(32) NOT NULL,
                           lastName  CHAR(32) NOT NULL,
                           phoneNum  CHAR(32) NOT NULL,
                           password  CHAR(32) NOT NULL,
                           PRIMARY KEY(email));

CREATE TABLE Movie (movie_ID     CHAR(11) NOT NULL,
                    title        CHAR(32) NOT NULL,
                    description  CHAR(128) NOT NULL,
                    genre        CHAR(32) NOT NULL,
                    country      CHAR(32) NOT NULL,
                    language     CHAR(32) NOT NULL,
                    duration     INTEGER  NOT NULL,
                    release_Date CHAR(32) NOT NULL,
                    PRIMARY KEY(movie_ID));

CREATE TABLE Show (show_ID    CHAR(11) NOT NULL,
                   date       CHAR(32) NOT NULL,
                   start_Time CHAR(11) NOT NULL,
                   end_Time   CHAR(11) NOT NULL,
                   PRIMARY KEY(show_ID),
                   FOREIGN KEY(movie_ID) REFERENCES Movie(movie_ID));

CREATE TABLE Booking (booking_ID    CHAR(11) NOT NULL,
                      status        CHAR(32) NOT NULL,
                      book_NumSeats INTEGER  NOT NULL,
                      book_DateTime CHAR(32) NOT NULL,
                      PRIMARY KEY(book_ID),
                      FOREIGN KEY(email)   REFERENCES User_Account(email),
                      FOREIGN KEY(show_ID) REFERENCES Show(show_ID));

CREATE TABLE Payment (pay_ID       CHAR(11) NOT NULL,
                      trans_ID     CHAR(11) NOT NULL,
                      Amount       INTEGER,
                      pay_Method   CHAR(32) NOT NULL,
                      pay_DateTime CHAR(32) NOT NULL,
                      PRIMARY KEY(ssn_payID),
                      FOREIGN KEY(booking_ID) REFERENCES Booking(booking_ID));

CREATE TABLE Cinema_Theater (cinemaTheater_ID  CHAR(11) NOT NULL,
                             total_NumSeats    INTEGER  NOT NULL,
                             theater_Name      CHAR(32) NOT NULL,
                             PRIMARY KEY(cinemaTheater_ID));

CREATE TABLE Cinema_Seating (cinemaSeat_ID CHAR(11) NOT NULL,
                             seatNum       INTEGER  NOT NULL,
                             type          CHAR(32) NOT NULL,
                             PRIMARY KEY(cinemaSeat_ID));

CREATE TABLE Show_Seating (show_Seat_ID CHAR(11) NOT NULL,
                           price        CHAR(11) NOT NULL,  -- Double probably wouldn't fit
                           PRIMARY KEY(show_Seat_ID),
                           FOREIGN KEY(show_ID)       REFERENCES Show(show_ID),
                           FOREIGN KEY(cinemaSeat_ID) REFERENCES Cinema_Seating(cinemaSeat_ID));

CREATE TABLE City (city_ID   CHAR(11) NOT NULL,
                   city_Name CHAR(32) NOT NULL,
                   zip_Code  INTEGER  NOT NULL,
                   state     CHAR(32) NOT NULL,
                   PRIMARY KEY(city_ID));

CREATE TABLE Cinema (cinema_ID      CHAR(11) NOT NULL,
                     cinema_Name    CHAR(32) NOT NULL,
                     total_Theaters INTEGER  NOT NULL,
                     PRIMARY KEY(cinema_ID),
                     FOREIGN KEY(city_ID) REFERENCES City(city_ID));

CREATE TABLE Seat_Booked (show_Seat_ID CHAR(11) NOT NULL,
                          booking_ID   CHAR(11) NOT NULL, 
                          PRIMARY KEY(show_Seat_ID, booking_ID),
                          FOREIGN KEY(show_Seat_ID) REFERENCES Show_Seating(show_Seat_ID),
                          FOREIGN KEY(booking_ID)   REFERENCES Booking(booking_ID));

CREATE TABLE Played_In (show_ID          CHAR(11) NOT NULL,
                        cinemaTheater_ID CHAR(11) NOT NULL,
                        PRIMARY KEY(show_ID, cinemaTheater_ID),
                        FOREIGN KEY(show_ID)          REFERENCES Show(show_ID),
                        FOREIGN KEY(cinemaTheater_ID) REFERENCES Cinema_Theater(cinemaTheater_ID));
