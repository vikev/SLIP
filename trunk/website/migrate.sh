#!/bin/bash
cd plates &&
python manage.py makemigrations &&
python manage.py migrate &&
python manage.py createsuperuser
