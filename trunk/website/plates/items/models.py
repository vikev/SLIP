from django.db import models
from hashlib import md5

import json

class Item(models.Model):
  item_id = models.CharField( max_length=64, blank=True, null=True )
  name = models.CharField( max_length=64, blank=True, null=True )
  mass = models.IntegerField( )
  barcode = models.CharField(max_length=64, blank=True, null=True )

  image_type = models.CharField( max_length=64, blank=True, null=True )
  app_image_type = models.CharField( max_length=64, blank=True, null=True )

  def __str__( self ):
    return str( self.item_id )

  def json( self ):
    return { 'barcode': self.barcode, 'item_id' : self.item_id, 'name' : self.name, 'mass' : self.mass, 'image_type': self.image_type, 'app_image_type': self.app_image_type }


def generate_item_id( item_name ):
  return md5( item_name ).hexdigest()
