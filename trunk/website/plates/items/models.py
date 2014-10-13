from django.db import models

import json

class Item(models.Model):
  item_id = models.CharField( max_length=64, blank=True, null=True )
  name = models.CharField( max_length=64, blank=True, null=True )
  mass = models.IntegerField( )

  def __str__( self ):
    return str( self.item_id )

  def json( self ):
    return { 'itemId' : self.item_id, 'name' : self.name, 'mass' : self.mass }
