from django.db import models

class Item(models.Model):
  barcode = models.CharField( max_length=32 )
  mass = models.IntegerField( )

  def __str__( self ):
    return str( self.barcode )

class Plate(models.Model):
  plate_id = models.CharField( max_length=32 )
  item = models.ForeignKey( Item )
  reading = models.FloatField( blank=True, null=True )

  def __str__( self ):
    return str( self.plate_id ) + " containing " + str ( self.item )
