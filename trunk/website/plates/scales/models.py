from django.db import models
from items.models import Item

import json

class Scale(models.Model):
  scale_id = models.CharField( max_length=32 )
  item = models.ForeignKey( Item, blank=True, null=True )
  quantity = models.FloatField( blank=True, null=True )

  def __str__( self ):
    return str( self.scale_id ) + " containing " + str ( self.item )

  def json( self ):
    if self.item == None:
      selfItem = None
    else:
      selfItem = self.item.json()

    return { 'scale_id' : self.scale_id, 'item' : selfItem, 'quantity' : self.quantity }

  def mass( self ):
    return ( self.quantity / 100.0 ) * self.item.mass

  mass = property( mass )
