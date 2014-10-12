from django.conf.urls import patterns, url

from api import views

urlpatterns = patterns('',
    url(r'^scale/(?P<the_scale_id>.+)/$', views.scale , name='api_scale'),
    url(r'^all/$', views.all, name='api_all'),
    url(r'^item/(?P<the_item_id>.+)/$', views.item, name='api_item')
)
