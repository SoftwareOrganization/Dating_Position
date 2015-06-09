# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Date',
            fields=[
                ('DateID', models.AutoField(serialize=False, primary_key=True)),
                ('DateName', models.CharField(max_length=15)),
                ('DateTime', models.DateTimeField()),
                ('Text', models.TextField()),
                ('Destination', models.CharField(max_length=15)),
            ],
            options={
            },
            bases=(models.Model,),
        ),
        migrations.CreateModel(
            name='Friends',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('UserID', models.IntegerField()),
                ('FriendID', models.IntegerField()),
            ],
            options={
            },
            bases=(models.Model,),
        ),
        migrations.CreateModel(
            name='Message',
            fields=[
                ('MessID', models.AutoField(serialize=False, primary_key=True)),
                ('FromID', models.IntegerField()),
                ('ToID', models.IntegerField()),
                ('Content', models.TextField()),
                ('isRead', models.CharField(max_length=1)),
                ('msgTime', models.DateTimeField()),
            ],
            options={
            },
            bases=(models.Model,),
        ),
        migrations.CreateModel(
            name='User',
            fields=[
                ('ID', models.AutoField(serialize=False, primary_key=True)),
                ('Username', models.CharField(max_length=15)),
                ('Password', models.CharField(max_length=15)),
                ('position', models.CharField(max_length=20)),
                ('sex', models.CharField(max_length=1)),
                ('avator', models.CharField(max_length=20)),
                ('online', models.IntegerField()),
            ],
            options={
            },
            bases=(models.Model,),
        ),
    ]
