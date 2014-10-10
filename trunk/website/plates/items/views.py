from django.shortcuts import render

from items.models import Item

def index( request ):
  items = Item.objects.all()
  context = { 'items' : items }
  return render( request, 'items/index.html', context )

def item( request, the_item_id ):
  item = Item.objects.get( item_id=the_item_id )
  context = { 'item' : item }
  return render( request, 'items/item.html', context )
