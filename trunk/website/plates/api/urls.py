from django.conf.urls import patterns, url

from api import views

urlpatterns = patterns('',
    url(r'^scale/(?P<the_scale_id>.+)/$', views.scale , name='api_scale'),
    url(r'^mac/(?P<the_mac_address>.+)/', views.mac, name='mac_address'),
    url(r'^allmac/$', views.allmac, name='all_mac_addresses'),
    url(r'^all/$', views.all, name='api_all'),
    url(r'^item/(?P<the_item_id>.+)/$', views.item, name='api_item'),
    url(r'^items/', views.items, name='all_items'),
    url(r'^put/', views.put, name='api_put'),
    url(r'^set_item/', views.set_item, name='set_item')
)
