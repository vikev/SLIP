from django.db import models
from items.models import Item

import json

class Scale(models.Model):
  scale_id = models.CharField( max_length=32 )
  item = models.ForeignKey( Item )
  reading = models.FloatField( blank=True, null=True )

  def __str__( self ):
    return str( self.scale_id ) + " containing " + str ( self.item )

  def json( self ):
    return { 'scale_id' : scale_id, 'item' : item.json(), 'reading' : reading }
