from flask_sqlalchemy import SQLAlchemy

db = SQLAlchemy()

class cities(db.Model): 
	__tablename__ = 'cities'
	city_id = db.Column(db.BIGINT, nullable=False, primary_key=True)
	city_name = db.Column(db.VARCHAR(64), nullable=False)
	city_state = db.Column(db.CHAR(2), nullable=False, unique=True)
	zip_code = db.Column(db.NUMERIC(5), nullable=False, unique=True) 

class cinemas(db.Model):
	__tablename__ = 'cinemas'
	cid = db.Column(db.BIGINT, nullable=False, primary_key=True)
	city_id = db.Column(db.BIGINT, db.ForeignKey('city_id'), nullable=False)
	cname = db.Column(db.VARCHAR(64), nullable=False) # Cinema name
	tnum = db.Column(db.Integer, nullable=False) # Number of theaters

class theaters(db.Model):
	__tablename__ = 'theaters'
	tid = db.Column(db.BIGINT, nullable=False, primary_key=True) # Theater ID
	cid = db.Column(db.BIGINT, db.ForeignKey('cid'), nullable=False) # Cinema ID
	tname = db.Column(db.VARCHAR(64), nullable=False) # Theater name
	tseats = db.Column(db.BIGINT, nullable=False) # Number of seats in the theater

class cinema_seats (db.Model): 
	__tablename__ = 'cinema_seats'
	csid = db.Column(db.BIGINT, nullable=False, primary_key=True) # Cinema seat ID
	tid = db.Column(db.BIGINT, db.ForeignKey('tid'), nullable=False)  #-- Theater ID
	sno = db.Column(db.INTEGER,  nullable=False) # Seat number in the theater
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
	mvid = db.Column(db.BIGINT, db.ForeignKey('mvid'), nullable=False) # Movie ID
	sdate = db.Column(db.DATE, nullable=False)  # Show date
	sttime = db.Column(db.TIME, nullable=False) # Start time
	edtime = db.Column(db.TIME, nullable=False)   # End time

class bookings (db.Model): 
	__tablename__ = 'bookings'
	bid = db.Column(db.BIGINT, nullable=False, primary_key=True) # Booking ID
	status = db.Column(db.VARCHAR(16), nullable=False)
	bdatetime = db.Column(db.TIMESTAMP, nullable=False) # Booking date and time
	seats = db.Column(db.INTEGER, nullable=False)  # Number of seats booked
	sid = db.Column(db.BIGINT,db.ForeignKey('sid'), nullable=False)  # Show ID
	email = db.Column(db.VARCHAR(64),  db.ForeignKey('email'), nullable=False) # User account

class payments (db.Model): 
	__tablename__ = 'payments'
	pid = db.Column(db.BIGINT, nullable=False, primary_key=True)   # Payment ID
	bid = db.Column(db.BIGINT, db.ForeignKey('bid'), nullable=False)   # Booking ID
	pmethod = db.Column(db.VARCHAR(32), nullable=False) 
	pdatetime = db.Column(db.TIMESTAMP, nullable=False) # Payment date and time
	amount = db.Column(db.REAL, nullable=False)
	trid = db.Column(db.BIGINT)  # Transaction ID

class show_seats (db.Model): 
	__tablename__ = 'show_seats'
	ssid = db.Column(db.BIGINT, nullable=False, primary_key=True) # Show seat ID
	sid = db.Column(db.BIGINT, db.ForeignKey('sid'), nullable=False)  # Show ID
	csid = db.Column(db.BIGINT, db.ForeignKey('csid'), nullable=False) # Cinema seat ID
	bid = db.Column(db.BIGINT, db.ForeignKey('bid'), nullable=False)  # Booking ID
	price = db.Column(db.REAL, nullable=False) 

class plays (db.Model):
	__tablename__ = 'plays'
	sid = db.Column(db.BIGINT, db.ForeignKey('sid'), nullable=False, primary_key=True)  # Show ID
	tid = db.Column(db.BIGINT, db.ForeignKey('tid'), nullable=False, primary_key=True)  # Theater ID
