from django.conf.urls import patterns, include, url
from django.contrib import admin

urlpatterns = patterns('',
    # Examples:
    # url(r'^$', 'plates.views.home', name='home'),
    # url(r'^blog/', include('blog.urls')),

    url(r'^items/', include('items.urls') ),
    url(r'^admin/', include(admin.site.urls)),
)
