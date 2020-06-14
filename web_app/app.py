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

