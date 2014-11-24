from django.db import models
from items.models import Item

import math

import json

class Scale(models.Model):
  name = models.CharField( max_length=32 )
  scale_id = models.CharField( max_length=32 )
  item = models.ForeignKey( Item, blank=True, null=True, on_delete=models.SET_NULL )
  quantity = models.FloatField( blank=True, null=True )

  def __str__( self ):
    return str( self.scale_id ) + " containing " + str ( self.item )

  def json( self ):
    if self.item == None:
      selfItem = None
    else:
      selfItem = self.item.json()

    return { 'name': self.name, 'scale_id' : self.scale_id, 'item' : selfItem, 'quantity' : self.quantity }

  @property
  def image_name( self ):
    if not self.item or not self.item.image_type:
        return ""

    if self.quantity > self.item.mass * 0.85:
        fullness = "4"
    else:
        fullness = str( int( math.floor( float( self.quantity ) / self.item.mass * 4.0 ) ) )

    return self.item.image_type + fullness + ".png"

  def mass( self ):
    return ( self.quantity / 100.0 ) * self.item.mass

  mass = property( mass )
