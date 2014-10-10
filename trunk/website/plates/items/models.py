from django.db import models

class Item(models.Model):
  item_id = models.CharField( max_length=64, blank=True, null=True )
  mass = models.IntegerField( )

  def __str__( self ):
    return str( self.item_id )
