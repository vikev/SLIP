import json
import urllib2

API_KEY = "57781297ec7e5ae0a3f2684d762ac12a" # for upc database

API_URL = "http://api.upcdatabase.org/json/" + API_KEY + "/"

def product_from_barcode( barcode ):
  response = urllib2.urlopen( API_URL + barcode )
  if response.getcode() == 200:
    return json.load( response )

  return None
