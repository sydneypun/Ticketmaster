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
    phone = db.Column(db.INTEGER)  
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
    bid = db.Column(db.BIGINT, nullable=False, primary_key=True) # Booking ID
    status = db.Column(db.VARCHAR(16), nullable=False)
    bdatetime = db.Column(db.TIMESTAMPTZ, nullable=False) # Booking date and time
    seats = db.Column(db.INTEGER, nullable=False)  # Number of seats booked
    sid = db.Column(db.BIGINT, nullable-False, db.ForeignKey('sid'))  # Show ID
    email = db.Column(db.VARCHAR(64), nullable=False, db.ForeignKey('email')) # User account

class payments (db.Model): 
	__tablename__ = 'payments'
    pid = db.Column(db.BIGINT, nullable=False, primary_key=True)   # Payment ID
    bid = db.Column(db.BIGINT, nullable=False, db.ForeignKey('bid'), unique=True)   # Booking ID
    pmethod = db.Column(db.VARCHAR(32), nullable=False) 
    pdatetime = db.Column(db.TIMESTAMPTZ, nullable=False) # Payment date and time
    amount = db.Column(db.REAL, nullable=False)
    trid = db.Column(db.BIGINT)  # Transaction ID

class show_seats (db.Model): 
	__tablename__ = 'show_seats'
    ssid = db.Column(db.BIGINT, nullable=False, primary_key=True) # Show seat ID
    sid = db.Column(db.BIGINT, nullable=False, db.ForeignKey('sid'), unique=True)  # Show ID
    csid = db.Column(db.BIGINT, nullable=False, db.ForeignKey('csid'), unique=True) # Cinema seat ID
    bid = db.Column(db.BIGINT, nullable=False, db.ForeignKey('bid'))  # Booking ID
    price = db.Column(db.REAL, nullable=False) 

class plays (db.Model):
	__tablename__ = 'plays'
    sid = db.Column(db.BIGINT, nullable=False, primary_key=True, db.ForeignKey('sid'))  # Show ID
    tid = db.Column(db.BIGINT, nullable=False, primary_key=True, db.ForeignKey('tid'))  # Theater ID