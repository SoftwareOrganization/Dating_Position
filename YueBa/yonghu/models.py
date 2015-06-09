from django.db import models

# Create your models here.
class User(models.Model):
    ID = models.AutoField(primary_key=True)
    Username = models.CharField(max_length=15)
    Password = models.CharField(max_length=15)
    position = models.CharField(max_length=20)
    sex = models.CharField(max_length=1)
    #avator = models.CharField(max_length=20)#touxiang
    online = models.IntegerField()
    newfriend = models.IntegerField(default=0)



class Friends(models.Model):
    UserID = models.IntegerField()
    FriendID = models.IntegerField()

class Message(models.Model):
    MessID = models.AutoField(primary_key=True)
    FromID = models.IntegerField()
    ToID = models.IntegerField()
    Content = models.TextField()
    isRead = models.CharField(max_length=1)
    msgTime = models.CharField(max_length=30)

class Date(models.Model):
    DateID = models.AutoField(primary_key=True)
    UserID = models.IntegerField()
    DateName = models.CharField(max_length=15)
    DateTime = models.CharField(max_length=30)
    Text = models.TextField()
    Destination = models.CharField(max_length=15)
    Member1ID =models.IntegerField()
    Member2ID =models.IntegerField()
    Member3ID =models.IntegerField()
    Member4ID =models.IntegerField()
    Member5ID =models.IntegerField()