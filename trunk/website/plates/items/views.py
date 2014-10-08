from django.shortcuts import render

from items.models import Plate

def index( request ):
  plates = Plate.objects.all()
  context = { 'plates' : plates }
  return render( request, 'items/index.html', context )
