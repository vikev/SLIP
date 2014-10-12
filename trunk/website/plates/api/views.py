from django.shortcuts import render
from django.http import JsonResponse, HttpResponseNotFound, Http404

from scales.models import Scale
from items.models import Item

def scale( request, the_scale_id ):
  try:
    scale = Scale.objects.get( scale_id = the_scale_id )
  except Scale.DoesNotExist:
    raise Http404

  return JsonResponse( scale.json() )

def item( request, the_item_id ):
  try:
    item = Item.objects.get( item_id = the_item_id )
  except Item.DoesNotExist:
    raise Http404

  return JsonResponse( item.json() )

def all( request ):
  try:
    items = Item.objects.all()
  except Item.DoesNotExist:
    raise Http404

  response = [ item.json() for item in items ]
  return JsonResponse( response, safe=False )
