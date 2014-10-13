from django.conf.urls import patterns, url

from items import views

urlpatterns = patterns('',
    url(r'^$', views.index, name='index'),
    url(r'^add/$', views.add, name='add'),
    url(r'^(?P<the_item_id>.+)/$', views.item, name='item')
)
