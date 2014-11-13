from django.shortcuts import render
from django.http import JsonResponse, HttpResponseNotFound, Http404, HttpResponseRedirect

from scales.models import Scale
from items.models import *

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
    scales = Scale.objects.all()
  except Item.DoesNotExist:
    raise Http404

  response = { "scales" : [ scale.json() for scale in scales ] }
  return JsonResponse( response, safe=False )

def items( request ):
  items = Item.objects.all()
  response = { "items" : [ item.json() for item in items ] }
  return JsonResponse( response, safe=False )

def allmac( request ):
  scales = Scale.objects.all()
  result = { 'addresses': [] }

  for scale in scales:
    result['addresses'].append( scale.scale_id )

  return JsonResponse( result )


def mac( request, the_mac_address ):
  reading = request.GET.get( 'reading', '' )

  if the_mac_address:
    try:
      scale = Scale.objects.get( scale_id = the_mac_address )
    except Scale.DoesNotExist:
      scale = Scale( scale_id = the_mac_address, item = None, quantity = reading )
      scale.save()
    else:
      scale.quantity = reading
      scale.save()

    return HttpResponseRedirect( '/scales/')
  else:
    return Http404()

# for the android app to change the item on the plate (adds a new item)
def set_item( request ):
  scale_id = request.GET.get( 'scale_id', '' )
  item_name = request.GET.get( 'item_name', '' )
  item_mass = request.GET.get( 'item_mass', '' )

  if scale_id and item_name and item_mass:
    scale = Scale.objects.get( scale_id = scale_id )
    scale.quantity = 0.2
    item = Item.objects.create( item_id = generate_item_id( item_name ), name = item_name, mass = item_mass )
    scale.item = item
    item.save()
    scale.save()
    return HttpResponseRedirect( '/scales/' )
  else:
    return Http404()


def put( request ):
  barcode = request.GET.get( 'barcode', '' )
  scale_id = request.GET.get( 'scale_id', '' )
  item_mass = request.GET.get( 'mass', '' )

  if barcode and scale_id and item_mass:
    product = product_from_barcode( barcode )
    scale = Scale.objects.get( scale_id=scale_id )
    if product != None:
      item = Item.objects.create( name=product['itemname'], item_id=barcode, mass=item_mass )
      scale.item = item
      scale.save()
      item.save()
    else:
      return Http404

  return HttpResponseRedirect( '/items/' )
