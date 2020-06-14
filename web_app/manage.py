from app import db

class cities(db.Model): 
	__tablename__ = 'cities'
	city_id = db.Column(db.BIGINT, nullable=False, primary_key=True)
    city_name = db.Column(db.VARCHAR(64), nullable=False, unique=True)
    city_state = db.Column(db.CHAR(2), nullable=False, unique=True)
    zip_code = db.Column(db.NUMERIC(5), nullable=False, unique=True) 
    #db.UNIQUE(city_state, city_name) # possibly edit this line? 
    #db.UNIQUE(zip_code) # possibly edit this line? 

class cinemas(db.Model):
	__tablename__ = 'cinemas'
	cid = db.Column(db.BIGINT, nullable=False, primary_key=True)
    city_id = db.Column(db.BIGINT, nullable=False, db.ForeignKey('city_id'), unique=True)
    cname = db.Column(db.VARCHAR(64), nullable=False, unique=True) # Cinema name
    tnum = db.Column(db.Integer, nullable=False) # Number of theaters
    #UNIQUE(city_id, cname)

class theaters(db.Model):
	__tablename__ = 'theaters'
    tid = db.Column(db.BIGINT, nullable=False, primary_key=True) # Theater ID
    cid = db.Column(db.BIGINT, nullable=False, db.ForeignKey('cid'), unique=True) # Cinema ID
    tname = db.Column(db.VARCHAR(64), nullable=False, unique=True) # Theater name
    tseats = db.Column(db.BIGINT, nullable=False) # Number of seats in the theater

class cinema_seats (db.Model): 
	__tablename__ = 'cinema_seats'
    csid = db.Column(db.BIGINT, nullable=False, primary_key=True) # Cinema seat ID
    tid = db.Column(db.BIGINT, nullable=False, db.ForeignKey('tid'), unique=True)  #-- Theater ID
    sno = db.Column(db.INTEGER,  nullable=False, unique=True) # Seat number in the theater
    stype = db.Column(db.VARCHAR(16), nullable = False) # Seat type

class movies (db.Model): 
	__tablename__ = 'movies'
    mvid = db.Column(db.BIGINT, nullable=False, primary_key=True) # Movie ID
    title = db.Column(db.VARCHAR(128), nullable=False) # Movie title
    rdate = db.Column(db.DATE, nullable=False)  # Release date
    country = db.Column(db.VARCHAR(64), nullable=False)  # Release country
    description = db.Column(db.TEXT, nullable=False)
    duration = db.Column(db.INTEGER)  # In seconds
    lang = db.Column(db.CHAR(2))  # Language code, such as en, de
    genre = db.Column(db.VARCHAR(16))

class user(db.Model): 
    __tablename__ = 'user'
	email = db.Column(db.VARCHAR(64), primary_key=True, nullable=False)
    lname = db.Column(db.VARCHAR(32), nullable=False)  # Last name
    fname = db.Column(db.VARCHAR(32), nullable=False)   # First name
    phone = db.Column(db.Integer)  
    pwd = db.Column(db.TEXT, nullable=False) # Password (should be hash instead of plain text)

class shows (db.Model): 
	__tablename__ = 'shows'
    sid = db.Column(db.BIGINT, nullable=False, primary_key=True)  # Show ID
    mvid = db.Column(db.BIGINT, nullable=False, db.ForeignKey('mvid')) # Movie ID
    sdate = db.Column(db.DATE, nullable=False)  # Show date
    sttime = db.Column(db.TIME, nullable=False) # Start time
    edtime = db.Column(db.TIME, nullable=False)   # End time

### EDIT THE DATABASE ENTRIES BEYOND THIS POINT TO REFLECT FORMATTING ABOVE LATER! 

class bookings (db.Model): 
	__tablename__ = 'bookings'
    bid BIGINT NOT NULL,  -- Booking ID
    status VARCHAR(16) NOT NULL,
    bdatetime TIMESTAMPTZ NOT NULL,  -- Booking date and time
    seats INTEGER NOT NULL,  -- Number of seats booked
    sid BIGINT NOT NULL,  -- Show ID
    email VARCHAR(64) NOT NULL,  -- User account
    PRIMARY KEY(bid)
    FOREIGN KEY(sid) REFERENCES Shows(sid),
    FOREIGN KEY(email) REFERENCES Users(email)    
);


CREATE TABLE Payments (
	__tablename__ = 'payments'
    pid BIGINT NOT NULL,  -- Payment ID
    bid BIGINT NOT NULL,  -- Booking ID
    pmethod VARCHAR(32) NOT NULL,
    pdatetime TIMESTAMPTZ NOT NULL,  -- Payment date and time
    amount REAL NOT NULL,
    trid BIGINT,  -- Transaction ID
    PRIMARY KEY(pid),
    FOREIGN KEY(bid) REFERENCES Bookings(bid),
    UNIQUE(bid)  -- No two payments can have the same booking
);


CREATE TABLE ShowSeats (
	__tablename__ = 'show_seats'
    ssid BIGINT NOT NULL,  -- Show seat ID
    sid BIGINT NOT NULL,  -- Show ID
    csid BIGINT NOT NULL, -- Cinema seat ID
    bid BIGINT, -- Booking ID
    price REAL NOT NULL,
    PRIMARY KEY(ssid),
    FOREIGN KEY(sid) REFERENCES Shows(sid),
    FOREIGN KEY(csid) REFERENCES CinemaSeats(csid),
    FOREIGN KEY(bid) REFERENCES Bookings(bid),
    UNIQUE(sid, csid)  -- The same seat can only be booked once for the same show

);


CREATE TABLE Plays (
	__tablename__ = 'plays'
    sid BIGINT NOT NULL,  -- Show ID
    tid BIGINT NOT NULL,  -- Theater ID
    PRIMARY KEY(sid, tid),
    FOREIGN KEY(sid) REFERENCES Shows(sid),
    FOREIGN KEY(tid) REFERENCES Theaters(tid)
)