# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('items', '0002_auto_20141007_1816'),
    ]

    operations = [
        migrations.AlterField(
            model_name='plate',
            name='reading',
            field=models.FloatField(null=True, blank=True),
        ),
    ]
