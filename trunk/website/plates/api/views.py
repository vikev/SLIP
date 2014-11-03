from django.shortcuts import render
from django.http import JsonResponse, HttpResponseNotFound, Http404, HttpResponseRedirect

from scales.models import Scale
from items.models import Item

from barcode.barcode import product_from_barcode

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
    scales = Scale.objects.all()
  except Item.DoesNotExist:
    raise Http404

  response = { "scales" : [ scale.json() for scale in scales ] }
  return JsonResponse( response, safe=False )

def put( request ):
  barcode = request.GET.get( 'barcode', '' )

  if barcode:
    product = product_from_barcode( barcode )
    if product != None:
      item = Item( name=product['itemname'], item_id=barcode, mass=barcode )
      item.save()
    else:
      return Http404

  return HttpResponseRedirect( '/items/' )
