# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('yonghu', '0001_initial'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='user',
            name='avator',
        ),
        migrations.AddField(
            model_name='date',
            name='Member1ID',
            field=models.IntegerField(default=0),
            preserve_default=False,
        ),
        migrations.AddField(
            model_name='date',
            name='Member2ID',
            field=models.IntegerField(default=0),
            preserve_default=False,
        ),
        migrations.AddField(
            model_name='date',
            name='Member3ID',
            field=models.IntegerField(default=0),
            preserve_default=False,
        ),
        migrations.AddField(
            model_name='date',
            name='Member4ID',
            field=models.IntegerField(default=0),
            preserve_default=False,
        ),
        migrations.AddField(
            model_name='date',
            name='Member5ID',
            field=models.IntegerField(default=0),
            preserve_default=False,
        ),
        migrations.AddField(
            model_name='date',
            name='UserID',
            field=models.IntegerField(default=0),
            preserve_default=False,
        ),
        migrations.AddField(
            model_name='user',
            name='newfriend',
            field=models.IntegerField(default=0),
            preserve_default=True,
        ),
    ]
