from django.shortcuts import render

from scales.models import Scale

def index( request ):
  scales = Scale.objects.all()

  context = { 'scales' : scales }
  return render( request, 'scales/index.html', context )
