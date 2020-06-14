from flask import Flask, render_template, redirect, url_for, flash, request
from flask_sqlalchemy import SQLAlchemy
from flask_migrate import Migrate
from flask_bootstrap import Bootstrap

app = Flask(__name__)

app.config['SQLALCHEMY_DATABASE_URI'] = 'postgresql+psycopg2://vagrant:@127.0.0.1:9998/vagrant_DB'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
app.config['SECRET_KEY'] = 'hard'

db = SQLAlchemy(app)

bootstrap = Bootstrap(app)

@app.route('/')
def index(): 
    return render_template('index.html')

@app.errorhandler(404)
def page_not_found(e): 
    return render_template('404.html'), 404

@app.errorhandler(505)
def page_not_found(e): 
    return render_template('505.html'), 505

# Adding a new user 
@app.route('/new_user', methods=['GET', 'POST'])
def add_user(): 
    from models import user
    if request.method == 'POST': 
        if not request.form['email'] or not request.form['fname'] or not request.form['lname'] or not request.form['phone'] or request.form['password']: 
            flash('Please enter all the information')
        elif(user.query.filter_by(email=request.form['email'].all() != []): 
                flash('A user account has already been created with this email! Please try again.')
                return redirect(url_for('add_user'))
        else: 
            phone = "(" + request.form['phone'][0:3] + ")" + request.form['phone'][3:6] + "-" + request.form['phone'][6:10]
            new_user = user(email=request.form['email'], fname=request.form['fname'], lname=request.form['lname'], phone=phone, password=request.form['password'])
            db.session.add(new_user)
            db.session.commit()
            flash('Your account has successfully been created!')
            return redirect(url_for('index'))
    
    return render_template('new_user.html')

# Adding booking 

