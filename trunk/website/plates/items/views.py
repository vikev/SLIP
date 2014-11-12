from django.shortcuts import render
from django.http import HttpResponseRedirect
from django import forms

from items.models import Item

def index( request ):
  delete = request.GET.get( 'delete', '' )
  if delete:
    Item.objects.filter( item_id = delete ).delete()
    return HttpResponseRedirect( '/items' )

  items = Item.objects.all()
  context = { 'items' : items }
  return render( request, 'items/index.html', context )

def item( request, the_item_id ):
  item = Item.objects.get( item_id=the_item_id )
  context = { 'item' : item }
  return render( request, 'items/item.html', context )

def add( request ):
  name = request.GET.get( 'name', '' )
  mass = request.GET.get( 'mass', '' )

  if name and mass:
    item = Item()
    item.name = name
    item.mass = mass
    item.item_id = len( Item.objects.all() )
    item.save()
    return HttpResponseRedirect( '/items/' )

  item = Item.objects.all()
  return render( request, 'items/add.html' )
