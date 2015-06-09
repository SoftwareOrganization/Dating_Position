# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('yonghu', '0002_auto_20150602_2236'),
    ]

    operations = [
        migrations.AlterField(
            model_name='date',
            name='DateTime',
            field=models.CharField(max_length=30),
            preserve_default=True,
        ),
        migrations.AlterField(
            model_name='message',
            name='msgTime',
            field=models.CharField(max_length=30),
            preserve_default=True,
        ),
    ]
