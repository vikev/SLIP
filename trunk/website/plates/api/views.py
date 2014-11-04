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
