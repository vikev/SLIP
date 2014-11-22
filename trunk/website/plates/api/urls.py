from django.conf.urls import patterns, url

from api import views

urlpatterns = patterns('',
    url(r'^scale/(?P<the_scale_id>.+)/$', views.scale , name='api_scale'), # return the item and quantity on the scale
    url(r'^mac/(?P<the_mac_address>.+)/', views.mac, name='mac_address'),
    url(r'^allmac/$', views.allmac, name='all_mac_addresses'),
    url(r'^all/$', views.all, name='api_all'), # return all scales and their items
    url(r'^item/(?P<the_item_id>.+)/$', views.item, name='api_item'), # get item from id (potentially unneeded)
    url(r'^items/', views.items, name='all_items'), # return all items in the database
    url(r'^put/', views.put, name='api_put'), # put a new item to a scale using the product barcode
    url(r'^set_item/', views.set_item, name='set_item'), # set the item on the scale. If the item doesn't exist, create it
    url(r'^add_scale/', views.add_scale, name='add_scale')
)
